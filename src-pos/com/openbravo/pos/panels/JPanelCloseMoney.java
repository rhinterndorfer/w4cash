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

package com.openbravo.pos.panels;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import javax.swing.table.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;

/**
 *
 * @author adrianromero
 */
public class JPanelCloseMoney extends JPanel implements JPanelView, BeanFactoryApp {

	private AppView m_App;
	private DataLogicSystem m_dlSystem;
	private Date m_dateEnd;

	private PaymentsModel m_PaymentsToClose = null;

	private TicketParser m_TTP;

	/** Creates new form JPanelCloseMoney */
	public JPanelCloseMoney() {

		initComponents();

	}

	public void init(AppView app) throws BeanFactoryException {

		m_App = app;
		m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
		m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

		m_jTicketTable.setDefaultRenderer(Object.class,
				new TableRendererBasic(new Formats[] { new FormatsPayment(), Formats.STRING, Formats.CURRENCY }));
		m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
		m_jTicketTable.getTableHeader().setReorderingAllowed(false);
		m_jTicketTable.setRowHeight(25);
		m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		m_jsalestable.setDefaultRenderer(Object.class, new TableRendererBasic(
				new Formats[] { Formats.STRING, Formats.CURRENCY, Formats.CURRENCY, Formats.CURRENCY }));
		m_jsalestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		m_jScrollSales.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
		m_jsalestable.getTableHeader().setReorderingAllowed(false);
		m_jsalestable.setRowHeight(25);
		m_jsalestable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		m_jsalestableFree.setDefaultRenderer(Object.class, new TableRendererBasic(
				new Formats[] { Formats.STRING, Formats.CURRENCY, Formats.CURRENCY, Formats.CURRENCY }));
		m_jsalestableFree.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		m_jScrollSalesFree.getVerticalScrollBar().setPreferredSize(new Dimension(25, 25));
		m_jsalestableFree.getTableHeader().setReorderingAllowed(false);
		m_jsalestableFree.setRowHeight(25);
		m_jsalestableFree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ScaleButtons();
	}

	public Object getBean() {
		return this;
	}

	public JComponent getComponent() {
		return this;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.CloseTPV");
	}

	public void activate() throws BasicException {
		loadData();
	}

	public boolean deactivate() {
		// se me debe permitir cancelar el deactivate
		return true;
	}

	private void resetData() {
		// Reset
		m_jMinDate.setText(null);
		m_jMaxDate.setText(null);
		m_jPrintCash.setEnabled(false);
		m_jPrintCash.setVisible(false);
		m_jPrintLastCash.setEnabled(true);
		m_jPrintLastCash.setVisible(true);
		m_jCloseCash.setEnabled(false);
		m_jCloseCash.setVisible(false);
		m_jCount.setText(null); // AppLocal.getIntString("label.noticketstoclose");
		m_jCash.setText(null);
		m_jCashTotal.setText(null);
		
		m_jCardTotal.setText(null);
		m_jPaperinTotal.setText(null);
		m_jCashInOutTotal.setText(null);
		m_jCashOthersTotal.setText(null);

		m_jSales.setText(null);
		m_jSalesSubtotal.setText(null);
		m_jSalesTaxes.setText(null);
		m_jSalesTotal.setText(null);
		m_jTicketTable.setModel(new DefaultTableModel());
		m_jsalestable.setModel(new DefaultTableModel());
		
		m_jSalesFree.setText(null);
		m_jSalesSubtotalFree.setText(null);
		m_jSalesTaxesFree.setText(null);
		m_jSalesTotalFree.setText(null);
		m_jsalestableFree.setModel(new DefaultTableModel());
		
	}

	private void loadData() throws BasicException {

		resetData();

		// LoadData
		m_PaymentsToClose = PaymentsModel.loadInstance(m_App);
		m_dateEnd = new Date(); 
		m_PaymentsToClose.setDateEnd(m_dateEnd);

		// Populate Data
		m_jMinDate.setText(m_PaymentsToClose.printDateStart());
		m_jMaxDate.setText(m_PaymentsToClose.printDateEnd());

		if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {

			m_jPrintCash.setEnabled(true);
			m_jPrintCash.setVisible(true);
			m_jPrintLastCash.setEnabled(false);
			m_jPrintLastCash.setVisible(false);
			m_jCloseCash.setEnabled(true);
			m_jCloseCash.setVisible(true);

			m_jCount.setText(m_PaymentsToClose.printPayments());
			m_jCash.setText(m_PaymentsToClose.printPaymentsTotal());
			m_jCashTotal.setText(m_PaymentsToClose.printPaymentsCashTotal());
			m_jCardTotal.setText(m_PaymentsToClose.printPaymentsCardTotal());
			m_jPaperinTotal.setText(m_PaymentsToClose.printPaymentsPaperinTotal());
			m_jCashInOutTotal.setText(m_PaymentsToClose.printPaymentsCashInOutTotal());
			m_jCashOthersTotal.setText(m_PaymentsToClose.printPaymentsOthersTotal());

			m_jSales.setText(m_PaymentsToClose.printSales());
			m_jSalesSubtotal.setText(m_PaymentsToClose.printSalesBase());
			m_jSalesTaxes.setText(m_PaymentsToClose.printSalesTaxes());
			m_jSalesTotal.setText(m_PaymentsToClose.printSalesTotal());
			
			m_jSalesFree.setText(m_PaymentsToClose.printSalesFree());
			m_jSalesSubtotalFree.setText(m_PaymentsToClose.printSalesBaseFree());
			m_jSalesTaxesFree.setText(m_PaymentsToClose.printSalesTaxesFree());
			m_jSalesTotalFree.setText(m_PaymentsToClose.printSalesTotalFree());
		}

		m_jTicketTable.setModel(m_PaymentsToClose.getPaymentsModel());

		TableColumnModel jColumns = m_jTicketTable.getColumnModel();
		jColumns.getColumn(0).setPreferredWidth(90);
		jColumns.getColumn(0).setResizable(false);
		jColumns.getColumn(1).setPreferredWidth(150);
		jColumns.getColumn(1).setResizable(false);

		m_jsalestable.setModel(m_PaymentsToClose.getSalesModel());

		jColumns = m_jsalestable.getColumnModel();
		jColumns.getColumn(0).setPreferredWidth(200);
		jColumns.getColumn(0).setResizable(false);
		jColumns.getColumn(1).setPreferredWidth(100);
		jColumns.getColumn(1).setResizable(false);
		
		m_jsalestableFree.setModel(m_PaymentsToClose.getSalesModelFree());

		jColumns = m_jsalestableFree.getColumnModel();
		jColumns.getColumn(0).setPreferredWidth(200);
		jColumns.getColumn(0).setResizable(false);
		jColumns.getColumn(1).setPreferredWidth(100);
		jColumns.getColumn(1).setResizable(false);
	}

	private void printPayments(String report, Boolean isRequired, Boolean isFreePayment) {

		
		
		String sresource = m_dlSystem.getResourceAsXML(report);
		if (sresource == null) {
			if(isRequired) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
				msg.show(m_App, this);	
			}
		} else {
			try {
				// reload data
				loadData();
				
				if(!isFreePayment || m_PaymentsToClose.getSaleFreeLines().size() > 0) {
					ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
					script.put("payments", m_PaymentsToClose);
					m_TTP.printTicket(script.eval(sresource).toString());
				}
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (TicketPrinterException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (Exception e)
			{
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			}
			
		}
	}
	
	private void printLastCashPayments(String report, Boolean isRequired, Boolean isFreePayment) {
		
		String sresource = m_dlSystem.getResourceAsXML(report);
		if (sresource == null) {
			if(isRequired) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
				msg.show(m_App, this);
			}
		} else {
			try {
				// reload data
				PaymentsModel m_PaymentsToPrint = PaymentsModel.loadInstance(m_App, m_App.getLastCashIndex());

				if(!isFreePayment || m_PaymentsToPrint.getSaleFreeLines().size() > 0) {
					ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
					script.put("payments", m_PaymentsToPrint);
					m_TTP.printTicket(script.eval(sresource).toString());
				}
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (TicketPrinterException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (Exception e)
			{
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			}
			
		}
	}
	

	private class FormatsPayment extends Formats {
		protected String formatValueInt(Object value) {
			return AppLocal.getIntString("transpayment." + (String) value);
		}

		protected Object parseValueInt(String value) throws ParseException {
			return value;
		}

		public int getAlignment() {
			return javax.swing.SwingConstants.LEFT;
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

		jPanel1 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		m_jMinDate = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		m_jMaxDate = new javax.swing.JTextField();
		jPanel5 = new javax.swing.JPanel();
		m_jScrollTableTicket = new javax.swing.JScrollPane();
		m_jTicketTable = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		m_jCount = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jLabelCashTotal = new javax.swing.JLabel();
		jLabelCardTotal = new javax.swing.JLabel();
		jLabelPaperinTotal = new javax.swing.JLabel();
		jLabelCashInOutTotal = new javax.swing.JLabel();
		jLabelOthersTotal = new javax.swing.JLabel();
		m_jCash = new javax.swing.JTextField();
		m_jCashTotal = new javax.swing.JTextField();
		m_jCardTotal = new javax.swing.JTextField();
		m_jPaperinTotal = new javax.swing.JTextField();
		m_jCashInOutTotal = new javax.swing.JTextField();
		m_jCashOthersTotal = new javax.swing.JTextField();
		jPanel6 = new javax.swing.JPanel();
		jPanel6Free = new javax.swing.JPanel();
		m_jSalesTotal = new javax.swing.JTextField();
		m_jScrollSales = new javax.swing.JScrollPane();
		m_jsalestable = new javax.swing.JTable();
		m_jSalesTaxes = new javax.swing.JTextField();
		m_jSalesSubtotal = new javax.swing.JTextField();
		m_jSales = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		m_jCloseCash = new javax.swing.JButton();
		m_jPrintCash = new javax.swing.JButton();
		m_jPrintLastCash = new javax.swing.JButton();

		jLabel5Free = new javax.swing.JLabel();
		jLabel6Free = new javax.swing.JLabel();
		jLabel12Free = new javax.swing.JLabel();
		jLabel7Free = new javax.swing.JLabel();
		m_jSalesTotalFree = new javax.swing.JTextField();
		m_jScrollSalesFree = new javax.swing.JScrollPane();
		m_jsalestableFree = new javax.swing.JTable();
		m_jSalesTaxesFree = new javax.swing.JTextField();
		m_jSalesSubtotalFree = new javax.swing.JTextField();
		m_jSalesFree = new javax.swing.JTextField();
		
		setLayout(new java.awt.BorderLayout());

		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.datestitle"))); // NOI18N

		jLabel2.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N

		m_jMinDate.setEditable(false);
		m_jMinDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel3.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N

		m_jMaxDate.setEditable(false);
		m_jMaxDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel4Layout.createSequentialGroup()
										.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jMinDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel4Layout.createSequentialGroup()
								.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(m_jMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160,
										javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(m_jMinDate, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(m_jMaxDate, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.paymentstitle"))); // NOI18N

		m_jScrollTableTicket.setMinimumSize(new java.awt.Dimension(350, 140));
		m_jScrollTableTicket.setPreferredSize(new java.awt.Dimension(350, 140));

		m_jTicketTable.setFocusable(false);
		m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
		m_jTicketTable.setRequestFocusEnabled(false);
		m_jTicketTable.setShowVerticalLines(false);
		m_jScrollTableTicket.setViewportView(m_jTicketTable);

		jLabel1.setText(AppLocal.getIntString("Label.Tickets")); // NOI18N

		m_jCount.setEditable(false);
		m_jCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel4.setText(AppLocal.getIntString("Label.Cash")); // NOI18N
		jLabelCashTotal.setText(AppLocal.getIntString("Label.CashTotal")); // NOI18N
		jLabelCardTotal.setText(AppLocal.getIntString("Label.CardTotal")); // NOI18N
		jLabelPaperinTotal.setText(AppLocal.getIntString("Label.PaperinTotal")); // NOI18N
		jLabelCashInOutTotal.setText(AppLocal.getIntString("Label.CashInOutTotal")); // NOI18N
		jLabelOthersTotal.setText(AppLocal.getIntString("Label.OthersTotal")); // NOI18N

		m_jCash.setEditable(false);
		m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		m_jCashTotal.setEditable(false);
		m_jCashTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jCardTotal.setEditable(false);
		m_jCardTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jPaperinTotal.setEditable(false);
		m_jPaperinTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jCashInOutTotal.setEditable(false);
		m_jCashInOutTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jCashOthersTotal.setEditable(false);
		m_jCashOthersTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap()
						.addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 350,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCount, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabelCashTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCashTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabelCardTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCardTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabelPaperinTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jPaperinTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabelCashInOutTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCashInOutTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabelOthersTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCashOthersTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jCash, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(67, Short.MAX_VALUE)));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 190,
										javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(jPanel5Layout.createSequentialGroup()
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1).addComponent(m_jCount,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabelCashTotal).addComponent(m_jCashTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabelCardTotal).addComponent(m_jCardTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabelPaperinTotal).addComponent(m_jPaperinTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabelOthersTotal).addComponent(m_jCashOthersTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabelCashInOutTotal).addComponent(m_jCashInOutTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel4).addComponent(m_jCash,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(16, Short.MAX_VALUE)));

		jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.salestitle"))); // NOI18N

		m_jSalesTotal.setEditable(false);
		m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jsalestable.setFocusable(false);
		m_jsalestable.setIntercellSpacing(new java.awt.Dimension(0, 1));
		m_jsalestable.setRequestFocusEnabled(false);
		m_jsalestable.setShowVerticalLines(false);
		m_jScrollSales.setViewportView(m_jsalestable);

		m_jSalesTaxes.setEditable(false);
		m_jSalesTaxes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		m_jSalesSubtotal.setEditable(false);
		m_jSalesSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		m_jSales.setEditable(false);
		m_jSales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel5.setText(AppLocal.getIntString("label.sales")); // NOI18N

		jLabel6.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

		jLabel12.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

		jLabel7.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap()
						.addComponent(m_jScrollSales, javax.swing.GroupLayout.PREFERRED_SIZE, 350,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel6Layout.createSequentialGroup()
										.addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel6Layout.createSequentialGroup()
										.addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel6Layout.createSequentialGroup()
										.addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel6Layout.createSequentialGroup()
										.addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(67, Short.MAX_VALUE)));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup()
						.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(m_jScrollSales, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
										javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(jPanel6Layout.createSequentialGroup()
								.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel5).addComponent(m_jSales,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel6).addComponent(m_jSalesSubtotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel12).addComponent(m_jSalesTaxes,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel7).addComponent(m_jSalesTotal,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(16, Short.MAX_VALUE)));

		/**
		 *  add free panel
		 */
		jPanel6Free.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("message.paymentfree"))); // NOI18N

		m_jSalesTotalFree.setEditable(false);
		m_jSalesTotalFree.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		m_jsalestableFree.setFocusable(false);
		m_jsalestableFree.setIntercellSpacing(new java.awt.Dimension(0, 1));
		m_jsalestableFree.setRequestFocusEnabled(false);
		m_jsalestableFree.setShowVerticalLines(false);
		m_jScrollSalesFree.setViewportView(m_jsalestableFree);

		m_jSalesTaxesFree.setEditable(false);
		m_jSalesTaxesFree.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		m_jSalesSubtotalFree.setEditable(false);
		m_jSalesSubtotalFree.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		m_jSalesFree.setEditable(false);
		m_jSalesFree.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel5Free.setText(AppLocal.getIntString("label.sales")); // NOI18N

		jLabel6Free.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

		jLabel12Free.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

		jLabel7Free.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel6Free);
		jPanel6Free.setLayout(jPanel7Layout);
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap()
						.addComponent(m_jScrollSalesFree, javax.swing.GroupLayout.PREFERRED_SIZE, 350,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel7Layout.createSequentialGroup()
										.addComponent(jLabel5Free, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesFree, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel7Layout.createSequentialGroup()
										.addComponent(jLabel6Free, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesSubtotalFree, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel7Layout.createSequentialGroup()
										.addComponent(jLabel12Free, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesTaxesFree, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel7Layout.createSequentialGroup()
										.addComponent(jLabel7Free, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(m_jSalesTotalFree, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(67, Short.MAX_VALUE)));
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup()
						.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(m_jScrollSalesFree, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
										javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(jPanel7Layout.createSequentialGroup()
								.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel5Free).addComponent(m_jSalesFree,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel6Free).addComponent(m_jSalesSubtotalFree,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel12Free).addComponent(m_jSalesTaxesFree,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel7Free).addComponent(m_jSalesTotalFree,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(16, Short.MAX_VALUE)));
		
		m_jCloseCash.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png")));
		m_jCloseCash.setText(AppLocal.getIntString("Button.CloseCash")); // NOI18N
		m_jCloseCash.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jCloseCashActionPerformed(evt);
			}
		});

		m_jPrintCash
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png")));
		m_jPrintCash.setText(AppLocal.getIntString("Button.PrintCash")); // NOI18N
		m_jPrintCash.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jPrintCashActionPerformed(evt);
			}
		});
		
		m_jPrintLastCash
			.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png")));
		m_jPrintLastCash.setText(AppLocal.getIntString("Button.PrintLastCash")); // NOI18N
		m_jPrintLastCash.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
						m_jPrintCashLastActionPerformed(evt);
					}
			});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(
						jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
												jPanel1Layout.createSequentialGroup()
													.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addPreferredGap(
															javax.swing.LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(m_jPrintCash)
													.addPreferredGap(
															javax.swing.LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(m_jPrintLastCash)
													.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(m_jCloseCash)
												)
								.addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel6Free, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel5,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
								)
							.addContainerGap()
						));
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
						jPanel1Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(m_jCloseCash)
									.addComponent(m_jPrintLastCash)
									.addComponent(m_jPrintCash)
							)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel6Free, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()
					));

		add(jPanel1, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private void ScaleButtons() {
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel1,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel2,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel3,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel4,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel5,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel6,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel7,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel12,
		// "common-large-fontsize", "64");
		//
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jCash,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jCount,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jMaxDate,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jMinDate,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jSales,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jSalesSubtotal,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jSalesTaxes,
		// "common-large-fontsize", "64");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jSalesTotal,
		// "common-large-fontsize", "64");
		//
		// PropertyUtil.ScaleTableColumnFontsize(m_App, m_jTicketTable,
		// "sales-tablecolumn-fontsize", "14");
		// // PropertyUtil.ScaleTableRowheight(m_App, m_jTicketTable,
		// // "sales-producttable-lineheight", "25");
		// PropertyUtil.ScaleTableColumnFontsize(m_App, m_jsalestable,
		// "sales-tablecolumn-fontsize", "14");
		// // PropertyUtil.ScaleTableRowheight(m_App, m_jsalestable,
		// // "sales-producttable-lineheight", "25");
		//
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(m_jCloseCash, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jPrintCash, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jPrintLastCash, btnWidth, btnHeight, fontsize);

	}

	private void m_jCloseCashActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCloseCashActionPerformed
		
		int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaclosecash"),
				AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {


			
			try {
				// reload data
				loadData();
				
				// Cerramos la caja si esta pendiente de cerrar.
				if (m_App.getActiveCashDateEnd() == null) {
					// set closed info
					String activeCash = m_App.getActiveCashIndex(true, false);

					// print report before closing
					// otherwise new cash session is opened
					printPayments("Printer.CloseCash", true, false);
					printPayments("Printer.CloseCash.Free", false, true);
					
					// split if contains "\"
					// support POS with same name in front of "\" character sharing a cash shift
					String host = m_App.getProperties().getHost();
					if(host.contains("\\"))
					{
						host = host.substring(0, host.indexOf('\\'));
					}
					
					
					new StaticSentence(m_App.getSession(),
							"UPDATE CLOSEDCASH SET DATEEND = ? WHERE HOST = ? AND MONEY = ?",
							new SerializerWriteBasic(new Datas[] { Datas.TIMESTAMP, Datas.STRING, Datas.STRING }))
									.exec(new Object[] { m_dateEnd, host, activeCash });

					m_App.closeCashIndex();
					
					
				}
			} catch (BasicException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"),
						e);
				msg.show(m_App, this);
			}

			// Mostramos el mensaje
			JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"),
					AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);

			resetData();

		}
	}// GEN-LAST:event_m_jCloseCashActionPerformed

	private void m_jPrintCashActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jPrintCashActionPerformed

		// print report
		printPayments("Printer.PartialCash", true, false);
		printPayments("Printer.PartialCash.Free", false, true);

	}// GEN-LAST:event_m_jPrintCashActionPerformed

	private void m_jPrintCashLastActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jPrintCashActionPerformed

		// print report
		printLastCashPayments("Printer.CloseCash", true, false);
		printLastCashPayments("Printer.CloseCash.Free", false, true);

	}// GEN-LAST:event_m_jPrintCashActionPerformed

	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel12Free;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabelCashTotal;
	private javax.swing.JLabel jLabelCardTotal;
	private javax.swing.JLabel jLabelPaperinTotal;
	private javax.swing.JLabel jLabelCashInOutTotal;
	private javax.swing.JLabel jLabelOthersTotal;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel5Free;
	private javax.swing.JLabel jLabel6Free;
	private javax.swing.JLabel jLabel7Free;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel6Free;
	private javax.swing.JTextField m_jCash;
	private javax.swing.JTextField m_jCashTotal;
	private javax.swing.JTextField m_jCardTotal;
	private javax.swing.JTextField m_jPaperinTotal;
	private javax.swing.JTextField m_jCashInOutTotal;
	private javax.swing.JTextField m_jCashOthersTotal;
	private javax.swing.JButton m_jCloseCash;
	private javax.swing.JTextField m_jCount;
	private javax.swing.JTextField m_jMaxDate;
	private javax.swing.JTextField m_jMinDate;
	private javax.swing.JButton m_jPrintCash;
	private javax.swing.JButton m_jPrintLastCash;
	private javax.swing.JTextField m_jSales;
	private javax.swing.JTextField m_jSalesFree;
	private javax.swing.JTextField m_jSalesSubtotal;
	private javax.swing.JTextField m_jSalesSubtotalFree;
	private javax.swing.JTextField m_jSalesTaxes;
	private javax.swing.JTextField m_jSalesTaxesFree;
	private javax.swing.JTextField m_jSalesTotal;
	private javax.swing.JTextField m_jSalesTotalFree;
	private javax.swing.JScrollPane m_jScrollSales;
	private javax.swing.JScrollPane m_jScrollSalesFree;
	private javax.swing.JScrollPane m_jScrollTableTicket;
	private javax.swing.JTable m_jTicketTable;
	private javax.swing.JTable m_jsalestable;
	private javax.swing.JTable m_jsalestableFree;
	// End of variables declaration//GEN-END:variables

}
