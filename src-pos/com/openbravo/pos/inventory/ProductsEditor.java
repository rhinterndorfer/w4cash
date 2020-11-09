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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.awt.image.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.ProductFilter;
import com.openbravo.pos.util.PropertyUtil;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author adrianromero
 */
public class ProductsEditor extends JPanel implements EditorRecord {

	private static final long serialVersionUID = 1L;
	private SentenceList m_sentcat;
	private ComboBoxValModel m_CategoryModel;

	private SentenceList taxcatsent;
	private ComboBoxValModel taxcatmodel;

	private SentenceList attsent;
	private ComboBoxValModel attmodel;

	private SentenceList taxsent;
	private TaxesLogic taxeslogic;

	private ComboBoxValModel m_CodetypeModel;

	private Object m_id;
	private Object pricesell;
	private boolean priceselllock = false;

	private boolean reportlock = false;
	private AppView m_App;
	private JScrollPane scrollView;
	private JLabel jLabel20;
	private JLabel jLabel21;
	private JLabel jLabel22;
	private JLabel jLabel23;
	private JTextField m_jstockheight;
	private JTextField m_jstockwidth;
	private JTextField m_jstockLength;
	// private JTextField m_jstockCount;
	private boolean issaege;
	private ProductFilter m_productfilter;
	private PreparedSentence refsent;

	/** Creates new form JEditProduct */
	public ProductsEditor(AppView app, DataLogicSales dlSales, DirtyManager dirty, ProductFilter productfilter) {
		m_App = app;
		
		m_productfilter = productfilter;

		initComponents();

		// The taxes sentence
		taxsent = dlSales.getTaxList();
		
		// next free ref sentence
		try {
			refsent = dlSales.getNextFreeProductReferenceSent();
		} catch (BasicException e1) {
			refsent = null;
		}

		// The categories model
		m_sentcat = dlSales.getCategoriesListSortedByName();
		m_CategoryModel = new ComboBoxValModel();

		// The taxes model
		taxcatsent = dlSales.getTaxCategoriesList();
		taxcatmodel = new ComboBoxValModel();

		// The attributes model
		attsent = dlSales.getAttributeSetList();
		attmodel = new ComboBoxValModel();

		m_CodetypeModel = new ComboBoxValModel();
		m_CodetypeModel.add(null);
		m_CodetypeModel.add(CodeType.EAN13);
		m_CodetypeModel.add(CodeType.CODE128);
		m_jCodetype.setModel(m_CodetypeModel);
		m_jCodetype.setVisible(false);

		m_jRef.getDocument().addDocumentListener(dirty);
		m_jCode.getDocument().addDocumentListener(dirty);
		m_jCode.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// for code only numeric and "," characters are allowed
				String pattern = "[^0-9,]+";
				
				if(Pattern.matches(".*" + pattern + ".*", m_jCode.getText()))
				{
					m_jCode.setText(m_jCode.getText().replaceAll(pattern, ""));
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		m_jName.getDocument().addDocumentListener(dirty);
		m_jComment.addActionListener(dirty);
		m_jScale.addActionListener(dirty);
		m_jCategory.addActionListener(dirty);
		m_jTax.addActionListener(dirty);
		m_jAtt.addActionListener(dirty);
		m_jPriceBuy.getDocument().addDocumentListener(dirty);
		m_jPriceSell.getDocument().addDocumentListener(dirty);
		m_jImage.addPropertyChangeListener("image", dirty);
		m_jImage.addPropertyChangeListener("background", dirty);
		m_jstockunit.getDocument().addDocumentListener(dirty);
		if (issaege) {
			m_jstockheight.getDocument().addDocumentListener(dirty);
			m_jstockwidth.getDocument().addDocumentListener(dirty);
			m_jstockLength.getDocument().addDocumentListener(dirty);
			// m_jstockCount.getDocument().addDocumentListener(dirty);
		}
		m_jstockcost.getDocument().addDocumentListener(dirty);
		m_jstockvolume.getDocument().addDocumentListener(dirty);
		m_jInCatalog.addActionListener(dirty);
		m_jCatalogOrder.getDocument().addDocumentListener(dirty);
		txtAttributes.getDocument().addDocumentListener(dirty);

		FieldsManager fm = new FieldsManager();
		m_jPriceBuy.getDocument().addDocumentListener(fm);
		m_jPriceSell.getDocument().addDocumentListener(new PriceSellManager());
		m_jTax.addActionListener(fm);

		m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
		m_jmargin.getDocument().addDocumentListener(new MarginManager());

		writeValueEOF();

		ScaleButtons();
	}

	public void activate() throws BasicException {

		// Load the taxes logic
		taxeslogic = new TaxesLogic(taxsent.list());

		m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
		m_jCategory.setModel(m_CategoryModel);

		taxcatmodel = new ComboBoxValModel(taxcatsent.list());
		m_jTax.setModel(taxcatmodel);

		attmodel = new ComboBoxValModel(attsent.list());
		attmodel.add(0, null);
		m_jAtt.setModel(attmodel);
	}

	public void refresh() {
	}

	public void writeValueEOF() {

		reportlock = true;
		// Los valores
		m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
		m_id = null;
		m_jRef.setText(null);
		m_jCode.setText(null);
		m_jName.setText(null);
		m_jComment.setSelected(false);
		m_jScale.setSelected(false);
		m_CategoryModel.setSelectedKey(null);
		taxcatmodel.setSelectedKey(null);
		attmodel.setSelectedKey(null);
		m_jPriceBuy.setText(null);
		setPriceSell(null);
		m_jImage.setImage(null);

		m_jstockunit.setText(null);
		if (issaege) {
			m_jstockheight.setText(null);
			m_jstockwidth.setText(null);
			m_jstockLength.setText(null);
			// m_jstockCount.setText(null);
		}
		m_jstockcost.setText(null);
		m_jstockvolume.setText(null);
		m_jInCatalog.setSelected(false);
		m_jCatalogOrder.setText(null);
		txtAttributes.setText(null);
		reportlock = false;

		// Los habilitados
		m_jRef.setEnabled(false);
		m_jCode.setEnabled(false);
		m_jName.setEnabled(false);
		m_jComment.setEnabled(false);
		m_jScale.setEnabled(false);
		m_jCategory.setEnabled(false);
		m_jTax.setEnabled(false);
		m_jAtt.setEnabled(false);
		m_jPriceBuy.setEnabled(false);
		m_jPriceSell.setEnabled(false);
		m_jPriceSellTax.setEnabled(false);
		m_jmargin.setEnabled(false);
		m_jImage.setEnabled(false);

		m_jstockunit.setEnabled(false);
		if (issaege) {
			m_jstockheight.setEnabled(false);
			m_jstockwidth.setEnabled(false);
			m_jstockLength.setEnabled(false);
			// m_jstockCount.setEnabled(false);
		}
		m_jstockcost.setEnabled(false);
		m_jstockvolume.setEnabled(false);
		m_jInCatalog.setEnabled(false);
		m_jCatalogOrder.setEnabled(false);
		txtAttributes.setEnabled(false);

		calculateMargin();
		calculatePriceSellTax();
	}

	public void writeValueInsert() {

		reportlock = true;
		// Los valores
		m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
		m_id = UUID.randomUUID().toString();
		
		
		String categoryId = null;
		if(m_productfilter != null)
			categoryId = m_productfilter.GetSelectedCategoryKey();
		
		String newreference = null;
		if(refsent != null) {
			try {
				String newrefvalue = (String)refsent.find(categoryId, categoryId);
				if(newrefvalue == null && categoryId != null)
					newrefvalue = (String)refsent.find(null, null);
				
				newreference = newrefvalue;
			} catch (BasicException e) {
				// do nothing
			}
		}
		
		m_jRef.setText(newreference);
		m_jCode.setText(newreference);
		
		m_jName.setText(null);
		m_jComment.setSelected(false);
		m_jScale.setSelected(false);
		
		m_CategoryModel.setSelectedKey(categoryId);
		
		taxcatmodel.setSelectedKey(null);
		attmodel.setSelectedKey(null);
		m_jPriceBuy.setText("0");
		setPriceSell(null);
		m_jImage.setImage(null);
		m_jImage.setSelecteBGColor(null);

		m_jstockunit.setText(null);
		if (issaege) {
			m_jstockheight.setText(null);
			m_jstockwidth.setText(null);
			m_jstockLength.setText(null);
			// m_jstockCount.setText(null);
		}
		m_jstockcost.setText(null);
		m_jstockvolume.setText(null);
		m_jInCatalog.setSelected(true);
		m_jCatalogOrder.setText(null);
		txtAttributes.setText(null);
		reportlock = false;

		// Los habilitados
		m_jRef.setEnabled(true);
		m_jCode.setEnabled(true);
		m_jName.setEnabled(true);
		m_jComment.setEnabled(true);
		m_jScale.setEnabled(true);
		m_jCategory.setEnabled(true);
		m_jTax.setEnabled(true);
		m_jAtt.setEnabled(true);
		m_jPriceBuy.setEnabled(true);
		m_jPriceSell.setEnabled(true);
		m_jPriceSellTax.setEnabled(true);
		m_jmargin.setEnabled(true);
		m_jImage.setEnabled(true);

		m_jstockunit.setEnabled(true);
		if (issaege) {
			m_jstockheight.setEnabled(true);
			m_jstockwidth.setEnabled(true);
			m_jstockLength.setEnabled(true);
			// m_jstockCount.setEnabled(true);
		}
		m_jstockcost.setEnabled(true);
		m_jstockvolume.setEnabled(true);
		m_jInCatalog.setEnabled(true);
		m_jCatalogOrder.setEnabled(true);
		txtAttributes.setEnabled(true);

		calculateMargin();
		calculatePriceSellTax();
	}

	public void writeValueDelete(Object value) {

		reportlock = true;
		Object[] myprod = (Object[]) value;
		m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]) + " "
				+ AppLocal.getIntString("label.recorddeleted"));
		m_id = myprod[0];
		m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
		m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
		m_jName.setText(Formats.STRING.formatValue(myprod[3]));
		m_jComment.setSelected(((Boolean) myprod[4]).booleanValue());
		m_jScale.setSelected(((Boolean) myprod[5]).booleanValue());
		m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
		setPriceSell(myprod[7]);
		m_CategoryModel.setSelectedKey(myprod[8]);
		taxcatmodel.setSelectedKey(myprod[9]);
		attmodel.setSelectedKey(myprod[10]);
		m_jImage.setImage((BufferedImage) myprod[11]);

		m_jstockunit.setText(Formats.CURRENCY.formatValue(myprod[18]));
		if (issaege) {
			m_jstockheight.setText(Formats.CURRENCY.formatValue(myprod[19]));
			m_jstockwidth.setText(Formats.CURRENCY.formatValue(myprod[20]));
			m_jstockLength.setText(Formats.CURRENCY.formatValue(myprod[21]));
			// m_jstockCount.setText(Formats.CURRENCY.formatValue(myprod[21]));
		}
		m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[13]));
		m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[14]));
		m_jInCatalog.setSelected(((Boolean) myprod[15]).booleanValue());
		m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[16]));
		txtAttributes.setText(Formats.BYTEA.formatValue(myprod[17]));
		txtAttributes.setCaretPosition(0);
		reportlock = false;

		// Los habilitados
		m_jRef.setEnabled(false);
		m_jCode.setEnabled(false);
		m_jName.setEnabled(false);
		m_jComment.setEnabled(false);
		m_jScale.setEnabled(false);
		m_jCategory.setEnabled(false);
		m_jTax.setEnabled(false);
		m_jAtt.setEnabled(false);
		m_jPriceBuy.setEnabled(false);
		m_jPriceSell.setEnabled(false);
		m_jPriceSellTax.setEnabled(false);
		m_jmargin.setEnabled(false);
		m_jImage.setEnabled(false);

		m_jstockunit.setEnabled(false);
		if (issaege) {
			m_jstockheight.setEnabled(false);
			m_jstockwidth.setEnabled(false);
			m_jstockLength.setEnabled(false);
			// m_jstockCount.setEnabled(false);
		}
		m_jstockcost.setEnabled(false);
		m_jstockvolume.setEnabled(false);
		m_jInCatalog.setEnabled(false);
		m_jCatalogOrder.setEnabled(false);
		txtAttributes.setEnabled(false);

		calculateMargin();
		calculatePriceSellTax();
	}

	public void writeValueEdit(Object value) {

		reportlock = true;
		Object[] myprod = (Object[]) value;
		m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]));
		m_id = myprod[0];
		m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
		m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
		m_jName.setText(Formats.STRING.formatValue(myprod[3]));
		m_jComment.setSelected(((Boolean) myprod[4]).booleanValue());
		m_jScale.setSelected(((Boolean) myprod[5]).booleanValue());
		m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
		setPriceSell(myprod[7]);
		m_CategoryModel.setSelectedKey(myprod[8]);
		taxcatmodel.setSelectedKey(myprod[9]);
		attmodel.setSelectedKey(myprod[10]);
		m_jImage.setImage((BufferedImage) myprod[11]);
		m_jImage.setSelecteBGColor(Formats.STRING.formatValue(myprod[12]));
		m_jstockunit.setText(Formats.STRING.formatValue(myprod[18]));
		if (issaege) {
			m_jstockheight.setText(Formats.STRING.formatValue(myprod[19]));
			m_jstockwidth.setText(Formats.STRING.formatValue(myprod[20]));
			m_jstockLength.setText(Formats.STRING.formatValue(myprod[21]));
			// m_jstockCount.setText(Formats.STRING.formatValue(myprod[21]));
		}
		m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[13]));
		m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[14]));
		m_jInCatalog.setSelected(((Boolean) myprod[15]).booleanValue());
		m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[16]));
		txtAttributes.setText(Formats.BYTEA.formatValue(myprod[17]));
		txtAttributes.setCaretPosition(0);
		reportlock = false;

		// Los habilitados
		m_jRef.setEnabled(true);
		m_jCode.setEnabled(true);
		m_jName.setEnabled(true);
		m_jComment.setEnabled(true);
		m_jScale.setEnabled(true);
		m_jCategory.setEnabled(true);
		m_jTax.setEnabled(true);
		m_jAtt.setEnabled(true);
		m_jPriceBuy.setEnabled(true);
		m_jPriceSell.setEnabled(true);
		m_jPriceSellTax.setEnabled(true);
		m_jmargin.setEnabled(true);
		m_jImage.setEnabled(true);

		m_jstockunit.setEnabled(true);
		if (issaege) {
			m_jstockheight.setEnabled(true);
			m_jstockwidth.setEnabled(true);
			m_jstockLength.setEnabled(true);
			// m_jstockCount.setEnabled(true);
		}
		m_jstockcost.setEnabled(true);
		m_jstockvolume.setEnabled(true);
		m_jInCatalog.setEnabled(true);
		m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());
		txtAttributes.setEnabled(true);

		calculateMargin();
		calculatePriceSellTax();
	}

	public Object createValue() throws BasicException {

		Object[] myprod = new Object[22];
		// if(issaege)
		// myprod = new Object[21];
		myprod[0] = m_id;
		myprod[1] = m_jRef.getText();
		myprod[2] = m_jCode.getText();
		myprod[3] = m_jName.getText();
		myprod[4] = Boolean.valueOf(m_jComment.isSelected());
		myprod[5] = Boolean.valueOf(m_jScale.isSelected());
		myprod[6] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
		myprod[7] = pricesell;
		myprod[8] = m_CategoryModel.getSelectedKey();
		myprod[9] = taxcatmodel.getSelectedKey();
		myprod[10] = attmodel.getSelectedKey();
		myprod[11] = m_jImage.getImage();
		myprod[12] = m_jImage.getSelecteBGColor();
		myprod[13] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
		myprod[14] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
		myprod[15] = Boolean.valueOf(m_jInCatalog.isSelected());

		if (m_jCatalogOrder.getText() == null || "".equals(m_jCatalogOrder.getText().trim())) {
			myprod[16] = Formats.DOUBLE.parseValue(m_jRef.getText() == null ? "" : m_jRef.getText().replaceAll("[^0-9]", "")); 
		} else
			myprod[16] = Formats.DOUBLE.parseValue(m_jCatalogOrder.getText());

		myprod[17] = Formats.BYTEA.parseValue(txtAttributes.getText());

		myprod[18] = m_jstockunit.getText();
		if (issaege) {
			myprod[19] = m_jstockheight.getText();
			myprod[20] = m_jstockwidth.getText();
			myprod[21] = m_jstockLength.getText();
			// myprod[21] = m_jstockCount.getText();
		}
		return myprod;
	}

	public Component getComponent() {
		return this;
	}

	private void calculateMargin() {

		if (!reportlock) {
			reportlock = true;

			Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
			Double dPriceSell = (Double) pricesell;

			if (dPriceBuy == null || dPriceSell == null) {
				m_jmargin.setText(null);
			} else {
				m_jmargin.setText(Formats.PERCENT
						.formatValue(new Double(dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
			}
			reportlock = false;
		}
	}

	private void calculatePriceSellTax() {

		if (!reportlock) {
			reportlock = true;

			Double dPriceSell = (Double) pricesell;

			if (dPriceSell == null) {
				m_jPriceSellTax.setText(null);
			} else {
				double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem(), new Date());
				m_jPriceSellTax
						.setText(Formats.CURRENCY.formatValue(new Double(dPriceSell.doubleValue() * (1.0 + dTaxRate))));
			}
			reportlock = false;
		}
	}

	private void calculatePriceSellfromMargin() {

		if (!reportlock) {
			reportlock = true;

			Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
			Double dMargin = readPercent(m_jmargin.getText());

			if (dMargin == null || dPriceBuy == null) {
				setPriceSell(null);
			} else {
				setPriceSell(new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue())));
			}

			reportlock = false;
		}

	}

	private void calculatePriceSellfromPST() {

		if (!reportlock) {
			reportlock = true;

			Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());

			if (dPriceSellTax == null) {
				setPriceSell(null);
			} else {
				double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem(), new Date());
				setPriceSell(new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate)));
			}

			reportlock = false;
		}
	}

	private void setPriceSell(Object value) {

		if (!priceselllock) {
			priceselllock = true;
			pricesell = value;
			m_jPriceSell.setText(Formats.CURRENCY.formatValue(pricesell));
			priceselllock = false;
		}
	}

	private class PriceSellManager implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			if (!priceselllock) {
				priceselllock = true;
				pricesell = readCurrency(m_jPriceSell.getText());
				priceselllock = false;
			}
			calculateMargin();
			calculatePriceSellTax();
		}

		public void insertUpdate(DocumentEvent e) {
			if (!priceselllock) {
				priceselllock = true;
				pricesell = readCurrency(m_jPriceSell.getText());
				priceselllock = false;
			}
			calculateMargin();
			calculatePriceSellTax();
		}

		public void removeUpdate(DocumentEvent e) {
			if (!priceselllock) {
				priceselllock = true;
				pricesell = readCurrency(m_jPriceSell.getText());
				priceselllock = false;
			}
			calculateMargin();
			calculatePriceSellTax();
		}
	}

	private class FieldsManager implements DocumentListener, ActionListener {
		public void changedUpdate(DocumentEvent e) {
			Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());
			if (dPriceSellTax == null) {
				calculatePriceSellfromPST();
			} else {
				calculatePriceSellTax();
			}
			calculateMargin();
		}

		public void insertUpdate(DocumentEvent e) {
			Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());
			if (dPriceSellTax == null || dPriceSellTax == 0) {
				calculatePriceSellTax();
			} else {
				calculatePriceSellfromPST();
			}
			calculateMargin();
		}

		public void removeUpdate(DocumentEvent e) {
			Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());
			if (dPriceSellTax == null || dPriceSellTax == 0) {
				calculatePriceSellTax();
			} else {
				calculatePriceSellfromPST();
			}
			calculateMargin();
		}

		public void actionPerformed(ActionEvent e) {
			Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());
			if (dPriceSellTax == null || dPriceSellTax == 0) {
				calculatePriceSellTax();
			} else {
				calculatePriceSellfromPST();
			}
			calculateMargin();
		}
	}

	private class PriceTaxManager implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			calculatePriceSellfromPST();
			calculateMargin();
		}

		public void insertUpdate(DocumentEvent e) {
			calculatePriceSellfromPST();
			calculateMargin();
		}

		public void removeUpdate(DocumentEvent e) {
			calculatePriceSellfromPST();
			calculateMargin();
		}
	}

	private class MarginManager implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			calculatePriceSellfromMargin();
			calculatePriceSellTax();
		}

		public void insertUpdate(DocumentEvent e) {
			calculatePriceSellfromMargin();
			calculatePriceSellTax();
		}

		public void removeUpdate(DocumentEvent e) {
			calculatePriceSellfromMargin();
			calculatePriceSellTax();
		}
	}

	private final static Double readCurrency(String sValue) {
		try {
			return (Double) Formats.CURRENCY.parseValue(sValue);
		} catch (BasicException e) {
			return null;
		}
	}

	private final static Double readPercent(String sValue) {
		try {
			return (Double) Formats.PERCENT.parseValue(sValue);
		} catch (BasicException e) {
			return null;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	// private void initComponents() {
	//
	// jLabel1 = new javax.swing.JLabel();
	// jLabel2 = new javax.swing.JLabel();
	// m_jRef = new javax.swing.JTextField();
	// m_jName = new javax.swing.JTextField();
	// m_jTitle = new javax.swing.JLabel();
	// jTabbedPane1 = new javax.swing.JTabbedPane();
	// jPanel1 = new javax.swing.JPanel();
	// jLabel6 = new javax.swing.JLabel();
	// m_jCode = new javax.swing.JTextField();
	// m_jImage = new com.openbravo.data.gui.JImageEditor(m_App);
	// jLabel3 = new javax.swing.JLabel();
	// m_jPriceBuy = new javax.swing.JTextField();
	// jLabel4 = new javax.swing.JLabel();
	// m_jPriceSell = new javax.swing.JTextField();
	// jLabel5 = new javax.swing.JLabel();
	// m_jCategory = new javax.swing.JComboBox();
	// jLabel7 = new javax.swing.JLabel();
	// m_jTax = new javax.swing.JComboBox();
	// m_jmargin = new javax.swing.JTextField();
	// m_jPriceSellTax = new javax.swing.JTextField();
	// jLabel16 = new javax.swing.JLabel();
	// m_jCodetype = new javax.swing.JComboBox();
	// jLabel13 = new javax.swing.JLabel();
	// m_jAtt = new javax.swing.JComboBox();
	// jPanel2 = new javax.swing.JPanel();
	// jLabel9 = new javax.swing.JLabel();
	// m_jstockcost = new javax.swing.JTextField();
	// jLabel10 = new javax.swing.JLabel();
	// m_jstockvolume = new javax.swing.JTextField();
	// m_jScale = new javax.swing.JCheckBox();
	// m_jComment = new javax.swing.JCheckBox();
	// jLabel18 = new javax.swing.JLabel();
	// m_jCatalogOrder = new javax.swing.JTextField();
	// m_jInCatalog = new javax.swing.JCheckBox();
	// jLabel8 = new javax.swing.JLabel();
	// jLabel11 = new javax.swing.JLabel();
	// jLabel12 = new javax.swing.JLabel();
	// jPanel3 = new javax.swing.JPanel();
	// jScrollPane1 = new javax.swing.JScrollPane();
	// txtAttributes = new javax.swing.JTextArea();
	//
	// setLayout(null);
	//
	// m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
	// add(m_jTitle);
	// m_jTitle.setBounds(10, 10, 320, 30);
	//
	// jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
	// add(jLabel1);
	// jLabel1.setBounds(10, 50, 80, 15);
	//
	// jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
	// add(jLabel2);
	// jLabel2.setBounds(180, 50, 70, 15);
	// add(m_jRef);
	// m_jRef.setBounds(90, 50, 70, 19);
	// add(m_jName);
	// m_jName.setBounds(250, 50, 220, 19);
	//
	// // JScrollPane jScrCont = new JScrollPane();
	// //
	// jScrCont.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	// jPanel1.setLayout(null);
	// // jScrCont.setViewportView(jPanel1);
	//
	// jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
	// jPanel1.add(jLabel6);
	// jLabel6.setBounds(10, 20, 150, 15);
	// jPanel1.add(m_jCode);
	// m_jCode.setBounds(160, 20, 170, 19);
	//
	// jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
	// jPanel1.add(jLabel3);
	// jLabel3.setBounds(10, 50, 150, 15);
	//
	// m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel1.add(m_jPriceBuy);
	// m_jPriceBuy.setBounds(160, 50, 80, 19);
	//
	// jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
	// jPanel1.add(jLabel4);
	// jLabel4.setBounds(10, 80, 150, 15);
	//
	// m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel1.add(m_jPriceSell);
	// m_jPriceSell.setBounds(160, 80, 80, 19);
	//
	// jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
	// jPanel1.add(jLabel7);
	// jLabel7.setBounds(10, 110, 150, 15);
	// jPanel1.add(m_jTax);
	// m_jTax.setBounds(160, 110, 170, 20);
	//
	// jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); //
	// NOI18N
	// jPanel1.add(jLabel16);
	// jLabel16.setBounds(10, 140, 150, 15);
	//
	// m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel1.add(m_jPriceSellTax);
	// m_jPriceSellTax.setBounds(160, 140, 80, 19);
	//
	// jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
	// jPanel1.add(jLabel5);
	// jLabel5.setBounds(10, 170, 150, 15);
	// jPanel1.add(m_jCategory);
	// m_jCategory.setBounds(160, 170, 170, 20);
	//
	// m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel1.add(m_jmargin);
	// m_jmargin.setBounds(250, 80, 80, 19);
	//
	// jPanel1.add(m_jCodetype);
	// m_jCodetype.setBounds(250, 40, 80, 20);
	//
	// jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N
	// jPanel1.add(jLabel13);
	// jLabel13.setBounds(10, 200, 150, 15);
	// jPanel1.add(m_jAtt);
	// m_jAtt.setBounds(160, 200, 170, 20);
	//
	// jPanel1.add(m_jImage);
	// m_jImage.setBounds(340, 20, 260, 360);
	//
	// jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1);
	// // NOI18N
	//
	// jPanel2.setLayout(null);
	//
	// jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
	// jPanel2.add(jLabel9);
	// jLabel9.setBounds(10, 20, 150, 15);
	//
	// m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel2.add(m_jstockcost);
	// m_jstockcost.setBounds(160, 20, 80, 19);
	//
	// jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
	// jPanel2.add(jLabel10);
	// jLabel10.setBounds(10, 50, 150, 15);
	//
	// m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel2.add(m_jstockvolume);
	// m_jstockvolume.setBounds(160, 50, 80, 19);
	// jPanel2.add(m_jScale);
	// m_jScale.setBounds(160, 140, 80, 21);
	// jPanel2.add(m_jComment);
	// m_jComment.setBounds(160, 110, 80, 21);
	//
	// jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
	// jPanel2.add(jLabel18);
	// jLabel18.setBounds(250, 80, 60, 15);
	//
	// m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
	// jPanel2.add(m_jCatalogOrder);
	// m_jCatalogOrder.setBounds(310, 80, 80, 19);
	//
	// m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(java.awt.event.ActionEvent evt) {
	// m_jInCatalogActionPerformed(evt);
	// }
	// });
	// jPanel2.add(m_jInCatalog);
	// m_jInCatalog.setBounds(160, 80, 50, 21);
	//
	// jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
	// jPanel2.add(jLabel8);
	// jLabel8.setBounds(10, 80, 150, 15);
	//
	// jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
	// jPanel2.add(jLabel11);
	// jLabel11.setBounds(10, 110, 150, 15);
	//
	// jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
	// jPanel2.add(jLabel12);
	// jLabel12.setBounds(10, 140, 150, 15);
	//
	// jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2);
	// // NOI18N
	//
	// jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5,
	// 5));
	// jPanel3.setLayout(new java.awt.BorderLayout());
	//
	// txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
	// jScrollPane1.setViewportView(txtAttributes);
	//
	// jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);
	//
	// jTabbedPane1.addTab(AppLocal.getIntString("label.properties"), jPanel3);
	// // NOI18N
	// add(jTabbedPane1);
	// jTabbedPane1.setBounds(10, 90, 660, 460);
	// }// </editor-fold>//GEN-END:initComponents

	private void initComponents() {
		this.issaege = Boolean
				.parseBoolean(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "module-saegewerk", "false"));

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		m_jRef = new javax.swing.JTextField();
		m_jRef.setBackground(new Color(255,230,230));
		m_jName = new javax.swing.JTextField();
		m_jName.setBackground(new Color(255,230,230));
		m_jTitle = new javax.swing.JLabel();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		m_jCode = new javax.swing.JTextField();
		m_jCode.setBackground(new Color(255,230,230));
		m_jImage = new com.openbravo.data.gui.JImageEditor(m_App);
		m_jImage.setMaxDimensions(new java.awt.Dimension(96, 96));
		
		jLabel3 = new javax.swing.JLabel();
		m_jPriceBuy = new javax.swing.JTextField();
		m_jPriceBuy.setBackground(new Color(255,230,230));
		jLabel4 = new javax.swing.JLabel();
		m_jPriceSell = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		m_jCategory = new javax.swing.JComboBox();
		m_jCategory.setBackground(new Color(255,230,230));
		jLabel7 = new javax.swing.JLabel();
		m_jTax = new javax.swing.JComboBox();
		m_jTax.setBackground(new Color(255,230,230));
		m_jmargin = new javax.swing.JTextField();
		m_jPriceSellTax = new javax.swing.JTextField();
		m_jPriceSellTax.setBackground(new Color(255,230,230));
		jLabel16 = new javax.swing.JLabel();
		m_jCodetype = new javax.swing.JComboBox();
		jLabel13 = new javax.swing.JLabel();
		m_jAtt = new javax.swing.JComboBox();
		jPanel2 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();

		m_jstockunit = new javax.swing.JTextField();
		if (issaege) {
			m_jstockheight = new javax.swing.JTextField();
			m_jstockwidth = new javax.swing.JTextField();
			m_jstockLength = new javax.swing.JTextField();
			// m_jstockCount = new javax.swing.JTextField();
		}
		m_jstockcost = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		m_jstockvolume = new javax.swing.JTextField();
		m_jScale = new javax.swing.JCheckBox();
		m_jComment = new javax.swing.JCheckBox();
		jLabel18 = new javax.swing.JLabel();
		m_jCatalogOrder = new javax.swing.JTextField();
		m_jInCatalog = new javax.swing.JCheckBox();
		jLabel8 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txtAttributes = new javax.swing.JTextArea();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.scrollView = new JScrollPane();
		add(scrollView);
		JPanel root = new JPanel();
		scrollView.setViewportView(root);

		GridBagLayout gblayout = new GridBagLayout();
		root.setLayout(gblayout);

		m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.WEST;
		gbc_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_title.insets = new Insets(5, 5, 0, 0);
		gbc_title.weightx = 1.0;
		gbc_title.gridx = 0;
		gbc_title.gridy = 0;
		gbc_title.gridwidth = 4;
		root.add(m_jTitle, gbc_title);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5, 5, 0, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 4;
		gbc_space1.gridy = 0;
		root.add(space1, gbc_space1);

		jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(5, 5, 0, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 1;
		root.add(jLabel1, gbc_lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 1;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5, 5, 0, 0);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 1;
		root.add(m_jRef, gbc_textPane);

		jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(5, 5, 0, 0);
		gbc_lbl2.gridx = 2;
		gbc_lbl2.gridy = 1;
		root.add(jLabel2, gbc_lbl2);

		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 1;
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(5, 5, 0, 0);
		gbc_textPane2.weightx = 1.0;
		gbc_textPane2.gridx = 3;
		gbc_textPane2.gridy = 1;
		root.add(m_jName, gbc_textPane2);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5, 5, 0, 0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 4;
		gbc_space2.gridy = 1;
		root.add(space2, gbc_space2);

		JScrollPane scrollView1 = new JScrollPane();
		scrollView1.setViewportView(jPanel1);
		jPanel1.setLayout(new GridBagLayout());

		jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
		GridBagConstraints gbc_lbl3 = new GridBagConstraints();
		gbc_lbl3.anchor = GridBagConstraints.WEST;
		gbc_lbl3.insets = new Insets(5, 5, 0, 0);
		gbc_lbl3.gridx = 0;
		gbc_lbl3.gridy = 0;
		jPanel1.add(jLabel6, gbc_lbl3);

		GridBagConstraints gbc_textPane3 = new GridBagConstraints();
		gbc_textPane3.gridwidth = 2;
		gbc_textPane3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane3.insets = new Insets(5, 5, 0, 0);
		gbc_textPane3.weightx = 2.0;
		gbc_textPane3.gridx = 1;
		gbc_textPane3.gridy = 0;
		jPanel1.add(m_jCode, gbc_textPane3);

		jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
		GridBagConstraints gbc_lbl4 = new GridBagConstraints();
		gbc_lbl4.anchor = GridBagConstraints.WEST;
		gbc_lbl4.insets = new Insets(5, 5, 0, 0);
		gbc_lbl4.gridx = 0;
		gbc_lbl4.gridy = 1;
		jPanel1.add(jLabel3, gbc_lbl4);

		m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane4 = new GridBagConstraints();
		gbc_textPane4.gridwidth = 1;
		gbc_textPane4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane4.insets = new Insets(5, 5, 0, 0);
		gbc_textPane4.weightx = 1.0;
		gbc_textPane4.gridx = 1;
		gbc_textPane4.gridy = 1;
		jPanel1.add(m_jPriceBuy, gbc_textPane4);

		jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
		GridBagConstraints gbc_lbl5 = new GridBagConstraints();
		gbc_lbl5.anchor = GridBagConstraints.WEST;
		gbc_lbl5.insets = new Insets(5, 5, 0, 0);
		gbc_lbl5.gridx = 0;
		gbc_lbl5.gridy = 2;
		jPanel1.add(jLabel4, gbc_lbl5);

		m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane5 = new GridBagConstraints();
		gbc_textPane5.gridwidth = 1;
		gbc_textPane5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane5.insets = new Insets(5, 5, 0, 0);
		gbc_textPane5.weightx = 1.0;
		gbc_textPane5.gridx = 1;
		gbc_textPane5.gridy = 2;
		jPanel1.add(m_jPriceSell, gbc_textPane5);

		m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane6 = new GridBagConstraints();
		gbc_textPane6.gridwidth = 1;
		gbc_textPane6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane6.insets = new Insets(5, 5, 0, 0);
		gbc_textPane6.weightx = 1.0;
		gbc_textPane6.gridx = 2;
		gbc_textPane6.gridy = 2;
		jPanel1.add(m_jmargin, gbc_textPane6);

		jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
		GridBagConstraints gbc_lbl6 = new GridBagConstraints();
		gbc_lbl6.anchor = GridBagConstraints.WEST;
		gbc_lbl6.insets = new Insets(5, 5, 0, 0);
		gbc_lbl6.gridx = 0;
		gbc_lbl6.gridy = 3;
		jPanel1.add(jLabel7, gbc_lbl6);

		GridBagConstraints gbc_textPane7 = new GridBagConstraints();
		gbc_textPane7.gridwidth = 2;
		gbc_textPane7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane7.insets = new Insets(5, 5, 0, 0);
		gbc_textPane7.weightx = 2.0;
		gbc_textPane7.gridx = 1;
		gbc_textPane7.gridy = 3;
		jPanel1.add(m_jTax, gbc_textPane7);

		jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
		GridBagConstraints gbc_lbl7 = new GridBagConstraints();
		gbc_lbl7.anchor = GridBagConstraints.WEST;
		gbc_lbl7.insets = new Insets(5, 5, 0, 0);
		gbc_lbl7.gridx = 0;
		gbc_lbl7.gridy = 4;
		jPanel1.add(jLabel16, gbc_lbl7);

		m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane8 = new GridBagConstraints();
		gbc_textPane8.gridwidth = 1;
		gbc_textPane8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane8.insets = new Insets(5, 5, 0, 0);
		gbc_textPane8.weightx = 1.0;
		gbc_textPane8.gridx = 1;
		gbc_textPane8.gridy = 4;
		jPanel1.add(m_jPriceSellTax, gbc_textPane8);

		jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
		GridBagConstraints gbc_lbl8 = new GridBagConstraints();
		gbc_lbl8.anchor = GridBagConstraints.WEST;
		gbc_lbl8.insets = new Insets(5, 5, 0, 0);
		gbc_lbl8.gridx = 0;
		gbc_lbl8.gridy = 5;
		jPanel1.add(jLabel5, gbc_lbl8);

		GridBagConstraints gbc_textPane9 = new GridBagConstraints();
		gbc_textPane9.gridwidth = 2;
		gbc_textPane9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane9.insets = new Insets(5, 5, 0, 0);
		gbc_textPane9.weightx = 2.0;
		gbc_textPane9.gridx = 1;
		gbc_textPane9.gridy = 5;
		jPanel1.add(m_jCategory, gbc_textPane9);

		// jPanel1.add(m_jCodetype);
		// m_jCodetype.setBounds(250, 40, 80, 20);

		jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N
		GridBagConstraints gbc_lbl9 = new GridBagConstraints();
		gbc_lbl9.anchor = GridBagConstraints.WEST;
		gbc_lbl9.insets = new Insets(5, 5, 0, 0);
		gbc_lbl9.gridx = 0;
		gbc_lbl9.gridy = 6;
		jPanel1.add(jLabel13, gbc_lbl9);

		// jPanel1.add(m_jAtt);
		GridBagConstraints gbc_textPane10 = new GridBagConstraints();
		gbc_textPane10.gridwidth = 2;
		gbc_textPane10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane10.insets = new Insets(5, 5, 0, 0);
		gbc_textPane10.weightx = 2.0;
		gbc_textPane10.gridx = 1;
		gbc_textPane10.gridy = 6;
		jPanel1.add(m_jAtt, gbc_textPane10);

		GridBagConstraints gbc_img1 = new GridBagConstraints();
		gbc_img1.insets = new Insets(5, 5, 0, 0);
		gbc_img1.weighty = 2.0;
		gbc_img1.fill = GridBagConstraints.BOTH;
		gbc_img1.gridwidth = 3;
		// gbc_img1.gridheight = 8;
		gbc_img1.gridx = 0;
		gbc_img1.gridy = 7;
		jPanel1.add(m_jImage, gbc_img1);

		jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), scrollView1); // NOI18N

		jPanel2.setLayout(new GridBagLayout());

		int index = 0;
		// add only if saegewerk modul is activated

		jLabel19.setText(AppLocal.getIntString("label.prodstockunit")); // NOI18N
		GridBagConstraints gbc_lbl20 = new GridBagConstraints();
		gbc_lbl20.anchor = GridBagConstraints.WEST;
		gbc_lbl20.insets = new Insets(5, 5, 0, 0);
		gbc_lbl20.gridx = 0;
		gbc_lbl20.gridy = index;
		jPanel2.add(jLabel19, gbc_lbl20);

		m_jstockunit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane21 = new GridBagConstraints();
		gbc_textPane21.gridwidth = 1;
		gbc_textPane21.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane21.insets = new Insets(5, 5, 0, 0);
		gbc_textPane21.weightx = 1.0;
		gbc_textPane21.gridx = 1;
		gbc_textPane21.gridy = index;
		jPanel2.add(m_jstockunit, gbc_textPane21);

		index++;
		if (issaege) {

			jLabel21.setText(AppLocal.getIntString("label.prodstockheight")); // NOI18N
			GridBagConstraints gbc_lbl22 = new GridBagConstraints();
			gbc_lbl22.anchor = GridBagConstraints.WEST;
			gbc_lbl22.insets = new Insets(5, 5, 0, 0);
			gbc_lbl22.gridx = 0;
			gbc_lbl22.gridy = index;
			jPanel2.add(jLabel21, gbc_lbl22);

			m_jstockheight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			GridBagConstraints gbc_textPane23 = new GridBagConstraints();
			gbc_textPane23.gridwidth = 1;
			gbc_textPane23.fill = GridBagConstraints.HORIZONTAL;
			gbc_textPane23.insets = new Insets(5, 5, 0, 0);
			gbc_textPane23.weightx = 1.0;
			gbc_textPane23.gridx = 1;
			gbc_textPane23.gridy = index;
			jPanel2.add(m_jstockheight, gbc_textPane23);

			index++;

			jLabel20.setText(AppLocal.getIntString("label.prodstockwidth")); // NOI18N
			GridBagConstraints gbc_lbl21 = new GridBagConstraints();
			gbc_lbl21.anchor = GridBagConstraints.WEST;
			gbc_lbl21.insets = new Insets(5, 5, 0, 0);
			gbc_lbl21.gridx = 0;
			gbc_lbl21.gridy = index;
			jPanel2.add(jLabel20, gbc_lbl21);

			m_jstockwidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			GridBagConstraints gbc_textPane22 = new GridBagConstraints();
			gbc_textPane22.gridwidth = 1;
			gbc_textPane22.fill = GridBagConstraints.HORIZONTAL;
			gbc_textPane22.insets = new Insets(5, 5, 0, 0);
			gbc_textPane22.weightx = 1.0;
			gbc_textPane22.gridx = 1;
			gbc_textPane22.gridy = index;
			jPanel2.add(m_jstockwidth, gbc_textPane22);

			index++;

			jLabel22.setText(AppLocal.getIntString("label.prodstocklength")); // NOI18N
			GridBagConstraints gbc_lbl23 = new GridBagConstraints();
			gbc_lbl23.anchor = GridBagConstraints.WEST;
			gbc_lbl23.insets = new Insets(5, 5, 0, 0);
			gbc_lbl23.gridx = 0;
			gbc_lbl23.gridy = index;
			jPanel2.add(jLabel22, gbc_lbl23);

			m_jstockLength.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			GridBagConstraints gbc_textPane24 = new GridBagConstraints();
			gbc_textPane24.gridwidth = 1;
			gbc_textPane24.fill = GridBagConstraints.HORIZONTAL;
			gbc_textPane24.insets = new Insets(5, 5, 0, 0);
			gbc_textPane24.weightx = 1.0;
			gbc_textPane24.gridx = 1;
			gbc_textPane24.gridy = index;
			jPanel2.add(m_jstockLength, gbc_textPane24);

			index++;

			// jLabel23.setText(AppLocal.getIntString("label.prodstockcount"));
			// // NOI18N
			// GridBagConstraints gbc_lbl24 = new GridBagConstraints();
			// gbc_lbl24.anchor = GridBagConstraints.WEST;
			// gbc_lbl24.insets = new Insets(5, 5, 0, 0);
			// gbc_lbl24.gridx = 0;
			// gbc_lbl24.gridy = index;
			// jPanel2.add(jLabel23, gbc_lbl24);
			//
			// m_jstockCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			// GridBagConstraints gbc_textPane25 = new GridBagConstraints();
			// gbc_textPane25.gridwidth = 1;
			// gbc_textPane25.fill = GridBagConstraints.HORIZONTAL;
			// gbc_textPane25.insets = new Insets(5, 5, 0, 0);
			// gbc_textPane25.weightx = 1.0;
			// gbc_textPane25.gridx = 1;
			// gbc_textPane25.gridy = index;
			// jPanel2.add(m_jstockCount, gbc_textPane25);

			index++;
		}

		jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
		GridBagConstraints gbc_lbl10 = new GridBagConstraints();
		gbc_lbl10.anchor = GridBagConstraints.WEST;
		gbc_lbl10.insets = new Insets(5, 5, 0, 0);
		gbc_lbl10.gridx = 0;
		gbc_lbl10.gridy = index;
		jPanel2.add(jLabel9, gbc_lbl10);

		m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane11 = new GridBagConstraints();
		gbc_textPane11.gridwidth = 1;
		gbc_textPane11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane11.insets = new Insets(5, 5, 0, 0);
		gbc_textPane11.weightx = 1.0;
		gbc_textPane11.gridx = 1;
		gbc_textPane11.gridy = index;
		jPanel2.add(m_jstockcost, gbc_textPane11);
		index++;

		jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
		GridBagConstraints gbc_lbl11 = new GridBagConstraints();
		gbc_lbl11.anchor = GridBagConstraints.WEST;
		gbc_lbl11.insets = new Insets(5, 5, 0, 0);
		gbc_lbl11.gridx = 0;
		gbc_lbl11.gridy = index;
		jPanel2.add(jLabel10, gbc_lbl11);

		m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane12 = new GridBagConstraints();
		gbc_textPane12.gridwidth = 1;
		gbc_textPane12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane12.insets = new Insets(5, 5, 0, 0);
		gbc_textPane12.weightx = 1.0;
		gbc_textPane12.gridx = 1;
		gbc_textPane12.gridy = index;
		jPanel2.add(m_jstockvolume, gbc_textPane12);
		index++;

		jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
		GridBagConstraints gbc_lbl12 = new GridBagConstraints();
		gbc_lbl12.anchor = GridBagConstraints.WEST;
		gbc_lbl12.insets = new Insets(5, 5, 0, 0);
		gbc_lbl12.gridx = 0;
		gbc_lbl12.gridy = index;
		jPanel2.add(jLabel18, gbc_lbl12);

		m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		GridBagConstraints gbc_textPane13 = new GridBagConstraints();
		gbc_textPane13.gridwidth = 1;
		gbc_textPane13.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane13.insets = new Insets(5, 5, 0, 0);
		gbc_textPane13.weightx = 1.0;
		gbc_textPane13.gridx = 1;
		gbc_textPane13.gridy = index;
		jPanel2.add(m_jCatalogOrder, gbc_textPane13);

		jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
		GridBagConstraints gbc_lbl13 = new GridBagConstraints();
		gbc_lbl13.anchor = GridBagConstraints.WEST;
		gbc_lbl13.insets = new Insets(5, 5, 0, 0);
		gbc_lbl13.gridx = 2;
		gbc_lbl13.gridy = index;
		jPanel2.add(jLabel8, gbc_lbl13);

		m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jInCatalogActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_textPane14 = new GridBagConstraints();
		gbc_textPane14.gridwidth = 1;
		gbc_textPane14.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane14.insets = new Insets(5, 5, 0, 0);
		gbc_textPane14.weightx = 1.0;
		gbc_textPane14.gridx = 3;
		gbc_textPane14.gridy = index;
		jPanel2.add(m_jInCatalog, gbc_textPane14);
		index++;

		jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
		GridBagConstraints gbc_lbl14 = new GridBagConstraints();
		gbc_lbl14.anchor = GridBagConstraints.WEST;
		gbc_lbl14.insets = new Insets(5, 5, 0, 0);
		gbc_lbl14.gridx = 0;
		gbc_lbl14.gridy = index;
		jPanel2.add(jLabel11, gbc_lbl14);

		GridBagConstraints gbc_textPane15 = new GridBagConstraints();
		gbc_textPane15.gridwidth = 1;
		gbc_textPane15.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane15.insets = new Insets(5, 5, 0, 0);
		gbc_textPane15.weightx = 1.0;
		gbc_textPane15.gridx = 1;
		gbc_textPane15.gridy = index;
		jPanel2.add(m_jComment, gbc_textPane15);
		index++;

		jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
		GridBagConstraints gbc_lbl15 = new GridBagConstraints();
		gbc_lbl15.anchor = GridBagConstraints.WEST;
		gbc_lbl15.insets = new Insets(5, 5, 0, 0);
		gbc_lbl15.gridx = 0;
		gbc_lbl15.gridy = index;
		jPanel2.add(jLabel12, gbc_lbl15);

		GridBagConstraints gbc_textPane16 = new GridBagConstraints();
		gbc_textPane16.gridwidth = 1;
		gbc_textPane16.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane16.insets = new Insets(5, 5, 0, 0);
		gbc_textPane16.weightx = 1.0;
		gbc_textPane16.gridx = 1;
		gbc_textPane16.gridy = index;
		jPanel2.add(m_jScale, gbc_textPane16);
		jLabel12.setVisible(false); // hide scale label and checkbox
		m_jScale.setVisible(false);
		index++;

		GridBagConstraints gbc_space = new GridBagConstraints();
		gbc_space.insets = new Insets(5, 5, 0, 0);
		gbc_space.weighty = 1.0;
		gbc_space.fill = GridBagConstraints.BOTH;
		gbc_space.gridwidth = 2;
		gbc_space.gridx = 0;
		gbc_space.gridy = index;
		jPanel2.add(new JLabel(""), gbc_space);

		jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N

		jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel3.setLayout(new java.awt.BorderLayout());

		txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
		jScrollPane1.setViewportView(txtAttributes);
		jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jTabbedPane1.addTab(AppLocal.getIntString("label.properties"), jPanel3); // NOI18N
		GridBagConstraints gbc_tab = new GridBagConstraints();
		gbc_tab.insets = new Insets(5, 5, 0, 0);
		gbc_tab.weighty = 1.0;
		gbc_tab.fill = GridBagConstraints.BOTH;
		gbc_tab.gridwidth = 4;
		gbc_tab.gridx = 0;
		gbc_tab.gridy = 2;
		root.add(jTabbedPane1, gbc_tab);
	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTitle, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel10, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel11, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel12, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel13, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel16, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel18, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel8, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel5, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel6, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel7, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel8, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel9, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel19, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel20, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel21, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel22, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel23, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jCatalogOrder, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jCode, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jPriceBuy, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jPriceSell, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jPriceSellTax, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jRef, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jmargin, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockunit, "common-small-fontsize", "32");
		if (issaege) {
			PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockheight, "common-small-fontsize", "32");
			PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockwidth, "common-small-fontsize", "32");
			PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockLength, "common-small-fontsize", "32");
			// PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockCount,
			// "common-small-fontsize", "32");
		}
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockcost, "common-small-fontsize", "32");
		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jstockvolume, "common-small-fontsize", "32");

		PropertyUtil.ScaleComboFontsize(m_App, m_jCategory, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jTax, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jCodetype, "common-small-fontsize", "32");
		PropertyUtil.ScaleComboFontsize(m_App, m_jAtt, "common-small-fontsize", "32");

		PropertyUtil.ScaleCheckboxSize(m_App, m_jScale, "common-small-fontsize", "32");
		PropertyUtil.ScaleCheckboxSize(m_App, m_jInCatalog, "common-small-fontsize", "32");
		PropertyUtil.ScaleCheckboxSize(m_App, m_jComment, "common-small-fontsize", "32");

		PropertyUtil.ScaleTabbedPaneFontsize(m_App, jTabbedPane1, "common-small-fontsize", "32");

		PropertyUtil.ScaleScrollbar(m_App, scrollView);

		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		// PropertyUtil.ScaleButtonIcon(m_jCatalogAdd, menuwidth, menuheight,
		// fontsize);
		// PropertyUtil.ScaleButtonIcon(m_jCatalogDelete, menuwidth, menuheight,
		// fontsize);
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

	private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jInCatalogActionPerformed

		if (m_jInCatalog.isSelected()) {
			m_jCatalogOrder.setEnabled(true);
		} else {
			m_jCatalogOrder.setEnabled(false);
			// m_jCatalogOrder.setText(null);
		}

	}// GEN-LAST:event_m_jInCatalogActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JComboBox m_jAtt;
	private javax.swing.JTextField m_jCatalogOrder;
	private javax.swing.JComboBox m_jCategory;
	private javax.swing.JTextField m_jCode;
	private javax.swing.JComboBox m_jCodetype;
	private javax.swing.JCheckBox m_jComment;
	private com.openbravo.data.gui.JImageEditor m_jImage;
	private javax.swing.JCheckBox m_jInCatalog;
	private javax.swing.JTextField m_jName;
	private javax.swing.JTextField m_jPriceBuy;
	private javax.swing.JTextField m_jPriceSell;
	private javax.swing.JTextField m_jPriceSellTax;
	private javax.swing.JTextField m_jRef;
	private javax.swing.JCheckBox m_jScale;
	private javax.swing.JComboBox m_jTax;
	private javax.swing.JLabel m_jTitle;
	private javax.swing.JTextField m_jmargin;
	private javax.swing.JTextField m_jstockcost;
	private javax.swing.JTextField m_jstockunit;
	private javax.swing.JTextField m_jstockvolume;
	private javax.swing.JTextArea txtAttributes;
	// End of variables declaration//GEN-END:variables

}
