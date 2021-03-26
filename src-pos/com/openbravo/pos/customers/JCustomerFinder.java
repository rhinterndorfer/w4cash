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
package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurant;
import com.openbravo.pos.util.OnScreenKeyboardUtil;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author adrianromero
 */
public class JCustomerFinder extends javax.swing.JDialog implements EditorCreator {

	private CustomerInfo selectedCustomer;
	private ListProvider lpr;
	private AppView m_App;
	private DataLogicCustomers m_dlCustomers;

	/** Creates new form JCustomerFinder */
	private JCustomerFinder(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JCustomerFinder */
	private JCustomerFinder(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	public static JCustomerFinder getCustomerFinder(AppView app, Component parent, DataLogicCustomers dlCustomers) {
		Window window = getWindow(parent);

		JCustomerFinder myMsg;
		if (window instanceof Frame) {
			myMsg = new JCustomerFinder((Frame) window, true);
		} else {
			myMsg = new JCustomerFinder((Dialog) window, true);
		}
		myMsg.init(app, dlCustomers);
		myMsg.applyComponentOrientation(parent.getComponentOrientation());
		return myMsg;
	}

	public CustomerInfo getSelectedCustomer() {
		return selectedCustomer;
	}

	@SuppressWarnings("unchecked")
	private void init(AppView app, DataLogicCustomers dlCustomers) {
		this.m_App = app;
		this.m_dlCustomers = dlCustomers;

		initComponents();

		PropertyUtil.ScaleScrollbar(m_App, jScrollPane1);

		// m_jtxtTaxID.addEditorKeys(m_jKeys);
		// m_jtxtSearchKey.addEditorKeys(m_jKeys);
		// m_jtxtName.addEditorKeys(m_jKeys);

		// m_jtxtTaxID.reset();
		m_jtxtSearchKey.setText("");
		m_jtxtName.setText("");

		m_jtxtSearchKey.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonSearch.doClick();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		m_jtxtName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonSearch.doClick();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		lpr = new ListProviderCreator(dlCustomers.getCustomerList(), this);

		jListCustomers.setCellRenderer(new CustomerRenderer());

		// getRootPane().setDefaultButton(jcmdOK);

		selectedCustomer = null;

		ScaleButtons();
	}

	public void search(CustomerInfo customer) {

		if (customer == null || customer.getName() == null || customer.getName().equals("")) {

			m_jtxtSearchKey.setText("");
			m_jtxtName.setText("");

			m_jtxtSearchKey.grabFocus();

			cleanSearch();
		} else {

			m_jtxtSearchKey.setText(customer.getSearchkey());
			//m_jtxtName.setText(customer.getName());

			m_jtxtSearchKey.grabFocus();

			executeSearch();
		}
	}

	@SuppressWarnings("unchecked")
	private void cleanSearch() {
		jListCustomers.setModel(new MyListData(new ArrayList()));
	}

	@SuppressWarnings("unchecked")
	public void executeSearch() {
		try {
			jListCustomers.setModel(new MyListData(lpr.loadData()));
			if (jListCustomers.getModel().getSize() > 0) {
				jListCustomers.setSelectedIndex(0);
			}
		} catch (BasicException e1) {
			JConfirmDialog.showError(m_App, JCustomerFinder.this, AppLocal.getIntString("error.network"),
					AppLocal.getIntString("message.databaseconnectionerror"), e1);
		}
	}

	public Object createValue() throws BasicException {

		Object[] afilter = new Object[4];

		// SearchKey
		if (m_jtxtSearchKey.getText() == null || m_jtxtSearchKey.getText().equals("")) {
			afilter[0] = QBFCompareEnum.COMP_NONE;
			afilter[1] = null;
		} else {
			afilter[0] = QBFCompareEnum.COMP_RE;
			afilter[1] = "%" + m_jtxtSearchKey.getText() + "%";
		}

		// Name
		if (m_jtxtName.getText() == null || m_jtxtName.getText().equals("")) {
			afilter[2] = QBFCompareEnum.COMP_NONE;
			afilter[3] = null;
		} else {
			afilter[2] = QBFCompareEnum.COMP_RE;
			afilter[3] = "%" + m_jtxtName.getText() + "%";
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

	@SuppressWarnings({ "serial", "rawtypes" })
	private static class MyListData extends javax.swing.AbstractListModel {

		private java.util.List m_data;

		public MyListData(java.util.List data) {
			m_data = data;
		}

		public Object getElementAt(int index) {
			return m_data.get(index);
		}

		public int getSize() {
			return m_data.size();
		}
	}

	@SuppressWarnings("rawtypes")
	private void initComponents() {

		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		jLabelName = new javax.swing.JLabel();
		m_jtxtName = new javax.swing.JTextField();
		jLabelSearchKey = new javax.swing.JLabel();
		m_jtxtSearchKey = new javax.swing.JTextField();
		jPanel6 = new javax.swing.JPanel();
		jButtonRefresh = new javax.swing.JButton();
		jButtonSearch = new javax.swing.JButton();
		jButtonKeyboard = new javax.swing.JButton();
		jButtonCustomer = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jListCustomers = new javax.swing.JList();
		jPanel8 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("form.customertitle")); // NOI18N

		jPanel2.setLayout(new java.awt.BorderLayout());
		// jPanel2.add(m_jKeys, java.awt.BorderLayout.NORTH);

		getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_END);

		jPanel3.setLayout(new java.awt.BorderLayout());

		jPanel5.setLayout(new java.awt.BorderLayout());

		jLabelName.setText(AppLocal.getIntString("label.prodname")); // NOI18N

		jLabelSearchKey.setText(AppLocal.getIntString("label.searchkey")); // NOI18N

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
		jPanel7.setLayout(jPanel7Layout);

		ScaleLabels();

		int width = PropertyUtil.findMaxLabelWidth(jLabelSearchKey, jLabelName, jLabelSearchKey);
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel7Layout.createSequentialGroup()
								.addComponent(jLabelSearchKey, width, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(m_jtxtSearchKey, width, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE))
						.addGroup(jPanel7Layout.createSequentialGroup()
								.addComponent(jLabelName, width, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(m_jtxtName, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)))));
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabelSearchKey).addComponent(
										m_jtxtSearchKey, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabelName).addComponent(m_jtxtName, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

		jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/refresh.png"))); // NOI18N
		jButtonRefresh.setText(AppLocal.getIntString("button.clean")); // NOI18N
		jButtonRefresh.setFocusPainted(false);
		jButtonRefresh.setFocusable(false);
		jButtonRefresh.setRequestFocusEnabled(false);
		jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonRefreshActionPerformed(evt);
			}
		});
		jPanel6.add(jButtonRefresh);

		jButtonSearch
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launchfilter.png"))); // NOI18N
		jButtonSearch.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
		jButtonSearch.setFocusPainted(false);
		jButtonSearch.setFocusable(false);
		jButtonSearch.setRequestFocusEnabled(false);
		jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSearchActionPerformed(evt);
			}
		});
		jPanel6.add(jButtonSearch);

		jButtonKeyboard.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_keyboard.png"))); // NOI18N
		jButtonKeyboard.setText(AppLocal.getIntString("Button.Keyboard"));
		jButtonKeyboard.setFocusPainted(false);
		jButtonKeyboard.setFocusable(false);
		jButtonKeyboard.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jButtonKeyboard.setRequestFocusEnabled(false);
		jButtonKeyboard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OnScreenKeyboardUtil.StartOSK();
			}
		});
		jPanel6.add(jButtonKeyboard);

		jButtonCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png"))); // NOI18N
		jButtonCustomer.setText(AppLocal.getIntString("label.customer"));
		jButtonCustomer.setFocusPainted(false);
		jButtonCustomer.setFocusable(false);
		jButtonCustomer.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jButtonCustomer.setRequestFocusEnabled(false);
		jButtonCustomer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectedCustomer = (CustomerInfo) jListCustomers.getSelectedValue();
				
				CustomerDialog dialog = CustomerDialog.getCustomerDialog(m_App, (Component) evt.getSource(),
						m_dlCustomers, selectedCustomer);
				dialog.setVisible(true);
				String customerSearchName = dialog.getCurrentCustomerSearchName();
				jButtonRefreshActionPerformed(evt);
				m_jtxtSearchKey.setText(customerSearchName);
				jButtonSearchActionPerformed(evt);
			}
		});
		jPanel6.add(jButtonCustomer);

		jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

		jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

		jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel4.setLayout(new java.awt.BorderLayout());

		jListCustomers.setFocusable(false);
		jListCustomers.setRequestFocusEnabled(false);
		jListCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jListCustomersMouseClicked(evt);
			}
		});
		jListCustomers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				jListCustomersValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(jListCustomers);

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

		PropertyUtil.ScaleDialog(m_App, this, 1024, 768);

	}// </editor-fold>//GEN-END:initComponents

	private void ScaleLabels() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelName, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelSearchKey, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jtxtName, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jtxtSearchKey, "common-dialog-fontsize", "22");

		Font f = jListCustomers.getFont();
		jListCustomers.setFont(PropertyUtil.ScaleFont(m_App, f, "common-dialog-fontsize", "22"));
	}

	@Override
	public void ScaleButtons() {
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "48"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "48"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jButtonRefresh, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButtonSearch, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButtonKeyboard, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButtonCustomer, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdOK, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdCancel, btnWidth, btnHeight, fontsize);
	}

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdOKActionPerformed

		selectedCustomer = (CustomerInfo) jListCustomers.getSelectedValue();
		dispose();

	}// GEN-LAST:event_jcmdOKActionPerformed

	private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdCancelActionPerformed

		dispose();

	}// GEN-LAST:event_jcmdCancelActionPerformed

	private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed

		executeSearch();

	}// GEN-LAST:event_jButton3ActionPerformed

	private void jListCustomersValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListCustomersValueChanged

		jcmdOK.setEnabled(jListCustomers.getSelectedValue() != null);

	}// GEN-LAST:event_jListCustomersValueChanged

	private void jListCustomersMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListCustomersMouseClicked

		if (evt.getClickCount() == 2) {
			selectedCustomer = (CustomerInfo) jListCustomers.getSelectedValue();
			dispose();
		}

	}// GEN-LAST:event_jListCustomersMouseClicked

	private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		m_jtxtSearchKey.setText("");
		m_jtxtName.setText("");

		m_jtxtSearchKey.grabFocus();

		cleanSearch();
	}// GEN-LAST:event_jButton1ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonRefresh;
	private javax.swing.JButton jButtonSearch;
	private javax.swing.JButton jButtonKeyboard;
	private javax.swing.JButton jButtonCustomer;
	private javax.swing.JLabel jLabelName;
	private javax.swing.JLabel jLabelSearchKey;
	@SuppressWarnings("rawtypes")
	private javax.swing.JList jListCustomers;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton jcmdCancel;
	private javax.swing.JButton jcmdOK;
	private javax.swing.JTextField m_jtxtName;
	private javax.swing.JTextField m_jtxtSearchKey;
	// End of variables declaration//GEN-END:variables

}
