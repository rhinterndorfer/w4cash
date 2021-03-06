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

import java.util.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.BrowseListener;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.StateListener;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.panels.JPanelTable;
import com.openbravo.pos.util.PropertyUtil;

public class JNavigator extends javax.swing.JPanel implements BrowseListener, StateListener {
	public final static int BUTTONS_ALL = 0;
	public final static int BUTTONS_NONAVIGATE = 1;

	protected BrowsableEditableData m_bd;
	protected ComparatorCreator m_cc;

	protected FindInfo m_LastFindInfo;

	private javax.swing.JButton jbtnFind = null;
	private javax.swing.JButton jbtnSort = null;
	private javax.swing.JButton jbtnFirst = null;
	private javax.swing.JButton jbtnLast = null;
	private javax.swing.JButton jbtnNext = null;
	private javax.swing.JButton jbtnPrev = null;
	private javax.swing.JButton jbtnMoveUp = null;
	private javax.swing.JButton jbtnMoveDown = null;
	private javax.swing.JButton jbtnReload = null;
	private AppView m_App;
	private int columnSortIndex = -1;
	private JPanelTable move = null;

	/** Creates new form JNavigator */
	public JNavigator(AppView app, BrowsableEditableData bd, Vectorer vec, ComparatorCreator cc, int iButtons,
			int columnSortIndex, JPanelTable move) {
		this.m_App = app;
		this.columnSortIndex = columnSortIndex;
		this.move  = move;
		
		initComponents();

		if (iButtons == BUTTONS_ALL) {
			jbtnFirst = new javax.swing.JButton();
			jbtnFirst.setIcon(
					new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/22leftarrow.png")));
			jbtnFirst.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnFirst.setFocusPainted(false);
			jbtnFirst.setFocusable(false);
			jbtnFirst.setRequestFocusEnabled(false);
			jbtnFirst.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnFirstActionPerformed(evt);
				}
			});
			add(jbtnFirst);
		}

		if (iButtons == BUTTONS_ALL) {
			jbtnPrev = new javax.swing.JButton();
			jbtnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/leftarrow.png")));
			jbtnPrev.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnPrev.setFocusPainted(false);
			jbtnPrev.setFocusable(false);
			jbtnPrev.setRequestFocusEnabled(false);
			jbtnPrev.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnPrevActionPerformed(evt);
				}
			});
			add(jbtnPrev);
		}
		//if (iButtons == BUTTONS_ALL) {
			jbtnMoveUp = new javax.swing.JButton();
			jbtnMoveUp
					.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/22uparrow.png")));
			jbtnMoveUp.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnMoveUp.setFocusPainted(false);
			jbtnMoveUp.setFocusable(false);
			jbtnMoveUp.setRequestFocusEnabled(false);
			jbtnMoveUp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnMoveUpActionPerformed(evt);
				}
			});
			add(jbtnMoveUp);
		//}

		
		//if (iButtons == BUTTONS_ALL) {
			jbtnMoveDown = new javax.swing.JButton();
			jbtnMoveDown.setIcon(
					new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/22downarrow.png")));
			jbtnMoveDown.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnMoveDown.setFocusPainted(false);
			jbtnMoveDown.setFocusable(false);
			jbtnMoveDown.setRequestFocusEnabled(false);
			jbtnMoveDown.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnMoveDownActionPerformed(evt);
				}
			});
			add(jbtnMoveDown);
		//}

		if (iButtons == BUTTONS_ALL) {
			jbtnNext = new javax.swing.JButton();
			jbtnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/rightarrow.png")));
			jbtnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnNext.setFocusPainted(false);
			jbtnNext.setFocusable(false);
			jbtnNext.setRequestFocusEnabled(false);
			jbtnNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnNextActionPerformed(evt);
				}
			});
			add(jbtnNext);
		}

		if (iButtons == BUTTONS_ALL) {
			jbtnLast = new javax.swing.JButton();
			jbtnLast.setIcon(
					new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/22rightarrow.png")));
			jbtnLast.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnLast.setFocusPainted(false);
			jbtnLast.setFocusable(false);
			jbtnLast.setRequestFocusEnabled(false);
			jbtnLast.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnLastActionPerformed(evt);
				}
			});
			add(jbtnLast);
		}

		add(new javax.swing.JSeparator());

		if (bd.canLoadData()) {
			jbtnReload = new javax.swing.JButton();
			jbtnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/refresh.png")));
			jbtnReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnReload.setFocusPainted(false);
			jbtnReload.setFocusable(false);
			jbtnReload.setRequestFocusEnabled(false);
			jbtnReload.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnReloadActionPerformed(evt);
				}
			});
			add(jbtnReload);

			add(new javax.swing.JSeparator());
		}

		if (vec == null) {
			m_LastFindInfo = null;
		} else {
			m_LastFindInfo = new FindInfo(vec);
			jbtnFind = new javax.swing.JButton();
			jbtnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png")));
			jbtnFind.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnFind.setFocusPainted(false);
			jbtnFind.setFocusable(false);
			jbtnFind.setRequestFocusEnabled(false);
			jbtnFind.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnFindActionPerformed(evt);
				}
			});
			add(jbtnFind);
		}

		m_cc = cc;
		if (m_cc != null) {
			jbtnSort = new javax.swing.JButton();
			jbtnSort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/sort_incr.png")));
			jbtnSort.setMargin(new java.awt.Insets(2, 2, 2, 2));
			jbtnSort.setFocusPainted(false);
			jbtnSort.setFocusable(false);
			jbtnSort.setRequestFocusEnabled(false);
			jbtnSort.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jbtnSortActionPerformed(evt);
				}
			});
			add(jbtnSort);
		}

		m_bd = bd;
		bd.addBrowseListener(this);
		bd.addStateListener(this);

		ScaleButtons();
	}

	public JNavigator(AppView app, BrowsableEditableData bd, int columnSortIndex, JPanelTable panel) {
		this(app, bd, null, null, BUTTONS_ALL, columnSortIndex, panel);
	}

	public JNavigator(AppView app, BrowsableEditableData bd, Vectorer vec, ComparatorCreator cc, int columnSortIndex,
			JPanelTable panel) {
		this(app, bd, vec, cc, BUTTONS_ALL, columnSortIndex, panel);
	}

	public void updateState(int iState) {
		if (iState == BrowsableEditableData.ST_INSERT || iState == BrowsableEditableData.ST_DELETE) {
			// Insert o Delete
			if (jbtnFirst != null)
				jbtnFirst.setEnabled(false);
			if (jbtnPrev != null)
				jbtnPrev.setEnabled(false);
			if (jbtnNext != null)
				jbtnNext.setEnabled(false);
			if (jbtnLast != null)
				jbtnLast.setEnabled(false);
		}
	}

	public void updateIndex(int iIndex, int iCounter) {

		if (iIndex >= 0 && iIndex < iCounter) {
			// Reposicionamiento
			if (jbtnFirst != null)
				jbtnFirst.setEnabled(iIndex > 0);
			if (jbtnPrev != null)
				jbtnPrev.setEnabled(iIndex > 0);
			if (jbtnNext != null)
				jbtnNext.setEnabled(iIndex < iCounter - 1);
			if (jbtnLast != null)
				jbtnLast.setEnabled(iIndex < iCounter - 1);
			if (jbtnMoveUp != null)
				jbtnMoveUp.setEnabled(iIndex > 0);
			if (jbtnMoveDown != null)
				jbtnMoveDown.setEnabled(iIndex < iCounter - 1);
		} else {
			// EOF
			if (jbtnFirst != null)
				jbtnFirst.setEnabled(false);
			if (jbtnPrev != null)
				jbtnPrev.setEnabled(false);
			if (jbtnNext != null)
				jbtnNext.setEnabled(false);
			if (jbtnLast != null)
				jbtnLast.setEnabled(false);
			if (jbtnMoveUp != null)
				jbtnMoveUp.setEnabled(false);
			if (jbtnMoveDown != null)
				jbtnMoveDown.setEnabled(false);
		}
	}

	private void jbtnSortActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			Comparator c = JSort.showMessage(m_App, this, m_cc);
			if (c != null) {
				m_bd.sort(c);
			}
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nolistdata"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnFindActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			FindInfo newFindInfo = JFind.showMessage(m_App, this, m_LastFindInfo);
			if (newFindInfo != null) {
				m_LastFindInfo = newFindInfo;

				int index = m_bd.findNext(newFindInfo);
				if (index < 0) {
					MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.norecord"));
					msg.show(m_App, this);
				} else {
					m_bd.moveTo(index);
				}
			}
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nolistdata"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnMoveUpActionPerformed(java.awt.event.ActionEvent evt) {

		m_bd.actionMoveUpCurrent(this.columnSortIndex,this.move, this);
	}

	private void jbtnMoveDownActionPerformed(java.awt.event.ActionEvent evt) {

		m_bd.actionMoveDownCurrent(this.columnSortIndex,this.move, this);
	}

	private void jbtnReloadActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			m_bd.actionLoad();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.noreload"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnLastActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			m_bd.moveLast();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nomove"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnFirstActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			m_bd.moveFirst();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nomove"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnPrevActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			m_bd.movePrev();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nomove"), eD);
			msg.show(m_App, this);
		}
	}

	private void jbtnNextActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			m_bd.moveNext();
		} catch (BasicException eD) {
			MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nomove"), eD);
			msg.show(m_App, this);
		}
	}

	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {

	}

	private void ScaleButtons() {
		int menuwidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "menubar-img-width", "16"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "menubar-img-height", "16"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		if (jbtnFind != null)
			PropertyUtil.ScaleButtonIcon(jbtnFind, menuwidth, menuheight, fontsize);
		if (jbtnSort != null)
			PropertyUtil.ScaleButtonIcon(jbtnSort, menuwidth, menuheight, fontsize);
		if (jbtnFirst != null)
			PropertyUtil.ScaleButtonIcon(jbtnFirst, menuwidth, menuheight, fontsize);
		if (jbtnLast != null)
			PropertyUtil.ScaleButtonIcon(jbtnLast, menuwidth, menuheight, fontsize);
		if (jbtnNext != null)
			PropertyUtil.ScaleButtonIcon(jbtnNext, menuwidth, menuheight, fontsize);
		if (jbtnPrev != null)
			PropertyUtil.ScaleButtonIcon(jbtnPrev, menuwidth, menuheight, fontsize);
		if (jbtnReload != null)
			PropertyUtil.ScaleButtonIcon(jbtnReload, menuwidth, menuheight, fontsize);
		if (jbtnMoveUp != null)
			PropertyUtil.ScaleButtonIcon(jbtnMoveUp, menuwidth, menuheight, fontsize);
		if (jbtnMoveDown != null)
			PropertyUtil.ScaleButtonIcon(jbtnMoveDown, menuwidth, menuheight, fontsize);

	}

	// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

}
