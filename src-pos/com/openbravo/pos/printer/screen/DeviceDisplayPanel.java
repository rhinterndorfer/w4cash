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
import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;

/**
 *
 * @author  adrianromero
 */
public class DeviceDisplayPanel extends JPanel implements DeviceDisplay, DeviceDisplayImpl {
    
    private String m_sName;
    
    private DeviceDisplayBase m_displaylines;
    
    /** Creates new form JVisor */
    public DeviceDisplayPanel() {
        this(1.0);
    }
    public DeviceDisplayPanel(double dZoom) {
        initComponents();
        
        m_sName = AppLocal.getIntString("Display.Screen");
        
        jline1.setFont(new Font("Courier New", Font.BOLD, (int)(16 * dZoom)));
        jline2.setFont(new Font("Courier New", Font.BOLD, (int)(16 * dZoom)));

        m_displaylines = new DeviceDisplayBase(this);
    }
    
    public void setAppView(AppView app) {};
    
    public String getDisplayName() {
        return m_sName;
    }    
    public String getDisplayDescription() {
        return null;
    }        
    public JComponent getDisplayComponent() {
        return this;
    }
    
    public void writeVisor(int animation, String sLine1, String sLine2) {
        
        m_displaylines.writeVisor(animation, sLine1, sLine2);
    }
    
    public void writeVisor(String sLine1, String sLine2) {
        
        m_displaylines.writeVisor(sLine1, sLine2);
    }

    public void clearVisor() {
        m_displaylines.clearVisor();
    }
    
    public void repaintLines() {
        jline1.setText(m_displaylines.getLine1());
        if(jline1.getText().trim().isEmpty()) {
        	jline1.setOpaque(false);
        } else {
        	jline1.setOpaque(true);
        }
        jline2.setText(m_displaylines.getLine2());
        if(jline2.getText().trim().isEmpty()) {
        	jline2.setOpaque(false);
        } else {
        	jline2.setOpaque(true);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        paintBorder(g);
        
        //Graphics2D g2d = (Graphics2D) g;     
        
        //Insets i = getInsets();
        
        //g.setColor(getBackground());
        /*
        g2d.setPaint(new GradientPaint(getWidth() - i.left - i.right - 50, getHeight() - i.top - i.bottom - 50, getBackground()
                                     , getWidth() - i.left - i.right, getHeight() - i.top - i.bottom, new Color(0xf0f0f0), true));
        
        //g2d.setPaint(new paint);
        //g2d.fillRect(i.left, i.top, getWidth() - i.left - i.right, getHeight() - i.top - i.bottom);
         * */
        
        //g2d.clearRect(i.left, i.top, getWidth() - i.left - i.right, getHeight() - i.top - i.bottom);

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jline1 = new javax.swing.JLabel();
        jline2 = new javax.swing.JLabel();

        //setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jline1.setText("jline1");
        jline1.setBackground(Color.white);
        jline1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jline1, gridBagConstraints);

        jline2.setText("jline2");
        jline2.setBackground(Color.white);
        jline2.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jline2, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jline1;
    private javax.swing.JLabel jline2;
    // End of variables declaration//GEN-END:variables
    
}
