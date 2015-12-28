package com.openbravo.license;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hbsoft.w4cash.license.LicenseTool;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.GridLayout;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class JLicenseDialog extends JDialog {

	public final static int OK = JOptionPane.YES_OPTION;
	public final static int CANCEL = JOptionPane.CANCEL_OPTION;
	private int returnCode = CANCEL;

	private final JPanel contentPanel = new JPanel();
	private JTextField txt_license;
	private JLabel m_jName;

	private JLabel m_jKey;
	/**
	 * Launch the application.
	 */

	private JButton okButton;

	private String host;
	private String mac;
	private String m_LicenseUserKey;
	private String license;
	private JButton cancelButton;
	private JLabel lbl_Name;
	// private JLabel lbl_Mac;
	// private JLabel m_jMac;
	private JLabel lbl_UserSerial;
	private JTextField m_jSerial;

	public static JLicenseDialog showDialog(AppView app, Component parent, String message, String title) {
		Window window = getWindow(parent);

		JLicenseDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new JLicenseDialog((Frame) window, true);
		} else {
			myMsg = new JLicenseDialog((Dialog) window, true);
		}
		myMsg.init(app);
		// myMsg.setTitle(title);
		// myMsg.init();

		return myMsg;
	}

	/**
	 * Create the dialog.
	 * 
	 * @wbp.parser.constructor
	 */
	private JLicenseDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
//		init(null);
	}

	/** Creates new form JMessageDialog */
	private JLicenseDialog(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);

	}

	public int getReturnCode() {
		return this.returnCode;
	}

	public String getLicense() {
		return this.license;
	}

	private void closeDialog(int code, String license) {
		this.returnCode = code;
		this.license = license;
		closeDialog();
	}

	private void closeDialog() {
		dispose();
	}

	private void init(AppView app) {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		// gbl_contentPanel.columnWidths = new int[] { 211, 211, 0 };
		// gbl_contentPanel.rowHeights = new int[] { 52, 52, 52, 52, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0 };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		contentPanel.setLayout(gbl_contentPanel);
		{
			this.lbl_Name = new JLabel("Kassenname");
			GridBagConstraints gbc_lbl_name = new GridBagConstraints();
			gbc_lbl_name.anchor = GridBagConstraints.EAST;
			gbc_lbl_name.fill = GridBagConstraints.BOTH;
			gbc_lbl_name.insets = new Insets(0, 0, 5, 5);
			gbc_lbl_name.gridx = 0;
			gbc_lbl_name.gridy = 0;
			contentPanel.add(lbl_Name, gbc_lbl_name);
		}
		{
			this.m_jName = new JLabel("Name");
			GridBagConstraints gbc_lblName = new GridBagConstraints();
			gbc_lblName.insets = new Insets(0, 0, 5, 0);
			gbc_lblName.gridx = 1;
			gbc_lblName.gridy = 0;
			contentPanel.add(m_jName, gbc_lblName);
		}
		// {
		// this.lbl_Mac = new JLabel("MAC");
		// GridBagConstraints gbc_lblMac = new GridBagConstraints();
		// gbc_lblMac.insets = new Insets(0, 0, 5, 5);
		// gbc_lblMac.gridx = 0;
		// gbc_lblMac.gridy = 1;
		// contentPanel.add(lbl_Mac, gbc_lblMac);
		// }
		// {
		// this.m_jMac = new JLabel("");
		// GridBagConstraints gbc_m_jMac = new GridBagConstraints();
		// gbc_m_jMac.insets = new Insets(0, 0, 5, 0);
		// gbc_m_jMac.gridx = 1;
		// gbc_m_jMac.gridy = 1;
		// contentPanel.add(m_jMac, gbc_m_jMac);
		// }

		{
			this.lbl_UserSerial = new JLabel("Serial");
			GridBagConstraints gbc_lblMac = new GridBagConstraints();
			gbc_lblMac.anchor = GridBagConstraints.EAST;
			gbc_lblMac.insets = new Insets(0, 0, 5, 5);
			gbc_lblMac.gridx = 0;
			gbc_lblMac.gridy = 1;
			contentPanel.add(lbl_UserSerial, gbc_lblMac);
		}
		{
			this.m_jSerial = new JTextField();
			this.m_jSerial.setBorder(null);
			this.m_jSerial.setEditable(false);

			GridBagConstraints gbc_m_jMac = new GridBagConstraints();
			gbc_m_jMac.insets = new Insets(0, 0, 5, 0);
			gbc_m_jMac.gridx = 1;
			gbc_m_jMac.gridy = 1;
			contentPanel.add(m_jSerial, gbc_m_jMac);
		}

		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 2;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			{
				txt_license = new JTextField();
				panel.add(txt_license);

				// txt_lic1.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				this.okButton = new JButton("OK");
				this.okButton.setIcon(
						new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png")));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						closeDialog(OK, txt_license.getText());
					}
				});
				getRootPane().setDefaultButton(okButton);
				okButton.setEnabled(false);
			}
			{
				this.cancelButton = new JButton("Cancel");
				this.cancelButton.setIcon(
						new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png")));

				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						closeDialog(CANCEL, txt_license.getText());
					}
				});
				buttonPane.add(cancelButton);
			}

			txt_license.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					validateSerialKey();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					validateSerialKey();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					validateSerialKey();
				}
			});
		}

		fill(app);

		ScaleButtons(app);

		pack();

		PropertyUtil.ScaleDialog(app, this, 550, 300);

		setVisible(true);
	}

	private void ScaleButtons(AppView app) {
		PropertyUtil.ScaleLabelFontsize(app, lbl_Name, "common-dialog-fontsize", "22");
		// PropertyUtil.ScaleLabelFontsize(app, lbl_Mac,
		// "common-dialog-fontsize", "22");
		PropertyUtil.ScaleLabelFontsize(app, lbl_UserSerial, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleLabelFontsize(app, m_jName, "common-dialog-fontsize", "22");
		// PropertyUtil.ScaleLabelFontsize(app, m_jMac,
		// "common-dialog-fontsize", "22");
		PropertyUtil.ScaleTextFieldFontsize(app, m_jSerial, "common-dialog-fontsize", "22");

		PropertyUtil.ScaleTextFieldFontsize(app, txt_license, "common-dialog-fontsize", "22");

		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchsmall-width", "48"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchsmall-height", "48"));
		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(okButton, menuwidth, menuheight, fontsize);
		PropertyUtil.ScaleButtonIcon(cancelButton, menuwidth, menuheight, fontsize);
	}

	private void validateSerialKey() {
		String text = txt_license.getText();

		String license2match = LicenseTool.genLicenseApplication(m_LicenseUserKey, LicenseTool.DEFAULT_DELIMITER);

		if (text != null && text.equals(license2match)) {
			this.okButton.setEnabled(true);
		} else {
			this.okButton.setEnabled(false);
		}

	}

	private void fill(AppView app) {
		this.host = app.getProperties().getHost();
		this.mac = LicenseManager.getMAC();
		String[] key = LicenseTool.genLicenseUser(host, mac);
		this.m_LicenseUserKey = LicenseTool.formatKey(key, LicenseTool.DEFAULT_DELIMITER);

		m_jName.setText(host);
		m_jSerial.setText(m_LicenseUserKey);
		// m_jKey.setText(serialKey);
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		} else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window) parent;
		} else {
			return getWindow(parent.getParent());
		}
	}

}
