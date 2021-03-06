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

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.pos.forms.AppLocal;

import com.openbravo.pos.forms.AppView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.reports.ReportEditorCreator;
import com.openbravo.pos.util.PropertyUtil;

public class ProductFilter extends javax.swing.JPanel implements ReportEditorCreator {

	private SentenceList m_sentcat;
	private ComboBoxValModel m_CategoryModel;
	private AppView m_App;
	private BrowsableEditableData m_bd;
	private Boolean m_hideToggle=false;

	public ProductFilter() {
		initComponents();
	}
	
	public ProductFilter(Boolean hideToggle) {
		m_hideToggle = hideToggle;
		initComponents();
	}
	
	/** Creates new form JQBFProduct */
	public ProductFilter(AppView app) {
		m_App = app;
		initComponents();

		ScaleButtons();
	}

	public void setBrowsableData(BrowsableEditableData bd)
	{
		m_bd = bd;
	}
	
	public void init(AppView app) {

		m_App = app;
		
		ScaleButtons();
	}

	public void activate() throws BasicException {
		DataLogicSales dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

		// El modelo de categorias
		m_sentcat = dlSales.getCategoriesListSortedByName();
		m_CategoryModel = new ComboBoxValModel();

		m_jCboName.setModel(ListQBFModelNumber.getMandatoryString());
		m_jCboPriceBuy.setModel(ListQBFModelNumber.getMandatoryNumber());
		m_jCboPriceSell.setModel(ListQBFModelNumber.getMandatoryNumber());

		
		
		List catlist = m_sentcat.list();
		catlist.add(0, null);
		
		m_CategoryModel = new ComboBoxValModel(catlist);
		m_jCategory.setModel(m_CategoryModel);
	}

	public SerializerWrite getSerializerWrite() {
		return new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
				Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING });
	}

	public Component getComponent() {
		return this;
	}

	public String GetSelectedCategoryKey() {
		if(m_CategoryModel != null && m_CategoryModel.getSelectedKey() != null)
			return m_CategoryModel.getSelectedKey().toString();
		else
			return null;
	}
	
	public Object createValue() throws BasicException {

		if (m_jBarcode.getText() == null || m_jBarcode.getText().equals("")) {
			// Filtro por formulario
			return new Object[] { m_jCboName.getSelectedItem(), m_jName.getText(), 
					m_jCboPriceBuy.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceBuy.getText()), 
					m_jCboPriceSell.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceSell.getText()),
					m_CategoryModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_CategoryModel.getSelectedKey(), 
					QBFCompareEnum.COMP_NONE, null };
		} else {
			// Filtro por codigo de barras.
			String barcode =  m_jBarcode.getText().trim();
			if(barcode.startsWith("*"))
			{
				barcode = barcode.substring(1);
			}
			
			return new Object[] { QBFCompareEnum.COMP_NONE, null, 
					QBFCompareEnum.COMP_NONE, null,
					QBFCompareEnum.COMP_NONE, null, 
					QBFCompareEnum.COMP_NONE, null, 
					QBFCompareEnum.COMP_RE, "%," + barcode + ",%" };
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

		jPanel2 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		m_jBarcode = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel();
		jPanelFilter = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		m_jCboName = new javax.swing.JComboBox();
		m_jName = new javax.swing.JTextField();
		m_jPriceBuy = new javax.swing.JTextField();
		m_jCboPriceBuy = new javax.swing.JComboBox();
		m_jCboPriceSell = new javax.swing.JComboBox();
		m_jPriceSell = new javax.swing.JTextField();
		m_jCategory = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		jToggleFilter = new javax.swing.JToggleButton();

		jPanelFilter.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		
		
		jToggleFilter.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (jToggleFilter.isSelected()) {
					jToggleFilter.setText(AppLocal.getIntString("label.collapse"));
				} else {
					jToggleFilter.setText(AppLocal.getIntString("label.expand"));
				}
			}
		});

		jToggleFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png"))); // NOI18N
		jToggleFilter.setSelected(true);
		jToggleFilter.setSelectedIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png"))); // NOI18N
		jToggleFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jToggleFilterActionPerformed(evt);
			}
		});
		if(!m_hideToggle) {
			jPanelFilter.add(jToggleFilter, BorderLayout.CENTER);
		}
		

		jPanel2.setLayout(new GridBagLayout());
		//jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				// javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bybarcode"))); // NOI18N

		
		jLabel5.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(10, 5, 10, 5);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		jPanel2.add(jLabel5, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets =new Insets(10, 5, 10, 5);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		jPanel2.add(m_jBarcode, gbc_textPane);
		m_jBarcode.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					firePropertyChange("barcode", null, m_jBarcode.getText());
					m_jBarcode.selectAll();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});;

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(10, 5, 10, 5);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 2;
		gbc_space1.gridy = 0;
		jPanel2.add(space1, gbc_space1);

		jPanel1.setLayout(new GridBagLayout());
		//jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		//jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.byform"))); // NOI18N

		jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.anchor = GridBagConstraints.WEST;
		gbc_lbl5.insets = new Insets(10, 5, 0, 0);
		gbc_lbl5.gridx = 0;
		gbc_lbl5.gridy = 0;
		jPanel1.add(jLabel2, gbc_lbl5);

		GridBagConstraints gbc_cmb1 = new GridBagConstraints();
		gbc_cmb1.gridwidth = 1;
		gbc_cmb1.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb1.insets = new Insets(10, 5, 0, 0);
		gbc_cmb1.weightx = 1.0;
		gbc_cmb1.gridx = 1;
		gbc_cmb1.gridy = 0;
		jPanel1.add(m_jCboName, gbc_cmb1);

		GridBagConstraints gbc_txt1 = new GridBagConstraints();
		gbc_txt1.gridwidth = 1;
		gbc_txt1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt1.insets = new Insets(10, 5, 0, 0);
		gbc_txt1.weightx = 1.0;
		gbc_txt1.gridx = 2;
		gbc_txt1.gridy = 0;
		jPanel1.add(m_jName, gbc_txt1);
		m_jName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					firePropertyChange("name", null, m_jName.getText());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(10, 5, 0, 0);
		gbc_space2.weightx = 2.0;
		gbc_space2.gridx = 3;
		gbc_space2.gridy = 0;
		jPanel1.add(space2, gbc_space2);

		jLabel4.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(5, 5, 0, 0);
		gbc_lbl4.gridx = 0;
		gbc_lbl4.gridy = 1;
		jPanel1.add(jLabel4, gbc_lbl4);

		GridBagConstraints gbc_cmb2 = new GridBagConstraints();
		gbc_cmb2.gridwidth = 1;
		gbc_cmb2.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb2.insets = new Insets(5, 5, 0, 0);
		gbc_cmb2.weightx = 1.0;
		gbc_cmb2.gridx = 1;
		gbc_cmb2.gridy = 1;
		jPanel1.add(m_jCboPriceBuy, gbc_cmb2);

		GridBagConstraints gbc_txt2 = new GridBagConstraints();
		gbc_txt2.gridwidth = 1;
		gbc_txt2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt2.insets = new Insets(5, 5, 0, 0);
		gbc_txt2.weightx = 1.0;
		gbc_txt2.gridx = 2;
		gbc_txt2.gridy = 1;
		jPanel1.add(m_jPriceBuy, gbc_txt2);
		m_jPriceBuy.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					firePropertyChange("pricebuy", null, m_jPriceBuy.getText());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		
		

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.insets = new Insets(5, 5, 0, 0);
		gbc_space3.weightx = 2.0;
		gbc_space3.gridx = 3;
		gbc_space3.gridy = 1;
		jPanel1.add(space3, gbc_space3);

		jLabel3.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 2;
		jPanel1.add(jLabel3, gbc_lbl3);

		GridBagConstraints gbc_cmb3 = new GridBagConstraints();
		gbc_cmb3.gridwidth = 1;
		gbc_cmb3.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb3.insets = new Insets(5, 5, 0, 0);
		gbc_cmb3.weightx = 1.0;
		gbc_cmb3.gridx = 1;
		gbc_cmb3.gridy = 2;
		jPanel1.add(m_jCboPriceSell, gbc_cmb3);
		
		
		
		GridBagConstraints gbc_txt3 = new GridBagConstraints();
		gbc_txt3.gridwidth = 1;
		gbc_txt3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt3.insets = new Insets(5, 5, 0, 0);
		gbc_txt3.weightx = 1.0;
		gbc_txt3.gridx = 2;
		gbc_txt3.gridy = 2;
		jPanel1.add(m_jPriceSell, gbc_txt3);
		m_jPriceSell.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					firePropertyChange("pricesell", null, m_jPriceSell.getText());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		
		JLabel space4 = new JLabel("");
		GridBagConstraints gbc_space4 = new GridBagConstraints();
		gbc_space4.insets = new Insets(5, 5, 0, 0);
		gbc_space4.weightx = 2.0;
		gbc_space4.gridx = 3;
		gbc_space4.gridy = 2;
		jPanel1.add(space4, gbc_space4);

		jLabel1.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 10, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 3;
		jPanel1.add(jLabel1, gbc_lbl2);

		GridBagConstraints gbc_txt5 = new GridBagConstraints();
		gbc_txt5.gridwidth = 1;
		gbc_txt5.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt5.insets = new Insets(5, 5, 10, 0);
		gbc_txt5.weightx = 1.0;
		gbc_txt5.gridx = 1;
		gbc_txt5.gridy = 3;
		jPanel1.add(m_jCategory, gbc_txt5);
		m_jCategory.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				firePropertyChange("category", null, m_jCategory.getSelectedItem());
			}
		});
		
		
		JLabel space6 = new JLabel("");
		GridBagConstraints gbc_space6 = new GridBagConstraints();
		gbc_space6.insets = new Insets(5, 5, 10, 0);
		gbc_space6.weightx = 1.0;
		gbc_space6.gridx = 3;
		gbc_space6.gridy = 3;
		jPanel1.add(space6, gbc_space6);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 
								512, Short.MAX_VALUE)
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				)
				.addComponent(jPanelFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				);
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel1,javax.swing.GroupLayout.PREFERRED_SIZE, 
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						)
				.addComponent(jPanelFilter,javax.swing.GroupLayout.PREFERRED_SIZE, 
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				);
	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void ScaleButtons() {
		//PropertyUtil.ScaleBorderFontsize(m_App, (TitledBorder) jPanel2.getBorder(), "common-filter-fontsize", "24");
		//PropertyUtil.ScaleBorderFontsize(m_App, (TitledBorder) jPanel1.getBorder(), "common-filter-fontsize", "24");

		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-filter-fontsize", "24");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jBarcode, "common-filter-fontsize", "24");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-filter-fontsize", "24");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jPriceBuy, "common-filter-fontsize", "24");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jPriceSell, "common-filter-fontsize", "24");

		PropertyUtil.ScaleComboFontsize(m_App, m_jCategory, "common-filter-fontsize", "24");
		PropertyUtil.ScaleComboFontsize(m_App, m_jCboName, "common-filter-fontsize", "24");
		PropertyUtil.ScaleComboFontsize(m_App, m_jCboPriceBuy, "common-filter-fontsize", "24");
		PropertyUtil.ScaleComboFontsize(m_App, m_jCboPriceSell, "common-filter-fontsize", "24");
		
		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));
		
		
		PropertyUtil.ScaleButtonIcon(jToggleFilter, menuwidth, menuheight, fontsize);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanelFilter;
	private javax.swing.JTextField m_jBarcode;
	private javax.swing.JComboBox m_jCategory;
	private javax.swing.JComboBox m_jCboName;
	private javax.swing.JComboBox m_jCboPriceBuy;
	private javax.swing.JComboBox m_jCboPriceSell;
	private javax.swing.JTextField m_jName;
	private javax.swing.JTextField m_jPriceBuy;
	private javax.swing.JTextField m_jPriceSell;
	private javax.swing.JToggleButton jToggleFilter;
	// End of variables declaration//GEN-END:variables

	private void jToggleFilterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleFilterActionPerformed

		jPanel1.setVisible(jToggleFilter.isSelected());
		jPanel2.setVisible(jToggleFilter.isSelected());
		
	}// GEN-LAST:event_jToggleFilterActionPerformed
	
}
