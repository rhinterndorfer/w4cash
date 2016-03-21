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

import java.util.List;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author adrianromero
 */
public class JPanelPayments extends JPanelTable {

	private PaymentsEditor jeditor;
	private DataLogicSales m_dlSales = null;
	private TableDefinition td;

	/** Creates a new instance of JPanelPayments */
	public JPanelPayments() {

	}

	protected void init() {
		m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
		jeditor = new PaymentsEditor(app, dirty);
	}

	public ListProvider getListProvider() {
		try {
			return new PaymentListProvider(m_dlSales.getPaymentList(app));
		} catch (BasicException e) {
			return null;
		}
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(null, m_dlSales.getPaymentMovementInsert(), m_dlSales.getPaymentMovementDelete());
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Payments");
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
		// TODO Auto-generated method stub
		
	}
	
	class PaymentListProvider extends ListProviderCreator {

		public PaymentListProvider(SentenceList sent) {
			super(sent);
		}

		@Override
		public List refreshData() throws BasicException {
			setSQL(m_dlSales.getPaymentList(app));
			return super.refreshData();
		}

	}

	
}
