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

import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.jfree.util.Log;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.*;
import com.openbravo.pos.sales.restaurant.PlaceSplit;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author adrianromero
 */
public class CategoriesPanel extends JPanelTable {

	private TableDefinition tcategories;
	private CategoriesEditor jeditor;

	/** Creates a new instance of JPanelCategories */
	public CategoriesPanel() {
	}

	@Override
	public void activate() throws BasicException {
		super.activate();
		PropertyUtil.fillSortOrderIfNeeded(bd, 5);
		ComparatorCreator ccreator = getComparatorCreator();
		Comparator c = ccreator.createComparator(new int[] { 0 });
		bd.sort(c);
	}

	protected void init() {
		DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
		tcategories = dlSales.getTableCategories();
		jeditor = new CategoriesEditor(app, dirty);
	}

	public ListProvider getListProvider() {
		return new ListProviderCreator(tcategories);
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(tcategories);
	}

	public Vectorer getVectorer() {
		return tcategories.getVectorerBasic(new int[] { 1 });
	}

	public ComparatorCreator getComparatorCreator() {
		return tcategories.getComparatorCreator(new int[] { 5, 1 });
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));

		Dictionary<String, String> parents = new Hashtable<String, String>();

		try {
			DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
			List<CategoryInfo> categories = dlSales.getCategoriesList().list();

			for (CategoryInfo category : categories) {
				parents.put(category.getID(), category.getName());
			}
		} catch (BasicException e) {
			// do nothing
			Log.error(e);
		}

		return new ListCellRendererBasic(tcategories.getRenderStringParent(new int[] { 2, 1 }, parents), fontsize);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Categories");
	}

	@Override
	public void ScaleButtons() {

	}

	@Override
	public int getSortColumnIndex() {
		return 5;
	}

	@Override
	public void onMove(BrowsableData browseableData, EditorRecord editorRecord, List<Object[]> values) {

	}

}
