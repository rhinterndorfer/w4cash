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

import java.awt.Component;
import java.awt.TextField;
import java.util.UUID;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import java.util.Date;

/**
 *
 * @author adrianromero
 */
public class PaymentsEditor extends javax.swing.JPanel implements EditorRecord {

	private ComboBoxValModel m_ReasonModel;

	private String m_sId;
	private String m_sPaymentId;
	private Date datenew;

	private AppView m_App;

	/** Creates new form JPanelPayments */
	public PaymentsEditor(AppView oApp, DirtyManager dirty) {
		m_App = oApp;

		initComponents();

		m_ReasonModel = new ComboBoxValModel();
		m_ReasonModel.add(new PaymentReasonPositive("cashin", AppLocal.getIntString("transpayment.cashin")));
		m_ReasonModel.add(new PaymentReasonNegative("cashout", AppLocal.getIntString("transpayment.cashout")));
		m_jreason.setModel(m_ReasonModel);

		jTotal.addEditorKeys(m_jKeys);

		m_jreason.addActionListener(dirty);
		jTotal.addPropertyChangeListener("Text", dirty);

		jDescription.addEditorKeys(m_jKeys);
		jDescription.addPropertyChangeListener("Text", dirty);

		writeValueEOF();

		m_jKeys.ScaleButtons();

		ScaleButtons();
	}

	public void writeValueEOF() {
		m_sId = null;
		m_sPaymentId = null;
		datenew = null;
		setReasonTotal(null, null);
		jDescription.setText(null);

		m_jreason.setEnabled(false);
		jDescription.setEnabled(false);
		jTotal.setEnabled(false);
	}

	public void writeValueInsert() {
		m_sId = null;
		m_sPaymentId = null;
		datenew = null;
		setReasonTotal("cashin", null);
		jDescription.setText(null);

		m_jreason.setEnabled(true);
		jDescription.setEnabled(true);
		jTotal.setEnabled(true);
		jTotal.activate();
	}

	public void writeValueDelete(Object value) {
		Object[] payment = (Object[]) value;
		m_sId = (String) payment[0];
		datenew = (Date) payment[2];
		m_sPaymentId = (String) payment[3];
		setReasonTotal(payment[4], payment[5]);
		jDescription.setText("" + payment[6]);

		jDescription.setEnabled(false);
		m_jreason.setEnabled(false);
		jTotal.setEnabled(false);
	}

	public void writeValueEdit(Object value) {
		Object[] payment = (Object[]) value;
		m_sId = (String) payment[0];
		datenew = (Date) payment[2];
		m_sPaymentId = (String) payment[3];
		setReasonTotal(payment[4], payment[5]);
		jDescription.setText((String) payment[6]);

		m_jreason.setEnabled(false);
		jDescription.setEnabled(false);
		jTotal.setEnabled(false);
		jTotal.activate();
	}

	public Object createValue() throws BasicException {
		Object[] payment = new Object[7];
		payment[0] = m_sId == null ? UUID.randomUUID().toString() : m_sId;
		payment[1] = m_App.getActiveCashIndex(true, false);
		payment[2] = datenew == null ? new Date() : datenew;
		payment[3] = m_sPaymentId == null ? UUID.randomUUID().toString() : m_sPaymentId;
		payment[4] = m_ReasonModel.getSelectedKey();
		PaymentReason reason = (PaymentReason) m_ReasonModel.getSelectedItem();
		Double dtotal = jTotal.getDoubleValue();
		payment[5] = reason == null ? dtotal : reason.addSignum(dtotal);
		payment[6] = jDescription.getText();
		return payment;
	}

	public Component getComponent() {
		return this;
	}

	public void refresh() {
	}

	private void setReasonTotal(Object reasonfield, Object totalfield) {
		m_ReasonModel.setSelectedKey(reasonfield);

		PaymentReason reason = (PaymentReason) m_ReasonModel.getSelectedItem();

		if (reason == null) {
			jTotal.setDoubleValue((Double) totalfield);
		} else {
			jTotal.setDoubleValue(reason.positivize((Double) totalfield));
		}
	}

	private static abstract class PaymentReason implements IKeyed {
		private String m_sKey;
		private String m_sText;

		public PaymentReason(String key, String text) {
			m_sKey = key;
			m_sText = text;
		}

		public Object getKey() {
			return m_sKey;
		}

		public abstract Double positivize(Double d);

		public abstract Double addSignum(Double d);

		@Override
		public String toString() {
			return m_sText;
		}
	}

	private static class PaymentReasonPositive extends PaymentReason {
		public PaymentReasonPositive(String key, String text) {
			super(key, text);
		}

		public Double positivize(Double d) {
			return d;
		}

		public Double addSignum(Double d) {
			if (d == null) {
				return null;
			} else if (d.doubleValue() < 0.0) {
				return new Double(-d.doubleValue());
			} else {
				return d;
			}
		}
	}

	private static class PaymentReasonNegative extends PaymentReason {
		public PaymentReasonNegative(String key, String text) {
			super(key, text);
		}

		public Double positivize(Double d) {
			return d == null ? null : new Double(-d.doubleValue());
		}

		public Double addSignum(Double d) {
			if (d == null) {
				return null;
			} else if (d.doubleValue() > 0.0) {
				return new Double(-d.doubleValue());
			} else {
				return d;
			}
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
		// jLabel5 = new javax.swing.JLabel();
		m_jreason = new javax.swing.JComboBox();
		// jLabel3 = new javax.swing.JLabel();
		jTotal = new com.openbravo.editor.JEditorCurrency();
		jPanel2 = new javax.swing.JPanel();
		m_jKeys = new com.openbravo.editor.JEditorKeys(m_App);
		jDescription = new com.openbravo.editor.JEditorString();

		jLabelDesc = new javax.swing.JLabel();

		setLayout(new java.awt.BorderLayout());

		jLabelDesc.setText(AppLocal.getIntString("tab.paper"));

		// jLabel5.setText(AppLocal.getIntString("label.paymentreason")); //
		// NOI18N

		m_jreason.setFocusable(false);

		// jLabel3.setText(AppLocal.getIntString("label.paymenttotal")); //
		// NOI18N

		ScaleLabels();

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);

		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						// .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						// .addComponent(jLabel5,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE)
						// .addComponent(jLabel3,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE)
						// )
						// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(m_jreason, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
								.addComponent(jTotal, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
								.addComponent(jLabelDesc, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
								.addComponent(jDescription, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE))
		// .addContainerGap(63, Short.MAX_VALUE)
		));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								// .addComponent(jLabel5)
								.addComponent(m_jreason, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jTotal, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
		// .addComponent(jLabel3)
		).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
				jLabelDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jDescription, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE))
						.addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)));

		add(jPanel3, java.awt.BorderLayout.CENTER);

		jPanel2.setLayout(new java.awt.BorderLayout());
		jPanel2.add(m_jKeys, java.awt.BorderLayout.NORTH);

		add(jPanel2, java.awt.BorderLayout.LINE_END);

		jDescription.reset();
	}// </editor-fold>//GEN-END:initComponents

	private void ScaleLabels() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabelDesc, "common-large-fontsize", "64");
		PropertyUtil.ScaleEditcurrencyFontsize(m_App, jTotal, "common-large-fontsize", "64");
		PropertyUtil.ScaleComboFontsize(m_App, m_jreason, "common-large-fontsize", "64");
		PropertyUtil.ScaleEditstringFontsize(m_App, jDescription, "common-large-fontsize", "64");
	}

	@Override
	public void ScaleButtons() {

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// private javax.swing.JLabel jLabel3;
	// private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private com.openbravo.editor.JEditorCurrency jTotal;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	private javax.swing.JComboBox m_jreason;
	private com.openbravo.editor.JEditorString jDescription;
	private javax.swing.JLabel jLabelDesc;

	// End of variables declaration//GEN-END:variables
	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}
}
