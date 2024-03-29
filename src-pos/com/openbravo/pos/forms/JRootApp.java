//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.forms;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.swing.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.openbravo.pos.printer.*;
import com.openbravo.beans.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.loader.BatchSentence;
import com.openbravo.data.loader.BatchSentenceResource;
import com.openbravo.data.loader.Session;
import com.openbravo.license.DeviceInfo;
import com.openbravo.license.JLicenseDialog;
import com.openbravo.license.LicenseManager;
import com.openbravo.pos.scale.DeviceScale;
import com.openbravo.pos.scanpal2.DeviceScanner;
import com.openbravo.pos.scanpal2.DeviceScannerFactory;
import com.openbravo.pos.util.Log;

import at.w4cash.signature.SignatureModul;

import java.util.Locale;
import java.util.regex.Matcher;

/**
 *
 * @author adrianromero
 */
public class JRootApp extends JPanel implements AppView {

	private AppProperties m_props;
	private Session session;
	private DataLogicSystem m_dlSystem;

	private Properties m_propsdb = null;
	private Properties posprops;
	private String m_sActiveCashIndex;
	private int m_iActiveCashSequence;
	private Date m_dActiveCashDateStart;
	private Date m_dActiveCashDateEnd;

	private String m_sInventoryLocation;

	private StringBuffer inputtext;

	private DeviceScale m_Scale;
	private DeviceScanner m_Scanner;
	private DeviceTicket m_TP;
	private TicketParser m_TTP;

	private Map<String, BeanFactory> m_aBeanFactories;

	private JPrincipalApp m_principalapp = null;

	private static HashMap<String, String> m_oldclasses; // This is for
															// backwards
															// compatibility
															// purposes

	static {
		initOldClasses();
	}

	/** Creates new form JRootApp */
	public JRootApp() {

		m_aBeanFactories = new HashMap<String, BeanFactory>();

		// Inicializo los componentes visuales
		initComponents();
		jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
	}

	public boolean initApp(AppProperties props, Boolean noGUI) {

		m_props = props;
		// setPreferredSize(new java.awt.Dimension(800, 600));

		// support for different component orientation languages.
		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

		// Database start
		// we have three connection requests, before we say no connection
		// possible
		try {
			session = AppViewConnection.createSession(m_props);
		} catch (BasicException e) {
			if (!noGUI)
				JMessageDialog.showMessage(this, this, new MessageInf(MessageInf.SGN_DANGER, e.getMessage(), e));

			// try to close session after a invalid connection!
			if (session != null) {
				session.close();
			}

			return false;
		}

		Log.Init(session, getWindowsHost() + ": " + m_props.getHost());

		m_dlSystem = (DataLogicSystem) getBean("com.openbravo.pos.forms.DataLogicSystem");

		// Create or upgrade the database if database version is not the
		// expected
		String sDBVersion;
		Boolean bQuestionOK = false;
		do {
			sDBVersion = readDataBaseVersion();

			if (!AppLocal.APP_VERSION.equals(sDBVersion)) {

				// Create or upgrade database
				String sScript = sDBVersion.equals("create") ? m_dlSystem.getInitScript() + "-create.sql"
						: m_dlSystem.getInitScript() + "-upgrade-" + sDBVersion + ".sql";

				/*
				 * only for testing !!! String sScript = sDBVersion.equals("create") ?
				 * m_dlSystem.getInitScript() + "-create-2.30.2.sql" :
				 * m_dlSystem.getInitScript() + "-upgrade-" + sDBVersion + ".sql";
				 */

				if (JRootApp.class.getResource(sScript) == null) {
					if (!noGUI) {
						JMessageDialog.showMessage(this, this, new MessageInf(MessageInf.SGN_DANGER,
								sDBVersion == null
										? AppLocal.getIntString("message.databasenotsupported", session.DB.getName())
										: AppLocal.getIntString("message.noupdatescript")));
					}
					session.close();
					return false;
				} else {

					// Create or upgrade script exists.
					if (!noGUI && (bQuestionOK || JOptionPane.showConfirmDialog(this,
							AppLocal.getIntString(
									sDBVersion == null ? "message.createdatabase" : "message.updatedatabase"),
							AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {

						bQuestionOK = true;

						try {
							BatchSentence bsentence = new BatchSentenceResource(session, sScript);
							bsentence.putParameter("APP_ID", Matcher.quoteReplacement(AppLocal.APP_ID));
							bsentence.putParameter("APP_NAME", Matcher.quoteReplacement(AppLocal.APP_NAME));
							bsentence.putParameter("APP_VERSION", Matcher.quoteReplacement(AppLocal.APP_VERSION));

							java.util.List l = bsentence.list();
							if (l.size() > 0) {
								JMessageDialog.showMessage(this, this,
										new MessageInf(MessageInf.SGN_WARNING,
												AppLocal.getIntString("Database.ScriptWarning"),
												l.toArray(new Throwable[l.size()])));
							}
						} catch (BasicException e) {
							JMessageDialog.showMessage(this, this, new MessageInf(MessageInf.SGN_DANGER,
									AppLocal.getIntString("Database.ScriptError"), e));
							session.close();
							return false;
						}
					} else {
						session.close();
						return false;
					}

				}
			}
		} while (!AppLocal.APP_VERSION.equals(sDBVersion));

		// Cargamos las propiedades de base de datos
		m_propsdb = m_dlSystem.getResourceAsProperties(m_props.getHost() + "/properties");

		// Leo la localizacion de la caja (Almacen).
		m_sInventoryLocation = m_propsdb.getProperty("location");
		if (m_sInventoryLocation == null) {
			m_sInventoryLocation = "0";
			// do not set properties
			// use default location 0
			m_propsdb.setProperty("location", m_sInventoryLocation);
			m_dlSystem.setResourceAsProperties(m_props.getHost() + "/properties", m_propsdb);
		}

		// Inicializo la impresora...
		m_TP = new DeviceTicket(this, this, m_props);

		// Inicializamos
		m_TTP = new TicketParser(getDeviceTicket(), m_dlSystem);
		printerStart();

		// Inicializamos la bascula
		m_Scale = new DeviceScale(this, this, m_props);

		// Inicializamos la scanpal
		m_Scanner = DeviceScannerFactory.createInstance(m_props);

		String sWareHouse;
		try {
			sWareHouse = m_dlSystem.findLocationName(m_sInventoryLocation);
		} catch (BasicException e) {
			sWareHouse = null; // no he encontrado el almacen principal
		}

		// Show Hostname, Warehouse and URL in taskbar
		String configName = m_props.getConfigName();
		m_jHost.setText("<html>" + m_props.getHost() + " - " + sWareHouse + " - " + configName);

		// load properties for login button size
		posprops = m_dlSystem.getResourceAsProperties("Window.Login");

		// initialise signature module
		Security.addProvider(new BouncyCastleProvider());
		try {
			SignatureModul.InitInstance(this);
			if (!noGUI) {
				LoadingIndicator.init(this);

				SignatureModul.getInstance().CheckSignatureUnitState(this, true);

				String signatureUnitInformation = SignatureModul.getInstance().GetSignatureUnitInformation(false);
				jTextSignatureInfo.setText(signatureUnitInformation);

				showLogin();
			}
		} catch (Exception e) {
			Log.Exception(e);
			return false;
		}
		return true;
	}

	private String readDataBaseVersion() {
		try {
			return m_dlSystem.findVersion();
		} catch (Exception ed) {
			// database exception => run create script
			return "create";
		}
	}

	public void tryToClose() {

		if (closeAppView()) {

			// success. continue with the shut down

			// apago el visor
			m_TP.getDeviceDisplay().clearVisor();
			// me desconecto de la base de datos.
			session.close();

			// Download Root form
			SwingUtilities.getWindowAncestor(this).dispose();
		}
	}

	private void tryToLicense() {
		LicenseManager manager = new LicenseManager();
		DeviceInfo deviceinfo = manager.readDeviceInfo(this);
		JLicenseDialog dialog = JLicenseDialog.showDialog(this, this, AppLocal.getIntString("Button.License"));
		// license generated successful
		if (JLicenseDialog.OK == dialog.getReturnCode()) {
			try {
				deviceinfo.writeDeviceInfoLicense(this, dialog.getLicense());
			} catch (BasicException e1) {
				e1.printStackTrace();
			}
		}
	}

	// Interfaz de aplicacion
	public DeviceTicket getDeviceTicket() {
		return m_TP;
	}

	public DeviceScale getDeviceScale() {
		return m_Scale;
	}

	public DeviceScanner getDeviceScanner() {
		return m_Scanner;
	}

	public Session getSession() {
		return session;
	}

	public String getInventoryLocation() {
		return m_sInventoryLocation;
	}

	public String getHost() {
		return this.getProperties().getHost();
	}

	public String getWindowsHost() {
		String hostname = System.getenv("COMPUTERNAME");
		if (hostname == null)
			hostname = System.getenv("HOSTNAME");

		return hostname;
	}

	private void CheckActiveCash(Boolean openNew, Boolean ignoreCache) throws BasicException {
		if (ignoreCache || m_sActiveCashIndex == null || m_dActiveCashDateEnd != null) {
			String host = this.getProperties().getHost();

			// split if contains "\"
			// support POS with same name in front of "\" character sharing a cash shift
			if (host.contains("\\")) {
				host = host.substring(0, host.indexOf('\\'));
			}

			Object[] valcash = m_dlSystem.findActiveCashHost(host);
			if (valcash == null || !host.equals(valcash[1])) {
				if (openNew) {
					String id = UUID.randomUUID().toString();
					Date start = new Date();
					String location = m_propsdb.getProperty("location");

					// open new cash session
					m_dlSystem.execInsertCash(new Object[] { id, host, start, null, location });

					valcash = m_dlSystem.findActiveCashHost(host); // necessary to get current cash sequence
					m_sActiveCashIndex = id;
					m_iActiveCashSequence = (int) valcash[2];
					m_dActiveCashDateStart = start;
					m_dActiveCashDateEnd = null;
				} else {
					m_sActiveCashIndex = null;
					m_dActiveCashDateStart = null;
					m_dActiveCashDateEnd = null;
					m_iActiveCashSequence = -1;
				}

			} else {
				m_sActiveCashIndex = (String) valcash[0];
				m_iActiveCashSequence = (int) valcash[2];
				m_dActiveCashDateStart = (Date) valcash[3];
				m_dActiveCashDateEnd = null;
			}
		}
	}

	public int getActiveCashSequence() {
		return m_iActiveCashSequence;
	}

	public String getActiveCashIndex(Boolean openNew, Boolean ignoreCache) throws BasicException {
		CheckActiveCash(openNew, ignoreCache);

		return m_sActiveCashIndex;
	}

	public Date getActiveCashDateStart() throws BasicException {
		CheckActiveCash(false, false);
		return m_dActiveCashDateStart;
	}

	public Date getActiveCashDateEnd() throws BasicException {
		CheckActiveCash(false, false);
		return m_dActiveCashDateEnd;
	}

	public void setActiveCashDateEnd(Date dateEnd) {
		m_dActiveCashDateEnd = dateEnd;
	}

	public String getLastCashIndex() throws BasicException {
		String host = this.getProperties().getHost();
		// split if contains "\"
		// support POS with same name in front of "\" character sharing a cash shift
		if (host.contains("\\")) {
			host = host.substring(0, host.indexOf('\\'));
		}

		String lastCashIndex = null;
		Object[] valcash = m_dlSystem.findLastCashHost(host);
		lastCashIndex = (String) valcash[0];
		return lastCashIndex;
	}

	public AppProperties getProperties() {
		return m_props;
	}

	public Object getBean(String beanfactory) throws BeanFactoryException {

		// For backwards compatibility
		beanfactory = mapNewClass(beanfactory);

		BeanFactory bf = m_aBeanFactories.get(beanfactory);
		if (bf == null) {

			// testing sripts
			if (beanfactory.startsWith("/")) {
				bf = new BeanFactoryScript(beanfactory);
			} else {
				// Class BeanFactory
				try {
					Class bfclass = Class.forName(beanfactory);

					if (BeanFactory.class.isAssignableFrom(bfclass)) {
						bf = (BeanFactory) bfclass.newInstance();
					} else {
						// the old construction for beans...
						Constructor constMyView = bfclass.getConstructor(new Class[] { AppView.class });
						Object bean = constMyView.newInstance(new Object[] { this });

						bf = new BeanFactoryObj(bean);
					}

				} catch (Exception e) {
					// ClassNotFoundException, InstantiationException,
					// IllegalAccessException, NoSuchMethodException,
					// InvocationTargetException
					throw new BeanFactoryException(e);
				}
			}

			// cache the factory
			m_aBeanFactories.put(beanfactory, bf);

			// Initialize if it is a BeanFactoryApp
			if (bf instanceof BeanFactoryApp) {
				((BeanFactoryApp) bf).init(this);
			}
		}
		return bf.getBean();
	}

	private static String mapNewClass(String classname) {
		String newclass = m_oldclasses.get(classname);
		return newclass == null ? classname : newclass;
	}

	private static void initOldClasses() {
		m_oldclasses = new HashMap<String, String>();

		// update bean names from 2.00 to 2.20
		m_oldclasses.put("com.openbravo.pos.reports.JReportCustomers", "/com/openbravo/reports/customers.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportCustomersB", "/com/openbravo/reports/customersb.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportClosedPos", "/com/openbravo/reports/closedpos.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportClosedProducts", "/com/openbravo/reports/closedproducts.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JChartSales", "/com/openbravo/reports/chartsales.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportInventory", "/com/openbravo/reports/inventory.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportInventory2", "/com/openbravo/reports/inventoryb.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportInventoryBroken",
				"/com/openbravo/reports/inventorybroken.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportInventoryDiff", "/com/openbravo/reports/inventorydiff.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportPeople", "/com/openbravo/reports/people.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportTaxes", "/com/openbravo/reports/taxes.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportUserSales", "/com/openbravo/reports/usersales.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportUserSalesCurrentUser", "/com/openbravo/reports/usersales_current_user.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportProducts", "/com/openbravo/reports/products.bs");
		m_oldclasses.put("com.openbravo.pos.reports.JReportCatalog", "/com/openbravo/reports/productscatalog.bs");

		// update bean names from 2.10 to 2.20
		m_oldclasses.put("com.openbravo.pos.panels.JPanelTax", "com.openbravo.pos.inventory.TaxPanel");

	}

	public void waitCursorBegin() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void waitCursorEnd() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public AppUserView getAppUserView() {
		return m_principalapp;
	}

	private void printerStart() {

		String sresource = m_dlSystem.getResourceAsXML("Printer.Start");
		if (sresource == null) {
			m_TP.getDeviceDisplay().writeVisor(AppLocal.APP_NAME, AppLocal.APP_VERSION);
		} else {
			try {
				m_TTP.printTicket(sresource);
			} catch (TicketPrinterException eTP) {
				m_TP.getDeviceDisplay().writeVisor(AppLocal.APP_NAME, AppLocal.APP_VERSION);
			}
		}
	}

	private void listPeople() {

		try {

			jScrollPane1.getViewport().setView(null);

			JFlowPanel jPeople = new JFlowPanel();
			jPeople.applyComponentOrientation(getComponentOrientation());

			java.util.List people = m_dlSystem.listPeopleVisible();

			for (int i = 0; i < people.size(); i++) {

				AppUser user = (AppUser) people.get(i);

				JButton btn = new JButton(new AppUserAction(user));
				btn.applyComponentOrientation(getComponentOrientation());
				btn.setFocusPainted(false);
				btn.setFocusable(false);
				btn.setRequestFocusEnabled(false);
				btn.setHorizontalAlignment(SwingConstants.LEADING);
				sizeButton(btn);
				jPeople.add(btn);
			}
			jScrollPane1.setMinimumSize(new Dimension(Integer.parseInt(posprops.getProperty("login-panel-width", "510")),
					Integer.parseInt(posprops.getProperty("login-panel-height", "118"))));
			jScrollPane1.setPreferredSize(new Dimension(Integer.parseInt(posprops.getProperty("login-panel-width", "510")),
					Integer.parseInt(posprops.getProperty("login-panel-height", "118"))));
			
			jScrollPane1.getViewport().setView(jPeople);

		} catch (BasicException ee) {
			ee.printStackTrace();
		}
	}
	
	private void sizeButton(JButton btn) {
		// posprops.getProperty(key, defaultValue)
		btn.setMaximumSize(new Dimension(Integer.parseInt(posprops.getProperty("login-img-width", "150")),
				Integer.parseInt(posprops.getProperty("login-img-height", "50"))));
		btn.setPreferredSize(new Dimension(Integer.parseInt(posprops.getProperty("login-img-width", "150")),
				Integer.parseInt(posprops.getProperty("login-img-height", "50"))));
		btn.setMinimumSize(new Dimension(Integer.parseInt(posprops.getProperty("login-img-width", "150")),
				Integer.parseInt(posprops.getProperty("login-img-height", "50"))));
		
		int fontSize = Integer.parseInt(posprops.getProperty("login-btn-fontsize", "12"));
		Font btnFont = btn.getFont().deriveFont(btn.getFont().getStyle(), fontSize);
		btn.setFont(btnFont);
	}

	// La accion del selector
	private class AppUserAction extends AbstractAction {

		private AppUser m_actionuser;

		public AppUserAction(AppUser user) {
			m_actionuser = user;
			putValue(Action.SMALL_ICON, m_actionuser.getIcon());
			putValue(Action.NAME, m_actionuser.getName());
		}

		public AppUser getUser() {
			return m_actionuser;
		}

		public void actionPerformed(ActionEvent evt) {
			// String sPassword = m_actionuser.getPassword();
			if (m_actionuser.authenticate()) {
				// p'adentro directo, no tiene password
				openAppView(m_actionuser);
			} else {
				// comprobemos la clave antes de entrar...
				String sPassword = JPasswordDialog.showEditPassword(JRootApp.this, JRootApp.this,
						AppLocal.getIntString("Label.Password"), m_actionuser.getName(), m_actionuser.getIcon());
				if (sPassword != null) {
					if (m_actionuser.authenticate(sPassword)) {
						openAppView(m_actionuser);
					} else {
						MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
								AppLocal.getIntString("message.BadPassword"));
						msg.show(JRootApp.this, JRootApp.this);
					}
				}
			}
		}
	}

	private void showView(String view) {
		CardLayout cl = (CardLayout) (m_jPanelContainer.getLayout());
		cl.show(m_jPanelContainer, view);
	}

	private void openAppView(AppUser user) {

		if (closeAppView()) {
			// LoadingIndicator loadingIndicator = LoadingIndicator.start();
			m_principalapp = new JPrincipalApp(this, user);

			// The user status notificator
			JComponent principal = m_principalapp.getNotificator();
			principal.setFont(new java.awt.Font("Tahoma", 0, 12));
			principal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jPanel3.add(principal);
			jPanel3.revalidate();

			// The main panel
			m_jPanelContainer.add(m_principalapp, "_" + m_principalapp.getUser().getId());
			showView("_" + m_principalapp.getUser().getId());

			m_principalapp.activate();
		}
	}

	public boolean closeAppView() {

		if (m_principalapp == null) {
			return true;
		} else if (!m_principalapp.deactivate()) {
			return false;
		} else {
			// the status label
			jPanel3.remove(m_principalapp.getNotificator());
			jPanel3.revalidate();
			jPanel3.repaint();

			// remove the card
			m_jPanelContainer.remove(m_principalapp);
			m_principalapp = null;

			showLogin();

			return true;
		}
	}

	private void showLogin() {

		// Show Login
		listPeople();
		showView("login");
		
		// resize buttons
		sizeButton(m_jClose);
		

		// show welcome message
		printerStart();

		// keyboard listener activation
		inputtext = new StringBuffer();
		m_txtKeys.setText(null);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				m_txtKeys.requestFocus();
			}
		});
	}

	private void processKey(char c) {

		if (c == '\n') {

			AppUser user = null;
			try {
				user = m_dlSystem.findPeopleByCard(inputtext.toString());
			} catch (BasicException e) {
				e.printStackTrace();
			}

			if (user == null) {
				// user not found
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocard"));
				msg.show(this, this);
			} else {
				openAppView(user);
			}

			inputtext = new StringBuffer();
		} else {
			inputtext.append(c);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		m_jPanelContainer = new javax.swing.JPanel();
		m_jPanelLogin = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jTextSignatureInfo = new javax.swing.JTextArea();
		jPanel5 = new javax.swing.JPanel();
		m_jLogonName = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		m_jClose = new javax.swing.JButton();
		m_jShowAES = new javax.swing.JButton();
		m_jDEPExport = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		m_txtKeys = new javax.swing.JTextField();
		m_jPanelDown = new javax.swing.JPanel();
		panelTask = new javax.swing.JPanel();
		m_jHost = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		m_txtExportBegin = new javax.swing.JTextField();

		setPreferredSize(new java.awt.Dimension(1024, 768));
		setLayout(new java.awt.BorderLayout());

		m_jPanelContainer.setLayout(new java.awt.CardLayout());

		m_jPanelLogin.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

		jLabel1.setText("<html><center>Registrierkasse W4CASH</center></html>");

		jLabel1.setAlignmentX(0.5F);
		jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLabel1.setMaximumSize(new java.awt.Dimension(800, 1024));
		jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel4.add(jLabel1);

		jTextSignatureInfo.setPreferredSize(new Dimension(640, 180));
		jTextSignatureInfo.setMaximumSize(new Dimension(640, 180));
		jTextSignatureInfo.setEditable(false);
		jTextSignatureInfo.setOpaque(false);
		jTextSignatureInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPanel4.add(jTextSignatureInfo);

		// show AES
		// m_jShowAES.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/key.png")));
		// // NOI18N
		m_jShowAES.setText(AppLocal.getIntString("Button.ShowAES")); // NOI18N
		m_jShowAES.setFocusPainted(false);
		m_jShowAES.setFocusable(false);
		m_jShowAES.setPreferredSize(new java.awt.Dimension(115, 35));
		m_jShowAES.setRequestFocusEnabled(false);
		m_jShowAES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		m_jShowAES.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_jShowAES.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jShowAESActionPerformed(evt);
			}
		});
		jPanel4.add(m_jShowAES);

		m_txtExportBegin.setPreferredSize(new java.awt.Dimension(100, 30));
		m_txtExportBegin.setMaximumSize(new java.awt.Dimension(100, 30));
		m_txtExportBegin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		m_txtExportBegin.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_txtExportBegin.setText("01.01.1990");
		m_txtExportBegin.setBackground(new Color(240, 240, 240, 250));
		jPanel4.add(m_txtExportBegin);

		// m_jDEPExport.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/key.png")));
		// // NOI18N
		m_jDEPExport.setText(AppLocal.getIntString("Button.DEPExport")); // NOI18N
		m_jDEPExport.setFocusPainted(false);
		m_jDEPExport.setFocusable(false);
		m_jDEPExport.setPreferredSize(new java.awt.Dimension(115, 35));
		m_jDEPExport.setRequestFocusEnabled(false);
		m_jDEPExport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		m_jDEPExport.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_jDEPExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jDEPExportActionPerformed(evt);
			}
		});
		jPanel4.add(m_jDEPExport);

		m_jPanelLogin.add(jPanel4, java.awt.BorderLayout.CENTER);

		m_jLogonName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jLogonName.setLayout(new java.awt.BorderLayout());

		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setPreferredSize(new java.awt.Dimension(510, 236));
		m_jLogonName.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel8.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

		// close
		m_jClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/exit.png"))); // NOI18N
		m_jClose.setText(AppLocal.getIntString("Button.Close")); // NOI18N
		m_jClose.setPreferredSize(new java.awt.Dimension(115, 35));
		m_jClose.setFocusPainted(false);
		m_jClose.setFocusable(false);
		m_jClose.setRequestFocusEnabled(false);
		m_jClose.setHorizontalAlignment(JButton.LEFT);
		m_jClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jCloseActionPerformed(evt);
			}
		});

		jPanel8.add(m_jClose);

		jPanel2.add(jPanel8, java.awt.BorderLayout.NORTH);

		jPanel1.setLayout(null);

		m_txtKeys.setPreferredSize(new java.awt.Dimension(0, 0));
		m_txtKeys.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				m_txtKeysKeyTyped(evt);
			}
		});
		jPanel1.add(m_txtKeys);
		m_txtKeys.setBounds(0, 0, 0, 0);

		jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

		m_jLogonName.add(jPanel2, java.awt.BorderLayout.LINE_END);

		jPanel5.add(m_jLogonName);

		m_jPanelLogin.add(jPanel5, java.awt.BorderLayout.SOUTH);

		m_jPanelContainer.add(m_jPanelLogin, "login");

		add(m_jPanelContainer, java.awt.BorderLayout.CENTER);

		m_jPanelDown.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0,
				javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
		m_jPanelDown.setLayout(new java.awt.BorderLayout());

		m_jHost.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		m_jHost.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		m_jHost.setText("*Hostname");
		panelTask.add(m_jHost);

		panelTask.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		m_jPanelDown.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		
		m_jPanelDown.add(panelTask, java.awt.BorderLayout.LINE_START);
		m_jPanelDown.add(jPanel3, java.awt.BorderLayout.LINE_END);

		add(m_jPanelDown, java.awt.BorderLayout.SOUTH);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jLicenseActionPerformed(java.awt.event.ActionEvent evt) {
		tryToLicense();
	}

	private void m_jCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCloseActionPerformed

		tryToClose();

	}// GEN-LAST:event_m_jCloseActionPerformed

	private void m_jShowAESActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCloseActionPerformed
		String signatureUnitInformation = SignatureModul.getInstance().GetSignatureUnitInformation(true);
		jTextSignatureInfo.setText(signatureUnitInformation);
		m_jShowAES.setVisible(false);
	}// GEN-LAST:event_m_jCloseActionPerformed

	private void m_jDEPExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCloseActionPerformed
		SignatureModul.getInstance().DEPExport(this, null, null, m_txtExportBegin.getText());
	}// GEN-LAST:event_m_jCloseActionPerformed

	private void m_txtKeysKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_m_txtKeysKeyTyped

		m_txtKeys.setText("0");

		processKey(evt.getKeyChar());

	}// GEN-LAST:event_m_txtKeysKeyTyped

	public void setMessage(String message) {
		if (message != null && !message.isEmpty()) {
			jLabel1.setText(
					"<html><center>Registrierkasse W4CASH<br><br>W4CASH ist ein auf den Verkauf von Waren oder Dienstleistungen spezialisierte Datenerfassungsapplikation.<br>Diese dient zur Abrechnung von Bargeldums&auml;tzen und zur Erstellung von Belegen.<br>"
							+ message + "</center></html>");
		} else {
			jLabel1.setText(
					"<html><center>Registrierkasse W4CASH<br><br>W4CASH ist ein auf den Verkauf von Waren oder Dienstleistungen spezialisierte Datenerfassungsapplikation.<br>Diese dient zur Abrechnung von Bargeldums&auml;tzen und zur Erstellung von Belegen.</center></html>");

		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JTextArea jTextSignatureInfo;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton m_jShowAES;
	private javax.swing.JButton m_jDEPExport;
	private javax.swing.JButton m_jClose;
	private javax.swing.JLabel m_jHost;
	private javax.swing.JPanel m_jLogonName;
	private javax.swing.JPanel m_jPanelContainer;
	private javax.swing.JPanel m_jPanelDown;
	private javax.swing.JPanel m_jPanelLogin;
	private javax.swing.JTextField m_txtKeys;
	private javax.swing.JPanel panelTask;
	private javax.swing.JTextField m_txtExportBegin;

	// End of variables declaration//GEN-END:variables
	@Override
	public void closeCashIndex() {
		this.m_sActiveCashIndex = null;
	}
}
