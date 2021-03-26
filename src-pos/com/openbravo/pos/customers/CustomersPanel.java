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

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.BrowseListener;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.Finder;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JPanelTable;
import com.openbravo.pos.util.PropertyUtil;

import java.util.List;

import javax.swing.ListCellRenderer;

/**
 *
 * @author adrianromero
 */
public class CustomersPanel extends JPanelTable {

	private static final long serialVersionUID = -6252967383983749840L;
	private TableDefinition tcustomers;
	private CustomersView jeditor;
	private String currentCustomerSearchName;
	private String initCustomerSearchName;

	/** Creates a new instance of CustomersPanel */
	public CustomersPanel() {
	}

	protected void init() {
		DataLogicCustomers dlCustomers = (DataLogicCustomers) app
				.getBean("com.openbravo.pos.customers.DataLogicCustomers");
		tcustomers = dlCustomers.getTableCustomers();
		jeditor = new CustomersView(app, dirty);
	}

	@Override
	public void activate() throws BasicException {

		jeditor.activate();
		super.activate();
	}

	@Override
	public void changeEntry(Object element) {
		if(element != null)
		{
			Object[] customer = (Object[])element;
			currentCustomerSearchName = customer[2].toString();
		}
	}
	
	public String getCurrentCustomerSearchName() {
		return currentCustomerSearchName;
	}
	
	public void setInitCustomerSearchName(String searchName) {
		initCustomerSearchName = searchName;
	}
	
	public ListProvider getListProvider() {
		return new ListProviderCreator(tcustomers);
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(tcustomers,
				new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 });
	}

	@Override
	public Finder getInitialFinder() {
		return new Finder() {
			
			@Override
			public boolean match(Object obj) throws BasicException {
				Object[] customer = (Object[])obj;
				String customerSearchName = customer[2].toString();
				
				if(customerSearchName != null && customerSearchName.equals(initCustomerSearchName))
				{
					return true;
				}
				else {
					return false;	
				}
				
			}
		};
	}
	
	@Override
	public Vectorer getVectorer() {
		return tcustomers.getVectorerBasic(new int[] { 2, 3, 1 });
	}

	@Override
	public ComparatorCreator getComparatorCreator() {
		return tcustomers.getComparatorCreator(new int[] { 2, 3, 1 });
	}

	@Override
	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));
		return new ListCellRendererBasic(tcustomers.getRenderStringBasic(new int[] { 2 }), fontsize);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.CustomersManagement");
	}

	@Override
	public void ScaleButtons() {
	}

	@Override
	public int getSortColumnIndex() {
		return -1;
	}

	@Override
	public void onMove(BrowsableData browseableData, EditorRecord editorRecord, List<Object[]> values) {

	}
}
