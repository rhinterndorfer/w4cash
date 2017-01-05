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

package com.openbravo.pos.sales.restaurant;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.util.Map;

import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import layout.TableLayout;
import layout.TableLayoutConstraints;

public class JCalendarItemRenderer extends javax.swing.JPanel implements ListCellRenderer, Serializable {

    protected static Border noFocusBorder;
    
    private boolean m_bDone = false;
    private AppView m_App;
    
    /** Creates new form JCalendarItemRenderer */
    public JCalendarItemRenderer(AppView app) {
        
        super();
        m_App = app;
        
        if (noFocusBorder == null) {
            noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        }
        
        initComponents();
        
        
        m_jTime.setFont(new Font("SansSerif", Font.BOLD, 11)); // HORA
        m_jTitle.setFont(new Font("SansSerif", Font.BOLD, 11)); // TITULO
        m_jDescription.setFont(new Font("SansSerif", Font.ITALIC, 11)); // TEXTO EXPLICATIVO
        m_jChairs.setFont(new Font("SansSerif", Font.ITALIC, 11));
        m_jPlaces.setFont(new Font("SansSerif", Font.ITALIC, 11));
        
        PropertyUtil.ScaleLabelFontsize(m_App, m_jTime, "common-small-fontsize", "32");
        PropertyUtil.ScaleLabelFontsize(m_App, m_jTitle, "common-small-fontsize", "32");
        PropertyUtil.ScaleLabelFontsize(m_App, m_jChairs, "common-small-fontsize", "32");
        PropertyUtil.ScaleLabelFontsize(m_App, m_jDescription, "common-small-fontsize", "32", 0.8);
        PropertyUtil.ScaleLabelFontsize(m_App, m_jPlaces, "common-small-fontsize", "32", 0.8);
        
        setOpaque(true);
        setBorder(noFocusBorder);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        applyComponentOrientation(list.getComponentOrientation());
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            m_jTime.setForeground(Color.BLUE);
            m_jTitle.setForeground(list.getSelectionForeground());
            m_jDescription.setForeground(list.getSelectionForeground());
            m_jChairs.setForeground(list.getSelectionForeground());
            m_jPlaces.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            m_jTime.setForeground(list.getSelectionForeground());
            m_jTitle.setForeground(list.getForeground());
            m_jDescription.setForeground(list.getForeground());
            m_jChairs.setForeground(list.getForeground());
            m_jPlaces.setForeground(list.getForeground());
        }

        if (value == null) {
        	m_jTime.setText("");
            m_jTitle.setText("");
            m_jChairs.setText("");
            m_bDone = false;
            m_jDescription.setText("");
            m_jPlaces.setText("");
        } else {
            Object[] avalue = (Object []) value;
            
            m_bDone = ((Boolean) avalue[8]).booleanValue();
            Font font = m_jTime.getFont();
            Map attributes = font.getAttributes();
            if(m_bDone)
            {
            	attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            }
            else
            {
            	attributes.put(TextAttribute.STRIKETHROUGH, null);
            }
            Font newFont = new Font(attributes);
        	m_jTime.setFont(newFont);
            
            m_jTime.setText(String.format("%1$td.%1$tm.%1$ty %1$tH:%1$tM - %2$td.%2$tm.%2$ty %2$tH:%2$tM", avalue[2], avalue[10]));
            m_jTitle.setText(Formats.STRING.formatValue(avalue[6]));
            m_jChairs.setText(Formats.INT.formatValue(avalue[7]));
            m_jDescription.setText(Formats.STRING.formatValue(avalue[9]));
            m_jPlaces.setText(getPlaceNames(Formats.STRING.formatValue(avalue[11])));
            
            if((int)avalue[12] > 0)
            {
            	m_jPlaces.setBackground(Color.red);
            	m_jPlaces.setOpaque(true);
            }
            else
            {
            	m_jPlaces.setBackground(null);
            	m_jPlaces.setOpaque(false);
            }
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

        return this;
    }
    
    private String getPlaceNames(String placesString)
    {
    	if(placesString == null)
    		return null;
    	
    	StringBuilder sb = new StringBuilder();
    	for(String place : placesString.split(";"))
    	{
    		if(place != null && place.length() > 0)
    		{
    			if(sb.length()>0)
    				sb.append(", ");
    			String[] parts = place.split(",");
    			if(parts.length > 1)
    				sb.append(parts[1]);
    		}
    	}
    	
    	
    	return sb.toString(); 
    }
    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    
   /**
    * Overridden for performance reasons.
    */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jDescription = new javax.swing.JLabel();
        m_jTime = new javax.swing.JLabel();
        m_jTitle = new javax.swing.JLabel();
        m_jChairs = new javax.swing.JLabel();
        m_jPlaces = new javax.swing.JLabel();

		double size[][] =
            {{TableLayout.PREFERRED, 20, TableLayout.FILL},  // Columns
             {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }}; // Rows

        setLayout(new TableLayout(size));

        m_jTime.setForeground(new java.awt.Color(0, 0, 255));
        m_jTime.setText("01.01.16 00:00 - 01.01.16 00:00");
        add(m_jTime, new TableLayoutConstraints(0, 0, 0, 0, TableLayout.LEFT, TableLayout.CENTER));

        m_jChairs.setText("5");
        add(m_jChairs, new TableLayoutConstraints(2, 0, 2, 0, TableLayout.LEFT, TableLayout.CENTER));
        

        m_jTitle.setText("This is a test");
        m_jTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(m_jTitle, "0, 1, 2, 1");
        
        m_jPlaces.setText("Place 1, Place 2");
        m_jPlaces.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(m_jPlaces, "0, 2, 2, 2");
        
        
        
        m_jDescription.setText("<html>This is a test comment that shows how a long line is printed with this renderer.");
        m_jDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        m_jDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        add(m_jDescription, "0, 3, 2, 3");

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel m_jChairs;
    javax.swing.JLabel m_jTime;
    javax.swing.JLabel m_jDescription;
    javax.swing.JLabel m_jPlaces;
    javax.swing.JLabel m_jTitle;
    // End of variables declaration//GEN-END:variables
    
}
