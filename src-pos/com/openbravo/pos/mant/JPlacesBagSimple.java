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

import com.openbravo.pos.forms.*;
import javax.swing.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.ticket.TicketInfo;

public class JPlacesBagSimple extends JPlacesBag {

	/** Creates new form JTicketsBagSimple */
	public JPlacesBagSimple(AppView app) {

		super(app);

		initComponents();
	}

	public void activate() {
		// TODO:
		// m_panelticket.setActiveTicket(new TicketInfo(), null);

		// Authorization
		m_jDelTicket.setEnabled(
				m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));

	}

	public boolean deactivate() {
		// TODO:
		// m_panelticket.setActiveTicket(null, null);
		return true;
	}

	public void deleteTicket() {
		// TODO:
		// m_panelticket.setActiveTicket(new TicketInfo(), null);
	}

	protected JComponent getBagComponent() {
		return this;
	}

	protected JComponent getNullComponent() {
		return new JPanel();
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

		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		m_jDelTicket
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/list-remove.png"))); // NOI18N
		m_jDelTicket.setFocusPainted(false);
		m_jDelTicket.setFocusable(false);
		m_jDelTicket.setMargin(new java.awt.Insets(0, 4, 0, 4));
		m_jDelTicket.setMaximumSize(new java.awt.Dimension(52, 44));
		m_jDelTicket.setMinimumSize(new java.awt.Dimension(52, 44));
		m_jDelTicket.setPreferredSize(new java.awt.Dimension(52, 44));
		m_jDelTicket.setRequestFocusEnabled(false);
		m_jDelTicket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jDelTicketActionPerformed(evt);
			}
		});
		add(m_jDelTicket);
	}// </editor-fold>//GEN-END:initComponents

	public void refreshPlaces(){}
	
	private void m_jDelTicketActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDelTicketActionPerformed

		int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"),
				AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			deleteTicket();
		}

	}// GEN-LAST:event_m_jDelTicketActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton m_jDelTicket;
	// End of variables declaration//GEN-END:variables

	@Override
	public void selectPlace(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Place getPlace(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ScaleButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Place[] getPlaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void floorChanged() {
		// TODO Auto-generated method stub
		
	}

}
