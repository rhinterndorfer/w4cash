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

import java.util.List;

import javax.swing.ListCellRenderer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.*;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class JPanelPlaces extends JPanelTable {

	private TableDefinition tplaces;
	private PlacesEditor jeditor;

	/** Creates a new instance of JPanelPlaces */
	public JPanelPlaces() {
	}

	protected void init() {
		DataLogicSales dlSales = null;
		dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

		TableDefinition td = new TableDefinition(app.getSession(), "PLACES",
				new String[] { "ID", "NAME", "X", "Y", "FLOOR", "WIDTH","HEIGHT", "FONTSIZE", "FONTCOLOR" },
				new String[] { "ID", AppLocal.getIntString("Label.Name"), "X", "Y",
						AppLocal.getIntString("label.placefloor"), "WIDTH", "HEIGHT" , "FONTSIZE", "FONTCOLOR"},
				new Datas[] { Datas.STRING, Datas.STRING, Datas.INT, Datas.INT, Datas.STRING , Datas.INT, Datas.INT, Datas.INT, Datas.STRING},
				new Formats[] { Formats.STRING, Formats.STRING, Formats.INT, Formats.INT,Formats.NULL , Formats.INT, Formats.INT, Formats.INT, Formats.STRING},
				new int[] { 0 });

		jeditor = new PlacesEditor(app, dlSales, this, dirty);

		setTableDefinition(td);
	}

	protected void setTableDefinition(TableDefinition definition) {
		tplaces = definition;
	}

	public ListProvider getListProvider() {
		return new ListProviderCreator(tplaces);
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(tplaces);
	}

	@Override
	public Vectorer getVectorer() {
		return tplaces.getVectorerBasic(new int[] { 1 });
	}

	@Override
	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));
		return new ListCellRendererBasic(tplaces.getRenderStringBasic(new int[] { 1 }), fontsize);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Tables");
	}

	@Override
	public void activate() throws BasicException {
		jeditor.activate(); // primero activo el editor
		super.activate(); // segundo activo el padre
		jeditor.activateFill();
	}

	public BrowsableEditableData getBrowseableData() {
		return this.bd;
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
