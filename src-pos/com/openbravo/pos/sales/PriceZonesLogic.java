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

package com.openbravo.pos.sales;

import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.PriceZoneProductInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.TicketTaxInfo;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Today;
import org.joda.time.DateTime;

public class PriceZonesLogic {
    
    private List<PriceZoneProductInfo> priceZoneProductList;
    private Map<String, List<PriceZoneProductInfo>> priceZonePricesByProduct;
    
    public PriceZonesLogic(List<PriceZoneProductInfo> priceZoneProductList) {
        this.priceZoneProductList = priceZoneProductList;
        
        this.priceZonePricesByProduct = new HashMap<String, List<PriceZoneProductInfo>>();
        for(PriceZoneProductInfo priceZoneProductInfo : priceZoneProductList)
        {
        	if(priceZonePricesByProduct.containsKey(priceZoneProductInfo.getProduct()))
        	{
        		priceZonePricesByProduct.get(priceZoneProductInfo.getProduct()).add(priceZoneProductInfo);
        	}
        	else
        	{
        		List<PriceZoneProductInfo> productList = new ArrayList<PriceZoneProductInfo>();
        		productList.add(priceZoneProductInfo);
        		priceZonePricesByProduct.put(priceZoneProductInfo.getProduct(), productList);
        	}
        }
    }
    
    public List<PriceZoneProductInfo> getByProduct(String product)
    {
    	return priceZonePricesByProduct.get(product);
    }
    
    public Double getPrice(String product, CustomerInfoExt customer, String location, double taxRate)
    {
    	
    	Double price = Double.MAX_VALUE;
    	List<PriceZoneProductInfo> productList = getByProduct(product);
    	if(productList != null)
    	{
    		for(PriceZoneProductInfo pz : productList)
    		{
    			if(
    					(pz.getIsCustomer() == 0 || (customer != null && customer.getPrices_Zone() != null && customer.getPrices_Zone().equals(pz.getId())))
    					&& (pz.getDateFrom() == null || pz.getDateFrom().compareTo(new java.util.Date()) < 0)
    					&& (pz.getDateTill() == null || pz.getDateTill().compareTo(new java.util.Date()) > 0)
    					&& (pz.getLocation() == null || pz.getLocation().equals(location))
    					&& pz.getPriceSellGross() < price * (1+taxRate)
					)
    			{
    				price = pz.getPriceSellGross() / (1+taxRate);
    			}
    		
    		}
    	}
    	
    	return price;
    }
}
