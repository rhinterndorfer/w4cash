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
package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.FindTicketsRenderer;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

/**
 *
 * @author Mikel irurita
 */
public class JTicketsFinder extends javax.swing.JDialog implements EditorCreator {

	private ListProvider lpr;
	private SentenceList m_sentcat;
	private ComboBoxValModel m_CategoryModel;
	private DataLogicSales dlSales;
	private DataLogicCustomers dlCustomers;
	private FindTicketsInfo selectedTicket;

	private AppView m_App;

	/** Creates new form JCustomerFinder */
	private JTicketsFinder(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JCustomerFinder */
	private JTicketsFinder(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	public static JTicketsFinder getReceiptFinder(AppView app, Component parent, DataLogicSales dlSales,
			DataLogicCustomers dlCustomers) {
		Window window = getWindow(parent);

		JTicketsFinder myMsg;
		if (window instanceof Frame) {
			myMsg = new JTicketsFinder((Frame) window, true);
		} else {
			myMsg = new JTicketsFinder((Dialog) window, true);
		}
		myMsg.init(app, dlSales, dlCustomers);
		myMsg.applyComponentOrientation(parent.getComponentOrientation());
		return myMsg;
	}

	public FindTicketsInfo getSelectedCustomer() {
		return selectedTicket;
	}

	private void init(AppView app, DataLogicSales dlSales, DataLogicCustomers dlCustomers) {
		this.m_App = app;

		this.dlSales = dlSales;
		this.dlCustomers = dlCustomers;

		initComponents();

		PropertyUtil.ScaleScrollbar(m_App, jScrollPane1);

		jtxtTicketID.addEditorKeys(m_jKeys);
		jtxtMoney.addEditorKeys(m_jKeys);

		// jtxtTicketID.activate();
		lpr = new ListProviderCreator(dlSales.getTicketsList(), this);

		jListTickets.setCellRenderer(new FindTicketsRenderer());

		getRootPane().setDefaultButton(jcmdOK);

		initCombos();

		defaultValues();

		selectedTicket = null;

		m_jKeys.ScaleButtons();

		ScaleButtons();
	}

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, labelCustomer, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleEditnumbersFontsize(m_App, jtxtTicketID, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleEditcurrencyFontsize(m_App, jtxtMoney, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleTextFieldFontsize(m_App, jtxtCustomer, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtEndDate, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtStartDate, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleComboFontsize(m_App, jComboBoxTicket, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleComboFontsize(m_App, jcboMoney, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleComboFontsize(m_App, jcboUser, "common-dialog-fontsize", "22");

		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "48"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "48"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jButton1, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButton3, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdOK, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdCancel, menuwidth, menuheight, fontsize);

		// menuwidth = Integer
		// .parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons",
		// "menubar-img-width", "16"));
		// menuheight = Integer
		// .parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons",
		// "menubar-img-height", "16"));
		menuwidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "common-dialog-fontsize", "22"));
		menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "common-dialog-fontsize", "22"));
		fontsize = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-menu-fontsize", "8"));

		PropertyUtil.ScaleButtonIcon(btnCustomer, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(btnDateStart, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(btnDateEnd, menuwidth, menuheight, fontsize);

		// PropertyUtil.ScaleButtonIcon(btnDateStart, menuwidth, menuheight);
		// PropertyUtil.ScaleButtonIcon(btnDateEnd, menuwidth, menuheight);

	}

	public void executeSearch() {
		try {
			jListTickets.setModel(new MyListData(lpr.loadData()));
			if (jListTickets.getModel().getSize() > 0) {
				jListTickets.setSelectedIndex(0);
			}
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	private void initCombos() {
		String[] values = new String[] { AppLocal.getIntString("label.all"), AppLocal.getIntString("label.sales"),
				AppLocal.getIntString("label.refunds") };
		jComboBoxTicket.setModel(new DefaultComboBoxModel(values));

		jcboMoney.setModel(ListQBFModelNumber.getMandatoryNumber());

		m_sentcat = dlSales.getUserList();
		m_CategoryModel = new ComboBoxValModel();

		List catlist = null;
		try {
			catlist = m_sentcat.list();
		} catch (BasicException ex) {
			ex.getMessage();
		}
		catlist.add(0, null);
		m_CategoryModel = new ComboBoxValModel(catlist);
		jcboUser.setModel(m_CategoryModel);
	}

	private void defaultValues() {

		jListTickets.setModel(new MyListData(new ArrayList()));

		jcboUser.setSelectedItem(null);

		jtxtTicketID.reset();
		jtxtTicketID.activate();

		jComboBoxTicket.setSelectedIndex(0);

		jcboUser.setSelectedItem(null);

		jcboMoney.setSelectedItem(((ListQBFModelNumber) jcboMoney.getModel()).getElementAt(0));
		jcboMoney.revalidate();
		jcboMoney.repaint();

		jtxtMoney.reset();

		jTxtStartDate.setText(null);
		jTxtEndDate.setText(null);

		jtxtCustomer.setText(null);

	}

	@Override
	public Object createValue() throws BasicException {

		Object[] afilter = new Object[14];

		// Ticket ID
		if (jtxtTicketID.getText() == null || jtxtTicketID.getText().equals("")) {
			afilter[0] = QBFCompareEnum.COMP_NONE;
			afilter[1] = null;
		} else {
			afilter[0] = QBFCompareEnum.COMP_EQUALS;
			afilter[1] = jtxtTicketID.getValueInteger();
		}

		// Sale and refund checkbox
		if (jComboBoxTicket.getSelectedIndex() == 0) {
			afilter[2] = QBFCompareEnum.COMP_DISTINCT;
			afilter[3] = -1;
		} else if (jComboBoxTicket.getSelectedIndex() == 1) {
			afilter[2] = QBFCompareEnum.COMP_EQUALS;
			afilter[3] = 0;
		} else if (jComboBoxTicket.getSelectedIndex() == 2) {
			afilter[2] = QBFCompareEnum.COMP_EQUALS;
			afilter[3] = 1;
		}

		// Receipt money
		afilter[5] = jtxtMoney.getDoubleValue();
		afilter[4] = afilter[5] == null ? QBFCompareEnum.COMP_NONE : jcboMoney.getSelectedItem();

		// Date range
		Object startdate = Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
		Object enddate = Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());

		afilter[6] = (startdate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_GREATEROREQUALS;
		afilter[7] = startdate;
		afilter[8] = (enddate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_LESS;
		afilter[9] = enddate;

		// User
		if (jcboUser.getSelectedItem() == null) {
			afilter[10] = QBFCompareEnum.COMP_NONE;
			afilter[11] = null;
		} else {
			afilter[10] = QBFCompareEnum.COMP_EQUALS;
			afilter[11] = ((TaxCategoryInfo) jcboUser.getSelectedItem()).getName();
		}

		// Customer
		if (jtxtCustomer.getText() == null || jtxtCustomer.getText().equals("")) {
			afilter[12] = QBFCompareEnum.COMP_NONE;
			afilter[13] = null;
		} else {
			afilter[12] = QBFCompareEnum.COMP_RE;
			afilter[13] = "%" + jtxtCustomer.getText() + "%";
		}

		return afilter;

	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		} else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

	private static class MyListData extends javax.swing.AbstractListModel {

		private java.util.List m_data;

		public MyListData(java.util.List data) {
			m_data = data;
		}

		@Override
		public Object getElementAt(int index) {
			return m_data.get(index);
		}

		@Override
		public int getSize() {
			return m_data.size();
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

		jPanel3 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jtxtMoney = new com.openbravo.editor.JEditorCurrency();
		jcboUser = new javax.swing.JComboBox();
		jcboMoney = new javax.swing.JComboBox();
		jtxtTicketID = new com.openbravo.editor.JEditorIntegerPositive();
		labelCustomer = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jTxtStartDate = new javax.swing.JTextField();
		jTxtEndDate = new javax.swing.JTextField();
		btnDateStart = new javax.swing.JButton();
		btnDateEnd = new javax.swing.JButton();
		jtxtCustomer = new javax.swing.JTextField();
		btnCustomer = new javax.swing.JButton();
		jComboBoxTicket = new javax.swing.JComboBox();
		jPanel6 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jListTickets = new javax.swing.JList();
		jPanel8 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		m_jKeys = new com.openbravo.editor.JEditorKeys(m_App);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("form.tickettitle")); // NOI18N

		jPanel3.setLayout(new java.awt.BorderLayout());

		jPanel5.setLayout(new java.awt.BorderLayout());

		jPanel7.setLayout(new GridBagLayout());
		// jPanel7.setPreferredSize(new java.awt.Dimension(0, 210));

		jLabel1.setText(AppLocal.getIntString("label.ticketid")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		jPanel7.add(jLabel1, gbc_lbl1);

		GridBagConstraints gbc_txt1 = new GridBagConstraints();
		gbc_txt1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt1.weightx = 1.0;
		gbc_txt1.insets = new Insets(5, 5, 0, 0);
		gbc_txt1.gridx = 1;
		gbc_txt1.gridy = 0;
		jPanel7.add(jtxtTicketID, gbc_txt1);

		GridBagConstraints gbc_cmb1 = new GridBagConstraints();
		gbc_cmb1.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb1.weightx = 1.0;
		gbc_cmb1.insets = new Insets(5, 5, 0, 0);
		gbc_cmb1.gridx = 2;
		gbc_cmb1.gridy = 0;
		jPanel7.add(jComboBoxTicket, gbc_cmb1);

		jLabel3.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		jPanel7.add(jLabel3, gbc_lbl2);

		GridBagConstraints gbc_txt2 = new GridBagConstraints();
		gbc_txt2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt2.gridwidth = 2;
		gbc_txt2.weightx = 1.0;
		gbc_txt2.insets = new Insets(5, 5, 0, 0);
		gbc_txt2.gridx = 1;
		gbc_txt2.gridy = 1;
		jPanel7.add(jTxtStartDate, gbc_txt2);

		btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		// btnDateStart.setPreferredSize(new java.awt.Dimension(50, 25));
		btnDateStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateStartActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_btn1.insets = new Insets(5, 5, 0, 0);
		gbc_btn1.gridx = 3;
		gbc_btn1.gridy = 1;
		jPanel7.add(btnDateStart, gbc_btn1);

		jLabel4.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		jPanel7.add(jLabel4, gbc_lbl3);

		GridBagConstraints gbc_txt3 = new GridBagConstraints();
		gbc_txt3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt3.gridwidth = 2;
		gbc_txt3.weightx = 1.0;
		gbc_txt3.insets = new Insets(5, 5, 0, 0);
		gbc_txt3.gridx = 1;
		gbc_txt3.gridy = 2;
		jPanel7.add(jTxtEndDate, gbc_txt3);

		btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		// btnDateEnd.setPreferredSize(new java.awt.Dimension(50, 25));
		btnDateEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateEndActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_btn2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_btn2.insets = new Insets(5, 5, 0, 0);
		gbc_btn2.gridx = 3;
		gbc_btn2.gridy = 2;
		jPanel7.add(btnDateEnd, gbc_btn2);

		labelCustomer.setText(AppLocal.getIntString("label.customer")); // NOI18N
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(5, 5, 0, 0);
		gbc_lbl4.gridx = 0;
		gbc_lbl4.gridy = 3;
		jPanel7.add(labelCustomer, gbc_lbl4);

		GridBagConstraints gbc_txt4 = new GridBagConstraints();
		gbc_txt4.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt4.gridwidth = 2;
		gbc_txt4.weightx = 1.0;
		gbc_txt4.insets = new Insets(5, 5, 0, 0);
		gbc_txt4.gridx = 1;
		gbc_txt4.gridy = 3;
		jPanel7.add(jtxtCustomer, gbc_txt4);

		btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
		btnCustomer.setFocusPainted(false);
		btnCustomer.setFocusable(false);
		btnCustomer.setRequestFocusEnabled(false);
		btnCustomer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCustomerActionPerformed(m_App, evt);
			}
		});
		GridBagConstraints gbc_btn3 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_btn3.insets = new Insets(5, 5, 0, 0);
		gbc_btn3.gridx = 3;
		gbc_btn3.gridy = 3;
		jPanel7.add(btnCustomer, gbc_btn3);

		jLabel6.setText(AppLocal.getIntString("label.user")); // NOI18N
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.anchor = GridBagConstraints.WEST;
		gbc_lbl5.insets = new Insets(5, 5, 0, 0);
		gbc_lbl5.gridx = 0;
		gbc_lbl5.gridy = 4;
		jPanel7.add(jLabel6, gbc_lbl5);

		GridBagConstraints gbc_cmb2 = new GridBagConstraints();
		gbc_cmb2.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb2.weightx = 1.0;
		gbc_cmb2.gridwidth = 2;
		gbc_cmb2.insets = new Insets(5, 5, 0, 0);
		gbc_cmb2.gridx = 1;
		gbc_cmb2.gridy = 4;
		jPanel7.add(jcboUser, gbc_cmb2);

		jLabel7.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
		GridBagConstraints gbc_lbl6 = new GridBagConstraints();
		gbc_lbl6.anchor = GridBagConstraints.WEST;
		gbc_lbl6.insets = new Insets(5, 5, 0, 0);
		gbc_lbl6.gridx = 0;
		gbc_lbl6.gridy = 5;
		jPanel7.add(jLabel7, gbc_lbl6);

		GridBagConstraints gbc_cmb3 = new GridBagConstraints();
		gbc_cmb3.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb3.weightx = 1.0;
		gbc_cmb3.insets = new Insets(5, 5, 0, 0);
		gbc_cmb3.gridx = 1;
		gbc_cmb3.gridy = 5;
		jPanel7.add(jcboMoney, gbc_cmb3);

		GridBagConstraints gbc_txt5 = new GridBagConstraints();
		gbc_txt5.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt5.weightx = 1.0;
		gbc_txt5.insets = new Insets(5, 5, 0, 0);
		gbc_txt5.gridx = 2;
		gbc_txt5.gridy = 5;
		jPanel7.add(jtxtMoney, gbc_txt5);

		jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

		jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/refresh.png"))); // NOI18N
		jButton1.setText(AppLocal.getIntString("button.clean")); // NOI18N
		jButton1.setFocusPainted(false);
		jButton1.setFocusable(false);
		jButton1.setRequestFocusEnabled(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jPanel6.add(jButton1);

		jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launchfilter.png"))); // NOI18N
		jButton3.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
		jButton3.setFocusPainted(false);
		jButton3.setFocusable(false);
		jButton3.setRequestFocusEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});
		jPanel6.add(jButton3);

		jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

		jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

		jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel4.setLayout(new java.awt.BorderLayout());

		jListTickets.setFocusable(false);
		jListTickets.setRequestFocusEnabled(false);
		jListTickets.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jListTicketsMouseClicked(evt);
			}
		});
		jListTickets.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				jListTicketsValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(jListTickets);

		jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

		jPanel8.setLayout(new java.awt.BorderLayout());

		jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		jcmdOK.setEnabled(false);
		jcmdOK.setFocusPainted(false);
		jcmdOK.setFocusable(false);
		jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdOK.setRequestFocusEnabled(false);
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdOKActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdOK);

		jcmdCancel.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		jcmdCancel.setFocusPainted(false);
		jcmdCancel.setFocusable(false);
		jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdCancel.setRequestFocusEnabled(false);
		jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdCancelActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdCancel);

		jPanel8.add(jPanel1, java.awt.BorderLayout.LINE_END);

		jPanel3.add(jPanel8, java.awt.BorderLayout.SOUTH);

		getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

		// jPanel2.setPreferredSize(new java.awt.Dimension(200, 250));
		jPanel2.setLayout(new java.awt.BorderLayout());
		jPanel2.add(m_jKeys, java.awt.BorderLayout.NORTH);

		getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_END);

		PropertyUtil.ScaleDialog(m_App, this, 840, 750);

		// java.awt.Dimension screenSize =
		// java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds((screenSize.width - 695) / 2, (screenSize.height - 684) /
		// 2, 695, 684);
	}// </editor-fold>//GEN-END:initComponents

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdOKActionPerformed
		selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
		dispose();
	}// GEN-LAST:event_jcmdOKActionPerformed

	private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdCancelActionPerformed
		dispose();
	}// GEN-LAST:event_jcmdCancelActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		executeSearch();
	}// GEN-LAST:event_jButton3ActionPerformed

	private void jListTicketsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListTicketsValueChanged
		jcmdOK.setEnabled(jListTickets.getSelectedValue() != null);

	}// GEN-LAST:event_jListTicketsValueChanged

	private void jListTicketsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListTicketsMouseClicked

		if (evt.getClickCount() == 2) {
			selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
			dispose();
		}

	}// GEN-LAST:event_jListTicketsMouseClicked

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		defaultValues();
	}// GEN-LAST:event_jButton1ActionPerformed

	private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateStartActionPerformed
		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateStartActionPerformed

	private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateEndActionPerformed
		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateEndActionPerformed

	private void btnCustomerActionPerformed(AppView app, java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed
		JCustomerFinder finder = JCustomerFinder.getCustomerFinder(app, this, dlCustomers);
		finder.search(null);
		finder.setVisible(true);

		try {
			jtxtCustomer.setText(finder.getSelectedCustomer() == null ? null
					: dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()).toString());
		} catch (BasicException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"),
					e);
			msg.show(app, this);
		}

	}// GEN-LAST:event_btnCustomerActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCustomer;
	private javax.swing.JButton btnDateEnd;
	private javax.swing.JButton btnDateStart;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton3;
	private javax.swing.JComboBox jComboBoxTicket;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JList jListTickets;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField jTxtEndDate;
	private javax.swing.JTextField jTxtStartDate;
	private javax.swing.JComboBox jcboMoney;
	private javax.swing.JComboBox jcboUser;
	private javax.swing.JButton jcmdCancel;
	private javax.swing.JButton jcmdOK;
	private javax.swing.JTextField jtxtCustomer;
	private com.openbravo.editor.JEditorCurrency jtxtMoney;
	private com.openbravo.editor.JEditorIntegerPositive jtxtTicketID;
	private javax.swing.JLabel labelCustomer;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	// End of variables declaration//GEN-END:variables

}
