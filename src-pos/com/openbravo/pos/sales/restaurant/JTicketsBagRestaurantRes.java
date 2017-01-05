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

package com.openbravo.pos.sales.restaurant;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.plaf.OptionPaneUI;

import org.apache.commons.collections.iterators.EnumerationIterator;

import com.openbravo.beans.*;
import com.openbravo.data.gui.*;
import com.openbravo.data.loader.*;
import com.openbravo.data.user.*;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.JPlacesListDialog;
import com.openbravo.pos.util.PropertyUtil;

import layout.TableLayout;
import layout.TableLayoutConstraints;

import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.customers.CustomerInfo;

public class JTicketsBagRestaurantRes extends javax.swing.JPanel implements EditorRecord {

	private JTicketsBagRestaurantMap m_restaurantmap;

	private DataLogicCustomers dlCustomers = null;

	private DirtyManager m_Dirty;
	private Object m_sID;
	private List<PlaceSplit> placesReservation;
	private CustomerInfo customer;
	private Date m_dCreated;
	// private JTimePanel m_timereservation;
	private boolean m_bReceived;
	private BrowsableEditableData m_bd;
	private Boolean m_isLoading = false;
	// private Date m_dcurrentday;

	//private JCalendarPanel m_datepanel;
	//private JTimePanel m_timepanel;
	private boolean m_bpaintlock = false;

	private AppView m_App;


	/** Creates new form JPanelReservations */
	public JTicketsBagRestaurantRes(AppView oApp, JTicketsBagRestaurantMap restaurantmap) {
		this.m_App = oApp;
		m_restaurantmap = restaurantmap;

		dlCustomers = (DataLogicCustomers) oApp.getBean("com.openbravo.pos.customers.DataLogicCustomers");

		initComponents();

		//m_timereservation = new JTimePanel(m_App, null, JTimePanel.BUTTONS_MINUTE);
		//m_jPanelTime.add(m_timereservation, BorderLayout.CENTER);
		
		txtCustomer.addEditorKeys(m_jKeys);
		m_jtxtChairs.addEditorKeys(m_jKeys);
		m_jtxtDescription.addEditorKeys(m_jKeys);

		m_Dirty = new DirtyManager();
		txtCustomer.addPropertyChangeListener("Text", m_Dirty);
		txtCustomer.addPropertyChangeListener("Text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				customer = new CustomerInfo(null);
				customer.setTaxid(null);
				customer.setSearchkey(null);
				customer.setName(txtCustomer.getText());
			}
		});
		m_jtxtChairs.addPropertyChangeListener("Text", m_Dirty);
		m_jtxtDescription.addPropertyChangeListener("Text", m_Dirty);
		m_jFromDate.getDocument().addDocumentListener(m_Dirty);
		m_jTillDate.getDocument().addDocumentListener(m_Dirty);
		
		writeValueEOF();

		ListProvider lpr = new ListProviderCreator(dlCustomers.getReservationsList(), new MyDateFilter());
		SaveProvider spr = new SaveProvider(dlCustomers.getReservationsUpdate(), dlCustomers.getReservationsInsert(),
				dlCustomers.getReservationsDelete());

		m_bd = new BrowsableEditableData(lpr, spr, new CompareReservations(), this, m_Dirty);
	
		JListNavigator nl = new JListNavigator(m_App, m_bd, true);
		nl.setCellRenderer(new JCalendarItemRenderer(m_App));
		m_jPanelList.add(nl, BorderLayout.CENTER);

		// La Toolbar
		m_jToolbar.add(new JLabelDirty(m_Dirty));
		m_jToolbar.add(new JCounter(m_bd));
		m_jToolbar.add(new JNavigator(m_App, m_bd,-1,null));
		m_jToolbar.add(new JSaver(m_App, m_bd));

		m_jKeys.ScaleButtons();
		ScaleButtons();
	}
	
	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelChairs, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelDateFrom, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelDateTill, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelCustomer, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelNotes, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelPlaces, "common-small-fontsize", "32");
		
		
		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(m_jbtnReceive, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jbtnTables, menuwidth, menuheight, fontsize);
		
		Font font = PropertyUtil.ScaleFont(m_App, m_jtxtDescription.getFont(), "common-small-fontsize", "32");
		m_jtxtChairs.setBFont(font);
		m_jtxtDescription.setBFont(font);
		txtCustomer.setBFont(font);
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jFromDate, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jTillDate, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextAreaFontsize(m_App, m_jPlaces, "common-small-fontsize", "32");
		
		PropertyUtil.ScaleButtonIcon(m_jFromDateButton, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jTillDateButton, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jPlacesAddButton, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jPlacesRemoveButton, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jCustomerButton, menuwidth, menuheight, fontsize);
	}

	private class MyDateFilter implements EditorCreator {
		public Object createValue() throws BasicException {
			return new Object[] { new Date(), new Date() }; // m_dcurrentday
		}

		@Override
		public void ScaleButtons() {
		
		}
	}

	public void activate() {
		reload(DateUtils.getTodayHours(new Date()));
	}

	public void refresh() {
	}

	public boolean deactivate() {
		try {
			return m_bd.actionClosingForm(this);
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.CannotMove"), eD);
			msg.show(m_App, this);
			return false;
		}
	}

	private void RefreshPlaces()
	{
		m_jPlaces.setText(getPlacesString());
	}
	
	public void writeValueEOF() {
		m_sID = null;
		placesReservation = null;
		RefreshPlaces();
		m_dCreated = null;
		m_jFromDate.setText("");
		m_jTillDate.setText("");
		assignCustomer(new CustomerInfo(null));
		m_jtxtChairs.reset();
		m_bReceived = false;
		m_jtxtDescription.reset();
		m_jFromDate.setEnabled(false);
		m_jTillDate.setEnabled(false);
		m_jPlaces.setEnabled(false);
		txtCustomer.setEnabled(false);
		m_jtxtChairs.setEnabled(false);
		m_jtxtDescription.setEnabled(false);
		m_jPlaces.setText("");
		m_jKeys.setEnabled(false);

		m_jbtnReceive.setEnabled(false);
	}

	public void writeValueInsert() {
		m_sID = null;
		placesReservation = new ArrayList<PlaceSplit>();
		RefreshPlaces();
		
		m_dCreated = null;
		
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour < 12)
			hour = 12;
		else 
			hour = 18;
		
		int hourEnd = hour + 5;
		
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hour, 0, 0);
		m_jFromDate.setText(Formats.TIMESTAMP.formatValue(c.getTime()));
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hourEnd, 0, 0);
		m_jTillDate.setText(Formats.TIMESTAMP.formatValue(c.getTime()));
		
		assignCustomer(new CustomerInfo(null));
		m_jPlaces.setText("");
		m_jtxtChairs.setValueInteger(2);
		m_bReceived = false;
		m_jtxtDescription.reset();
		m_jFromDate.setEnabled(true);
		m_jTillDate.setEnabled(true);
		m_jPlaces.setEnabled(false);
		txtCustomer.setEnabled(true);
		m_jtxtChairs.setEnabled(true);
		m_jtxtDescription.setEnabled(true);
		m_jKeys.setEnabled(true);

		m_jbtnReceive.setEnabled(true);

		txtCustomer.activate();
	}

	public void writeValueDelete(Object value) {
		Object[] res = (Object[]) value;
		m_sID = res[0];
		m_dCreated = (Date) res[1];
		
		m_jFromDate.setText(Formats.TIMESTAMP.formatValue((Date) res[2]));
		m_jTillDate.setText(Formats.TIMESTAMP.formatValue((Date) res[10]));

		
		CustomerInfo c = new CustomerInfo((String) res[3]);
		c.setTaxid((String) res[4]);
		c.setSearchkey((String) res[5]);
		c.setName((String) res[6]);
		assignCustomer(c);
		m_jtxtChairs.setValueInteger(((Integer) res[7]).intValue());
		m_bReceived = ((Boolean) res[8]).booleanValue();
		m_jtxtDescription.setText(Formats.STRING.formatValue(res[9]));
		
		
		placesReservation = getPlacesListByString(Formats.STRING.formatValue(res[11]));
		RefreshPlaces();
		
		
		
		m_jFromDate.setEnabled(true);
		m_jTillDate.setEnabled(true);
		m_jPlaces.setEnabled(true);
		
		txtCustomer.setEnabled(false);
		m_jtxtChairs.setEnabled(false);
		m_jtxtDescription.setEnabled(false);
		m_jKeys.setEnabled(false);

		m_jbtnReceive.setEnabled(false);
	}
	
	
	private String getPlacesString()
	{
		StringBuilder sb = new StringBuilder();
		if(placesReservation != null)
		{
			for(PlaceSplit place : placesReservation)
			{
				if(sb.length() > 0)
					sb.append(", ");
				sb.append(place.getName());
			}
		}
		
		return sb.toString();
	}
	
	private String getPlacesSaveString()
	{
		StringBuilder sb = new StringBuilder();
		if(placesReservation != null)
		{
			for(PlaceSplit place : placesReservation)
			{
				if(sb.length() > 0)
					sb.append(";");
				sb.append(place.getId());
				sb.append(",");
				sb.append(place.getName());
			}
		}
		
		return sb.toString();
	}
	
	private List<PlaceSplit> getPlacesListByString(String placesString)
	{
		List<PlaceSplit> list = new ArrayList<PlaceSplit>();
		String[] places = placesString.split(";");
		for(String place : places)
		{
			if(place != null && !"".equals(place))
			{
				PlaceSplit p = new PlaceSplit();
				p.setId(place.split(",")[0]);
				p.setName(place.split(",")[1]);
				list.add(p);
			}
		}
		return list;
	}

	public void writeValueEdit(Object value) {
		Object[] res = (Object[]) value;
		m_sID = res[0];
		m_dCreated = (Date) res[1];
		
		m_jFromDate.setText(Formats.TIMESTAMP.formatValue((Date) res[2]));
		m_jTillDate.setText(Formats.TIMESTAMP.formatValue((Date) res[10]));
		
		CustomerInfo c = new CustomerInfo((String) res[3]);
		c.setTaxid((String) res[4]);
		c.setSearchkey((String) res[5]);
		c.setName((String) res[6]);
		assignCustomer(c);
		
		placesReservation = getPlacesListByString(Formats.STRING.formatValue(res[11]));
		RefreshPlaces();
		
		m_jtxtChairs.setValueInteger(((Integer) res[7]).intValue());
		m_bReceived = ((Boolean) res[8]).booleanValue();
		m_jtxtDescription.setText(Formats.STRING.formatValue(res[9]));

		m_jFromDate.setEnabled(true);
		m_jTillDate.setEnabled(true);
		m_jPlaces.setEnabled(false);

		txtCustomer.setEnabled(true);
		m_jtxtChairs.setEnabled(true);
		m_jtxtDescription.setEnabled(true);
		m_jKeys.setEnabled(true);

		m_jbtnReceive.setEnabled(!m_bReceived); // se habilita si no se ha
												// recibido al cliente

		txtCustomer.activate();
	}

	public Object createValue() throws BasicException {

		Object[] res = new Object[13];

		res[0] = m_sID == null ? UUID.randomUUID().toString() : m_sID;
		res[1] = m_dCreated == null ? new Date() : m_dCreated;
		
		try {
			res[2] = (Date) Formats.TIMESTAMP.parseValue(m_jFromDate.getText());
			if(res[2] == null)
				res[2] = new Date();
		} catch (BasicException e) {
			res[2] = new Date();
		}
		
		res[3] = customer.getId();
		res[4] = customer.getTaxid();
		res[5] = customer.getSearchkey();
		res[6] = customer.getName();
		res[7] = new Integer(m_jtxtChairs.getValueInteger());
		res[8] = new Boolean(m_bReceived);
		res[9] = m_jtxtDescription.getText();
		
		try {
			res[10] = (Date) Formats.TIMESTAMP.parseValue(m_jTillDate.getText());
		} catch (BasicException e) {
			res[10] = new Date();
		}
		
		if(res[10] == null 
				|| ((Date)res[2]).compareTo((Date)res[10]) > 0)
		{
			res[10] = res[2];
		}
		
		res[11] = getPlacesSaveString();
		res[12] = 0;

		return res;
	}

	public Component getComponent() {
		return this;
	}

	private static class CompareReservations implements Comparator {
		public int compare(Object o1, Object o2) {
			Object[] a1 = (Object[]) o1;
			Object[] a2 = (Object[]) o2;
			Date d1 = (Date) a1[2];
			Date d2 = (Date) a2[2];
			int c = d1.compareTo(d2);
			if (c == 0) {
				d1 = (Date) a1[10];
				d2 = (Date) a2[10];
				c = d1.compareTo(d2);
				if(c == 0)
				{
					d1 = (Date) a1[1];
					d2 = (Date) a2[1];
					return d1.compareTo(d2);
				}
				else
				{
					return c;
				}
				
			} else {
				return c;
			}
		}
	}

	private void reload(Date dDate) {

		try {
			m_isLoading = true;
			m_bd.actionLoad();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.noreload"), eD);
			msg.show(m_App, this);
		}
		m_isLoading = false;
		paintDate();
	}

	private void paintDate() {

		m_bpaintlock = true;
		// m_datepanel.setDate(m_dcurrentday);
		// m_timepanel.setDate(m_dcurrentday);
		m_bpaintlock = false;
	}

	private void assignCustomer(CustomerInfo c) {

		txtCustomer.setText(c.getName());
		customer = c;
	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		//jPanel3 = new javax.swing.JPanel();
		//jPanelDate = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		m_jToolbarContainer = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		m_jbtnTables = new javax.swing.JButton();
		m_jbtnReceive = new javax.swing.JButton();
		m_jToolbar = new javax.swing.JPanel();
		m_jPanelList = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		// m_jPanelTime = new javax.swing.JPanel();
		m_jFromDate = new javax.swing.JTextField();
		m_jFromDateButton = new javax.swing.JButton();
		m_jTillDate = new javax.swing.JTextField();
		m_jTillDateButton = new javax.swing.JButton();
		m_jPlacesAddButton = new javax.swing.JButton();
		m_jPlacesRemoveButton = new javax.swing.JButton();
		jLabelDateFrom = new javax.swing.JLabel();
		jLabelDateTill = new javax.swing.JLabel();
		jLabelCustomer = new javax.swing.JLabel();
		jLabelChairs = new javax.swing.JLabel();
		jLabelNotes = new javax.swing.JLabel();
		jLabelPlaces = new javax.swing.JLabel();
		m_jtxtDescription = new com.openbravo.editor.JEditorString();
		m_jtxtChairs = new com.openbravo.editor.JEditorIntegerPositive();
		txtCustomer = new com.openbravo.editor.JEditorString();
		jCustomerButton = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		m_jPlaces = new javax.swing.JTextArea();
		m_jKeys = new com.openbravo.editor.JEditorKeys(m_App);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		setLayout(new java.awt.BorderLayout());

		jPanel2.setLayout(new java.awt.BorderLayout());

		m_jToolbarContainer.setLayout(new java.awt.BorderLayout());

		m_jbtnTables.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/atlantikdesignersmall.png"))); // NOI18N
		m_jbtnTables.setText(AppLocal.getIntString("Button.Tables")); // NOI18N
		m_jbtnTables.setFocusPainted(false);
		m_jbtnTables.setFocusable(false);
		m_jbtnTables.setRequestFocusEnabled(false);
		m_jbtnTables.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jbtnTablesActionPerformed(evt);
			}
		});
		jPanel4.add(m_jbtnTables);

		m_jbtnReceive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/mime.png"))); // NOI18N
		m_jbtnReceive.setText(AppLocal.getIntString("button.receive")); // NOI18N
		m_jbtnReceive.setFocusPainted(false);
		m_jbtnReceive.setFocusable(false);
		m_jbtnReceive.setRequestFocusEnabled(false);
		m_jbtnReceive.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jbtnReceiveActionPerformed(evt);
			}
		});
		jPanel4.add(m_jbtnReceive);

		m_jToolbarContainer.add(jPanel4, java.awt.BorderLayout.LINE_START);
		m_jToolbarContainer.add(m_jToolbar, java.awt.BorderLayout.CENTER);

		jPanel2.add(m_jToolbarContainer, java.awt.BorderLayout.NORTH);

		m_jPanelList.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jPanelList.setLayout(new java.awt.BorderLayout());
		m_jPanelList.setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height));
		jPanel2.add(m_jPanelList, java.awt.BorderLayout.LINE_START);

		
		double size[][] =
            {{TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED},  // Columns
             {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.FILL}}; // Rows


		jPanel1.setLayout(new TableLayout(size));
		
		jLabelDateFrom.setText(AppLocal.getIntString("rest.label.dateFrom")); // NOI18N
		jPanel1.add(jLabelDateFrom, "0, 0");
		jPanel1.add(m_jFromDate, "1, 0");
		
		m_jFromDateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
		m_jFromDateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateStartActionPerformed(evt);
			}
		});
		jPanel1.add(m_jFromDateButton, new TableLayoutConstraints(2,0,3,0,TableLayout.LEFT,TableLayout.TOP));

		
		jLabelDateTill.setText(AppLocal.getIntString("rest.label.dateTill")); // NOI18N
		jPanel1.add(jLabelDateTill, "0, 1");
		jPanel1.add(m_jTillDate, "1, 1");
		m_jTillDateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
		m_jTillDateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateEndActionPerformed(evt);
			}
		});
		jPanel1.add(m_jTillDateButton, new TableLayoutConstraints(2,1,3,1,TableLayout.LEFT,TableLayout.TOP));
		
		

		
		jLabelCustomer.setText(AppLocal.getIntString("rest.label.customer")); // NOI18N
		jPanel1.add(jLabelCustomer, "0, 2");
		
		jPanel1.add(txtCustomer, "1, 2");
		
		jCustomerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
		jCustomerButton.setText(AppLocal.getIntString("Menu.Customers"));
		jCustomerButton.setFocusPainted(false);
		jCustomerButton.setFocusable(false);
		jCustomerButton.setRequestFocusEnabled(false);
		jCustomerButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCustomerActionPerformed(evt);
			}
		});
		jPanel1.add(jCustomerButton, new TableLayoutConstraints(2,2,3,2,TableLayout.LEFT,TableLayout.TOP));
		
		

		jLabelChairs.setText(AppLocal.getIntString("rest.label.chairs")); // NOI18N
		jPanel1.add(jLabelChairs, "0, 3");
		
		jPanel1.add(m_jtxtChairs, "1, 3");
		
		jLabelPlaces.setText(AppLocal.getIntString("rest.label.places")); // NOI18N
		jPanel1.add(jLabelPlaces, "0, 4");
		m_jPlaces.setWrapStyleWord(true);
		m_jPlaces.setLineWrap(true);
		jPanel1.add(m_jPlaces, "1, 4");
		
		
		m_jPlacesAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png")));
		m_jPlacesAddButton.setText(AppLocal.getIntString("rest.btn.tablesAdd"));
		m_jPlacesAddButton.setFocusPainted(false);
		m_jPlacesAddButton.setFocusable(false);
		m_jPlacesAddButton.setRequestFocusEnabled(false);
		m_jPlacesAddButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonPlacesAddActionPerformed(evt);
			}
		});
		jPanel1.add(m_jPlacesAddButton, new TableLayoutConstraints(2,4,2,4,TableLayout.LEFT,TableLayout.TOP));
		
		
		
		m_jPlacesRemoveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnminus.png")));
		m_jPlacesRemoveButton.setText(AppLocal.getIntString("rest.btn.tablesRemove"));
		m_jPlacesRemoveButton.setFocusPainted(false);
		m_jPlacesRemoveButton.setFocusable(false);
		m_jPlacesRemoveButton.setRequestFocusEnabled(false);
		m_jPlacesRemoveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonPlacesRemoveActionPerformed(evt);
			}
		});
		jPanel1.add(m_jPlacesRemoveButton, new TableLayoutConstraints(3,4,3,4,TableLayout.LEFT,TableLayout.TOP));
		
		
		
		
		jLabelNotes.setText(AppLocal.getIntString("rest.label.notes")); // NOI18N
		jPanel1.add(jLabelNotes, "0, 5");
		
		jPanel1.add(m_jtxtDescription, "1, 5");
		

		jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

		jPanel5.setLayout(new java.awt.BorderLayout());
		jPanel5.add(m_jKeys, java.awt.BorderLayout.NORTH);

		jPanel2.add(jPanel5, java.awt.BorderLayout.LINE_END);

		add(jPanel2, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jbtnReceiveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnReceiveActionPerformed

		// marco el cliente como recibido...
		m_bReceived = true;
		m_Dirty.setDirty(true);

		try {
			m_bd.saveData();
			m_restaurantmap.viewTables(customer);
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosaveticket"), eD);
			msg.show(m_App, this);
		}

	}// GEN-LAST:event_m_jbtnReceiveActionPerformed

	private void m_jbtnTablesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnTablesActionPerformed

		try {
			List l = dlCustomers.getReservationsList()
					.list(new Date(), new Date());
			for(Object val : l)
			{
				Object[] e = (Object[])val;
				if((int)e[12] > 0)
				{
					// stay and check errors!!!
					if(JConfirmDialog.showConfirm(m_App, this, 
							AppLocal.getIntString("message.reservationconflicts"), 
							null) == JOptionPane.CANCEL_OPTION)
					{
						m_bd.refreshData();
						return;
					}
					else
					{
						break;
					}
				}
			}
		} catch (BasicException e) {
		}
		
		m_restaurantmap.viewTables();

	}// GEN-LAST:event_m_jbtnTablesActionPerformed

	private void jButtonCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		JCustomerFinder finder = JCustomerFinder.getCustomerFinder(m_App, this, dlCustomers);
		finder.search(customer);
		finder.setVisible(true);

		CustomerInfo c = finder.getSelectedCustomer();

		if (c == null) {
			assignCustomer(new CustomerInfo(null));
		} else {
			assignCustomer(c);
		}

	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButtonPlacesAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		try {
			Date start = (Date) Formats.TIMESTAMP.parseValue(m_jFromDate.getText());
			Date end = (Date) Formats.TIMESTAMP.parseValue(m_jTillDate.getText());
			List<PlaceSplit> placesAvailable;
			placesAvailable = dlCustomers.getAvailablePlaces(start, end);
			placesAvailable.removeAll(placesReservation);
			
			JPlacesListDialog dialog = JPlacesListDialog.newJDialog(m_App, (JButton)evt.getSource());
			PlaceSplit place2Add = dialog.showPlacesList(placesAvailable);
			
			if(place2Add != null && !placesReservation.contains(place2Add))
			{
				placesReservation.add(place2Add);
				RefreshPlaces();
				m_Dirty.setDirty(true);
			}
			
		} catch (BasicException e) {
			JConfirmDialog.showInformation(m_App, JTicketsBagRestaurantRes.this,
					AppLocal.getIntString("message.checkinput"),
					AppLocal.getIntString("error.information"));
		}
		
		
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButtonPlacesRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		JPlacesListDialog dialog = JPlacesListDialog.newJDialog(m_App, (JButton)evt.getSource());
		PlaceSplit place2Remove = dialog.showPlacesList(placesReservation);
		if(place2Remove != null)
		{
			placesReservation.remove(place2Remove);
			RefreshPlaces();
			m_Dirty.setDirty(true);
		}	
		
	}// GEN-LAST:event_jButton1ActionPerformed

	
	
	private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateStartActionPerformed
		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(m_jFromDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			m_jFromDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateStartActionPerformed

	private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateEndActionPerformed
		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(m_jTillDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			m_jTillDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateEndActionPerformed
	
	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jCustomerButton;
	private javax.swing.JLabel jLabelDateFrom;
	private javax.swing.JLabel jLabelDateTill;
	private javax.swing.JLabel jLabelCustomer;
	private javax.swing.JLabel jLabelChairs;
	private javax.swing.JLabel jLabelPlaces;
	private javax.swing.JLabel jLabelNotes;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	//private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	//private javax.swing.JPanel jPanelDate;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	private javax.swing.JPanel m_jPanelList;
	//private javax.swing.JPanel m_jPanelTime;
	private javax.swing.JTextField m_jFromDate;
	private javax.swing.JButton m_jFromDateButton;
	private javax.swing.JTextField m_jTillDate;
	private javax.swing.JButton m_jTillDateButton;
	private javax.swing.JTextArea m_jPlaces;
	private javax.swing.JButton m_jPlacesAddButton;
	private javax.swing.JButton m_jPlacesRemoveButton;
	private javax.swing.JPanel m_jToolbar;
	private javax.swing.JPanel m_jToolbarContainer;
	private javax.swing.JButton m_jbtnReceive;
	private javax.swing.JButton m_jbtnTables;
	private com.openbravo.editor.JEditorIntegerPositive m_jtxtChairs;
	private com.openbravo.editor.JEditorString m_jtxtDescription;
	private com.openbravo.editor.JEditorString txtCustomer;
	// End of variables declaration//GEN-END:variables

	@Override
	public void sortEditor(BrowsableEditableData bd) {
		
	}
}
