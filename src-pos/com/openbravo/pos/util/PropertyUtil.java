package com.openbravo.pos.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openbravo.data.loader.LocalRes;
import com.openbravo.editor.JEditorCurrency;
import com.openbravo.editor.JEditorNumber;
import com.openbravo.editor.JEditorString;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;

public class PropertyUtil {

	private static Logger logger = Logger.getLogger("com.openbravo.pos.util.PropertyUtil");

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
			// String text = btn.getText();
			Font font = btn.getFont();
			btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));

			FontMetrics fm = btn.getFontMetrics(font);
			// newWidth += fm.stringWidth(text);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			newWidth += fm.getHeight();
			newHeight += fm.getHeight();
		}

		btn.setSize(newWidth, newHeight);
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
		if (btn.getSelectedIcon() != null
				&& javax.swing.ImageIcon.class.isAssignableFrom(btn.getSelectedIcon().getClass())) {
			javax.swing.ImageIcon selectionicon = javax.swing.ImageIcon.class.cast(btn.getSelectedIcon());
			double radio2 = selectionicon.getIconWidth() / selectionicon.getIconWidth();
			Image img2 = selectionicon.getImage().getScaledInstance(radio2 > 1 ? width : -1, radio2 > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			btn.setSelectedIcon(new javax.swing.ImageIcon(img2));
		}

		if (btn.getText() != null && !btn.getText().isEmpty()) {
			String text = btn.getText();
			Font font = btn.getFont();
			btn.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			FontMetrics fm = btn.getFontMetrics(font);
			// newWidth += fm.stringWidth(text);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			newWidth += fm.getHeight();
			newHeight += fm.getHeight();
		}

		btn.setSize(newWidth, newHeight);
		// btn.setMinimumSize(new Dimension(newWidth, newHeight));
		// btn.setPreferredSize(new Dimension(width, newHeight));
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

	public static void ScaleDialog(AppView app, JDialog dialog, int width, int height) {
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
	}

	public static void ScaleEditcurrencyFontsize(AppView app, JEditorCurrency label, String key, String defaultValue) {
		DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		String value = getProperty(app, dlSystem, "Ticket.Buttons", key);
		if (value == null) {
			value = defaultValue;
		}
		try {
			int fontsize = Integer.parseInt(value);
			Font font = label.getFont();
			label.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			label.setSize((int) label.getSize().getWidth(), fontsize);
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

			Font font = label.getFont();
			label.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			label.setSize((int) label.getSize().getWidth(), fontsize);
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

			Font font = unit.getFont();
			unit.setFont(new Font(font.getName(), font.getStyle(), fontsize));
			unit.setSize((int) unit.getSize().getWidth(), fontsize);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	public static void ScaleLabelFontsize(AppView app, JLabel label, String key, String defaultValue) {
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
			// label.setPreferredSize(new Dimension((int)
			// label.getSize().getWidth(), fontsize));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
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
		label.setSize((int) (label.getSize().getWidth() * scaleFactor), fontSize);

		return scaleFactor;
	}

	public static double ScaleLabelFontsize(JTextField textfield, int fontSize) {
		Font fontLabel = textfield.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();

		textfield.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
		if (textfield.getSize().getWidth() > 0)
			textfield.setSize((int) (textfield.getSize().getWidth() * scaleFactor), fontSize);
		if (textfield.getPreferredSize().getWidth() > 0)
			textfield.setPreferredSize(
					new Dimension((int) (textfield.getPreferredSize().getWidth() * scaleFactor), fontSize));

		return scaleFactor;
	}

	public static double ScaleComboboxFontSize(JComboBox combobox, int fontSize) {
		Font fontLabel = combobox.getFont();

		double scaleFactor = fontSize / fontLabel.getSize();
		combobox.setFont(new Font(fontLabel.getName(), fontLabel.getStyle(), fontSize));
		combobox.setSize((int) (combobox.getSize().getWidth() * scaleFactor), fontSize);

		return scaleFactor;
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

	public static void StyleTabbedPane(JTabbedPane tabbedPane) {
		// tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
		// @Override
		// protected int calculateTabHeight(int tabPlacement, int tabIndex, int
		// fontHeight) {
		// return 32;
		// }
		//
		// @Override
		// protected void paintTab(Graphics g, int tabPlacement, Rectangle[]
		// rects, int tabIndex, Rectangle iconRect,
		// Rectangle textRect) {
		//
		// rects[tabIndex].height += 20;
		//
		// super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
		// }
		// });
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
	}

}
