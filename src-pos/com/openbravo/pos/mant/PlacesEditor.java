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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
		
		ScaleButtons();
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

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };

		setLayout(gbl_panel);

		jLabel2.setText(AppLocal.getIntString("Label.Name"));
		GridBagConstraints lbl1 = new GridBagConstraints();
		lbl1.anchor = GridBagConstraints.WEST;
		lbl1.insets = new Insets(5,5,0,0);
		lbl1.gridx = 0;
		lbl1.gridy = 0;
		add(jLabel2, lbl1);

		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 2;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(5,5,0,0);
		gbc_textPane.weightx = 1.0;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 0;
		add(m_jName, gbc_textPane);

		JLabel lblSpace1 = new JLabel("");
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.insets = new Insets(5,5,0,0);
		gbc_space1.weightx = 1.0;
		gbc_space1.gridx = 3;
		gbc_space1.gridy = 0;
		add(lblSpace1, gbc_space1);

		jLabel1.setText(AppLocal.getIntString("label.placefloor"));
		GridBagConstraints gbc_lblfloor = new GridBagConstraints();
		gbc_lblfloor.anchor = GridBagConstraints.WEST;
		gbc_lblfloor.insets = new Insets(5,5,0,0);
		gbc_lblfloor.gridx = 0;
		gbc_lblfloor.gridy = 1;
		add(jLabel1, gbc_lblfloor);

		m_jFloor.setEditable(false);
		m_jFloor.setEnabled(false);
		GridBagConstraints gbc_cmb_floor = new GridBagConstraints();
		gbc_cmb_floor.gridwidth = 2;
		gbc_cmb_floor.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmb_floor.insets = new Insets(5,5,0,0);
		gbc_cmb_floor.weightx = 1.0;
		gbc_cmb_floor.gridx = 1;
		gbc_cmb_floor.gridy = 1;
		add(m_jFloor, gbc_cmb_floor);

		JLabel lblSpace2 = new JLabel("");
		GridBagConstraints gbc_space2 = new GridBagConstraints();
		gbc_space2.insets = new Insets(5,5,0,0);
		gbc_space2.weightx = 1.0;
		gbc_space2.gridx = 3;
		gbc_space2.gridy = 1;
		add(lblSpace2, gbc_space2);

		jLabel3.setText(AppLocal.getIntString("label.placeposition"));
		GridBagConstraints lbl2 = new GridBagConstraints();
		lbl2.anchor = GridBagConstraints.WEST;
		lbl2.insets = new Insets(5,5,0,0);
		lbl2.gridx = 0;
		lbl2.gridy = 2;
		add(jLabel3, lbl2);

		GridBagConstraints gbc_spinner1 = new GridBagConstraints();
		gbc_spinner1.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner1.weightx = 1.0;
		gbc_spinner1.insets = new Insets(5,5,0,0);
		gbc_spinner1.gridx = 1;
		gbc_spinner1.gridy = 2;
		add(m_jX, gbc_spinner1);

		GridBagConstraints gbc_spinner2 = new GridBagConstraints();
		gbc_spinner2.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner2.weightx = 1.0;
		gbc_spinner2.insets = new Insets(5,5,0,0);
		gbc_spinner2.gridx = 2;
		gbc_spinner2.gridy = 2;
		add(m_jY, gbc_spinner2);

		JLabel space3 = new JLabel("");
		GridBagConstraints gbc_space3 = new GridBagConstraints();
		gbc_space3.insets = new Insets(5,5,0,0);
		gbc_space3.weightx = 2.0;
		gbc_space3.gridx = 3;
		gbc_space3.gridy = 2;
		add(space3, gbc_space3);

		jLabel4.setText(AppLocal.getIntString("label.placesize"));
		GridBagConstraints lbl3 = new GridBagConstraints();
		lbl3.anchor = GridBagConstraints.WEST;
		lbl3.insets = new Insets(5,5,0,0);
		lbl3.gridx = 0;
		lbl3.gridy = 3;
		add(jLabel4, lbl3);

		GridBagConstraints gbc_spinner3 = new GridBagConstraints();
		gbc_spinner3.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner3.weightx = 1.0;
		gbc_spinner3.insets = new Insets(5,5,0,0);
		gbc_spinner3.gridx = 1;
		gbc_spinner3.gridy = 3;
		add(m_jWidth, gbc_spinner3);

		GridBagConstraints gbc_spinner4 = new GridBagConstraints();
		gbc_spinner4.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner4.weightx = 1.0;
		gbc_spinner4.insets = new Insets(5,5,0,0);
		gbc_spinner4.gridx = 2;
		gbc_spinner4.gridy = 3;
		add(m_jHeight, gbc_spinner4);

		JLabel space4 = new JLabel("");
		GridBagConstraints gbc_space4 = new GridBagConstraints();
		gbc_space4.insets = new Insets(5,5,0,0);
		gbc_space4.weightx = 2.0;
		gbc_space4.gridx = 3;
		gbc_space4.gridy = 3;
		add(space4, gbc_space4);

		GridBagConstraints gbc_placesbag = new GridBagConstraints();
		gbc_placesbag.weighty = 1.0;
		gbc_placesbag.fill = GridBagConstraints.BOTH;
		gbc_placesbag.gridwidth = 4;
		gbc_placesbag.insets = new Insets(5,5,0,0);
		gbc_placesbag.gridx = 0;
		gbc_placesbag.gridy = 4;

		add(m_placesBag, gbc_placesbag);

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
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel1, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel3, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, jLabel4, "common-small-fontsize", "32");

		PropertyUtil.ScaleTextFieldFontsize(m_App, m_jName, "common-small-fontsize", "32");

		PropertyUtil.ScaleComboFontsize(m_App, m_jFloor, "common-small-fontsize", "32");

		PropertyUtil.ScaleSpinnerFontsize(m_App, m_jX, "common-small-fontsize", "32");
		PropertyUtil.ScaleSpinnerFontsize(m_App, m_jY, "common-small-fontsize", "32");
		PropertyUtil.ScaleSpinnerFontsize(m_App, m_jHeight, "common-small-fontsize", "32");
		PropertyUtil.ScaleSpinnerFontsize(m_App, m_jWidth, "common-small-fontsize", "32");
		
		PropertyUtil.ScaleSpinnerScrollbar(m_App, m_jX, "","");
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
