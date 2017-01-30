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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;

import com.openbravo.format.Formats;
import com.openbravo.pos.instance.InstanceQuery;
import com.openbravo.pos.util.Log;

import at.w4cash.signature.SignatureModul;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.*;

/**
 *
 * @author adrianromero
 */
public class StartPOS {

	private Icon icon = new ImageIcon(getClass().getResource("/com/openbravo/images/wait.gif"));

	/** Creates a new instance of StartPOS */
	private StartPOS() {
	}

	public static boolean registerApp() {

		// vemos si existe alguna instancia
		InstanceQuery i = null;
		try {
			i = new InstanceQuery();
			i.getAppMessage().restoreWindow();
			return false;
		} catch (Exception e) {
			return true;
		}
	}


	public static void main(final String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				if (!registerApp()) {
					System.exit(1);
				}

				AppConfig config = new AppConfig(args);
				config.load();

				if(args != null && args.length >= 2)
				{
					// DEP export path
					try {
						
						String DEPExportPath = args[1];
						JRootApp rootapp = new JRootApp();
					
						if(rootapp.initApp(config, true))
						{
							if(SignatureModul.getInstance() != null && SignatureModul.getInstance().GetIsActive())
							{
								SignatureModul.getInstance().DEPExport(DEPExportPath);
							}
						}
						else
							Log.Exception("Init JRootApp for DEP export failed.");
					
					} catch(Exception ex)
					{
						Log.Exception(ex);
					}
					System.exit(0); // and exit
				}
				
				
				String screenmode = config.getProperty("machine.screenmode");

				// Set the look and feel.
				try {

					Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();

					if (laf instanceof LookAndFeel) {
						UIManager.setLookAndFeel((LookAndFeel) laf);

					} else if (laf instanceof SubstanceSkin) {
						SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
					}
				} catch (Exception e) {
					Log.Exception("Cannot set look and feel", e);
				}

				JRootGUI r = null;

				if ("fullscreen".equals(screenmode)) {
					r = new JRootKiosk();
				} else {
					r = new JRootFrame();
				}

				final JRootGUI root = r;
				final GuiWorker action = new StartPOS().new GuiWorker(r);
				
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						action.execute();
					}
					
				});
				t.start();
				
				// set Locale.
				String slang = config.getProperty("user.language");
				String scountry = config.getProperty("user.country");
				String svariant = config.getProperty("user.variant");
				if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
					Locale.setDefault(new Locale(slang, scountry, svariant));
				}

				// Set the format patterns
				Formats.setIntegerPattern(config.getProperty("format.integer"));
				Formats.setDoublePattern(config.getProperty("format.double"));
				Formats.setCurrencyPattern(config.getProperty("format.currency"));
				Formats.setPercentPattern(config.getProperty("format.percent"));
				Formats.setDatePattern(config.getProperty("format.date"));
				Formats.setTimePattern(config.getProperty("format.time"));
				Formats.setDateTimePattern(config.getProperty("format.datetime"));

				Boolean result;
				if ("fullscreen".equals(screenmode)) {
					result=((JRootKiosk) root).initFrame(config);
				} else {
					result=((JRootFrame) root).initFrame(config);
				}

				action.done();
				
				if(!result)
					System.exit(1);
			}
		});
	}

	class GuiWorker extends SwingWorker<Integer, Integer> {
		// public class ShowWaitAction extends AbstractAction {
		/**
		* 
		*/
		// private static final long serialVersionUID = -7964048200093662696L;

		protected static final long SLEEP_TIME = 30 * 1000;

		private JDialog dialog = new JDialog();
		private Component win = null;

		public GuiWorker(Component win) {
			this.win = win;

			dialog.setUndecorated(true);
			dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			JLabel label = new JLabel(icon);
			label.setSize(250, 250);
			dialog.setLayout(new FlowLayout());
			dialog.add(label);
			JLabel text = new JLabel("Lade w4cash");
			text.setFont(new Font("Tahoma", Font.BOLD, 24));
			text.setHorizontalAlignment(SwingConstants.CENTER);
			dialog.add(text);
			dialog.pack();

			dialog.dispose();
			dialog.setLocationRelativeTo(win);
			dialog.setSize(250, 280);
			dialog.setUndecorated(true);
			dialog.setVisible(true);
		}

		// mySwingWorker = new SwingWorker<Void, Void>() {
		@Override
		protected Integer doInBackground() throws Exception {

			Thread.sleep(SLEEP_TIME);
			return null;
		}
		@Override
		protected void done() {
			dialog.dispose();
		}

	}
}
