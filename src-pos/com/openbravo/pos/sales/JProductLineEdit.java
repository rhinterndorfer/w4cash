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

import com.openbravo.basic.BasicException;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class JProductLineEdit extends javax.swing.JDialog {

	private TicketLineInfo returnLine;
	private TicketLineInfo m_oLine;
	private boolean m_bunitsok;
	private boolean m_bpriceok;
	private AppView m_App;

	/** Creates new form JProductLineEdit */
	private JProductLineEdit(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JProductLineEdit */
	private JProductLineEdit(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	private TicketLineInfo init(AppView app, TicketLineInfo oLine) throws BasicException {
		// Inicializo los componentes
		this.m_App = app;
		initComponents();

		if (oLine.getTaxInfo() == null) {
			throw new BasicException(AppLocal.getIntString("message.cannotcalculatetaxes"));
		}

		m_oLine = new TicketLineInfo(oLine);
		m_bunitsok = true;
		m_bpriceok = true;

		m_jName.setEnabled(m_oLine.getProductID() == null
				&& app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
		m_jPrice.setEnabled(app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
		m_jPriceTax
				.setEnabled(app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));

		m_jName.setText(m_oLine.getProperty("product.name"));
		m_jUnits.setDoubleValue(oLine.getMultiply());
		m_jPrice.setDoubleValue(oLine.getPrice());
		m_jPriceTax.setDoubleValue(oLine.getPriceTax());
		m_jTaxrate.setText(oLine.getTaxInfo().getName());

		m_jName.addPropertyChangeListener("Edition", new RecalculateName());
		m_jUnits.addPropertyChangeListener("Edition", new RecalculateUnits());
		m_jPrice.addPropertyChangeListener("Edition", new RecalculatePrice());
		m_jPriceTax.addPropertyChangeListener("Edition", new RecalculatePriceTax());

		m_jName.addEditorKeys(m_jKeys);
		m_jUnits.addEditorKeys(m_jKeys);
		m_jPrice.addEditorKeys(m_jKeys);
		m_jPriceTax.addEditorKeys(m_jKeys);

		if (m_jName.isEnabled()) {
			m_jName.activate();
		} else {
			m_jUnits.activate();
		}

		m_jKeys.ScaleButtons();

		printTotals();

		getRootPane().setDefaultButton(m_jButtonOK);
		returnLine = null;

		ScaleButtons();

		setVisible(true);

		return returnLine;
	}

	private void ScaleLabels() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jSubtotal, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTaxrate, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTotal, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleEditnumbersFontsize(m_App, m_jUnits, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleEditcurrencyFontsize(m_App, m_jPrice, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleEditcurrencyFontsize(m_App, m_jPriceTax, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleEditstringFontsize(m_App, m_jName, "common-dialog-fontsize", "22");
	}

	private void ScaleButtons() {

	}

	private void printTotals() {

		if (m_bunitsok && m_bpriceok) {
			m_jSubtotal.setText(m_oLine.printSubValue());
			m_jTotal.setText(m_oLine.printValue());
			m_jButtonOK.setEnabled(true);
		} else {
			m_jSubtotal.setText(null);
			m_jTotal.setText(null);
			m_jButtonOK.setEnabled(false);
		}
	}

	private class RecalculateUnits implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			Double value = m_jUnits.getDoubleValue();
			if (value == null || value == 0.0) {
				m_bunitsok = false;
			} else {
				m_oLine.setMultiply(value);
				m_bunitsok = true;
			}

			printTotals();
		}
	}

	private class RecalculatePrice implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {

			Double value = m_jPrice.getDoubleValue();
			if (value == null || value == 0.0) {
				m_bpriceok = false;
			} else {
				m_oLine.setPrice(value);
				m_jPriceTax.setDoubleValue(m_oLine.getPriceTax());
				m_bpriceok = true;
			}

			printTotals();
		}
	}

	private class RecalculatePriceTax implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {

			Double value = m_jPriceTax.getDoubleValue();
			if (value == null || value == 0.0) {
				// m_jPriceTax.setValue(m_oLine.getPriceTax());
				m_bpriceok = false;
			} else {
				m_oLine.setPriceTax(value);
				m_jPrice.setDoubleValue(m_oLine.getPrice());
				m_bpriceok = true;
			}

			printTotals();
		}
	}

	private class RecalculateName implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			m_oLine.setProperty("product.name", m_jName.getText());
		}
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

	public static TicketLineInfo showMessage(Component parent, AppView app, TicketLineInfo oLine)
			throws BasicException {

		Window window = getWindow(parent);

		JProductLineEdit myMsg;
		if (window instanceof Frame) {
			myMsg = new JProductLineEdit((Frame) window, true);
		} else {
			myMsg = new JProductLineEdit((Dialog) window, true);
		}
		return myMsg.init(app, oLine);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel5 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		m_jName = new com.openbravo.editor.JEditorString();
		m_jUnits = new com.openbravo.editor.JEditorDouble();
		m_jPrice = new com.openbravo.editor.JEditorCurrency();
		m_jPriceTax = new com.openbravo.editor.JEditorCurrency();
		m_jTaxrate = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		m_jTotal = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		m_jSubtotal = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		m_jButtonOK = new javax.swing.JButton();
		m_jButtonCancel = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		m_jKeys = new com.openbravo.editor.JEditorKeys(m_App);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("label.editline")); // NOI18N

		jPanel5.setLayout(new java.awt.BorderLayout());

		jPanel2.setLayout(new GridLayout(7, 2, 5,5));
		// GridBagConstraints layoutData = new GridBagConstraints();

		jLabel1.setText(AppLocal.getIntString("label.price")); // NOI18N
		jLabel2.setText(AppLocal.getIntString("label.units")); // NOI18N
		jLabel3.setText(AppLocal.getIntString("label.pricetax")); // NOI18N
		jLabel4.setText(AppLocal.getIntString("label.item")); // NOI18N

		m_jTaxrate.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
		m_jTaxrate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jTaxrate.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTaxrate.setOpaque(true);
		// m_jTaxrate.setPreferredSize(new java.awt.Dimension(150, 25));
		m_jTaxrate.setRequestFocusEnabled(false);

		jLabel5.setText(AppLocal.getIntString("label.tax")); // NOI18N

		jLabel6.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
		
		m_jTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
		m_jTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTotal.setOpaque(true);
		// m_jTotal.setPreferredSize(new java.awt.Dimension(150, 25));
		m_jTotal.setRequestFocusEnabled(false);

		jLabel7.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

		m_jSubtotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
		m_jSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jSubtotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jSubtotal.setOpaque(true);
		// m_jSubtotal.setPreferredSize(new java.awt.Dimension(150, 25));
		m_jSubtotal.setRequestFocusEnabled(false);

		ScaleLabels();

		jPanel2.add(jLabel4);
		jPanel2.add(m_jName);
		jPanel2.add(jLabel2);
		jPanel2.add(m_jUnits);
		jPanel2.add(jLabel1);
		jPanel2.add(m_jPrice);
		jPanel2.add(jLabel3);
		jPanel2.add(m_jPriceTax);

		jPanel2.add(jLabel5);
		jPanel2.add(m_jTaxrate);
		jPanel2.add(jLabel7 );
		jPanel2.add(m_jSubtotal);
		jPanel2.add(jLabel6);
		jPanel2.add(m_jTotal);

		// PropertyUtil.setGridBagConstraints(layoutData, 0, 2,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel1, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 1,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel2, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 3,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel3, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 0,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel4, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 0,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jName, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 1,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jUnits, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 2,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jPrice, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 3,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jPriceTax, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 4,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jTaxrate, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 4,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel5, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 6,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel6, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 6,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jTotal, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 0, 5,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(jLabel7, layoutData);
		// PropertyUtil.setGridBagConstraints(layoutData, 1, 5,
		// GridBagConstraints.HORIZONTAL);
		// jPanel2.add(m_jSubtotal, layoutData);

		jPanel5.add(jPanel2, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

		m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
		m_jButtonOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		m_jButtonOK.setFocusPainted(false);
		m_jButtonOK.setFocusable(false);
		m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonOK.setRequestFocusEnabled(false);
		m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonOKActionPerformed(evt);
			}
		});
		jPanel1.add(m_jButtonOK);

		m_jButtonCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
		m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		m_jButtonCancel.setFocusPainted(false);
		m_jButtonCancel.setFocusable(false);
		m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonCancel.setRequestFocusEnabled(false);
		m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonCancelActionPerformed(evt);
			}
		});
		jPanel1.add(m_jButtonCancel);

		jPanel5.add(jPanel1, java.awt.BorderLayout.SOUTH);

		getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

		jPanel3.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));
		jPanel4.add(m_jKeys);

		jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

		getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

		PropertyUtil.ScaleDialog(m_App, this, 860, 410);

		// java.awt.Dimension screenSize =
		// java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds((screenSize.width - 580) / 2, (screenSize.height - 362) /
		// 2, 580, 362);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonCancelActionPerformed

		dispose();

	}// GEN-LAST:event_m_jButtonCancelActionPerformed

	private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonOKActionPerformed

		returnLine = m_oLine;

		dispose();

	}// GEN-LAST:event_m_jButtonOKActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JButton m_jButtonCancel;
	private javax.swing.JButton m_jButtonOK;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	private com.openbravo.editor.JEditorString m_jName;
	private com.openbravo.editor.JEditorCurrency m_jPrice;
	private com.openbravo.editor.JEditorCurrency m_jPriceTax;
	private javax.swing.JLabel m_jSubtotal;
	private javax.swing.JLabel m_jTaxrate;
	private javax.swing.JLabel m_jTotal;
	private com.openbravo.editor.JEditorDouble m_jUnits;
	// End of variables declaration//GEN-END:variables

}
