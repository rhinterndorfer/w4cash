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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;
import javax.swing.*;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
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
		m_jX.getDocument().addDocumentListener(dirty);
		m_jY.getDocument().addDocumentListener(dirty);
	
//		m_jWidth.getDocument().addDocumentListener(dirty);
//		m_jHeight.getDocument().addDocumentListener(dirty);
		
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
		m_jX.setText(null);
		m_jY.setText(null);

		m_jName.setEnabled(false);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
//		m_jWidth.setEnabled(false);
//		m_jHeight.setEnabled(false);
	}

	public void writeValueInsert() {
		m_sID = UUID.randomUUID().toString();
		m_jName.setText(null);
//		 m_FloorModel.setSelectedKey();
		m_jX.setText("0");
		m_jY.setText("0");
//		m_jWidth.setText("80");
//		m_jHeight.setText("50");
		
		m_jName.setEnabled(true);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
//		m_jWidth.setEnabled(false);
//		m_jHeight.setEnabled(false);
	}

	public void writeValueDelete(Object value) {

		Object[] place = (Object[]) value;
		m_sID = Formats.STRING.formatValue(place[0]);
		m_jName.setText(Formats.STRING.formatValue(place[1]));
		m_jX.setText(Formats.INT.formatValue(place[2]));
		m_jY.setText(Formats.INT.formatValue(place[3]));
		m_FloorModel.setSelectedKey(place[4]);

		m_jName.setEnabled(false);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(false);
		m_jY.setEnabled(false);
//		m_jWidth.setEnabled(false);
//		m_jHeight.setEnabled(false);
}

	public void writeValueEdit(Object value) {

		Object[] place = (Object[]) value;
		m_sID = Formats.STRING.formatValue(place[0]);
		m_jName.setText(Formats.STRING.formatValue(place[1]));
		m_jX.setText(Formats.INT.formatValue(place[2]));
		m_jY.setText(Formats.INT.formatValue(place[3]));
		m_FloorModel.setSelectedKey(place[4]);
//		m_jWidth.setText(Formats.INT.formatValue(place[5]));
//		m_jHeight.setText(Formats.INT.formatValue(place[6]));

		m_jName.setEnabled(true);
		m_jFloor.setEnabled(false);
		m_jX.setEnabled(true);
		m_jY.setEnabled(true);
//		m_jWidth.setEnabled(true);
//		m_jHeight.setEnabled(true);
	

		this.m_placesBag.selectPlace(Formats.STRING.formatValue(place[0]));
	}

	public Object createValue() throws BasicException {
		Object[] place = new Object[7];
		place[0] = m_sID;
		place[1] = m_jName.getText();
		place[2] = Formats.INT.parseValue(m_jX.getText());
		place[3] = Formats.INT.parseValue(m_jY.getText());
		place[4] = m_FloorModel.getSelectedKey();
//		place[5] = Formats.INT.parseValue(m_jWidth.getText());
//		place[6] = Formats.INT.parseValue(m_jHeight.getText());
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
		m_jX = new javax.swing.JTextField();
		m_jY = new javax.swing.JTextField();
//		m_jWidth = new javax.swing.JTextField();
//		m_jHeight = new javax.swing.JTextField();

		jLabel4 = new JLabel();
		jLabel1 = new javax.swing.JLabel();
		m_jFloor = new javax.swing.JComboBox();
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

		jLabel4.setText(AppLocal.getIntString("label.placesize"));
		add(jLabel3);
		jLabel3.setBounds(20, 80, 90, 15);
		
//		add(m_jWidth);
//		m_jWidth.setBounds(110, 110, 50, 19);
//
//		add(m_jHeight);
//		m_jHeight.setBounds(170, 110, 50, 19);

		jLabel1.setText(AppLocal.getIntString("label.placefloor"));
		add(jLabel1);
		jLabel1.setBounds(20, 50, 90, 15);

		m_jFloor.setEditable(false);
		m_jFloor.setEnabled(false);
		add(m_jFloor);
		m_jFloor.setBounds(110, 50, 170, 20);

		add(m_placesBag);
		m_placesBag.setBounds(20, 140, 1000, 750);

		m_jX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				moveXYPlace();
			}

		});
		m_jY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				moveXYPlace();
			}
		});
		
//		m_jWidth.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				sizePlace();
//			}
//		});
//		
//		m_jHeight.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				sizePlace();
//			}
//			
//			
//			
//		});

	}// </editor-fold>//GEN-END:initComponents

	private void sizePlace(){
//		String v1 = m_jWidth.getText();
//		String v2 = m_jHeight.getText();
//	
//		int width = Integer.parseInt(String.valueOf(v1));
//		int height = Integer.parseInt(String.valueOf(v2));
//	
//		Place place = m_placesBag.getPlace(m_panelPlaces.getBrowseableData().getIndex());
//		if (place == null) {
//			return;
//		}
//		
//		place.setButtonBounds(m_App,width, height);
	}
	
	private void moveXYPlace(){
		String valueX = m_jX.getText();
		String valueY = m_jY.getText();

		try {
			int x = Integer.parseInt(String.valueOf(valueX));
			int y = Integer.parseInt(String.valueOf(valueY));

			Place place = m_placesBag.getPlace(m_panelPlaces.getBrowseableData().getIndex());
			if (place == null) {
				return;
			}
			place.setX(x);
			place.setY(y);
			place.setButtonBounds(m_App);
		} catch (NumberFormatException exc) {

		}
	}
	
	public JPanelPlaces getPanelPlaces() {
		return this.m_panelPlaces;
	}

	public void setXYCoordinates(int x, int y, int width, int height) {
		this.m_jX.setText("" + x);
		this.m_jY.setText("" + y);
		
//		this.m_jWidth.setText(""+width);
//		this.m_jHeight.setText(""+height);
	}

	public void selectFloor(String id) {
		if(m_FloorModel != null)
			m_FloorModel.setSelectedKey(id);
	}
	
	public void selectPlace(Place place) {
		BrowsableEditableData bd = this.m_panelPlaces.getBrowseableData();
		ListModel listmodel = bd.getListModel();
		int index = 0;
		for (int i = 0; i < listmodel.getSize(); i++) {
			Object[] element = (Object[]) listmodel.getElementAt(i);
			String id = element[0].toString();
			if (place.getId().equals(element[0])) {
				break;
			}
			index++;
		}

		try {
			bd.moveTo(index);
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
	private javax.swing.JTextField m_jX;
	private javax.swing.JTextField m_jY;
//	private JTextField m_jWidth;
//	private JTextField m_jHeight;
	// End of variables declaration//GEN-END:variables

	@Override
	public void ScaleButtons() {

	}

	public void activateFill() {
		m_placesBag.activateFill();
	}

	public void setFloorModel(String id) {
		for(int i = 0; i < m_FloorModel.getSize(); i++){
			Object element = m_FloorModel.getElementAt(i);
			if(!(element instanceof FloorsInfo)){
				continue;
			}
			
			
			if(((FloorsInfo)element).getID().equals(id)){
				m_FloorModel.setSelectedItem(element);
				break;
			}
		}
	}
	@Override
	public void sortEditor(BrowsableEditableData bd) {
		
	}
}
