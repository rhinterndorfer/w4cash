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

package com.openbravo.pos.admin;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.Hashcypher;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.image.BufferedImage;
import java.util.UUID;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.util.StringUtils;

/**
 *
 * @author adrianromero
 */
public class PeopleView extends JPanel implements EditorRecord {

	private Object m_oId;
	private String m_sPassword;

	private DirtyManager m_Dirty;

	private SentenceList m_sentrole;
	private ComboBoxValModel m_RoleModel;
	private AppView m_App;

	/** Creates new form PeopleEditor */
	public PeopleView(AppView app, DataLogicAdmin dlAdmin, DirtyManager dirty) {
		this.m_App = app;
		initComponents();

		// El modelo de roles
		m_sentrole = dlAdmin.getRolesList();
		m_RoleModel = new ComboBoxValModel();

		m_Dirty = dirty;
		m_jName.getDocument().addDocumentListener(dirty);
		m_jRole.addActionListener(dirty);
		m_jVisible.addActionListener(dirty);
		m_jImage.addPropertyChangeListener("image", dirty);

		writeValueEOF();

		ScaleButtons();
	}

	public void writeValueEOF() {
		m_oId = null;
		m_jName.setText(null);
		m_sPassword = null;
		m_RoleModel.setSelectedKey(null);
		m_jVisible.setSelected(false);
		jcard.setText(null);
		m_jImage.setImage(null);
		m_jName.setEnabled(false);
		m_jRole.setEnabled(false);
		m_jVisible.setEnabled(false);
		jcard.setEnabled(false);
		m_jImage.setEnabled(false);
		jButton1.setEnabled(false);
		jButton2.setEnabled(false);
		jButton3.setEnabled(false);
	}

	public void writeValueInsert() {
		m_oId = null;
		m_jName.setText(null);
		m_sPassword = null;
		m_RoleModel.setSelectedKey(null);
		m_jVisible.setSelected(true);
		jcard.setText(null);
		m_jImage.setImage(null);
		m_jName.setEnabled(true);
		m_jRole.setEnabled(true);
		m_jVisible.setEnabled(true);
		jcard.setEnabled(true);
		m_jImage.setEnabled(true);
		jButton1.setEnabled(true);
		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
	}

	public void writeValueDelete(Object value) {
		Object[] people = (Object[]) value;
		m_oId = people[0];
		m_jName.setText(Formats.STRING.formatValue(people[1]));
		m_sPassword = Formats.STRING.formatValue(people[2]);
		m_RoleModel.setSelectedKey(people[3]);
		m_jVisible.setSelected(((Boolean) people[4]).booleanValue());
		jcard.setText(Formats.STRING.formatValue(people[5]));
		m_jImage.setImage((BufferedImage) people[6]);
		m_jName.setEnabled(false);
		m_jRole.setEnabled(false);
		m_jVisible.setEnabled(false);
		jcard.setEnabled(false);
		m_jImage.setEnabled(false);
		jButton1.setEnabled(false);
		jButton2.setEnabled(false);
		jButton3.setEnabled(false);
	}

	public void writeValueEdit(Object value) {
		Object[] people = (Object[]) value;
		m_oId = people[0];
		m_jName.setText(Formats.STRING.formatValue(people[1]));
		m_sPassword = Formats.STRING.formatValue(people[2]);
		m_RoleModel.setSelectedKey(people[3]);
		m_jVisible.setSelected(((Boolean) people[4]).booleanValue());
		jcard.setText(Formats.STRING.formatValue(people[5]));
		m_jImage.setImage((BufferedImage) people[6]);
		m_jName.setEnabled(true);
		m_jRole.setEnabled(true);
		m_jVisible.setEnabled(true);
		jcard.setEnabled(true);
		m_jImage.setEnabled(true);
		jButton1.setEnabled(true);
		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
	}

	public Object createValue() throws BasicException {
		Object[] people = new Object[7];
		people[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
		people[1] = Formats.STRING.parseValue(m_jName.getText());
		people[2] = Formats.STRING.parseValue(m_sPassword);
		people[3] = m_RoleModel.getSelectedKey();
		people[4] = Boolean.valueOf(m_jVisible.isSelected());
		people[5] = Formats.STRING.parseValue(jcard.getText());
		people[6] = m_jImage.getImage();
		return people;
	}

	public Component getComponent() {
		return this;
	}

	public void activate() throws BasicException {

		m_RoleModel = new ComboBoxValModel(m_sentrole.list());
		m_jRole.setModel(m_RoleModel);
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

		jButton3 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField();
		m_jVisible = new javax.swing.JCheckBox();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		m_jImage = new com.openbravo.data.gui.JImageEditor(m_App);
		jButton1 = new javax.swing.JButton();
		m_jRole = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		jcard = new javax.swing.JTextField();
		jButton2 = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JScrollPane scrollView = new JScrollPane();
		add(scrollView);
		JPanel root = new JPanel();
		scrollView.setViewportView(root);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		jLabel1.setText(AppLocal.getIntString("label.peoplename")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(0, 0, 5, 5);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		root.add(jLabel1, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		root.add(m_jName, gbc_textPane);

		jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/key.png"))); // NOI18N
		// jButton1.setText(AppLocal.getIntString("button.peoplepassword")); //
		// NOI18N
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_btnpassword = new GridBagConstraints();
		// gbc_btnpassword.weightx = 2.0;
		gbc_btnpassword.gridwidth = 1;
//		 gbc_btnpassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnpassword.insets = new Insets(0, 0, 0, 0);
		gbc_btnpassword.gridx = 2;
		gbc_btnpassword.gridy = 0;
		root.add(jButton1, gbc_btnpassword);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(0, 0, 5, 0);
		gbc_space1.weightx = 2.0;
		gbc_space1.gridx = 4;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(0, 0, 5, 5);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		root.add(jLabel5, gbc_lbl2);

		jcard.setEditable(false);
		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 1;
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(0, 0, 5, 5);
		gbc_textPane2.weightx = 1.0;
		gbc_textPane2.gridx = 1;
		gbc_textPane2.gridy = 1;
		root.add(jcard, gbc_textPane2);

		FlowLayout fl1 = new FlowLayout();
		fl1.setAlignment(FlowLayout.LEFT);
		JPanel pfl1 = new JPanel();
		pfl1.setLayout(fl1);

		jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		pfl1.add(jButton2);

		jButton3.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		pfl1.add(jButton3);

		GridBagConstraints gbc_btn3 = new GridBagConstraints();
		gbc_btn3.gridwidth = 1;
		// gbc_btn2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn3.insets = new Insets(0, 0, 5, 5);
		// gbc_btn3.weightx = 1;
		gbc_btn3.gridx = 2;
		gbc_btn3.gridy = 1;
		root.add(pfl1, gbc_btn3);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(0, 0, 5, 5);
		gbc_space2.weightx = 2.0;
		gbc_space2.gridx = 4;
		gbc_space2.gridy = 1;
		root.add(space2, gbc_space2);

		jLabel2.setText(AppLocal.getIntString("label.role")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(0, 0, 5, 5);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		root.add(jLabel2, gbc_lbl3);

		GridBagConstraints gbc_cmb1 = new GridBagConstraints();
		gbc_cmb1.gridwidth = 1;
		gbc_cmb1.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb1.insets = new Insets(0, 0, 5, 5);
		gbc_cmb1.weightx = 1.0;
		gbc_cmb1.gridx = 1;
		gbc_cmb1.gridy = 2;
		root.add(m_jRole, gbc_cmb1);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.gridwidth = 2;
		gbc_space3.insets = new Insets(0, 0, 5, 5);
		gbc_space3.weightx = 1.0;
		gbc_space3.gridx = 2;
		gbc_space3.gridy = 2;
		root.add(space3, gbc_space3);

		JLabel space4 = new JLabel("");
		GridBagConstraints gbc_space4 = new GridBagConstraints();
		gbc_space4.insets = new Insets(0, 0, 5, 5);
		gbc_space4.weightx = 2.0;
		gbc_space4.gridx = 4;
		gbc_space4.gridy = 2;
		root.add(space4, gbc_space4);

		jLabel3.setText(AppLocal.getIntString("label.peoplevisible")); // NOI18N
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(0, 0, 5, 5);
		gbc_lbl4.gridx = 0;
		gbc_lbl4.gridy = 3;
		root.add(jLabel3, gbc_lbl4);

		GridBagConstraints gbc_cb1 = new GridBagConstraints();
		gbc_cb1.gridwidth = 1;
		gbc_cb1.fill = GridBagConstraints.HORIZONTAL;
		gbc_cb1.insets = new Insets(0, 0, 5, 5);
		gbc_cb1.weightx = 1.0;
		gbc_cb1.gridx = 1;
		gbc_cb1.gridy = 3;
		root.add(m_jVisible, gbc_cb1);

		JLabel space5 = new JLabel("");
		GridBagConstraints gbc_space5 = new GridBagConstraints();
		gbc_space5.gridwidth = 2;
		gbc_space5.insets = new Insets(0, 0, 5, 5);
		gbc_space5.weightx = 1.0;
		gbc_space5.gridx = 2;
		gbc_space5.gridy = 3;
		root.add(space5, gbc_space5);

		JLabel space6 = new JLabel("");
		GridBagConstraints gbc_space6 = new GridBagConstraints();
		gbc_space6.insets = new Insets(0, 0, 5, 5);
		gbc_space6.weightx = 2.0;
		gbc_space6.gridx = 4;
		gbc_space6.gridy = 3;
		root.add(space6, gbc_space6);

		// jLabel4.setText(AppLocal.getIntString("label.peopleimage")); //
		// NOI18N
		// GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		// gbc_lbl5.anchor = GridBagConstraints.WEST;
		// gbc_lbl5.insets = new Insets(0, 0, 5, 5);
		// gbc_lbl5.gridx = 0;
		// gbc_lbl5.gridy = 4;
		// root.add(jLabel4, gbc_lbl5);

		GridBagConstraints gbc_img1 = new GridBagConstraints();
		gbc_img1.insets = new Insets(0, 0, 5, 5);
		gbc_img1.weighty = 1.0;
		gbc_img1.fill = GridBagConstraints.BOTH;
		gbc_img1.gridwidth = 4;
		gbc_img1.gridx = 0;
		gbc_img1.gridy = 4;
		root.add(m_jImage, gbc_img1);

		m_jImage.setMaxDimensions(new java.awt.Dimension(32, 32));

		// javax.swing.GroupLayout layout = new javax.swing.GroupLayout(root);
		// root.setLayout(layout);

		// layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addGroup(layout.createSequentialGroup().addContainerGap()
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addGroup(layout.createSequentialGroup()
		// .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jButton1))
		// .addGroup(layout.createSequentialGroup()
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addGroup(layout.createSequentialGroup()
		// .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jcard, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addGroup(layout.createSequentialGroup()
		// .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(m_jRole, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
		// javax.swing.GroupLayout.PREFERRED_SIZE)))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jButton2)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(jButton3))
		// .addGroup(layout.createSequentialGroup()
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(m_jImage, javax.swing.GroupLayout.PREFERRED_SIZE, 380,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(m_jVisible))))
		// .addContainerGap(129, Short.MAX_VALUE)));
		// layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
		// javax.swing.GroupLayout.Alignment.TRAILING,
		// layout.createSequentialGroup().addContainerGap()
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jLabel1)
		// .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jButton1))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jButton3)
		// .addGroup(layout.createSequentialGroup()
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jLabel5).addComponent(jcard,
		// javax.swing.GroupLayout.PREFERRED_SIZE,
		// javax.swing.GroupLayout.DEFAULT_SIZE,
		// javax.swing.GroupLayout.PREFERRED_SIZE))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		// .addComponent(jLabel2).addComponent(m_jRole,
		// javax.swing.GroupLayout.PREFERRED_SIZE, 20,
		// javax.swing.GroupLayout.PREFERRED_SIZE)))
		// .addComponent(jButton2))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(jLabel3).addComponent(m_jVisible))
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		// .addComponent(m_jImage, javax.swing.GroupLayout.PREFERRED_SIZE, 330,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addComponent(jLabel4))
		// .addGap(246, 246, 246)));
	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jcard, "common-small-fontsize", "32");

		PropertyUtil.ScaleComboFontsize(m_App, m_jRole, "common-small-fontsize", "32");

		PropertyUtil.ScaleCheckboxSize(m_App, m_jVisible, "common-small-fontsize", "32");

		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jButton1, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButton2, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(jButton3, menuwidth, menuheight, fontsize);
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		String sNewPassword = Hashcypher.changePassword(m_App, this);
		if (sNewPassword != null) {
			m_sPassword = sNewPassword;
			m_Dirty.setDirty(true);
		}

	}// GEN-LAST:event_jButton1ActionPerformed

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

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JTextField jcard;
	private com.openbravo.data.gui.JImageEditor m_jImage;
	private javax.swing.JTextField m_jName;
	private javax.swing.JComboBox m_jRole;
	private javax.swing.JCheckBox m_jVisible;
	// End of variables declaration//GEN-END:variables

}
