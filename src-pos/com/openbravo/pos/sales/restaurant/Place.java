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
	private static final Icon ICO_OCU = new ImageIcon(Place.class.getResource("/com/openbravo/images/edit_group.png"));
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
//			npe.printStackTrace();
			m_iWidth = 100;
		}
		try {
			m_iHeight = dr.getInt(7);
		} catch (NullPointerException npe) {
//			npe.printStackTrace();
			m_iHeight = 60;
		}
		m_bPeople = false;
		m_btn = new JPlaceButton(this);

		m_btn.setFocusPainted(false);
		m_btn.setFocusable(false);
		m_btn.setRequestFocusEnabled(false);
		m_btn.setHorizontalTextPosition(SwingConstants.CENTER);
		m_btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		m_btn.setIcon(ICO_FRE);
		m_btn.setText(m_sName);
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

	public int getWidth(){
		return m_iWidth;
	}

	public int getHeight(){
		return m_iHeight;
	}
	
	public String getFloor() {
		return m_sfloor;
	}

	public JPlaceButton getButton() {
		return m_btn;
	}

	public boolean hasPeople() {
		return m_bPeople;
	}

	public void setPeople(boolean bValue) {
		m_bPeople = bValue;
		m_btn.setIcon(bValue ? ICO_OCU : ICO_FRE);
	}

//	public void setButtonBounds(AppView app) {
//		int width = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-table-width", "60"));
//		int height = Integer.parseInt(PropertyUtil.getProperty(app, "Ticket.Buttons", "button-table-height", "40"));
//
//		setButtonBounds(app, width, height);
//	}

	public void setButtonBounds(AppView app, int width, int height) {
		this.m_iWidth = width;
		this.m_iHeight = height;
		m_btn.setBounds(m_ix, m_iy, width, height);
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

	public void setSize(int width, int height) {
		this.m_iWidth = width;
		this.m_iHeight = height;

		this.editor.setPlaceSize(m_iWidth, m_iHeight);
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

}
