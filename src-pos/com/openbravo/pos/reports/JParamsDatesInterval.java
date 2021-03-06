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

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import com.openbravo.beans.JCalendarDialog;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;

public class JParamsDatesInterval extends javax.swing.JPanel implements ReportEditorCreator {

	private AppView m_App;

	/** Creates new form JParamsClosedPos */
	public JParamsDatesInterval() {
		initComponents();
	}

	public void setStartDate(Date d) {
		jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(d));
	}

	public void setEndDate(Date d) {
		jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(d));
	}

	public void init(AppView app) {
		m_App = app;
		ScaleButtons();
	}

	public void activate() throws BasicException {
	}

	public SerializerWrite getSerializerWrite() {
		return new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP });
	}

	public Component getComponent() {
		return this;
	}

	public Object createValue() throws BasicException {
		Object startdate = Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
		Object enddate = Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
		return new Object[] { startdate == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_GREATEROREQUALS,
				startdate, enddate == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_LESS, enddate };
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jTxtStartDate = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jTxtEndDate = new javax.swing.JTextField();
		btnDateStart = new javax.swing.JButton();
		btnDateEnd = new javax.swing.JButton();
		TitledBorder tb = javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bydates"));
		setBorder(tb); // NOI18N
		// setPreferredSize(new java.awt.Dimension(0, 100));
		setLayout(new GridBagLayout());

		jLabel1.setText(AppLocal.getIntString("Label.StartDate")); // NOI18N
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.anchor = GridBagConstraints.WEST;
		gbc_lbl1.insets = new Insets(0, 0, 5, 5);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 0;
		add(jLabel1, gbc_lbl1);

		GridBagConstraints gbc_txt1 = new GridBagConstraints();
		gbc_txt1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt1.weightx = 1.0;
		gbc_txt1.insets = new Insets(0, 0, 5, 5);
		gbc_txt1.gridx = 1;
		gbc_txt1.gridy = 0;
		add(jTxtStartDate, gbc_txt1);

		btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		btnDateStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateStartActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_btn1.anchor = GridBagConstraints.WEST;
		gbc_btn1.insets = new Insets(0, 0, 5, 5);
		gbc_btn1.gridx = 2;
		gbc_btn1.gridy = 0;
		add(btnDateStart, gbc_btn1);

		JLabel space1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(0, 0, 0, 0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 3;
		gbc_space1.gridy = 0;
		add(space1, gbc_space1);

		jLabel2.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(0, 0, 5, 5);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 1;
		add(jLabel2, gbc_lbl2);

		GridBagConstraints gbc_txt2 = new GridBagConstraints();
		gbc_txt2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt2.weightx = 1.0;
		gbc_txt2.insets = new Insets(0, 0, 5, 5);
		gbc_txt2.gridx = 1;
		gbc_txt2.gridy = 1;
		add(jTxtEndDate, gbc_txt2);

		btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
		btnDateEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDateEndActionPerformed(evt);
			}
		});
		GridBagConstraints gbc_btn2 = new GridBagConstraints();
		gbc_btn2.anchor = GridBagConstraints.WEST;
		gbc_btn2.insets = new Insets(0, 0, 5, 5);
		gbc_btn2.gridx = 2;
		gbc_btn2.gridy = 1;
		add(btnDateEnd, gbc_btn2);

		JLabel space2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(0, 0, 0, 0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 3;
		gbc_space2.gridy = 1;
		add(space2, gbc_space2);

	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void ScaleButtons() {

		PropertyUtil.ScaleBorderFontsize(m_App, (TitledBorder) getBorder(), "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-filter-fontsize", "24");

		PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtStartDate, "common-filter-fontsize", "24");
		PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtEndDate, "common-filter-fontsize", "24");

		int btnWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "common-filter-fontsize", "32"));
		int btnHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "common-filter-fontsize", "32"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(btnDateEnd, btnWidth, btnHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(btnDateStart, btnWidth, btnHeight, fontsize);
		//
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel1,
		// "common-small-fontsize", "32");
		// PropertyUtil.ScaleLabelFontsize(m_App, jLabel2,
		// "common-small-fontsize", "32");
		//
		// PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtEndDate,
		// "common-small-fontsize", "32");
		// PropertyUtil.ScaleTextFieldFontsize(m_App, jTxtStartDate,
		// "common-small-fontsize", "32");
		//
		// PropertyUtil.ScaleBorderFontsize(m_App, (TitledBorder)getBorder(),
		// "common-small-fontsize", "32");
	}

	private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateStartActionPerformed

		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateStartActionPerformed

	private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDateEndActionPerformed

		Date date;
		try {
			date = (Date) Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
		} catch (BasicException e) {
			date = null;
		}
		date = JCalendarDialog.showCalendarTimeHours(m_App, this, date);
		if (date != null) {
			jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(date));
		}
	}// GEN-LAST:event_btnDateEndActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnDateEnd;
	private javax.swing.JButton btnDateStart;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JTextField jTxtEndDate;
	private javax.swing.JTextField jTxtStartDate;

	// End of variables declaration//GEN-END:variables

}
