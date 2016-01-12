//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceFind;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.inventory.AttributeSetInfo;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.SwingUtilities;

/**
 *
 * @author adrianromero
 */
public class JProductAttEdit extends javax.swing.JDialog {

	private SentenceFind attsetSent;
	
	private SentenceList attvaluesSent;
	private SentenceList attinstSent;
	private SentenceList attinstSent2;
	private SentenceList attinstSentMisc;
	private SentenceFind attsetinstExistsSent;

	private SentenceExec attsetSave;
	private SentenceExec attinstSave;
	private SentenceExec attinstDelete;
	private SentenceExec attinstDelete2;
	
	private List<JProductAttEditI> itemslist;
	private String attsetid;
	private String attsetDescription;
	private String attInstanceId;
	private String attInstanceDescription;
	

	private boolean ok;

	public Boolean isForSingleProduct = false;
	private AppView m_App;

	/** Creates new form JProductAttEdit */
	private JProductAttEdit(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	/** Creates new form JProductAttEdit */
	private JProductAttEdit(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	private void init(AppView app, Session s) {
		this.m_App = app;
		initComponents();

		attsetSave = new PreparedSentence(s,
				"INSERT INTO ATTRIBUTESETINSTANCE (ID, ATTRIBUTESET_ID, DESCRIPTION) VALUES (?, ?, ?)",
				new SerializerWriteBasic(Datas.STRING, Datas.STRING, Datas.STRING));
		attinstSave = new PreparedSentence(s,
				"INSERT INTO ATTRIBUTEINSTANCE(ID, ATTRIBUTESETINSTANCE_ID, ATTRIBUTE_ID, VALUE) VALUES (?, ?, ?, ?)",
				new SerializerWriteBasic(Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING));
		attinstDelete = new PreparedSentence(s,
				"DELETE FROM ATTRIBUTEINSTANCE WHERE ATTRIBUTESETINSTANCE_ID = ? AND ATTRIBUTE_ID is NULL",
				SerializerWriteString.INSTANCE);
		attinstDelete2 = new PreparedSentence(s,
				"DELETE FROM ATTRIBUTEINSTANCE WHERE ATTRIBUTESETINSTANCE_ID = ? AND ATTRIBUTE_ID = ?",
				new SerializerWriteBasic(Datas.STRING, Datas.STRING));
		
		
		attsetSent = new PreparedSentence(s, "SELECT ID, NAME FROM ATTRIBUTESET WHERE ID = ?",
				SerializerWriteString.INSTANCE, new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new AttributeSetInfo(dr.getString(1), dr.getString(2));
					}
				});
		attsetinstExistsSent = new PreparedSentence(s,
				"SELECT ID FROM ATTRIBUTESETINSTANCE WHERE ATTRIBUTESET_ID = ? AND DESCRIPTION = ?",
				new SerializerWriteBasic(Datas.STRING, Datas.STRING), SerializerReadString.INSTANCE);

		attinstSent = new PreparedSentence(s,
				"SELECT A.ID, A.NAME, " + s.DB.CHAR_NULL() + ", " + s.DB.CHAR_NULL() + " "
						+ "FROM ATTRIBUTEUSE AU JOIN ATTRIBUTE A ON AU.ATTRIBUTE_ID = A.ID "
						+ "WHERE AU.ATTRIBUTESET_ID = ? " + "ORDER BY AU.LINENO",
				SerializerWriteString.INSTANCE, new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new AttributeInstInfo(dr.getString(1), dr.getString(2), dr.getString(3),
								dr.getString(4));
					}
				});
		attinstSent2 = new PreparedSentence(s,
				"SELECT A.ID, A.NAME, AI.ID, AI.VALUE "
						+ "FROM ATTRIBUTEUSE AU JOIN ATTRIBUTE A ON AU.ATTRIBUTE_ID = A.ID LEFT OUTER JOIN ATTRIBUTEINSTANCE AI ON AI.ATTRIBUTE_ID = A.ID "
						+ "WHERE AU.ATTRIBUTESET_ID = ? AND AI.ATTRIBUTESETINSTANCE_ID = ?" + "ORDER BY AU.LINENO",
				new SerializerWriteBasic(Datas.STRING, Datas.STRING), new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new AttributeInstInfo(dr.getString(1), dr.getString(2), dr.getString(3),
								dr.getString(4));
					}
				});

		attinstSentMisc = new PreparedSentence(s,
				"SELECT AI.ID, AI.VALUE " + "FROM ATTRIBUTEINSTANCE AI " + "WHERE AI.ATTRIBUTESETINSTANCE_ID = ?",
				SerializerWriteString.INSTANCE, new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new AttributeInstInfo("", "", dr.getString(1), dr.getString(2));
					}
				});
		attvaluesSent = new PreparedSentence(s, "SELECT VALUE FROM ATTRIBUTEVALUE WHERE ATTRIBUTE_ID = ?",
				SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE);

		getRootPane().setDefaultButton(m_jButtonOK);

		// m_jKeys.ScaleButtons();

		ScaleButtons();
	}

	private void ScaleButtons() {

	}

	public static JProductAttEdit getAttributesEditor(AppView app, Component parent, Session s) {

		Window window = SwingUtilities.getWindowAncestor(parent);

		JProductAttEdit myMsg;
		if (window instanceof Frame) {
			myMsg = new JProductAttEdit((Frame) window, true);
		} else {
			myMsg = new JProductAttEdit((Dialog) window, true);
		}
		myMsg.init(app, s);
		myMsg.applyComponentOrientation(parent.getComponentOrientation());
		return myMsg;
	}

	public void editAttributes(String attsetid, String attsetinstid, Boolean withAmountSelection)
			throws BasicException {

		itemslist = new ArrayList<JProductAttEditI>();
		this.attsetid = attsetid;
		this.attInstanceId = attsetinstid;
		
		m_jButtonOKSingle.setVisible(withAmountSelection);
		this.ok = false;
		
		if (attsetid != null) {
			this.attInstanceDescription = null;

			// get attsetinst values
			AttributeSetInfo asi = (AttributeSetInfo) attsetSent.find(attsetid);

			if (asi == null) {
				throw new BasicException(AppLocal.getIntString("message.cannotfindattributes"));
			}

			setTitle(asi.getName());
			this.attsetDescription = asi.getName(); 
			List<AttributeInstInfo> attinstinfo = attsetinstid == null ? attinstSent.list(attsetid)
					: attinstSent2.list(attsetid, attsetinstid);

			for (AttributeInstInfo aii : attinstinfo) {

				JProductAttEditI item;

				List<String> values = attvaluesSent.list(aii.getAttid());
				if (values.isEmpty()) {
					// Does not exist a list of values then a textfield
					item = new JProductAttEditItem(aii.getAttid(), aii.getAttname(), aii.getValue());
				} else {
					// Does exist a list with the values
					item = new JProductAttListItem(aii.getAttid(), aii.getAttname(), aii.getValue(), values);
				}

				itemslist.add(item);
				jPanel2.add(item.getComponent());
			}
		} else
		{
			// TODO: Translations
			setTitle(AppLocal.getIntString("title.attributesMisc"));
			this.attsetDescription = AppLocal.getIntString("title.attributesMisc");
		}
		// always add an text input field
		String value = "";
		if (attsetinstid != null) {
			List<AttributeInstInfo> attinstinfoMisc = attinstSentMisc.list(attsetinstid);
			value = attinstinfoMisc.get(0).getValue();
		}
		// TODO: Translations		
		JProductAttEditI itemMisc = new JProductAttEditItem(null, AppLocal.getIntString("label.attributesMisc"), value);
		itemslist.add(itemMisc);
		jPanel2.add(itemMisc.getComponent());

		if (itemslist.size() > 0) {
			itemslist.get(0).assignSelection();
		}

	}

	public void scaleFont(int fontSize) {

		if (itemslist != null && itemslist.size() > 0) {
			double scaleFactor = 1;

			for (int i = 0; i < itemslist.size(); i++) {
				JProductAttEditI item = itemslist.get(i);
				scaleFactor = item.scaleFont(fontSize);
			}

			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			int compWidth = (int) ((fontSize + 80) * scaleFactor) + 450;
			int compHeight = (int) (fontSize * scaleFactor) * itemslist.size() + (int) (fontSize * scaleFactor) + 200;

			if (compWidth > screenSize.getWidth())
				compWidth = (int) screenSize.getWidth();
			if (compHeight > screenSize.getHeight())
				compHeight = (int) screenSize.getHeight();

			PropertyUtil.ScaleDialog(m_App, this, compWidth, compHeight);

			// setBounds((screenSize.width - compWidth) / 2, (screenSize.height
			// - compHeight) / 2, compWidth, compHeight);

			int buttonFontSize = (int) (m_jButtonOK.getFont().getSize() * scaleFactor);
			// PropertyUtil.ScaleButtonFontsize(m_jButtonOK, buttonFontSize);
			PropertyUtil.ScaleButtonIcon(m_jButtonOK, buttonFontSize*3, buttonFontSize*3, buttonFontSize);
			// PropertyUtil.ScaleButtonFontsize(m_jButtonOKSingle,
			// buttonFontSize);
			PropertyUtil.ScaleButtonIcon(m_jButtonOKSingle, buttonFontSize*3, buttonFontSize*3, buttonFontSize);
			// PropertyUtil.ScaleButtonFontsize(m_jButtonCancel,
			// buttonFontSize);
			PropertyUtil.ScaleButtonIcon(m_jButtonCancel, buttonFontSize*3, buttonFontSize*3, buttonFontSize);

		}
	}

	public boolean isOK() {
		return ok;
	}

	public String getAttributeSetInst() {
		return attInstanceId;
	}

	public String getAttributeSetInstDescription() {
		return attInstanceDescription;
	}

	private static class AttributeInstInfo {

		private String attid;
		private String attname;
		private String id;
		private String value;

		public AttributeInstInfo(String attid, String attname, String id, String value) {
			this.attid = attid;
			this.attname = attname;
			this.id = id;
			this.value = value;
		}

		/**
		 * @return the attid
		 */
		public String getAttid() {
			return attid;
		}

		/**
		 * @return the attname
		 */
		public String getAttname() {
			return attname;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel5 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		m_jButtonOK = new javax.swing.JButton();
		m_jButtonOKSingle = new javax.swing.JButton();
		m_jButtonCancel = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		// jPanel4 = new javax.swing.JPanel();
		// m_jKeys = new com.openbravo.editor.JEditorKeys();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jPanel5.setLayout(new java.awt.BorderLayout());

		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));
		jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

		m_jButtonOKSingle
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		m_jButtonOKSingle.setText(AppLocal.getIntString("ProductAttEdit.Button.OKSingle")); // NOI18N
		m_jButtonOKSingle.setFocusPainted(false);
		m_jButtonOKSingle.setFocusable(false);
		m_jButtonOKSingle.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonOKSingle.setRequestFocusEnabled(false);
		m_jButtonOKSingle.setVisible(false);
		m_jButtonOKSingle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonOKSingleActionPerformed(evt);
			}
		});
		jPanel1.add(m_jButtonOKSingle);

		m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok2.png"))); // NOI18N
		m_jButtonOK.setText(AppLocal.getIntString("ProductAttEdit.Button.OKAll")); // NOI18N
		m_jButtonOK.setFocusPainted(false);
		m_jButtonOK.setFocusable(false);
		m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonOK.setRequestFocusEnabled(false);
		m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonOKActionPerformed(evt);
			}
		});
		jPanel1.add(m_jButtonOK);

		m_jButtonCancel
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
		m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
		m_jButtonCancel.setFocusPainted(false);
		m_jButtonCancel.setFocusable(false);
		m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
		m_jButtonCancel.setRequestFocusEnabled(false);
		m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jButtonCancelActionPerformed(evt);
			}
		});
		jPanel1.add(m_jButtonCancel);

		jPanel5.add(jPanel1, java.awt.BorderLayout.SOUTH);

		getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

		jPanel3.setLayout(new java.awt.BorderLayout());

		// jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4,
		// javax.swing.BoxLayout.Y_AXIS));
		// jPanel4.add(m_jKeys);

		// jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

		getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 350) / 2, (screenSize.height - 200) / 2, 350, 200);

		// java.awt.Dimension screenSize =
		// java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// setBounds((screenSize.width - 609) / 2, (screenSize.height - 388) /
		// 2, 609, 388);
	}// </editor-fold>//GEN-END:initComponents

	private void m_jButtonOKSingleActionPerformed(java.awt.event.ActionEvent evt) {
		isForSingleProduct = true;
		m_jButtonOKActionPerformed(evt);
	}

	private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonOKActionPerformed

		StringBuilder description = new StringBuilder();
		for (JProductAttEditI item : itemslist) {
			String value = item.getValue();
			if (value != null && value.length() > 0) {
				if (description.length() > 0) {
					description.append(", ");
				}
				description.append(value);
			}
		}

		if (description.length() == 0) {
			// No values then id is null
			this.attInstanceId = null;
		} else {

			if (this.attInstanceId == null) {
				this.attInstanceId = UUID.randomUUID().toString();
				try {
					attsetSave.exec(this.attInstanceId, attsetid, this.attsetDescription);
				} catch (BasicException ex) {
					return;
				}
			}
			
			try {
				attinstDelete.exec(this.attInstanceId); //remove old entries with no attribute reference (misc entries)
				for (JProductAttEditI item : itemslist) {
					attinstDelete2.exec(this.attInstanceId, item.getAttribute()); //remove old entries with attribute reference
					attinstSave.exec(UUID.randomUUID().toString(), this.attInstanceId, item.getAttribute(), item.getValue());
				}

			} catch (BasicException ex) {
				return;
			}
		}

		ok = true;
		attInstanceDescription = description.toString();

		dispose();
	}// GEN-LAST:event_m_jButtonOKActionPerformed

	private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jButtonCancelActionPerformed

		dispose();
	}// GEN-LAST:event_m_jButtonCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JButton m_jButtonCancel;
	private javax.swing.JButton m_jButtonOK;
	private javax.swing.JButton m_jButtonOKSingle;
	private com.openbravo.editor.JEditorKeys m_jKeys;
	// End of variables declaration//GEN-END:variables

}
