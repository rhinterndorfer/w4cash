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
    private AppView app;
    private Boolean printToDB;
    private DataLogicSales dlSales = null;
    private String id;

    
    public PrintableBasicTicket(BasicTicket ticket, int imageable_x, int imageable_y, int imageable_width, int imageable_height) {
    
        this.ticket = ticket;
        this.imageable_x = imageable_x;
        this.imageable_y = imageable_y;
        this.imageable_width = imageable_width;
        this.imageable_height = imageable_height;
    }
    
    
    public PrintableBasicTicket(BasicTicket ticket, int imageable_x, int imageable_y, int imageable_width, int imageable_height, AppView app, Boolean printToDB, String id) {
        this.ticket = ticket;
        this.imageable_x = imageable_x;
        this.imageable_y = imageable_y;
        this.imageable_width = imageable_width;
        this.imageable_height = imageable_height;
        this.app = app;
        dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        this.printToDB = printToDB;
        this.id = id;
        
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        Graphics2D g2d = (Graphics2D) graphics;
        Graphics2D g2dImage = null;
        BufferedImage bufImg = null;
        int scale = 10;

        if(printToDB) {
        	bufImg = new BufferedImage(imageable_width * scale, imageable_height * scale, BufferedImage.TYPE_INT_ARGB);
        	g2dImage = bufImg.createGraphics();
        	g2dImage.scale(scale, scale);
        	g2dImage.setComposite(AlphaComposite.Clear);
        	g2dImage.fillRect(0, 0, imageable_width, imageable_height);
        	
        	g2dImage.setComposite(AlphaComposite.Src);
        	g2dImage.setColor(Color.black);
        }
        
        
        int line = 0;
        int currentpage = 0;
        int currentpagey = 0;
        boolean printed = false;


        g2d.translate(imageable_x, imageable_y);
        if(g2dImage != null) g2dImage.translate(imageable_x, imageable_y);

        java.util.List<PrintItem> commands = ticket.getCommands();

        while (line < commands.size()) {

            int itemheight = commands.get(line).getHeight();

            if (currentpagey + itemheight <= imageable_height) {
                currentpagey += itemheight;
            } else {
                currentpage ++;
                currentpagey = imageable_y + itemheight; // add top margin
            }

            if (currentpage < pageIndex) {
                line ++;
            } else if (currentpage == pageIndex) {
                printed = true;
                commands.get(line).draw(g2d, 0, currentpagey - itemheight, imageable_width);
                if(g2dImage != null) commands.get(line).draw(g2dImage, 0, currentpagey - itemheight, imageable_width);

                line ++;
            } else if (currentpage > pageIndex) {
                line ++;
            }
        }
        
        if(g2dImage != null && printed) {
            try {
            	BufferedImage bufImgOut = bufImg.getSubimage(0, 0, imageable_width * scale, currentpagey * scale);
            	ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufImgOut, "png", os);
                String base64content = Base64.getEncoder().encodeToString(os.toByteArray());
                //byte[] byteArray = base64content.getBytes("ASCII");
                dlSales.addTicketImage(id, pageIndex, "image/png", base64content);
            } catch(Exception e) {
            	Log.Exception(e);
            }
        }
        
        
        
        return printed
            ? Printable.PAGE_EXISTS
            : Printable.NO_SUCH_PAGE;
    }
}
