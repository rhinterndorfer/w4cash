package com.openbravo.data.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class JConfirmDialog extends JDialog {

	public final static int OK = JOptionPane.YES_OPTION;
	public final static int CANCEL = JOptionPane.CANCEL_OPTION;
	private final static String OK2CANCEL = "      ";

	private JPanel contentPanel;
	private JLabel m_jMessage;
	private JButton okButton;
	private JButton cancelButton;

	private int returnCode = -1;
	private JLabel m_jIcon;

	private JConfirmDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JMessageDialog */
	private JConfirmDialog(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	/**
	 * Create the dialog.
	 */
	private void init(AppView app, String message) {
		getContentPane().setLayout(new BorderLayout());

		this.contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPanel.setLayout(new FlowLayout());
		{
			m_jIcon = new JLabel(
					new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/question.png")));
			contentPanel.add(m_jIcon);
			m_jMessage = new JLabel(" " + message);
			contentPanel.add(m_jMessage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				this.okButton = new JButton(OK2CANCEL + AppLocal.getIntString("Button.OK") + OK2CANCEL);
				this.okButton.setIcon(
						new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png")));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						closeDialog(OK);
					}
				});
			}
			{
				this.cancelButton = new JButton(AppLocal.getIntString("Button.Cancel"));
				this.cancelButton.setIcon(new javax.swing.ImageIcon(
						getClass().getResource("/com/openbravo/images/locationbar_erase.png")));

				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						closeDialog(CANCEL);
					}

				});
			}
		}

		getContentPane().add(contentPanel, BorderLayout.CENTER);

		ScaleButtons(app);

		setVisible(true);
	}

	private void ScaleButtons(AppView app) {
		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchsmall-width", "30"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchsmall-height", "30"));

		int lblWidth = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchlarge-width", "30"));
		int lblHeight = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchlarge-height", "30"));

		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "20"));

		PropertyUtil.ScaleLabelFontsize(app, m_jMessage, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelIcon(app, m_jIcon, lblWidth, lblHeight);

		PropertyUtil.ScaleButtonIcon(okButton, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(cancelButton, btnWidth, btnHeight, fontsize);

		pack();

		PropertyUtil.ScaleDialog(app, this, getWidth(), getHeight() + 20);
	}

	private void closeDialog(int code) {
		this.returnCode = code;
		dispose();
	}

	public int getReturnCode() {
		return this.returnCode;
	}

	public static int showConfirm(AppView app, Component parent, String message, String title) {
		Window window = getWindow(parent);

		JConfirmDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new JConfirmDialog((Frame) window, true);
		} else {
			myMsg = new JConfirmDialog((Dialog) window, true);
		}

		myMsg.setTitle(title);
		myMsg.init(app, message);

		return myMsg.getReturnCode();
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
