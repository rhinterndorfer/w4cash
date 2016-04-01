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

package com.openbravo.pos.catalog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.openbravo.beans.JFlowPanel;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class JCatalogTab extends javax.swing.JPanel {

	private JFlowPanel flowpanel;
	private JScrollPane scroll;

	/** Creates new form JCategoryProducts */
	public JCatalogTab(AppView app) {
		initComponents();

		flowpanel = new JFlowPanel();
		this.scroll = new JScrollPane(flowpanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		PropertyUtil.ScaleScrollbar(app, scroll);

		add(scroll, BorderLayout.CENTER);
	}

	protected JScrollPane getScrollPane() {
		return this.scroll;
	}

	public void setEnabled(boolean value) {
		flowpanel.setEnabled(value);
		super.setEnabled(value);
	}

	public void addButton(Icon ico, Color color, ActionListener al) {
		JButton btn = new JButton();
		btn.applyComponentOrientation(getComponentOrientation());
		btn.setIcon(ico);
		btn.setFocusPainted(false);
		btn.setFocusable(false);
		btn.setRequestFocusEnabled(false);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setMargin(new Insets(2, 2, 2, 2));
		btn.addActionListener(al);
		btn.setBackground(color);
		flowpanel.add(btn);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setLayout(new java.awt.BorderLayout());
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

}
