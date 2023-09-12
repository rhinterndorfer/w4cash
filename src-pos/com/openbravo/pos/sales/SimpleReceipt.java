//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.restaurant.PlaceSplit;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrian
 */
public class SimpleReceipt extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1387584794105769578L;

	private final String RESTAURANT = "restaurant";

	protected DataLogicCustomers dlCustomers;
	protected DataLogicSales dlSales;
	protected TaxesLogic taxeslogic;

	private JTicketLines ticketlines;
	private TicketInfo ticket;
	private Object ticketext;
	private String ticketId;
	private AppView m_App;
	private boolean tableSelect = false;
	private PlaceSplit selectedTable;

	public PlaceSplit getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(PlaceSplit selectedTable) {
		this.selectedTable = selectedTable;
	}

	private ReceiptSplit parent = null;

	private String m_appType;

	/** Creates new form SimpleReceipt 
	 * @throws BasicException */
	public SimpleReceipt(AppView app, String ticketline, DataLogicSales dlSales, DataLogicCustomers dlCustomers,
			TaxesLogic taxeslogic, boolean tableSelect, ReceiptSplit parent) throws BasicException {
		this.m_App = app;
		this.dlCustomers = dlCustomers;
		this.dlSales = dlSales;
		this.taxeslogic = taxeslogic;

		this.tableSelect = tableSelect;
		this.parent = parent;
		this.m_appType = m_App.getProperties().getProperty("machine.ticketsbag");
		initComponents();

		// dlSystem.getResourceAsXML("Ticket.Line")
		ticketlines = new JTicketLines(app, "sales-dialogtable-lineheight", "sales-dialogtable-fontsize", ticketline);
		ticketlines.addListDoubleClickListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// System.out.println(ticket.getLine(e.getFirstIndex()).getProductName());
				dblClickProduct(e.getFirstIndex());
			}
		});
		
		jPanel2.add(ticketlines, BorderLayout.CENTER);

		ScaleButtons();
	}

	private void ScaleButtons() {
		int bwidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "dialog-img-small", "32"));
		int bheight = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "dialog-img-small", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		if(m_jPlaceId != null)
			PropertyUtil.ScaleButtonFontsize(m_App, m_jPlaceId, "common-dialog-fontsize", "22");
		
		PropertyUtil.ScaleButtonIcon(btnCustomer, bwidth, bheight, fontsize);

	}

	public void setCustomerEnabled(boolean value) {
		btnCustomer.setEnabled(value);
	}

	public void setCustomerVisible(boolean value) {
		btnCustomer.setVisible(value);
	}

	public void setTicket(TicketInfo ticket, Object tt) {

		this.ticket = ticket;
		this.ticketext = tt;

		// The ticket name
		if (this.tableSelect && tt != null) {

			m_jPlaceId.setVisible(true);
			if (m_jLTicketId != null)
				m_jLTicketId.setVisible(false);

			m_jPlaceId.setText(tt.toString());
			
			m_jPlaceId.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent e) {
					
					// selectedTable = (PlaceSplit) ((JComboBox<PlaceSplit>) e.getSource()).getSelectedItem();
					PlaceSplit selectedPlace = JPlacesListDialog.newJDialog(m_App, (JButton)e.getSource()).showPlacesList(getPossibleTables());
					
					if(parent != null)
						parent.toFront();
					
					if(selectedPlace != null)
					{
						selectedTable = selectedPlace;
						m_jPlaceId.setText(selectedTable.getName());
						ticketext = selectedTable.getName();
						ticketId = selectedTable.getId();
					}
				}
			});
		} else {
			if (m_jPlaceId != null)
				m_jPlaceId.setVisible(false);
			m_jLTicketId.setVisible(true);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<html><div style='width: 150px'>");
			sb.append(ticket.getName(tt));
			sb.append("</div></html>");
			m_jLTicketId.setText(sb.toString());
		}

		ticketlines.clearTicketLines();
		for (int i = 0; i < ticket.getLinesCount(); i++) {
			ticketlines.addTicketLine(ticket.getLine(i));
		}

		if (ticket.getLinesCount() > 0) {
			ticketlines.setSelectedIndex(0);
		}

		printTotals();

	}

	private void refreshTicketTaxes() {

		for (TicketLineInfo line : ticket.getLines()) {
			line.setTaxInfo(
					taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), ticket.getDate(), ticket.getCustomer()));
		}
	}

	private void printTotals() {

		if (ticket.getLinesCount() == 0) {
			m_jSubtotalEuros.setText(Formats.CURRENCY.formatValue(0.0));
			m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(0.0));
			m_jTotalEuros.setText(Formats.CURRENCY.formatValue(0.0));
		} else {
			m_jSubtotalEuros.setText(ticket.printSubTotal());
			m_jTaxesEuros.setText(ticket.printTax());
			m_jTotalEuros.setText(ticket.printTotal());
		}
	}

	public TicketInfo getTicket() {
		return ticket;
	}

	private int findFirstNonAuxiliarLine() {

		int i = ticketlines.getSelectedIndex();
		while (i >= 0 && ticket.getLine(i).isProductCom()) {
			i--;
		}
		return i;
	}

	public TicketLineInfo[] getSelectedLines() {

		// never returns an empty array, or null, or an array with at least one
		// element.

		int i = findFirstNonAuxiliarLine();

		if (i >= 0) {

			List<TicketLineInfo> l = new ArrayList<TicketLineInfo>();

			TicketLineInfo line = ticket.getLine(i);
			l.add(line);
			ticket.removeLine(i);
			ticketlines.removeTicketLine(i);

			// add also auxiliars
			while (i < ticket.getLinesCount() && ticket.getLine(i).isProductCom()) {
				l.add(ticket.getLine(i));
				ticket.removeLine(i);
				ticketlines.removeTicketLine(i);
			}
			printTotals();
			return l.toArray(new TicketLineInfo[l.size()]);
		} else {
			return null;
		}
	}

	public TicketLineInfo[] getSelectedLinesUnit() {

		// never returns an empty array, or null, or an array with at least one
		// element.

		//int i = findFirstNonAuxiliarLine();
		int i = ticketlines.getSelectedIndex();

		if (i >= 0) {

			TicketLineInfo line = ticket.getLine(i);

			if (line.getMultiply() >= 1.0) {

				List<TicketLineInfo> l = new ArrayList<TicketLineInfo>();

				if (line.getMultiply() > 1.0) {
					line.setMultiply(line.getMultiply() - 1.0);
					ticketlines.setTicketLine(i, line);
					line = line.copyTicketLine(false);
					line.setMultiply(1.0);
					l.add(line);
					i++;
				} else { // == 1.0
					l.add(line);
					ticket.removeLine(i);
					ticketlines.removeTicketLine(i);
					
					// add also auxiliars if last item
					while (i < ticket.getLinesCount() && ticket.getLine(i).isProductCom()) {
						l.add(ticket.getLine(i));
						ticket.removeLine(i);
						ticketlines.removeTicketLine(i);
					}
				}
				
				printTotals();
				return l.toArray(new TicketLineInfo[l.size()]);
			} else { // < 1.0
				return null;
			}
		} else {
			return null;
		}
	}

	public void addSelectedLines(TicketLineInfo[] lines) {

		int i = findFirstNonAuxiliarLine();

		TicketLineInfo firstline = lines[0];

		if (i >= 0 && ticket.getLine(i).getProductID() != null && firstline.getProductID() != null
				&& ticket.getLine(i).getProductID().equals(firstline.getProductID())
				&& ticket.getLine(i).getTaxInfo().getId().equals(firstline.getTaxInfo().getId())
				&& ticket.getLine(i).getPrice() == firstline.getPrice()) {

			// add the auxiliars.
			for (int j = 1; j < lines.length; j++) {
				ticket.insertLine(i + 1, lines[j]);
				ticketlines.insertTicketLine(i + 1, lines[j]);
			}

			// inc the line
			ticket.getLine(i).setMultiply(ticket.getLine(i).getMultiply() + firstline.getMultiply());
			ticketlines.setTicketLine(i, ticket.getLine(i));
			ticketlines.setSelectedIndex(i);

		} else {
			// add all at the end in inverse order.
			int insertpoint = ticket.getLinesCount();
			for (int j = lines.length - 1; j >= 0; j--) {
				ticket.insertLine(insertpoint, lines[j]);
				ticketlines.insertTicketLine(insertpoint, lines[j]);
			}
		}

		printTotals();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 * @throws BasicException 
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() throws BasicException {

		jPanel1 = new javax.swing.JPanel();
		m_jPanTotals = new javax.swing.JPanel();
		m_jTotalEuros = new javax.swing.JLabel();
		m_jLblTotalEuros1 = new javax.swing.JLabel();
		m_jSubtotalEuros = new javax.swing.JLabel();
		m_jTaxesEuros = new javax.swing.JLabel();
		m_jLblTotalEuros2 = new javax.swing.JLabel();
		m_jLblTotalEuros3 = new javax.swing.JLabel();
		m_jButtons = new javax.swing.JPanel();
		m_jLTicketId = new javax.swing.JLabel();
		btnCustomer = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();

		// m_jTicketId = new javax.swing.JLabel();
		if (RESTAURANT.compareTo(m_appType) == 0) {
			if (this.tableSelect) {
				m_jPlaceId = new JButton();
			}
		}

		setLayout(new java.awt.BorderLayout());

		jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel1.setLayout(new java.awt.BorderLayout());

		// GroupLayout totalLayout = new GroupLayout(m_jPanTotals);
		GridLayout layout = new GridLayout(1, 2);
		m_jPanTotals.setLayout(layout);

		init2();
		ScaleLabels();

		m_jPanTotals.add(m_jLblTotalEuros1);
		m_jPanTotals.add(m_jTotalEuros);

		jPanel1.add(m_jPanTotals, java.awt.BorderLayout.CENTER);

		add(jPanel1, java.awt.BorderLayout.SOUTH);

		m_jButtons.setLayout(new java.awt.BorderLayout());

		if (RESTAURANT.compareTo(m_appType) == 0) {
			if (tableSelect) {

				m_jPlaceId.setBackground(java.awt.Color.white);
				// m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

				m_jPlaceId.setBorder(javax.swing.BorderFactory.createCompoundBorder(
						javax.swing.BorderFactory
								.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
						javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
				
				m_jPlaceId.setOpaque(true);
				
				// m_jTicketId.setPreferredSize(new java.awt.Dimension(160,
				// 25));
				m_jPlaceId.setRequestFocusEnabled(false);
				m_jButtons.add(m_jPlaceId, BorderLayout.LINE_START);
				m_jLTicketId.setVisible(false);
			}
		}

		// m_jLTicketId.setBackground(java.awt.Color.white);
		// m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		// m_jLTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(
		// javax.swing.BorderFactory
		// .createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
		// javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jLTicketId.setOpaque(true);
		// m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
		m_jLTicketId.setRequestFocusEnabled(false);
		//m_jLTicketId.setMinimumSize(new Dimension(150, 15));
		m_jButtons.add(m_jLTicketId);

		btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
		btnCustomer.setFocusPainted(false);
		btnCustomer.setFocusable(false);
		btnCustomer.setMargin(new java.awt.Insets(0, 0, 0, 0));
		btnCustomer.setLayout(new java.awt.BorderLayout());
		btnCustomer.setRequestFocusEnabled(false);
		btnCustomer.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCustomerActionPerformed(evt);
			}

		});
//		m_jButtons.setBackground(Color.red);
		m_jButtons.add(btnCustomer, java.awt.BorderLayout.AFTER_LINE_ENDS);

		add(m_jButtons, java.awt.BorderLayout.NORTH);

		jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel2.setLayout(new java.awt.BorderLayout());
		add(jPanel2, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private List<PlaceSplit> getPossibleTables()
	{
		if (RESTAURANT.compareTo(m_appType) == 0) {
			if (this.tableSelect) {
				try {
					return this.dlSales.getPlacesSplit();
				} catch (BasicException e) {
					Log.Exception(e);
				}
			}
		}
		return null;
	}

	private void dblClickProduct(int i) {
		TicketLineInfo info = ticket.getLine(i);

		ticket.removeLine(i);
		ticketlines.removeTicketLine(i);

		if (!tableSelect) {
			parent.dblclickToRightAllActionPerformed(info);
		} else {
			System.out.println("move left: " + info.getProductName());
			parent.dblclickToLeftAllActionPerformed(info);
		}
	}

	private void ScaleLabels() {
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTotalEuros, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTaxesEuros, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jSubtotalEuros, "common-dialog-fontsize", "22");
		if (this.m_jPlaceId != null) {
			//PropertyUtil.ScaleComboFontsize(m_App, ((JComboBox<PlaceSplit>) m_jPlaceId), "common-dialog-fontsize", "22");
			PropertyUtil.ScaleButtonFontsize(m_App, m_jPlaceId, "common-dialog-fontsize", "22");
		}
		// } else {
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLTicketId, "common-dialog-fontsize", "22");
		// }
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblTotalEuros1, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblTotalEuros2, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblTotalEuros3, "common-dialog-fontsize", "22");
	}

	private void init2() {

		m_jTotalEuros.setBackground(java.awt.Color.white);
		m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
		m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTotalEuros.setOpaque(true);
		// m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
		m_jTotalEuros.setRequestFocusEnabled(false);

		m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

		m_jSubtotalEuros.setBackground(java.awt.Color.white);
		m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jSubtotalEuros.setOpaque(true);
		m_jSubtotalEuros.setRequestFocusEnabled(false);

		m_jTaxesEuros.setBackground(java.awt.Color.white);
		m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTaxesEuros.setOpaque(true);
		m_jTaxesEuros.setRequestFocusEnabled(false);

		m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

		m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash1")); // NOI18N
	}

	private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed

		JCustomerFinder finder = JCustomerFinder.getCustomerFinder(m_App, this, dlCustomers);
		finder.search(ticket.getCustomer());
		finder.setVisible(true);

		try {
			ticket.setCustomer(finder.getSelectedCustomer() == null ? null
					: dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()));
		} catch (BasicException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"),
					e);
			msg.show(m_App, this);
		}

		if (RESTAURANT.compareTo(m_appType) == 0) {
			// The ticket name
			if (this.tableSelect) {
				if (finder.getSelectedCustomer() == null) {
					m_jPlaceId.setVisible(true);
					m_jLTicketId.setVisible(false);
					m_jPlaceId.setText(ticket.getName(ticketext));
				} else {
					m_jPlaceId.setVisible(false);
					m_jLTicketId.setVisible(true);
					StringBuilder sb = new StringBuilder();
					sb.append("<html><div style='width: 150px'>");
					sb.append(ticket.getName(ticketext));
					sb.append("</div></html>");
					m_jLTicketId.setText(sb.toString());
				}

			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("<html><div style='width: 150px'>");
				sb.append(ticket.getName(ticketext));
				sb.append("</div></html>");
				m_jLTicketId.setText(sb.toString());
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><div style='width: 150px'>");
			sb.append(ticket.getName(ticketext));
			sb.append("</div></html>");
			m_jLTicketId.setText(sb.toString());
		}

		refreshTicketTaxes();

		// refresh the receipt....
		// setTicket(ticket, ticketext);

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCustomer;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel m_jButtons;
	private javax.swing.JLabel m_jLblTotalEuros1;
	private javax.swing.JLabel m_jLblTotalEuros2;
	private javax.swing.JLabel m_jLblTotalEuros3;
	private javax.swing.JPanel m_jPanTotals;
	private javax.swing.JLabel m_jSubtotalEuros;
	private javax.swing.JLabel m_jTaxesEuros;

	private javax.swing.JButton m_jPlaceId;
	private javax.swing.JLabel m_jLTicketId;
	private javax.swing.JLabel m_jTotalEuros;

	public Object getTicketText() {
		return this.ticketext;
	}

	public String getTicketId() {
		return this.ticketId;
	}
}
