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

import com.openbravo.pos.ticket.ProductFilterSales;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.ProductRenderer;
import com.openbravo.pos.util.PropertyUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurant;
import com.openbravo.pos.scale.Scale;

/**
 *
 * @author adrianromero
 */
public class JProductFinder extends javax.swing.JDialog {

	private ProductInfoExt m_ReturnProduct;
	private ListProvider lpr;
	private AppView m_App;

	public final static int PRODUCT_ALL = 0;
	public final static int PRODUCT_NORMAL = 1;
	public final static int PRODUCT_AUXILIAR = 2;

	/** Creates new form JProductFinder */
	private JProductFinder(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JProductFinder */
	private JProductFinder(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	private ProductInfoExt init(AppView app, DataLogicSales dlSales, int productsType) throws BasicException {
		this.m_App = app;
		initComponents();

		PropertyUtil.ScaleScrollbar(m_App, jScrollPane1);

		ProductFilterSales jproductfilter = new ProductFilterSales(app, dlSales);
		jproductfilter.activate();
		m_jProductSelect.add(jproductfilter, BorderLayout.CENTER);
		jproductfilter.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonSearch.doClick();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		switch (productsType) {
		case PRODUCT_NORMAL:
			lpr = new ListProviderCreator(dlSales.getProductListNormal(), jproductfilter);
			break;
		case PRODUCT_AUXILIAR:
			lpr = new ListProviderCreator(dlSales.getProductListAuxiliar(), jproductfilter);
			break;
		default: // PRODUCT_ALL
			lpr = new ListProviderCreator(dlSales.getProductList(), jproductfilter);
			break;

		}

		TaxesLogic taxeslogic = new TaxesLogic(dlSales.getTaxList().list());
		jListProducts.setCellRenderer(new ProductRenderer(taxeslogic));
		
		//getRootPane().setDefaultButton(jcmdOK);

		m_ReturnProduct = null;

		// m_jKeys.ScaleButtons();

		ScaleButtons();

		// show();
		setVisible(true);

		return m_ReturnProduct;
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		} else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

	public static ProductInfoExt showMessage(AppView app, Component parent, DataLogicSales dlSales)
			throws BasicException {
		return showMessage(app, parent, dlSales, PRODUCT_ALL);
	}

	public static ProductInfoExt showMessage(AppView app, Component parent, DataLogicSales dlSales, int productsType)
			throws BasicException {

		Window window = getWindow(parent);

		JProductFinder myMsg;
		if (window instanceof Frame) {
			myMsg = new JProductFinder((Frame) window, true);
		} else {
			myMsg = new JProductFinder((Dialog) window, true);
		}
		return myMsg.init(app, dlSales, productsType);
	}

	private static class MyListData extends javax.swing.AbstractListModel {

		private java.util.List m_data;

		public MyListData(java.util.List data) {
			m_data = data;
		}

		public Object getElementAt(int index) {
			return m_data.get(index);
		}

		public int getSize() {
			return m_data.size();
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

		jPanel4 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		m_jProductSelect = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jButtonSearch = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jListProducts = new javax.swing.JList();
		jPanel1 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("form.productslist")); // NOI18N

		jPanel4.setLayout(new java.awt.BorderLayout());

		getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

		jPanel2.setLayout(new java.awt.BorderLayout());

		m_jProductSelect.setLayout(new java.awt.BorderLayout());

		jButtonSearch
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launchfilter.png"))); // NOI18N
		jButtonSearch.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
		jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});
		jPanel3.add(jButtonSearch);

		m_jProductSelect.add(jPanel3, java.awt.BorderLayout.SOUTH);

		jPanel2.add(m_jProductSelect, java.awt.BorderLayout.NORTH);

		jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		jPanel5.setLayout(new java.awt.BorderLayout());

		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		jListProducts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jListProducts.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				jListProductsValueChanged(evt);
			}
		});
		jListProducts.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jListProductsMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(jListProducts);

		jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

		jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		jcmdOK.setEnabled(false);
		jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdOKActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdOK);

		jcmdCancel.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdCancelActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdCancel);

		jPanel2.add(jPanel1, java.awt.BorderLayout.SOUTH);

		getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

		int width = 1024;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		if (d.getWidth() < 1024) {
			width = (int) d.getWidth();
		}
		PropertyUtil.ScaleDialog(m_App, this, width, 768);
	}// </editor-fold>//GEN-END:initComponents

	public void ScaleButtons() {
		int width = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "48"));
		int height = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "48"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jButtonSearch, width, height, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdOK, width, height, fontsize);
		PropertyUtil.ScaleButtonIcon(jcmdCancel, width, height, fontsize);
		
		Font font = jListProducts.getFont();
		jListProducts.setFont(new Font(font.getFontName(), font.getStyle(), fontsize ));
	}

	private void jListProductsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListProductsMouseClicked

		if (evt.getClickCount() == 2) {
			m_ReturnProduct = (ProductInfoExt) jListProducts.getSelectedValue();
			dispose();
		}

	}// GEN-LAST:event_jListProductsMouseClicked

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdOKActionPerformed

		m_ReturnProduct = (ProductInfoExt) jListProducts.getSelectedValue();
		dispose();

	}// GEN-LAST:event_jcmdOKActionPerformed

	private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdCancelActionPerformed

		dispose();

	}// GEN-LAST:event_jcmdCancelActionPerformed

	private void jListProductsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListProductsValueChanged

		jcmdOK.setEnabled(jListProducts.getSelectedValue() != null);

	}// GEN-LAST:event_jListProductsValueChanged

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed

		try {
			jListProducts.setModel(new MyListData(lpr.loadData()));
			if (jListProducts.getModel().getSize() > 0) {
				jListProducts.setSelectedIndex(0);
			}
		} catch (BasicException e1) {
			JConfirmDialog.showError(m_App, JProductFinder.this, AppLocal.getIntString("error.network"),
					AppLocal.getIntString("message.databaseconnectionerror"), e1);
		}

	}// GEN-LAST:event_jButton3ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonSearch;
	private javax.swing.JList jListProducts;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton jcmdCancel;
	private javax.swing.JButton jcmdOK;
	// private com.openbravo.editor.JEditorKeys m_jKeys;
	private javax.swing.JPanel m_jProductSelect;
	// End of variables declaration//GEN-END:variables

}
