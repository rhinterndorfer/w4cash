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

import com.openbravo.pos.printer.ticket.BasicTicket;
import com.openbravo.pos.printer.ticket.PrintItem;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 *
 * @author adrianromero
 */
public class PageableBasicTicket implements Pageable {

    private int imageable_width;
    private int imageable_height;
    private int imageable_x;
    private int imageable_y;

    private BasicTicket ticket;

    public PageableBasicTicket(BasicTicket ticket, int imageable_x, int imageable_y, int imageable_width, int imageable_height) {
        this.ticket = ticket;
        this.imageable_x = imageable_x;
        this.imageable_y = imageable_y;
        this.imageable_width = imageable_width;
        this.imageable_height = imageable_height;
    }

	@Override
	public int getNumberOfPages() {
		return 1;
	}

	@Override
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
		Paper paper = new Paper();
	    paper.setSize(imageable_width, imageable_height); 
	    paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
		
		PageFormat pf = new PageFormat();
		pf.setPaper(paper);
		return pf;
	}

	@Override
	public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
		return new PrintableBasicTicket(ticket, imageable_x, imageable_y,
				imageable_width, imageable_height);
	}

    
}
