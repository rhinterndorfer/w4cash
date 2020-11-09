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

package com.openbravo.pos.catalog;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.ThumbNailBuilder;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TaxInfo;

/**
 *
 * @author adrianromero
 */
public class JCatalog extends JPanel implements /* ListSelectionListener, */ CatalogSelector {

	private static final long serialVersionUID = 7484712968447112855L;
	protected EventListenerList listeners = new EventListenerList();
	private DataLogicSales m_dlSales;
	private TaxesLogic taxeslogic;

	private boolean pricevisible;
	private boolean taxesincluded;

	// Set of Products panels
	private Map<String, ProductInfoExt> m_productsset = new HashMap<String, ProductInfoExt>();

	// Set of Categoriespanels
	private Set<String> m_categoriesset = new HashSet<String>();

	private ThumbNailBuilder tnbbutton;
	private ThumbNailBuilder tnbcat;

	// private CategoryInfo showingcategory = null;
	private CategoryInfo selectedCategory = null;
	private CategoryInfo mainCategory = null;
	private CategoryInfo mainCategoryFirst = null;
	private CategoryInfo previousCategory = null;

	private AppView m_App;

	private JCatalogTab jcategoryTab;
	private int productFontSize;
	private int catFontSize;
	private String categoriesFilter;

	private int doubleClickTimeoutMillis = 0;
	private String lastProductId = null;
	private Long lastProductTimeMillis = (long) 0;

	/** Creates new form JCatalog */
	public JCatalog(AppView app, DataLogicSales dlSales) {
		this(app, dlSales, false, false, 64, 54, 32, 32, 20, 20);
	}

	public JCatalog(AppView app, DataLogicSales dlSales, boolean pricevisible, boolean taxesincluded, int widthProduct,
			int heightProduct, int widthCat, int heightCat, int productFont, int catFont) {

		m_App = app;
		m_dlSales = dlSales;
		this.categoriesFilter = PropertyUtil.GetCategoriesFilter(app);
		this.pricevisible = pricevisible;
		this.taxesincluded = taxesincluded;

		this.productFontSize = productFont;
		this.catFontSize = catFont;

		initComponents();

		// m_jListCategories.addListSelectionListener(this);

		PropertyUtil.ScaleScrollbar(m_App, m_jscrollcat);

		tnbcat = new ThumbNailBuilder(widthCat, heightCat, "com/openbravo/images/empty.png");
		tnbbutton = new ThumbNailBuilder(widthProduct, heightProduct, "com/openbravo/images/empty.png");

		doubleClickTimeoutMillis = Integer
				.parseInt(m_App.getProperties().getProperty("product.doubleClickTimeoutMillis"));

	}

	public Component getComponent() {
		return this;
	}

	@Override
	public void ScaleButtonIcons() {
		int width = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "category-img-width", "32"));
		int height = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "category-img-height", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		// PropertyUtil.ScaleButtonIcon(m_jUp, width, height, fontsize);
		// PropertyUtil.ScaleButtonIcon(m_jDown, width, height, fontsize);
		PropertyUtil.ScaleButtonIcon(m_btnBack1, width, height, fontsize);
		// ScaleButtonIcon(btn, width, height);
	}

	public void showCatalogPanel(String id) {

		if ("".equals(id) && this.mainCategoryFirst != null) {
			this.selectedCategory = this.mainCategoryFirst;
			this.previousCategory = this.mainCategoryFirst;
			this.mainCategory = this.mainCategoryFirst;
			showRootCategoriesPanel();
		} else if (id == null) {
			showRootCategoriesPanel();
		} else {
			showProductPanel(id);
		}
	}

	public void loadCatalog() throws BasicException {

		// delete all categories panel
		m_jProducts.removeAll();
		m_jCategoryList.removeAll();
		jcategoryTab = null;
		m_productsset.clear();
		m_categoriesset.clear();

		// showingcategory = null;

		// Load the taxes logic
		taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());

		// Load all categories.
		java.util.List<CategoryInfo> categories = m_dlSales.getRootCategories(categoriesFilter);

		// Select the first category

		fillCategories(categories);

		// m_jListCategories.setCellRenderer(new SmallCategoryRenderer());
		// m_jListCategories.setModel(new CategoriesListModel(categories)); //
		// aCatList

		if (categories.size() == 0) {
			m_jscrollcat.setVisible(false);
			jPanel2.setVisible(false);
		} else {
			m_jscrollcat.setVisible(true);
			jPanel2.setVisible(true);
			this.selectedCategory = categories.get(0);
			this.mainCategory = categories.get(0);
			this.mainCategoryFirst = categories.get(0);
			selectCategoryPanel(selectedCategory);
			// m_jListCategories.setSelectedIndex(0);
		}

		// Display catalog panel
		showRootCategoriesPanel();
	}

	private void fillCategories(java.util.List<CategoryInfo> categories) {

		for (CategoryInfo category : categories) {
			if (this.jcategoryTab == null) {
				this.jcategoryTab = new JCatalogTab(m_App);
				this.jcategoryTab.getScrollPane()
						.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				jcategoryTab.applyComponentOrientation(getComponentOrientation());
				// jcategoryTab.
				m_jCategoryList.add(jcategoryTab, "CATEGORY");
			}
			jcategoryTab.addButton(
					new ImageIcon(tnbcat.getThumbNailText(category.getImage(), category.getName(), catFontSize)),
					category.getBgColor(), new SelectedCategoryMain(category));

			selectCategoryPanel(category);

			// product components not used at the moment
			// fillCategoryProductComments(category);

		}

		CardLayout cl = (CardLayout) (m_jCategoryList.getLayout());
		cl.show(m_jCategoryList, "CATEGORY");
	}

	private void fillCategoryProductComments(CategoryInfo category) {
		List<ProductInfoExt> products2 = new ArrayList<>();
		try {
			products2 = m_dlSales.getProductCatalog(category.getID());
		} catch (BasicException e) {
			e.printStackTrace();
		}
		for (ProductInfoExt prod : products2) {
			try {
				// Create products panel
				java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(prod.getID());

				if (products.size() == 0) {
					// no hay productos por tanto lo anado a la de vacios y
					// muestro el panel principal.
					m_productsset.put(prod.getID(), null);
				} else {
					// Load product panel
					ProductInfoExt product = m_dlSales.getProductInfo(prod.getID());
					m_productsset.put(prod.getID(), product);

					JCatalogTab jcurrTab = new JCatalogTab(m_App);
					jcurrTab.getScrollPane()
							.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					jcurrTab.applyComponentOrientation(getComponentOrientation());
					m_jProducts.add(jcurrTab, "PRODUCT." + prod.getID());

					// Add products
					for (ProductInfoExt prod2 : products) {
						jcurrTab.addButton(new ImageIcon(
								tnbbutton.getThumbNailText(prod2.getImage(), getProductLabel(prod2), productFontSize)),
								prod2.getBgColor(), new SelectedAction(prod2));
					}
				}
			} catch (BasicException eb) {
				eb.printStackTrace();
				m_productsset.put(prod.getID(), null);
			}
		}
	}

	public void setComponentEnabled(boolean value) {

		m_jCategoryList.setEnabled(value);
		m_jscrollcat.setEnabled(value);
		m_lblIndicator.setEnabled(value);
		m_btnBack1.setEnabled(value);
		m_jProducts.setEnabled(value);
		synchronized (m_jProducts.getTreeLock()) {
			int compCount = m_jProducts.getComponentCount();
			for (int i = 0; i < compCount; i++) {
				m_jProducts.getComponent(i).setEnabled(value);
			}
		}

		this.setEnabled(value);
	}

	public void addActionListener(ActionListener l) {
		listeners.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l) {
		listeners.remove(ActionListener.class, l);
	}

	public void valueChanged(ListSelectionEvent evt) {
		// scroll to button
		// if (!evt.getValueIsAdjusting()) {
		// int i = m_jListCategories.getSelectedIndex();
		// if (i >= 0) {
		// // Lo hago visible...
		// Rectangle oRect = m_jListCategories.getCellBounds(i, i);
		// m_jListCategories.scrollRectToVisible(oRect);
		// }
		// }
	}

	public void setCatWidth(int catWidth) {
		Dimension ms = m_jCategories.getMaximumSize();
		Dimension ps = m_jCategories.getPreferredSize();
		m_jCategories.setMaximumSize(new java.awt.Dimension(catWidth, ms.height));
		m_jCategories.setPreferredSize(new java.awt.Dimension(catWidth, ps.height));
	}

	protected void fireSelectedProduct(ProductInfoExt prod) {

		Long currentTimeMillis = System.currentTimeMillis();
		if (doubleClickTimeoutMillis > 0 && lastProductId == prod.getID()
				&& currentTimeMillis < lastProductTimeMillis + doubleClickTimeoutMillis) {
			lastProductTimeMillis = System.currentTimeMillis();
			return;
		}

		EventListener[] l = listeners.getListeners(ActionListener.class);
		ActionEvent e = null;
		for (int i = 0; i < l.length; i++) {
			if (e == null) {
				e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
			}
			((ActionListener) l[i]).actionPerformed(e);
		}
		lastProductTimeMillis = System.currentTimeMillis();
		lastProductId = prod.getID();
	}

	private void selectCategoryPanel(CategoryInfo catid) {

		try {
			// Load categories panel if not exists
			if (!m_categoriesset.contains(catid.getID())) {
				JCatalogTab jcurrTab = new JCatalogTab(m_App);
				jcurrTab.getScrollPane()
						.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				jcurrTab.applyComponentOrientation(getComponentOrientation());
				m_jProducts.add(jcurrTab, catid.getID());
				m_categoriesset.add(catid.getID());

				// Add subcategories
				java.util.List<CategoryInfo> categories = m_dlSales.getSubcategories(catid.getID(), categoriesFilter);
				for (CategoryInfo cat : categories) {
					jcurrTab.addButton(
							new ImageIcon(tnbcat.getThumbNailText(cat.getImage(), cat.getName(), catFontSize)),
							cat.getBgColor(), new SelectedCategory(cat));
					selectCategoryPanel(cat);

					// product components not used at the moment
					// fillCategoryProductComments(cat);
				}

				// Add products
				java.util.List<ProductInfoExt> products = m_dlSales.getProductCatalog(catid.getID());
				for (ProductInfoExt prod : products) {
					jcurrTab.addButton(new ImageIcon(
							tnbbutton.getThumbNailText(prod.getImage(), getProductLabel(prod), productFontSize)),
							prod.getBgColor(), new SelectedAction(prod));
				}
			}
			// HB select actual categorie
			this.selectedCategory = catid;

			// Show categories panel
			CardLayout cl = (CardLayout) (m_jProducts.getLayout());
			cl.show(m_jProducts, catid.getID());
		} catch (BasicException e) {
			JMessageDialog.showMessage(m_App, this,
					new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notactive"), e));
		}
	}

	private String getProductLabel(ProductInfoExt product) {

		if (pricevisible) {
			if (taxesincluded) {
				TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID(), new Date());
				return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax(tax);
			} else {
				return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
			}
		} else {
			return product.getName();
		}
	}

	private void selectIndicatorPanel(Icon icon, String label) {

		m_lblIndicator.setText(label);
		m_lblIndicator.setIcon(icon);

		// Show subcategories panel
		CardLayout cl = (CardLayout) (m_jCategories.getLayout());
		cl.show(m_jCategories, "subcategories");
	}

	private void selectIndicatorCategories() {
		// Show root categories panel
		CardLayout cl = (CardLayout) (m_jCategories.getLayout());
		cl.show(m_jCategories, "rootcategories");
	}

	private void showPreviousCategoriesPanel() {
		if (this.previousCategory == this.selectedCategory) {
			this.previousCategory = null;
			this.selectedCategory = this.mainCategory;
		} else if (this.previousCategory == mainCategory) {
			this.previousCategory = null;
			this.selectedCategory = mainCategory;
		}

		if (this.previousCategory != null) {
			selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(this.previousCategory.getImage())),
					this.previousCategory.getName());
			// Show subcategories panel
			// selectIndicatorCategories();

			// Show selected root category
			CategoryInfo cat = (CategoryInfo) this.previousCategory;

			if (cat != null) {
				selectCategoryPanel(cat);
			}

			// showingcategory = null;
		} else {
			selectIndicatorCategories();
			showRootCategoriesPanel();
		}
	}

	private void showRootCategoriesPanel() {

		selectIndicatorCategories();
		// Show selected root category
		CategoryInfo cat = (CategoryInfo) this.selectedCategory;

		if (cat != null) {
			selectCategoryPanel(cat);
		}
		// showingcategory = null;
	}

	private void showSubcategoryPanel(CategoryInfo category) {
		// always show root category
		// selectIndicatorPanel(new
		// ImageIcon(tnbbutton.getThumbNail(category.getImage())), category.getName());

		previousCategory = this.selectedCategory;
		selectCategoryPanel(category);
		// showingcategory = category;
	}

	private void showProductPanel(String id) {

		ProductInfoExt product = m_productsset.get(id);

		if (product == null) {
			if (m_productsset.containsKey(id)) {
				// It is an empty panel
				/*
				 * if (showingcategory == null) { showRootCategoriesPanel(); } else {
				 * showSubcategoryPanel(showingcategory); }
				 */
				showRootCategoriesPanel();
			} else {
				try {
					// Create products panel
					java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(id);

					if (products.size() == 0) {
						// no hay productos por tanto lo anado a la de vacios y
						// muestro el panel principal.
						m_productsset.put(id, null);

						/*
						 * if (showingcategory == null) {
						 * 
						 * showRootCategoriesPanel(); } else { showSubcategoryPanel(showingcategory); }
						 */
						showRootCategoriesPanel();
					} else {

						// Load product panel
						product = m_dlSales.getProductInfo(id);
						m_productsset.put(id, product);

						JCatalogTab jcurrTab = new JCatalogTab(m_App);
						jcurrTab.getScrollPane().setVerticalScrollBarPolicy(
								javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
						jcurrTab.applyComponentOrientation(getComponentOrientation());
						m_jProducts.add(jcurrTab, "PRODUCT." + id);

						// Add products
						for (ProductInfoExt prod : products) {
							jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(),
									getProductLabel(prod), productFontSize)), prod.getBgColor(),
									new SelectedAction(prod));
						}

						selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())),
								product.getName());

						CardLayout cl = (CardLayout) (m_jProducts.getLayout());
						cl.show(m_jProducts, "PRODUCT." + id);
					}
				} catch (BasicException eb) {
					eb.printStackTrace();
					m_productsset.put(id, null);

					/*
					 * if (showingcategory == null) { showRootCategoriesPanel(); } else {
					 * showSubcategoryPanel(showingcategory); }
					 */

					showRootCategoriesPanel();
				}
			}
		} else {
			// already exists
			selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

			CardLayout cl = (CardLayout) (m_jProducts.getLayout());
			cl.show(m_jProducts, "PRODUCT." + id);
		}
	}

	private void initComponents() {

		m_jCategories = new javax.swing.JPanel();
		m_jRootCategories = new javax.swing.JPanel();
		m_jscrollcat = new javax.swing.JScrollPane();
		// m_jListCategories = new javax.swing.JList();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		// m_jUp = new javax.swing.JButton();
		// m_jDown = new javax.swing.JButton();
		m_jSubCategories = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		m_lblIndicator = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		m_btnBack1 = new javax.swing.JButton();
		m_jProducts = new javax.swing.JPanel();
		m_jCategoryList = new JPanel();

		setLayout(new java.awt.BorderLayout());

		m_jCategories.setLayout(new java.awt.CardLayout());
		m_jCategoryList.setLayout(new java.awt.CardLayout());

		m_jRootCategories.setLayout(new java.awt.BorderLayout());

		m_jscrollcat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		m_jscrollcat.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		PropertyUtil.ScaleScrollbar(m_App, m_jscrollcat);

		m_jscrollcat.setViewportView(m_jCategoryList);

		m_jRootCategories.add(m_jscrollcat, java.awt.BorderLayout.CENTER);

		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		jPanel3.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

		m_jRootCategories.add(jPanel2, java.awt.BorderLayout.LINE_END);

		m_jCategories.add(m_jRootCategories, "rootcategories");

		m_jSubCategories.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new java.awt.BorderLayout());

		m_lblIndicator.setText("jLabel1");
		jPanel4.add(m_lblIndicator, java.awt.BorderLayout.NORTH);

		m_jSubCategories.add(jPanel4, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new java.awt.BorderLayout());

		jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		jPanel5.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

		m_btnBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/leftarrow.png"))); // NOI18N
		m_btnBack1.setFocusPainted(false);
		m_btnBack1.setFocusable(false);
		m_btnBack1.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_btnBack1.setRequestFocusEnabled(false);
		m_btnBack1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_btnBack1ActionPerformed(evt);
			}
		});
		jPanel5.add(m_btnBack1);

		jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

		m_jSubCategories.add(jPanel1, java.awt.BorderLayout.LINE_END);

		m_jCategories.add(m_jSubCategories, "subcategories");

		add(m_jCategories, java.awt.BorderLayout.LINE_START);

		m_jProducts.setLayout(new java.awt.CardLayout());
		add(m_jProducts, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private class SelectedAction implements ActionListener {
		private ProductInfoExt prod;

		public SelectedAction(ProductInfoExt prod) {
			this.prod = prod;
		}

		public void actionPerformed(ActionEvent e) {
			fireSelectedProduct(prod);
		}
	}

	private class SelectedCategoryMain implements ActionListener {
		private CategoryInfo category;

		public SelectedCategoryMain(CategoryInfo category) {
			this.category = category;
		}

		public void actionPerformed(ActionEvent e) {
			m_jListCategoriesValueChanged(this.category);
		}
	}

	private class SelectedCategory implements ActionListener {
		private CategoryInfo category;

		public SelectedCategory(CategoryInfo category) {
			this.category = category;
		}

		public void actionPerformed(ActionEvent e) {
			showSubcategoryPanel(category);
		}
	}

	private class CategoriesListModel extends AbstractListModel {
		private java.util.List m_aCategories;

		public CategoriesListModel(java.util.List aCategories) {
			m_aCategories = aCategories;
		}

		public int getSize() {
			return m_aCategories.size();
		}

		public Object getElementAt(int i) {
			return m_aCategories.get(i);
		}
	}

	private class SmallCategoryRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
			CategoryInfo cat = (CategoryInfo) value;
			ImageIcon icon = new ImageIcon(tnbcat.getThumbNailText(cat.getImage(), cat.getName(), productFontSize));
			if (icon.getIconWidth() > 50) {
				setIcon(icon);
				setText("");
				setHorizontalAlignment(CENTER);
			} else {
				setText(cat.getName());
				setIcon(new ImageIcon(tnbcat.getThumbNail(cat.getImage())));
			}
			return this;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents

	// private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//
	// GEN-FIRST:event_m_jDownActionPerformed
	//
	// int i = m_jListCategories.getSelectionModel().getMaxSelectionIndex();
	// if (i < 0) {
	// i = 0; // No hay ninguna seleccionada
	// } else {
	// i++;
	// if (i >= m_jListCategories.getModel().getSize()) {
	// i = m_jListCategories.getModel().getSize() - 1;
	// }
	// }
	//
	// if ((i >= 0) && (i < m_jListCategories.getModel().getSize())) {
	// // Solo seleccionamos si podemos.
	// m_jListCategories.getSelectionModel().setSelectionInterval(i, i);
	// }
	//
	// }// GEN-LAST:event_m_jDownActionPerformed

	// private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//
	// GEN-FIRST:event_m_jUpActionPerformed
	//
	// int i = m_jListCategories.getSelectionModel().getMinSelectionIndex();
	// if (i < 0) {
	// i = m_jListCategories.getModel().getSize() - 1; // No hay ninguna
	// // seleccionada
	// } else {
	// i--;
	// if (i < 0) {
	// i = 0;
	// }
	// }
	//
	// if ((i >= 0) && (i < m_jListCategories.getModel().getSize())) {
	// // Solo seleccionamos si podemos.
	// m_jListCategories.getSelectionModel().setSelectionInterval(i, i);
	// }
	//
	// }// GEN-LAST:event_m_jUpActionPerformed

	private void m_jListCategoriesValueChanged(CategoryInfo category/* javax.swing.event.ListSelectionEvent evt */) {// GEN-FIRST:event_m_jListCategoriesValueChanged

		// if (!evt.getValueIsAdjusting()) {
		CategoryInfo cat = (CategoryInfo) category;
		if (cat != null) {
			selectCategoryPanel(cat);
		}

		// store selection for navigation backwards
		this.previousCategory = cat;
		this.mainCategory = cat;
		// }

	}// GEN-LAST:event_m_jListCategoriesValueChanged

	private void m_btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_btnBack1ActionPerformed
		showPreviousCategoriesPanel();
	}// GEN-LAST:event_m_btnBack1ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel m_jCategoryList;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JButton m_btnBack1;
	private javax.swing.JPanel m_jCategories;
	// private javax.swing.JButton m_jDown;
	// private javax.swing.JList m_jListCategories;
	private javax.swing.JPanel m_jProducts;
	private javax.swing.JPanel m_jRootCategories;
	private javax.swing.JPanel m_jSubCategories;
	// private javax.swing.JButton m_jUp;
	private javax.swing.JScrollPane m_jscrollcat;
	private javax.swing.JLabel m_lblIndicator;
	// End of variables declaration//GEN-END:variables

}
