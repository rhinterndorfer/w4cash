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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import com.openbravo.pos.forms.AppLocal;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.basic.SignatureUnitException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.util.PropertyUtil;

import java.util.ArrayList;

/**
 *
 * @author adrianromero
 */
public class CategoriesEditor extends JPanel implements EditorRecord {

	private SentenceList m_sentcat;
	private ComboBoxValModel m_CategoryModel;

	private SentenceExec m_sentadd;
	private SentenceExec m_sentdel;

	private Object m_id;
	private Integer m_printerIndex;

	private AppView m_App;
	private Integer lastOrderIndex = 0;
	private JScrollPane scrollView;

	/** Creates new form JPanelCategories */
	public CategoriesEditor(AppView app, DirtyManager dirty) {
		this.m_App = app;

		DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

		initComponents();

		// El modelo de categorias
		m_sentcat = dlSales.getCategoriesList();
		m_CategoryModel = new ComboBoxValModel();

		m_sentadd = dlSales.getCatalogCategoryAdd();
		m_sentdel = dlSales.getCatalogCategoryDel();
		dlSales.getCatalogCategoryAdd();

		m_jName.getDocument().addDocumentListener(dirty);
		m_jCategory.addActionListener(dirty);
		m_jPrinter.addActionListener(dirty);
		// ((JTextField)
		// m_jPrinter.getEditor().getEditorComponent()).getDocument().addDocumentListener(dirty);

		m_jImage.addPropertyChangeListener("image", dirty);
		m_jImage.addPropertyChangeListener("background", dirty);

		writeValueEOF();

		ScaleButtons();
	}

	public void refresh() {
		List a;

		try {
			a = m_sentcat.list();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotloadlists"),
					eD);
			msg.show(m_App, this);
			a = new ArrayList();
		}
		if (!a.isEmpty()) {
			this.lastOrderIndex = ((CategoryInfo) a.get(a.size() - 1)).getSortOrder();
		}
		a.add(0, null); // The null item
		m_CategoryModel = new ComboBoxValModel(a);
		m_jCategory.setModel(m_CategoryModel);
		

	}

	private Integer getNextOrderNumber() {
		return lastOrderIndex + 1;
	}

	private void incOrderNumber() {
		this.lastOrderIndex += 1;
	}

	public void writeValueEOF() {
		m_id = null;
		m_sortOrder = null;
		m_jName.setText(null);
		m_CategoryModel.setSelectedKey(null);
		m_jPrinter.setSelectedIndex(-1);
		m_jImage.setImage(null);
		m_jImage.setSelecteBGColor(null);
		m_jName.setEnabled(false);
		m_jCategory.setEnabled(false);
		m_jPrinter.setEnabled(false);
		m_jImage.setEnabled(false);
		//m_jCatalogDelete.setEnabled(false);
		//m_jCatalogAdd.setEnabled(false);
	}

	public void writeValueInsert() {
		m_id = UUID.randomUUID().toString();
		m_jName.setText(null);
		m_CategoryModel.setSelectedKey(null);
		m_jPrinter.setSelectedIndex(-1);
		m_jImage.setImage(null);
		m_jImage.setSelecteBGColor(null);
		m_jName.setEnabled(true);
		m_jCategory.setEnabled(true);
		m_jPrinter.setEnabled(true);
		m_jImage.setEnabled(true);
		//m_jCatalogDelete.setEnabled(false);
		//m_jCatalogAdd.setEnabled(false);

		m_sortOrder = getNextOrderNumber();
		incOrderNumber();
	}

	public void writeValueDelete(Object value) {
		Object[] cat = (Object[]) value;
		m_id = cat[0];
		m_jName.setText(Formats.STRING.formatValue(cat[1]));
		m_CategoryModel.setSelectedKey(cat[2]);
		m_jImage.setImage((BufferedImage) cat[3]);
		m_jImage.setSelecteBGColor(Formats.STRING.formatValue(cat[4]));
		m_sortOrder = cat[5];
		m_printerIndex = (Integer) cat[6];

		m_jName.setEnabled(false);
		m_jCategory.setEnabled(false);
		m_jPrinter.setEnabled(false);
		m_jImage.setEnabled(false);
		//m_jCatalogDelete.setEnabled(false);
		//m_jCatalogAdd.setEnabled(false);
	}

	public void writeValueEdit(Object value) {
		Object[] cat = (Object[]) value;
		m_id = cat[0];
		m_sortOrder = cat[5];
		m_printerIndex = (Integer) cat[6];

		m_jName.setText(Formats.STRING.formatValue(cat[1]));
		m_CategoryModel.setSelectedKey(cat[2]);
		m_jImage.setImage((BufferedImage) cat[3]);
		m_jImage.setSelecteBGColor(Formats.STRING.formatValue(cat[4]));
		m_jPrinter.setSelectedIndex(m_printerIndex);

		m_jName.setEnabled(true);
		m_jCategory.setEnabled(true);
		m_jPrinter.setEnabled(true);
		m_jImage.setEnabled(true);
		//m_jCatalogDelete.setEnabled(true);
		//m_jCatalogAdd.setEnabled(true);
	}

	public Object createValue() throws BasicException {

		Object[] cat = new Object[7];
		cat[0] = m_id;
		cat[1] = m_jName.getText();
		cat[2] = m_CategoryModel.getSelectedKey();
		cat[3] = m_jImage.getImage();
		cat[4] = m_jImage.getSelecteBGColor();
		cat[5] = m_sortOrder;
		cat[6] = m_jPrinter.getSelectedIndex();

		// if (m_jPrinter.getSelectedIndex() >= 0) {
		//
		// } else {
		// cat[5] = -1;
		// }

		return cat;
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

		jLabel2 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		m_jImage = new com.openbravo.data.gui.JImageEditor(m_App);
		m_jImage.setMaxDimensions(new java.awt.Dimension(96, 96));
		
		//m_jCatalogAdd = new javax.swing.JButton();
		//m_jCatalogDelete = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();
		m_jCategory = new javax.swing.JComboBox();
		jLabel7 = new javax.swing.JLabel();
		m_jPrinter = new javax.swing.JComboBox();
		m_jPrinter.addItem("");
		m_jPrinter.addItem("Drucker 1");
		m_jPrinter.addItem("Drucker 2");
		m_jPrinter.addItem("Drucker 3");
		m_jPrinter.addItem("Drucker 4");
		m_jPrinter.addItem("Drucker 5");
		m_jPrinter.addItem("Drucker 6");
		m_jPrinter.addItem("Drucker 7");
		m_jPrinter.addItem("Drucker 8");
		m_jPrinter.addItem("Drucker 9");

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.scrollView = new JScrollPane();
		add(scrollView);
		JPanel root = new JPanel();
		scrollView.setViewportView(root);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N
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
		gbc_space1.gridx = 3;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		root.add(jLabel5, gbc_lbl2);

		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 1;
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(5, 5, 0, 0);
		gbc_textPane2.weightx = 1.0;
		gbc_textPane2.gridx = 1;
		gbc_textPane2.gridy = 1;
		root.add(m_jCategory, gbc_textPane2);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5, 5, 0, 0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 2;
		gbc_space2.gridy = 1;
		root.add(space2, gbc_space2);

		jLabel7.setText(AppLocal.getIntString("Menu.Printer")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		root.add(jLabel7, gbc_lbl3);

		GridBagConstraints gbc_textPane3 = new GridBagConstraints();
		gbc_textPane3.gridwidth = 1;
		gbc_textPane3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane3.insets = new Insets(5, 5, 0, 0);
		gbc_textPane3.weightx = 1.0;
		gbc_textPane3.gridx = 1;
		gbc_textPane3.gridy = 2;
		root.add(m_jPrinter, gbc_textPane3);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.insets = new Insets(5, 5, 0, 0);
		gbc_space3.weightx = 1.0;
		gbc_space3.gridx = 2;
		gbc_space3.gridy = 2;
		root.add(space3, gbc_space3);

		FlowLayout fl1 = new FlowLayout();
		fl1.setAlignment(FlowLayout.LEFT);
		JPanel pfl1 = new JPanel();
		pfl1.setLayout(fl1);

		//m_jCatalogAdd.setText(AppLocal.getIntString("button.catalogadd")); // NOI18N
		//pfl1.add(m_jCatalogAdd);

		//m_jCatalogDelete.setText(AppLocal.getIntString("button.catalogdel")); // NOI18N
		//pfl1.add(m_jCatalogDelete);

		GridBagConstraints gbc_pane = new GridBagConstraints();
		gbc_pane.insets = new Insets(5, 5, 0, 0);
		gbc_pane.gridwidth = 3;
		gbc_pane.weightx = 1.0;
		gbc_pane.gridx = 0;
		gbc_pane.gridy = 3;
		root.add(pfl1, gbc_pane);

		GridBagConstraints gbc_img1 = new GridBagConstraints();
		gbc_img1.insets = new Insets(5, 5, 0, 0);
		gbc_img1.weighty = 1.0;
		gbc_img1.fill = GridBagConstraints.BOTH;
		gbc_img1.gridwidth = 3;
		gbc_img1.gridx = 0;
		gbc_img1.gridy = 4;
		root.add(m_jImage, gbc_img1);

		
		/*
		m_jCatalogAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jCatalogAddActionPerformed(evt);
			}
		});

		m_jCatalogDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jCatalogDeleteActionPerformed(evt);
			}
		});
		*/

	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");

		PropertyUtil.ScaleComboFontsize(m_App, m_jCategory, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jPrinter, "common-small-fontsize", "32");

		PropertyUtil.ScaleScrollbar(m_App, this.scrollView);

		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		//PropertyUtil.ScaleButtonIcon(m_jCatalogAdd, menuwidth, menuheight, fontsize);
		//PropertyUtil.ScaleButtonIcon(m_jCatalogDelete, menuwidth, menuheight, fontsize);
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {
		PropertyUtil.fillSortOrder(bd, 5);
	}

	private void m_jCatalogDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCatalogDeleteActionPerformed

		try {
			m_sentdel.exec(m_id);
		} catch (BasicException e) {
			JMessageDialog.showMessage(m_App, this,
					new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e));
		}

	}// GEN-LAST:event_m_jCatalogDeleteActionPerformed

	private void m_jCatalogAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jCatalogAddActionPerformed

		try {
			Object param = m_id;
			m_sentdel.exec(param); // primero borramos
			m_sentadd.exec(param); // y luego insertamos lo que queda
		} catch (BasicException e) {
			JMessageDialog.showMessage(m_App, this,
					new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e));
		}

	}// GEN-LAST:event_m_jCatalogAddActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JTextField m_jName;
	private javax.swing.JComboBox<String> m_jPrinter;
	//private javax.swing.JButton m_jCatalogAdd;
	//private javax.swing.JButton m_jCatalogDelete;
	private javax.swing.JComboBox m_jCategory;
	private Object m_sortOrder;
	private com.openbravo.data.gui.JImageEditor m_jImage;

	// End of variables declaration//GEN-END:variables

}
