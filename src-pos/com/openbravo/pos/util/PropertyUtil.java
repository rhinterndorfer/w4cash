package com.openbravo.pos.util;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;

public class PropertyUtil {

	private static Logger logger = Logger.getLogger("com.openbravo.pos.util.PropertyUtil");

	public static void ScaleButtonIcon(javax.swing.JButton btn, int width, int height) {

		if (btn.getIcon() != null && javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1, radio > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			btn.setIcon(new javax.swing.ImageIcon(img));
		}
		btn.setSize(width, height);
	}

	public static void ScaleButtonIcon(JToggleButton btn, int width, int height) {
		if (javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1, radio > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			btn.setIcon(new javax.swing.ImageIcon(img));

			javax.swing.ImageIcon selectionicon = javax.swing.ImageIcon.class.cast(btn.getSelectedIcon());
			double radio2 = selectionicon.getIconWidth() / icon.getIconWidth();
			Image img2 = selectionicon.getImage().getScaledInstance(radio2 > 1 ? width : -1, radio2 > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			btn.setSelectedIcon(new javax.swing.ImageIcon(img2));

			btn.setSize(width, height);
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
		// Properties property =
		// dlSystem.getResourceAsProperties(app.getProperties().getHost() +
		// "/properties");
		Properties property = null;
		// dlSystem.getResourceAsProperties(sProperty);

		// if (property != null && property.isEmpty()) {

		if (sProperty != null) {
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
		}

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

}
