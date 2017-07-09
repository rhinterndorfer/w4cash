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

import java.io.Serializable;
import com.openbravo.data.loader.IKeyed;
import java.util.Date;

import org.joda.time.DateTime;

/**
 *
 * @author adrianromero
 */
public class PriceZoneProductInfo implements Serializable, IKeyed {
    
    private static final long serialVersionUID = -2705212098816473043L;
    private String id;
    private String name;
    private int isActiv;
    private int isCustomer;
    private int isPriceRise;
    private java.util.Date dateFrom;
    private java.util.Date dateTill;
    private String location;
    private String product;
    private double priceSellGross;
    
    
    public PriceZoneProductInfo(String id, String name, int isActiv, int isCustomer, 
    		java.util.Date dateFrom, java.util.Date dateTill, 
    		String location, String product, double priceSellGross, int isPriceRise) {
        this.id = id;
        this.name = name;
        this.isActiv = isActiv;
        this.isCustomer = isCustomer;
        this.isPriceRise = isPriceRise;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
        this.location = location;
        this.product = product;
        this.priceSellGross = priceSellGross;
    }
    
    public Object getKey() {
        return id;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public int getIsActiv() {
        return isActiv;
    }
    
    public int getIsCustomer() {
        return isCustomer;
    }
    
    public int getIsPriceRise() {
        return isPriceRise;
    }

    public java.util.Date getDateFrom() {
        return dateFrom;
    }
    
    public java.util.Date getDateTill() {
        return dateTill;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getProduct() {
        return product;
    }
    
    public Double getPriceSellGross() {
        return priceSellGross;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
