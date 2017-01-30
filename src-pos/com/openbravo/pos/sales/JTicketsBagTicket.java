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

import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.*;
import com.openbravo.basic.BasicException;
import com.openbravo.basic.SignatureUnitException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.pos.admin.DataLogicAdmin;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.panels.JTicketsFinder;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.TaxInfo;

public class JTicketsBagTicket extends JTicketsBag {

	private DataLogicSystem m_dlSystem = null;
	protected DataLogicCustomers dlCustomers = null;
	protected DataLogicSales dlSales;

	private DeviceTicket m_TP;
	private TicketParser m_TTP;
	private TicketParser m_TTP2;

	private TicketInfo m_ticket;
	private TicketInfo m_ticketCopy;

	private JTicketsBagTicketBag m_TicketsBagTicketBag;

	private JPanelTicketEdits m_panelticketedit;

	private SentenceList senttax;
	private ListKeyed taxcollection;
	private TaxesLogic taxeslogic;

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
	
	/** Creates new form JTicketsBagTicket */
	public JTicketsBagTicket(AppView app, JPanelTicketEdits panelticket) {
		super(app, panelticket);
		m_panelticketedit = panelticket;
		m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
		dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
		dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

		// Inicializo la impresora...
		m_TP = new DeviceTicket(app);

		// Inicializo el parser de documentos de ticket
		m_TTP = new TicketParser(m_TP, m_dlSystem); // para visualizar el ticket
		m_TTP2 = new TicketParser(m_App.getDeviceTicket(), m_dlSystem); // para
																		// imprimir
																		// el
																		// ticket
		senttax = dlSales.getTaxList();
		try {
			java.util.List<TaxInfo> taxlist = senttax.list();
			taxcollection = new ListKeyed<TaxInfo>(taxlist);
			taxeslogic = new TaxesLogic(taxlist);
		} catch (BasicException bex) {
			taxcollection = null;
			taxeslogic = null;
		}

		initComponents();

		m_TicketsBagTicketBag = new JTicketsBagTicketBag(this);

		m_jTicketEditor.addEditorKeys(m_jKeys);

		// Este deviceticket solo tiene una impresora, la de pantalla
		m_jPanelTicket.add(m_TP.getDevicePrinter("1").getPrinterComponent(), BorderLayout.CENTER);

		m_jKeys.ScaleButtons();
	}

	public void activate() {

		m_jEdit.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.EditTicket"));
		m_jRefund.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.RefundTicket"));
		m_jPrint.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.PrintTicket"));

		// precondicion es que no tenemos ticket activado ni ticket en el panel

		m_ticket = null;
		m_ticketCopy = null;

		printTicket();

		m_jTicketEditor.reset();
		m_jTicketEditor.activate();

		m_panelticketedit.setActiveTicket(null, null);

		jrbTicketId.setSelected(true);

		// postcondicion es que tenemos ticket activado aqui y ticket en el
		// panel
	}

	public boolean deactivate() {

		// precondicion es que tenemos ticket activado aqui y ticket en el panel
		m_ticket = null;
		m_ticketCopy = null;
		return true;
		// postcondicion es que no tenemos ticket activado ni ticket en el panel
	}

	public void deleteTicket(boolean deletTicket) {

		if (m_ticketCopy != null) {
			// Para editar borramos el ticket anterior
			try {
				m_dlSales.deleteTicket(m_ticketCopy, m_App.getInventoryLocation());
			} catch (SignatureUnitException seData) {
				MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"),
						seData);
				msg.show(m_App, this);
			} catch (BasicException eData) {
				MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"),
						eData);
				msg.show(m_App, this);
			}
		}

		m_ticket = null;
		m_ticketCopy = null;
		resetToTicket();
	}

	public void canceleditionTicket() {

		m_ticketCopy = null;
		resetToTicket();
	}

	private void resetToTicket() {
		printTicket();
		m_jTicketEditor.reset();
		m_jTicketEditor.activate();
		m_panelticketedit.setActiveTicket(null, null);
	}

	protected JComponent getBagComponent() {
		return m_TicketsBagTicketBag;
	}

	protected JComponent getNullComponent() {
		return this;
	}

	private void readTicket(int iTicketid, Boolean isCashTicketId) {

		try {
			TicketInfo ticket = (iTicketid == -1) 
					? m_dlSales.loadTicket(isCashTicketId, m_jTicketEditor.getValueInteger(), taxeslogic)
					: m_dlSales.loadTicket(isCashTicketId, iTicketid, taxeslogic);
			
			if (ticket == null) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.notexiststicket"));
				msg.show(m_App, this);
			} else {
				m_ticket = ticket;
				m_ticketCopy = null; // se asigna al pulsar el boton de editar o
										// devolver
				printTicket();
			}

		} catch (Exception e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"),
					e);
			msg.show(m_App, this);
		}

		m_jTicketEditor.reset();
		m_jTicketEditor.activate();
	}

	private void printTicket() {

		// imprimo m_ticket

		try {
			m_jEdit.setEnabled(m_ticket != null
					&& (m_ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL
							|| m_ticket.getTicketType() == TicketInfo.RECEIPT_REFUND)
					&& m_dlSales.isCashActive(m_ticket.getActiveCash()));
		} catch (BasicException e) {
			m_jEdit.setEnabled(false);
		}

		m_jRefund.setEnabled(m_ticket != null && m_ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL);
		m_jPrint.setEnabled(m_ticket != null);

		// Este deviceticket solo tiene una impresora, la de pantalla
		m_TP.getDevicePrinter("1").reset();

		if (m_ticket == null) {
			m_jTicketId.setText(null);
		} else {
			m_jTicketId.setText(m_ticket.getName());

			try {
				ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
				if (taxcollection != null)
					script.put("taxes", taxcollection);
				if (taxeslogic != null) {
					script.put("taxeslogic", taxeslogic);
					try {
						taxeslogic.calculateTaxes(m_ticket);
					} catch (Exception ex) {

					}
				}

				//** get selected printer size
				
				
				
				
				script.put("ticket", m_ticket);
				
				if(m_ticket.getLinesCount() > 0)
				{
					String place = m_ticket.getLine(0).getProperty("Place");
					script.put("place", place == null ? "" : place);
				}
				else
				{
					script.put("place", ""); // put empty place
				}
				
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
				
				String []bonsize = m_App.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if(bonsize.length > 2)
					ticketsuffix = "."+bonsize[2];
				
				String asxml = m_dlSystem.getResourceAsXML("Printer.TicketPreview" + ticketsuffix);
				Object o = script.eval(asxml);
				m_TTP.printTicket(o.toString());
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (TicketPrinterException eTP) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), eTP);
				msg.show(m_App, this);
			}
		}
	}

	private void initSystemData() {
		DataLogicAdmin dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin"); 
        TableDefinition tresources = dlAdmin.getTableResources();
        
        try {
			List res = tresources.getListSentence().list();
			Object o = res.get(0);
			// try to find System.AddressLine1
			for(int i = 0; i < res.size(); i++) {
				if("System.AddressLine1".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAddressLine1 = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					
					continue;
				} else if("System.AddressLine2".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAddressLine2 = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.Street".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataStreet = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.City".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataCity = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.TAXID".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataTaxid = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.Thanks".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataThanks = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
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
			//res.get(0);
		} catch (BasicException e) {
			Log.Exception(e);
		}
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		
		initSystemData();
		
		java.awt.GridBagConstraints gridBagConstraints;

		buttonGroup1 = new javax.swing.ButtonGroup();
		m_jOptions = new javax.swing.JPanel();
		m_jButtons = new javax.swing.JPanel();
		m_jTicketId = new javax.swing.JLabel();
		jButton2 = new javax.swing.JButton();
		m_jEdit = new javax.swing.JButton();
		m_jRefund = new javax.swing.JButton();
		m_jPrint = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		m_jPanelTicket = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		m_jKeys = new com.openbravo.editor.JEditorKeys(m_App);
		jPanel5 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		m_jTicketEditor = new com.openbravo.editor.JEditorIntegerPositive();
		jPanel1 = new javax.swing.JPanel();
		jrbTicketId = new javax.swing.JRadioButton();
		jrbCashTicketId = new javax.swing.JRadioButton();

		setLayout(new java.awt.BorderLayout());

		m_jOptions.setLayout(new java.awt.BorderLayout());

		m_jButtons.setLayout(new java.awt.GridBagLayout());
		GridBagConstraints layoutData = new GridBagConstraints();

		m_jTicketId.setBackground(java.awt.Color.white);
		m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTicketId.setOpaque(true);
		// m_jTicketId.setPreferredSize(new java.awt.Dimension(160, -1));
		m_jTicketId.setRequestFocusEnabled(false);
		PropertyUtil.setGridBagConstraints(layoutData, 0, 0, GridBagConstraints.NONE);
		m_jButtons.add(m_jTicketId, layoutData);

		jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
		jButton2.setText(AppLocal.getIntString("label.search")); // NOI18N
		jButton2.setFocusPainted(false);
		jButton2.setFocusable(false);
		jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jButton2.setRequestFocusEnabled(false);

		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 1, 0, GridBagConstraints.NONE);
		m_jButtons.add(jButton2, layoutData);

		m_jRefund.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/inbox.png"))); // NOI18N
		m_jRefund.setText(AppLocal.getIntString("button.refund")); // NOI18N
		m_jRefund.setFocusPainted(false);
		m_jRefund.setFocusable(false);
		m_jRefund.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jRefund.setRequestFocusEnabled(false);
		m_jRefund.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jRefundActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 2, 0, GridBagConstraints.NONE);
		m_jButtons.add(m_jRefund, layoutData);

		m_jPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
		m_jPrint.setText(AppLocal.getIntString("button.print")); // NOI18N
		m_jPrint.setFocusPainted(false);
		m_jPrint.setFocusable(false);
		m_jPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jPrint.setRequestFocusEnabled(false);
		m_jPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jPrintActionPerformed(evt);
			}
		});
		PropertyUtil.setGridBagConstraints(layoutData, 3, 0, GridBagConstraints.NONE);
		m_jButtons.add(m_jPrint, layoutData);

		m_jOptions.add(m_jButtons, java.awt.BorderLayout.WEST);

		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		m_jOptions.add(jPanel2, java.awt.BorderLayout.CENTER);

		add(m_jOptions, java.awt.BorderLayout.NORTH);

		m_jPanelTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jPanelTicket.setLayout(new java.awt.BorderLayout());
		add(m_jPanelTicket, java.awt.BorderLayout.CENTER);

		jPanel3.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

		m_jKeys.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jKeysActionPerformed(evt);
			}
		});
		jPanel4.add(m_jKeys);

		jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel5.setLayout(new java.awt.GridBagLayout());

		jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
		jButton1.setFocusPainted(false);
		jButton1.setFocusable(false);
		jButton1.setMargin(new java.awt.Insets(8, 14, 8, 14));
		jButton1.setRequestFocusEnabled(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		jPanel5.add(jButton1, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		jPanel5.add(m_jTicketEditor, gridBagConstraints);

		jPanel4.add(jPanel5);

		jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

		jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

		buttonGroup1.add(jrbTicketId);
		jrbTicketId.setText(AppLocal.getIntString("label.tickets")); // NOI18N
		jrbTicketId.setFocusPainted(false);
		jrbTicketId.setFocusable(false);
		jrbTicketId.setRequestFocusEnabled(false);
		jPanel1.add(jrbTicketId);

		buttonGroup1.add(jrbCashTicketId);
		jrbCashTicketId.setText(AppLocal.getIntString("label.cashtickets")); // NOI18N
		jrbCashTicketId.setFocusPainted(false);
		jrbCashTicketId.setFocusable(false);
		jrbCashTicketId.setRequestFocusEnabled(false);
		jPanel1.add(jrbCashTicketId);

		jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

		add(jPanel3, java.awt.BorderLayout.EAST);
	}// </editor-fold>//GEN-END:initComponents

	public void ScaleButtons() {

		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "60"));
		//

		PropertyUtil.ScaleEditnumbersFontsize(m_App, m_jTicketEditor, "common-large-fontsize", "32");
		PropertyUtil.ScaleLabelFontsizePrefered(m_App, m_jTicketId, "common-large-fontsize", "32", 200);

		PropertyUtil.ScaleRadiobuttonFontsize(m_App, jrbCashTicketId, "common-small-fontsize", "32");
		PropertyUtil.ScaleRadiobuttonFontsize(m_App, jrbTicketId, "common-small-fontsize", "32");

		// int bwidth = Integer
		// .parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons",
		// "button-touchsmall-width", "48"));
		// int bheight = Integer
		// .parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons",
		// "button-touchsmall-height", "48"));
		//
		// PropertyUtil.ScaleButtonIcon(jButton2, bwidth, bheight);

		// int width = Integer.parseInt(PropertyUtil.getProperty(m_App,
		// "Ticket.Buttons", "menubar-img-width", "16"));
		// int height = Integer.parseInt(PropertyUtil.getProperty(m_App,
		// "Ticket.Buttons", "menubar-img-height", "16"));

		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-large-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jButton2, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jRefund, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jPrint, btnWidth, btnHeight, fontsize);

	}

	/*
	private void m_jEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jEditActionPerformed

		m_ticketCopy = m_ticket;
		m_TicketsBagTicketBag.showEdit();
		m_panelticketedit.showCatalog();
		m_panelticketedit.setActiveTicket(m_ticket.copyTicket(), null);

	}// GEN-LAST:event_m_jEditActionPerformed
	*/
	
	
	private void m_jPrintActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jPrintActionPerformed

		if (m_ticket != null) {
			try {
				ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
				if (taxcollection != null)
					script.put("taxes", taxcollection);
				if (taxeslogic != null) {
					script.put("taxeslogic", taxeslogic);
					try {
						taxeslogic.calculateTaxes(m_ticket);
					} catch (Exception ex) {

					}
				}
				script.put("ticket", m_ticket);
				
				if(m_ticket.getLinesCount() > 0)
				{
					String place = m_ticket.getLine(0).getProperty("Place");
					script.put("place", place == null ? "" : place);
				}
				else
				{
					script.put("place", ""); // put empty place
				}
				
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
				
				String []bonsize = m_App.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if(bonsize.length > 2)
					ticketsuffix = "."+bonsize[2];
				m_TTP2.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.TicketPreview"+ ticketsuffix)).toString());
			} catch (ScriptException e) {
				JMessageDialog.showMessage(m_App, this,
						new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotprint"), e));
			} catch (TicketPrinterException e) {
				JMessageDialog.showMessage(m_App, this,
						new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotprint"), e));
			}
		}

	}// GEN-LAST:event_m_jPrintActionPerformed

	private void m_jRefundActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jRefundActionPerformed

		java.util.List aRefundLines = new ArrayList();

		for (int i = 0; i < m_ticket.getLinesCount(); i++) {
			TicketLineInfo newline = new TicketLineInfo(m_ticket.getLine(i));
			aRefundLines.add(newline);
		}

		m_ticketCopy = null;
		m_TicketsBagTicketBag.showRefund();
		m_panelticketedit.showRefundLines(aRefundLines);

		TicketInfo refundticket = new TicketInfo();
		refundticket.setTicketType(TicketInfo.RECEIPT_REFUND);
		refundticket.setCustomer(m_ticket.getCustomer());
		refundticket.setPayments(m_ticket.getPayments());
		m_panelticketedit.setActiveTicket(refundticket, null);

	}// GEN-LAST:event_m_jRefundActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		readTicket(-1, jrbTicketId.isSelected() ? false : true);

	}// GEN-LAST:event_jButton1ActionPerformed

	private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jKeysActionPerformed

		readTicket(-1, jrbTicketId.isSelected() ? false : true);

	}// GEN-LAST:event_m_jKeysActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		JTicketsFinder finder = JTicketsFinder.getReceiptFinder(m_App, this, m_dlSales, dlCustomers);
		finder.setVisible(true);
		FindTicketsInfo selectedTicket = finder.getSelectedCustomer();
		if (selectedTicket == null) {
			m_jTicketEditor.reset();
			m_jTicketEditor.activate();
		} else {
			readTicket(selectedTicket.getTicketId(), false);
		}
	}// GEN-LAST:event_jButton2ActionPerformed

	@Override
	protected void ticketListChange(JTicketLines ticketLines) {
		// // ticket list
		if (ticketLines.getTableModelSize() >= 0) {
			// jButton2.setEnabled(true);
			// m_jDelTicket.setEnabled(true);
		}
		// // empty
		else {
			// jButton2.setEnabled(false);
			// m_jDelTicket.setEnabled(false);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JRadioButton jrbCashTicketId;
	private javax.swing.JRadioButton jrbTicketId;
	private javax.swing.JPanel m_jButtons;
	private javax.swing.JButton m_jEdit;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	private javax.swing.JPanel m_jOptions;
	private javax.swing.JPanel m_jPanelTicket;
	private javax.swing.JButton m_jPrint;
	private javax.swing.JButton m_jRefund;
	private com.openbravo.editor.JEditorIntegerPositive m_jTicketEditor;
	private javax.swing.JLabel m_jTicketId;
	// End of variables declaration//GEN-END:variables

}
