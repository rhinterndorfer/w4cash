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

import com.openbravo.format.Formats;
import com.openbravo.pos.util.RoundUtils;


public class TicketTaxInfo {
    
    private TaxInfo tax;
    
    private double grosstotal;
    private double taxtotal;
            
    /** Creates a new instance of TicketTaxInfo */
    public TicketTaxInfo(TaxInfo tax) {
        this.tax = tax;
        
        grosstotal = 0.0;
        taxtotal = 0.0;
    }
    
    public TaxInfo getTaxInfo() {
        return tax;
    }
    
    private double gross;
    // only use when base price is not rounded! otherwise use addGross!!!
    public void add(double dValue) {
    	gross = RoundUtils.round(dValue * (1 + tax.getRate()));
    	grosstotal += gross;
    	grosstotal = RoundUtils.round(grosstotal);
    	taxtotal += RoundUtils.round(dValue * (1 + tax.getRate())) - RoundUtils.round(dValue);
    	taxtotal = RoundUtils.round(taxtotal);
    }
    
    public void addGross(double gross, double tax) {
    	grosstotal += gross;
    	grosstotal = RoundUtils.round(grosstotal);
    	taxtotal += tax;
    	taxtotal = RoundUtils.round(taxtotal);
    }
    
    public double getSubTotal() {    
        return grosstotal - taxtotal;
    }
    
    public double getTax() {       
        return taxtotal;
    }
    
    public double getTotal() {         
        return grosstotal;
    }
    
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }
    public String printTax() {
        return Formats.CURRENCY.formatValue(new Double(getTax()));
    }    
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }    
}
