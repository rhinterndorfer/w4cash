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

package com.openbravo.data.gui;

import javax.swing.*;
import java.awt.*;
import com.openbravo.data.loader.IRenderString;
import com.openbravo.pos.util.PropertyUtil;

public class ListCellRendererBasic extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	private IRenderString m_renderer;
	private Font newFont;

	/** Creates a new instance of ListCellRendererBasic */
	public ListCellRendererBasic(IRenderString renderer) {
		m_renderer = renderer;
	}

	public ListCellRendererBasic(IRenderString renderer, int fontsize) {
		m_renderer = renderer;
		setFontsize(fontsize);
	}

	public void setFontsize(int fontsize) {
		this.newFont = new Font(getFont().getName(), getFont().getStyle(), fontsize);
	}
	
	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		String s = m_renderer.getRenderString(value);
		setText((s == null || s.equals("")) ? " " : s); // Un espacio en caso de
														// nulo para que no deja
														// la celda chiquitita.
		label.setFont(newFont);
		return label;
	}

}
