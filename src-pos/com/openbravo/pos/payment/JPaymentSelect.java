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

package com.openbravo.pos.payment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.ComponentOrientation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author adrianromero
 */
public abstract class JPaymentSelect extends javax.swing.JDialog implements JPaymentNotifier {

	private PaymentInfoList m_aPaymentInfo;
	private boolean printselected;
	private boolean printsecond;

	private boolean accepted;

	private AppView m_App;
	private double m_dTotal;
	private CustomerInfoExt customerext;
	private DataLogicSystem dlSystem;

	private Map<String, JPaymentInterface> payments = new HashMap<String, JPaymentInterface>();
	private String m_sTransactionID;

	/** Creates new form JPaymentSelect */
	protected JPaymentSelect(java.awt.Frame parent, boolean modal, ComponentOrientation o) {
		super(parent, modal);
		// initComponents();

		this.applyComponentOrientation(o);

	}

	/** Creates new form JPaymentSelect */
	protected JPaymentSelect(java.awt.Dialog parent, boolean modal, ComponentOrientation o) {
		super(parent, modal);

	}

	public void init(AppView app) {
		this.m_App = app;
		dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		printselected = true;
		printsecond = false;
		
		initComponents();

		getRootPane().setDefaultButton(m_jButtonOK);
		ScaleButtons();
	}

	public void setPrintSelected(boolean value) {
		printselected = value;
	}

	public boolean isPrintSelected() {
		return printselected;
	}
	
	public boolean isPrintSecond() {
		return printsecond;
	}
	
	public void setPrintSecond(boolean value) {
		printsecond = value;
		if(m_jSecondPrint != null)
			m_jSecondPrint.setSelected(value);
	}

	public List<PaymentInfo> getSelectedPayments() {
		return m_aPaymentInfo.getPayments();
	}

	public boolean showDialog(double total, CustomerInfoExt customerext) {

		m_aPaymentInfo = new PaymentInfoList();
		accepted = false;

		m_dTotal = total;

		this.customerext = customerext;

		m_jButtonPrint.setSelected(printselected);
		m_jButtonPrint.setVisible(false); // hide print toggle button !!

		m_jSecondPrint.setSelected(false);
		m_jSecondPrint.setVisible(true);
		
		m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dTotal)));

		addTabs();

		if (m_jTabPayment.getTabCount() == 0) {
			// No payment panels available
			m_aPaymentInfo.add(getDefaultPayment(total));
			accepted = true;
		} else {
			getRootPane().setDefaultButton(m_jButtonOK);
			printState();
			setAlwaysOnTop(true);
			setVisible(true);
		}

		// gets the print button state
		printselected = m_jButtonPrint.isSelected();
		printsecond = m_jSecondPrint.isSelected();

		// remove all tabs
		m_jTabPayment.removeAll();

		return accepted;
	}

	protected abstract void addTabs();

	protected abstract void setStatusPanel(boolean isPositive, boolean isComplete, boolean isPrintSecond);

	protected abstract PaymentInfo getDefaultPayment(double total);

	protected void setOKEnabled(boolean value) {
		m_jButtonOK.setEnabled(value);
	}
	
	protected void setPrintSecondEnabled(boolean value) {
		m_jSecondPrint.setEnabled(value);
	}

	protected void setAddEnabled(boolean value) {
		// m_jButtonAdd.setEnabled(value);
	}

	protected void addTabPayment(JPaymentCreator jpay) {
		if (m_App.getAppUserView().getUser().hasPermission(jpay.getKey())) {

			JPaymentInterface jpayinterface = payments.get(jpay.getKey());
			if (jpayinterface == null) {
				jpayinterface = jpay.createJPayment();
				payments.put(jpay.getKey(), jpayinterface);
			}

			jpayinterface.getComponent().applyComponentOrientation(getComponentOrientation());
			javax.swing.ImageIcon ico = new javax.swing.ImageIcon(getClass().getResource(jpay.getIconKey()));
			
			m_jTabPayment.addTab(
					AppLocal.getIntString(jpay.getLabelKey()),
					ico, 
					jpayinterface.getComponent()
				);
			
		}
	}

	public interface JPaymentCreator {
		public JPaymentInterface createJPayment();

		public String getKey();

		public String getLabelKey();

		public String getIconKey();
	}

	public class JPaymentCashCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentCashPos(m_App, JPaymentSelect.this, dlSystem);
		}

		public String getKey() {
			return "payment.cash";
		}

		public String getLabelKey() {
			return "tab.cash";
		}

		public String getIconKey() {
			return "/com/openbravo/images/cash.png";
		}
	}

	public class JPaymentChequeCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentCheque(m_App, JPaymentSelect.this);
		}

		public String getKey() {
			return "payment.cheque";
		}

		public String getLabelKey() {
			return "tab.cheque";
		}

		public String getIconKey() {
			return "/com/openbravo/images/desktop.png";
		}
	}

	public class JPaymentPaperCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentPaper(m_App, JPaymentSelect.this, "paperin");
		}

		public String getKey() {
			return "payment.paper";
		}

		public String getLabelKey() {
			return "tab.paper";
		}

		public String getIconKey() {
			return "/com/openbravo/images/knotes.png";
		}
	}

	public class JPaymentMagcardCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentMagcard(m_App, JPaymentSelect.this);
		}

		public String getKey() {
			return "payment.magcard";
		}

		public String getLabelKey() {
			return "tab.magcard";
		}

		public String getIconKey() {
			return "/com/openbravo/images/vcard.png";
		}
	}

	public class JPaymentFreeCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentFree(m_App, JPaymentSelect.this);
		}

		public String getKey() {
			return "payment.free";
		}

		public String getLabelKey() {
			return "tab.free";
		}

		public String getIconKey() {
			return "/com/openbravo/images/package_toys.png";
		}
	}

	public class JPaymentDebtCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentDebt(m_App, JPaymentSelect.this);
		}

		public String getKey() {
			return "payment.debt";
		}

		public String getLabelKey() {
			return "tab.debt";
		}

		public String getIconKey() {
			return "/com/openbravo/images/kdmconfig32.png";
		}
	}

	public class JPaymentCashRefundCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentRefund(m_App, JPaymentSelect.this, "cashrefund");
		}

		public String getKey() {
			return "refund.cash";
		}

		public String getLabelKey() {
			return "tab.cashrefund";
		}

		public String getIconKey() {
			return "/com/openbravo/images/cash.png";
		}
	}

	public class JPaymentChequeRefundCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentRefund(m_App, JPaymentSelect.this, "chequerefund");
		}

		public String getKey() {
			return "refund.cheque";
		}

		public String getLabelKey() {
			return "tab.chequerefund";
		}

		public String getIconKey() {
			return "/com/openbravo/images/desktop.png";
		}
	}

	public class JPaymentPaperRefundCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentPaper(m_App, JPaymentSelect.this, "paperout");
		}

		public String getKey() {
			return "refund.paper";
		}

		public String getLabelKey() {
			return "tab.paper";
		}

		public String getIconKey() {
			return "/com/openbravo/images/knotes.png";
		}
	}

	public class JPaymentMagcardRefundCreator implements JPaymentCreator {
		public JPaymentInterface createJPayment() {
			return new JPaymentMagcard(m_App, JPaymentSelect.this);
		}

		public String getKey() {
			return "refund.magcard";
		}

		public String getLabelKey() {
			return "tab.magcard";
		}

		public String getIconKey() {
			return "/com/openbravo/images/vcard.png";
		}
	}

	protected void setHeaderVisible(boolean value) {
		jPanel6.setVisible(value);
	}

	private void printState() {

		m_jRemaininglEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dTotal - m_aPaymentInfo.getTotal())));
		//m_jButtonRemove.setEnabled(!m_aPaymentInfo.isEmpty());
		m_jTabPayment.setSelectedIndex(0); // selecciono el primero
		((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(customerext,
				m_dTotal - m_aPaymentInfo.getTotal(), m_sTransactionID);
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

	public void setStatus(boolean isPositive, boolean isComplete, boolean isPrintSecond) {

		setStatusPanel(isPositive, isComplete, isPrintSecond);
	}

	public void setTransactionID(String tID) {
		this.m_sTransactionID = tID;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel4 = new javax.swing.JPanel();
		m_jLblTotalEuros1 = new javax.swing.JLabel();
		m_jTotalEuros = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		m_jLblRemainingEuros = new javax.swing.JLabel();
		m_jRemaininglEuros = new javax.swing.JLabel();
		// m_jButtonAdd = new javax.swing.JButton();
		// m_jButtonRemove = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		m_jTabPayment = new javax.swing.JTabbedPane();
		jPanel5 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		m_jButtonPrint = new javax.swing.JToggleButton();
		m_jSecondPrint = new javax.swing.JToggleButton();
		jPanel1 = new javax.swing.JPanel();
		m_jButtonOK = new javax.swing.JButton();
		m_jButtonCancel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(AppLocal.getIntString("payment.title")); // NOI18N

		PropertyUtil.ScaleTabbedPaneFontsize(m_App, m_jTabPayment, "common-dialog-fontsize", "22");
		
		m_jTabPayment.setUI(new BasicTabbedPaneUI() {
			   @Override
			   protected void installDefaults() {
			       super.installDefaults();
			       tabInsets = new Insets(0,4,40,4);
			       lightHighlight = Color.gray;
			       shadow = Color.gray;
			       darkShadow = Color.gray;
			   }
			});
		// reset color
		m_jTabPayment.setBackground(null);
		
		m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
		jPanel4.add(m_jLblTotalEuros1);

		m_jTotalEuros.setBackground(java.awt.Color.white);
		m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
		m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jTotalEuros.setOpaque(true);
		// m_jTotalEuros.setPreferredSize(new java.awt.Dimension(125, 25));
		m_jTotalEuros.setRequestFocusEnabled(false);
		jPanel4.add(m_jTotalEuros);

		jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

		m_jLblRemainingEuros.setText(AppLocal.getIntString("label.remainingcash")); // NOI18N
		jPanel6.add(m_jLblRemainingEuros);

		m_jRemaininglEuros.setBackground(java.awt.Color.white);
		m_jRemaininglEuros.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
		m_jRemaininglEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		m_jRemaininglEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory
						.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")),
				javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
		m_jRemaininglEuros.setOpaque(true);
		// m_jRemaininglEuros.setPreferredSize(new java.awt.Dimension(125, 25));
		m_jRemaininglEuros.setRequestFocusEnabled(false);
		jPanel6.add(m_jRemaininglEuros);

		/*
		m_jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png"))); // NOI18N
		m_jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonAddActionPerformed(evt);
			}
		});
		jPanel6.add(m_jButtonAdd);
		*/
		/*
		m_jButtonRemove
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnminus.png"))); // NOI18N
		m_jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonRemoveActionPerformed(evt);
			}
		});
		jPanel6.add(m_jButtonRemove);
		*/

		jPanel4.add(jPanel6);

		getContentPane().add(jPanel4, java.awt.BorderLayout.NORTH);

		jPanel3.setLayout(new java.awt.BorderLayout());
//		m_jTabPayment.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
//		m_jTabPayment.setPreferredSize(new Dimension(1000, 1000));
		m_jTabPayment.setTabPlacement(javax.swing.JTabbedPane.LEFT);
		m_jTabPayment.setFocusable(false);
		m_jTabPayment.setRequestFocusEnabled(false);
		m_jTabPayment.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				m_jTabPaymentStateChanged(evt);
			}
		});
		jPanel3.add(m_jTabPayment, java.awt.BorderLayout.CENTER);

		getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

		jPanel5.setLayout(new java.awt.BorderLayout());

		m_jButtonPrint
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileprint.png"))); // NOI18N
		m_jButtonPrint.setSelected(true);
		m_jButtonPrint.setFocusPainted(false);
		m_jButtonPrint.setFocusable(false);
		m_jButtonPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jButtonPrint.setRequestFocusEnabled(false);
		jPanel2.add(m_jButtonPrint);
		
		m_jSecondPrint
			.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/receipt_printer_x2.png"))); // NOI18N
		m_jSecondPrint.setSelected(false);
		m_jSecondPrint.setFocusPainted(false);
		m_jSecondPrint.setFocusable(false);
		m_jSecondPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jSecondPrint.setRequestFocusEnabled(false);
		m_jSecondPrint.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource().getClass().equals(JToggleButton.class))
				{
					JToggleButton btn = (JToggleButton)e.getSource();
					
					if(btn.isSelected())
					{
						btn.setContentAreaFilled(false);
						btn.setOpaque(true);
						btn.setBackground(Color.green);
					}
					else
					{
						btn.setContentAreaFilled(true);
						btn.setOpaque(true);
						btn.setBackground(null);
					}
				}
			}
		});
		jPanel2.add(m_jSecondPrint);
		
		jPanel2.add(jPanel1);
		
		m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		m_jButtonOK.setText(AppLocal.getIntString("message.pay")); // NOI18N
		m_jButtonOK.setFocusPainted(false);
		m_jButtonOK.setFocusable(false);
		m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonOK.setRequestFocusEnabled(false);
		m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonOKActionPerformed(evt);
			}
		});
		jPanel2.add(m_jButtonOK);

		m_jButtonCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		m_jButtonCancel.setFocusPainted(false);
		m_jButtonCancel.setFocusable(false);
		m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonCancel.setRequestFocusEnabled(false);
		m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonCancelActionPerformed(evt);
			}
		});
		jPanel2.add(m_jButtonCancel);

		jPanel5.add(jPanel2, java.awt.BorderLayout.LINE_END);

		getContentPane().add(jPanel5, java.awt.BorderLayout.SOUTH);

		int width = 860;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		if(d.getWidth() < 860)
		{
			width = (int)d.getWidth(); 
		}
		int dialogWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "dialog-payment-width", String.format("%1$d", width)));
		int dialogHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "dialog-payment-height", "700"));
		
		PropertyUtil.ScaleDialog(m_App, this, dialogWidth, dialogHeight);

		// java.awt.Dimension screenSize =
		// java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds((screenSize.width - 672) / 2, (screenSize.height - 497) /
		// 2, 672, 497);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonRemoveActionPerformed

		m_aPaymentInfo.removeLast();
		printState();

	}// GEN-LAST:event_m_jButtonRemoveActionPerformed

	private void m_jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonAddActionPerformed

		PaymentInfo returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
		if (returnPayment != null) {
			m_aPaymentInfo.add(returnPayment);
			printState();
		}

	}// GEN-LAST:event_m_jButtonAddActionPerformed

	private void m_jTabPaymentStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_m_jTabPaymentStateChanged

		if (m_jTabPayment.getSelectedComponent() != null) {
			((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(customerext,
					m_dTotal - m_aPaymentInfo.getTotal(), m_sTransactionID);
		}

	}// GEN-LAST:event_m_jTabPaymentStateChanged

	private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonOKActionPerformed

		PaymentInfo returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
		if (returnPayment != null) {
			m_aPaymentInfo.add(returnPayment);
			accepted = true;
			dispose();
		}

	}// GEN-LAST:event_m_jButtonOKActionPerformed

	private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonCancelActionPerformed

		dispose();

	}// GEN-LAST:event_m_jButtonCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	//private javax.swing.JButton m_jButtonAdd;
	private javax.swing.JButton m_jButtonCancel;
	private javax.swing.JButton m_jButtonOK;
	private javax.swing.JToggleButton m_jSecondPrint;
	private javax.swing.JToggleButton m_jButtonPrint;
	//private javax.swing.JButton m_jButtonRemove;
	private javax.swing.JLabel m_jLblRemainingEuros;
	private javax.swing.JLabel m_jLblTotalEuros1;
	private javax.swing.JLabel m_jRemaininglEuros;
	private javax.swing.JTabbedPane m_jTabPayment;
	private javax.swing.JLabel m_jTotalEuros;
	// End of variables declaration//GEN-END:variables

	private void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, m_jTotalEuros, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jRemaininglEuros,"common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblTotalEuros1, "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jLblRemainingEuros, "common-dialog-fontsize", "22");
		
		int blwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-width", "32"));
		int blheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-height", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));
		
		//PropertyUtil.ScaleButtonIcon(m_jButtonAdd, blwidth, blheight, fontsize);
		//PropertyUtil.ScaleButtonIcon(m_jButtonRemove, blwidth, blheight, fontsize);
		
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "60"));
		PropertyUtil.ScaleButtonIcon(m_jButtonOK, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jButtonCancel, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jSecondPrint,  btnWidth, btnHeight, fontsize);

	}
}
