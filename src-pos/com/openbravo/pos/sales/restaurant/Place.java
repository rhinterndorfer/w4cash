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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import com.openbravo.data.gui.NullIcon;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.mant.JPlaceButton;
import com.openbravo.pos.mant.PlacesEditor;
import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.basic.BasicException;

public class Place implements SerializableRead, java.io.Serializable {

	private static final long serialVersionUID = 8652254694281L;
	//private static final Icon ICO_OCU = new ImageIcon(Place.class.getResource("/com/openbravo/images/edit_group.png"));
	private Color placeOccupiedColor = new Color(255, 255, 0, 192);
	private Icon ICO_OCU = new ColorIcon(22, 22, placeOccupiedColor);
	private static final Icon ICO_FRE = new NullIcon(22, 22);
	

	private String m_sId;

	public String getSId() {
		return m_sId;
	}

	public void setSId(String m_sId) {
		this.m_sId = m_sId;
	}

	private String m_sName;

	public String getSName() {
		return m_sName;
	}

	public void setSName(String m_sName) {
		this.m_sName = m_sName;
	}

	private int m_ix;
	private int m_iy;
	private String m_sfloor;

	private boolean m_bPeople;
	private JPlaceButton m_btn;

	private PlacesEditor editor;
	private int m_iWidth;
	private int m_iHeight;
	private int m_iFontsize;
	private int m_iFontsizeCurrent;
	private Color m_iFontColor;

	/** Creates a new instance of TablePlace */
	public Place() {
	}

	public void readValues(DataRead dr) throws BasicException {
		m_sId = dr.getString(1);
		m_sName = dr.getString(2);
		m_ix = dr.getInt(3).intValue();
		m_iy = dr.getInt(4).intValue();
		m_sfloor = dr.getString(5);
		try {
			m_iWidth = dr.getInt(6);
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			m_iWidth = 100;
		}
		try {
			m_iHeight = dr.getInt(7);
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			m_iHeight = 60;
		}

		try {
			m_iFontsize = dr.getInt(8);
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			m_iFontsize = 12;
		}
		m_iFontsizeCurrent = m_iFontsize; 
				
		try {
			String cc[] = dr.getString(9).split(";");
			m_iFontColor = new Color(Integer.parseInt(cc[0]), Integer.parseInt(cc[1]), Integer.parseInt(cc[2]));
			if (m_iFontColor == null) {
				m_iFontColor = Color.black;
			}
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			m_iFontColor = Color.black;
		} catch (BasicException eD) {
			m_iFontColor = Color.black;
		} catch (NumberFormatException ex) {
			m_iFontColor = Color.black;
		}

		m_bPeople = false;
		m_btn = new JPlaceButton(this);

		m_btn.setFocusPainted(false);
		m_btn.setFocusable(false);
		m_btn.setRequestFocusEnabled(false);
		m_btn.setHorizontalTextPosition(SwingConstants.CENTER);
		m_btn.setVerticalTextPosition(SwingConstants.CENTER);
		m_btn.setIcon(ICO_FRE);
		ICO_OCU = new ColorIcon(m_iWidth, m_iHeight, placeOccupiedColor);
		// m_btn.setText(m_sName);
		// m_btn.setFont(new Font(m_btn.getFont().getName(),
		// m_btn.getFont().getStyle(), m_iFontsize));
		m_btn.setForeground(m_iFontColor);

		// fontsize
		Font font = m_btn.getFont();

		// The text
		StringBuilder sb = new StringBuilder();
		sb.append("<html><div style='text-align: center; font-family:");
		sb.append(font.getName());
		sb.append(";font-size:");
		sb.append(m_iFontsize);
		sb.append(";'>");
		sb.append(m_sName);
		sb.append("</div></html>");

		m_btn.setText(sb.toString());

		m_btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
	}

	public String getId() {
		return m_sId;
	}

	public String getName() {
		return m_sName;
	}

	public int getX() {
		return m_ix;
	}

	public int getY() {
		return m_iy;
	}

	public int getWidth() {
		return m_iWidth;
	}

	public int getHeight() {
		return m_iHeight;
	}

	public String getFloor() {
		return m_sfloor;
	}

	public int getFontsize() {
		return this.m_iFontsize;
	}

	public int getFontsizeCurrent() {
		return this.m_iFontsizeCurrent;
	}

	
	public JPlaceButton getButton() {
		return m_btn;
	}

	public boolean hasPeople() {
		return m_bPeople;
	}

	public void setPeople(boolean bValue) {
		m_bPeople = bValue;
		
		Icon ocu = ICO_OCU;
		m_btn.setIcon(bValue ? ocu : ICO_FRE);
		Font font = m_btn.getFont();
		
		if (m_bPeople) {
			// The text
			StringBuilder sb = new StringBuilder();
			sb.append("<html><div style='");
			sb.append("text-align: center;font-weight: bold;");
			sb.append("font-family:");
			sb.append(font.getName());
			sb.append(";font-size:");
			sb.append(m_iFontsizeCurrent);
			sb.append(";'>");
			sb.append(m_sName);
			sb.append("</div></html>");

			m_btn.setText(sb.toString());
		} else {
			// The text
			StringBuilder sb = new StringBuilder();
			sb.append("<html><div style='");
			sb.append("text-align: center;font-weight: bold;");
			sb.append("font-family:");
			sb.append(font.getName());
			sb.append(";font-size:");
			sb.append(m_iFontsizeCurrent);
			sb.append(";'>");
			sb.append(m_sName);
			sb.append("</div></html>");

			m_btn.setText(sb.toString());
		}
	}

	public void setButtonBounds(AppView app, int width, int height) {
		this.m_iWidth = width;
		this.m_iHeight = height;
		m_btn.setBounds(m_ix, m_iy, width, height);
		m_btn.repaint();
	}

	public void setX(int x) {
		this.m_ix = x;
	}

	public void setY(int y) {
		this.m_iy = y;
	}

	public void setXYCoordinates(int x, int y) {
		this.m_ix = x;
		this.m_iy = y;

		this.editor.setXYCoordinates(x, y);
	}

	public PlacesEditor getEditor() {
		return this.editor;
	}

	public void setEditor(PlacesEditor editor) {
		this.editor = editor;
	}

	public void selectPlace() {
		if (this.editor == null) {
			return;
		}
		this.editor.selectPlace(this);
	}

	public void setFontsizeCurrent(int fontsize) {
		this.m_iFontsizeCurrent = fontsize;
		Font newFont = new Font(m_btn.getFont().getName(), m_btn.getFont().getStyle(), m_iFontsizeCurrent);
		m_btn.setFont(newFont);
		setPeople(hasPeople());
		m_btn.repaint();
	}

	public void setFontColor(Color fontcolor) {
		m_btn.setForeground(fontcolor);
	}
}
