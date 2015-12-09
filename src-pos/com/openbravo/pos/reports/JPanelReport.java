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

package com.openbravo.pos.reports;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.util.JRViewer300;
import com.openbravo.pos.util.PropertyUtil;

public abstract class JPanelReport extends JPanel implements JPanelView, BeanFactoryApp {

	private JRViewer300 reportviewer = null;
	private JasperReport jr = null;
	private EditorCreator editor = null;
	private HashMap<String, Object> props = null;

	private String arg_address = null;

	protected AppView m_App;

	protected SentenceList taxsent;
	protected TaxesLogic taxeslogic;

	/** Creates new form JPanelReport */
	public JPanelReport() {

		initComponents();
	}

	public void init(AppView app) throws BeanFactoryException {

		m_App = app;
		props = new HashMap<String, Object>();

		DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
		DataLogicSystem m_dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystem");
		taxsent = dlSales.getTaxList();

		arg_address = m_dlSystem.getResourceAsText("Reports.Address");

		editor = getEditorCreator();
		if (editor instanceof ReportEditorCreator) {
			jPanelFilter.add(((ReportEditorCreator) editor).getComponent(), BorderLayout.CENTER);
		}

		reportviewer = new JRViewer300(m_App, null);

		add(reportviewer, BorderLayout.CENTER);

		try {

			InputStream in = getClass().getResourceAsStream(getReport() + ".ser");
			if (in == null) {
				// read and compile the report
				JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(getReport() + ".jrxml"));
				jr = JasperCompileManager.compileReport(jd);
			} else {
				// read the compiled report
				ObjectInputStream oin = new ObjectInputStream(in);
				jr = (JasperReport) oin.readObject();
				oin.close();
			}
		} catch (Exception e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"),
					e);
			msg.show(m_App, this);
			jr = null;
		}

		ScaleButtons();
	}

	public Object getBean() {
		return this;
	}

	protected abstract String getReport();

	protected abstract String getResourceBundle();

	protected abstract BaseSentence getSentence();

	protected abstract ReportFields getReportFields();

	protected EditorCreator getEditorCreator() {
		return null;
	}

	public JComponent getComponent() {
		return this;
	}

	public void activate() throws BasicException {

		setVisibleFilter(true);
		taxeslogic = new TaxesLogic(taxsent.list());
//		launchreport();
	}

	public boolean deactivate() {

		reportviewer.loadJasperPrint(null);
		return true;
	}

	protected void setVisibleButtonFilter(boolean value) {
		jToggleFilter.setVisible(value);
	}

	protected void setVisibleFilter(boolean value) {
		jToggleFilter.setSelected(value);
		jToggleFilterActionPerformed(null);
	}

	public void addProperty(String name, Object o) {
		props.put(name, o);
	}

	private void launchreport() {

		m_App.waitCursorBegin();

		if (jr != null) {
			try {

				// Archivo de recursos
				String res = getResourceBundle();

				// Parametros y los datos
				Object params = (editor == null) ? null : editor.createValue();
				BaseSentence sentence = getSentence();

				JRDataSource data = new JRDataSourceBasic(sentence, getReportFields(), params);

				// Construyo el mapa de los parametros.
				Map<String, Object> reportparams = new HashMap<String, Object>();
				reportparams.put("ARG", params);
				if (res != null) {
					reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(res));
				}
				reportparams.put("TAXESLOGIC", taxeslogic);
				reportparams.put("ARG_ADDRESS", arg_address);

				if (JParamsComposed.class.isInstance(editor)) {
					JParamsComposed paramsComposed = JParamsComposed.class.cast(editor);
					for (ReportEditorCreator e : paramsComposed.getEditors()) {
						if (JParamsClosedPosInterval.class.isInstance(e)) {
							JParamsClosedPosInterval filter = JParamsClosedPosInterval.class.cast(e);
							reportparams.put("ARG_START", filter.getStartItem());
							reportparams.put("ARG_END", filter.getEndItem());
						}
					}
				}

				for (Entry<String, Object> e : props.entrySet()) {
					reportparams.put(e.getKey(), e.getValue());
				}

				JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, data);

				reportviewer.loadJasperPrint(jp);

				setVisibleFilter(false);

			} catch (MissingResourceException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotloadresourcedata"), e);
				msg.show(m_App, this);
			} catch (JRException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotfillreport"), e);
				msg.show(m_App, this);
			} catch (BasicException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotloadreportdata"), e);
				msg.show(m_App, this);
			}
		}

		m_App.waitCursorEnd();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanelHeader = new javax.swing.JPanel();
		jPanelFilter = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jToggleFilter = new javax.swing.JToggleButton();
		jButton1 = new javax.swing.JButton();

		setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setLayout(new java.awt.BorderLayout());

		jPanelHeader.setLayout(new java.awt.BorderLayout());

		jPanelFilter.setLayout(new java.awt.BorderLayout());
		jPanelHeader.add(jPanelFilter, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
		jToggleFilter.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (jToggleFilter.isSelected()) {
					jToggleFilter.setText(AppLocal.getIntString("label.collapse"));
				} else {
					jToggleFilter.setText(AppLocal.getIntString("label.expand"));
				}
			}
		});

		jToggleFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png"))); // NOI18N
		jToggleFilter.setSelected(true);
		jToggleFilter.setMargin(new Insets(0, 0, 0, 0));
		jToggleFilter.setSelectedIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png"))); // NOI18N
		jToggleFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jToggleFilterActionPerformed(evt);
			}
		});

		jPanel1.add(jToggleFilter);

		jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
		jButton1.setMargin(new Insets(0, 0, 0, 0));
		jButton1.setText(AppLocal.getIntString("Button.ExecuteReport")); // NOI18N
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton1);

		jPanelHeader.add(jPanel1, java.awt.BorderLayout.SOUTH);

		add(jPanelHeader, java.awt.BorderLayout.NORTH);
	}// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		launchreport();

	}// GEN-LAST:event_jButton1ActionPerformed

	private void jToggleFilterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jToggleFilterActionPerformed

		jPanelFilter.setVisible(jToggleFilter.isSelected());

	}// GEN-LAST:event_jToggleFilterActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanelFilter;
	private javax.swing.JPanel jPanelHeader;
	private javax.swing.JToggleButton jToggleFilter;

	// End of variables declaration//GEN-END:variables
	public void ScaleButtons() {
		int menuwidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));
		
		PropertyUtil.ScaleButtonIcon(jButton1, menuwidth, menuheight,fontsize);
		PropertyUtil.ScaleButtonIcon(jToggleFilter, menuwidth, menuheight, fontsize);
	}
}
