package com.openbravo.pos.printer.screen;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.admin.DataLogicAdmin;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author adrian
 */
public class DevicePrinterDialog extends javax.swing.JDialog {

	private AppView m_App;
	private DataLogicSystem m_dlSystem = null;
	private DeviceTicket m_TP;
	private TicketParser m_TTP;
	
	private String SystemDataAddressLine1 = "";
	private String SystemDataAddressLine2 = "";
	private String SystemDataStreet = "";
	private String SystemDataCity = "";
	private String SystemDataTaxid = "";
	private String SystemDataThanks = "";
	private String SystemDataInfo ="";
	private String SystemDataAccountBank = "";
	private String SystemDataAccountOwner = "";
	private String SystemDataAccountBIC = "";
	private String SystemDataAccountIBAN = "";

	/** Creates new form SelectPrinter */
	private DevicePrinterDialog(java.awt.Frame parent, boolean modal, ComponentOrientation o) {
		super(parent, modal);
		this.applyComponentOrientation(o);
	}

	/** Creates new form SelectPrinter */
	private DevicePrinterDialog(java.awt.Dialog parent, boolean modal, ComponentOrientation o) {
		super(parent, modal);
		this.applyComponentOrientation(o);
	}
	
	public static DevicePrinterDialog getDialog(Component parent) {
        
        Window window = getWindow(parent);
        
        if (window instanceof Frame) { 
            return new DevicePrinterDialog((Frame) window, true, parent.getComponentOrientation());
        } else {
            return new DevicePrinterDialog((Dialog) window, true, parent.getComponentOrientation());
        }
    } 
	
	protected static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		} else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

	
	public void init(AppView app) {
		this.m_App = app;
		initComponents();
		
		m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");

		m_TP = new DeviceTicket(app);
		m_TTP = new TicketParser(m_TP, m_dlSystem); 
		JComponent ticket = m_TP.getDevicePrinter("1").getPrinterComponent();
		m_fitPanel = new ScaleToFitPanel(ticket, ScaleToFitPanel.Fit.AUTO);
		m_jPanelTicket.add(m_fitPanel, BorderLayout.CENTER);

		getRootPane().setDefaultButton(jcmdOK);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent e) {
				if (m_secondScreen != null) {
					m_secondScreen.dispose();
					m_secondScreen = null;
				}
			}
		});

		
	}
	
	public boolean showDialog(TicketInfo m_ticket) {

		m_TP.getDevicePrinter("1").reset();

		if (m_ticket != null) {
			try {
				initSystemData();
				
				ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
				
				script.put("ticket", m_ticket);
				script.put("place", ""); // put empty place
				script.put("host", m_App.getHost());

				script.put("SystemDataAddresLine1", SystemDataAddressLine1);
				script.put("SystemDataAddresLine2", SystemDataAddressLine2);
				script.put("SystemDataStreet", SystemDataStreet);
				script.put("SystemDataCity", SystemDataCity);
				script.put("SystemDataTaxid", SystemDataTaxid);
				script.put("SystemDataThanks", SystemDataThanks);
				script.put("SystemDataInfo", SystemDataInfo);
				script.put("SystemDataAccountBank", SystemDataAccountBank);
				script.put("SystemDataAccountOwner", SystemDataAccountOwner);
				script.put("SystemDataAccountBIC", SystemDataAccountBIC);
				script.put("SystemDataAccountIBAN", SystemDataAccountIBAN);
				
				String []bonsize = m_App.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if(bonsize.length > 2)
					ticketsuffix = "."+bonsize[2];
				
				Boolean print2db = m_App.getAppUserView().getUser().isServer();
				m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.Ticket" + ticketsuffix)).toString(), m_ticket.getId(), print2db);
				
				if (m_fitPanel != null) {
					m_fitPanel.refresh();
				}
				
				showSecondScreen();
				
				
				this.setAlwaysOnTop(true);
				this.setVisible(true);
				
				return true;
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(m_App, this);
			} catch (TicketPrinterException eTP) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), eTP);
				msg.show(m_App, this);
			}
		}
		return false;
	}
	
	private void showSecondScreen() {
		if (!SecondScreenReceipt.hasSecondScreen() || m_fitPanel == null) {
			return;
		}
		java.awt.image.BufferedImage receipt = m_fitPanel.getSnapshot();
		if (receipt == null) {
			return;
		}
		if (m_secondScreen == null) {
			m_secondScreen = new SecondScreenReceipt();
		}
		m_secondScreen.show(receipt);
	}

	private void initSystemData() {
		DataLogicAdmin dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin"); 
        TableDefinition tresources = dlAdmin.getTableResources();
        
        try {
			List res = tresources.getListSentence().list();
			Object o = res.get(0);
			// try to find System.AddressLine1
			for(int i = 0; i < res.size(); i++) {
				if("System.AddressLine1".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAddressLine1 = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					
					continue;
				} else if("System.AddressLine2".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAddressLine2 = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.Street".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataStreet = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.City".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataCity = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.TAXID".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataTaxid = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.Thanks".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataThanks = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.Info".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataInfo = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountBank".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountBank = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountOwner".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountOwner = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountBIC".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountBIC = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				} else if("System.AccountIBAN".compareTo(((Object [])res.get(i))[1].toString())==0) {
					SystemDataAccountIBAN = ((Formats.BYTEA.formatValue(((Object [])res.get(i))[3])));
					continue;
				}
			}
			//res.get(0);
		} catch (BasicException e) {
			Log.Exception(e);
		}
	}

	
	private void initComponents() {

		jPanel8 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new javax.swing.JButton();
		m_jPanelTicket = new javax.swing.JPanel();
		m_jPanelTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_jPanelTicket.setLayout(new java.awt.BorderLayout());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("form.DevicePrinterDialog")); // NOI18N

		jPanel8.setLayout(new java.awt.BorderLayout());

		jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
		jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdOKActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdOK);

		jcmdCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdCancelActionPerformed(evt);
			}
		});
		jPanel1.add(jcmdCancel);
		jPanel8.add(jPanel1, java.awt.BorderLayout.LINE_END);

		getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);
		getContentPane().add(m_jPanelTicket, java.awt.BorderLayout.CENTER);
		
		PropertyUtil.ScaleDialogFullScreen(m_App, this);
		
		//scale button
		int btnWidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width","60"));
		int btnHeight = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height","60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));
		
		PropertyUtil.ScaleButtonIcon(jcmdOK, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonFontsize(jcmdOK, fontsize);

		PropertyUtil.ScaleButtonIcon(jcmdCancel, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonFontsize(jcmdCancel, fontsize);
	}// </editor-fold>//GEN-END:initComponents

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdOKActionPerformed

		dispose();

	}// GEN-LAST:event_jcmdOKActionPerformed

	private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jcmdCancelActionPerformed

		dispose();

	}// GEN-LAST:event_jcmdCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel m_jPanelTicket;
	private ScaleToFitPanel m_fitPanel;
	private SecondScreenReceipt m_secondScreen;
	private javax.swing.JButton jcmdCancel;
	private javax.swing.JButton jcmdOK;
	// End of variables declaration//GEN-END:variables

}
