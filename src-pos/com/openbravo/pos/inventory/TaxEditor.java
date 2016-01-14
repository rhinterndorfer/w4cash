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

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;
import javax.swing.*;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.util.PropertyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaxEditor extends JPanel implements EditorRecord {

	private Object m_oId;

	private SentenceList taxcatsent;
	private ComboBoxValModel taxcatmodel;

	private SentenceList taxcustcatsent;
	private ComboBoxValModel taxcustcatmodel;

	private SentenceList taxparentsent;
	private ComboBoxValModel taxparentmodel;

	private AppView m_App;

	/** Creates new form taxEditor */
	public TaxEditor(AppView app, DirtyManager dirty) {

		DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
		m_App = app;
		initComponents();

		taxcatsent = dlSales.getTaxCategoriesList();
		taxcatmodel = new ComboBoxValModel();

		taxcustcatsent = dlSales.getTaxCustCategoriesList();
		taxcustcatmodel = new ComboBoxValModel();

		taxparentsent = dlSales.getTaxList();
		taxparentmodel = new ComboBoxValModel();

		m_jName.getDocument().addDocumentListener(dirty);
		m_jTaxCategory.addActionListener(dirty);
		txtValidFrom.getDocument().addDocumentListener(dirty);
		m_jCustTaxCategory.addActionListener(dirty);
		m_jTaxParent.addActionListener(dirty);
		m_jRate.getDocument().addDocumentListener(dirty);
		jCascade.addActionListener(dirty);
		jOrder.getDocument().addDocumentListener(dirty);

		writeValueEOF();

		ScaleButtons();
	}

	public void activate() throws BasicException {

		List a = taxcatsent.list();
		taxcatmodel = new ComboBoxValModel(a);
		m_jTaxCategory.setModel(taxcatmodel);

		a = taxcustcatsent.list();
		a.add(0, null); // The null item
		taxcustcatmodel = new ComboBoxValModel(a);
		m_jCustTaxCategory.setModel(taxcustcatmodel);

	}

	public void refresh() {

		List a;

		try {
			a = taxparentsent.list();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotloadlists"),
					eD);
			msg.show(m_App, this);
			a = new ArrayList();
		}

		a.add(0, null); // The null item
		taxparentmodel = new ComboBoxValModel(a);
		m_jTaxParent.setModel(taxparentmodel);
	}

	public void writeValueEOF() {
		m_oId = null;
		m_jName.setText(null);
		taxcatmodel.setSelectedKey(null);
		txtValidFrom.setText(null);
		taxcustcatmodel.setSelectedKey(null);
		taxparentmodel.setSelectedKey(null);
		m_jRate.setText(null);
		jCascade.setSelected(false);
		jOrder.setText(null);

		m_jName.setEnabled(false);
		m_jTaxCategory.setEnabled(false);
		txtValidFrom.setEnabled(false);
		btnValidFrom.setEnabled(false);
		m_jCustTaxCategory.setEnabled(false);
		m_jTaxParent.setEnabled(false);
		m_jRate.setEnabled(false);
		jCascade.setEnabled(false);
		jOrder.setEnabled(false);
	}

	public void writeValueInsert() {
		m_oId = UUID.randomUUID().toString();
		m_jName.setText(null);
		taxcatmodel.setSelectedKey(null);
		txtValidFrom.setText(null);
		taxcustcatmodel.setSelectedKey(null);
		taxparentmodel.setSelectedKey(null);
		m_jRate.setText(null);
		jCascade.setSelected(false);
		jOrder.setText(null);

		m_jName.setEnabled(true);
		m_jTaxCategory.setEnabled(true);
		txtValidFrom.setEnabled(true);
		btnValidFrom.setEnabled(true);
		m_jCustTaxCategory.setEnabled(true);
		m_jTaxParent.setEnabled(true);
		m_jRate.setEnabled(true);
		jCascade.setEnabled(true);
		jOrder.setEnabled(true);
	}

	public void writeValueDelete(Object value) {

		Object[] tax = (Object[]) value;
		m_oId = tax[0];
		m_jName.setText(Formats.STRING.formatValue(tax[1]));
		taxcatmodel.setSelectedKey(tax[2]);
		txtValidFrom.setText(Formats.TIMESTAMP.formatValue(tax[3]));
		taxcustcatmodel.setSelectedKey(tax[4]);
		taxparentmodel.setSelectedKey(tax[5]);
		m_jRate.setText(Formats.PERCENT.formatValue(tax[6]));
		jCascade.setSelected((Boolean) tax[7]);
		jOrder.setText(Formats.INT.formatValue(tax[8]));

		m_jName.setEnabled(false);
		m_jTaxCategory.setEnabled(false);
		txtValidFrom.setEnabled(false);
		btnValidFrom.setEnabled(false);
		m_jCustTaxCategory.setEnabled(false);
		m_jTaxParent.setEnabled(false);
		m_jRate.setEnabled(false);
		jCascade.setEnabled(false);
		jOrder.setEnabled(false);
	}

	public void writeValueEdit(Object value) {

		Object[] tax = (Object[]) value;
		m_oId = tax[0];
		m_jName.setText(Formats.STRING.formatValue(tax[1]));
		taxcatmodel.setSelectedKey(tax[2]);
		txtValidFrom.setText(Formats.TIMESTAMP.formatValue(tax[3]));
		taxcustcatmodel.setSelectedKey(tax[4]);
		taxparentmodel.setSelectedKey(tax[5]);
		m_jRate.setText(Formats.PERCENT.formatValue(tax[6]));
		jCascade.setSelected((Boolean) tax[7]);
		jOrder.setText(Formats.INT.formatValue(tax[8]));

		m_jName.setEnabled(true);
		m_jTaxCategory.setEnabled(true);
		txtValidFrom.setEnabled(true);
		btnValidFrom.setEnabled(true);
		m_jCustTaxCategory.setEnabled(true);
		m_jTaxParent.setEnabled(true);
		m_jRate.setEnabled(true);
		jCascade.setEnabled(true);
		jOrder.setEnabled(true);
	}

	public Object createValue() throws BasicException {

		Object[] tax = new Object[9];

		tax[0] = m_oId;
		tax[1] = m_jName.getText();
		tax[2] = taxcatmodel.getSelectedKey();
		tax[3] = Formats.TIMESTAMP.parseValue(txtValidFrom.getText());
		tax[4] = taxcustcatmodel.getSelectedKey();
		tax[5] = taxparentmodel.getSelectedKey();
		tax[6] = Formats.PERCENT.parseValue(m_jRate.getText());
		tax[7] = Boolean.valueOf(jCascade.isSelected());
		tax[8] = Formats.INT.parseValue(jOrder.getText());

		return tax;
	}

	public Component getComponent() {
		return this;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		m_jName = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		m_jRate = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jCascade = new javax.swing.JCheckBox();
		m_jTaxCategory = new javax.swing.JComboBox();
		m_jTaxParent = new javax.swing.JComboBox();
		m_jCustTaxCategory = new javax.swing.JComboBox();
		jLabel6 = new javax.swing.JLabel();
		jOrder = new javax.swing.JTextField();
		jLabel7 = new javax.swing.JLabel();
		txtValidFrom = new javax.swing.JTextField();
		btnValidFrom = new javax.swing.JButton();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JScrollPane scrollView = new JScrollPane();

		JPanel root = new JPanel();
		scrollView.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollView.setViewportView(root);
		add(scrollView);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(0, 0, 5, 5);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		root.add(jLabel2, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 2;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		root.add(m_jName, gbc_textPane);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(0, 0, 5, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 4;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		jLabel1.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(0, 0, 5, 5);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		root.add(jLabel1, gbc_lbl2);

		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 2;
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(0, 0, 5, 5);
		gbc_textPane2.weightx = 1.0;
		gbc_textPane2.gridx = 1;
		gbc_textPane2.gridy = 1;
		root.add(m_jTaxCategory, gbc_textPane2);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(0, 0, 5, 0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 4;
		gbc_space2.gridy = 1;
		root.add(space2, gbc_space2);

		jLabel7.setText(AppLocal.getIntString("Label.ValidFrom")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(0, 0, 5, 5);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		root.add(jLabel7, gbc_lbl3);

		GridBagConstraints gbc_textPane3 = new GridBagConstraints();
		gbc_textPane3.gridwidth = 2;
		gbc_textPane3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane3.insets = new Insets(0, 0, 5, 5);
		gbc_textPane3.weightx = 1.0;
		gbc_textPane3.gridx = 1;
		gbc_textPane3.gridy = 2;
		root.add(txtValidFrom, gbc_textPane3);

		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(0, 0, 5, 5);
		gbc_lbl4.gridx = 3;
		gbc_lbl4.gridy = 2;
		root.add(btnValidFrom, gbc_lbl4);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.insets = new Insets(0, 0, 5, 0);
		gbc_space3.weightx = 1.0;
		gbc_space3.gridx = 4;
		gbc_space3.gridy = 2;
		root.add(space3, gbc_space3);

		jLabel4.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.anchor = GridBagConstraints.WEST;
		gbc_lbl5.insets = new Insets(0, 0, 5, 5);
		gbc_lbl5.gridx = 0;
		gbc_lbl5.gridy = 3;
		root.add(jLabel4, gbc_lbl5);

		GridBagConstraints gbc_textPane6 = new GridBagConstraints();
		gbc_textPane6.gridwidth = 2;
		gbc_textPane6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane6.insets = new Insets(0, 0, 5, 5);
		gbc_textPane6.weightx = 1.0;
		gbc_textPane6.gridx = 1;
		gbc_textPane6.gridy = 3;
		root.add(m_jCustTaxCategory, gbc_textPane6);

		JLabel space4 = new JLabel("");
		GridBagConstraints gbc_space4 = new GridBagConstraints();
		gbc_space4.insets = new Insets(0, 0, 5, 0);
		gbc_space4.weightx = 1.0;
		gbc_space4.gridx = 4;
		gbc_space4.gridy = 3;
		root.add(space4, gbc_space4);

		jLabel5.setText(AppLocal.getIntString("label.taxparent")); // NOI18N
		GridBagConstraints gbc_lbl6 = new GridBagConstraints();
		gbc_lbl6.anchor = GridBagConstraints.WEST;
		gbc_lbl6.insets = new Insets(0, 0, 5, 5);
		gbc_lbl6.gridx = 0;
		gbc_lbl6.gridy = 4;
		root.add(jLabel5, gbc_lbl6);

		GridBagConstraints gbc_textPane7 = new GridBagConstraints();
		gbc_textPane7.gridwidth = 2;
		gbc_textPane7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane7.insets = new Insets(0, 0, 5, 5);
		gbc_textPane7.weightx = 1.0;
		gbc_textPane7.gridx = 1;
		gbc_textPane7.gridy = 4;
		root.add(m_jTaxParent, gbc_textPane7);

		JLabel space5 = new JLabel("");
		GridBagConstraints gbc_space5 = new GridBagConstraints();
		gbc_space5.insets = new Insets(0, 0, 5, 0);
		gbc_space5.weightx = 1.0;
		gbc_space5.gridx = 4;
		gbc_space5.gridy = 4;
		root.add(space5, gbc_space5);

		jLabel3.setText(AppLocal.getIntString("label.dutyrate")); // NOI18N
		GridBagConstraints gbc_lbl7 = new GridBagConstraints();
		gbc_lbl7.anchor = GridBagConstraints.WEST;
		gbc_lbl7.insets = new Insets(0, 0, 5, 5);
		gbc_lbl7.gridx = 0;
		gbc_lbl7.gridy = 5;
		root.add(jLabel3, gbc_lbl7);

		GridBagConstraints gbc_textPane8 = new GridBagConstraints();
		gbc_textPane8.gridwidth = 1;
		gbc_textPane8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane8.insets = new Insets(0, 0, 5, 5);
		gbc_textPane8.weightx = 1.0;
		gbc_textPane8.gridx = 1;
		gbc_textPane8.gridy = 5;
		root.add(m_jRate, gbc_textPane8);

		jCascade.setText(AppLocal.getIntString("label.cascade")); // NOI18N
		GridBagConstraints gbc_textPane9 = new GridBagConstraints();
		gbc_textPane9.gridwidth = 1;
		gbc_textPane9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane9.insets = new Insets(0, 0, 5, 5);
		gbc_textPane9.weightx = 1.0;
		gbc_textPane9.gridx = 2;
		gbc_textPane9.gridy = 5;
		root.add(jCascade, gbc_textPane9);

		JLabel space6 = new JLabel("");
		GridBagConstraints gbc_space6 = new GridBagConstraints();
		gbc_space6.insets = new Insets(0, 0, 5, 0);
		gbc_space6.weightx = 2.0;
		gbc_space6.gridx = 4;
		gbc_space6.gridy = 5;
		root.add(space6, gbc_space6);

		jLabel6.setText(AppLocal.getIntString("label.order")); // NOI18N
		GridBagConstraints gbc_lbl9 = new GridBagConstraints();
		gbc_lbl9.anchor = GridBagConstraints.WEST;
		gbc_lbl9.insets = new Insets(0, 0, 5, 5);
		gbc_lbl9.gridx = 0;
		gbc_lbl9.gridy = 6;
		root.add(jLabel6, gbc_lbl9);

		GridBagConstraints gbc_textPane10 = new GridBagConstraints();
		gbc_textPane10.gridwidth = 1;
		gbc_textPane10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane10.insets = new Insets(0, 0, 5, 5);
		gbc_textPane10.weightx = 1.0;
		gbc_textPane10.gridx = 1;
		gbc_textPane10.gridy = 6;
		root.add(jOrder, gbc_textPane10);

		JLabel space7 = new JLabel("");
		GridBagConstraints gbc_space7 = new GridBagConstraints();
		gbc_space7.insets = new Insets(0, 0, 5, 0);
		gbc_space7.weightx = 1.0;
		gbc_space7.gridx = 4;
		gbc_space7.gridy = 6;
		root.add(space7, gbc_space7);

		JLabel space8 = new JLabel("");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.weighty = 1.0;
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 7;
		root.add(space8, gbc_btnNewButton);

		btnValidFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		btnValidFrom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnValidFromActionPerformed(evt);
			}
		});

	}// </editor-fold>//GEN-END:initComponents

	private void btnValidFromActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnValidFromActionPerformed

		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(txtValidFrom.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			txtValidFrom.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnValidFromActionPerformed

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnValidFrom;
	private javax.swing.JCheckBox jCascade;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JTextField jOrder;
	private javax.swing.JComboBox m_jCustTaxCategory;
	private javax.swing.JTextField m_jName;
	private javax.swing.JTextField m_jRate;
	private javax.swing.JComboBox m_jTaxCategory;
	private javax.swing.JComboBox m_jTaxParent;
	private javax.swing.JTextField txtValidFrom;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jRate, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jOrder, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtValidFrom, "common-small-fontsize", "32");

		PropertyUtil.ScaleComboFontsize(m_App, m_jTaxCategory, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jTaxParent, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jCustTaxCategory, "common-small-fontsize", "32");

		PropertyUtil.ScaleCheckboxSize(m_App, jCascade, "common-small-fontsize", "32");
		
		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(btnValidFrom, menuwidth, menuheight, fontsize);
	}

}
