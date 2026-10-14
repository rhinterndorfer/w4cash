//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

package com.openbravo.pos.payment;

import java.util.Locale;

import com.openbravo.format.Formats;

public abstract class PaymentInfo {
    
    public abstract String getName();
    public abstract double getTotal();
    public abstract PaymentInfo copyPayment();
    public abstract String getTransactionID();
    
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }
    
    
    public String buildPaymentQRCode(String owner, String bic, String iban, String reference) {
    	String newLine = System.getProperty("line.separator");
    	return "BCD".concat(newLine)
    			.concat("002").concat(newLine)
    			.concat("1").concat(newLine)
    			.concat("SCT").concat(newLine)
    			.concat(bic == null ? "" : bic).concat(newLine)
    			.concat(owner == null ? "" : owner).concat(newLine)
    			.concat(iban == null ? "" : iban.replace(" ", "")).concat(newLine)
    			.concat("EUR").concat(String.format(Locale.US, "%.2f",getTotal())).concat(newLine)
    			.concat(newLine)
    			.concat(reference == null ? "" : reference)
    			;
    }
}
