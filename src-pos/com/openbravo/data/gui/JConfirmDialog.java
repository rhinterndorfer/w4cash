package com.openbravo.data.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
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

import java.awt.Font;
import com.openbravo.beans.DialogType;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.Log;
import com.openbravo.pos.util.PropertyUtil;

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
	// private JLabel m_jIcon;

	/**
	 * @wbp.parser.constructor
	 */
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
	private void init(AppView app, String message, DialogType dialogType) {
		getContentPane().setLayout(new BorderLayout());

		
		this.contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		java.awt.Dimension screenSize =
				java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		String w = "800";
		if(screenSize.getWidth() < 800 * 1.4) // 1.4 html size factor
		{
			w = String.format("%1$d", (int)(screenSize.getWidth() / 1.4));
		}
		
		this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/question.png")).getImage());
		
		contentPanel.setLayout(new FlowLayout());
		{
			//m_jIcon = new JLabel(
			//		new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/question.png")));
			
			// contentPanel.add(m_jIcon);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<html><div style='width: "+w+"px'>");
			sb.append(message);
			sb.append("</div></html>");
			
			m_jMessage = new JLabel(sb.toString());
			java.awt.Font f = m_jMessage.getFont();
			f = new java.awt.Font(f.getName(), f.getStyle(), (int)(32 / 1.4)); // set default size 32 (1.4 html size factor) 
			m_jMessage.setFont(f);
			
			
			contentPanel.add(m_jMessage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			switch (dialogType) {
			case Confirm:
				createButtonOk(buttonPane, true);
				createButtonCancel(buttonPane, false);
				break;
			case Error:
				createButtonCancel(buttonPane, true);
				break;
			case ConfirmError:
				createButtonOk(buttonPane, true);
				createButtonCancel(buttonPane, false);
				break;
			case Information:
				createButtonOk(buttonPane, true);
				break;
			}

		}

		getContentPane().add(contentPanel, BorderLayout.CENTER);

		ScaleButtons(app);

		
		setMaximumSize(screenSize);
		
		setModal(true);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	private void createButtonCancel(JPanel buttonPane, boolean useDefault) {
		this.cancelButton = new JButton(AppLocal.getIntString("Button.Cancel"));
		this.cancelButton.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png")));

		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		if (useDefault) {
			getRootPane().setDefaultButton(cancelButton);
		}
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog(CANCEL);
			}

		});
	}

	private void createButtonOk(JPanel buttonPane, boolean useDefault) {
		this.okButton = new JButton(OK2CANCEL + AppLocal.getIntString("Button.OK") + OK2CANCEL);
		this.okButton
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png")));
		buttonPane.add(okButton);
		if (useDefault) {
			getRootPane().setDefaultButton(okButton);
		}
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog(OK);
			}
		});
	}

	private void ScaleButtons(AppView app) {
		int width = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchlarge-width", "30"));
		int height = Integer
				.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-touchlarge-height", "30"));

		int fontsize = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-small-fontsize", "20"));

		PropertyUtil.ScaleLabelFontsize(app, m_jMessage, "common-small-fontsize", "32");
		// PropertyUtil.ScaleLabelIcon(app, m_jIcon, width, height);

		if (okButton != null) {
			PropertyUtil.ScaleButtonIcon(okButton, width, height, fontsize);
		}
		if (cancelButton != null) {
			PropertyUtil.ScaleButtonIcon(cancelButton, width, height, fontsize);
		}
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

	public static int showConfirm(AppView app, Component parent, String title, String message) {
		Window window = getWindow(parent);

		JConfirmDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new JConfirmDialog((Frame) window, true);
		} else {
			myMsg = new JConfirmDialog((Dialog) window, true);
		}

		myMsg.setTitle(title);
		myMsg.init(app, message, DialogType.Confirm);

		return myMsg.getReturnCode();
	}
	
	public static int showInformation(AppView app, Component parent, String title, String message) {
		Window window = getWindow(parent);

		JConfirmDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new JConfirmDialog((Frame) window, true);
		} else {
			myMsg = new JConfirmDialog((Dialog) window, true);
		}

		myMsg.setTitle(title);
		myMsg.init(app, message, DialogType.Information);

		return myMsg.getReturnCode();
	}

	public static int showError(AppView app, Component parent, String title, String message, Exception e,
			DialogType type) {
		if (e != null) {
			Log.Exception(e);
		}

		Window window = getWindow(parent);

		JConfirmDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new JConfirmDialog((Frame) window, true);
		} else {
			myMsg = new JConfirmDialog((Dialog) window, true);
		}

		myMsg.setTitle(title);
		myMsg.init(app, message, type);

		return myMsg.getReturnCode();
	}

	public static int showError(AppView app, Component parent, String title, String message, Exception e) {
		return showError(app, parent, title, message, e, DialogType.Error);
	}

	public static int showError(AppView app, Component parent, String title, String message) {
		return showError(app, parent, title, message, null, DialogType.Error);
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
