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

package com.openbravo.pos.printer.escpos;

import com.openbravo.pos.printer.DeviceTicket;

/**
 *
 * @author adrianromero
 */
public class DeviceDisplayGlancetron extends DeviceDisplaySerial {
    
    private UnicodeTranslator trans;
    
    public DeviceDisplayGlancetron(PrinterWritter display) { 
        trans = new UnicodeTranslatorEur();
        init(display);                
    }
   
    @Override
    public void initVisor() {
    	display.write(new byte[]{0x1b, 0x3a}); // clear
    	display.write(new byte[]{0x1b, 0x60, 0x01}); // turn off cursor
        display.flush();
    }

    public void repaintLines() {
        display.write(new byte[]{0x1b, 0x3d, 0x30, 0x30}); // VISOR HOME
        display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20)));
        
        display.write(new byte[]{0x1b, 0x3d, 0x30, 0x31}); // VISOR HOME
        display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20)));        
        display.flush();
    }
}
