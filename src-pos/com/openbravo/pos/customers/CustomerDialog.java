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
package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.user.DirtyListener;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.admin.PeoplePanel;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurant;
import com.openbravo.pos.util.OnScreenKeyboardUtil;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.axis.wsdl.toJava.JavaBuildFileWriter;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Raphael Hinterndorfer
 */
public class CustomerDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 589258512297472116L;
	private AppView m_App;
	private String initSearchName;

	/** Creates new form JCustomerFinder */
	private CustomerDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JCustomerFinder */
	private CustomerDialog(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	public static CustomerDialog getCustomerDialog(AppView app, Component parent, DataLogicCustomers dlCustomers, CustomerInfo selectCustomer) {
		Window window = getWindow(parent);

		CustomerDialog dialog;
		if (window instanceof Frame) {
			dialog = new CustomerDialog((Frame) window, true);
		} else {
			dialog = new CustomerDialog((Dialog) window, true);
		}
		if(selectCustomer != null) {
			dialog.setInitCustomerSearchName(selectCustomer.searchkey);	
		}
		dialog.init(app, dlCustomers);
		dialog.applyComponentOrientation(parent.getComponentOrientation());
		dialog.setUndecorated(true);
		
		
		
		return dialog;
	}

	private void init(AppView app, DataLogicCustomers dlCustomers) {
		this.m_App = app;

		initComponents();
	}

	public String getCurrentCustomerSearchName() {
		return customerPanel.getCurrentCustomerSearchName();
	}
	
	public void setInitCustomerSearchName(String searchName) {
		initSearchName = searchName;
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

	private void initComponents() {
		jcmdOK = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		
		setTitle(AppLocal.getIntString("label.customer"));
		
		customerPanel = new CustomersPanel();
		customerPanel.init(this.m_App);
		customerPanel.getComponent().applyComponentOrientation(getComponentOrientation());
		customerPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
		
		//getContentPane().add(customerPanel.getComponent(), java.awt.BorderLayout.CENTER);
		getContentPane().add(customerPanel, java.awt.BorderLayout.CENTER);
		
		jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		jcmdOK.setEnabled(true);
		jcmdOK.setFocusPainted(false);
		jcmdOK.setFocusable(false);
		jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdOK.setRequestFocusEnabled(false);
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionOKPerformed(evt);
			}
		});
		jPanel1.add(jcmdOK);
		getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);
		
		
		PropertyUtil.ScaleDialogFullScreen(m_App, this);
		ScaleButtons();
		getRootPane().setDefaultButton(jcmdOK);
		
		try {
			customerPanel.setInitCustomerSearchName(initSearchName);
			customerPanel.activate();
			DirtyManager dirty = customerPanel.GetDirtyManager();
			dirty.addDirtyListener(new DirtyListener() {
				
				@Override
				public void changedDirty(boolean bDirty) {
					jcmdOK.setEnabled(!bDirty);
				}
			});
			
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actionOKPerformed(java.awt.event.ActionEvent evt) {
		setVisible(false);
	}
	
	public void ScaleButtons() {
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "48"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "48"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(jcmdOK, btnWidth, btnHeight, fontsize);
	}

	private javax.swing.JButton jcmdOK;
	private CustomersPanel customerPanel;
	private javax.swing.JPanel jPanel1;

}
