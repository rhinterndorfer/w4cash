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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author adrianromero
 */
public class AttributeUseEditor extends javax.swing.JPanel implements EditorRecord {

	private SentenceList attributesent;
	private ComboBoxValModel attributemodel;

	private Object id;
	private Object attuseid;

	private Object insertid;
	private AppView m_App;

	/** Creates new form AttributeSetEditor */
	public AttributeUseEditor(AppView app, DirtyManager dirty) {
		this.m_App = app;
		attributesent = new StaticSentence(app.getSession(), "SELECT ID, NAME FROM ATTRIBUTE ORDER BY NAME", null,
				new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new AttributeInfo(dr.getString(1), dr.getString(2));
					}
				});
		attributemodel = new ComboBoxValModel();

		initComponents();

		jLineno.getDocument().addDocumentListener(dirty);
		jAttribute.addActionListener(dirty);

		ScaleButtons();
	}

	public void setInsertId(String insertid) {

		this.insertid = insertid;
	}

	public void activate() throws BasicException {

		attributemodel = new ComboBoxValModel(attributesent.list());
		jAttribute.setModel(attributemodel);
	}

	public void refresh() {
	}

	public void writeValueEOF() {

		id = null;
		attuseid = null;
		attributemodel.setSelectedKey(null);
		jLineno.setText(null);

		jAttribute.setEnabled(false);
		jLineno.setEnabled(false);
	}

	public void writeValueInsert() {

		id = UUID.randomUUID().toString();
		attuseid = insertid;
		attributemodel.setSelectedKey(null);
		jLineno.setText(null);

		jAttribute.setEnabled(true);
		jLineno.setEnabled(true);
	}

	public void writeValueEdit(Object value) {

		Object[] obj = (Object[]) value;

		id = obj[0];
		attuseid = obj[1];
		attributemodel.setSelectedKey(obj[2]);
		jLineno.setText(Formats.INT.formatValue(obj[3]));

		jAttribute.setEnabled(true);
		jLineno.setEnabled(true);
	}

	public void writeValueDelete(Object value) {

		Object[] obj = (Object[]) value;

		id = obj[0];
		attuseid = obj[1];
		attributemodel.setSelectedKey(obj[2]);
		jLineno.setText(Formats.INT.formatValue(obj[3]));

		jAttribute.setEnabled(false);
		jLineno.setEnabled(false);
	}

	public Component getComponent() {
		return this;
	}

	public Object createValue() throws BasicException {
		Object[] value = new Object[5];

		value[0] = id;
		value[1] = attuseid;
		value[2] = attributemodel.getSelectedKey();
		value[3] = Formats.INT.parseValue(jLineno.getText());
		value[4] = attributemodel.getSelectedText();

		return value;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel3 = new javax.swing.JLabel();
		jLineno = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jAttribute = new javax.swing.JComboBox();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JScrollPane scrollView = new JScrollPane();

		JPanel root = new JPanel();
		scrollView.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollView.setViewportView(root);
		add(scrollView);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		jLabel3.setText(AppLocal.getIntString("label.order")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		root.add(jLabel3, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5, 5, 0, 0);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		root.add(jLineno, gbc_textPane);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5, 5, 0, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 2;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		jLabel4.setText(AppLocal.getIntString("label.attribute")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		root.add(jLabel4, gbc_lbl2);

		GridBagConstraints gbc_attributeid = new GridBagConstraints();
		gbc_attributeid.gridwidth = 1;
		gbc_attributeid.fill = GridBagConstraints.HORIZONTAL;
		gbc_attributeid.insets = new Insets(5, 5, 0, 0);
		gbc_attributeid.weightx = 1.0;
		gbc_attributeid.gridx = 1;
		gbc_attributeid.gridy = 1;
		root.add(jAttribute, gbc_attributeid);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5, 5, 0, 0);
		// gbc_space2.fill = GridBagConstraints.BOTH;
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 2;
		gbc_space2.gridy = 1;
		root.add(space2, gbc_space2);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(5, 5, 0, 0);
		gbc_btnNewButton.weighty = 1.0;
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		root.add(space3, gbc_btnNewButton);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox jAttribute;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JTextField jLineno;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jLineno, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, jAttribute, "common-small-fontsize", "32");
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

}
