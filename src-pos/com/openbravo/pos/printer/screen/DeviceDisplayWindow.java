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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.printer.DeviceDisplay;
/**
 *
 * @author adrian
 */
public class DeviceDisplayWindow extends javax.swing.JFrame implements DeviceDisplay {

	private String m_sName;
	private DeviceDisplayPanel m_display;
	private Image backgroundImage;
	private Timer m_timer;
	private String backgroundImageFolderPath;
	private int backgroundImageSeconds;
	private HashSet<String> usedBackgroundImages;
	private AppView app;
	
	/** Creates new form DeviceDisplayWindow */
	public DeviceDisplayWindow(AppView app) {
		initComponents();
		
		this.app = app;
		m_sName = AppLocal.getIntString("Display.Window");
		
		backgroundImageFolderPath = app.getProperties().getProperty("Display.Window.BackgroundPath");
		
		backgroundImageSeconds = 60;
		try {
			backgroundImageSeconds = Integer.parseInt(app.getProperties().getProperty("Display.Window.Seconds"));
		} catch(Exception e){
			// do nothing
		}
		
		if(backgroundImageFolderPath != null)
		{
			usedBackgroundImages = new HashSet<String>();
			LoadNextBackgroundImage();
		}
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		if (gs.length > 1) {
			GraphicsConfiguration configuration = gs[1].getDefaultConfiguration();
			Rectangle rectNew = configuration.getBounds();
			Rectangle rectOld = this.getBounds();
			this.setBounds(rectNew);
		
			double zoom = 3.0 * (rectNew.getWidth() / rectOld.getWidth());
			m_display = new DeviceDisplayPanel(zoom);
		}
		else
			m_display = new DeviceDisplayPanel(3.0);

		
		m_background.add(m_display.getDisplayComponent());

		setVisible(true);
	}
	
	private void LoadNextBackgroundImage() {
		
		if(backgroundImageFolderPath != null)
		{
			Path backgroundImagePath = Paths.get(backgroundImageFolderPath);
			if(Files.exists(backgroundImagePath, LinkOption.NOFOLLOW_LINKS)) {
				if(m_timer == null) {
					m_timer = new javax.swing.Timer(backgroundImageSeconds * 1000, new TimerAction());
				}
				StopTimer();
				
				try {
					try (Stream<Path> stream = Files.list(backgroundImagePath)) {
						Integer retry = 2;
						do {
							Set<String> files = stream
									.filter(file -> !Files.isDirectory(file)
											&& !usedBackgroundImages.contains(file.getFileName().toString())
									)
									.map(Path::getFileName)
							        .map(Path::toString)
									.collect(Collectors.toSet());
							if(!files.isEmpty()) {
								String fileName = files.iterator().next();
								Path fullFileName = Paths.get(backgroundImageFolderPath, fileName);
								usedBackgroundImages.add(fileName);
								javax.swing.ImageIcon icon = new javax.swing.ImageIcon(fullFileName.toString());
								Image iconImage = icon.getImage().getScaledInstance(m_background.getWidth(), m_background.getHeight(), Image.SCALE_SMOOTH);
								
								m_background.setIcon(new javax.swing.ImageIcon(iconImage));
								retry = 0;
							} else {
								usedBackgroundImages.clear();
								retry--;
							}
						} while(retry > 0);
					}		
				} catch(Exception e) {
					// ignore error
				}
				StartTimer();
			}
		}
	}
	
	private void StopTimer() {
		if (m_timer != null && m_timer.isRunning())
			m_timer.stop();
	}

	private void StartTimer() {
		if (m_timer != null && !m_timer.isRunning())
			m_timer.restart();
	}

	public String getDisplayName() {
		return m_sName;
	}

	public String getDisplayDescription() {
		return null;
	}

	public JComponent getDisplayComponent() {
		return null;
	}

	public void writeVisor(int animation, String sLine1, String sLine2) {
		m_display.writeVisor(animation, sLine1, sLine2);
	}

	public void writeVisor(String sLine1, String sLine2) {
		m_display.writeVisor(sLine1, sLine2);
	}

	public void clearVisor() {
		m_display.clearVisor();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		//m_jContainer = new javax.swing.JPanel();
		m_background = new javax.swing.JLabel();

		setTitle(AppLocal.getIntString("Display.Window"));
		
		m_background.setLayout(new java.awt.BorderLayout());
		
		getContentPane().setLayout(new java.awt.BorderLayout());
		getContentPane().add(m_background, java.awt.BorderLayout.CENTER);
		  
		setSize(new java.awt.Dimension(767, 245));
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
	  }
	
	// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	//private javax.swing.JPanel m_jContainer;
	private javax.swing.JLabel m_background;
	// End of variables declaration//GEN-END:variables
	
	private class TimerAction implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			try {
				LoadNextBackgroundImage();
			} catch (Exception ex) {
				// do nothing
			}
		}
	}

}
