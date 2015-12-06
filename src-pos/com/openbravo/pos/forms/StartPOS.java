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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;

import com.openbravo.format.Formats;
import com.openbravo.pos.instance.InstanceQuery;

import java.awt.Dialog.ModalityType;
import java.awt.Point;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 *
 * @author adrianromero
 */
public class StartPOS {

	private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");

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

	// static BasicService basicService = null;
	//
	// public static boolean javaWebStart() {
	// try {
	// basicService = (BasicService)
	// ServiceManager.lookup("javax.jnlp.BasicService");
	//
	// URL url = new URL("http://www.hb-softsolution.com");
	// basicService.showDocument(url);
	// } catch (UnavailableServiceException e) {
	// System.err.println("Lookup failed: " + e);
	// return false;
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// return false;
	// }
	// return true;
	// }

	public static void main(final String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				if (!registerApp()) {
					System.exit(1);
				}

				doLicense();

				AppConfig config = new AppConfig(args);
				config.load();

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

				// Set the look and feel.
				try {

					Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();

					if (laf instanceof LookAndFeel) {
						UIManager.setLookAndFeel((LookAndFeel) laf);
					} else if (laf instanceof SubstanceSkin) {
						SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
					}
				} catch (Exception e) {
					logger.log(Level.WARNING, "Cannot set look and feel", e);
				}

				// boolean started = javaWebStart();
				// if (!started) {
				// return;
				// }

				String screenmode = config.getProperty("machine.screenmode");
				if ("fullscreen".equals(screenmode)) {
					JRootKiosk rootkiosk = new JRootKiosk();
					rootkiosk.initFrame(config);
				} else {
					JRootFrame rootframe = new JRootFrame();
					
					new StartPOS().new ShowWaitAction("Show Wait Dialog").actionPerformed(new ActionEvent(rootframe, 0, "") );
					rootframe.initFrame(config);
				}
			}

			private void doLicense() {
				File license = new File("w4cash.sha1");
				File hashing = new File("start.bat");

				// match localfile with uploaded file
				InputStream inTarget = null;
				InputStream inTargetSha1 = null;
				try {
					try {
						inTarget = new FileInputStream(hashing);
						inTargetSha1 = new FileInputStream(license);
						W4CashSha1 sha1 = new W4CashSha1();
						// validate
						boolean isSha1 = sha1.validateSha(inTarget, inTargetSha1,
								"" + W4CashSha1.readCreationDate(hashing));
						if (!isSha1) {
							throw new NoSuchAlgorithmException("License is not working!");
						}
					} catch (NoSuchAlgorithmException e) {
						throw new IOException(e);
					} finally {
						if (inTarget != null) {
							try {
								inTarget.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (inTargetSha1 != null) {
							try {
								inTargetSha1.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (IOException e) {
					// TODO: ERROR DIALOG
//					e.printStackTrace();
					final JDialog dialog = new JDialog(null, "W4CASH Lizenz.", ModalityType.APPLICATION_MODAL);
					JPanel panel = new JPanel(new BorderLayout());
					panel.add(new JLabel("Lizenz wurde nicht gefunden, w4cash schlieﬂt."), BorderLayout.PAGE_START);
					dialog.add(panel);
					dialog.pack();
					dialog.setLocation(new Point(100, 100));
					dialog.setVisible(true);
					
					System.exit(0);
				}
			}
		});
	}

	public class ShowWaitAction extends AbstractAction {
		/**
		* 
		*/
		private static final long serialVersionUID = -7964048200093662696L;

		protected static final long SLEEP_TIME = 3 * 1000;

		public ShowWaitAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent evt) {
			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {

					// mimic some long-running process here...
					Thread.sleep(SLEEP_TIME);
					return null;
				}
			};

			Window win = SwingUtilities.getWindowAncestor((JRootFrame) evt.getSource());
			final JDialog dialog = new JDialog(win, "W4CASH startet.", ModalityType.APPLICATION_MODAL);

			mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("state")) {
						if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
							dialog.dispose();
						}
					}
				}
			});
			mySwingWorker.execute();

			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(progressBar, BorderLayout.CENTER);
			panel.add(new JLabel("Bitte warten ......."), BorderLayout.PAGE_START);
			dialog.add(panel);
			dialog.pack();
			dialog.setLocationRelativeTo(win);
			dialog.setVisible(true);
		}
	}
}
