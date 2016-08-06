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

package com.openbravo.pos.mant;

import java.util.Comparator;
import java.util.List;

import javax.swing.ListCellRenderer;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.panels.*;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class JPanelFloors extends JPanelTable {

	private TableDefinition tfloors;
	private FloorsEditor jeditor;

	/** Creates a new instance of JPanelFloors */
	public JPanelFloors() {
	}

	@Override
	public void activate() throws BasicException {
		super.activate();
		PropertyUtil.fillSortOrderIfNeeded(bd, 3);
		ComparatorCreator ccreator = getComparatorCreator();
		Comparator c = ccreator.createComparator(new int[] { 1 });
		bd.sort(c);
	}

	protected void init() {
		tfloors = new TableDefinition(app.getSession(), "FLOORS", new String[] { "ID", "NAME", "IMAGE", "SORTORDER" },
				new String[] { "ID", AppLocal.getIntString("Label.Name"), "IMAGE", "SORTORDER" },
				new Datas[] { Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.INT },
				new Formats[] { Formats.NULL, Formats.STRING }, new int[] { 0 });
		jeditor = new FloorsEditor(app, dirty, app.getSession());
	}

	public ListProvider getListProvider() {
		return new ListProviderCreator(tfloors);
	}

	public Vectorer getVectorer() {
		return tfloors.getVectorerBasic(new int[] { 1 });
	}

	@Override
	public ComparatorCreator getComparatorCreator() {
		return tfloors.getComparatorCreator(new int[] { 3, 1 });
	}

	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));
		return new ListCellRendererBasic(tfloors.getRenderStringBasic(new int[] { 1 }), fontsize);
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(tfloors);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Floors");
	}

	@Override
	public void ScaleButtons() {

	}

	@Override
	public int getSortColumnIndex() {
		return 3;
	}

	@Override
	public void onMove(BrowsableData browseableData, EditorRecord editorRecord, List<Object[]> values) {
		
	}
	
	
	
	
}
