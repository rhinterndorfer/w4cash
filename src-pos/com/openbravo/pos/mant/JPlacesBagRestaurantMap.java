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

import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.openbravo.pos.sales.*;
import com.openbravo.pos.sales.restaurant.Floor;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.forms.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.user.ListProvider;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.Session;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.PropertyUtil;

public class JPlacesBagRestaurantMap extends JPlacesBag {

	// private static final Icon ICO_OCU = new
	// ImageIcon(JTicketsBag.class.getResource("/com/openbravo/images/edit_group.png"));
	// private static final Icon ICO_FRE = new NullIcon(22, 22);

	private java.util.List<Place> m_aplaces;
	private java.util.List<Floor> m_afloors;

	// private JPlacesBagRestaurant m_restaurantmap;
	// private JPlacesBagRestaurantRes m_jreservations;

	private Place m_PlaceCurrent;

	// State vars
	private Place m_PlaceClipboard;
	private CustomerInfo customer;

	private DataLogicReceipts dlReceipts = null;
	private DataLogicSales dlSales = null;

	private PlacesEditor m_Editor;
	private JTabbedPane jTabFloors;
	private JPanel jSingleTabFloor;
	
	private String placeFreeColor = null;

	/** Creates new form JTicketsBagRestaurant */
	public JPlacesBagRestaurantMap(AppView app, PlacesEditor editor) {

		super(app/* , panelticket */);

		dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
		dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

		// m_restaurantmap = new JPlacesBagRestaurant(app, this);
		m_PlaceCurrent = null;
		m_PlaceClipboard = null;
		customer = null;

		this.m_Editor = editor;
		
		
		placeFreeColor = PropertyUtil.getProperty(app, "Ticket.Buttons", "place-free-color", null);

		try {
			SentenceList sent = new StaticSentence(app.getSession(),
					"SELECT ID, NAME, IMAGE FROM FLOORS ORDER BY SORTORDER,NAME", null,
					new SerializerReadClass(Floor.class));
			m_afloors = sent.list();
		} catch (BasicException eD) {
			m_afloors = new ArrayList<Floor>();
		}

		// read all places
		refreshPlaces();

		initComponents();

		// add the Floors containers (Tabbed)
		if (m_afloors.size() > 1) {
			// A tab container for 2 or more floors
			this.jTabFloors = new JTabbedPane();

			jTabFloors.applyComponentOrientation(getComponentOrientation());
			jTabFloors.setBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)));
			jTabFloors.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
			jTabFloors.setFocusable(false);
			jTabFloors.setRequestFocusEnabled(false);
			m_jPanelMap.add(jTabFloors, BorderLayout.CENTER);

			for (Floor f : m_afloors) {

				// calculate dimensions
				int maxWidth = 0;
				int maxHeight = 0;
				for (Place pl : m_aplaces) {
					if (pl.getFloor().equals(f.getID())) {
						int placeMaxX = pl.getX() + pl.getWidth() + 640; // for
																			// design
																			// view
																			// add
																			// additional
																			// space
						int placeMaxY = pl.getY() + pl.getHeight() + 640; // for
																			// design
																			// view
																			// add
																			// additional
																			// space

						maxWidth = maxWidth < placeMaxX ? placeMaxX : maxWidth;
						maxHeight = maxHeight < placeMaxY ? placeMaxY : maxHeight;
					}
				}

				if (maxWidth > 0 && maxHeight > 0)
					f.getContainer().setPreferredSize(new Dimension(maxWidth, maxHeight));

				f.getContainer().applyComponentOrientation(getComponentOrientation());

				JScrollPane jScrCont = new JScrollPane();
				jScrCont.applyComponentOrientation(getComponentOrientation());
				JPanel jPanCont = new JPanel();
				jPanCont.applyComponentOrientation(getComponentOrientation());
				PropertyUtil.ScaleScrollbar(m_App, jScrCont);

				jTabFloors.addTab(f.getName(), f.getIcon(), jScrCont);
				jScrCont.setViewportView(jPanCont);
				jPanCont.add(f.getContainer());
			}

			jTabFloors.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					floorChanged();
				}
			});
		}
		// A tab container for 1 floor
		else if (m_afloors.size() == 1) {
			// Just a frame for 1 floor
			Floor f = m_afloors.get(0);
			f.getContainer().applyComponentOrientation(getComponentOrientation());

			this.jSingleTabFloor = new JPanel();
			jSingleTabFloor.applyComponentOrientation(getComponentOrientation());
			jSingleTabFloor.setLayout(new BorderLayout());
			jSingleTabFloor.setBorder(
					new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)),
							new javax.swing.border.TitledBorder(f.getName())));

			JScrollPane jScrCont = new JScrollPane();
			jScrCont.applyComponentOrientation(getComponentOrientation());
			JPanel jPanCont = new JPanel();
			jPanCont.applyComponentOrientation(getComponentOrientation());
			PropertyUtil.ScaleScrollbar(m_App, jScrCont);

			// jPlaces.setLayout(new FlowLayout());
			m_jPanelMap.add(jSingleTabFloor, BorderLayout.CENTER);
			jSingleTabFloor.add(jScrCont, BorderLayout.CENTER);
			jScrCont.setViewportView(jPanCont);
			jPanCont.add(f.getContainer());
		}

		// Add all the Table buttons.
		Floor currfloor = null;

		for (Place pl : m_aplaces) {
			int iFloor = 0;

			if (currfloor == null || !currfloor.getID().equals(pl.getFloor())) {
				// Look for a new floor
				do {
					currfloor = m_afloors.get(iFloor++);
				} while (!currfloor.getID().equals(pl.getFloor()));
			}

			
			pl.SetPlaceFreeColor(placeFreeColor);
			currfloor.getContainer().add(pl.getButton());
			pl.setButtonBounds(m_App, pl.getWidth(), pl.getHeight());
			pl.setEditor(editor);
			// pl.getButton().addActionListener(new MyActionListener(pl));
		}

		// Add the reservations panel
		// m_jreservations = new JPlacesBagRestaurantRes(app, this);
		// add(m_jreservations, "res");
	}

	public void refreshPlaces() {

		try {
			// read all places for floor
			SentenceList sent = new StaticSentence(m_App.getSession(),
					"SELECT ID, NAME, X, Y, FLOOR, WIDTH, HEIGHT, FONTSIZE, FONTCOLOR FROM PLACES ORDER BY FLOOR", null,
					new SerializerReadClass(Place.class));
			m_aplaces = sent.list();
		} catch (BasicException eD) {
			m_aplaces = new ArrayList<Place>();
		}
		// refresh/repaint floor
		Floor currfloor = null;
		if (jTabFloors != null) {

			// Add all the Table buttons.

			String title = jTabFloors.getTitleAt(jTabFloors.getSelectedIndex());
			for (int i = 0; i < m_afloors.size(); i++) {
				Floor floor = m_afloors.get(i);
				if (title.equals(floor.getName())) {
					currfloor = floor;
					break;
				}
			}
		} else if (jSingleTabFloor != null && !m_afloors.isEmpty()) {

			currfloor = m_afloors.get(0);
		}

		if (currfloor != null) {
			List<Place> places2remove = new ArrayList<>();
			for (Place place : m_aplaces) {
				if (!currfloor.getID().equals(place.getFloor())) {
					places2remove.add(place);
				}
			}
			m_aplaces.removeAll(places2remove);

			Component[] components = currfloor.getContainer().getComponents();
			for (Component c : components) {
				currfloor.getContainer().remove(c);
			}
			currfloor.getContainer().repaint();

			for (Place pl : m_aplaces) {
				if (currfloor.getID().equals(pl.getFloor())) {
					
					pl.SetPlaceFreeColor(placeFreeColor);
					currfloor.getContainer().add(pl.getButton());
					pl.setButtonBounds(m_App, pl.getWidth(), pl.getHeight());
					pl.setEditor(this.m_Editor);
					// pl.getButton().addActionListener(new
					// MyActionListener(pl));
				}
			}
		}

	}

	@Override
	protected void floorChanged() {
		if (jTabFloors == null) {
			return;
		}

		String title = jTabFloors.getTitleAt(jTabFloors.getSelectedIndex());

		String id = null;
		for (int i = 0; i < m_afloors.size(); i++) {
			Floor floor = m_afloors.get(i);
			if (title.equals(floor.getName())) {
				id = floor.getID();
				break;
			}
		}

		TableDefinition td = new PlacesTableDefinition(m_App.getSession(), "PLACES",
				new String[] { "ID", "NAME", "X", "Y", "FLOOR", "WIDTH", "HEIGHT" ,"FONTSIZE", "FONTCOLOR"},
				new String[] { "ID", AppLocal.getIntString("Label.Name"), "X", "Y",
						AppLocal.getIntString("label.placefloor"), "WIDTH", "HEIGHT","FONTSIZE", "FONTCOLOR" },
				new Datas[] { Datas.STRING, Datas.STRING, Datas.INT, Datas.INT, Datas.STRING, Datas.INT, Datas.INT,  Datas.INT, Datas.STRING},
				new Formats[] { Formats.STRING, Formats.STRING, Formats.INT, Formats.INT, Formats.NULL, Formats.INT, Formats.INT,
						Formats.INT, Formats.STRING },
				new int[] { 0 }, id);

		m_Editor.getPanelPlaces().setTableDefinition(td);
		try {
			ListProvider lProv = m_Editor.getPanelPlaces().getListProvider();
			m_Editor.getPanelPlaces().getBrowseableData().setListProvider(lProv);
			m_Editor.getPanelPlaces().getBrowseableData().refreshData();
			m_Editor.setFloorModel(id);
		} catch (BasicException e1) {
			e1.printStackTrace();
		}
	}

	public void activate() {

		// precondicion es que no tenemos ticket activado ni ticket en el panel

		m_PlaceClipboard = null;
		customer = null;
		loadTickets();
		printState();

		showView("map"); // arrancamos en la vista de las mesas.
	}

	public boolean deactivate() {

		// precondicion es que tenemos ticket activado aqui y ticket en el panel

		if (viewTables()) {

			// borramos el clipboard
			m_PlaceClipboard = null;
			customer = null;

			// guardamos el ticket
			if (m_PlaceCurrent != null) {
				m_PlaceCurrent = null;
			}

			// desactivamos cositas.
			printState();

			return true;
		} else {
			return false;
		}

		// postcondicion es que no tenemos ticket activado
	}

	protected JComponent getBagComponent() {
		return this;
	}

	protected JComponent getNullComponent() {
		return this;
	}

	public void moveTicket() {

		// guardamos el ticket
		if (m_PlaceCurrent != null) {

			// me guardo el ticket que quiero copiar.
			m_PlaceClipboard = m_PlaceCurrent;
			customer = null;
			m_PlaceCurrent = null;
		}

		printState();
	}

	public boolean viewTables(CustomerInfo c) {
		// deberiamos comprobar si estamos en reservations o en tables...
		// if (m_jreservations.deactivate()) {
		showView("map"); // arrancamos en la vista de las mesas.

		m_PlaceClipboard = null;
		customer = c;
		printState();

		return true;
		// } else {
		// return false;
		// }
	}

	public boolean viewTables() {
		return viewTables(null);
	}

	public void newTicket() {

		// guardamos el ticket
		if (m_PlaceCurrent != null) {
			m_PlaceCurrent = null;
		}

		printState();
	}

	public void deleteTicket() {

		if (m_PlaceCurrent != null) {

			String id = m_PlaceCurrent.getId();
			try {
				dlReceipts.deleteSharedTicket(id);
			} catch (BasicException e) {
				new MessageInf(e).show(m_App, this);
			}

			m_PlaceCurrent.setTempName(null);
			m_PlaceCurrent.setPeople(false);

			m_PlaceCurrent = null;
		}

		printState();

	}

	public void loadTickets() {

		Set<String> atickets = new HashSet<String>();

		try {
			java.util.List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();
			for (SharedTicketInfo ticket : l) {
				atickets.add(ticket.getId());
			}
		} catch (BasicException e) {
			new MessageInf(e).show(m_App, this);
		}

		
		for (Place table : m_aplaces) {
			table.setTempName(null);
			table.setPeople(atickets.contains(table.getId()));
		}
	}

	// public PlacesEditor getEditor() {
	// return this.m_Editor;
	// }

	private void printState() {

		if (m_PlaceClipboard == null) {
			if (customer == null) {
				// Select a table
				m_jText.setText(null);
				// Enable all tables
				for (Place place : m_aplaces) {
					place.getButton().setEnabled(true);
				}
				// m_jbtnReservations.setEnabled(true);
			} else {
				// receive a customer
				m_jText.setText(AppLocal.getIntString("label.restaurantcustomer", new Object[] { customer.getName() }));
				// Enable all tables
				for (Place place : m_aplaces) {
					place.getButton().setEnabled(!place.hasPeople());
				}
				// m_jbtnReservations.setEnabled(false);
			}
		} else {
			// Moving or merging the receipt to another table
			m_jText.setText(AppLocal.getIntString("label.restaurantmove", new Object[] { m_PlaceClipboard.getName() }));
			// Enable all empty tables and origin table.
			for (Place place : m_aplaces) {
				place.getButton().setEnabled(true);
			}
			// m_jbtnReservations.setEnabled(false);
		}
	}

	private TicketInfo getTicketInfo(Place place) {

		try {
			return dlReceipts.getSharedTicket(place.getId());
		} catch (BasicException e) {
			new MessageInf(e).show(m_App, JPlacesBagRestaurantMap.this);
			return null;
		}
	}

	private void setActivePlace(Place place, TicketInfo ticket) {
		m_PlaceCurrent = place;
	}

	private void showView(String view) {
		CardLayout cl = (CardLayout) (getLayout());
		cl.show(this, view);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		m_jPanelMap = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();

		m_jText = new javax.swing.JLabel();

		setLayout(new java.awt.CardLayout());

		m_jPanelMap.setLayout(new java.awt.BorderLayout());
		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		jPanel2.add(m_jText);

		jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);
		m_jPanelMap.add(jPanel1, java.awt.BorderLayout.NORTH);

		add(m_jPanelMap, "map");
	}// </editor-fold>//GEN-END:initComponents

	private void m_jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnRefreshActionPerformed

		m_PlaceClipboard = null;
		customer = null;
		loadTickets();
		printState();

	}// GEN-LAST:event_m_jbtnRefreshActionPerformed

	// private void m_jbtnReservationsActionPerformed(java.awt.event.ActionEvent
	// evt) {// GEN-FIRST:event_m_jbtnReservationsActionPerformed
	//
	// showView("res");
	// m_jreservations.activate();
	//
	// }// GEN-LAST:event_m_jbtnReservationsActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel m_jPanelMap;
	private javax.swing.JLabel m_jText;

	@Override
	public Place selectPlace(String id) {
		boolean hasSelected = false;
		for (Place place : m_aplaces) {
			if (place.getId().equals(id)) {
				place.getButton().setSelected(true);
				setSelectedPlace(place);
				hasSelected = true;
			} else {
				place.getButton().setSelected(false);
			}
		}

		if (!hasSelected) {
			setSelectedPlace(null);
		}

		return getSelectedPlace();
	}

	@Override
	public Place getPlace() {

		return getSelectedPlace();

		// if (index < 0 || index >= m_aplaces.size()) {
		// return null;
		// }
		// return m_aplaces.get(index);
	}

	@Override
	public void ScaleButtons() {

	}

	@Override
	public Place[] getPlaces() {
		return this.m_aplaces.toArray(new Place[0]);
	}

	// private javax.swing.JButton m_jbtnRefresh;
	// private javax.swing.JButton m_jbtnReservations;

	// End of variables declaration//GEN-END:variables

	class PlacesTableDefinition extends TableDefinition {

		private String id;

		public PlacesTableDefinition(Session s, String tablename, String[] fieldname, String[] fieldtran,
				Datas[] fielddata, Formats[] fieldformat, int[] idinx) {
			super(s, tablename, fieldname, fielddata, fieldformat, idinx);
		}

		public PlacesTableDefinition(Session s, String tablename, String[] fieldname, Datas[] fielddata,
				Formats[] fieldformat, int[] idinx) {
			super(s, tablename, fieldname, fielddata, fieldformat, idinx);
		}

		public PlacesTableDefinition(Session s, String tablename, String[] fieldname, String[] fieldtran,
				Datas[] fielddata, Formats[] fieldformat, int[] idinx, String id) {
			this(s, tablename, fieldname, fieldtran, fielddata, fieldformat, idinx);
			this.id = id;
		}

		@Override
		public SentenceList getListSentence() {
			return super.getListSentence();
		}

		@Override
		public SentenceList getListSentence(SerializerRead sr) {
			return super.getListSentence(sr);
		}

		@Override
		public String getListSQL() {
			String sent = super.getListSQL();

			if (id != null) {
				sent += " where FLOOR ='" + id + "'";
			}

			return sent;
		}

	}

}
