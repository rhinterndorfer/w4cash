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

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.format.Formats;

import java.awt.Component;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.AltEncrypter;
import com.openbravo.pos.util.DirectoryEvent;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class JPanelSystemData extends javax.swing.JPanel implements PanelConfig {

	private static final long serialVersionUID = 1055100919119422081L;
	
	private DirtyManager dirty = new DirtyManager();
	private AppView m_App;
	
	private int systemDataAddressLine1 = -1;
	private int systemDataAddressLine2 = -1;
	private int systemDataStreet = -1;
	private int systemDataCity = -1;
	private int systemDataTAXID = -1;
	private int systemDataThanks = -1;
	private int systemDataAccountBank = -1;
	private int systemDataAccountOwner = -1;
	private int systemDataAccountBIC = -1;
	private int systemDataAccountIBAN = -1;
	
	/** Creates new form JPanelConfigDatabase */
	public JPanelSystemData(AppView oApp) {

		this.m_App = oApp;
		initComponents();

		txtSystemDataAddressLine1.getDocument().addDocumentListener(dirty);
		txtSystemDataAddressLine2.getDocument().addDocumentListener(dirty);
		txtSystemDataStreet.getDocument().addDocumentListener(dirty);
		txtSystemDataTAXID.getDocument().addDocumentListener(dirty);
		txtSystemDataCity.getDocument().addDocumentListener(dirty);
		txtSystemDataThanks.getDocument().addDocumentListener(dirty);
		txtSystemDataAccountBank.getDocument().addDocumentListener(dirty);
		txtSystemDataAccountOwner.getDocument().addDocumentListener(dirty);
		txtSystemDataAccountBIC.getDocument().addDocumentListener(dirty);
		txtSystemDataAccountIBAN.getDocument().addDocumentListener(dirty);
//		jbtnDbDriverLib.addActionListener(new DirectoryEvent(jtxtDbDriverLib));
	}

	public boolean hasChanged() {
		return dirty.isDirty();
	}

	public Component getConfigComponent() {
		return this;
	}

	public void loadProperties() {
		DataLogicAdmin dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin"); 
        TableDefinition tresources = dlAdmin.getTableResources();
        
        try {
			List res = tresources.getListSentence().list();
			Object o = res.get(0);
			// try to find System.AddressLine1
			for(int i = 0; i < res.size(); i++) {
				if("System.AddressLine1".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAddressLine1.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAddressLine1 = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.AddressLine2".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAddressLine2.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAddressLine2 = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.Street".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataStreet.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataStreet = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.City".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataCity.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataCity = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.TAXID".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataTAXID.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataTAXID = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.Thanks".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataThanks.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataThanks = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.AccountBank".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAccountBank.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAccountBank = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.AccountOwner".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAccountOwner.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAccountOwner = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.AccountBIC".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAccountBIC.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAccountBIC = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				} else if("System.AccountIBAN".compareTo(((Object [])res.get(i))[1].toString())==0) {
					txtSystemDataAccountIBAN.setText((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					this.systemDataAccountIBAN = Integer.parseInt(((Object [])res.get(i))[0].toString());
					continue;
				}
			}
			//res.get(0);
		} catch (BasicException e) {
			Log.Exception(e);
		}
	}

	public void saveProperties(AppConfig config) {
		// m_App.
		DataLogicAdmin dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin"); 
        TableDefinition tresources = dlAdmin.getTableResources();
        
       SentenceExec upd = tresources.getUpdateSentence();
       // SentenceExec ins = tresources.getInsertSentence();
       try {
    	   if(systemDataAddressLine1 > -1) {
    		   upd.exec(new Object[]{systemDataAddressLine1 + "", "System.AddressLine1", 0, Formats.BYTEA.parseValue(this.txtSystemDataAddressLine1.getText())}); 
    	   }
    	   if(systemDataAddressLine2 > -1) {
    		   upd.exec(new Object[]{systemDataAddressLine2 + "", "System.AddressLine2", 0, Formats.BYTEA.parseValue(this.txtSystemDataAddressLine2.getText())}); 
    	   }
    	   if(systemDataStreet > -1) {
    		   upd.exec(new Object[]{systemDataStreet + "", "System.Street", 0, Formats.BYTEA.parseValue(this.txtSystemDataStreet.getText())}); 
    	   }
    	   if(systemDataCity > -1) {
    		   upd.exec(new Object[]{systemDataCity + "", "System.City", 0, Formats.BYTEA.parseValue(this.txtSystemDataCity.getText())}); 
    	   }
    	   if(systemDataTAXID > -1) {
    		   upd.exec(new Object[]{systemDataTAXID + "", "System.TAXID", 0, Formats.BYTEA.parseValue(this.txtSystemDataTAXID.getText())}); 
    	   }
    	   if(systemDataThanks > -1) {
    		   upd.exec(new Object[]{systemDataThanks + "", "System.Thanks", 0, Formats.BYTEA.parseValue(this.txtSystemDataThanks.getText())}); 
    	   }
    	   if(systemDataAccountBank > -1) {
    		   upd.exec(new Object[]{systemDataAccountBank + "", "System.AccountBank", 0, Formats.BYTEA.parseValue(this.txtSystemDataAccountBank.getText())}); 
    	   }
    	   if(systemDataAccountOwner > -1) {
    		   upd.exec(new Object[]{systemDataAccountOwner + "", "System.AccountOwner", 0, Formats.BYTEA.parseValue(this.txtSystemDataAccountOwner.getText())}); 
    	   }
    	   if(systemDataAccountBIC > -1) {
    		   upd.exec(new Object[]{systemDataAccountBIC + "", "System.AccountBIC", 0, Formats.BYTEA.parseValue(this.txtSystemDataAccountBIC.getText())}); 
    	   }
    	   if(systemDataAccountIBAN > -1) {
    		   upd.exec(new Object[]{systemDataAccountIBAN + "", "System.AccountIBAN", 0, Formats.BYTEA.parseValue(this.txtSystemDataAccountIBAN.getText())}); 
    	   }
	} catch (BasicException e) {
		Log.Exception(e);
	}
        

		dirty.setDirty(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtSystemDataAddressLine1 = new javax.swing.JTextField();
//		jbtnDbDriverLib = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		txtSystemDataAddressLine2 = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		txtSystemDataStreet = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		txtSystemDataCity = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		txtSystemDataTAXID = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		txtSystemDataThanks = new javax.swing.JTextField();
		jLabel7 = new javax.swing.JLabel();
		txtSystemDataAccountBank = new javax.swing.JTextField();
		jLabel8 = new javax.swing.JLabel();
		txtSystemDataAccountOwner = new javax.swing.JTextField();
		jLabel9 = new javax.swing.JLabel();
		txtSystemDataAccountBIC = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		txtSystemDataAccountIBAN = new javax.swing.JTextField();

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder()); //TitledBorder(AppLocal.getIntString("Label.Database"))); // NOI18N

		jLabel1.setText(AppLocal.getIntString("System.AddressLine1")); // NOI18N

//		jbtnDbDriverLib
//				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N

		jLabel2.setText(AppLocal.getIntString("System.AddressLine2")); // NOI18N

		jLabel3.setText(AppLocal.getIntString("System.Street")); // NOI18N

		jLabel4.setText(AppLocal.getIntString("System.City")); // NOI18N

		jLabel5.setText(AppLocal.getIntString("System.TAXID")); // NOI18N

		jLabel6.setText(AppLocal.getIntString("System.Thanks"));
		
		jLabel7.setText(AppLocal.getIntString("System.AccountBank"));
		
		jLabel8.setText(AppLocal.getIntString("System.AccountOwner"));
		
		jLabel9.setText(AppLocal.getIntString("System.AccountBIC"));
		
		jLabel10.setText(AppLocal.getIntString("System.AccountIBAN"));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel2)
								.addComponent(jLabel3)
								.addComponent(jLabel4)
								.addComponent(jLabel5)
								.addComponent(jLabel6)
								.addComponent(jLabel7)
								.addComponent(jLabel8)
								.addComponent(jLabel9)
								.addComponent(jLabel10)
						) 
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(txtSystemDataStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
														javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(txtSystemDataAddressLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
												javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtSystemDataAddressLine2,
														javax.swing.GroupLayout.PREFERRED_SIZE, 328,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										)
								.addComponent(txtSystemDataTAXID, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataCity, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataThanks, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataAccountBank, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataAccountOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataAccountBIC, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSystemDataAccountIBAN, javax.swing.GroupLayout.PREFERRED_SIZE, 328,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								)
						.addContainerGap(131, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1).addComponent(txtSystemDataAddressLine1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2).addComponent(txtSystemDataAddressLine2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(txtSystemDataStreet, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel4).addComponent(txtSystemDataCity, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel5).addComponent(txtSystemDataTAXID,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel6).addComponent(txtSystemDataThanks,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7).addComponent(txtSystemDataAccountBank,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel8).addComponent(txtSystemDataAccountOwner,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9).addComponent(txtSystemDataAccountBIC,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel10).addComponent(txtSystemDataAccountIBAN,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//								.addComponent(jLabel7).addComponent(cbDemomode,
//										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
//										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(14, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(14, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	private void ScaleButtons(AppConfig app) {
		int btnwidth = 20;
		int fontsize = 16;

//		PropertyUtil.ScaleButtonIcon(jbtnDbDriverLib, btnwidth, btnwidth, fontsize);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel10;
	// private javax.swing.JLabel jLabel7;
	
	private javax.swing.JPanel jPanel1;
//	private javax.swing.JButton jbtnDbDriverLib;
	private javax.swing.JTextField txtSystemDataAddressLine2;
	private javax.swing.JTextField txtSystemDataAddressLine1;
	private javax.swing.JTextField txtSystemDataTAXID;
	private javax.swing.JTextField txtSystemDataStreet;
	private javax.swing.JTextField txtSystemDataCity;
	private javax.swing.JTextField txtSystemDataThanks;
	private javax.swing.JTextField txtSystemDataAccountBank;
	private javax.swing.JTextField txtSystemDataAccountOwner;
	private javax.swing.JTextField txtSystemDataAccountBIC;
	private javax.swing.JTextField txtSystemDataAccountIBAN;
	// private javax.swing.JCheckBox cbDemomode;
	// End of variables declaration//GEN-END:variables

	@Override
	public void loadProperties(AppConfig config) {
		this.loadProperties();
	}

}
