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

package com.openbravo.pos.sales;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurantMap;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.JPrincipalApp;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.basic.BasicException;
import com.openbravo.basic.SignatureUnitException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.admin.DataLogicAdmin;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.ticket.PriceZoneProductInfo;
import com.openbravo.pos.ticket.ProductInfoEdit;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.transfer.SalesTransferModule;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.OnScreenKeyboardUtil;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.ReportUtils;

import at.w4cash.signature.SignatureModul;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.print.PrintService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import sun.security.krb5.internal.Ticket;
import sun.util.logging.resources.logging;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, TicketsEditor {

	private static final long serialVersionUID = 6475490073932528270L;
	// Variable numerica
	private final static int NUMBERZERO = 0;
	private final static int NUMBERVALID = 1;

	private final static int NUMBER_INPUTZERO = 0;
	private final static int NUMBER_INPUTZERODEC = 1;
	private final static int NUMBER_INPUTINT = 2;
	private final static int NUMBER_INPUTDEC = 3;
	private final static int NUMBER_PORZERO = 4;
	private final static int NUMBER_PORZERODEC = 5;
	private final static int NUMBER_PORINT = 6;
	private final static int NUMBER_PORDEC = 7;

	protected JTicketLines m_ticketlines;

	// private Template m_tempLine;
	private TicketParser m_TTP;

	protected TicketInfo m_oTicket;
	protected Object m_oTicketExt;

	// Estas tres variables forman el estado...
	private int m_iNumberStatus;
	private int m_iNumberStatusInput;
	private int m_iNumberStatusPor;
	private StringBuffer m_sBarcode;

	private JTicketsBag m_ticketsbag;

	private SentenceList senttax;
	private SentenceList sentpriceZoneProducts;
	private ListKeyed taxcollection;
	// private ComboBoxValModel m_TaxModel;

	private SentenceList senttaxcategories;
	private ListKeyed taxcategoriescollection;
	private ComboBoxValModel taxcategoriesmodel;

	private TaxesLogic taxeslogic;
	private PriceZonesLogic priceZonesLogic;

	// private ScriptObject scriptobjinst;
	protected JPanelButtons m_jbtnconfig;

	protected AppView m_App;
	protected DataLogicSystem dlSystem;
	protected DataLogicSales dlSales;
	protected DataLogicCustomers dlCustomers;

	private JPaymentSelect paymentdialogreceipt;
	private JPaymentSelect paymentdialogrefund;
	private TicketInfo m_oTicketClone;

	private SalesTransferModule transferModule;

	protected JTicketsBagRestaurantMap m_restaurant;

	public JTicketsBagRestaurantMap getRestaurant() {
		return m_restaurant;
	}

	public void setRestaurant(JTicketsBagRestaurantMap m_restaurant) {
		this.m_restaurant = m_restaurant;
	}

	/** Creates new form JTicketView */
	public JPanelTicket() {

	}

	private String SystemDataAddressLine1 = "";
	private String SystemDataAddressLine2 = "";
	private String SystemDataStreet = "";
	private String SystemDataCity = "";
	private String SystemDataTaxid = "";
	private String SystemDataThanks = "";
	private String SystemDataAccountBank = "";
	private String SystemDataAccountOwner = "";
	private String SystemDataAccountBIC = "";
	private String SystemDataAccountIBAN = "";
	private boolean issaege;

	private void initSystemData() {
		DataLogicAdmin dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin");
		TableDefinition tresources = dlAdmin.getTableResources();

		try {
			List res = tresources.getListSentence().list();
			Object o = res.get(0);
			// try to find System.AddressLine1
			for (int i = 0; i < res.size(); i++) {
				if ("System.AddressLine1".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataAddressLine1 = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));

					continue;
				} else if ("System.AddressLine2".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataAddressLine2 = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));
					continue;
				} else if ("System.Street".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataStreet = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));
					continue;
				} else if ("System.City".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataCity = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));
					continue;
				} else if ("System.TAXID".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataTaxid = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));
					continue;
				} else if ("System.Thanks".compareTo(((Object[]) res.get(i))[1].toString()) == 0) {
					SystemDataThanks = ((Formats.BYTEA.formatValue(((Object[]) res.get(i))[3])));
					continue;
				} else if("System.AccountBank".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountBank = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountOwner".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountOwner = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountBIC".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountBIC = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountIBAN".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountIBAN = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				}
			}
			// res.get(0);
		} catch (BasicException e) {
			Log.Exception(e);
		}
	}

	public void init(AppView app) throws BeanFactoryException {
		m_App = app;

		initSystemData();

		this.transferModule = new SalesTransferModule();

		initComponents();

		dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
		dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
		dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
		dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");

		m_ticketlines = new JTicketLines(app, "sales-producttable-lineheight", "sales-producttable-fontsize",
				dlSystem.getResourceAsXML("Ticket.Line"));
		m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);
		m_ticketlines.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					ticketListChanged();

				}
			}
		});

		m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

		// add configuration dependend (default, standard, restaurant) sales
		// views and buttons
		m_ticketsbag = getJTicketsBag();
		JComponent bag = m_ticketsbag.getBagComponent();
		m_jPanelBag.add(bag); // , BorderLayout.LINE_START);
		JComponent bagNull = m_ticketsbag.getNullComponent();
		add(bagNull, "null");

		// Los botones configurables...
		m_jbtnconfig = new JPanelButtons("Ticket.Buttons", this);
		m_jButtonsExt.add(m_jbtnconfig);

		// categories and product panel
		Component categories = getSouthComponent();
		catcontainer.add(categories, BorderLayout.CENTER);

		// El modelo de impuestos
		senttax = dlSales.getTaxList();
		senttaxcategories = dlSales.getTaxCategoriesList();

		taxcategoriesmodel = new ComboBoxValModel();

		sentpriceZoneProducts = dlSales.getPriceZonesProductList();
		
		// ponemos a cero el estado
		stateToZero();

		// inicializamos
		m_oTicket = null;
		m_oTicketExt = null;

		dlSystem.getResourceAsXML("ticket.addline");

		ScaleButtons();
	}

	public Object getBean() {
		return this;
	}

	public JComponent getComponent() {
		return this;
	}

	public void activate() throws BasicException {

		paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
		paymentdialogreceipt.init(m_App);
		paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
		paymentdialogrefund.init(m_App);

		
		
		// impuestos incluidos seleccionado ?
		m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

	
		// Inicializamos el combo de los impuestos.
		java.util.List<TaxInfo> taxlist = senttax.list();
		taxcollection = new ListKeyed<TaxInfo>(taxlist);
		java.util.List<TaxCategoryInfo> taxcategorieslist = senttaxcategories.list();
		taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

		taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
		m_jTax.setModel(taxcategoriesmodel);

		String taxesid = m_jbtnconfig.getProperty("taxcategoryid");
		if (taxesid == null) {
			if (m_jTax.getItemCount() > 0) {
				m_jTax.setSelectedIndex(0);
			}
		} else {
			taxcategoriesmodel.setSelectedKey(taxesid);
		}

		taxeslogic = new TaxesLogic(taxlist);
		
		java.util.List<PriceZoneProductInfo> priceZoneProductList = sentpriceZoneProducts.list();
		priceZonesLogic = new PriceZonesLogic(priceZoneProductList);
		
		// Show taxes options
		if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
			m_jTax.setVisible(true);
			m_jaddtax.setVisible(true);
		} else {
			m_jTax.setVisible(false);
			m_jaddtax.setVisible(false);
		}

		// Authorization for buttons
		btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
		if(!m_App.getAppUserView().getUser().hasPermission("sales.Not.DeleteLines"))
			m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
		else
			m_jDelete.setEnabled(false);
		
		m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
		m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
		m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());

		m_ticketsbag.activate();
		
		
		// Signature unit datalogic init and checks
		SignatureModul sig = SignatureModul.getInstance();
		sig.InitDataLogic(this, dlSales, taxeslogic);
		sig.CheckSignatureUnitState(this, true);
		if(sig.GetIsActive())
		{
			sig.CheckSignatureExpiration(this);
			sig.CheckSignatureOutage(this);
			sig.CheckStartTicket(this);
			sig.CheckOpenTicketValidations(this);
		}
	}

	public boolean deactivate() {
		/*
		 * if (this.m_restaurant != null) { try { this.m_restaurant.newTicket();
		 * } catch (Exception ex) {
		 * 
		 * } }
		 */
		return m_ticketsbag.deactivate();
	}

	protected abstract JTicketsBag getJTicketsBag();

	protected abstract Component getSouthComponent();

	protected abstract void resetSouthComponent();

	public void ticketListChanged() {
		m_ticketsbag.ticketListChange(m_ticketlines);

		int i = m_ticketlines.getSelectedIndex();
		if (i >= 0) {

			// side buttons
			btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
			
			m_jEditLine.setEnabled(true);
			
			if(!m_App.getAppUserView().getUser().hasPermission("sales.Not.DeleteLines"))
				m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
			else
				m_jDelete.setEnabled(false);
			
			jEditAttributes.setEnabled(true);
		} else {
			btnSplit.setEnabled(false);
			jEditAttributes.setEnabled(false);
			m_jEditLine.setEnabled(false);
			m_jDelete.setEnabled(false);
		}
	}

	
	
	public void setActiveTicket(TicketInfo oTicket, Object oTicketExt) {
		m_oTicket = oTicket;
		m_oTicketExt = oTicketExt;

		if (m_oTicket != null) {
			// Asign preeliminary properties to the receipt
			m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());

			/*
			 * set at ticket close try {
			 * m_oTicket.setActiveCash(m_App.getActiveCashIndex()); } catch
			 * (Exception ex) { m_oTicket.setActiveCash(null); }
			 */
			m_oTicket.setDate(new Date()); // Set the edition date.
		}

		executeEvent(m_oTicket, m_oTicketExt, "ticket.show");

		if (m_oTicket != null)
			m_oTicketClone = m_oTicket.copyTicket();
		else
			m_oTicketClone = null;

		if (oTicket != null)
			resetSouthComponent(); // reset categories and products

		refreshTicket();

		ticketListChanged();
	}
	
	public void SyncTicketClone()
	{
		m_oTicketClone = m_oTicket.copyTicket();
	}

	public void DoSaveTicketEvent(){
		if(m_oTicket != null)
			executeEvent(m_oTicket, m_oTicketExt, "ticket.save");
	}
	
	
	public TicketInfo getActiveTicket() {
		return m_oTicket;
	}

	public TicketInfo getActiveTicketClone() {
		return m_oTicketClone;
	}

	private void refreshTicket() {

		CardLayout cl = (CardLayout) (getLayout());

		if (m_oTicket == null) {
			m_jTicketId.setText(null);
			m_ticketlines.clearTicketLines();

			// m_jSubtotalEuros.setText(null);
			// m_jTaxesEuros.setText(null);
			m_jTotalEuros.setText(null);

			stateToZero();

			// Muestro el panel de nulos.
			cl.show(this, "null");

		} else {
			if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
				// Make disable Search and Edit Buttons
				m_jEditLine.setVisible(false);
				m_jList.setVisible(false);
			}

			// Refresh ticket taxes
			for (TicketLineInfo line : m_oTicket.getLines()) {
				line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), m_oTicket.getDate(),
						m_oTicket.getCustomer()));
			}

			// The ticket name
			m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));

			// Limpiamos todas las filas y anadimos las del ticket actual
			m_ticketlines.clearTicketLines();

			for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
				m_ticketlines.addTicketLine(m_oTicket.getLine(i));
			}
			printPartialTotals();
			stateToZero();

			// Muestro el panel de tickets.
			cl.show(this, "ticket");

			// activo el tecleador...
			m_jKeyFactory.setText(null);
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					m_jKeyFactory.requestFocus();
				}
			});
		}
	}

	private void printPartialTotals() {

		if (m_oTicket.getLinesCount() == 0) {
			// m_jSubtotalEuros.setText(null);
			// m_jTaxesEuros.setText(null);
			// m_jTotalEuros.setText(""); // do not clear last total
		} else {
			// m_jSubtotalEuros.setText(m_oTicket.printSubTotal());
			// m_jTaxesEuros.setText(m_oTicket.printTax());
			m_jTotalEuros.setText(m_oTicket.printTotal());
		}
	}

	private void paintTicketLine(int index, TicketLineInfo oLine) {
		TicketLineInfo oOrigLine = m_oTicket.getLine(index);
		if (executeEventAndRefresh("ticket.setline", new ScriptArg("index", index),
				new ScriptArg("line", oLine)) == null) {

			m_oTicket.setLine(index, oLine);
			m_ticketlines.setTicketLine(index, oLine);
			m_ticketlines.setSelectedIndex(index);

			visorTicketLine(oLine, oOrigLine, true); // Y al visor tambien...
			printPartialTotals();
			stateToZero();

			// event receipt
			executeEventAndRefresh("ticket.change");
		}
	}

	private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice, String count, String height, String width, String length) {

		TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getDate(), m_oTicket.getCustomer());

		addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, tax,
				(java.util.Properties) (oProduct.getProperties().clone()), issaege, height, width, length, count));
	}

	protected void addTicketLine(TicketLineInfo oLine) {

		TicketLineInfo oOrigLine = oLine.copyTicketLine();

		if (executeEventAndRefresh("ticket.addline", new ScriptArg("line", oLine)) == null) {

			if (oLine.isProductCom()) {
				// Comentario entonces donde se pueda
				int i = m_ticketlines.getSelectedIndex();

				// me salto el primer producto normal...
				if (i >= 0 && !m_oTicket.getLine(i).isProductCom()) {
					i++;
				}

				// me salto todos los productos auxiliares...
				while (i >= 0 && i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
					i++;
				}

				if (i >= 0) {
					// get previous line and copy sort number and category id
					
					TicketLineInfo previousLine = m_oTicket.getLine(i-1);
					oLine.setProperty("product.sort", previousLine.getProperty("product.sort", previousLine.getProductID()));
					
					if(previousLine.getProperty("product.categoryid") != null)
						oLine.setProperty("product.categoryid", previousLine.getProperty("product.categoryid"));

					m_oTicket.insertLine(i, oLine);
					m_ticketlines.insertTicketLine(i, oLine); // Pintamos la
																// linea en la
																// vista...
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			} else {
				// Producto normal, entonces al finalnewline.getMultiply()
				m_oTicket.addLine(oLine);
				m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la
													// vista...
			}

			// visorTicketLine(oLine, oOrigLine, false);
			printPartialTotals();
			stateToZero();

			// event receipt
			executeEventAndRefresh("ticket.change");
		}
	}

	private void removeTicketLine(int i) {

		TicketLineInfo oOrigLine = m_oTicket.getLine(i);
		if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {

			if (m_oTicket.getLine(i).isProductCom()) {
				// Es un producto auxiliar, lo borro y santas pascuas.
				m_oTicket.removeLine(i);
				m_ticketlines.removeTicketLine(i);
			} else {
				// Es un producto normal, lo borro.
				m_oTicket.removeLine(i);
				m_ticketlines.removeTicketLine(i);
				// Y todos lo auxiliaries que hubiera debajo.
				while (i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
					m_oTicket.removeLine(i);
					m_ticketlines.removeTicketLine(i);
				}
			}

			visorTicketLine(null, oOrigLine, false); // borro el visor
			printPartialTotals(); // pinto los totales parciales...
			stateToZero(); // Pongo a cero

			// event receipt
			executeEventAndRefresh("ticket.change");
		}
	}

	/*
	 * do not allow sales without product reference ! private ProductInfoExt
	 * getInputProduct() { ProductInfoExt oProduct = new ProductInfoExt(); // Es
	 * un ticket oProduct.setReference(null); oProduct.setCode(null);
	 * oProduct.setName(""); oProduct.setTaxCategoryID(((TaxCategoryInfo)
	 * taxcategoriesmodel.getSelectedItem()).getID());
	 * 
	 * oProduct.setPriceSell(includeTaxes(oProduct.getTaxCategoryID(),
	 * getInputValue()));
	 * 
	 * return oProduct; }
	 */

	private double includeTaxes(String tcid, double dValue) {
		if (m_jaddtax.isSelected()) {
			TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getDate(), m_oTicket.getCustomer());
			double dTaxRate = tax == null ? 0.0 : tax.getRate();
			return dValue / (1.0 + dTaxRate);
		} else {
			return dValue;
		}
	}

	private double getInputValue() {
		try {
			return Double.parseDouble(m_jPrice.getText());
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	private double getPorValue() {
		try {
			return Double.parseDouble(m_jPor.getText().substring(1));
		} catch (NumberFormatException e) {
			return 1.0;
		} catch (StringIndexOutOfBoundsException e) {
			return 1.0;
		}
	}

	private String getPorStrValue() {
		try {
			double val = Double.parseDouble(m_jPor.getText().substring(1));

			if (val == (long) val) {
				return String.format("%1$.0f", val);
			} else {
				return String.format("%1$s", val).replace(",", ".");
			}

		} catch (NumberFormatException e) {
			return "";
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
	}

	private void stateToZero() {
		m_jPor.setText("");
		m_jPrice.setText("");
		m_sBarcode = new StringBuffer();

		m_iNumberStatus = NUMBER_PORZERO;
		m_iNumberStatusInput = NUMBERZERO;
		m_iNumberStatusPor = NUMBERZERO;
	}

	private void incProductByCode(String sCode) {
		// precondicion: sCode != null

		try {
			ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
			if (oProduct == null) {
				Toolkit.getDefaultToolkit().beep();
				new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(m_App, this);
				stateToZero();
			} else {
				// Se anade directamente una unidad con el precio y todo
				incProduct(oProduct);
			}
		} catch (BasicException eData) {
			stateToZero();
			new MessageInf(eData).show(m_App, this);
		}
	}

	private void incProductByCodePrice(String sCode, double dPriceSell, String count) {
		// precondicion: sCode != null

		try {
			ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
			if (oProduct == null) {
				Toolkit.getDefaultToolkit().beep();
				new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(m_App, this);
				stateToZero();
			} else {
				// Se anade directamente una unidad con el precio y todo
				if (m_jaddtax.isSelected()) {
					// debemos quitarle los impuestos ya que el precio es con
					// iva incluido...
					TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getDate(),
							m_oTicket.getCustomer());
					addTicketLine(oProduct, 1.0, dPriceSell / (1.0 + tax.getRate()), count, oProduct.getHeight(), oProduct.getWidth(), oProduct.getLength());
				} else {
					addTicketLine(oProduct, 1.0, dPriceSell, count, oProduct.getHeight(), oProduct.getWidth(), oProduct.getLength());
				}
			}
		} catch (BasicException eData) {
			stateToZero();
			new MessageInf(eData).show(m_App, this);
		}
	}

	private void incProduct(ProductInfoExt prod) {

		if (prod.isScale() && m_App.getDeviceScale().existsScale()) {
			try {
				Double value = m_App.getDeviceScale().readWeight();
				if (value != null) {
					incProduct(value.doubleValue(), prod);
				}
			} catch (ScaleException e) {
				Toolkit.getDefaultToolkit().beep();
				new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(m_App, this);
				stateToZero();
			}
		} else {
			// No es un producto que se pese o no hay balanza
			incProduct(getPorValue(), prod);
		}
	}

	private void incProduct(double dPor, ProductInfoExt prod) {
		double price = 0.0;
		String count = "";
		String height = "";
		String width = "";
		String length = "";

		this.issaege = Boolean
				.parseBoolean(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "module-saegewerk", "false"));
		if ((prod.getPriceSell() > 0.0 
				|| (prod.isCom() && prod.getPriceSell() <= -0.01))
				&& !issaege) {
			price = prod.getPriceSell();
			TaxInfo tax = taxeslogic.getTaxInfo(prod.getTaxCategoryID(), m_oTicket.getDate(), m_oTicket.getCustomer());
			
			// PRICEZONE 
			Double priceZonePrice = priceZonesLogic.getPrice(prod.getID(), m_oTicket.getCustomer(), m_App.getInventoryLocation(), price, tax.getRate());
			price = priceZonePrice;
			
			String amountEqualsPrice = prod.getProperty("AmountEqualsPrice", "False");
			if("True".equals(amountEqualsPrice))
			{
				double pricetax = prod.getPriceSellTax(tax);
				dPor = dPor / pricetax;
			}
			addTicketLine(prod, dPor, price, count, prod.getHeight(), prod.getWidth(), prod.getLength());
		} else {
			// open edit dialog to input price
			TaxInfo tax = taxeslogic.getTaxInfo(prod.getTaxCategoryID(), m_oTicket.getDate(), m_oTicket.getCustomer());
			TicketLineInfo ticketLine = new TicketLineInfo(prod, dPor, prod.getPriceSell(), tax,
					(java.util.Properties) (prod.getProperties().clone()), issaege, prod.getHeight(), prod.getWidth(), prod.getLength(), null);

			try {
				TicketLineInfo newTL = JProductLineEdit.showMessage(this, m_App, ticketLine);
				if (newTL != null) {
					height = newTL.getHeight();
					width = newTL.getWidth();
					length = newTL.getLength();
					
					price = newTL.getPrice();
					dPor = newTL.getMultiply();
					count = newTL.getCount();
					
					addTicketLine(prod, dPor, price, count, height, width, length);
				}
			} catch (BasicException e) {
				e.printStackTrace();
				price = 0.0;
				dPor = 1.0;
				count = "";
			}

		}

		// precondicion: prod != null
//		addTicketLine(prod, dPor, price, count);
	}

	protected void buttonTransition(ProductInfoExt prod) {
		// precondicion: prod != null

		if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
			incProduct(prod);
		} else if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
			incProduct(getPorValue(), prod);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	private void stateTransition(char cTrans) {

		if (cTrans == '\n') {
			// Codigo de barras introducido
			if (m_sBarcode.length() > 0) {
				String sCode = m_sBarcode.toString();
				if (sCode.startsWith("c")) {
					// barcode of a customers card
					try {
						CustomerInfoExt newcustomer = dlSales.findCustomerExt(sCode);
						if (newcustomer == null) {
							Toolkit.getDefaultToolkit().beep();
							new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"))
									.show(m_App, this);
						} else {
							m_oTicket.setCustomer(newcustomer);
							m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
						}
					} catch (BasicException e) {
						Toolkit.getDefaultToolkit().beep();
						new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e)
								.show(m_App, this);
					}
					stateToZero();
				} else {
					incProductByCode(sCode);
				}
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
		} else {

			// Esto es para el los productos normales...
			if (cTrans == '\u007f') {
				stateToZero();

			} else if ((cTrans == '0') && (m_iNumberStatus == NUMBER_INPUTZERO)) {
				m_jPrice.setText("0");
				m_sBarcode.append(cTrans);
			} else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5'
					|| cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_INPUTZERO)) {
				// Un numero entero
				m_jPrice.setText(Character.toString(cTrans));
				m_sBarcode.append(cTrans);
				m_iNumberStatus = NUMBER_INPUTINT;
				m_iNumberStatusInput = NUMBERVALID;
			} else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4'
					|| cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_INPUTINT)) {
				// Un numero entero
				m_jPrice.setText(m_jPrice.getText() + cTrans);
				m_sBarcode.append(cTrans);
			} else if ((cTrans == '.' || cTrans == ',') && m_iNumberStatus == NUMBER_INPUTZERO) {
				m_jPrice.setText("0.");
				m_sBarcode.append(cTrans);
				m_iNumberStatus = NUMBER_INPUTZERODEC;
			} else if ((cTrans == '.' || cTrans == ',') && m_iNumberStatus == NUMBER_INPUTINT) {
				m_jPrice.setText(m_jPrice.getText() + ".");
				m_sBarcode.append(cTrans);
				m_iNumberStatus = NUMBER_INPUTDEC;

			} else if ((cTrans == '0')
					&& (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
				// Un numero decimal
				m_jPrice.setText(m_jPrice.getText() + cTrans);
				m_sBarcode.append(cTrans);
			} else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5'
					|| cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
				// Un numero decimal
				m_jPrice.setText(m_jPrice.getText() + cTrans);
				m_sBarcode.append(cTrans);
				m_iNumberStatus = NUMBER_INPUTDEC;
				m_iNumberStatusInput = NUMBERVALID;

			} else if (cTrans == '*' && (m_iNumberStatus == NUMBER_PORINT || m_iNumberStatus == NUMBER_PORDEC)) {
				m_iNumberStatus = NUMBER_INPUTZERO;
			} else if (cTrans == '*' && (m_iNumberStatus == NUMBER_PORZERO || m_iNumberStatus == NUMBER_PORZERODEC)) {
				m_jPor.setText("x1");
				m_iNumberStatus = NUMBER_INPUTZERO;

			} else if ((cTrans == '0') && (m_iNumberStatus == NUMBER_PORZERO)) {
				m_jPor.setText("x0");
			} else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5'
					|| cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_PORZERO)) {
				// Un numero entero
				m_jPor.setText("x" + Character.toString(cTrans));
				m_iNumberStatus = NUMBER_PORINT;
				m_iNumberStatusPor = NUMBERVALID;
			} else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4'
					|| cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_PORINT)) {
				// Un numero entero
				m_jPor.setText(m_jPor.getText() + cTrans);

			} else if ((cTrans == '.' || cTrans == ',') && m_iNumberStatus == NUMBER_PORZERO) {
				m_jPor.setText("x0.");
				m_iNumberStatus = NUMBER_PORZERODEC;
			} else if ((cTrans == '.' || cTrans == ',') && m_iNumberStatus == NUMBER_PORINT) {
				m_jPor.setText(m_jPor.getText() + ".");
				m_iNumberStatus = NUMBER_PORDEC;

			} else if ((cTrans == '0') && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
				// Un numero decimal
				m_jPor.setText(m_jPor.getText() + cTrans);
			} else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5'
					|| cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
					&& (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
				// Un numero decimal
				m_jPor.setText(m_jPor.getText() + cTrans);
				m_iNumberStatus = NUMBER_PORDEC;
				m_iNumberStatusPor = NUMBERVALID;

				/*
				 * do not allow sales without product reference ! } else if
				 * (cTrans == '\u00a7' && m_iNumberStatusInput == NUMBERVALID &&
				 * m_iNumberStatusPor == NUMBERZERO) { // Scale button pressed
				 * and a number typed as a price if
				 * (m_App.getDeviceScale().existsScale() &&
				 * m_App.getAppUserView().getUser().hasPermission(
				 * "sales.EditLines")) { try { Double value =
				 * m_App.getDeviceScale().readWeight(); if (value != null) {
				 * ProductInfoExt product = getInputProduct();
				 * addTicketLine(product, value.doubleValue(),
				 * product.getPriceSell()); } } catch (ScaleException e) {
				 * Toolkit.getDefaultToolkit().beep(); new
				 * MessageInf(MessageInf.SGN_WARNING,
				 * AppLocal.getIntString("message.noweight"), e).show(this);
				 * stateToZero(); } } else { // No existe la balanza;
				 * Toolkit.getDefaultToolkit().beep(); }
				 */
			} else if (cTrans == '\u00a7' && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
				// Scale button pressed and no number typed.
				int i = m_ticketlines.getSelectedIndex();
				if (i < 0) {
					Toolkit.getDefaultToolkit().beep();
				} else if (m_App.getDeviceScale().existsScale()) {
					try {
						Double value = m_App.getDeviceScale().readWeight();
						if (value != null) {
							TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
							newline.setMultiply(value.doubleValue());
							newline.setPrice(Math.abs(newline.getPrice()));
							paintTicketLine(i, newline);
						}
					} catch (ScaleException e) {
						// Error de pesada.
						Toolkit.getDefaultToolkit().beep();
						new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(m_App,
								this);
						stateToZero();
					}
				} else {
					// No existe la balanza;
					Toolkit.getDefaultToolkit().beep();
				}

				// set sign for multiplier
			} else if (cTrans == '+' && (m_iNumberStatus == NUMBER_PORINT || m_iNumberStatus == NUMBER_PORZERODEC
					|| m_iNumberStatus == NUMBER_PORDEC)) {
				m_jPor.setText(String.format("x%1$s", getPorStrValue().replace("-", "")));
				// set sign for multiplier
			} else if (cTrans == '-' && (m_iNumberStatus == NUMBER_PORINT || m_iNumberStatus == NUMBER_PORZERODEC
					|| m_iNumberStatus == NUMBER_PORDEC)) {
				String newText = String.format("x-%1$s", getPorStrValue().replace("-", ""));
				m_jPor.setText(newText);
				if ("x-".equals(newText)) {
					m_iNumberStatus = NUMBER_PORINT;
				}

				// Add one product more to the selected line
			} else if (cTrans == '+' && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
				int i = m_ticketlines.getSelectedIndex();
				if (i < 0) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
					// If it's a refund + button means one unit less
					if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
						newline.setMultiply(newline.getMultiply() - 1.0);
						paintTicketLine(i, newline);
					} else {
						// add one unit to the selected line
						newline.setMultiply(newline.getMultiply() + 1.0);
						paintTicketLine(i, newline);
					}
				}

				// Delete one product of the selected line
			} else if (cTrans == '-' && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO
					&& m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {

				int i = m_ticketlines.getSelectedIndex();
				if (i < 0) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
					// If it's a refund - button means one unit more
					if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
						newline.setMultiply(newline.getMultiply() + 1.0);
						if (newline.getMultiply() >= 0) {
							removeTicketLine(i);
						} else {
							paintTicketLine(i, newline);
						}
					} else {
						// substract one unit to the selected line
						newline.setMultiply(newline.getMultiply() - 1.0);
						if (newline.getMultiply() <= 0.0) {
							removeTicketLine(i); // elimino la linea
						} else {
							paintTicketLine(i, newline);
						}
					}
				}

				// Set n products to the selected line
			} else if (cTrans == '+' && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
				int i = m_ticketlines.getSelectedIndex();
				if (i < 0) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					double dPor = getPorValue();
					TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
					double oldMultiply = newline.getMultiply();
					newline.setMultiply(oldMultiply + dPor);
					paintTicketLine(i, newline);
				}

				// Set n negative products to the selected line
			} else if (cTrans == '-' && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID
					&& m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {

				int i = m_ticketlines.getSelectedIndex();
				if (i < 0) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					double dPor = getPorValue();
					TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
					double oldMultiply = newline.getMultiply();
					newline.setMultiply(oldMultiply - dPor);
					paintTicketLine(i, newline);
				}

				/*
				 * do not allow sales without product reference ! // Anadimos 1
				 * producto } else if (cTrans == '+' && m_iNumberStatusInput ==
				 * NUMBERVALID && m_iNumberStatusPor == NUMBERZERO &&
				 * m_App.getAppUserView().getUser().hasPermission(
				 * "sales.EditLines")) { ProductInfoExt product =
				 * getInputProduct(); addTicketLine(product, 1.0,
				 * product.getPriceSell());
				 */

				/*
				 * do not allow sales without product reference ! // Anadimos 1
				 * producto con precio negativo } else if (cTrans == '-' &&
				 * m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor ==
				 * NUMBERZERO && m_App.getAppUserView().getUser().hasPermission(
				 * "sales.EditLines")) { ProductInfoExt product =
				 * getInputProduct(); addTicketLine(product, 1.0,
				 * -product.getPriceSell());
				 */

				/*
				 * do not allow sales without product reference ! // Anadimos n
				 * productos } else if (cTrans == '+' && m_iNumberStatusInput ==
				 * NUMBERVALID && m_iNumberStatusPor == NUMBERVALID &&
				 * m_App.getAppUserView().getUser().hasPermission(
				 * "sales.EditLines")) { ProductInfoExt product =
				 * getInputProduct(); addTicketLine(product, getPorValue(),
				 * product.getPriceSell());
				 */

				/*
				 * do not allow sales without product reference ! // Anadimos n
				 * productos con precio negativo ? } else if (cTrans == '-' &&
				 * m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor ==
				 * NUMBERVALID &&
				 * m_App.getAppUserView().getUser().hasPermission(
				 * "sales.EditLines")) { ProductInfoExt product =
				 * getInputProduct(); addTicketLine(product, getPorValue(),
				 * -product.getPriceSell());
				 */

				// Totals() Igual;
			} else if (cTrans == ' ' || cTrans == '=') {
				try {
					if (this.m_restaurant != null)
						this.m_restaurant.generateOrder();
				} catch (BasicException e) {
					JConfirmDialog.showError(m_App, this, AppLocal.getIntString("error.network"),
							AppLocal.getIntString("message.cannotprintticket"), e);
				}
				
				// TicketInfo tt = m_oTicket.copyTicket();
				if (closeTicket(m_oTicket, m_oTicketExt)) {
					// Ends edition of current receipt
					// verify booked products
					// this.m_App.getAppUserView().
					try {
						if (this.m_restaurant != null)
							this.m_restaurant.newTicket();

						m_ticketsbag.deleteTicket(false);
					} catch (BasicException e) {
						JConfirmDialog.showError(m_App, this, AppLocal.getIntString("error.network"),
								AppLocal.getIntString("message.cannotprintticket"), e);
					}
				} else {
					// repaint current ticket
					refreshTicket();
				}
			}
		}
	}

	private boolean closeTicket(TicketInfo ticket, Object ticketext) {

		boolean resultok = false;

		if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {

			try {
				// check signature device state
				SignatureModul sig = SignatureModul.getInstance();
				if(sig.GetIsActive() && sig.GetIsOutOfOrder())
				{
					sig.CheckOutageInterval48h(this);
				}
				
				// reset the payment info
				taxeslogic.calculateTaxes(ticket);
				if (ticket.getTotal() >= 0.0) {
					ticket.resetPayments(); // Only reset if is sale
				}

				if (executeEvent(ticket, ticketext, "ticket.total") == null) {

					// Muestro el total
					printTicket("Printer.TicketTotal", ticket, ticketext);

					// Select the Payments information
					JPaymentSelect paymentdialog = ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL
							? paymentdialogreceipt : paymentdialogrefund;
					paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));

					paymentdialog.setTransactionID(ticket.getTransactionID());
					// paymentdialog.setSize(800, 500);

					if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer())) {

						// reset MultiplyClone
						ticket.SetTicketLinesMultiplyCloneInvalid();
						
						// assign the payments selected and calculate taxes.
						ticket.setPayments(paymentdialog.getSelectedPayments());

						// Asigno los valores definitivos del ticket...
						ticket.setUser(m_App.getAppUserView().getUser().getUserInfo());

						if (executeEvent(ticket, ticketext, "ticket.save") == null) {
							// Save the receipt and assign a receipt number
							try {
								ticket.setActiveCash(m_App.getActiveCashIndex(true, true));
								ticket.setDate(new Date());
								
								// set place
								if(ticketext != null 
										&& ticketext.getClass().equals(String.class)
										&& ticket.getLinesCount() > 0)
								{
									String place = ticketext.toString();
									for(TicketLineInfo line : ticket.getLines())
									{
										line.setProperty("Place", place);
									}
								}

								dlSales.saveTicket(ticket, m_App.getInventoryLocation(), taxeslogic);
								resultok = true;

								try {
									executeEvent(ticket, ticketext, "ticket.close",
											new ScriptArg("print", paymentdialog.isPrintSelected()));

									String[] bonsize = m_App.getProperties().getProperty("machine.printer").split(",");
									String ticketsuffix = "";
									if (bonsize.length > 2)
										ticketsuffix = "." + bonsize[2];
									// Print receipt.
									
									int printMultiplier = 1;
									for(PaymentInfo pi : ticket.getPayments())
									{
										if(pi.getName().equals("paperin") || pi.getName().equals("paperout"))
										{
											printMultiplier = 2;
										}
									}
									
									for(int i=1; i <= printMultiplier; i++)
									{
										printTicket(paymentdialog.isPrintSelected() ? "Printer.Ticket" + ticketsuffix
											: "Printer.Ticket2", ticket, ticketext);
									}
								} catch (Exception eData) {
									JConfirmDialog.showError(m_App, this, 
											AppLocal.getIntString("message.cannotprintticket"),
											AppLocal.getIntString("error.network"), 
											eData);
								}

							} catch (SignatureUnitException seData) {
								JConfirmDialog.showError(m_App, this, 
										AppLocal.getIntString("message.signatureunit.error"),
										AppLocal.getIntString("message.nosaveticket"),
										seData);
								
								// check signature unit
								sig.CheckSignatureUnitState(this, false);
							} catch (Exception eData) {
								JConfirmDialog.showError(m_App, this, 
										AppLocal.getIntString("message.databaseconnectionerror"),
										AppLocal.getIntString("message.nosaveticket"),
										eData);
							}
						}
					}
				}
			} catch (TaxesException e) {
				// MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
				// AppLocal.getIntString("message.cannotcalculatetaxes"));
				// msg.show(m_App, this);
				JConfirmDialog.showError(m_App, this, AppLocal.getIntString("message.nosaveticket"),
						AppLocal.getIntString("message.cannotcalculatetaxes"), e);
			}

			// reset the payment info
			m_oTicket.resetTaxes();
			m_oTicket.resetPayments();
		}

		// cancelled the ticket.total script
		// or canceled the payment dialog
		// or canceled the ticket.close script
		return resultok;
	}

	public void printTicket(String sresourcename, TicketInfo ticket, Object ticketext) {

		String sresource = dlSystem.getResourceAsXML(sresourcename);
		if (sresource == null) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
			msg.show(m_App, JPanelTicket.this);
		} else {
			try {
				ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
				script.put("taxes", taxcollection);
				if (taxeslogic != null) {
					script.put("taxeslogic", taxeslogic);
					try {
						taxeslogic.calculateTaxes(ticket);
					} catch (Exception ex) {

					}
				}
				script.put("ticket", ticket);
				script.put("place",
						ticketext != null && ticketext.getClass().equals(String.class) && !ticketext.toString().endsWith("$") 
						? ticketext.toString() : "");
				script.put("host", m_App.getHost());
				
				script.put("SystemDataAddresLine1", SystemDataAddressLine1);
				script.put("SystemDataAddresLine2", SystemDataAddressLine2);
				script.put("SystemDataStreet", SystemDataStreet);
				script.put("SystemDataCity", SystemDataCity);
				script.put("SystemDataTaxid", SystemDataTaxid);
				script.put("SystemDataThanks", SystemDataThanks);
				script.put("SystemDataAccountBank", SystemDataAccountBank);
				script.put("SystemDataAccountOwner", SystemDataAccountOwner);
				script.put("SystemDataAccountBIC", SystemDataAccountBIC);
				script.put("SystemDataAccountIBAN", SystemDataAccountIBAN);

				m_TTP.printTicket(script.eval(sresource).toString());
			} catch (ScriptException e) {
				// MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
				// AppLocal.getIntString("message.cannotprintticket"), e);
				// msg.show(m_App, JPanelTicket.this);

				JConfirmDialog.showError(m_App, this, AppLocal.getIntString("error.network"),
						AppLocal.getIntString("message.cannotprintticket"), e);
			} catch (TicketPrinterException e) {
				// MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
				// AppLocal.getIntString("message.cannotprintticket"), e);
				// msg.show(m_App, JPanelTicket.this);
				JConfirmDialog.showError(m_App, this, AppLocal.getIntString("error.network"),
						AppLocal.getIntString("message.cannotprintticket"), e);
			}
		}
	}

	private void printReport(String resourcefile, TicketInfo ticket, Object ticketext) {

		try {

			JasperReport jr;

			InputStream in = getClass().getResourceAsStream(resourcefile + ".ser");
			if (in == null) {
				// read and compile the report
				JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
				jr = JasperCompileManager.compileReport(jd);
			} else {
				// read the compiled reporte
				ObjectInputStream oin = new ObjectInputStream(in);
				jr = (JasperReport) oin.readObject();
				oin.close();
			}

			// Construyo el mapa de los parametros.
			Map<String,Object> reportparams = new HashMap<String,Object>();
			// reportparams.put("ARG", params);
			try {
				reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
			} catch (MissingResourceException e) {
			}
			reportparams.put("TAXESLOGIC", taxeslogic);

			Map<String, Object> reportfields = new HashMap<String, Object>();
			reportfields.put("TICKET", ticket);
			reportfields.put("PLACE",
					ticketext != null && ticketext.getClass().equals(String.class) ? ticketext.toString() : "");

			JasperPrint jp = JasperFillManager.fillReport(jr, reportparams,
					new JRMapArrayDataSource(new Object[] { reportfields }));

			PrintService service = ReportUtils
					.getPrintService(m_App.getProperties().getProperty("machine.printername"));

			JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

		} catch (Exception e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"),
					e);
			msg.show(m_App, this);
		}
	}

	private void visorTicketLine(TicketLineInfo oLine, TicketLineInfo oOrigLine, Boolean isEditOperation) {
		if (oLine == null) {
			m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
		} else {
			try {
				ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
				script.put("ticketline", oLine);
				script.put("ticketlineOrig", oOrigLine);
				script.put("ticketlineEdit", isEditOperation);
				script.put("place", 
						m_oTicketExt != null && m_oTicketExt.getClass().equals(String.class) // && !m_oTicketExt.toString().endsWith("$") 
						? m_oTicketExt.toString() : "");
				script.put("host", m_App.getHost());
				
				m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintline"), e);
				msg.show(m_App, JPanelTicket.this);
			} catch (TicketPrinterException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintline"), e);
				msg.show(m_App, JPanelTicket.this);
			}
		}
	}

	private Object evalScript(ScriptObject scr, String resource, ScriptArg... args) {

		// resource here is guaratied to be not null
		try {
			scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
			return scr.evalScript(dlSystem.getResourceAsXML(resource), args);
		} catch (ScriptException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e);
			msg.show(m_App, this);
			return msg;
		}
	}

	public void evalScriptAndRefresh(String resource, ScriptArg... args) {

		if (resource == null) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
			msg.show(m_App, this);
		} else {
			ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
			scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
			evalScript(scr, resource, args);
			refreshTicket();
			setSelectedIndex(scr.getSelectedIndex());
		}
	}

	public void printTicket(String resource) {
		m_oTicket.SetTicketLinesMultiplyCloneInvalid();
		printTicket(resource, m_oTicket, m_oTicketExt);
	}

	private Object executeEventAndRefresh(String eventkey, ScriptArg... args) {

		Object result = null;
		for(int i=-1; i<=1; i++)
		{	
			String s = "";
			if(i!=0)
				s = String.format(".%d", i);
			String resource = m_jbtnconfig.getEvent(eventkey + s);
			if (resource != null) {
				ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
				scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
				result = evalScript(scr, resource, args);
				refreshTicket();
				
				setSelectedIndex(scr.getSelectedIndex());
			}
		}
		return result;
	}

	private Object executeEvent(TicketInfo ticket, Object ticketext, String eventkey, ScriptArg... args) {

		String resource = m_jbtnconfig.getEvent(eventkey);
		if (resource == null) {
			return null;
		} else {
			ScriptObject scr = new ScriptObject(ticket, ticketext);
			return evalScript(scr, resource, args);
		}
	}

	public String getResourceAsXML(String sresourcename) {
		return dlSystem.getResourceAsXML(sresourcename);
	}

	public BufferedImage getResourceAsImage(String sresourcename) {
		return dlSystem.getResourceAsImage(sresourcename);
	}

	private void setSelectedIndex(int i) {

		if (i >= 0 && i < m_oTicket.getLinesCount()) {
			m_ticketlines.setSelectedIndex(i);
		} else if (m_oTicket.getLinesCount() > 0) {
			m_ticketlines.setSelectedIndex(m_oTicket.getLinesCount() - 1);
		}
	}

	public static class ScriptArg {
		private String key;
		private Object value;

		public ScriptArg(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}

	public class ScriptObject {

		private TicketInfo ticket;
		private Object ticketext;

		private int selectedindex;

		private ScriptObject(TicketInfo ticket, Object ticketext) {
			this.ticket = ticket;
			this.ticketext = ticketext;
		}

		public double getInputValue() {
			if (m_iNumberStatusInput == NUMBERVALID) {
				return JPanelTicket.this.getInputValue();
			} else {
				return 0.0;
			}
		}

		public double getPorValue() {
			if (m_iNumberStatusPor == NUMBERVALID) {
				return JPanelTicket.this.getPorValue();
			} else {
				return 0.0;
			}
		}

		public int getSelectedIndex() {
			return selectedindex;
		}

		public void setSelectedIndex(int i) {
			selectedindex = i;
		}

		public void printReport(String resourcefile) {
			JPanelTicket.this.printReport(resourcefile, ticket, ticketext);
		}

		public void printTicket(String sresourcename) {
			JPanelTicket.this.printTicket(sresourcename, ticket, ticketext);
		}

		public Object evalScript(String code, ScriptArg... args) throws ScriptException {

			ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
			script.put("ticket", ticket);
			script.put("place",
					ticketext != null && ticketext.getClass().equals(String.class) // && !ticketext.toString().endsWith("$") 
					? ticketext.toString() : "");
			script.put("host", m_App.getHost());
			
			script.put("taxes", taxcollection);
			script.put("taxeslogic", taxeslogic);
			script.put("user", m_App.getAppUserView().getUser());
			script.put("sales", this);
			script.put("app", m_App.getAppUserView());

			// more arguments
			for (ScriptArg arg : args) {
				script.put(arg.getKey(), arg.getValue());
			}

			return script.eval(code);
		}
	}

	private void ScaleButtons() {
		// font size
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTotalEuros, "common-large-fontsize", "64");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblTotalEuros1, "common-large-fontsize", "64");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jPrice, "sales-amountprice-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jPor, "sales-amountprice-fontsize", "32");
		PropertyUtil.ScaleButtonFontsize(m_App, m_jTicketId, "sales-ticketid-fontsize", "32");

		int fontsizeSmall = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		m_jNumberKeys.ScaleButtons(Integer.parseInt(m_jbtnconfig.getProperty("button-touchsmall-width", "48")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchsmall-height", "48")));

		PropertyUtil.ScaleButtonIcon(m_jEnter,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchsmall-width", "48")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchsmall-height", "48")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(btnCustomer,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(btnSplit,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(m_jDelete,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(m_jEditLine,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(jEditAttributes,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		PropertyUtil.ScaleButtonIcon(m_jList,
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-width", "60")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-touchlarge-height", "60")), fontsizeSmall);

		m_ticketsbag.ScaleButtons();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		m_jPanContainer = new javax.swing.JPanel();
		m_jOptions = new javax.swing.JPanel();
		m_jButtons = new javax.swing.JPanel();
		m_jTicketId = new javax.swing.JButton();
		btnCustomer = new javax.swing.JButton();
		btnSplit = new javax.swing.JButton();
		m_jPanelScripts = new javax.swing.JPanel();
		m_jButtonsExt = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		m_jPanelBag = new javax.swing.JPanel();
		m_jPanTicket = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		// m_jUp = new javax.swing.JButton();
		// m_jDown = new javax.swing.JButton();
		m_jDelete = new javax.swing.JButton();
		m_jList = new javax.swing.JButton();
		m_jEditLine = new javax.swing.JButton();
		jEditAttributes = new javax.swing.JButton();
		m_jPanelCentral = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		m_jPanTotals = new javax.swing.JPanel();
		m_jTotalEuros = new javax.swing.JLabel();
		m_jLblTotalEuros1 = new javax.swing.JLabel();
		m_jContEntries = new javax.swing.JPanel();
		m_jPanEntries = new javax.swing.JPanel();
		m_jNumberKeys = new com.openbravo.beans.JNumberKeys(m_App);
		jPanel9 = new javax.swing.JPanel();
		m_jPrice = new javax.swing.JLabel();
		m_jPor = new javax.swing.JLabel();
		m_jEnter = new javax.swing.JButton();
		m_jTax = new javax.swing.JComboBox();
		m_jaddtax = new javax.swing.JToggleButton();
		m_jKeyFactory = new javax.swing.JTextField();
		catcontainer = new javax.swing.JPanel();

		// setBackground(new java.awt.Color(255, 204, 153));
		setLayout(new java.awt.CardLayout());

		m_jPanContainer.setLayout(new java.awt.BorderLayout());

		m_jOptions.setLayout(new java.awt.BorderLayout());

		// btnCustomer.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png")));
		// // NOI18N
		btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png"))); // NOI18N
		btnCustomer.setText(AppLocal.getIntString("label.customer"));
		btnCustomer.setFocusPainted(false);
		btnCustomer.setFocusable(false);
		btnCustomer.setMargin(new java.awt.Insets(0, 0, 0, 0));
		btnCustomer.setRequestFocusEnabled(false);
		btnCustomer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCustomerActionPerformed(evt);
			}
		});
		m_jButtons.add(btnCustomer);

		// btnSplit.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editcut.png")));
		// // NOI18N
		btnSplit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/splitTicket.png"))); // NOI18N
		btnSplit.setText(AppLocal.getIntString("caption.split"));
		btnSplit.setFocusPainted(false);
		btnSplit.setFocusable(false);
		btnSplit.setMargin(new java.awt.Insets(0, 0, 0, 0));
		btnSplit.setRequestFocusEnabled(false);
		btnSplit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSplitActionPerformed(evt);
			}
		});
		m_jButtons.add(btnSplit);

		m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

		m_jPanelScripts.setLayout(new java.awt.BorderLayout());

		m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));
		m_jButtonsExt.add(jPanel1);

		m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

		m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

		m_jPanelBag.setLayout(new java.awt.BorderLayout());
		m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

		m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

		m_jPanTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		m_jPanTicket.setLayout(new java.awt.BorderLayout());

		jPanel5.setLayout(new java.awt.BorderLayout());

		jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jPanel2.setLayout(new java.awt.GridBagLayout());
		GridBagConstraints layoutData = new GridBagConstraints();

		// m_jUp.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png")));
		// // NOI18N
		// // // NOI18N
		// m_jUp.setFocusPainted(false);
		// m_jUp.setFocusable(false);
		// m_jUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jUp.setRequestFocusEnabled(false);
		// m_jUp.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// m_jUpActionPerformed(evt);
		// }
		// });
		// jPanel2.add(m_jUp);
		//
		// m_jDown.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png")));
		// // NOI18N
		// // // NOI18N
		// m_jDown.setFocusPainted(false);
		// m_jDown.setFocusable(false);
		// m_jDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jDown.setRequestFocusEnabled(false);
		// m_jDown.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// m_jDownActionPerformed(evt);
		// }
		// });
		// jPanel2.add(m_jDown);

		m_jDelete.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		m_jDelete.setFocusPainted(false);
		m_jDelete.setFocusable(false);
		m_jDelete.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jDelete.setRequestFocusEnabled(false);
		m_jDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jDeleteActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 0, 0, GridBagConstraints.NONE);
		jPanel2.add(m_jDelete, layoutData);

		m_jList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
		m_jList.setFocusPainted(false);
		m_jList.setFocusable(false);
		m_jList.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jList.setRequestFocusEnabled(false);
		m_jList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jListActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 0, 1, GridBagConstraints.NONE);
		jPanel2.add(m_jList, layoutData);

		m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
		m_jEditLine.setFocusPainted(false);
		m_jEditLine.setFocusable(false);
		// m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_jEditLine.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jEditLine.setRequestFocusEnabled(false);
		m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jEditLineActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 0, 2, GridBagConstraints.NONE);
		jPanel2.add(m_jEditLine, layoutData);

		jEditAttributes
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/colorize25.png"))); // NOI18N
		jEditAttributes.setFocusPainted(false);
		jEditAttributes.setFocusable(false);
		// jEditAttributes.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_jEditLine.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jEditAttributes.setRequestFocusEnabled(false);
		jEditAttributes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jEditAttributesActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 0, 3, GridBagConstraints.NONE);
		jPanel2.add(jEditAttributes, layoutData);

		jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

		m_jPanTicket.add(jPanel5, java.awt.BorderLayout.LINE_END);

		m_jPanelCentral.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new java.awt.BorderLayout());

		m_jPanTotals.setLayout(new java.awt.GridBagLayout());

		m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
		m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTotalEuros.setOpaque(true);
		m_jTotalEuros.setRequestFocusEnabled(false);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);

		m_jPanTotals.add(m_jTotalEuros, gridBagConstraints);

		m_jLblTotalEuros1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
		// m_jPanTotals.add(m_jLblTotalEuros1, gridBagConstraints);

		m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		m_jTicketId.setOpaque(false);
		m_jTicketId.setContentAreaFilled(false);
		m_jTicketId.setFont(new Font(m_jTicketId.getFont().getName(), Font.PLAIN, 16));
		m_jTicketId.setRequestFocusEnabled(false);
		m_jTicketId.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(m_oTicket != null)
				{	
					OnScreenKeyboardUtil.StartOSK();
					String value = javax.swing.JOptionPane.showInputDialog(AppLocal.getIntString("ticket.comment"), m_oTicket.getTempComment());
					m_oTicket.setTempComment(value);
					refreshTicket();
				}
				
			}
		});

		jPanel4.add(m_jTicketId, java.awt.BorderLayout.LINE_START);

		jPanel4.add(m_jPanTotals, java.awt.BorderLayout.LINE_END);
		jPanel4.setComponentZOrder(m_jPanTotals, 0);
		jPanel4.setComponentZOrder(m_jTicketId, 1);
		m_jPanelCentral.add(jPanel4, java.awt.BorderLayout.SOUTH);
		m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);
		m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);
		m_jContEntries.setLayout(new java.awt.BorderLayout());
		m_jPanEntries.setLayout(new javax.swing.BoxLayout(m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

		m_jNumberKeys.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
			public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
				m_jNumberKeysKeyPerformed(evt);
			}
		});
		

		jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel9.setLayout(new java.awt.GridBagLayout());

		m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jPrice.setOpaque(true);

		m_jPrice.setRequestFocusEnabled(false);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.weighty = 1.0;

		jPanel9.add(m_jPrice, gridBagConstraints);

		m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jPor.setOpaque(true);

		m_jPor.setRequestFocusEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

		gridBagConstraints.weightx = 2.0;
		gridBagConstraints.weighty = 1.0;

		// gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);

		jPanel9.add(m_jPor, gridBagConstraints);

		// m_jEnter.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode.png")));
		// // NOI18N
		m_jEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode1.png"))); // NOI18N
		m_jEnter.setFocusPainted(false);
		m_jEnter.setFocusable(false);
		m_jEnter.setRequestFocusEnabled(false);
		m_jEnter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jEnterActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		// gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		jPanel9.add(m_jEnter, gridBagConstraints);

		m_jTax.setFocusable(false);
		m_jTax.setRequestFocusEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.weighty = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		// jPanel9.add(m_jTax, gridBagConstraints);

		m_jaddtax.setText("+");
		m_jaddtax.setFocusPainted(false);
		m_jaddtax.setFocusable(false);
		m_jaddtax.setRequestFocusEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.weighty = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		// jPanel9.add(m_jaddtax, gridBagConstraints);

		m_jPanEntries.add(jPanel9);
		m_jPanEntries.add(m_jNumberKeys);

		m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
		m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
		m_jKeyFactory.setBorder(null);
		m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
		m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
		m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				m_jKeyFactoryKeyTyped(evt);
			}
		});
		m_jPanEntries.add(m_jKeyFactory);

		m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.NORTH);

		m_jPanContainer.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

		catcontainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		catcontainer.setLayout(new java.awt.BorderLayout());
		m_jPanContainer.add(catcontainer, java.awt.BorderLayout.SOUTH);

		add(m_jPanContainer, "ticket");
	}// </editor-fold>//GEN-END:initComponents

	private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jEditLineActionPerformed

		int i = m_ticketlines.getSelectedIndex();
		if (i < 0) {
			Toolkit.getDefaultToolkit().beep(); // no line selected
		} else {
			try {
				TicketLineInfo newline = JProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
				if (newline != null) {
					// line has been modified
					paintTicketLine(i, newline);
				}
			} catch (BasicException e) {
				new MessageInf(e).show(m_App, this);
			}
		}

	}// GEN-LAST:event_m_jEditLineActionPerformed

	private void m_jEnterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jEnterActionPerformed

		stateTransition('\n');

	}// GEN-LAST:event_m_jEnterActionPerformed

	private void m_jNumberKeysKeyPerformed(com.openbravo.beans.JNumberEvent evt) {// GEN-FIRST:event_m_jNumberKeysKeyPerformed

		stateTransition(evt.getKey());

	}// GEN-LAST:event_m_jNumberKeysKeyPerformed

	private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_m_jKeyFactoryKeyTyped

		m_jKeyFactory.setText(null);
		stateTransition(evt.getKeyChar());

	}// GEN-LAST:event_m_jKeyFactoryKeyTyped

	private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDeleteActionPerformed
		
		
		int i = m_ticketlines.getSelectedIndex();
		if (i < 0) {
			Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
		} else {
			TicketLineInfo line = m_oTicket.getLine(i);
			int res = JOptionPane.YES_OPTION;
			
			if(line.getMultiply() != 0)
				res = JConfirmDialog.showConfirm(m_App, this,
						null,
						String.format(AppLocal.getIntString("message.wannadeleteline"),line.getMultiply(), line.getProductName()));
			
			if (res == JOptionPane.YES_OPTION) {
				removeTicketLine(i); // elimino la linea
			}
		}

	}// GEN-LAST:event_m_jDeleteActionPerformed

	private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jUpActionPerformed

		m_ticketlines.selectionUp();

	}// GEN-LAST:event_m_jUpActionPerformed

	private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDownActionPerformed

		m_ticketlines.selectionDown();

	}// GEN-LAST:event_m_jDownActionPerformed

	private void m_jListActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jListActionPerformed

		ProductInfoExt prod = JProductFinder.showMessage(m_App, JPanelTicket.this, dlSales);
		if (prod != null) {
			buttonTransition(prod);
		}

	}// GEN-LAST:event_m_jListActionPerformed

	private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed

		JCustomerFinder finder = JCustomerFinder.getCustomerFinder(m_App, this, dlCustomers);
		finder.search(m_oTicket.getCustomer());
		finder.setVisible(true);

		try {
			m_oTicket.setCustomer(finder.getSelectedCustomer() == null ? null
					: dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()));
		} catch (BasicException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"),
					e);
			msg.show(m_App, this);
		}

		refreshTicket();

	}// GEN-LAST:event_btnCustomerActionPerformed

	private void btnSplitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSplitActionPerformed

		if (this.m_restaurant != null)
		{
			try {
				this.m_restaurant.generateOrder();
			} catch(Exception ex) {
				JConfirmDialog.showError(m_App, JPanelTicket.this,
						AppLocal.getIntString("error.network"),
						AppLocal.getIntString("message.databaseconnectionerror"), ex);
			}
		}
		
		if (m_oTicket.getLinesCount() > 0) {
			try {
				ReceiptSplit splitdialog = ReceiptSplit.getDialog(m_App, this, dlSystem.getResourceAsXML("Ticket.Line"),
						dlSales, dlCustomers, taxeslogic);

				TicketInfo ticket1 = m_oTicket.copyTicket();
				TicketInfo ticket2 = new TicketInfo();
				ticket2.setCustomer(m_oTicket.getCustomer());

				// splitdialog.
				if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {
					Object currentTicket = splitdialog.getTicketText();
					String currentTicketId = splitdialog.getTicketId();
					if (splitdialog.isReceipt()) {
						if (closeTicket(ticket2, currentTicket)) {
							// Ends edition of current receipt
							if (ticket1.getLinesCount() > 0)
							{
								if (this.m_restaurant != null)
									ticket1.SetTicketLinesMultiplyClone();
									
								setActiveTicket(ticket1, m_oTicketExt);
							}
							else
								m_ticketsbag.deleteTicket(false);
						} else {
							// repaint current ticket
							refreshTicket();
						}

					} else {
						// now we move lines to the selected Table
						// check if different place is selected
						if(currentTicketId != null) 
						{
							try {
								ticket2.getName(currentTicket);
								ticket2.setTempComment(ticket1.getTempComment());
								dlReceipts.insertSharedTicket(currentTicketId, ticket2);
								
								if (this.m_restaurant != null)
									ticket1.SetTicketLinesMultiplyClone();
								
								setActiveTicket(ticket1, m_oTicketExt);
							} catch (BasicException e) {
								// insert was not possible, so try to perform an
								// update
								try {
									// first read all booked elements
									String lockBy = dlReceipts.checkoutSharedTicket(currentTicketId);
									if (lockBy == null) {
	
										TicketInfo dummy = dlReceipts.getSharedTicket(currentTicketId);
										for (TicketLineInfo info : dummy.getLines()) {
											// does info exists?
											TicketLineInfo inf = null;
											for (TicketLineInfo lni : ticket2.getLines()) {
												if (lni.getProductID().compareTo(info.getProductID()) == 0 
														&& lni.getPrice() == info.getPrice()
														&& (lni.getProductAttSetInstDesc() == null ? "" : lni.getProductAttSetInstDesc()).equals(info.getProductAttSetInstDesc() == null ? "" : info.getProductAttSetInstDesc())
												) {
												//if (lni.getProductID().compareTo(info.getProductID()) == 0) {
													inf = lni;
													break;
												}
											}
											if (inf != null) {
												inf.setMultiply(inf.getMultiply() + info.getMultiply());
											} else {
												ticket2.addLine(info);
											}
										}
										dlReceipts.updateSharedTicket(currentTicketId, ticket2);
										dlReceipts.checkinSharedTicket(currentTicketId);
										
										if (this.m_restaurant != null)
											ticket1.SetTicketLinesMultiplyClone();
										
										setActiveTicket(ticket1, m_oTicketExt);
									} else {
										JConfirmDialog.showError(m_App, JPanelTicket.this,
												AppLocal.getIntString("error.error"),
												AppLocal.getIntString("message.placeLocked") + " (" + lockBy + ")");
	
										refreshTicket();
									}
								} catch (BasicException ex) {
									JConfirmDialog.showError(m_App, JPanelTicket.this,
											AppLocal.getIntString("error.network"),
											AppLocal.getIntString("message.databaseconnectionerror"), ex);
	
									setActiveTicket(ticket1, m_oTicketExt);
								}
							}
						}
						else
						{
							refreshTicket();
						}
					}
				}
			} catch (BasicException e1) {
				JConfirmDialog.showError(m_App, JPanelTicket.this, AppLocal.getIntString("error.network"),
						AppLocal.getIntString("message.databaseconnectionerror"), e1);
			}
		}
	}// GEN-LAST:event_btnSplitActionPerformed

	private void jEditAttributesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jEditAttributesActionPerformed

		int i = m_ticketlines.getSelectedIndex();
		if (i < 0) {
			Toolkit.getDefaultToolkit().beep(); // no line selected
		} else {
			try {
				TicketLineInfo line = m_oTicket.getLine(i);
				JProductAttEdit attedit = JProductAttEdit.getAttributesEditor(m_App, this, m_App.getSession());
				attedit.editAttributes(line.getProductAttSetId(), line.getProductAttSetInstId(),
						line.getMultiply() > 1);
				attedit.scaleFont(Integer.parseInt(m_jbtnconfig.getProperty("common-dialog-fontsize", "22")));
				attedit.setVisible(true);
				if (attedit.isOK()) {
					// The user pressed OK
					// check amount
					// if amount > 1 add new line
					if (line.getMultiply() > 1 && attedit.isForSingleProduct) {
						line.setMultiply(line.getMultiply() - 1);
						paintTicketLine(i, line);

						TicketLineInfo newLine = line.copyTicketLine();
						newLine.setMultiply(1);
						newLine.setProductAttSetInstId(attedit.getAttributeSetInst());
						newLine.setProductAttSetInstDesc(attedit.getAttributeSetInstDescription());
						addTicketLine(newLine);

					} else {
						line.setProductAttSetInstId(attedit.getAttributeSetInst());
						line.setProductAttSetInstDesc(attedit.getAttributeSetInstDescription());
						paintTicketLine(i, line);
					}
				}
			} catch (BasicException ex) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotfindattributes"), ex);
				msg.show(m_App, this);
			}
		}

	}// GEN-LAST:event_jEditAttributesActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCustomer;
	private javax.swing.JButton btnSplit;
	private javax.swing.JPanel catcontainer;
	private javax.swing.JButton jEditAttributes;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JPanel m_jButtons;
	private javax.swing.JPanel m_jButtonsExt;
	private javax.swing.JPanel m_jContEntries;
	private javax.swing.JButton m_jDelete;
	// private javax.swing.JButton m_jDown;
	private javax.swing.JButton m_jEditLine;
	private javax.swing.JButton m_jEnter;
	private javax.swing.JTextField m_jKeyFactory;
	private javax.swing.JLabel m_jLblTotalEuros1;
	private javax.swing.JButton m_jList;
	private com.openbravo.beans.JNumberKeys m_jNumberKeys;
	private javax.swing.JPanel m_jOptions;
	private javax.swing.JPanel m_jPanContainer;
	private javax.swing.JPanel m_jPanEntries;
	private javax.swing.JPanel m_jPanTicket;
	private javax.swing.JPanel m_jPanTotals;
	private javax.swing.JPanel m_jPanelBag;
	private javax.swing.JPanel m_jPanelCentral;
	private javax.swing.JPanel m_jPanelScripts;
	private javax.swing.JLabel m_jPor;
	private javax.swing.JLabel m_jPrice;
	private javax.swing.JComboBox m_jTax;
	private javax.swing.JButton m_jTicketId;
	private javax.swing.JLabel m_jTotalEuros;
	// private javax.swing.JButton m_jUp;
	private javax.swing.JToggleButton m_jaddtax;

	private DataLogicReceipts dlReceipts = null;
	// End of variables declaration//GEN-END:variables

}
