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
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.StringUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author adrianromero
 */
public class CustomersView extends javax.swing.JPanel implements EditorRecord {

	private static final long serialVersionUID = 9096171464180942921L;

	private Object m_oId;

	private SentenceList m_sentcat;
	private ComboBoxValModel m_CategoryModel;

	private DirtyManager m_Dirty;

	private AppView m_App;

	/** Creates new form CustomersView */
	public CustomersView(AppView app, DirtyManager dirty) {
		this.m_App = app;
		DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

		initComponents();

		m_sentcat = dlSales.getTaxCustCategoriesList();
		m_CategoryModel = new ComboBoxValModel();

		m_Dirty = dirty;
		m_jTaxID.getDocument().addDocumentListener(dirty);
		m_jSearchkey.getDocument().addDocumentListener(dirty);
		m_jName.getDocument().addDocumentListener(dirty);
		m_jCategory.addActionListener(dirty);
		m_jNotes.getDocument().addDocumentListener(dirty);
		txtMaxdebt.getDocument().addDocumentListener(dirty);
		m_jVisible.addActionListener(dirty);

		txtFirstName.getDocument().addDocumentListener(dirty);
		txtLastName.getDocument().addDocumentListener(dirty);
		txtEmail.getDocument().addDocumentListener(dirty);
		txtPhone.getDocument().addDocumentListener(dirty);
		txtPhone2.getDocument().addDocumentListener(dirty);
		txtFax.getDocument().addDocumentListener(dirty);

		txtAddress.getDocument().addDocumentListener(dirty);
		txtAddress2.getDocument().addDocumentListener(dirty);
		txtPostal.getDocument().addDocumentListener(dirty);
		txtCity.getDocument().addDocumentListener(dirty);
		txtRegion.getDocument().addDocumentListener(dirty);
		txtCountry.getDocument().addDocumentListener(dirty);

		writeValueEOF();

		ScaleButtons();
	}

	public void activate() throws BasicException {

		List a = m_sentcat.list();
		a.add(0, null); // The null item
		m_CategoryModel = new ComboBoxValModel(a);
		m_jCategory.setModel(m_CategoryModel);
	}

	public void refresh() {
	}

	public void writeValueEOF() {
		m_oId = null;
		m_jTaxID.setText(null);
		m_jSearchkey.setText(null);
		m_jName.setText(null);
		m_CategoryModel.setSelectedKey(null);
		m_jNotes.setText(null);
		txtMaxdebt.setText(null);
		txtCurdebt.setText(null);
		txtCurdate.setText(null);
		m_jVisible.setSelected(false);
		jcard.setText(null);

		txtFirstName.setText(null);
		txtLastName.setText(null);
		txtEmail.setText(null);
		txtPhone.setText(null);
		txtPhone2.setText(null);
		txtFax.setText(null);

		txtAddress.setText(null);
		txtAddress2.setText(null);
		txtPostal.setText(null);
		txtCity.setText(null);
		txtRegion.setText(null);
		txtCountry.setText(null);

		m_jTaxID.setEnabled(false);
		m_jSearchkey.setEnabled(false);
		m_jName.setEnabled(false);
		m_jCategory.setEnabled(false);
		m_jNotes.setEnabled(false);
		txtMaxdebt.setEnabled(false);
		txtCurdebt.setEnabled(false);
		txtCurdate.setEnabled(false);
		m_jVisible.setEnabled(false);
		jcard.setEnabled(false);

		txtFirstName.setEnabled(false);
		txtLastName.setEnabled(false);
		txtEmail.setEnabled(false);
		txtPhone.setEnabled(false);
		txtPhone2.setEnabled(false);
		txtFax.setEnabled(false);

		txtAddress.setEnabled(false);
		txtAddress2.setEnabled(false);
		txtPostal.setEnabled(false);
		txtCity.setEnabled(false);
		txtRegion.setEnabled(false);
		txtCountry.setEnabled(false);

		jButton2.setEnabled(false);
		jButton3.setEnabled(false);
	}

	public void writeValueInsert() {
		m_oId = null;
		m_jTaxID.setText(null);
		m_jSearchkey.setText(null);
		m_jName.setText(null);
		m_CategoryModel.setSelectedKey(null);
		m_jNotes.setText(null);
		txtMaxdebt.setText(null);
		txtCurdebt.setText(null);
		txtCurdate.setText(null);
		m_jVisible.setSelected(true);
		jcard.setText(null);

		txtFirstName.setText(null);
		txtLastName.setText(null);
		txtEmail.setText(null);
		txtPhone.setText(null);
		txtPhone2.setText(null);
		txtFax.setText(null);

		txtAddress.setText(null);
		txtAddress2.setText(null);
		txtPostal.setText(null);
		txtCity.setText(null);
		txtRegion.setText(null);
		txtCountry.setText(null);

		m_jTaxID.setEnabled(true);
		m_jSearchkey.setEnabled(true);
		m_jName.setEnabled(true);
		m_jCategory.setEnabled(true);
		m_jNotes.setEnabled(true);
		txtMaxdebt.setEnabled(true);
		txtCurdebt.setEnabled(true);
		txtCurdate.setEnabled(true);
		m_jVisible.setEnabled(true);
		jcard.setEnabled(true);

		txtFirstName.setEnabled(true);
		txtLastName.setEnabled(true);
		txtEmail.setEnabled(true);
		txtPhone.setEnabled(true);
		txtPhone2.setEnabled(true);
		txtFax.setEnabled(true);

		txtAddress.setEnabled(true);
		txtAddress2.setEnabled(true);
		txtPostal.setEnabled(true);
		txtCity.setEnabled(true);
		txtRegion.setEnabled(true);
		txtCountry.setEnabled(true);

		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
	}

	public void writeValueDelete(Object value) {
		Object[] customer = (Object[]) value;
		m_oId = customer[0];
		m_jTaxID.setText((String) customer[1]);
		m_jSearchkey.setText((String) customer[2]);
		m_jName.setText((String) customer[3]);
		m_jNotes.setText((String) customer[4]);
		m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
		jcard.setText((String) customer[6]);
		txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
		txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
		txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

		txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
		txtLastName.setText(Formats.STRING.formatValue(customer[11]));
		txtEmail.setText(Formats.STRING.formatValue(customer[12]));
		txtPhone.setText(Formats.STRING.formatValue(customer[13]));
		txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
		txtFax.setText(Formats.STRING.formatValue(customer[15]));

		txtAddress.setText(Formats.STRING.formatValue(customer[16]));
		txtAddress2.setText(Formats.STRING.formatValue(customer[17]));
		txtPostal.setText(Formats.STRING.formatValue(customer[18]));
		txtCity.setText(Formats.STRING.formatValue(customer[19]));
		txtRegion.setText(Formats.STRING.formatValue(customer[20]));
		txtCountry.setText(Formats.STRING.formatValue(customer[21]));

		m_CategoryModel.setSelectedKey(customer[22]);

		m_jTaxID.setEnabled(false);
		m_jSearchkey.setEnabled(false);
		m_jName.setEnabled(false);
		m_jNotes.setEnabled(false);
		txtMaxdebt.setEnabled(false);
		txtCurdebt.setEnabled(false);
		txtCurdate.setEnabled(false);
		m_jVisible.setEnabled(false);
		jcard.setEnabled(false);

		txtFirstName.setEnabled(false);
		txtLastName.setEnabled(false);
		txtEmail.setEnabled(false);
		txtPhone.setEnabled(false);
		txtPhone2.setEnabled(false);
		txtFax.setEnabled(false);

		txtAddress.setEnabled(false);
		txtAddress2.setEnabled(false);
		txtPostal.setEnabled(false);
		txtCity.setEnabled(false);
		txtRegion.setEnabled(false);
		txtCountry.setEnabled(false);

		m_jCategory.setEnabled(false);

		jButton2.setEnabled(false);
		jButton3.setEnabled(false);
	}

	public void writeValueEdit(Object value) {
		Object[] customer = (Object[]) value;
		m_oId = customer[0];
		m_jTaxID.setText((String) customer[1]);
		m_jSearchkey.setText((String) customer[2]);
		m_jName.setText((String) customer[3]);
		m_jNotes.setText((String) customer[4]);
		m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
		jcard.setText((String) customer[6]);
		txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
		txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
		txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

		txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
		txtLastName.setText(Formats.STRING.formatValue(customer[11]));
		txtEmail.setText(Formats.STRING.formatValue(customer[12]));
		txtPhone.setText(Formats.STRING.formatValue(customer[13]));
		txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
		txtFax.setText(Formats.STRING.formatValue(customer[15]));

		txtAddress.setText(Formats.STRING.formatValue(customer[16]));
		txtAddress2.setText(Formats.STRING.formatValue(customer[17]));
		txtPostal.setText(Formats.STRING.formatValue(customer[18]));
		txtCity.setText(Formats.STRING.formatValue(customer[19]));
		txtRegion.setText(Formats.STRING.formatValue(customer[20]));
		txtCountry.setText(Formats.STRING.formatValue(customer[21]));

		m_CategoryModel.setSelectedKey(customer[22]);

		m_jTaxID.setEnabled(true);
		m_jSearchkey.setEnabled(true);
		m_jName.setEnabled(true);
		m_jNotes.setEnabled(true);
		txtMaxdebt.setEnabled(true);
		txtCurdebt.setEnabled(true);
		txtCurdate.setEnabled(true);
		m_jVisible.setEnabled(true);
		jcard.setEnabled(true);

		txtFirstName.setEnabled(true);
		txtLastName.setEnabled(true);
		txtEmail.setEnabled(true);
		txtPhone.setEnabled(true);
		txtPhone2.setEnabled(true);
		txtFax.setEnabled(true);

		txtAddress.setEnabled(true);
		txtAddress2.setEnabled(true);
		txtPostal.setEnabled(true);
		txtCity.setEnabled(true);
		txtRegion.setEnabled(true);
		txtCountry.setEnabled(true);

		m_jCategory.setEnabled(true);

		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
	}

	public Object createValue() throws BasicException {
		Object[] customer = new Object[23];
		customer[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
		customer[1] = m_jTaxID.getText();
		customer[2] = m_jSearchkey.getText();
		customer[3] = m_jName.getText();
		customer[4] = m_jNotes.getText();
		customer[5] = Boolean.valueOf(m_jVisible.isSelected());
		customer[6] = Formats.STRING.parseValue(jcard.getText()); // Format to
																	// manage
																	// NULL
																	// values
		customer[7] = Formats.CURRENCY.parseValue(txtMaxdebt.getText(), new Double(0.0));
		customer[8] = Formats.TIMESTAMP.parseValue(txtCurdate.getText()); // not
																			// saved
		customer[9] = Formats.CURRENCY.parseValue(txtCurdebt.getText()); // not
																			// saved

		customer[10] = Formats.STRING.parseValue(txtFirstName.getText());
		customer[11] = Formats.STRING.parseValue(txtLastName.getText());
		customer[12] = Formats.STRING.parseValue(txtEmail.getText());
		customer[13] = Formats.STRING.parseValue(txtPhone.getText());
		customer[14] = Formats.STRING.parseValue(txtPhone2.getText());
		customer[15] = Formats.STRING.parseValue(txtFax.getText());

		customer[16] = Formats.STRING.parseValue(txtAddress.getText());
		customer[17] = Formats.STRING.parseValue(txtAddress2.getText());
		customer[18] = Formats.STRING.parseValue(txtPostal.getText());
		customer[19] = Formats.STRING.parseValue(txtCity.getText());
		customer[20] = Formats.STRING.parseValue(txtRegion.getText());
		customer[21] = Formats.STRING.parseValue(txtCountry.getText());

		customer[22] = m_CategoryModel.getSelectedKey();

		return customer;
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

		jLabel7 = new javax.swing.JLabel();
		m_jTaxID = new javax.swing.JTextField(80);
		jLabel8 = new javax.swing.JLabel();
		m_jSearchkey = new javax.swing.JTextField(80);
		m_jSearchkey.setBackground(new Color(255,230,230));
		jLabel3 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField(80);
		m_jName.setBackground(new Color(255,230,230));
		jLabel4 = new javax.swing.JLabel();
		m_jVisible = new javax.swing.JCheckBox();

		jLabel5 = new javax.swing.JLabel();
		jLabel5.setVisible(false);
		jcard = new javax.swing.JTextField();
		jcard.setVisible(false);
		jLabel9 = new javax.swing.JLabel();
		jLabel9.setVisible(false);
		m_jCategory = new javax.swing.JComboBox();
		m_jCategory.setVisible(false);
		jButton2 = new javax.swing.JButton();
		jButton2.setVisible(false);
		jButton3 = new javax.swing.JButton();
		jButton3.setVisible(false);
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setVisible(false);
		txtMaxdebt = new javax.swing.JTextField();
		txtMaxdebt.setVisible(false);

		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jLabel14 = new javax.swing.JLabel();
		txtFax = new javax.swing.JTextField();
		jLabel15 = new javax.swing.JLabel();
		txtLastName = new javax.swing.JTextField();
		jLabel16 = new javax.swing.JLabel();
		txtEmail = new javax.swing.JTextField();
		jLabel17 = new javax.swing.JLabel();
		txtPhone = new javax.swing.JTextField();
		jLabel18 = new javax.swing.JLabel();
		txtPhone2 = new javax.swing.JTextField();
		jLabel19 = new javax.swing.JLabel();
		txtFirstName = new javax.swing.JTextField();
		jPanel2 = new javax.swing.JPanel();
		jLabel13 = new javax.swing.JLabel();
		txtAddress = new javax.swing.JTextField();
		jLabel20 = new javax.swing.JLabel();
		txtCountry = new javax.swing.JTextField();
		jLabel21 = new javax.swing.JLabel();
		txtAddress2 = new javax.swing.JTextField();
		jLabel22 = new javax.swing.JLabel();
		txtPostal = new javax.swing.JTextField();
		jLabel23 = new javax.swing.JLabel();
		txtCity = new javax.swing.JTextField();
		jLabel24 = new javax.swing.JLabel();
		txtRegion = new javax.swing.JTextField();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		m_jNotes = new javax.swing.JTextArea();

		jLabel2 = new javax.swing.JLabel();
		jLabel2.setVisible(false);
		txtCurdebt = new javax.swing.JTextField();
		txtCurdebt.setVisible(false);
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setVisible(false);
		txtCurdate = new javax.swing.JTextField();
		txtCurdate.setVisible(false);

		jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
		txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N

		txtCurdebt.setEditable(false);
		txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N

		setLayout(new GridBagLayout());

		// ---------------------------------------------------------------

		txtCurdate.setEditable(false);
		txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);

		jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
		GridBagConstraints gbc_lbl_1 = new GridBagConstraints();
		gbc_lbl_1.anchor = GridBagConstraints.WEST;
		gbc_lbl_1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl_1.gridx = 0;
		gbc_lbl_1.gridy = 0;
		add(jLabel7, gbc_lbl_1);

		GridBagConstraints gbc_txt_1 = new GridBagConstraints();
		gbc_txt_1.gridwidth = 1;
		gbc_txt_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_1.insets = new Insets(5, 5, 0, 0);
		gbc_txt_1.weightx = 100.0;
		gbc_txt_1.gridx = 1;
		gbc_txt_1.gridy = 0;
		add(m_jTaxID, gbc_txt_1);

		JLabel space_1 = new JLabel("");
		GridBagConstraints gbc_space_1 = new GridBagConstraints();
		gbc_space_1.insets = new Insets(5, 5, 0, 0);
		gbc_space_1.weightx = 1.0;
		gbc_space_1.gridx = 2;
		gbc_space_1.gridy = 0;
		add(space_1, gbc_space_1);

		jLabel8.setText(AppLocal.getIntString("label.searchkey")); // NOI18N
		GridBagConstraints gbc_lbl_2 = new GridBagConstraints();
		gbc_lbl_2.anchor = GridBagConstraints.WEST;
		gbc_lbl_2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl_2.gridx = 0;
		gbc_lbl_2.gridy = 1;
		add(jLabel8, gbc_lbl_2);

		GridBagConstraints gbc_txt_2 = new GridBagConstraints();
		gbc_txt_2.gridwidth = 1;
		gbc_txt_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_2.insets = new Insets(5, 5, 0, 0);
		gbc_txt_2.weightx = 100.0;
		gbc_txt_2.gridx = 1;
		gbc_txt_2.gridy = 1;
		add(m_jSearchkey, gbc_txt_2);

		JLabel space_2 = new JLabel("");
		GridBagConstraints gbc_space_2 = new GridBagConstraints();
		gbc_space_2.insets = new Insets(5, 5, 0, 0);
		gbc_space_2.weightx = 1.0;
		gbc_space_2.gridx = 2;
		gbc_space_2.gridy = 1;
		add(space_2, gbc_space_2);

		jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
		GridBagConstraints gbc_lbl_3 = new GridBagConstraints();
		gbc_lbl_3.anchor = GridBagConstraints.WEST;
		gbc_lbl_3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl_3.gridx = 0;
		gbc_lbl_3.gridy = 2;
		add(jLabel3, gbc_lbl_3);

		GridBagConstraints gbc_txt_3 = new GridBagConstraints();
		gbc_txt_3.gridwidth = 1;
		gbc_txt_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_3.insets = new Insets(5, 5, 0, 0);
		gbc_txt_3.weightx = 100.0;
		gbc_txt_3.gridx = 1;
		gbc_txt_3.gridy = 2;
		add(m_jName, gbc_txt_3);

		JLabel space_3 = new JLabel("");
		GridBagConstraints gbc_space_3 = new GridBagConstraints();
		gbc_space_3.insets = new Insets(5, 5, 0, 0);
		gbc_space_3.weightx = 1.0;
		gbc_space_3.gridx = 2;
		gbc_space_3.gridy = 2;
		add(space_3, gbc_space_3);

		jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
		GridBagConstraints gbc_lbl_4 = new GridBagConstraints();
		gbc_lbl_4.anchor = GridBagConstraints.WEST;
		gbc_lbl_4.insets = new Insets(5, 5, 0, 0);
		gbc_lbl_4.gridx = 0;
		gbc_lbl_4.gridy = 3;
		add(jLabel4, gbc_lbl_4);

		GridBagConstraints gbc_txt_4 = new GridBagConstraints();
		gbc_txt_4.gridwidth = 1;
		gbc_txt_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_4.insets = new Insets(5, 5, 0, 0);
		gbc_txt_4.weightx = 1.0;
		gbc_txt_4.gridx = 1;
		gbc_txt_4.gridy = 3;
		add(m_jVisible, gbc_txt_4);

		JLabel space_4 = new JLabel("");
		GridBagConstraints gbc_space_4 = new GridBagConstraints();
		gbc_space_4.insets = new Insets(5, 5, 0, 0);
		gbc_space_4.weightx = 1.0;
		gbc_space_4.gridx = 2;
		gbc_space_4.gridy = 3;
		add(space_4, gbc_space_4);

		jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N

		jcard.setEditable(false);

		jLabel9.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N

		jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jPanel1.setLayout(new GridBagLayout());

		jLabel19.setText(AppLocal.getIntString("label.firstname")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		jPanel1.add(jLabel19, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5, 5, 0, 0);
		gbc_textPane.weightx = 100.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		jPanel1.add(txtFirstName, gbc_textPane);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5, 5, 0, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 2;
		gbc_space1.gridy = 0;
		jPanel1.add(space1, gbc_space1);

		jLabel15.setText(AppLocal.getIntString("label.lastname")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		jPanel1.add(jLabel15, gbc_lbl2);

		GridBagConstraints gbc_textPane1 = new GridBagConstraints();
		gbc_textPane1.gridwidth = 1;
		gbc_textPane1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane1.insets = new Insets(5, 5, 0, 0);
		gbc_textPane1.weightx = 100.0;
		gbc_textPane1.gridx = 1;
		gbc_textPane1.gridy = 1;
		jPanel1.add(txtLastName, gbc_textPane1);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5, 5, 0, 0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 2;
		gbc_space2.gridy = 1;
		jPanel1.add(space2, gbc_space2);

		jLabel16.setText(AppLocal.getIntString("label.email")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		jPanel1.add(jLabel16, gbc_lbl3);

		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 1;
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(5, 5, 0, 0);
		gbc_textPane2.weightx = 100.0;
		gbc_textPane2.gridx = 1;
		gbc_textPane2.gridy = 2;
		jPanel1.add(txtEmail, gbc_textPane2);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.insets = new Insets(5, 5, 0, 0);
		gbc_space3.weightx = 1.0;
		gbc_space3.gridx = 2;
		gbc_space3.gridy = 2;
		jPanel1.add(space3, gbc_space3);

		jLabel17.setText(AppLocal.getIntString("label.phone")); // NOI18N
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(5, 5, 0, 0);
		gbc_lbl4.gridx = 0;
		gbc_lbl4.gridy = 3;
		jPanel1.add(jLabel17, gbc_lbl4);

		GridBagConstraints gbc_textPane3 = new GridBagConstraints();
		gbc_textPane3.gridwidth = 1;
		gbc_textPane3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane3.insets = new Insets(5, 5, 0, 0);
		gbc_textPane3.weightx = 100.0;
		gbc_textPane3.gridx = 1;
		gbc_textPane3.gridy = 3;
		jPanel1.add(txtPhone, gbc_textPane3);

		JLabel space4 = new JLabel("");
		GridBagConstraints gbc_space4 = new GridBagConstraints();
		gbc_space4.insets = new Insets(5, 5, 0, 0);
		gbc_space4.weightx = 1.0;
		gbc_space4.gridx = 2;
		gbc_space4.gridy = 3;
		jPanel1.add(space4, gbc_space4);

		jLabel18.setText(AppLocal.getIntString("label.phone2")); // NOI18N
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.anchor = GridBagConstraints.WEST;
		gbc_lbl5.insets = new Insets(5, 5, 0, 0);
		gbc_lbl5.gridx = 0;
		gbc_lbl5.gridy = 4;
		jPanel1.add(jLabel18, gbc_lbl5);

		GridBagConstraints gbc_textPane4 = new GridBagConstraints();
		gbc_textPane4.gridwidth = 1;
		gbc_textPane4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane4.insets = new Insets(5, 5, 0, 0);
		gbc_textPane4.weightx = 100.0;
		gbc_textPane4.gridx = 1;
		gbc_textPane4.gridy = 4;
		jPanel1.add(txtPhone2, gbc_textPane4);

		JLabel space5 = new JLabel("");
		GridBagConstraints gbc_space5 = new GridBagConstraints();
		gbc_space5.insets = new Insets(5, 5, 0, 0);
		gbc_space5.weightx = 1.0;
		gbc_space5.gridx = 2;
		gbc_space5.gridy = 4;
		jPanel1.add(space5, gbc_space5);

		jLabel14.setText(AppLocal.getIntString("label.fax")); // NOI18N
		GridBagConstraints gbc_lbl6 = new GridBagConstraints();
		gbc_lbl6.anchor = GridBagConstraints.WEST;
		gbc_lbl6.insets = new Insets(5, 5, 0, 0);
		gbc_lbl6.gridx = 0;
		gbc_lbl6.gridy = 5;
		jPanel1.add(jLabel14, gbc_lbl6);

		GridBagConstraints gbc_textPane5 = new GridBagConstraints();
		gbc_textPane5.gridwidth = 1;
		gbc_textPane5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane5.insets = new Insets(5, 5, 0, 0);
		gbc_textPane5.weightx = 100.0;
		gbc_textPane5.gridx = 1;
		gbc_textPane5.gridy = 5;
		jPanel1.add(txtFax, gbc_textPane5);

		JLabel space6 = new JLabel("");
		GridBagConstraints gbc_space6 = new GridBagConstraints();
		gbc_space6.insets = new Insets(5, 5, 0, 0);
		gbc_space6.weightx = 1.0;
		gbc_space6.gridx = 2;
		gbc_space6.gridy = 5;
		jPanel1.add(space6, gbc_space6);

		JLabel space7 = new JLabel("");
		GridBagConstraints gbc_tab = new GridBagConstraints();
		gbc_tab.insets = new Insets(5, 5, 0, 0);
		gbc_tab.weighty = 1.0;
		gbc_tab.fill = GridBagConstraints.BOTH;
		gbc_tab.gridwidth = 3;
		gbc_tab.gridx = 0;
		gbc_tab.gridy = 6;
		jPanel1.add(space7, gbc_tab);

		jTabbedPane1.addTab(AppLocal.getIntString("label.contact"), jPanel1); // NOI18N

		jPanel2.setLayout(new GridBagLayout());

		jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
		GridBagConstraints gbc_lbl7 = new GridBagConstraints();
		gbc_lbl7.anchor = GridBagConstraints.WEST;
		gbc_lbl7.insets = new Insets(5, 5, 0, 0);
		gbc_lbl7.gridx = 0;
		gbc_lbl7.gridy = 0;
		jPanel2.add(jLabel13, gbc_lbl7);

		GridBagConstraints gbc_textPane6 = new GridBagConstraints();
		gbc_textPane6.gridwidth = 1;
		gbc_textPane6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane6.insets = new Insets(5, 5, 0, 0);
		gbc_textPane6.weightx = 100.0;
		gbc_textPane6.gridx = 1;
		gbc_textPane6.gridy = 0;
		jPanel2.add(txtAddress, gbc_textPane6);

		JLabel space8 = new JLabel("");
		GridBagConstraints gbc_space8 = new GridBagConstraints();
		gbc_space8.insets = new Insets(5, 5, 0, 0);
		gbc_space8.weightx = 1.0;
		gbc_space8.gridx = 2;
		gbc_space8.gridy = 0;
		jPanel2.add(space8, gbc_space8);

		jLabel21.setText(AppLocal.getIntString("label.address2")); // NOI18N
		GridBagConstraints gbc_lbl8 = new GridBagConstraints();
		gbc_lbl8.anchor = GridBagConstraints.WEST;
		gbc_lbl8.insets = new Insets(5, 5, 0, 0);
		gbc_lbl8.gridx = 0;
		gbc_lbl8.gridy = 1;
		jPanel2.add(jLabel21, gbc_lbl8);

		GridBagConstraints gbc_textPane7 = new GridBagConstraints();
		gbc_textPane7.gridwidth = 1;
		gbc_textPane7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane7.insets = new Insets(5, 5, 0, 0);
		gbc_textPane7.weightx = 100.0;
		gbc_textPane7.gridx = 1;
		gbc_textPane7.gridy = 1;
		jPanel2.add(txtAddress2, gbc_textPane7);

		JLabel space9 = new JLabel("");
		GridBagConstraints gbc_space9 = new GridBagConstraints();
		gbc_space9.insets = new Insets(5, 5, 0, 0);
		gbc_space9.weightx = 1.0;
		gbc_space9.gridx = 2;
		gbc_space9.gridy = 1;
		jPanel2.add(space9, gbc_space9);

		jLabel22.setText(AppLocal.getIntString("label.postal")); // NOI18N
		GridBagConstraints gbc_lbl9 = new GridBagConstraints();
		gbc_lbl9.anchor = GridBagConstraints.WEST;
		gbc_lbl9.insets = new Insets(5, 5, 0, 0);
		gbc_lbl9.gridx = 0;
		gbc_lbl9.gridy = 2;
		jPanel2.add(jLabel22, gbc_lbl9);

		GridBagConstraints gbc_textPane8 = new GridBagConstraints();
		gbc_textPane8.gridwidth = 1;
		gbc_textPane8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane8.insets = new Insets(5, 5, 0, 0);
		gbc_textPane8.weightx = 100.0;
		gbc_textPane8.gridx = 1;
		gbc_textPane8.gridy = 2;
		jPanel2.add(txtPostal, gbc_textPane8);

		JLabel space10 = new JLabel("");
		GridBagConstraints gbc_space10 = new GridBagConstraints();
		gbc_space10.insets = new Insets(5, 5, 0, 0);
		gbc_space10.weightx = 1.0;
		gbc_space10.gridx = 2;
		gbc_space10.gridy = 2;
		jPanel2.add(space10, gbc_space10);

		jLabel23.setText(AppLocal.getIntString("label.city")); // NOI18N
		GridBagConstraints gbc_lbl10 = new GridBagConstraints();
		gbc_lbl10.anchor = GridBagConstraints.WEST;
		gbc_lbl10.insets = new Insets(5, 5, 0, 0);
		gbc_lbl10.gridx = 0;
		gbc_lbl10.gridy = 3;
		jPanel2.add(jLabel23, gbc_lbl10);

		GridBagConstraints gbc_textPane9 = new GridBagConstraints();
		gbc_textPane9.gridwidth = 1;
		gbc_textPane9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane9.insets = new Insets(5, 5, 0, 0);
		gbc_textPane9.weightx = 100.0;
		gbc_textPane9.gridx = 1;
		gbc_textPane9.gridy = 3;
		jPanel2.add(txtCity, gbc_textPane9);

		JLabel space11 = new JLabel("");
		GridBagConstraints gbc_space11 = new GridBagConstraints();
		gbc_space11.insets = new Insets(5, 5, 0, 0);
		gbc_space11.weightx = 1.0;
		gbc_space11.gridx = 2;
		gbc_space11.gridy = 3;
		jPanel2.add(space11, gbc_space11);

		jLabel24.setText(AppLocal.getIntString("label.region")); // NOI18N
		GridBagConstraints gbc_lbl11 = new GridBagConstraints();
		gbc_lbl11.anchor = GridBagConstraints.WEST;
		gbc_lbl11.insets = new Insets(5, 5, 0, 0);
		gbc_lbl11.gridx = 0;
		gbc_lbl11.gridy = 4;
		jPanel2.add(jLabel24, gbc_lbl11);

		GridBagConstraints gbc_textPane10 = new GridBagConstraints();
		gbc_textPane10.gridwidth = 1;
		gbc_textPane10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane10.insets = new Insets(5, 5, 0, 0);
		gbc_textPane10.weightx = 100.0;
		gbc_textPane10.gridx = 1;
		gbc_textPane10.gridy = 4;
		jPanel2.add(txtRegion, gbc_textPane10);

		JLabel space12 = new JLabel("");
		GridBagConstraints gbc_space12 = new GridBagConstraints();
		gbc_space12.insets = new Insets(5, 5, 0, 0);
		gbc_space12.weightx = 1.0;
		gbc_space12.gridx = 2;
		gbc_space12.gridy = 4;
		jPanel2.add(space12, gbc_space12);

		jLabel20.setText(AppLocal.getIntString("label.country")); // NOI18N
		GridBagConstraints gbc_lbl12 = new GridBagConstraints();
		gbc_lbl12.anchor = GridBagConstraints.WEST;
		gbc_lbl12.insets = new Insets(5, 5, 0, 0);
		gbc_lbl12.gridx = 0;
		gbc_lbl12.gridy = 5;
		jPanel2.add(jLabel20, gbc_lbl12);

		GridBagConstraints gbc_textPane11 = new GridBagConstraints();
		gbc_textPane11.gridwidth = 1;
		gbc_textPane11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane11.insets = new Insets(5, 5, 0, 0);
		gbc_textPane11.weightx = 1.0;
		gbc_textPane11.gridx = 1;
		gbc_textPane11.gridy = 5;
		jPanel2.add(txtCountry, gbc_textPane11);

		JLabel space13 = new JLabel("");
		GridBagConstraints gbc_space13 = new GridBagConstraints();
		gbc_space13.insets = new Insets(5, 5, 0, 0);
		gbc_space13.weightx = 1.0;
		gbc_space13.gridx = 2;
		gbc_space13.gridy = 5;
		jPanel2.add(space13, gbc_space13);

		JLabel space14 = new JLabel("");
		GridBagConstraints gbc_tab2 = new GridBagConstraints();
		gbc_tab2.insets = new Insets(5, 5, 0, 0);
		gbc_tab2.weighty = 1.0;
		gbc_tab2.fill = GridBagConstraints.BOTH;
		gbc_tab2.gridwidth = 3;
		gbc_tab2.gridx = 0;
		gbc_tab2.gridy = 6;
		jPanel2.add(space14, gbc_tab2);

		jTabbedPane1.addTab(AppLocal.getIntString("label.location"), jPanel2); // NOI18N

		jScrollPane1.setViewportView(m_jNotes);
		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
						.addContainerGap()));

		jTabbedPane1.addTab(AppLocal.getIntString("label.notes"), jPanel3); // NOI18N

		GridBagConstraints gbc_tab_3 = new GridBagConstraints();
		gbc_tab_3.insets = new Insets(10, 5, 0, 0);
		gbc_tab_3.weighty = 1.0;
		gbc_tab_3.fill = GridBagConstraints.BOTH;
		gbc_tab_3.gridwidth = 3;
		gbc_tab_3.gridx = 0;
		gbc_tab_3.gridy = 6;
		add(jTabbedPane1, gbc_tab_3);

	}// </editor-fold>//GEN-END:initComponents

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"),
				AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			jcard.setText("c" + StringUtils.getCardNumber());
			m_Dirty.setDirty(true);
		}
	}// GEN-LAST:event_jButton2ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"),
				AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			jcard.setText(null);
			m_Dirty.setDirty(true);
		}
	}// GEN-LAST:event_jButton3ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextField jcard;
	private javax.swing.JComboBox m_jCategory;
	private javax.swing.JTextField m_jName;
	private javax.swing.JTextArea m_jNotes;
	private javax.swing.JTextField m_jSearchkey;
	private javax.swing.JTextField m_jTaxID;
	private javax.swing.JCheckBox m_jVisible;
	private javax.swing.JTextField txtAddress;
	private javax.swing.JTextField txtAddress2;
	private javax.swing.JTextField txtCity;
	private javax.swing.JTextField txtCountry;
	private javax.swing.JTextField txtCurdate;
	private javax.swing.JTextField txtCurdebt;
	private javax.swing.JTextField txtEmail;
	private javax.swing.JTextField txtFax;
	private javax.swing.JTextField txtFirstName;
	private javax.swing.JTextField txtLastName;
	private javax.swing.JTextField txtMaxdebt;
	private javax.swing.JTextField txtPhone;
	private javax.swing.JTextField txtPhone2;
	private javax.swing.JTextField txtPostal;
	private javax.swing.JTextField txtRegion;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel13, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel14, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel15, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel16, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel17, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel18, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel19, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel21, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel20, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel22, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel23, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel24, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel21, "common-small-fontsize", "32");

		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel8, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel9, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jSearchkey, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jTaxID, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, txtAddress, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtAddress2, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtCity, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtCountry, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtCurdate, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtCurdebt, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtEmail, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtFax, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtFirstName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtLastName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtPhone, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtPhone2, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtPostal, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, txtRegion, "common-small-fontsize", "32");

		PropertyUtil.ScaleCheckboxSize(m_App, m_jVisible, "common-small-fontsize", "32");
		PropertyUtil.ScaleCheckboxSize(m_App, m_jVisible, "common-small-fontsize", "32");

		PropertyUtil.ScaleTabbedPaneFontsize(m_App, jTabbedPane1, "common-small-fontsize", "32");

		int menuwidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "menubar-img-width", "16"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "menubar-img-height", "16"));

		// PropertyUtil.ScaleButtonIcon(m_jSearch, menuwidth, menuheight);
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}
}
