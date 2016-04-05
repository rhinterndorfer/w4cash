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
import java.util.List;

import javax.swing.JButton;
import javax.swing.ListCellRenderer;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.IRenderString;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.EditorListener;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable2;
import com.openbravo.pos.ticket.ProductFilter;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero Created on 1 de marzo de 2007, 22:15
 *
 */
public class ProductsPanel extends JPanelTable2 implements EditorListener {

	private ProductsEditor jeditor;
	private ProductFilter jproductfilter;

	private DataLogicSales m_dlSales = null;

	/** Creates a new instance of ProductsPanel2 */
	public ProductsPanel() {

	}

	protected void init() {
		m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

		// el panel del filtro
		jproductfilter = new ProductFilter(app);
		jproductfilter.init(app);

		row = m_dlSales.getProductsRow();

		lpr = new ListProviderCreator(m_dlSales.getProductCatQBF(), jproductfilter);

		spr = new SaveProvider(m_dlSales.getProductCatUpdate(), m_dlSales.getProductCatInsert(),
				m_dlSales.getProductCatDelete());

		// el panel del editor
		jeditor = new ProductsEditor(app, m_dlSales, dirty);
	}

	@Override
	public ComparatorCreator getComparatorCreator() {
		return null;
	}

	@Override
	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));
		ListCellRendererBasic lcrb = (ListCellRendererBasic) super.getListCellRenderer();
		lcrb.setFontsize(fontsize);
		return lcrb;
		// return new ListCellRendererBasic(tcategories.getRenderStringBasic(new
		// int[] { 1 }), fontsize);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	@Override
	public Component getFilter() {
		return jproductfilter.getComponent();
	}

	@Override
	public Component getToolbarExtras() {

		JButton btnScanPal = new JButton();
		btnScanPal.setText("ScanPal");
		btnScanPal.setVisible(app.getDeviceScanner() != null);
		btnScanPal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnScanPalActionPerformed(evt);
			}
		});

		return btnScanPal;
	}

	private void btnScanPalActionPerformed(java.awt.event.ActionEvent evt) {

		JDlgUploadProducts.showMessage(app, this, app.getDeviceScanner(), bd);
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Products");
	}

	@Override
	public void activate() throws BasicException {

		jeditor.activate();
		jproductfilter.activate();

		super.activate();
	}

	public void updateValue(Object value) {
	}

	@Override
	public void ScaleButtons() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getSortColumnIndex() {
		return -1;
	}

	@Override
	public void onMove(BrowsableData browseableData, EditorRecord editorRecord, List<Object[]> values) {
		int moveColumnIndex = 16;

		Object value1 = values.get(0)[moveColumnIndex];
		Object value2 = values.get(1)[moveColumnIndex];

		if (value1 != null && value2 != null) {
			values.get(0)[moveColumnIndex] = values.get(1)[moveColumnIndex];
			values.get(1)[moveColumnIndex] = value1;

			for (Object[] change : values) {
				int index1 = browseableData.findElementIndex(change);
				try {
					browseableData.updateRecord(index1, change);
					editorRecord.refresh();
				} catch (BasicException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isMoveAllowed(List<Object[]> values) {
		// move only in same category
		Object lastValue = null;
		for (Object[] value : values) {
			Object newValue = value[8];
			if (lastValue == null) {
				lastValue = newValue;
				continue;
			}

			if (!lastValue.equals(newValue)) {
				JConfirmDialog.showError(this.app, this, AppLocal.getIntString("error.information"),
						AppLocal.getIntString("message.productcategory"));
				return false;
			}
		}

		// move only when sortorder property is set
		for (Object[] value : values) {
			Object newValue = value[16];
			if (newValue == null) {
				JConfirmDialog.showError(this.app, this, AppLocal.getIntString("error.information"),
						AppLocal.getIntString("message.sortorder"));
				return false;
			}
		}

		return true;
	}

}
