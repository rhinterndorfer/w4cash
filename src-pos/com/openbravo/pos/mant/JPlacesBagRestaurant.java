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

import javax.swing.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;

public class JPlacesBagRestaurant extends javax.swing.JPanel {

	private AppView m_App;
	private JPlacesBagRestaurantMap m_restaurant;

	/** Creates new form JTicketsBagRestaurantMap */
	public JPlacesBagRestaurant(AppView app, JPlacesBagRestaurantMap restaurant) {

		m_App = app;
		m_restaurant = restaurant;

		initComponents();
	}

	public void activate() {

		// Authorization
		m_jDelTicket.setEnabled(
				m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		m_jDelTicket = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();

		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		m_jDelTicket
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/list-remove.png"))); // NOI18N
		m_jDelTicket.setFocusPainted(false);
		m_jDelTicket.setFocusable(false);
		m_jDelTicket.setMargin(new java.awt.Insets(0, 4, 0, 4));
		m_jDelTicket.setRequestFocusEnabled(false);
		m_jDelTicket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jDelTicketActionPerformed(evt);
			}
		});
		add(m_jDelTicket);

		jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/redo16.png"))); // NOI18N
		jButton2.setFocusPainted(false);
		jButton2.setFocusable(false);
		jButton2.setMargin(new java.awt.Insets(0, 4, 0, 4));
		jButton2.setRequestFocusEnabled(false);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		add(jButton2);

		jButton1.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/atlantikdesigner.png"))); // NOI18N
		jButton1.setFocusPainted(false);
		jButton1.setFocusable(false);
		jButton1.setMargin(new java.awt.Insets(0, 4, 0, 4));
		jButton1.setRequestFocusEnabled(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		add(jButton1);
	}// </editor-fold>//GEN-END:initComponents

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed

		m_restaurant.moveTicket();

	}// GEN-LAST:event_jButton2ActionPerformed

	private void m_jDelTicketActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDelTicketActionPerformed

		int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"),
				AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			m_restaurant.deleteTicket();
		}

	}// GEN-LAST:event_m_jDelTicketActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		m_restaurant.newTicket();

	}// GEN-LAST:event_jButton1ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton m_jDelTicket;
	// End of variables declaration//GEN-END:variables

}
