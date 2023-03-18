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
import com.openbravo.pos.forms.ClosedCashInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.util.PropertyUtil;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import com.openbravo.beans.JCalendarDialog;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;

public class JParamsClosedPosIntervalUser extends javax.swing.JPanel implements ReportEditorCreator {

	private static final long serialVersionUID = -4443246826729773270L;

	private ComboBoxValModel m_endClosedPosModel;
	private ComboBoxValModel m_startClosedPosModel;
	private AppView m_App;
	private Boolean _filterBranch = false;

	/** Creates new form JParamsClosedPos */
	public JParamsClosedPosIntervalUser() {
		initComponents();
	}

	public JParamsClosedPosIntervalUser(Boolean filterBranch) {
		_filterBranch = filterBranch;
		initComponents();
	}
	
	public void init(AppView app) {
		m_App = app;
		
		ScaleButtons();
	}

	public void activate() throws BasicException {
		DataLogicSystem dlSys = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
		try {
			
			List<ClosedCashInfo> closedCashList = dlSys.getAllClosedCashList();

			if(_filterBranch)
			{
				closedCashList.removeIf(cc -> cc.getBranchSequence() != null);
			}
			
			m_endClosedPosModel = new ComboBoxValModel(closedCashList);
			m_endClosedPosModel.setSelectedFirst();
			jcbEndClosedPos.setModel(m_endClosedPosModel);

			m_startClosedPosModel = new ComboBoxValModel(closedCashList);
			m_startClosedPosModel.setSelectedFirst();
			jcbStartClosedPos.setModel(m_startClosedPosModel);

		} catch (BasicException e) {
			// do nothing
		}
	}

	public SerializerWrite getSerializerWrite() {
		return new SerializerWriteBasic(new Datas[] { 
				Datas.OBJECT, Datas.INT, 
				Datas.OBJECT, Datas.INT,
				Datas.OBJECT, Datas.STRING
				});
	}

	public Component getComponent() {
		return this;
	}

	public Object createValue() throws BasicException {
		Object startClosedPos = 1; // Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
		Object endClosedPos = 1; // Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
		String userId = "";
		if(m_App != null && m_App.getAppUserView() != null && m_App.getAppUserView().getUser() != null)
			userId = m_App.getAppUserView().getUser().getId();
		
		return new Object[] {
				m_startClosedPosModel.getSelectedKey() == null 
						? QBFCompareEnum.COMP_NONE
						: QBFCompareEnum.COMP_GREATEROREQUALS,
				m_startClosedPosModel.getSelectedKey(), 
				m_endClosedPosModel.getSelectedKey() == null
						? QBFCompareEnum.COMP_NONE 
					    : QBFCompareEnum.COMP_LESSOREQUALS,
				m_endClosedPosModel.getSelectedKey(),
				userId == null
						? QBFCompareEnum.COMP_NONE
						: QBFCompareEnum.COMP_EQUALS,
				userId
		};
	}

	public Object getStartItem() {
		return m_startClosedPosModel.getSelectedItem();
	}

	public Object getEndItem() {
		return m_endClosedPosModel.getSelectedItem();
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
		jLabel2 = new javax.swing.JLabel();
		jcbEndClosedPos = new javax.swing.JComboBox<>();
		jcbStartClosedPos = new javax.swing.JComboBox<>();
		
		setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.byclosedpos"))); // NOI18N
		setLayout(new GridBagLayout());

		jLabel1.setText(AppLocal.getIntString("Label.StartClosedCash")); // NOI18N
		GridBagConstraints lbl1 = new GridBagConstraints();
		lbl1.anchor = GridBagConstraints.WEST;
		lbl1.insets = new Insets(5, 5, 0, 0);
		lbl1.gridx = 0;
		lbl1.gridy = 0;
		add(jLabel1, lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5, 10, 0, 0);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		add(jcbStartClosedPos, gbc_textPane);
		
		JLabel lblSpace1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5,5,0,0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 2;
		gbc_space1.gridy = 0;
		add(lblSpace1, gbc_space1);

		jLabel2.setText(AppLocal.getIntString("Label.EndClosedCash")); // NOI18N
		GridBagConstraints lbl2 = new GridBagConstraints();
		lbl2.anchor = GridBagConstraints.WEST;
		lbl2.insets = new Insets(5, 5, 0, 0);
		lbl2.gridx = 0;
		lbl2.gridy = 1;
		add(jLabel2, lbl2);

		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane2.insets = new Insets(5, 10, 0, 0);
		gbc_textPane2.weightx = 1.0;
		gbc_textPane2.gridx = 1;
		gbc_textPane2.gridy = 1;
		add(jcbEndClosedPos, gbc_textPane2);
		
		JLabel lblSpace2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5,5,0,0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 2;
		gbc_space2.gridy = 1;
		add(lblSpace2, gbc_space2);

	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JComboBox<String> jcbEndClosedPos;
	private javax.swing.JComboBox<String> jcbStartClosedPos;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-filter-fontsize", "24");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-filter-fontsize", "24");
		PropertyUtil.ScaleComboFontsize(m_App, jcbEndClosedPos, "common-filter-fontsize", "24");
		PropertyUtil.ScaleComboFontsize(m_App, jcbStartClosedPos, "common-filter-fontsize", "24");
		PropertyUtil.ScaleBorderFontsize(m_App, (TitledBorder) getBorder(), "common-filter-fontsize", "24");
	}

}