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

package com.openbravo.pos.sales.shared;

import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.forms.*;

public class JTicketsBagShared extends JTicketsBag {

	private String m_sCurrentTicket = null;
	private DataLogicReceipts dlReceipts = null;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel m_jPanelMap;

	/** Creates new form JTicketsBagShared */
	public JTicketsBagShared(AppView app, TicketsEditor panelticket) {

		super(app, panelticket);

		dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");

		initComponents();
	}

	public void ScaleButtons() {
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));
		PropertyUtil.ScaleButtonIcon(m_jNewTicket, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jDelTicket, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jListTickets, btnWidth, btnHeight, fontsize);

		PropertyUtil.ScaleButtonIcon(m_jbtnLogout, btnWidth, btnHeight, fontsize);
		
//		m_jNewTicket.setPreferredSize(new Dimension(btnWidth, btnHeight));
//		m_jDelTicket.setPreferredSize(new Dimension(btnWidth, btnHeight));
//		m_jListTickets.setPreferredSize(new Dimension(btnWidth, btnHeight));
//		m_jbtnLogout.setPreferredSize(new Dimension(btnWidth, btnHeight));
	}

	public void activate() {

		// Authorization
		m_jDelTicket.setEnabled(
				m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));

		// precondicion es que no tenemos ticket activado ni ticket en el panel
		m_sCurrentTicket = null;
		selectValidTicket();

		// postcondicion es que tenemos ticket activado aqui y ticket en el
		// panel
	}

	public boolean deactivate() {

		// precondicion es que tenemos ticket activado aqui y ticket en el panel

		saveCurrentTicket();

		m_sCurrentTicket = null;
		m_panelticket.setActiveTicket(null, null);

		return true;

		// postcondicion es que no tenemos ticket activado ni ticket en el panel
	}

	public void deleteTicket(boolean delete) {
		m_sCurrentTicket = null;
		selectValidTicket();
	}

	protected JComponent getBagComponent() {
		return this;
	}

	protected JComponent getNullComponent() {
		return new JPanel();
	}

	private void saveCurrentTicket() {

		// save current ticket, if exists,
		if (m_sCurrentTicket != null) {
			try {
				dlReceipts.insertSharedTicket(m_sCurrentTicket, m_panelticket.getActiveTicket());
			} catch (BasicException e) {
				new MessageInf(e).show(m_App, this);
			}
		}
	}

	private void setActiveTicket(String id) throws BasicException {

		// BEGIN TRANSACTION
		TicketInfo ticket = dlReceipts.getSharedTicket(id);
		if (ticket == null) {
			// Does not exists ???
			throw new BasicException(AppLocal.getIntString("message.noticket"));
		} else {
			dlReceipts.deleteSharedTicket(id);
			m_sCurrentTicket = id;
			m_panelticket.setActiveTicket(ticket, null);
		}
		// END TRANSACTION
	}

	private void selectValidTicket() {

		try {
			List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();
			if (l.size() == 0) {
				newTicket();
			} else {
				setActiveTicket(l.get(0).getId());
			}
		} catch (BasicException e) {
			new MessageInf(e).show(m_App, this);
			newTicket();
		}
	}

	private void newTicket() {

		saveCurrentTicket();

		TicketInfo ticket = new TicketInfo();
		m_sCurrentTicket = UUID.randomUUID().toString(); // m_fmtid.format(ticket.getId());
		m_panelticket.setActiveTicket(ticket, null);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		m_jPanelMap = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		m_jNewTicket = new javax.swing.JButton();
		m_jDelTicket = new javax.swing.JButton();
		m_jListTickets = new javax.swing.JButton();
		m_jbtnLogout = new javax.swing.JButton();

		setLayout(new java.awt.CardLayout());

		m_jPanelMap.setLayout(new java.awt.BorderLayout());

		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		m_jNewTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png")));
		m_jNewTicket.setText(AppLocal.getIntString("Button.NewTicket"));
		m_jNewTicket.setFocusPainted(false);
		m_jNewTicket.setFocusable(false);
		// m_jNewTicket.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_jNewTicket.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jNewTicket.setRequestFocusEnabled(false);
		m_jNewTicket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jNewTicketActionPerformed(evt);
			}
		});

		jPanel2.add(m_jNewTicket);

		m_jDelTicket
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/deleteTicket.png")));
		m_jDelTicket.setText(AppLocal.getIntString("Button.DeleteTicket")); // NOI18N
		m_jDelTicket.setFocusPainted(false);
		m_jDelTicket.setFocusable(false);
		// m_jDelTicket.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_jDelTicket.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jDelTicket.setRequestFocusEnabled(false);
		m_jDelTicket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jDelTicketActionPerformed(evt);
			}
		});

		jPanel2.add(m_jDelTicket);

		m_jListTickets
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/unsortedList.png")));
		m_jListTickets.setText(AppLocal.getIntString("caption.tickets"));
		m_jListTickets.setFocusPainted(false);
		m_jListTickets.setFocusable(false);
		// m_jListTickets.setMargin(new java.awt.Insets(8, 14, 8, 14));
		m_jListTickets.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jListTickets.setRequestFocusEnabled(false);
		m_jListTickets.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jListTicketsActionPerformed(evt);
			}
		});

		jPanel2.add(m_jListTickets);

		add(jPanel1, java.awt.BorderLayout.NORTH);
		jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);
		jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_END);
		
		
//		m_jPanelMap.add(jPanel1, java.awt.BorderLayout.LINE_END);
//
//		add(m_jPanelMap, "map");
		
		m_jbtnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/logout.png"))); // NOI18N
		m_jbtnLogout.setText(AppLocal.getIntString("button.logout"));
		m_jbtnLogout.setFocusPainted(false);
		m_jbtnLogout.setFocusable(false);
		// m_jbtnLogout.setMargin(new java.awt.Insets(0,0,0,0));
		m_jbtnLogout.setRequestFocusEnabled(false);
		m_jbtnLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jbtnLogoutActionPerformed(evt);
			}
		});

		jPanel3.add(m_jbtnLogout);
	}
	
	private void m_jbtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnRefreshActionPerformed

		// TODO logout
		((JPrincipalApp) m_App.getAppUserView()).getAppview().closeAppView();
	}
	
	private void m_jListTicketsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jListTicketsActionPerformed

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();

					JTicketsBagSharedList listDialog = JTicketsBagSharedList.newJDialog(m_App, JTicketsBagShared.this);
					String id = listDialog.showTicketsList(l);

					if (id != null) {
						saveCurrentTicket();
						setActiveTicket(id);
					}
				} catch (BasicException e) {
					new MessageInf(e).show(m_App, JTicketsBagShared.this);
					newTicket();
				}
			}
		});

	}// GEN-LAST:event_m_jListTicketsActionPerformed

	private void m_jDelTicketActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDelTicketActionPerformed
		int res = JConfirmDialog.showConfirm(m_App, this, AppLocal.getIntString("message.wannadelete"), null);
		if (res == JOptionPane.YES_OPTION) {
			deleteTicket(true);
		}

	}// GEN-LAST:event_m_jDelTicketActionPerformed

	private void m_jNewTicketActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jNewTicketActionPerformed

		newTicket();

	}// GEN-LAST:event_m_jNewTicketActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton m_jDelTicket;
	private javax.swing.JButton m_jListTickets;
	private javax.swing.JButton m_jNewTicket;
	
	private JButton m_jbtnLogout;
	// End of variables declaration//GEN-END:variables

	@Override
	protected void ticketListChange(JTicketLines ticketLines) {

		// ticket list
		// if (ticketLines.getTableModelSize() >= 0) {
		// m_jDelTicket.setEnabled(true);
		// }
		// // empty
		// else {
		// m_jDelTicket.setEnabled(false);
		// }
	}

}
