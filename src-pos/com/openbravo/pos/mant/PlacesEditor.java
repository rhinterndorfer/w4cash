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

package com.openbravo.pos.mant;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class PlacesEditor extends JPanel implements EditorRecord {

	private SentenceList m_sentfloor;
	private ComboBoxValModel m_FloorModel;

	private String m_sID;

	private AppView m_App;
	private JPlacesBag m_placesBag;
	private JPanelPlaces m_panelPlaces;

	/** Creates new form PlacesEditor */
	public PlacesEditor(AppView appView, DataLogicSales dlSales, JPanelPlaces panelPlaces, DirtyManager dirty) {
		this.m_App = appView;

		initComponents();

		m_sentfloor = dlSales.getFloorsList();
		m_FloorModel = new ComboBoxValModel();
		m_panelPlaces = panelPlaces;

		m_jName.getDocument().addDocumentListener(dirty);
		m_jFloor.addActionListener(dirty);
		((JSpinner.DefaultEditor) m_jX.getEditor()).getTextField().getDocument().addDocumentListener(dirty);
		((JSpinner.DefaultEditor) m_jY.getEditor()).getTextField().getDocument().addDocumentListener(dirty);

		((JSpinner.DefaultEditor) m_jWidth.getEditor()).getTextField().getDocument().addDocumentListener(dirty);
		((JSpinner.DefaultEditor) m_jHeight.getEditor()).getTextField().getDocument().addDocumentListener(dirty);

		writeValueEOF();
	}

	public void activate() throws BasicException {

		m_FloorModel = new ComboBoxValModel(m_sentfloor.list());
		m_jFloor.setModel(m_FloorModel);

		m_placesBag.activate();
	}

	public void refresh() {
		this.m_placesBag.refreshPlaces();
	}

	public void writeValueEOF() {
		m_sID = null;
		m_jName.setText(null);
		m_FloorModel.setSelectedKey(null);
		m_jX.setValue(0);
		m_jY.setValue(0);
		m_jHeight.setValue(0);
		m_jWidth.setValue(0);

		m_jName.setEnabled(false);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
		m_jWidth.setEnabled(false);
		m_jHeight.setEnabled(false);
	}

	public void writeValueInsert() {
		this.m_placesBag.selectPlace(null);
		m_sID = UUID.randomUUID().toString();
		m_jName.setText(null);
		// m_FloorModel.setSelectedKey();
		m_jX.setValue(0);
		m_jY.setValue(0);

		int width = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-table-width", "60"));
		int height = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-table-height", "40"));

		m_jWidth.setValue(width);
		m_jHeight.setValue(height);

		m_jName.setEnabled(true);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
		m_jWidth.setEnabled(false);
		m_jHeight.setEnabled(false);
	}

	public void writeValueDelete(Object value) {

		Object[] place = (Object[]) value;
		m_sID = Formats.STRING.formatValue(place[0]);
		m_jName.setText(Formats.STRING.formatValue(place[1]));
		m_jX.setValue(place[2]);
		m_jY.setValue(place[3]);
		m_FloorModel.setSelectedKey(place[4]);

		m_jName.setEnabled(false);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
		m_jWidth.setEnabled(false);
		m_jHeight.setEnabled(false);

		refresh();
	}

	public void writeValueEdit(Object value) {

		Object[] place = (Object[]) value;
		m_sID = Formats.STRING.formatValue(place[0]);
		m_jName.setText(Formats.STRING.formatValue(place[1]));
		// m_jX.setValue(Formats.INT.formatValue(place[2]));
		// m_jY.setValue(Formats.INT.formatValue(place[3]));

		m_FloorModel.setSelectedKey(place[4]);

		// m_jWidth.setValue(Formats.INT.formatValue(place[5]));
		// m_jHeight.setValue(Formats.INT.formatValue(place[6]));

		m_jName.setEnabled(true);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(true);
		m_jY.setEnabled(true);
		m_jWidth.setEnabled(true);
		m_jHeight.setEnabled(true);

		Place current = this.m_placesBag.selectPlace(Formats.STRING.formatValue(place[0]));

		m_jX.setValue(place[2]);
		m_jY.setValue(place[3]);

		m_jWidth.setValue(place[5] == null ? 0 : place[5]);
		m_jHeight.setValue(place[6] == null ? 0 : place[6]);
	}

	public Object createValue() throws BasicException {
		Object[] place = new Object[7];
		place[0] = m_sID;

		if (m_jName.getText() != null && !m_jName.getText().isEmpty()) {
			place[1] = m_jName.getText();
		} else {
			place[1] = this.m_placesBag.getSelectedPlace().getName();
		}

		place[2] = Formats.INT.parseValue("" + m_jX.getValue());
		place[3] = Formats.INT.parseValue("" + m_jY.getValue());
		if (m_FloorModel.getSelectedKey() == null) {
			List<Place> places = new ArrayList<Place>();
			try {
				// read all places for floor
				SentenceList sent = new StaticSentence(m_App.getSession(),
						"SELECT ID, NAME, X, Y, FLOOR, WIDTH, HEIGHT FROM PLACES ORDER BY FLOOR", null,
						new SerializerReadClass(Place.class));
				places.addAll(sent.list());
			} catch (BasicException eD) {
				places = new ArrayList<Place>();
			}
			for (Place p : places) {
				if (p.getId().equals(place[0])) {
					place[4] = p.getFloor();
					break;
				}
			}
		} else {
			place[4] = m_FloorModel.getSelectedKey();
		}
		place[5] = Formats.INT.parseValue("" + m_jWidth.getValue());
		place[6] = Formats.INT.parseValue("" + m_jHeight.getValue());

		// this.m_placesBag.selectPlace((String) place[0]);

		return place;
	}

	public Component getComponent() {
		return this;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jLabel2 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		m_jX = new javax.swing.JSpinner();
		m_jY = new javax.swing.JSpinner();
		m_jWidth = new javax.swing.JSpinner();
		m_jHeight = new javax.swing.JSpinner();

		jLabel4 = new JLabel();
		jLabel1 = new javax.swing.JLabel();
		m_jFloor = new javax.swing.JComboBox();
		// m_jPosDec = new javax.swing.jspin();
		// m_jPosInc = new javax.swing.JButton();
		// m_jSizeDec = new javax.swing.JButton();
		// m_jSizeInc = new javax.swing.JButton();

		m_placesBag = JPlacesBag.createPlacesBag(m_App.getProperties().getProperty("machine.ticketsbag"), m_App, this);

		setLayout(null);

		jLabel2.setText(AppLocal.getIntString("Label.Name"));
		add(jLabel2);
		jLabel2.setBounds(20, 20, 90, 15);

		add(m_jName);
		m_jName.setBounds(110, 20, 180, 19);

		jLabel3.setText(AppLocal.getIntString("label.placeposition"));
		add(jLabel3);
		jLabel3.setBounds(20, 80, 90, 15);

		jLabel4.setText(AppLocal.getIntString("label.placesize"));
		add(jLabel4);
		jLabel4.setBounds(20, 110, 90, 15);

		add(m_jX);
		m_jX.setBounds(110, 80, 50, 19);

		add(m_jY);
		m_jY.setBounds(170, 80, 50, 19);

		// m_jPosDec.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png")));
		// // NOI18N
		// m_jPosDec.setText(AppLocal.getIntString("label.customer"));
		// m_jPosDec.setFocusPainted(false);
		// m_jPosDec.setFocusable(false);
		// m_jPosDec.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jPosDec.setRequestFocusEnabled(false);
		// m_jPosDec.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnPosDecActionPerformed(evt);
		// }
		// });
		// add(m_jPosDec);
		// m_jPosDec.setBounds(230, 80, 20, 20);

		// m_jPosInc.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png")));
		// // NOI18N
		// m_jPosInc.setText(AppLocal.getIntString("label.customer"));
		// m_jPosInc.setFocusPainted(false);
		// m_jPosInc.setFocusable(false);
		// m_jPosInc.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jPosInc.setRequestFocusEnabled(false);
		// m_jPosInc.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnPosIncActionPerformed(evt);
		// }
		// });
		// add(m_jPosInc);
		// m_jPosInc.setBounds(260, 80, 20, 20);

		jLabel4.setText(AppLocal.getIntString("label.placesize"));
		add(jLabel3);
		jLabel3.setBounds(20, 80, 90, 15);

		add(m_jWidth);
		m_jWidth.setBounds(110, 110, 50, 19);

		add(m_jHeight);
		m_jHeight.setBounds(170, 110, 50, 19);

		// m_jSizeDec.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png")));
		// // NOI18N
		// m_jSizeDec.setText(AppLocal.getIntString("label.customer"));
		// m_jSizeDec.setFocusPainted(false);
		// m_jSizeDec.setFocusable(false);
		// m_jSizeDec.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jSizeDec.setRequestFocusEnabled(false);
		// m_jSizeDec.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnSizeDecActionPerformed(evt);
		// }
		// });
		// add(m_jSizeDec);
		// m_jSizeDec.setBounds(230, 110, 20, 20);

		// m_jSizeInc.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png")));
		// // NOI18N
		// m_jSizeInc.setText(AppLocal.getIntString("label.customer"));
		// m_jSizeInc.setFocusPainted(false);
		// m_jSizeInc.setFocusable(false);
		// m_jSizeInc.setMargin(new java.awt.Insets(0, 0, 0, 0));
		// m_jSizeInc.setRequestFocusEnabled(false);
		// m_jSizeInc.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// btnSizeIncActionPerformed(evt);
		// }
		// });
		// add(m_jSizeInc);
		// m_jSizeInc.setBounds(260, 110, 20, 20);

		jLabel1.setText(AppLocal.getIntString("label.placefloor"));
		add(jLabel1);
		jLabel1.setBounds(20, 50, 90, 15);

		m_jFloor.setEditable(false);
		m_jFloor.setEnabled(false);
		add(m_jFloor);
		m_jFloor.setBounds(110, 50, 170, 20);

		add(m_placesBag);
		m_placesBag.setBounds(20, 140, 1000, 750);

		SpinnerNumberModel m1 = new SpinnerNumberModel();
		m1.setStepSize(10);
		m_jX.setModel(m1);
		m_jX.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				moveXYPlace();
			}

		});

		SpinnerNumberModel m2 = new SpinnerNumberModel();
		m2.setStepSize(10);
		m_jY.setModel(m2);
		m_jY.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				moveXYPlace();
			}
		});

		SpinnerNumberModel m3 = new SpinnerNumberModel();
		m3.setStepSize(10);
		m_jWidth.setModel(m3);
		m_jWidth.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				sizePlace();
			}
		});

		SpinnerNumberModel m4 = new SpinnerNumberModel();
		m4.setStepSize(10);
		m_jHeight.setModel(m4);
		m_jHeight.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				sizePlace();
			}

		});

	}// </editor-fold>//GEN-END:initComponents

	private void sizePlace() {
		String v1 = "" + m_jWidth.getValue();
		String v2 = "" + m_jHeight.getValue();

		int width = Integer.parseInt(String.valueOf(v1));
		int height = Integer.parseInt(String.valueOf(v2));

		Place place = m_placesBag.getPlace();
		if (place == null) {
			return;
		}

		place.setButtonBounds(m_App, width, height);
	}

	private void moveXYPlace() {
		String valueX = "" + m_jX.getValue();
		String valueY = "" + m_jY.getValue();

		try {
			int x = Integer.parseInt(String.valueOf(valueX));
			int y = Integer.parseInt(String.valueOf(valueY));

			Place place = m_placesBag.getPlace();
			if (place == null) {
				return;
			}
			place.setX(x);
			place.setY(y);
			place.setButtonBounds(m_App, place.getWidth(), place.getHeight());
		} catch (NumberFormatException exc) {

		}
	}

	public JPanelPlaces getPanelPlaces() {
		return this.m_panelPlaces;
	}

	public void setXYCoordinates(int x, int y) {
		this.m_jX.setValue(x);
		this.m_jY.setValue(y);
	}

	public void setPlaceSize(int width, int height) {
		this.m_jWidth.setValue(width);
		this.m_jHeight.setValue(height);
	}

	public void selectFloor(String id) {
		if (m_FloorModel != null)
			m_FloorModel.setSelectedKey(id);
	}

	public void selectPlace(Place place) {
		BrowsableEditableData bd = this.m_panelPlaces.getBrowseableData();
		ListModel listmodel = bd.getListModel();
		int index = 0;
		Object[] element = null;
		for (int i = 0; i < listmodel.getSize(); i++) {
			element = (Object[]) listmodel.getElementAt(i);
			if (place.getId().equals(element[0])) {
				break;
			}
			index++;
		}

		try {
			bd.moveTo(index);
			// this.m_placesBag.setSelectedPlace(place);
			// if (element != null) {
			// writeValueEdit(element);
			// }
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JComboBox m_jFloor;
	private javax.swing.JTextField m_jName;
	private javax.swing.JSpinner m_jX;
	private javax.swing.JSpinner m_jY;
	private javax.swing.JSpinner m_jWidth;
	private javax.swing.JSpinner m_jHeight;

	/**
	 * private JButton m_jPosInc; private JButton m_jPosDec; private JButton
	 * m_jSizeInc; private JButton m_jSizeDec;
	 */

	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {

	}

	public void activateFill() {
		m_placesBag.activateFill();
	}

	public void setFloorModel(String id) {
		for (int i = 0; i < m_FloorModel.getSize(); i++) {
			Object element = m_FloorModel.getElementAt(i);
			if (!(element instanceof FloorsInfo)) {
				continue;
			}

			if (((FloorsInfo) element).getID().equals(id)) {
				m_FloorModel.setSelectedItem(element);
				break;
			}
		}
	}

	@Override
	public void sortEditor(BrowsableEditableData bd) {

	}

}
