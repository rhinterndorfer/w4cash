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

package com.openbravo.pos.forms;

import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.pos.config.JFrmConfig;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.openbravo.pos.instance.AppMessage;
import com.openbravo.pos.instance.InstanceManager;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author  adrianromero
 */
public class JRootFrame extends JRootGUI implements AppMessage {
    
	private static final long serialVersionUID = 1L;
	// Gestor de que haya solo una instancia corriendo en cada maquina.
    private InstanceManager m_instmanager = null;
    
    private JRootApp m_rootapp;
    private AppProperties m_props;
    
    /** Creates new form JRootFrame */
    public JRootFrame() {
        
        initComponents();    
    }
    
    @Override
    public Boolean initFrame(AppProperties props) {        
        m_props = props;
             
        m_rootapp = new JRootApp();       
        
        if (m_rootapp.initApp(m_props, false)) {
        	if (!"true".equals(props.getProperty("machine.nouniqueinstance"))) {
                // Register the running application
                try {
                    m_instmanager = new InstanceManager();
                } catch (Exception e) {
					JConfirmDialog.showError(m_rootapp, 
							this, 
							AppLocal.getIntString("error.error"), 
							AppLocal.getIntString("message.singleinstance"));
                	return false;
                }
            }
        
            // Show the application
            add(m_rootapp, BorderLayout.CENTER);            
 
            try {
                this.setIconImage(ImageIO.read(JRootFrame.class.getResourceAsStream("/com/openbravo/images/favicon.png")));
            } catch (IOException e) {
            }   
            setTitle(AppLocal.APP_NAME + " - " + AppLocal.APP_VERSION);
            
            
            // pack();
            this.setSize(800, 600); // set size for normal window state
            this.setExtendedState(MAXIMIZED_BOTH);
            setLocationRelativeTo(null);        
            super.initFrame(props);
            setVisible(true);
            return true;
        }
        else
        {
        	return false;
        }
    }
    
    public void restoreWindow() throws RemoteException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (getExtendedState() == JFrame.ICONIFIED) {
                    setExtendedState(JFrame.NORMAL);
                }
                requestFocus();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    	
    	
    	int res = JConfirmDialog.showConfirm(m_rootapp, this,
				null,
				AppLocal.getIntString("message.closeApp"));
        if(res==JOptionPane.YES_OPTION){
        	m_rootapp.tryToClose();
        }
        
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        System.exit(0);
        
    }//GEN-LAST:event_formWindowClosed

	@Override
	protected AppView getAppView() {
		return m_rootapp;
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
