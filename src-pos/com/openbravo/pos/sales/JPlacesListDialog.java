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

package com.openbravo.pos.sales;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.restaurant.PlaceSplit;
import com.openbravo.pos.util.ModifiedFlowLayout;
import com.openbravo.pos.util.PropertyUtil;



import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class JPlacesListDialog extends javax.swing.JDialog {

	private PlaceSplit m_sDialogPlace;
	private AppView m_App;

	/** Creates new form JTicketsBagSharedList */
	private JPlacesListDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JTicketsBagSharedList */
	private JPlacesListDialog(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	public static JPlacesListDialog newJDialog(AppView app, Component parentComponent) {
		Window window = getWindow(parentComponent);
		JPlacesListDialog mydialog;
		if (window instanceof Frame) {
			mydialog = new JPlacesListDialog((Frame) window, true);
		} else {
			mydialog = new JPlacesListDialog((Dialog) window, true);
		}

		mydialog.init(app);
		return mydialog;
	}

	public PlaceSplit showPlacesList(java.util.List<PlaceSplit> places) {
		if(places == null)
			return null;
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		String prevFloor = null;
		Color currentColor = null;
		for (int i = 0; i < places.size(); i++) {
			
			PlaceSplit place = places.get(i);
			
			if(prevFloor == null || !prevFloor.equals(place.getFloorName()))
			{
				if(prevFloor != null)
				{
					JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
					sep.setPreferredSize(new Dimension(screenSize.width-80, 2));
					m_jplaces.add(sep);
					
				}
				
				JButton floor = new JButton(place.getFloorName());
				floor.setBackground(Color.LIGHT_GRAY);
				floor.setMargin(new Insets(8, 8, 8, 8));
				PropertyUtil.ScaleButtonFontsize(m_App, floor, "common-dialog-fontsize", "22");
				m_jplaces.add(floor);
			}


			JButtonTicket placeBtn = new JButtonTicket(place);
			m_jplaces.add(placeBtn);
			
			prevFloor = place.getFloorName();
		}
		
		PropertyUtil.ScaleDialog(m_App, this, screenSize.width, screenSize.height);
		
		m_sDialogPlace = null;
		setVisible(true);
		return m_sDialogPlace;
	}
	
	
	private void init(AppView app) {
		this.m_App = app;
		initComponents();
		
		PropertyUtil.ScaleScrollbar(m_App, jScrollPane1);
		PropertyUtil.ScaleButtonFontsize(m_App, m_jButtonCancel, "common-dialog-fontsize", "22");
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		} else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

	private class JButtonTicket extends JButton {

		private PlaceSplit m_Place;

		public JButtonTicket(PlaceSplit place) {

			super();

			m_Place = place;
			setFocusPainted(false);
			setFocusable(false);
			setRequestFocusEnabled(false);
			setMargin(new Insets(8, 14, 8, 14));
			
			
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					m_sDialogPlace = m_Place;
					JPlacesListDialog.this.setVisible(false);
				}
			});

			
			String name = place.getName(); 
			setText(name);
			if(place.getOccupied())
			{
				setBackground(Color.YELLOW);
			}
			
			if(place.getLocked())
			{
				setBackground(Color.ORANGE);
				setEnabled(false);
			}

			PropertyUtil.ScaleButtonFontsize(m_App, this, "common-dialog-fontsize", "22");
			
			
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

		
		
		m_jplaces = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane(m_jplaces, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		m_jbuttons = new javax.swing.JPanel();
		m_jButtonCancel = new javax.swing.JButton();

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setAlwaysOnTop(true);
		setTitle(AppLocal.getIntString("caption.places")); // NOI18N
		setResizable(false);
		setLayout(new java.awt.BorderLayout());
	
		
		
		
		m_jplaces.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jplaces.setLayout(
				new ModifiedFlowLayout(FlowLayout.LEFT, 10, 10, screenSize.width));
		jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setViewportView(m_jplaces);
		getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
		
		
		
		m_jbuttons.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jbuttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		m_jButtonCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
		m_jButtonCancel.setText(AppLocal.getIntString("Button.Close")); // NOI18N
		m_jButtonCancel.setFocusPainted(false);
		m_jButtonCancel.setFocusable(false);
		m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonCancel.setRequestFocusEnabled(false);
		m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonCancelActionPerformed(evt);
			}
		});
		
		m_jbuttons.add(m_jButtonCancel);
		getContentPane().add(m_jbuttons, java.awt.BorderLayout.SOUTH);
		
		PropertyUtil.ScaleDialog(m_App, this, screenSize.width, screenSize.height);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonCancelActionPerformed

		dispose();

	}// GEN-LAST:event_m_jButtonCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton m_jButtonCancel;
	private javax.swing.JPanel m_jplaces;
	private javax.swing.JPanel m_jbuttons;
	// End of variables declaration//GEN-END:variables

}
