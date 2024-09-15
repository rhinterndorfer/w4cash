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

import java.io.*;

import com.openbravo.pos.util.PropertyUtil;
import com.openbravo.pos.util.RoundUtils;
import com.openbravo.pos.util.StringUtils;

import net.sf.jasperreports.engine.util.Java14BigDecimalHandler;

import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;

import java.util.Date;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.jfree.util.Log;

import java.util.Properties;

/**
 *
 * @author adrianromero
 */
public class TicketLineInfo implements SerializableWrite, SerializableRead, Serializable {

	private static final long serialVersionUID = 6608012948284450199L;
	private String m_sTicket;
	private int m_iLine;
	private double multiply;
	private double multiplyClone;
	private boolean multiplyCloneValid;
	private double price;
	private TaxInfo tax;
	private Properties attributes;
	private String productid;
	private String attsetinstid;
	private String unit = "";
	private String width = "";
	private String length = "";
	private String height = "";
	private String count = "";

	/** Creates new TicketLineInfo */
	public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax, Properties props) {
		init(productid, null, dMultiply, dPrice, tax, props);
	}

	public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax) {
		init(productid, null, dMultiply, dPrice, tax, new Properties());
	}

	public TicketLineInfo(String productid, String productname, String producttaxcategory, double dMultiply,
			double dPrice, TaxInfo tax) {
		Properties props = new Properties();
		props.setProperty("product.name", productname);
		props.setProperty("product.taxcategoryid", producttaxcategory);
		init(productid, null, dMultiply, dPrice, tax, props);
	}

	public TicketLineInfo(String productname, String producttaxcategory, double dMultiply, double dPrice, TaxInfo tax) {

		Properties props = new Properties();
		props.setProperty("product.name", productname);
		props.setProperty("product.taxcategoryid", producttaxcategory);
		init(null, null, dMultiply, dPrice, tax, props);
	}

	public TicketLineInfo(String productname, TaxInfo tax, double dMultiply, double dPrice) {

		Properties props = new Properties();
		props.setProperty("product.name", productname);
		props.setProperty("product.taxcategoryid", tax.getTaxCategoryID());
		init(null, null, dMultiply, dPrice, tax, props);
	}

	
	public TicketLineInfo() {
		init(null, null, 0.0, 0.0, null, new Properties());
	}

	public TicketLineInfo(ProductInfoExt product, double dMultiply, double dPrice, TaxInfo tax, Properties attributes,
			boolean issaege, String height, String width, String length, String count) {

		String pid;

		if (product == null) {
			pid = null;
		} else {
			pid = product.getID();
			attributes.setProperty("product.name", product.getName());
			attributes.setProperty("product.com", product.isCom() ? "true" : "false");
			if (product.getAttributeSetID() != null) {
				attributes.setProperty("product.attsetid", product.getAttributeSetID());
			}
			attributes.setProperty("product.taxcategoryid", product.getTaxCategoryID());
			if (product.getCategoryID() != null) {
				attributes.setProperty("product.categoryid", product.getCategoryID());
			}
			if (product.getUnit() != null) {
				attributes.setProperty("product.unit", product.getUnit());
			} else {
				attributes.setProperty("product.unit", "x");
			}
			
			if (product.getSort() > 0) {
				attributes.setProperty("product.sort", String.format("%010d", product.getSort()));
			} else {
				attributes.setProperty("product.sort", "0000000000");
			}
			
			if (issaege) {

				if (height != null) {
					attributes.setProperty("product.height", height);
				} else {
					attributes.setProperty("product.height", "");
				}
				if (width != null) {
					attributes.setProperty("product.width", width);
				} else {
					attributes.setProperty("product.width", "");
				}
				if (length != null) {
					attributes.setProperty("product.length", length);
				} else {
					attributes.setProperty("product.length", "");
				}
				if (count != null) {
					attributes.setProperty("product.count", count);
				} else {
					attributes.setProperty("product.count", "");
				}
			}

		}
		init(pid, null, dMultiply, dPrice, tax, attributes);
	}

	public TicketLineInfo(ProductInfoExt oProduct, double dPrice, TaxInfo tax, Properties attributes, boolean issaege,
			String height, String width, String length, String count) {
		this(oProduct, 1.0, dPrice, tax, attributes, issaege, height, width, length, count);
	}

	public TicketLineInfo(TicketLineInfo line) {
		init(line.productid, line.attsetinstid, 
				line.multiply, line.multiplyClone, line.multiplyCloneValid, 
				line.price, line.tax,
				(Properties) line.attributes.clone());
	}

	private void init(String productid, String attsetinstid, 
			double dMultiply,  
			double dPrice, TaxInfo tax,
			Properties attributes)
	{
		init(productid, attsetinstid,
				dMultiply, 0.0, false,
				dPrice, tax,
				attributes);
	}
	
	private void init(String productid, String attsetinstid, 
			double dMultiply, double dMultiplyClone, boolean bMultiplyCloneValid,  
			double dPrice, TaxInfo tax,
			Properties attributes) {

		this.productid = productid;
		this.attsetinstid = attsetinstid;
		multiply = dMultiply;
		multiplyClone = dMultiplyClone;
		multiplyCloneValid = bMultiplyCloneValid;
		price = RoundUtils.round4DB(dPrice);
		this.tax = tax;
		this.attributes = attributes;
		this.unit = attributes.getProperty("product.unit");
		m_sTicket = null;
		m_iLine = -1;
		try {
			this.width = attributes.getProperty("product.width");
		} catch (NullPointerException e) {
			this.width = "";
		}
		try {
			this.height = attributes.getProperty("product.height");
		} catch (NullPointerException e) {
			this.height = "";
		}
		try {
			this.length = attributes.getProperty("product.length");
		} catch (NullPointerException e) {
			this.length = "";
		}
		try {
			this.count = attributes.getProperty("product.count");
		} catch (NullPointerException e) {
			this.count = "";
		}
	}

	void setTicket(String ticket, int line) {
		m_sTicket = ticket;
		m_iLine = line;
	}

	public void writeValues(DataWrite dp) throws BasicException {
		dp.setString(1, m_sTicket);
		dp.setInt(2, new Integer(m_iLine));
		dp.setString(3, productid);
		dp.setString(4, attsetinstid);

		dp.setDouble(5, new Double(multiply));
		dp.setDouble(6, new Double(price));

		dp.setString(7, tax.getId());
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			attributes.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
			dp.setBytes(8, o.toByteArray());
		} catch (IOException e) {
			dp.setBytes(8, null);
		}
		
		dp.setString(9, attributes.getProperty("product.unit"));
		dp.setString(10, attributes.getProperty("product.height"));
		dp.setString(11, attributes.getProperty("product.width"));
		dp.setString(12, attributes.getProperty("product.length"));
		dp.setString(13, attributes.getProperty("product.count"));
	}

	public void readValues(DataRead dr) throws BasicException {
		m_sTicket = dr.getString(1);
		m_iLine = dr.getInt(2).intValue();
		productid = dr.getString(3);
		attsetinstid = dr.getString(4);

		multiply = dr.getDouble(5);
		price = dr.getDouble(6);

		tax = new TaxInfo(dr.getString(7), dr.getString(8), dr.getString(9), dr.getTimestamp(10), dr.getString(11),
				dr.getString(12), dr.getDouble(13), dr.getBoolean(14), dr.getInt(15));
		attributes = new Properties();
		try {
			byte[] img = dr.getBytes(16);
			if (img != null) {
				attributes.loadFromXML(new ByteArrayInputStream(img));
			}
		} catch (IOException e) {
		}
		this.unit = dr.getString(17);
		if(this.unit == null)
			this.unit = "";
		
		this.height = dr.getString(18);
		if(this.height == null)
			this.height = "";
		
		this.width = dr.getString(19);
		if(this.width == null)
			this.width = "";
		
		this.length = dr.getString(20);
		if(this.length == null)
			this.length = "";
		
		this.count = dr.getString(21);
		if(this.count == null)
			this.count = "";
	}

	public TicketLineInfo copyTicketLine() {
		TicketLineInfo l = new TicketLineInfo();
		// l.m_sTicket = null;
		// l.m_iLine = -1;
		l.productid = productid;
		l.attsetinstid = attsetinstid;
		l.multiply = multiply;
		l.price = price;
		l.tax = tax;
		l.attributes = (Properties) attributes.clone();
		return l;
	}

	public int getTicketLine() {
		return m_iLine;
	}

	public String getProductID() {
		return productid;
	}

	public String getProductName() {
		return attributes.getProperty("product.name");
	}
	
	public void setProductName(String value) {
		attributes.setProperty("product.name", value);
	}

	public Boolean getProductAttTicketShow() {
		return getProductAttSetInstTicketDesc().length() > 0;
	}
	
	public String getProductAttSetInstTicketDesc() {
		String desc = attributes.getProperty("product.attsetdesc", "");
		Log.info(desc);
		desc = desc.replaceAll("[^,]*?\\$[,]*", "").trim();
		if(desc.endsWith(","))
		{
			Log.info(desc);
			desc = desc.substring(0, desc.length() - 1);
		}
		Log.info(desc);
		return StringUtils.encodeXML(desc);
	}
	
	public String getProductAttSetId() {
		return attributes.getProperty("product.attsetid");
	}

	public String getProductAttSetInstDesc() {
		String attsetinstdesc = attributes.getProperty("product.attsetdesc", "");
		if(attsetinstdesc != "")
			return StringUtils.encodeXML(attsetinstdesc);
		else
			return attsetinstdesc;
	}

	public void setProductAttSetInstDesc(String value) {
		if (value == null) {
			attributes.remove("product.attsetdesc");
		} else {
			attributes.setProperty("product.attsetdesc", value);
		}
	}
	
	public String getLineGroup() {
		String lineGroup = attributes.getProperty("line.group", "");
		if(lineGroup.contains("{seconds}")) {
			lineGroup = lineGroup.replace("{seconds}",  String.format("%d", new Date().getTime()));
			setLineGroup(lineGroup);
		}
		return lineGroup;
	}

	public void setLineGroup(String value) {
		if (value == null) {
			attributes.remove("line.group");
		} else {
			attributes.setProperty("line.group", value);
		}
	}

	public String getProductAttSetInstId() {
		return attsetinstid;
	}

	public void setProductAttSetInstId(String value) {
		attsetinstid = value;
	}

	public boolean isProductCom() {
		return "true".equals(attributes.getProperty("product.com"));
	}

	public String getProductTaxCategoryID() {
		return (attributes.getProperty("product.taxcategoryid"));
	}

	public String getProductCategoryID() {
		return (attributes.getProperty("product.categoryid"));
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String dValue) {
		height = dValue;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String dValue) {
		width = dValue;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String dValue) {
		length = dValue;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String dValue) {
		count = dValue;
	}

	public double getMultiply() {
		return multiply;
	}

	public void setMultiply(double dValue) {
		multiply = dValue;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double dValue) {
		price = RoundUtils.round4DB(dValue);
	}

	public double getPriceTax() {
		return price * (1.0 + getTaxRate());
	}

	public void setPriceTax(double dValue) {
		price = RoundUtils.round4DB(dValue / (1.0 + getTaxRate()));
	}

	public TaxInfo getTaxInfo() {
		return tax;
	}

	public void setTaxInfo(TaxInfo value) {
		tax = value;
	}

	public String getProperty(String key) {
		return attributes.getProperty(key);
	}

	public String getProperty(String key, String defaultvalue) {
		return attributes.getProperty(key, defaultvalue);
	}

	public void setProperty(String key, String value) {
		attributes.setProperty(key, value);
	}
	
	public void setPropertyNotExists(String key, String value) {
		if(!attributes.containsKey(key)) {
			attributes.setProperty(key, value);
		}
	}

	public void clearProperty(String key) {
		attributes.remove(key);
	}
	
	public Properties getProperties() {
		return attributes;
	}

	public double getTaxRate() {
		return tax == null ? 0.0 : tax.getRate();
	}

	public double getSubValue() {
		return price * multiply;
	}

	public double getTax() {
		return RoundUtils.round(getValue()-RoundUtils.round(getSubValue()));
	}

	public double getValue() {
		return RoundUtils.round(price * multiply * (1.0 + getTaxRate()));
	}

	public String printName() {
		return StringUtils.encodeXML(attributes.getProperty("product.name"));
	}

	public String printMultiply() {
		// display difference since last clone
		if(multiplyCloneValid)
			return String.format("%1$s (%2$s)", Formats.DOUBLE.formatValue(multiply), Formats.DOUBLE.formatValue(multiply - multiplyClone));
		else
			return Formats.DOUBLE.formatValue(multiply);
	}

	public String printUnit() {
		return unit;
	}

	public String printHeight() {
		try {
			return Formats.DOUBLE.formatValue(Double.parseDouble(height.replace(",", ".")));
		} catch(NumberFormatException ex) {
			return "";
		} catch(NullPointerException ex) {
			return "";
		}
	}

	public String printCount() {
		try {
			return Formats.DOUBLE.formatValue(Double.parseDouble(count.replace(",", ".")));
		} catch(NumberFormatException ex) {
			return "";
		} catch(NullPointerException ex) {
			return "";
		}
	}

	public String printWidth() {
		try {
			return Formats.DOUBLE.formatValue(Double.parseDouble(width.replace(",", ".")));
		} catch(NumberFormatException ex) {
			return "";
		} catch(NullPointerException ex) {
			return "";
		}
	}

	public String printLength() {
		try {
			return Formats.DOUBLE.formatValue(Double.parseDouble(length.replace(",", ".")));
		} catch(NumberFormatException ex) {
			return "";
		} catch(NullPointerException ex) {
			return "";
		}
	}

	public String printPrice() {
		return Formats.CURRENCY.formatValue(getPrice());
	}

	public String printPriceTax() {
		return Formats.CURRENCY.formatValue(getPriceTax());
	}

	public String printTax() {
		return Formats.CURRENCY.formatValue(getTax());
	}

	public String printTaxRate() {
		return Formats.PERCENT.formatValue(getTaxRate());
	}

	public String printSubValue() {
		return Formats.CURRENCY.formatValue(getValue()-getTax());
	}

	public String printValue() {
		return Formats.CURRENCY.formatValue(getValue());
	}
	
	public void CloneMultiply()
	{
		multiplyClone = multiply;
		multiplyCloneValid = true;
	}
	
	public void InvalidateMultiply()
	{
		multiplyCloneValid = false;
	}
}
