package com.openbravo.pos.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.TabbedPaneUI;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdesktop.swingx.JXTaskPane;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.editor.JEditorCurrency;
import com.openbravo.editor.JEditorNumber;
import com.openbravo.editor.JEditorString;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPrincipalApp;

public class PropertyUtil {

	private static Logger logger = Logger.getLogger("com.openbravo.pos.util.PropertyUtil");

	private static Graphics2D m_TempGraphics;

	private static Graphics2D getTempGraphics() {
		if (m_TempGraphics == null) {
			BufferedImage imgtext = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
			m_TempGraphics = imgtext.createGraphics();
		}
		return m_TempGraphics;
	}

	public static int getGraphicsFontHeight(Font f) {
		return getTempGraphics().getFontMetrics(f).getHeight();
	}

	public static int getGraphicsFontStringWidth(Font f, String str) {
		return getTempGraphics().getFontMetrics(f).stringWidth(str);
	}

	public static double getGraphicsFontWidthAvg(Font f) {
		int[] widths = getTempGraphics().getFontMetrics(f).getWidths();
		int sum = 0;
		for (int d : widths)
			sum += d;

		return 1.0d * sum / widths.length;
	}

	public static void fillSortOrderIfNeeded(BrowsableEditableData bd, int sortColumnIndex) {
		boolean hasChanged = true;
		ListModel model = bd.getListModel();
		for (int i = 0; i < model.getSize(); i++) {
			Object[] element = (Object[]) model.getElementAt(i);

			if (element[sortColumnIndex] != null) {
				hasChanged = false;
				break;
			}
			element[sortColumnIndex] = i;
		}

		if (!hasChanged) {
			return;
		}

		try {
			bd.saveDataSortOrder();
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	public static void fillSortOrder(BrowsableEditableData bd, int sortColumnIndex) {
		boolean hasChanged = true;
		ListModel model = bd.getListModel();
		for (int i = 0; i < model.getSize(); i++) {
			Object[] element = (Object[]) model.getElementAt(i);

			if (element[sortColumnIndex] != null) {
				hasChanged = false;
				// break;
			}
			element[sortColumnIndex] = i;
		}

		// if (!hasChanged) {
		// return;
		// }

		try {
			bd.saveDataSortOrder();
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	public static String GetCategoriesFilter(AppView app) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", "Categories.Filter");
		return value;
	}

	public static String GetCloseMoneyProductsFilter(AppView app) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", "CloseMoney.Products.Filter");
		return value;
	}
	
	public static void ScaleBorderFontsize(AppView app, TitledBorder border, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			border.setTitlePosition(TitledBorder.ABOVE_TOP);
			int fontsize = Integer.parseInt(value);
			Font fontTotalEuros = border.getTitleFont();
			border.setTitleFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleButtonIcon(javax.swing.JButton btn, int width, int height, int fontsize) {
		btn.setMargin(null);

		int iconWidth = width;
		int iconHeigth = height;

		if (btn.getText() != null && !btn.getText().isEmpty()) {
			// String text = btn.getText();
			Font font = btn.getFont();
			btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));

			FontMetrics fm = btn.getFontMetrics(font);
			// newWidth += fm.stringWidth(text);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			iconWidth -= fm.getHeight();
			iconHeigth -= fm.getHeight();
		}

		if (btn.getIcon() != null && javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? iconWidth : -1, radio > 1 ? -1 : iconHeigth,
					Image.SCALE_SMOOTH);
			btn.setIcon(new javax.swing.ImageIcon(img));
		}

		btn.setSize(width, height);
		// btn.setMinimumSize(new Dimension(newWidth, newHeight));
		// btn.setPreferredSize(new Dimension(width, newHeight));
	}

	public static void ScaleButtonIcon(javax.swing.JButton btn, int width, int height, int align, int fontsize) {
		int newWidth = width;
		int newHeight = height;
		btn.setMargin(null);
		if (btn.getIcon() != null && javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1, radio > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			btn.setIcon(new javax.swing.ImageIcon(img));
		}

		if (btn.getText() != null && !btn.getText().isEmpty()) {
			String text = btn.getText();
			Font font = btn.getFont();
			btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			FontMetrics fm = btn.getFontMetrics(font);
			// newWidth += fm.stringWidth(text);
			btn.setHorizontalTextPosition(align);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			newWidth += fm.getHeight();
			newHeight += fm.getHeight();
		}

		btn.setSize(newWidth, newHeight);
		// btn.setMinimumSize(new Dimension(newWidth, newHeight));
		// btn.setPreferredSize(new Dimension(newWidth, newHeight));
	}

	public static void ScaleButtonIcon(JToggleButton btn, int width, int height, int fontsize) {
		btn.setMargin(null);

		int iconWidth = width;
		int iconHeigth = height;

		if (btn.getText() != null && !btn.getText().isEmpty()) {
			// String text = btn.getText();
			Font font = btn.getFont();
			btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));

			FontMetrics fm = btn.getFontMetrics(font);
			// newWidth += fm.stringWidth(text);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			iconWidth -= fm.getHeight();
			iconHeigth -= fm.getHeight();
		}

		if (btn.getIcon() != null && javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? iconWidth : -1, radio > 1 ? -1 : iconHeigth,
					Image.SCALE_SMOOTH);
			btn.setIcon(new javax.swing.ImageIcon(img));
		}
		if (btn.getSelectedIcon() != null
				&& javax.swing.ImageIcon.class.isAssignableFrom(btn.getSelectedIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getSelectedIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? iconWidth : -1, radio > 1 ? -1 : iconHeigth,
					Image.SCALE_SMOOTH);
			btn.setSelectedIcon(new javax.swing.ImageIcon(img));
		}

		btn.setSize(width, height);

		// int newWidth = width;
		// int newHeight = height;
		// btn.setMargin(null);
		// if (btn.getIcon() != null &&
		// javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass()))
		// {
		// javax.swing.ImageIcon icon =
		// javax.swing.ImageIcon.class.cast(btn.getIcon());
		// double radio = icon.getIconWidth() / icon.getIconWidth();
		// Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1,
		// radio > 1 ? -1 : height,
		// Image.SCALE_SMOOTH);
		// btn.setIcon(new javax.swing.ImageIcon(img));
		// }
		// if (btn.getSelectedIcon() != null
		// &&
		// javax.swing.ImageIcon.class.isAssignableFrom(btn.getSelectedIcon().getClass()))
		// {
		// javax.swing.ImageIcon selectionicon =
		// javax.swing.ImageIcon.class.cast(btn.getSelectedIcon());
		// double radio2 = selectionicon.getIconWidth() /
		// selectionicon.getIconWidth();
		// Image img2 = selectionicon.getImage().getScaledInstance(radio2 > 1 ?
		// width : -1, radio2 > 1 ? -1 : height,
		// Image.SCALE_SMOOTH);
		// btn.setSelectedIcon(new javax.swing.ImageIcon(img2));
		// }
		//
		// if (btn.getText() != null && !btn.getText().isEmpty()) {
		// String text = btn.getText();
		// Font font = btn.getFont();
		// btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));
		// FontMetrics fm = btn.getFontMetrics(font);
		// // newWidth += fm.stringWidth(text);
		// btn.setHorizontalTextPosition(JButton.CENTER);
		// btn.setVerticalTextPosition(JButton.BOTTOM);
		// newWidth += fm.getHeight();
		// newHeight += fm.getHeight();
		// }
		//
		// btn.setSize(newWidth, newHeight);
		// btn.setMinimumSize(new Dimension(newWidth, newHeight));
		// btn.setPreferredSize(new Dimension(width, newHeight));
	}

	public static void ScaleComponentFontsize(AppView app, Component compo, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = compo.getFont();
			compo.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			compo.setSize((int) compo.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleComboFontsize(AppView app, JComboBox combo, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = combo.getFont();
			combo.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			combo.setSize((int) combo.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleComboFontsizePrefered(AppView app, JComboBox label, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = label.getFont();
			label.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			label.setSize((int) label.getSize().getWidth(), fontsize);
			label.setMaximumSize(new java.awt.Dimension((int) 100, fontsize));
			label.setMinimumSize(new java.awt.Dimension((int) label.getSize().getWidth(), fontsize));
			label.setPreferredSize(new java.awt.Dimension((int) label.getSize().getWidth(), fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}
	
	public static void ScaleListFontsize(AppView app, JList list, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font font = list.getFont();
			list.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			list.setSize((int) list.getSize().getWidth(), fontsize);
			
			
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleDialog(AppView app, JDialog dialog, int width, int height) {
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		if(width > screenSize.width)
			width = screenSize.width;
		if(height > screenSize.height)
			height = screenSize.height;
		
		dialog.setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
	}

	public static void ScaleDialogFullScreen(AppView app, JDialog dialog) {
		Window win = getWindow((Component) dialog);

		if (win != null) {
			Insets insets = win.getInsets();
			dialog.setBounds(win.getX() + insets.left, win.getY() + insets.top,
					win.getWidth() - insets.left - insets.right, win.getHeight() - insets.top - insets.left);
			dialog.setPreferredSize(new Dimension(win.getWidth() - insets.left - insets.right,
					win.getHeight() - insets.top - insets.left));
		} else {
			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			dialog.setBounds(5, 5, screenSize.width, screenSize.height);
			dialog.setPreferredSize(screenSize);
		}

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

	public static void ScaleEditcurrencyFontsize(AppView app, JEditorCurrency label, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);
			Font font = label.getBFont();
			label.setBFont(new Font(font.getName(), font.getStyle(), fontsize));
			label.setBSize((int) label.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleEditstringFontsize(AppView app, JEditorString label, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font font = label.getBFont();
			label.setBFont(new Font(font.getName(), font.getStyle(), fontsize));
			label.setBSize((int) label.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleEditnumbersFontsize(AppView app, JEditorNumber unit, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font font = unit.getBFont();
			unit.setBFont(new Font(font.getName(), font.getStyle(), fontsize));
			unit.setBSize((int) unit.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleLabelIcon(AppView app, JLabel label, int width, int height) {
		if (label.getIcon() != null && javax.swing.ImageIcon.class.isAssignableFrom(label.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(label.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1, radio > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			label.setIcon(new javax.swing.ImageIcon(img));
		}
	}

	public static void ScaleJXTaskPaneFontSize(AppView app, JXTaskPane pane, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = pane.getFont();
			pane.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			pane.setSize((int) pane.getSize().getWidth(), fontsize);
			// label.setPreferredSize(new Dimension((int)
			// label.getSize().getWidth(), fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleLabelFontsize(AppView app, JLabel label, String key, String defaultValue) {
		ScaleLabelFontsize(app, label, key, defaultValue, 1.0);
	}

	public static void ScaleLabelFontsize(AppView app, JLabel label, String key, String defaultValue,
			Double scaleFactor) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = label.getFont();
			int newfontsize = (int) (fontsize * scaleFactor);
			label.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), newfontsize));
			label.setSize((int) label.getSize().getWidth(), newfontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static Font ScaleFont(AppView app, Font font, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);
			return new Font(font.getName(), font.getStyle(), fontsize);

		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return font;
	}

	public static void ScaleLabelFontsizePrefered(AppView app, JLabel label, String key, String defaultValue,
			int preferedWidth) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = label.getFont();
			label.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			label.setSize((int) label.getSize().getWidth(), fontsize);
			// label.setMaximumSize(new java.awt.Dimension((int) 100,
			// fontsize));
			label.setMinimumSize(new java.awt.Dimension((int) preferedWidth, fontsize));
			label.setPreferredSize(new java.awt.Dimension((int) preferedWidth, fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleLabelFontsizePrefered(AppView app, JLabel label, String key, String defaultValue) {
		ScaleLabelFontsizePrefered(app, label, key, defaultValue, 100);
	}

	public static void ScaleRadiobuttonFontsize(AppView app, JRadioButton btn, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = btn.getFont();
			btn.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			btn.setSize((int) btn.getSize().getWidth(), fontsize);
			// label.setMaximumSize(new java.awt.Dimension((int) 100,
			// fontsize));
			// btn.setMinimumSize(new java.awt.Dimension((int) 100, fontsize));
			// btn.setPreferredSize(new java.awt.Dimension((int) 100,
			// fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleSpinnerFontsize(AppView app, JSpinner spinner, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = spinner.getFont();
			spinner.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			spinner.setSize((int) spinner.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleCheckboxSize(AppView app, JCheckBox checkbox, String key, String defaultValue) {
		// Icon oldIcon = checkbox.getIcon();
		// Image oldImage = oldIcon.getImage();
		// Image newImage = oldImage.getScaledInstance(newWidth, newHeight,
		// Image.SCALE_DEFAULT);
		// Icon newIcon = new ImageIcon(newImage);
		// checkbox.setIcon(newIcon);

		// large
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = checkbox.getFont();
			checkbox.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			// checkbox.setSize((int) checkbox.getSize().getWidth(), fontsize);
			// checkbox.set(new Dimension(fontsize, fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		// checkbox.setPreferredSize(new Dimension());
	}

	public static void ScaleSpinnerScrollbar(AppView m_App, JSpinner m_jX, String string, String string2) {

	}

	public static void ScaleIconImage(AppView app, ImageIcon image, String widthKey, String heightKey,
			String defaultWidth, String defaultHeight) {
		// new ImageIcon().getImage().getScaledInstance(120, 120,
		// java.awt.Image.SCALE_SMOOTH);
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");

		int width = Integer.parseInt(defaultWidth);
		int height = Integer.parseInt(defaultHeight);
		try {
			width = Integer.parseInt(getProperty(app, dlSystem, "Ticket.Buttons", widthKey));
		} catch (IllegalArgumentException ex) {
			width = 22;
		} catch (NullPointerException e) {
			width = 22;
		}
		try {
			height = Integer.parseInt(getProperty(app, dlSystem, "Ticket.Buttons", heightKey));
		} catch (IllegalArgumentException ex) {
			height = 22;
		} catch (NullPointerException e) {
			height = 22;
		}

		image.setImage(image.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT));
	}

	public static void ScaleTextFieldFontsize(AppView app, JTextField text, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = text.getFont();
			text.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			text.setSize((int) text.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static int ScaleTextAreaFontsize(AppView app, JTextArea text, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = text.getFont();
			text.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			text.setSize((int) text.getSize().getWidth(), fontsize);
			return fontsize;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return 25;
	}

	public static void ScaleTextFieldFontsizePrefered(AppView app, JTextField text, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);
			Font fontTotalEuros = text.getFont();
			text.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			text.setSize((int) text.getSize().getWidth(), fontsize);
			text.setMaximumSize(new java.awt.Dimension((int) 100, fontsize));
			text.setMinimumSize(new java.awt.Dimension((int) text.getSize().getWidth(), fontsize));
			text.setPreferredSize(new java.awt.Dimension((int) text.getSize().getWidth(), fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleTabbedPaneFontsize(AppView app, JTabbedPane pane, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int size = Integer.parseInt(value);
			Font font = pane.getFont();
			pane.setFont(new Font(font.getName(), font.getStyle(), size));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleTableColumnFontsize(AppView app, JTable table, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int size = Integer.parseInt(value);
			Font font = table.getTableHeader().getFont();
			table.getTableHeader().setFont(new Font(font.getName(), font.getStyle(), size));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static int GetTableRowHeight(AppView app, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}

		try {
			int size = Integer.parseInt(value);
			return size;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return 25;
	}

	public static void ScaleTableRowheight(AppView app, final JTable table, int row, int multiply, String key,
			String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int size = Integer.parseInt(value);
			table.setRowHeight(row, size * multiply);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleTableLabelFontsize(AppView app, final JLabel label, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontSizeToUse = Integer.parseInt(value);
			label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), fontSizeToUse));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleButtonFontsize(AppView app, JButton button, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);

			Font fontTotalEuros = button.getFont();
			button.setFont(new Font(fontTotalEuros.getName(), fontTotalEuros.getStyle(), fontsize));
			button.setSize((int) button.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static double ScaleButtonFontsize(JButton button, int fontSize) {
		Font fontLabel = button.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();

		button.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
		button.setSize((int) (button.getSize().getWidth() * scaleFactor), fontSize);

		return scaleFactor;
	}

	public static double ScaleLabelFontsize(JLabel label, int fontSize) {
		Font fontLabel = label.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();

		label.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
		if (label.getSize().getWidth() > 0)
			label.setSize((int) (label.getSize().getWidth()), fontSize);
		else
			label.setPreferredSize(new Dimension((int) (label.getPreferredSize().getWidth()), fontSize));

		return scaleFactor;
	}

	public static double ScaleLabelFontsizeAndDimension(JLabel label, int fontSize) {
		Font fontLabel = label.getFont();
		Font newFont = new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize);

		double scaleFactor = fontSize / fontLabel.getSize();
		double scaleFactorWidth = getGraphicsFontWidthAvg(newFont) / getGraphicsFontWidthAvg(fontLabel);

		label.setFont(newFont);
		if (label.getSize().getWidth() > 0)
			label.setSize((int) (label.getSize().getWidth() * scaleFactorWidth), fontSize);
		else
			label.setPreferredSize(
					new Dimension((int) (label.getPreferredSize().getWidth() * scaleFactorWidth), fontSize));
		return scaleFactor;
	}

	public static double ScaleTextFieldFontsize(JTextField textfield, int fontSize) {
		Font fontLabel = textfield.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();
		Font newFont = new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize);

		textfield.setFont(newFont);

		int height = getTempGraphics().getFontMetrics(newFont).getHeight();

		if (textfield.getSize().getWidth() > 0)
			textfield.setSize((int) (textfield.getSize().getWidth() * scaleFactor), height + 4);
		if (textfield.getPreferredSize().getWidth() > 0)
			textfield.setPreferredSize(
					new Dimension((int) (textfield.getPreferredSize().getWidth() * scaleFactor), height + 4));

		return scaleFactor;
	}

	public static double ScaleComboboxFontSize(JComboBox combobox, int fontSize) {
		Font fontLabel = combobox.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();
		combobox.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
		combobox.setSize((int) (combobox.getSize().getWidth() * scaleFactor), fontSize);

		return scaleFactor;
	}

	public static void ScaleComboboxFontSize(AppView app, JComboBox combobox, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontSize = Integer.parseInt(value);
			Font fontLabel = combobox.getFont();
			double scaleFactor = fontSize / fontLabel.getSize();
			combobox.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
			combobox.setSize((int) (combobox.getSize().getWidth() * scaleFactor), fontSize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static String getProperty(AppView app, String sProperty, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, sProperty, key);
		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	private static String getProperty(AppView app, DataLogicSystem dlSystem, String sProperty, String key) {
		Properties property = dlSystem.getResourceAsProperties(app.getProperties().getHost() + "/properties");

		if (property.getProperty(key) != null) {
			return property.getProperty(key);
		}

		if ("".equals(sProperty)) {
			return null;
		}

		try {
			String xmlProp = dlSystem.getResourceAsXML(sProperty);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			ConfigurationHandler handler = new ConfigurationHandler(app);
			sp.parse(new InputSource(new StringReader(xmlProp)), handler);
			property = handler.getProperties();
		} catch (ParserConfigurationException ePC) {
			logger.log(Level.WARNING, LocalRes.getIntString("exception.parserconfig"), ePC);
		} catch (SAXException eSAX) {
			logger.log(Level.WARNING, LocalRes.getIntString("exception.xmlfile"), eSAX);
		} catch (IOException eIO) {
			logger.log(Level.WARNING, LocalRes.getIntString("exception.iofile"), eIO);
		}

		// dlSystem.getResourceAsProperties(sProperty);

		// if (property != null && property.isEmpty()) {

		// if (sProperty != null) {
		// // try {
		// String xmlProp = dlSystem.getResourceAsXML(sProperty);
		//
		// SAXParserFactory spf = SAXParserFactory.newInstance();
		// SAXParser sp = spf.newSAXParser();
		//
		// ConfigurationHandler handler = new ConfigurationHandler(app);
		// sp.parse(new InputSource(new StringReader(xmlProp)), handler);
		// property = handler.getProperties();
		//
		// }

		// }

		if (property == null)

		{
			throw new IllegalArgumentException("property -" + sProperty + " not set!");
		}

		return property.getProperty(key);
	}

	static class ConfigurationHandler extends DefaultHandler {

		private AppView m_App;
		// private Map<String, String> events;
		private Properties props;

		public ConfigurationHandler(AppView app) {
			this.m_App = app;

			// this.events = new HashMap<>();
			this.props = new Properties();
		}

		public Properties getProperties() {
			return this.props;
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if ("button".equals(qName)) {

				// The button title text
				String titlekey = attributes.getValue("titlekey");
				if (titlekey == null) {
					titlekey = attributes.getValue("name");
				}
				String title = titlekey == null ? attributes.getValue("title") : AppLocal.getIntString(titlekey);

				// adding the button to the panel
				// JButton btn = new JButtonFunc(attributes.getValue("key"),
				// attributes.getValue("image"), title);

				// The template resource or the code resource
				final String template = attributes.getValue("template");
				if (template == null) {
					final String code = attributes.getValue("code");
					// btn.addActionListener(new ActionListener() {
					// public void actionPerformed(ActionEvent evt) {
					// panelticket.evalScriptAndRefresh(code);
					// }
					// });
				} else {
					// btn.addActionListener(new ActionListener() {
					// public void actionPerformed(ActionEvent evt) {
					// panelticket.printTicket(template);
					// }
					// });
				}
				// add(btn);

			} else if ("event".equals(qName)) {
				// events.put(attributes.getValue("key"),
				// attributes.getValue("code"));
			} else {
				String value = attributes.getValue("value");

				// check if property has been overwritten for current POS
				String valuePOS = m_App.getProperties().getProperty(qName);

				if (value != null || valuePOS != null) {
					props.setProperty(qName, valuePOS != null ? valuePOS : value);
				}
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		}
	}

	public static int findMaxLabelWidth(JLabel... jLabels) {
		int maxvalue = 0;
		for (JLabel label : jLabels) {

			FontMetrics fm = label.getFontMetrics(label.getFont());
			int width = fm.stringWidth(label.getText());
			if (maxvalue < width) {
				maxvalue = width;
			}

		}

		return maxvalue;
	}

	public static void setGridBagConstraints(GridBagConstraints layoutData, int column, int row, int align,
			int columnspan) {
		setGridBagConstraints(layoutData, column, row, align);
		layoutData.gridwidth = columnspan;

	}

	public static void setGridBagConstraints(GridBagConstraints layoutData, int gridx, int gridy, int align) {
		layoutData.fill = align;
		layoutData.gridx = gridx;
		layoutData.gridy = gridy;
		layoutData.gridwidth = 1;
	}

	public static void ScaleScrollbar(AppView app, JScrollPane jScrollPane1) {
		String property = getProperty(app, "Ticket.Buttons", "scrollbar-vertical-size", "35");
		int value = Integer.parseInt(property);
		jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(value, value));
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		jScrollPane1.getHorizontalScrollBar().setPreferredSize(new Dimension(value, value));
		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public static void ScaleJComboBoxScrollbar(AppView app, JComboBox<?> jScrollBox) {
		String property = getProperty(app, "Ticket.Buttons", "scrollbar-vertical-size", "35");
		int value = Integer.parseInt(property);

		Object popup = jScrollBox.getUI().getAccessibleChild(jScrollBox, 0);
		Component c = ((Container) popup).getComponent(0);
		if (c instanceof JScrollPane) {
			JScrollPane scrollpane = (JScrollPane) c;
			JScrollBar scrollBar = scrollpane.getVerticalScrollBar();
			Dimension scrollBarDim = new Dimension(value, scrollBar.getPreferredSize().height);
			scrollBar.setPreferredSize(scrollBarDim);
		}

		// jScrollBox.getVerticalScrollBar().setPreferredSize(new
		// Dimension(value, value));
		// jScrollBox.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	public static Map<String, Integer> getTicketLineWidths(AppView app) {
		Map<String, Integer> widths = new HashMap<>();
		String v1 = getProperty(app, "", "Ticket.Line.label.item", null);
		if (v1 != null) {
			widths.put("label.item", Integer.parseInt(v1));
		}
		String v2 = getProperty(app, "", "Ticket.Line.label.units", null);
		if (v2 != null) {
			widths.put("label.units", Integer.parseInt(v2));
		}
		String v3 = getProperty(app, "", "Ticket.Line.label.value", null);
		if (v3 != null) {
			widths.put("label.value", Integer.parseInt(v3));
		}
		return widths;
	}

}
