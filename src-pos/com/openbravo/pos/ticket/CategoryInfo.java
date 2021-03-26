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
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;

import java.awt.Color;
import java.awt.image.*;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.pos.sales.restaurant.PlaceSplit;

/**
 *
 * @author Adrian
 * @version
 */
public class CategoryInfo implements IKeyed, Comparable<CategoryInfo> {

	private static final long serialVersionUID = 8612449444103L;
	private String m_sID;
	private String m_sName;
	private BufferedImage m_Image;
	private Color bgColor;
	private Integer m_SortOrder;
	private int m_printerId = -1;

	/** Creates new CategoryInfo */
	public CategoryInfo(String id, String name, BufferedImage image, String color, Integer sortOrder, int printerId) {
		m_sID = id;
		m_sName = name;
		m_Image = image;
		m_SortOrder = sortOrder;
		m_printerId = printerId;
		this.setBgColor(color);
	}

	public Object getKey() {
		return m_sID;
	}

	public void setID(String sID) {
		m_sID = sID;
	}
	
	public void setSortOrder(int sortOrder){
		this.m_SortOrder = sortOrder;
	}

	public Integer getSortOrder() {
		return this.m_SortOrder == null ? 0 : this.m_SortOrder;
	}

	public String getID() {
		return m_sID;
	}

	public String getName() {
		return m_sName;
	}

	public void setName(String sName) {
		m_sName = sName;
	}

	public BufferedImage getImage() {
		return m_Image;
	}

	public void setImage(BufferedImage img) {
		m_Image = img;
	}

	@Override
	public String toString() {
		return m_sName;
	}

	public static SerializerRead getSerializerRead() {
		return new SerializerRead() {
			public Object readValues(DataRead dr) throws BasicException {
				return new CategoryInfo(dr.getString(1), dr.getString(2), ImageUtils.readImage(dr.getBytes(3)), dr.getString(4),
						dr.getInt(5), dr.getInt(6));
			}
		};
	}

	public int getPrinterId() {
		return this.m_printerId ;
	}
	
	public void setPrinterId(int id) {
		this.m_printerId = id;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(String color) {
    	String col[] = (color == null ? null : color.toString().split(";"));

    	if(col == null)
    		this.bgColor = null;
		try {
			this.bgColor =
					new Color(Integer.parseInt(col[0]), Integer.parseInt(col[1]), Integer.parseInt(col[2]));
		} catch (NumberFormatException ex) {
			bgColor = null;
		} catch (NullPointerException ex) {
			bgColor = null;
		}
    }
	
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	
	@Override
	public int compareTo(CategoryInfo o) {
		if (m_sID == null)
			return -10;
		if (o == null || o.m_sID == null)
			return 10;
		return this.m_sID.compareTo(o.m_sID);
	}

	@Override
	public int hashCode() {
		return this.m_sID == null ? -1 : this.m_sID.hashCode();
	}

	@Override
	public boolean equals(Object c) {
		if (m_sID == null && c == null)
			return true;
		if (c == null || !c.getClass().equals(CategoryInfo.class))
			return false;
		return this.m_sID.compareTo(((CategoryInfo) c).m_sID) == 0 ? true : false;
	}
	
}
