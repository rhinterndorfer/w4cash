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

package com.openbravo.data.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.user.BrowsableData;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.BrowseListener;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

public class JListNavigator extends javax.swing.JPanel implements BrowseListener, ListSelectionListener {

	protected BrowsableEditableData m_bd;
	private AppView m_App;

	/** Creates new form JListBrowse */
	public JListNavigator(AppView app, BrowsableEditableData bd) {
		this(app, bd, false);
	}

	public JListNavigator(AppView app, BrowsableEditableData bd, boolean bTouchable) {
		m_App = app;
		m_bd = bd;

		initComponents();

		if (bTouchable) {
			PropertyUtil.ScaleScrollbar(m_App, jScrollPane1);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}

		m_jlist.addListSelectionListener(this);
		m_jlist.setModel(m_bd.getListModel());

		m_bd.addBrowseListener(this);
	}

	public void setCellRenderer(ListCellRenderer cellRenderer) {
		m_jlist.setCellRenderer(cellRenderer);
	}

	public void updateIndex(int iIndex, int iCounter) {

		if (iIndex >= 0 && iIndex < iCounter) {
			m_jlist.setSelectedIndex(iIndex);
		} else {
			m_jlist.setSelectedIndex(-1);
		}
	}

	public void valueChanged(ListSelectionEvent evt) {

		if (!evt.getValueIsAdjusting()) {
			int i = m_jlist.getSelectedIndex();
			if (i >= 0) {
				if (!m_bd.isAdjusting()) {

					try {
						m_bd.moveTo(i);
					} catch (BasicException eD) {
						MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nomove"),
								eD);
						msg.show(m_App, this);
						// Y ahora tendriamos que seleccionar silenciosamente el
						// registro donde estabamos.
					}
				}
				// Lo hago visible...
				Rectangle oRect = m_jlist.getCellBounds(i, i);
				m_jlist.scrollRectToVisible(oRect);
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		m_jlist = new javax.swing.JList();

		setLayout(new java.awt.BorderLayout());

		setPreferredSize(new java.awt.Dimension(200, 2));
		m_jlist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		m_jlist.setFocusable(false);
		m_jlist.setRequestFocusEnabled(false);
		jScrollPane1.setViewportView(m_jlist);

		add(jScrollPane1, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JList m_jlist;
	// End of variables declaration//GEN-END:variables

}
