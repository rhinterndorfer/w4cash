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
import com.sun.media.jfxmedia.effects.EqualizerBand;
import com.openbravo.basic.BasicException;

public class PlaceSplit implements SerializableRead, java.io.Serializable, Comparable<PlaceSplit> {

	private static final long serialVersionUID = 2484674619046089773L;

	private String m_sId;
	private String m_sName;
	private String m_sFloorName;
	private Integer m_occupied;

	public PlaceSplit() {
	}

	public void readValues(DataRead dr) throws BasicException {
		m_sId = dr.getString(1);
		m_sName = dr.getString(2);
		m_sFloorName = dr.getString(3);
		m_occupied = dr.getInt(4);
	}

	public void setId(String id) {
		m_sId = id;
	}

	public String getId() {
		return m_sId;
	}

	public void setName(String name) {
		m_sName = name;
	}

	public String getName() {
		return m_sName;
	}

	public String getFloorName() {
		return m_sFloorName == null ? "" : m_sFloorName;
	}

	public Boolean getOccupied() {
		return m_occupied != null && m_occupied > 0 ? true : false;
	}

	public Boolean getLocked() {
		return m_occupied != null && m_occupied > 1 ? true : false;
	}

	@Override
	public int compareTo(PlaceSplit o) {
		if (m_sId == null)
			return -10;
		if (o == null || o.m_sId == null)
			return 10;
		return this.m_sId.compareTo(o.m_sId);
	}

	@Override
	public int hashCode() {
		return this.m_sId == null ? -1 : this.m_sId.hashCode();
	}

	@Override
	public boolean equals(Object c) {
		if (m_sId == null && c == null)
			return true;
		if (c == null || !c.getClass().equals(PlaceSplit.class))
			return false;
		return this.m_sId.compareTo(((PlaceSplit) c).m_sId) == 0 ? true : false;
	}
}
