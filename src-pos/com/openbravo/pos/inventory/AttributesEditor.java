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
 * @author adrian
 */
public class AttributesEditor extends javax.swing.JPanel implements EditorRecord {

	private Object id;
	private AppView m_App;

	/** Creates new form AttributesEditor */
	public AttributesEditor(AppView app, DirtyManager dirty) {
		m_App = app;
		initComponents();

		m_jName.getDocument().addDocumentListener(dirty);

		writeValueEOF();

		ScaleButtons();
	}

	public void writeValueEOF() {
		id = null;
		m_jName.setText(null);
		m_jName.setEnabled(false);
	}

	public void writeValueInsert() {
		id = UUID.randomUUID().toString();
		m_jName.setText(null);
		m_jName.setEnabled(true);
	}

	public void writeValueDelete(Object value) {

		Object[] attr = (Object[]) value;
		id = attr[0];
		m_jName.setText(Formats.STRING.formatValue(attr[1]));
		m_jName.setEnabled(false);
	}

	public void writeValueEdit(Object value) {

		Object[] attr = (Object[]) value;
		id = attr[0];
		m_jName.setText(Formats.STRING.formatValue(attr[1]));
		m_jName.setEnabled(true);
	}

	public Object createValue() throws BasicException {

		Object[] attr = new Object[2];

		attr[0] = id;
		attr[1] = m_jName.getText();

		return attr;
	}

	public Component getComponent() {
		return this;
	}

	public void refresh() {
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel2 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JScrollPane scrollView = new JScrollPane();

		JPanel root = new JPanel();
		scrollView.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollView.setViewportView(root);
		add(scrollView);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		jLabel2.setText(AppLocal.getIntString("Label.AttributeName")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		root.add(jLabel2, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5, 5, 0, 0);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		root.add(m_jName, gbc_textPane);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5, 5, 0, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 2;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(5, 5, 0, 0);
		gbc_btnNewButton.weighty = 1.0;
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		root.add(space3, gbc_btnNewButton);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel2;
	private javax.swing.JTextField m_jName;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

}
