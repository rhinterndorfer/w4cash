//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.RoundUtils;

public class JPaymentPaper extends javax.swing.JPanel implements JPaymentInterface {
    
    private JPaymentNotifier m_notifier;
    
    private double m_dTicket;
    
    private String m_sPaper; // "paperin", "paperout"
    // private String m_sCustomer; 

	private AppView m_App;
    
    
    /** Creates new form JPaymentTicket */
    public JPaymentPaper(AppView app, JPaymentNotifier notifier, String sPaper) {
        m_App = app;
        m_sPaper = sPaper;
        
        initComponents();
        
        m_notifier = notifier;
        
		ScaleButtons();
    }

	public void activate(CustomerInfoExt customerext, double dTotal, String transID) {
        m_dTicket = dTotal;
        
        m_notifier.setStatus(customerext != null, customerext != null, true);
        
        if(customerext == null)
        {
        	jlblMessage.setText(AppLocal.getIntString("message.nocustomerselected")); 
        }
        else
        	jlblMessage.setText("");
    }
    
    public Component getComponent() {
        return this;
    }
    
    public PaymentInfo executePayment() {

        return new PaymentInfoTicket(m_dTicket, m_sPaper);
    }    
    
   
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	jPanel1 = new javax.swing.JPanel();
        jlblMessage = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jlblMessage.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        jlblMessage.setEditable(false);
        jlblMessage.setLineWrap(true);
        jlblMessage.setWrapStyleWord(true);
        jlblMessage.setFocusable(false);
        jlblMessage.setPreferredSize(new java.awt.Dimension(350, 150));
        jlblMessage.setRequestFocusEnabled(false);
        jPanel1.add(jlblMessage);

        add(jPanel1, java.awt.BorderLayout.CENTER);
        
    }// </editor-fold>//GEN-END:initComponents
    
    private void ScaleButtons() {
    	PropertyUtil.ScaleTextAreaFontsize(m_App, jlblMessage, "common-small-fontsize", "32");
	}
    
    
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea jlblMessage;
}
