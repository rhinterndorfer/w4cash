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
import java.awt.Color;
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
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.*;

/**
 *
 * @author adrianromero
 */
public class LoadingIndicator extends SwingWorker<Integer, Integer> {

	private static Component m_owner;
	private Icon icon = new ImageIcon(getClass().getResource("/com/openbravo/images/wait.gif"));

	private JDialog dialog;
	private Runnable m_runnable;

	public LoadingIndicator(Component owner) {

		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());

		JLabel label = new JLabel(icon);
		label.setSize(256, 256);
		pan.add(label, BorderLayout.NORTH);

		dialog = new JDialog(getWindow(owner));
		dialog.setUndecorated(true);

		dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		dialog.setLocale(null);
		dialog.add(pan);
		dialog.pack();
		dialog.setLocationRelativeTo(owner);
		dialog.setVisible(true);
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return null;
		} else if (parent instanceof Frame) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

	public static void init(Component owner) {
		m_owner = owner;
	}

	public static LoadingIndicator start() {
		LoadingIndicator loadingIndicator = new LoadingIndicator(m_owner);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				loadingIndicator.execute();
			}

		});
		t.start();
		return loadingIndicator;
	}

	// mySwingWorker = new SwingWorker<Void, Void>() {
	@Override
	protected Integer doInBackground() throws Exception {
		for (int i = 0; i < 30; i++) {
			dialog.toFront();
			Thread.sleep(1000);
		}
		return null;
	}

	@Override
	protected void done() {
		dialog.dispose();
	}

}
