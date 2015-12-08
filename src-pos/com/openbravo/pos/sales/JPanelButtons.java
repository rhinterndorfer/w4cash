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

import com.openbravo.data.loader.LocalRes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.payment.PaymentPanelType;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.ThumbNailBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JPanelButtons extends javax.swing.JPanel {

    private static Logger logger = Logger.getLogger("com.openbravo.pos.sales.JPanelButtons");

    private static SAXParser m_sp = null;
    
    private Properties props;
    private Properties posprops;
    private Map<String, String> events;
    
    private ThumbNailBuilder tnbmacro;
    
    private JPanelTicket panelticket;
    
    /** Creates new form JPanelButtons */
    public JPanelButtons(String sConfigKey, JPanelTicket panelticket) {
        initComponents();
        
        // Load categories default thumbnail
        tnbmacro = new ThumbNailBuilder(100, 100, "com/openbravo/images/greenled.png");
        
        this.panelticket = panelticket;
        posprops = panelticket.dlSystem.getResourceAsProperties(panelticket.m_App.getProperties().getHost() + "/properties");
        
        props = new Properties();
        events = new HashMap<String, String>();
        
        String sConfigRes = panelticket.getResourceAsXML(sConfigKey);
        
        if (sConfigRes != null) {
            try {
                if (m_sp == null) {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    m_sp = spf.newSAXParser();
                }
                m_sp.parse(new InputSource(new StringReader(sConfigRes)), new ConfigurationHandler());

            } catch (ParserConfigurationException ePC) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.parserconfig"), ePC);
            } catch (SAXException eSAX) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.xmlfile"), eSAX);
            } catch (IOException eIO) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.iofile"), eIO);
            }
        }     
        
        
        int fontsize = Integer
				.parseInt(getProperty("button-small-fontsize", "16"));
        for(int i=0;i < this.getComponents().length; i++)
        {
        	Component c = this.getComponents()[i];
        
        	if(JButton.class.isAssignableFrom(c.getClass()))
        	{
        		PropertyUtil.ScaleButtonIcon(JButton.class.cast(c), 
        				Integer.parseInt(getProperty("button-touchlarge-width", "60")), 
        				Integer.parseInt(getProperty("button-touchlarge-height", "60")), fontsize);
        	}
        }
    }
    

    
    public void setPermissions(AppUser user) {
        for (Component c : this.getComponents()) {
            String sKey = c.getName();
            if (sKey == null || sKey.equals("")) {
                c.setEnabled(true);
            } else {
                c.setEnabled(user.hasPermission(c.getName()));
            }
        }
    }
    
    public String getProperty(String key) {
        if(posprops.getProperty(key) != null)
        	return posprops.getProperty(key);
    	
    	return props.getProperty(key);
    }
    
     public String getProperty(String key, String defaultvalue) {
         if(posprops.getProperty(key) != null)
         	return posprops.getProperty(key);
         
    	 return props.getProperty(key, defaultvalue);
    }
     
    public String getEvent(String key) {
        return events.get(key);
    }
    
    private class ConfigurationHandler extends DefaultHandler {     
        @Override
        public void startDocument() throws SAXException {}
        @Override
        public void endDocument() throws SAXException {}    
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        	if ("button".equals(qName)){
                
                
                // The button title text
                String titlekey = attributes.getValue("titlekey");
                if (titlekey == null) {
                    titlekey = attributes.getValue("name");
                }
                String title = titlekey == null
                        ? attributes.getValue("title")
                        : AppLocal.getIntString(titlekey);
                
                // adding the button to the panel
                JButton btn = new JButtonFunc(attributes.getValue("key"), 
                        attributes.getValue("image"), 
                        title);
                
               
                 // The template resource or the code resource
                final String template = attributes.getValue("template");
                if (template == null) {
                    final String code = attributes.getValue("code");
                    btn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            panelticket.evalScriptAndRefresh(code);
                        }
                    });
                } else {
                    btn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            panelticket.printTicket(template);
                        }
                    });     
                }
                add(btn);
                
            } else if ("event".equals(qName)) {
                events.put(attributes.getValue("key"), attributes.getValue("code"));
            } else {
                String value = attributes.getValue("value");
                
                // check if property has been overwritten for current POS
                String valuePOS = panelticket.m_App.getProperties().getProperty(qName);
                
                if (value != null || valuePOS != null) {                  
                    props.setProperty(qName, valuePOS != null ? valuePOS : value);
                }
            }
            

        }      
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}
    }  
        
    private class JButtonFunc extends JButton {
       
        public JButtonFunc(String sKey, String sImage, String title) {
            
            setName(sKey);
            if(sImage == null)
            	setText(title);
            
            java.net.URL resourceURL = getClass().getResource("/com/openbravo/images/" + sImage);
            if(resourceURL == null)
            	setIcon(new ImageIcon(tnbmacro.getThumbNail(panelticket.getResourceAsImage(sImage))));
            else
            	setIcon(new javax.swing.ImageIcon(resourceURL));
            
            setFocusPainted(false);
            setFocusable(false);
            setRequestFocusEnabled(false);
            setMargin(new Insets(0, 4, 0, 0));  
        }         
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
