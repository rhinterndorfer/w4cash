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

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.util.WrapLayout;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JSaver;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.JCounter;
import com.openbravo.data.gui.JLabelDirty;
import com.openbravo.data.gui.JListNavigator;
import com.openbravo.data.gui.JNavigator;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.BrowseListener;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.Finder;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelTable extends JPanel implements JPanelView, BeanFactoryApp {

	protected BrowsableEditableData bd;
	protected DirtyManager dirty;
	protected AppView app;

	
	
	/** Creates new form JPanelTableEditor */
	public JPanelTable() {

		initComponents();
	}

	public void init(AppView app) throws BeanFactoryException {

		this.app = app;
		dirty = new DirtyManager();
		bd = null;

		init();
	}

	public DirtyManager GetDirtyManager() {
		return dirty;
	}
	
	public Object getBean() {
		return this;
	}

	protected void startNavigation() {

		if (bd == null) {

			// init browsable editable data
			Comparator comp = null;
			if(getComparatorCreator() != null)
				comp = getComparatorCreator().createComparator(new int[] {0});
			
			bd = new BrowsableEditableData(getListProvider(), getSaveProvider(), comp, getEditor(), dirty);
			bd.addBrowseListener(new BrowseListener() {
				@Override
				public void updateIndex(int iIndex, int iCounter) {
					changeEntry(bd.getCurrentElement());
				}
			});
			
			// Add the filter panel
			Component c = getFilter();
			if (c != null) {
				c.applyComponentOrientation(getComponentOrientation());
				add(c, BorderLayout.NORTH);
			}

			// Add the editor
			c = getEditor().getComponent();
			if (c != null) {
				c.applyComponentOrientation(getComponentOrientation());
				container.add(c, BorderLayout.CENTER);
			}

			// el panel este
			ListCellRenderer cr = getListCellRenderer();
			if (cr != null) {
				JListNavigator nl = new JListNavigator(app, bd);
				nl.applyComponentOrientation(getComponentOrientation());
				if (cr != null)
					nl.setCellRenderer(cr);
				container.add(nl, java.awt.BorderLayout.LINE_START);
			}

			// add toolbar extras
			c = getToolbarExtras();
			if (c != null) {
				c.applyComponentOrientation(getComponentOrientation());
				toolbar.add(c);
			}

			// La Toolbar
			c = new JLabelDirty(dirty);
			c.applyComponentOrientation(getComponentOrientation());
			toolbar.add(c);
			
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			
			if(d.getWidth() >= 900)
			{
				c = new JCounter(bd);
				c.applyComponentOrientation(getComponentOrientation());
				toolbar.add(c);
				
				c = new JNavigator(app, bd, getVectorer(), getComparatorCreator(), JNavigator.BUTTONS_ALL, getSortColumnIndex(), this);
				c.applyComponentOrientation(getComponentOrientation());
				toolbar.add(c);
			}
			else
			{
				c = new JNavigator(app, bd, getVectorer(), getComparatorCreator(), JNavigator.BUTTONS_NONAVIGATE, getSortColumnIndex(), this);
				c.applyComponentOrientation(getComponentOrientation());
				toolbar.add(c);
			}
			
			c = new JSaver(this.app, bd);
			c.applyComponentOrientation(getComponentOrientation());
			toolbar.add(c);
			
			
			
		}
	}

	public Finder getInitialFinder() {
		return null;
	}
	
	public Component getToolbarExtras() {
		return null;
	}

	public Component getFilter() {
		return null;
	}

	public void changeEntry(Object element) {
	}
	
	protected abstract void init();

	public abstract EditorRecord getEditor();

	public abstract ListProvider getListProvider();

	public abstract SaveProvider getSaveProvider();

	public Vectorer getVectorer() {
		return null;
	}

	public ComparatorCreator getComparatorCreator() {
		return null;
	}

	public ListCellRenderer getListCellRenderer() {
		return null;
	}

	public JComponent getComponent() {
		return this;
	}

	public void activate() throws BasicException {
		startNavigation();
		bd.actionLoad();
		
		// initial sort by first column in sort index
		if (getComparatorCreator() != null) {
			try {
				bd.sort(getComparatorCreator().createComparator(new int[] { 0 }));
			} catch (BasicException e) {
				e.printStackTrace();
			}
		}
		
		Finder initFinder = getInitialFinder();
		if(initFinder != null) {
			int index = bd.findNext(initFinder);
			if (index >= 0) {
				bd.moveTo(index);
			} else {
				bd.moveLast();	
			}
		}
		else {
			bd.moveLast();
		}
		
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return null;
		} else if (parent instanceof Frame || parent instanceof JDialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}
	
	public boolean deactivate() {

		try {
			return bd.actionClosingForm(this);
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.CannotMove"), eD);
			msg.show(app, this);
			return false;
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

		container = new javax.swing.JPanel();
		toolbar = new javax.swing.JPanel();
		toolbar.setLayout(new WrapLayout());

		setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setLayout(new java.awt.BorderLayout());

		container.setLayout(new java.awt.BorderLayout());
		container.add(toolbar, java.awt.BorderLayout.NORTH);

		add(container, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel container;
	private javax.swing.JPanel toolbar;
	// End of variables declaration//GEN-END:variables

	public abstract void ScaleButtons();

	/** SORTORDER column index */
	public abstract int getSortColumnIndex();
	
	/** When moving items in a panel, swap database columns between items 
	 * @param m_editorrecord 
	 * @param m_bd */
//	public abstract int getMoveColumnIndex();
	public abstract void onMove(BrowsableData browseableData, EditorRecord editorRecord, List<Object[]> values);
	
	public boolean isMoveAllowed(List<Object[]> values){
		return true;
	}
}
