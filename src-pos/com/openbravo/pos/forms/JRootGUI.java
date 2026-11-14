package com.openbravo.pos.forms;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;


public abstract class JRootGUI extends javax.swing.JFrame {
	
	TrayIcon trayIcon;
    SystemTray tray;
    
    Boolean isServer = false;
	
	public Boolean initFrame(AppProperties app, CommandLine cmd) {
		isServer = cmd.hasOption('s');
		if(SystemTray.isSupported() && isServer){
			tray=SystemTray.getSystemTray();
			
			Image image = new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/money.png")).getImage();
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    System.exit(0);
                }
            };
            
            PopupMenu popup=new PopupMenu();
            MenuItem defaultItem=new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem=new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            trayIcon=new TrayIcon(image, "w4cash", popup);
            trayIcon.setImageAutoSize(true);
            
            addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent e) {
                    
                	if(e.getNewState()==ICONIFIED){
                        try {
                            tray.add(trayIcon);
                            setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to tray");
                        }
                    }
    		        
                    if(e.getNewState()==7){
    		            try{
    			            tray.add(trayIcon);
    			            setVisible(false);
    			            System.out.println("added to SystemTray");
    		            }catch(AWTException ex){
    		            	System.out.println("unable to add to system tray");
    		            }
    		        }
    		        
    		        if(e.getNewState()==MAXIMIZED_BOTH){
                        tray.remove(trayIcon);
                        setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                    
    		        if(e.getNewState()==NORMAL){
                        tray.remove(trayIcon);
                        setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                }
            });
            
            
            setExtendedState(JFrame.ICONIFIED);
            setExtendedState(getExtendedState() | JFrame.ICONIFIED);
		}
		
		return true;
	}
	
	protected abstract AppView getAppView();
}
