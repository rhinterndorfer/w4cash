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

package com.openbravo.pos.printer.screen;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.ticket.BasicTicket;
import com.openbravo.pos.printer.ticket.BasicTicketForScreen;

public class DevicePrinterPanel extends javax.swing.JPanel implements DevicePrinter {
    
    private String m_sName;
   
    private JTicketContainer m_jTicketContainer;    
    private BasicTicket m_ticketcurrent;
    
    /** Creates new form JPrinterScreen2 */
    public DevicePrinterPanel() {
        initComponents();
        
        m_sName = AppLocal.getIntString("Printer.Screen");
        
         m_ticketcurrent = null;
       
        m_jTicketContainer = new JTicketContainer();
        m_jScrollView.setViewportView(m_jTicketContainer);
    }
    
    public String getPrinterName() {
        return m_sName;
    }
    public String getPrinterDescription() {
        return null;
    }       
    public JComponent getPrinterComponent() {
        return this;
    }
    public void reset() {
        m_ticketcurrent = null;
        m_jTicketContainer.removeAllTickets();
        m_jTicketContainer.repaint();
    }
    
    // INTERFAZ PRINTER 2
    public void beginReceipt() {
        m_ticketcurrent = new BasicTicketForScreen();
    }
    public void printImage(BufferedImage image) {
        m_ticketcurrent.printImage(image);
    }
    public void printBarCode(String type, String position, String code) {
        m_ticketcurrent.printBarCode(type, position, code);
    }
    public void beginLine(int iTextSize) {
        m_ticketcurrent.beginLine(iTextSize);
    }
    public void printText(int iStyle, String sText) {
        m_ticketcurrent.printText(iStyle, sText);
    }
    public void endLine() {
        m_ticketcurrent.endLine();
    } 
    public void endReceipt() {
        m_jTicketContainer.addTicket(new JTicket(m_ticketcurrent));
        m_ticketcurrent = null;
    }
    
    public void openDrawer(String byteCode) {
        // Una simulacion
        Toolkit.getDefaultToolkit().beep();
    }   
       
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        m_jScrollView = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        add(m_jScrollView, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane m_jScrollView;
    // End of variables declaration//GEN-END:variables
    
}

