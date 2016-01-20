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

package com.openbravo.pos.sales.restaurant;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.sales.restaurant.Floor.JPanelDrawing;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.PropertyUtil;

public class JTicketsBagRestaurantMap extends JTicketsBag {

	// private static final Icon ICO_OCU = new
	// ImageIcon(JTicketsBag.class.getResource("/com/openbravo/images/edit_group.png"));
	// private static final Icon ICO_FRE = new NullIcon(22, 22);

	private static final long serialVersionUID = 5721678894828393596L;
	private java.util.List<Place> m_aplaces;
	private java.util.List<Floor> m_afloors;

	private JTicketsBagRestaurant m_restaurantmap;
	private JTicketsBagRestaurantRes m_jreservations;

	private Place m_PlaceCurrent;

	public Place getPlaceCurrent() {
		return m_PlaceCurrent;
	}

	public void setPlaceCurrent(Place m_PlaceCurrent) {
		this.m_PlaceCurrent = m_PlaceCurrent;
	}

	// State vars
	private Place m_PlaceClipboard;
	private CustomerInfo customer;

	private DataLogicReceipts dlReceipts = null;
	private DataLogicSales dlSales = null;
	private JButton m_jbtnLogout;
	private DataLogicSystem dlSystem = null;

	private TicketParser m_TTP;
	private JPanel jPanel3;

	/** Creates new form JTicketsBagRestaurant */
	public JTicketsBagRestaurantMap(AppView app, TicketsEditor panelticket) {
		super(app, panelticket);

		dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
		dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
		dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

		m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

		m_restaurantmap = new JTicketsBagRestaurant(app, this);
		m_PlaceCurrent = null;
		m_PlaceClipboard = null;
		customer = null;

		try {
			SentenceList sent = new StaticSentence(app.getSession(),
					"SELECT ID, NAME, IMAGE FROM FLOORS ORDER BY SORTORDER,NAME", null,
					new SerializerReadClass(Floor.class));
			m_afloors = sent.list();

		} catch (BasicException eD) {
			m_afloors = new ArrayList<Floor>();
		}
		try {
			SentenceList sent = new StaticSentence(app.getSession(),
					"SELECT ID, NAME, X, Y, FLOOR, WIDTH, HEIGHT, FONTSIZE FROM PLACES ORDER BY FLOOR", null,
					new SerializerReadClass(Place.class));
			m_aplaces = sent.list();
		} catch (BasicException eD) {
			m_aplaces = new ArrayList<Place>();
		}

		initComponents();

		// add the Floors containers
		if (m_afloors.size() > 1) {
			// A tab container for 2 or more floors
			JTabbedPane jTabFloors = new JTabbedPane();
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
						int placeMaxX = pl.getX() + pl.getWidth();
						int placeMaxY = pl.getY() + pl.getHeight();

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
				PropertyUtil.ScaleTabbedPaneFontsize(m_App, jTabFloors, "restaurant-room-fontsize", "16");

				jTabFloors.addTab(f.getName(), f.getIcon(), jScrCont);
				jScrCont.setViewportView(jPanCont);
				jPanCont.add(f.getContainer());
			}
		} else if (m_afloors.size() == 1) {
			// Just a frame for 1 floor
			Floor f = m_afloors.get(0);
			f.getContainer().applyComponentOrientation(getComponentOrientation());

			JPanel jPlaces = new JPanel();
			jPlaces.applyComponentOrientation(getComponentOrientation());
			jPlaces.setLayout(new BorderLayout());
			jPlaces.setBorder(
					new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)),
							new javax.swing.border.TitledBorder(f.getName())));

			JScrollPane jScrCont = new JScrollPane();
			jScrCont.applyComponentOrientation(getComponentOrientation());
			JPanel jPanCont = new JPanel();
			jPanCont.applyComponentOrientation(getComponentOrientation());
			PropertyUtil.ScaleScrollbar(m_App, jScrCont);
			// jPlaces.setLayout(new FlowLayout());
			m_jPanelMap.add(jPlaces, BorderLayout.CENTER);
			jPlaces.add(jScrCont, BorderLayout.CENTER);
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

			currfloor.getContainer().add(pl.getButton());
			pl.setButtonBounds(m_App, pl.getWidth(), pl.getHeight());
			pl.getButton().addActionListener(new MyActionListener(pl));
			pl.getButton().setDraggable(false);
		}

		// Add the reservations panel
		m_jreservations = new JTicketsBagRestaurantRes(app, this);
		add(m_jreservations, "res");

		// add resize listener
		m_jPanelMap.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				Dimension d = e.getComponent().getSize();
				for (Floor f : m_afloors) {
					Container c = f.getContainer();
					JPanelDrawing pd = JPanelDrawing.class.cast(c);
					Dimension df = c.getPreferredSize();
					double oldWidth = df.getWidth();

					BufferedImage img = f.getImage();
					if (img != null) {
						oldWidth = img.getWidth();
					}

					double newWidth = d.getWidth() - 10; // 10 Margin
					double scale = newWidth / oldWidth;
					if (scale > 1)
						scale = 1;

					if (img != null) {
						int w = img.getWidth();
						int h = img.getHeight();
						BufferedImage dimg = new BufferedImage((int) (w * scale), (int) (h * scale), img.getType());
						Graphics2D g = dimg.createGraphics();
						g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
								RenderingHints.VALUE_INTERPOLATION_BILINEAR);
						g.drawImage(img, 0, 0, (int) (w * scale), (int) (h * scale), 0, 0, w, h, null);
						g.dispose();

						pd.SetImage(dimg);
						pd.repaint();
					}

					for (Place p : m_aplaces) {
						if (p.getFloor().equals(f.getID())) {
							Rectangle rect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
							rect.setBounds((int) (rect.x * scale), (int) (rect.y * scale), (int) (rect.width * scale),
									(int) (rect.height * scale));
							p.getButton().setBounds(rect);
						}
					}

				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void ScaleButtons() {
		int smallWidth = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-width", "60"));
		int smallHeight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchlarge-height", "60"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		PropertyUtil.ScaleButtonIcon(m_jbtnRefresh, smallWidth, smallHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(m_jbtnLogout, smallWidth, smallHeight, fontsize);

		// m_restaurantmap.ScaleButtons();

		m_restaurantmap.ScaleButtons();

		PropertyUtil.ScaleButtonIcon(m_jbtnLogout, smallWidth, smallHeight, fontsize);
		PropertyUtil.ScaleButtonIcon(btn_promptTicket, smallWidth, smallHeight, fontsize);
	}

	public void activate() {

		// precondicion es que no tenemos ticket activado ni ticket en el panel

		m_PlaceClipboard = null;
		customer = null;
		loadTickets();
		printState();

		m_panelticket.setActiveTicket(null, null);
		m_restaurantmap.activate();

		showView("map"); // arrancamos en la vista de las mesas.

		// postcondicion es que tenemos ticket activado aqui y ticket en el
		// panel
	}

	public boolean deactivate() {

		// precondicion es que tenemos ticket activado aqui y ticket en el panel

		if (viewTables()) {

			// borramos el clipboard
			m_PlaceClipboard = null;
			customer = null;

			// guardamos el ticket
			if (m_PlaceCurrent != null) {

				try {
					dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
				} catch (BasicException e) {
					new MessageInf(e).show(m_App, this);
				}

				m_PlaceCurrent = null;
			}

			// desactivamos cositas.
			printState();
			m_panelticket.setActiveTicket(null, null);

			return true;
		} else {
			return false;
		}

		// postcondicion es que no tenemos ticket activado
	}

	public void moveTicket() {

		if (m_panelticket.getActiveTicket().getLinesCount() > 0) {
			// guardamos el ticket
			if (m_PlaceCurrent != null) {
				try {
					dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
				} catch (BasicException e) {
					new MessageInf(e).show(m_App, this);
				}

				// me guardo el ticket que quiero copiar.
				m_PlaceClipboard = m_PlaceCurrent;
				customer = null;
				m_PlaceCurrent = null;
			}

			printState();
			m_panelticket.setActiveTicket(null, null);
		}

	}

	public boolean viewTables(CustomerInfo c) {
		// deberiamos comprobar si estamos en reservations o en tables...
		if (m_jreservations.deactivate()) {
			showView("map"); // arrancamos en la vista de las mesas.

			m_PlaceClipboard = null;
			customer = c;
			printState();

			return true;
		} else {
			return false;
		}
	}

	public boolean viewTables() {
		return viewTables(null);
	}

	/**
	 * find the printer id by category
	 * 
	 * @param categoryID
	 * @return
	 */
	private int findPrinterIdByCategory(List<CategoryInfo> infos, String categoryID) {

		for (CategoryInfo info : infos) {
			if (info.getID().compareTo(categoryID) == 0) {
				// category was found
				return info.getPrinterId();
			}
		}

		return -1;
	}

	public void newTicket(TicketInfo ticketinfo1, TicketInfo clone) {

		// guardamos el ticket
		if (m_PlaceCurrent != null) {

			try {
				// if (m_panelticket.getActiveTicket().getLinesCount() > 0) {
				// here we add ticket for each printer
				HashMap<Integer, TicketInfo> printabletickets = new HashMap<Integer, TicketInfo>();
				List<CategoryInfo> categories = dlSales.getCategories();
				// and schank printer
				// TicketInfo ticketinfo1 =
				// m_panelticket.getActiveTicket().copyTicket();
				// TicketInfo clone = m_panelticket.getActiveTicketClone();

				int printerId = -1;
				TicketInfo ti = null;
				// first check if we have something to print
				for (TicketLineInfo line : ticketinfo1.getLines()) {

					printerId = findPrinterIdByCategory(categories, line.getProperty("product.categoryid"));
					int index = 0;

					if (printerId < 0) {
						for (TicketLineInfo inf : clone.getLines()) {
							if (line.getProductID().compareTo(inf.getProductID()) == 0) {
								// line found, so verify amount
								clone.removeLine(index);
								break;
							}
							index++;
						}
						continue; // no printer for category configured
					}

					// get ti from map
					ti = printabletickets.get(printerId);

					// we couldn't find a ticketinfo for the configured
					// printer, so we add a new one
					if (ti == null) {
						ti = ticketinfo1.copyTicket();
						ti.getLines().clear();
						printabletickets.put(printerId, ti);
					}

					// try to find line in clone
					index = 0;
					boolean linematch = false;
					for (TicketLineInfo inf : clone.getLines()) {
						if (line.getProductID().compareTo(inf.getProductID()) == 0) {
							// line found, so verify amount
							if (line.getMultiply() != inf.getMultiply()) {
								line.setMultiply(line.getMultiply() - inf.getMultiply());
								ti.addLine(line);

								// clone.removeLine(index);
								// break;
							}
							linematch = true;
							clone.removeLine(index);
							break;
						}
						index++;
					}
					// if we couldn't find the line so put the whole line
					// into printer spooler
					if (!linematch) {
						ti.addLine(line);
					}
					// now we put all different lines into printer ordered
					// ticketinfo

				}

				// now try to find lines which were deleted from
				// original
				for (TicketLineInfo inf : clone.getLines()) {
					printerId = findPrinterIdByCategory(categories, inf.getProperty("product.categoryid"));
					if (printerId < 0)
						continue;

					ti = printabletickets.get(printerId);
					if (ti == null) {
						ti = ticketinfo1.copyTicket();
						ti.getLines().clear();
						printabletickets.put(printerId, ti);
					}

					inf.setMultiply(0 - inf.getMultiply());
					ti.addLine(inf);
					//
				}

				printOrder("Printer.AdditionalPrinter", printabletickets, m_PlaceCurrent.getSName());
				// for(int key : printabletickets.keySet()) {
				// printOrder("Printer.AdditionalPrinter",
				// printabletickets.get(key), m_PlaceCurrent.getSName());
				// }
				if (m_panelticket.getActiveTicket().getLinesCount() > 0) {
					dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
				} else {
					dlReceipts.deleteSharedTicket(m_PlaceCurrent.getId());
					m_jbtnRefreshActionPerformed(null);
				}
			} catch (BasicException e) {
				new MessageInf(e).show(m_App, this); // maybe other guy deleted
			}

			// m_PlaceCurrent = null;
		}

		printState();
		m_panelticket.setActiveTicket(null, null);
	}

	private void printOrder(String sresourcename, HashMap<Integer, TicketInfo> tickets, Object ticketext) {
		String sresource = dlSystem.getResourceAsXML(sresourcename);
		if (sresource == null) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintorder"));
			msg.show(m_App, JTicketsBagRestaurantMap.this);
		} else {
			for (int key : tickets.keySet()) {
				if (tickets.get(key).getLinesCount() <= 0)
					continue;
				try {
					ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
					script.put("ticket", tickets.get(key));
					script.put("place",
							ticketext != null && ticketext.getClass().equals(String.class) ? ticketext.toString() : "");
					script.put("printer", "" + key);
					script.put("printername", "Drucker " + key);
					m_TTP.printTicket(script.eval(sresource).toString());
				} catch (ScriptException e) {
					MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
							AppLocal.getIntString("message.cannotprintticket"), e);
					msg.show(m_App, JTicketsBagRestaurantMap.this);
				} catch (TicketPrinterException e) {
					MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
							AppLocal.getIntString("message.cannotprintticket"), e);
					msg.show(m_App, JTicketsBagRestaurantMap.this);
				}
			}
		}
	}

	public void promptTicket() {

		Place m_place = new Place();
		m_place.setSId("direkt");
		m_place.setSName("direkt");

		TicketInfo ticket = new TicketInfo();

		m_restaurantmap.setPromptTicket(true);
		setActivePlace(m_place, ticket);
	}

	public void deleteTicket(boolean printdelete) {

		// generate empty ticket
		TicketInfo info = new TicketInfo();
		if(printdelete)
			newTicket(info, m_panelticket.getActiveTicket().copyTicket());

		if (m_PlaceCurrent != null) {

			String id = m_PlaceCurrent.getId();
			try {
				dlReceipts.deleteSharedTicket(id);
			} catch (BasicException e) {
				new MessageInf(e).show(m_App, this);
			}

			if (!m_restaurantmap.isPromptTicket())
				m_PlaceCurrent.setPeople(false);

			m_PlaceCurrent = null;
		}

		printState();
		m_panelticket.setActiveTicket(null, null);
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
			table.setPeople(atickets.contains(table.getId()));
		}
	}

	protected JComponent getBagComponent() {
		return m_restaurantmap;
	}

	protected JComponent getNullComponent() {
		return this;
	}

	@Override
	protected void ticketListChange(JTicketLines ticketLines) {
		m_restaurantmap.ticketListChange(ticketLines);

	}

	private void printState() {

		if (m_PlaceClipboard == null) {
			if (customer == null) {
				// Select a table
				m_jText.setText(null);
				// Enable all tables
				for (Place place : m_aplaces) {
					place.getButton().setEnabled(true);
				}
				m_jbtnReservations.setEnabled(true);
			} else {
				// receive a customer
				m_jText.setText(AppLocal.getIntString("label.restaurantcustomer", new Object[] { customer.getName() }));
				// Enable all tables
				for (Place place : m_aplaces) {
					place.getButton().setEnabled(!place.hasPeople());
				}
				m_jbtnReservations.setEnabled(false);
			}
		} else {
			// Moving or merging the receipt to another table
			m_jText.setText(AppLocal.getIntString("label.restaurantmove", new Object[] { m_PlaceClipboard.getName() }));
			// Enable all empty tables and origin table.
			for (Place place : m_aplaces) {
				place.getButton().setEnabled(true);
			}
			m_jbtnReservations.setEnabled(false);
		}
	}

	private TicketInfo getTicketInfo(Place place) {

		try {
			return dlReceipts.getSharedTicket(place.getId());
		} catch (BasicException e) {
			new MessageInf(e).show(m_App, JTicketsBagRestaurantMap.this);
			return null;
		}
	}

	private void setActivePlace(Place place, TicketInfo ticket) {
		m_PlaceCurrent = place;
		m_panelticket.setActiveTicket(ticket, m_PlaceCurrent.getName());
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
		jPanel3 = new javax.swing.JPanel();
		m_jbtnReservations = new javax.swing.JButton();
		m_jbtnRefresh = new javax.swing.JButton();
		m_jbtnLogout = new javax.swing.JButton();
		m_jText = new javax.swing.JLabel();
		btn_promptTicket = new JButton();

		setLayout(new java.awt.CardLayout());

		m_jPanelMap.setLayout(new java.awt.BorderLayout());

		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		/*
		 * m_jbtnReservations.setIcon(new
		 * javax.swing.ImageIcon(getClass().getResource(
		 * "/com/openbravo/images/date.png"))); // NOI18N
		 * m_jbtnReservations.setText(AppLocal.getIntString(
		 * "button.reservations")); // NOI18N
		 * m_jbtnReservations.setFocusPainted(false);
		 * m_jbtnReservations.setFocusable(false);
		 * m_jbtnReservations.setMargin(new java.awt.Insets(8, 14, 8, 14));
		 * m_jbtnReservations.setRequestFocusEnabled(false);
		 * m_jbtnReservations.addActionListener(new
		 * java.awt.event.ActionListener() { public void
		 * actionPerformed(java.awt.event.ActionEvent evt) {
		 * m_jbtnReservationsActionPerformed(evt); } });
		 */
		// jPanel2.add(m_jbtnReservations);

		m_jbtnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/refresh.png"))); // NOI18N
		m_jbtnRefresh.setText(AppLocal.getIntString("button.reloadticket"));
		m_jbtnRefresh.setFocusPainted(false);
		m_jbtnRefresh.setFocusable(false);
		// m_jbtnRefresh.setMargin(new java.awt.Insets(0, 0, 0, 0));
		m_jbtnRefresh.setRequestFocusEnabled(false);
		m_jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jbtnRefreshActionPerformed(evt);
			}
		});
		jPanel2.add(m_jbtnRefresh);

		// add direct bonieren button
		// TODO make a check if a direct bon table is available
		btn_promptTicket
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/promptTicket.png")));
		btn_promptTicket.setText(AppLocal.getIntString("button.promptTicket"));
		btn_promptTicket.setFocusPainted(false);
		btn_promptTicket.setFocusable(false);
		// jButton1.setMargin(new java.awt.Insets(8, 14, 8, 14));
		btn_promptTicket.setMargin(new java.awt.Insets(0, 0, 0, 0));
		btn_promptTicket.setRequestFocusEnabled(false);
		btn_promptTicket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_promptTicketActionPerformed(evt);
			}
		});
		jPanel2.add(btn_promptTicket);

		jPanel2.add(m_jText);

		jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);
		jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_END);

		m_jPanelMap.add(jPanel1, java.awt.BorderLayout.NORTH);

		add(m_jPanelMap, "map");

		m_jbtnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/logout.png"))); // NOI18N
		m_jbtnLogout.setText(AppLocal.getIntString("button.logout"));
		m_jbtnLogout.setFocusPainted(false);
		m_jbtnLogout.setFocusable(false);
		// m_jbtnLogout.setMargin(new java.awt.Insets(0,0,0,0));
		m_jbtnLogout.setRequestFocusEnabled(false);
		m_jbtnLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jbtnLogoutActionPerformed(evt);
			}
		});

		jPanel3.add(m_jbtnLogout);
	}

	private void m_jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnRefreshActionPerformed

		m_PlaceClipboard = null;
		customer = null;
		loadTickets();
		printState();

	}// GEN-LAST:event_m_jbtnRefreshActionPerformed

	private void m_jbtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnRefreshActionPerformed

		// TODO logout
		((JPrincipalApp) m_App.getAppUserView()).getAppview().closeAppView();
	}

	private void m_jbtnReservationsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnReservationsActionPerformed

		showView("res");
		m_jreservations.activate();

	}// GEN-LAST:event_m_jbtnReservationsActionPerformed

	private class MyActionListener implements ActionListener {

		private Place m_place;

		public MyActionListener(Place place) {
			m_place = place;
		}

		public void actionPerformed(ActionEvent evt) {
			m_restaurantmap.setPromptTicket(false);
			if (m_PlaceClipboard == null) {

				if (customer == null) {
					// tables

					// check if the sharedticket is the same
					TicketInfo ticket = getTicketInfo(m_place);

					// check
					if (ticket == null && !m_place.hasPeople()) {
						// Empty table and checked

						// table occupied
						ticket = new TicketInfo();
						try {
							dlReceipts.insertSharedTicket(m_place.getId(), ticket);
						} catch (BasicException e) {
							new MessageInf(e).show(m_App, JTicketsBagRestaurantMap.this); // Glup.
							// But
							// It
							// was
							// empty.
						}
						m_place.setPeople(true);
						setActivePlace(m_place, ticket);

					} else if (ticket == null && m_place.hasPeople()) {
						// The table is now empty
						new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(m_App,
								JTicketsBagRestaurantMap.this);
						m_place.setPeople(false); // fixed

					} else if (ticket != null && !m_place.hasPeople()) {
						// The table is now full
						new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(m_App,
								JTicketsBagRestaurantMap.this);
						m_place.setPeople(true);

					} else { // both != null
						// Full table
						// m_place.setPeople(true); // already true
						setActivePlace(m_place, ticket);
					}
				} else {
					// receiving customer.

					// check if the sharedticket is the same
					TicketInfo ticket = getTicketInfo(m_place);
					if (ticket == null) {
						// receive the customer
						// table occupied
						ticket = new TicketInfo();

						try {
							ticket.setCustomer(
									customer.getId() == null ? null : dlSales.loadCustomerExt(customer.getId()));
						} catch (BasicException e) {
							MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
									AppLocal.getIntString("message.cannotfindcustomer"), e);
							msg.show(m_App, JTicketsBagRestaurantMap.this);
						}

						try {
							dlReceipts.insertSharedTicket(m_place.getId(), ticket);
						} catch (BasicException e) {
							new MessageInf(e).show(m_App, JTicketsBagRestaurantMap.this); // Glup.
							// But
							// It
							// was
							// empty.
						}
						m_place.setPeople(true);

						m_PlaceClipboard = null;
						customer = null;

						setActivePlace(m_place, ticket);
					} else {
						// TODO: msg: The table is now full
						new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(m_App,
								JTicketsBagRestaurantMap.this);
						m_place.setPeople(true);
						m_place.getButton().setEnabled(false);
					}
				}
			} else {
				// check if the sharedticket is the same
				TicketInfo ticketclip = getTicketInfo(m_PlaceClipboard);

				if (ticketclip == null) {
					new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(m_App,
							JTicketsBagRestaurantMap.this);
					m_PlaceClipboard.setPeople(false);
					m_PlaceClipboard = null;
					customer = null;
					printState();
				} else {
					// tenemos que copiar el ticket del clipboard
					if (m_PlaceClipboard == m_place) {
						// the same button. Canceling.
						Place placeclip = m_PlaceClipboard;
						m_PlaceClipboard = null;
						customer = null;
						printState();
						setActivePlace(placeclip, ticketclip);
					} else if (!m_place.hasPeople()) {
						// Moving the receipt to an empty table
						TicketInfo ticket = getTicketInfo(m_place);

						if (ticket == null) {
							try {
								dlReceipts.insertSharedTicket(m_place.getId(), ticketclip);
								m_place.setPeople(true);
								dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
								m_PlaceClipboard.setPeople(false);
							} catch (BasicException e) {
								new MessageInf(e).show(m_App, JTicketsBagRestaurantMap.this); // Glup.
								// But
								// It
								// was
								// empty.
							}

							m_PlaceClipboard = null;
							customer = null;
							printState();

							// No hace falta preguntar si estaba bloqueado
							// porque ya lo estaba antes
							// activamos el ticket seleccionado
							setActivePlace(m_place, ticketclip);
						} else {
							// Full table
							new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull"))
									.show(m_App, JTicketsBagRestaurantMap.this);
							m_PlaceClipboard.setPeople(true);
							printState();
						}
					} else {
						// Merge the lines with the receipt of the table
						TicketInfo ticket = getTicketInfo(m_place);

						if (ticket == null) {
							// The table is now empty
							new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty"))
									.show(m_App, JTicketsBagRestaurantMap.this);
							m_place.setPeople(false); // fixed
						} else {
							// asks if you want to merge tables
							if (JOptionPane.showConfirmDialog(JTicketsBagRestaurantMap.this,
									AppLocal.getIntString("message.mergetablequestion"),
									AppLocal.getIntString("message.mergetable"),
									JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								// merge lines ticket

								try {
									dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
									m_PlaceClipboard.setPeople(false);
									if (ticket.getCustomer() == null) {
										ticket.setCustomer(ticketclip.getCustomer());
									}
									for (TicketLineInfo line : ticketclip.getLines()) {
										ticket.addLine(line);
									}
									dlReceipts.updateSharedTicket(m_place.getId(), ticket);
								} catch (BasicException e) {
									new MessageInf(e).show(m_App, JTicketsBagRestaurantMap.this); // Glup.
									// But
									// It
									// was
									// empty.
								}

								m_PlaceClipboard = null;
								customer = null;
								printState();

								setActivePlace(m_place, ticket);
							} else {
								// Cancel merge operations
								Place placeclip = m_PlaceClipboard;
								m_PlaceClipboard = null;
								customer = null;
								printState();
								setActivePlace(placeclip, ticketclip);
							}
						}
					}
				}
			}
		}
	}

	private void btn_promptTicketActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		promptTicket();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel m_jPanelMap;
	private javax.swing.JLabel m_jText;
	private javax.swing.JButton m_jbtnRefresh;
	private javax.swing.JButton m_jbtnReservations;
	private javax.swing.JButton btn_promptTicket;
	// End of variables declaration//GEN-END:variables

}
