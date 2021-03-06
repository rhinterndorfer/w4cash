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

package com.openbravo.pos.admin;

import java.util.List;

import javax.swing.ListCellRenderer;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.panels.*;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.*;

/**
 *
 * @author adrianromero
 */
public class ResourcesPanel extends JPanelTable {

	private TableDefinition tresources;
	private ResourcesView jeditor;

	/** Creates a new instance of JPanelResources */
	public ResourcesPanel() {

	}

	protected void init() {
		DataLogicAdmin dlAdmin = (DataLogicAdmin) app.getBean("com.openbravo.pos.admin.DataLogicAdmin");
		tresources = dlAdmin.getTableResources();
		jeditor = new ResourcesView(app, dirty);
	}

	@Override
	public boolean deactivate() {
		if (super.deactivate()) {
			DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
			dlSystem.resetResourcesCache();
			return true;
		} else {
			return false;
		}
	}

	public ListProvider getListProvider() {
		return new ListProviderCreator(tresources);
	}

	public SaveProvider getSaveProvider() {
		return new SaveProvider(tresources);
	}

	@Override
	public Vectorer getVectorer() {
		return tresources.getVectorerBasic(new int[] { 1 });
	}

	@Override
	public ComparatorCreator getComparatorCreator() {
		return tresources.getComparatorCreator(new int[] { 1, 2 });
	}

	@Override
	public ListCellRenderer getListCellRenderer() {
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));
		return new ListCellRendererBasic(tresources.getRenderStringBasic(new int[] { 1 }), fontsize);
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.Resources");
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
