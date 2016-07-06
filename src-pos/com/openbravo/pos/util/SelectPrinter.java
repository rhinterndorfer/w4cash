//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2009 Openbravo, S.L.
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

package com.openbravo.pos.util;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.SwingUtilities;

/**
 *
 * @author adrian
 */
public class SelectPrinter extends javax.swing.JDialog {

	private String printservice;
	private boolean ok;
	private AppView m_App;

	/** Creates new form SelectPrinter */
	private SelectPrinter(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form SelectPrinter */
	private SelectPrinter(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	public static SelectPrinter getSelectPrinter(Component parent, String[] printers, AppView app) {

		Window window = SwingUtilities.windowForComponent(parent);

		SelectPrinter myMsg;
		if (window instanceof Frame) {
			myMsg = new SelectPrinter((Frame) window, true);
		} else {
			myMsg = new SelectPrinter((Dialog) window, true);
		}
		myMsg.init(app, printers);
		myMsg.applyComponentOrientation(parent.getComponentOrientation());
		return myMsg;
	}

	private void init(AppView app, String[] printers) {
		this.m_App = app;
		initComponents();

		jPrinters.removeAllItems();
		jPrinters.addItem("(Default)");
		for (String name : printers) {
			jPrinters.addItem(name);
		}

		jPrinters.setSelectedIndex(0);

		getRootPane().setDefaultButton(jcmdOK);

		ok = false;
		printservice = null;
	}

	public boolean isOK() {
		return ok;
	}

	public String getPrintService() {
		return printservice;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel8 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();
		jPrinters = new javax.swing.JComboBox();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("form.selectprintertitle")); // NOI18N

		jPanel8.setLayout(new java.awt.BorderLayout());

		jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdOKActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdOK);

		jcmdCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdCancelActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdCancel);

		jPanel8.add(jPanel1, java.awt.BorderLayout.LINE_END);

		getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);

		jLabel7.setText(AppLocal.getIntString("Label.MachinePrinter")); // NOI18N

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(
						jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
										.addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 165,
												javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(30, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7).addComponent(jPrinters, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(57, Short.MAX_VALUE)));

		getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

		PropertyUtil.ScaleDialog(m_App, this, 359, 176);

		// java.awt.Dimension screenSize =
		// java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds((screenSize.width - 359) / 2, (screenSize.height - 176) / 2, 359, 176);
	}// </editor-fold>//GEN-END:initComponents

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdOKActionPerformed

		ok = true;
		printservice = (String) jPrinters.getSelectedItem();
		dispose();

	}// GEN-LAST:event_jcmdOKActionPerformed

	private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdCancelActionPerformed

		dispose();

	}// GEN-LAST:event_jcmdCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JComboBox jPrinters;
	private javax.swing.JButton jcmdCancel;
	private javax.swing.JButton jcmdOK;
	// End of variables declaration//GEN-END:variables

}
