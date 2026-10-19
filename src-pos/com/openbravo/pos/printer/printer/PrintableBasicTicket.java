//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2009 Openbravo, S.L.
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

package com.openbravo.pos.printer.printer;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.printer.ticket.BasicTicket;
import com.openbravo.pos.printer.ticket.PrintItem;
import com.openbravo.pos.util.Log;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 *
 * @author adrianromero
 */
public class PrintableBasicTicket implements Printable {

	private int imageable_width;
	private int imageable_height;
	private int imageable_x;
	private int imageable_y;

	private BasicTicket ticket;
	private Graphics2D dbGraphics;
	private Hashtable<Integer, Integer> dbGraphics_page_y = new Hashtable<Integer, Integer>();
	

	public PrintableBasicTicket(BasicTicket ticket, int imageable_x, int imageable_y, int imageable_width,
			int imageable_height) {

		this.ticket = ticket;
		this.imageable_x = imageable_x;
		this.imageable_y = imageable_y;
		this.imageable_width = imageable_width;
		this.imageable_height = imageable_height;
	}

	public PrintableBasicTicket(BasicTicket ticket, int imageable_x, int imageable_y, int imageable_width,
			int imageable_height, Graphics2D dbGraphics ) {
		this.ticket = ticket;
		this.imageable_x = imageable_x;
		this.imageable_y = imageable_y;
		this.imageable_width = imageable_width;
		this.imageable_height = imageable_height;
		
		
		this.dbGraphics = dbGraphics;

	}

	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

		Graphics2D g2d = (Graphics2D) graphics;

		int line = 0;
		int currentpage = 0;
		int currentpagey = 0;
		boolean printed = false;

		Integer dbGraphics_y = 0;
		if(dbGraphics_page_y.contains(pageIndex-1)) {
			dbGraphics_y = dbGraphics_page_y.get(pageIndex-1);
		}
		
		g2d.translate(imageable_x, imageable_y);

		java.util.List<PrintItem> commands = ticket.getCommands();
		while (line < commands.size()) {

			int itemheight = commands.get(line).getHeight();

			if (currentpagey + itemheight <= imageable_height) {
				currentpagey += itemheight;
			} else {
				currentpage++;
				currentpagey = imageable_y + itemheight; // add top margin
			}
			
			dbGraphics_y += itemheight;

			if (currentpage < pageIndex) {
				line++;
			} else if (currentpage == pageIndex) {
				printed = true;
				commands.get(line).draw(g2d, 0, currentpagey - itemheight, imageable_width);
				if (this.dbGraphics != null && !dbGraphics_page_y.contains(pageIndex))
					commands.get(line).draw(this.dbGraphics, 0, dbGraphics_y - itemheight, imageable_width);
				line++;
			} else if (currentpage > pageIndex) {
				line++;
			}
		}
		
		if(!dbGraphics_page_y.contains(pageIndex)) {
			dbGraphics_page_y.put(pageIndex, dbGraphics_y);
		}

		return printed ? Printable.PAGE_EXISTS : Printable.NO_SUCH_PAGE;
	}
}
