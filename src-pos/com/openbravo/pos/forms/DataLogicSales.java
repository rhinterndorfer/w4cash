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

package com.openbravo.pos.forms;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jfree.chart.labels.IntervalXYItemLabelGenerator;
import org.tempuri.Sig;

import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.basic.SignatureUnitException;
import com.openbravo.data.model.Field;
import com.openbravo.data.model.Row;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.inventory.AttributeSetInfo;
import com.openbravo.pos.inventory.TaxCustCategoryInfo;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.mant.FloorsInfo;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoMagcard;
import com.openbravo.pos.payment.PaymentInfoMagcardRefund;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.sales.TaxesException;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.sales.restaurant.PlaceSplit;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.PriceZoneProductInfo;
import com.openbravo.pos.ticket.TicketTaxInfo;
import com.openbravo.pos.util.PropertyUtil;

import at.w4cash.signature.SignatureModul;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author adrianromero
 */
public class DataLogicSales extends BeanFactoryDataSingle {

	protected Session s;
	protected AppView m_app;

	protected Datas[] auxiliarDatas;
	protected Datas[] stockdiaryDatas;
	// protected Datas[] productcatDatas;
	protected Datas[] paymenttabledatas;
	protected Datas[] stockdatas;
	protected List<PlaceSplit> placesSplit;

	protected Row productsRow;

	/** Creates a new instance of SentenceContainerGeneric */
	public DataLogicSales() {
		stockdiaryDatas = new Datas[] { Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING,
				Datas.STRING, Datas.DOUBLE, Datas.DOUBLE };
		paymenttabledatas = new Datas[] { Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING,
				Datas.DOUBLE, Datas.STRING };
		stockdatas = new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE };
		auxiliarDatas = new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
				Datas.STRING };

		productsRow = new Row(new Field("ID", Datas.STRING, Formats.STRING),
				new Field(AppLocal.getIntString("label.prodref"), Datas.STRING, Formats.STRING, true, true, true),
				new Field(AppLocal.getIntString("label.prodbarcode"), Datas.STRING, Formats.STRING, false, true, true),
				new Field(AppLocal.getIntString("label.prodname"), Datas.STRING, Formats.STRING, true, true, true),
				new Field("ISCOM", Datas.BOOLEAN, Formats.BOOLEAN),
				new Field("ISSCALE", Datas.BOOLEAN, Formats.BOOLEAN),
				new Field(AppLocal.getIntString("label.prodpricebuy"), Datas.DOUBLE, Formats.CURRENCY, false, true, true),
				new Field(AppLocal.getIntString("label.prodpricesell"), Datas.DOUBLE, Formats.CURRENCY, false, true, true),
				new Field(AppLocal.getIntString("label.prodcategory"), Datas.STRING, Formats.STRING, false, false, true),
				new Field(AppLocal.getIntString("label.taxcategory"), Datas.STRING, Formats.STRING, false, false, true),
				new Field(AppLocal.getIntString("label.attributeset"), Datas.STRING, Formats.STRING, false, false, true),
				new Field("IMAGE", Datas.IMAGE, Formats.NULL),
				new Field("BGCOLOR", Datas.STRING, Formats.STRING), 
				new Field("STOCKCOST", Datas.DOUBLE, Formats.CURRENCY),
				new Field("STOCKVOLUME", Datas.DOUBLE, Formats.DOUBLE),
				new Field("ISCATALOG", Datas.BOOLEAN, Formats.BOOLEAN), 
				new Field("CATORDER", Datas.INT, Formats.INT),
				new Field("PROPERTIES", Datas.BYTES, Formats.NULL),
				new Field("UNIT", Datas.STRING, Formats.STRING),
				new Field("ATTR1", Datas.STRING, Formats.STRING),
				new Field("ATTR2", Datas.STRING, Formats.STRING),
				new Field("ATTR3", Datas.STRING, Formats.STRING));
	}

	public void init(AppView app){
		
		this.s = app.getSession();
		this.m_app = app;
		
		// init places for split dialog
		try {
			getPlacesSplit();
		}catch(Exception ex) {
			// do nothing
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PlaceSplit> getPlacesSplit() throws BasicException
	{
		SentenceList sent = new StaticSentence(s,
				"SELECT p.ID, case when st.id is null then p.NAME else st.NAME end as Name, f.Name as FloorName, case when st.ID is null then 0 else 1 end as Occupied "
				+ "FROM "
				+ "PLACES p "
				+ "INNER JOIN FLOORS f "
				+ "ON p.FLOOR=f.ID "
				+ "LEFT JOIN SHAREDTICKETS st "
				+ "ON st.Id = p.Id "
				+ "ORDER BY f.SORTORDER, p.Name ",
				null, new SerializerReadClass(PlaceSplit.class));
		placesSplit = sent.list();
		return placesSplit;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PlaceSplit> getOccupied() throws BasicException
	{
		SentenceList sent = new StaticSentence(s,
				"SELECT p.ID, st.NAME, f.Name as FloorName, 1 as Occupied "
				+ "FROM "
				+ "PLACES p "
				+ "INNER JOIN FLOORS f "
				+ "ON p.FLOOR=f.ID "
				+ "INNER JOIN SHAREDTICKETS st "
				+ "ON st.Id = p.Id "
				+ "ORDER BY f.SORTORDER, p.Name",
				null, new SerializerReadClass(PlaceSplit.class));
		placesSplit = sent.list();
		return placesSplit;
	}

	public final Row getProductsRow() {
		return productsRow;
	}

	// Utilidades de productos
	public final ProductInfoExt getProductInfo(String id) throws BasicException {
		return (ProductInfoExt) new PreparedSentence(s,
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE P.ID = ?",
				SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(id);
	}

	public final ProductInfoExt getProductInfoByCode(String sCode) throws BasicException {
		return (ProductInfoExt) new PreparedSentence(s,
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE P.CODE = ? OR P.ID = (SELECT PRODUCT FROM PRODUCTS_CODES WHERE CODE = ?)",
						new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING }), ProductInfoExt.getSerializerRead()).find(sCode, sCode);
	}

	public final ProductInfoExt getProductInfoByReference(String sReference) throws BasicException {
		return (ProductInfoExt) new PreparedSentence(s,
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE P.REFERENCE = ?",
				SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(sReference);
	}

	// Catalogo de productos
	public final List<CategoryInfo> getCategories(String filter) throws BasicException {
		
		if(filter == null)
			filter = "";
			
		return new PreparedSentence(s,
				"SELECT ID, NAME, IMAGE, BGCOLOR, SORTORDER, PRINTER FROM CATEGORIES WHERE 1=1 " + filter + " ORDER BY SORTORDER",
				null, CategoryInfo.getSerializerRead()).list();
	}

	// Catalogo de productos
	public final List<CategoryInfo> getRootCategories(String filter) throws BasicException {
		if(filter == null)
			filter = "";
		
		return new PreparedSentence(s,
				"SELECT ID, NAME, IMAGE , BGCOLOR, SORTORDER, PRINTER FROM CATEGORIES WHERE PARENTID IS NULL " + filter + " ORDER BY SORTORDER",
				null, CategoryInfo.getSerializerRead()).list();
	}

	public final List<CategoryInfo> getSubcategories(String category, String filter) throws BasicException {
		if(filter == null)
			filter = "";
		
		// only return categories that contain other categories or products
		return new PreparedSentence(s,
				"SELECT ID, NAME, IMAGE, BGCOLOR, SORTORDER, PRINTER "
				+ "FROM CATEGORIES "
				+ "WHERE (EXISTS(SELECT p.ID FROM PRODUCTS p, PRODUCTS_CAT pc WHERE p.ID=pc.PRODUCT and CATEGORY = CATEGORIES.ID) OR EXISTS(SELECT c.ID FROM CATEGORIES c WHERE c.PARENTID = CATEGORIES.ID))"
				+ "AND PARENTID = ? " + filter + " ORDER BY SORTORDER",
				SerializerWriteString.INSTANCE, CategoryInfo.getSerializerRead()).list(category);
	}

	public List<ProductInfoExt> getProductCatalog(String category) throws BasicException {
		return new PreparedSentence(s,
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P, PRODUCTS_CAT O WHERE P.ID = O.PRODUCT AND P.CATEGORY = ? "
						+ "ORDER BY O.CATORDER, P.NAME",
				SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(category);
	}

	public List<ProductInfoExt> getProductComments(String id) throws BasicException {
		return new PreparedSentence(s,
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P, PRODUCTS_CAT O, PRODUCTS_COM M WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2 AND M.PRODUCT = ? "
						+ "AND P.ISCOM = " + s.DB.TRUE() + " " + "ORDER BY O.CATORDER, P.NAME",
				SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(id);
	}

	// Products list
	public final SentenceList getProductList() {
		return new StaticSentence(s,
				new QBFBuilder(
						"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
								+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE ?(QBF_FILTER) ORDER BY P.REFERENCE",
						new String[] { "NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE" }),
				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
						Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }),
				ProductInfoExt.getSerializerRead());
	}

	// Products list
	public SentenceList getProductListNormal() {
		return new StaticSentence(s, new QBFBuilder(
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE P.ISCOM = " + s.DB.FALSE() + " AND ?(QBF_FILTER) ORDER BY P.REFERENCE",
				new String[] { "NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE" }),
				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
						Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }),
				ProductInfoExt.getSerializerRead());
	}

	// Auxiliar list for a filter
	public SentenceList getProductListAuxiliar() {
		return new StaticSentence(s, new QBFBuilder(
				"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.BGCOLOR, P.ATTRIBUTES, P.UNIT, P.ATTR1, P.ATTR2, P.ATTR3, NVL(O.CATORDER, 2147483647) "
						+ "FROM PRODUCTS P LEFT JOIN PRODUCTS_CAT O ON P.ID = O.PRODUCT WHERE P.ISCOM = " + s.DB.TRUE() + " AND ?(QBF_FILTER) ORDER BY P.REFERENCE",
				new String[] { "NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE" }),
				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
						Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }),
				ProductInfoExt.getSerializerRead());
	}

	// Tickets and Receipt list
	public SentenceList getTicketsList() {
		return new StaticSentence(s,
				new QBFBuilder(
						"SELECT T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME, SUM(PM.TOTAL), T.CASHTICKETID, "
						+ "(SELECT MAX(EXTRACTVALUE(XMLTYPE(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(TL.Attributes),'<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">','')),'/properties/entry[@key=\"Place\"]')) as Room FROM TICKETLINES TL WHERE TL.TICKET=T.ID GROUP BY TL.TICKET) as Room, "
						+ "MAX(EXTRACTVALUE(XMLTYPE(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(R.Attributes),'<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">','')),'/properties/entry[@key=\"rksvnotes\"]')) as RKSVNOTES "
								+ "FROM RECEIPTS R "
								+ "JOIN TICKETS T ON R.ID = T.ID "
								+ "LEFT OUTER JOIN PAYMENTS PM ON R.ID = PM.RECEIPT "
								+ "LEFT OUTER JOIN CUSTOMERS C ON C.ID = T.CUSTOMER "
								+ "LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID "
								+ "WHERE ?(QBF_FILTER) "
								+ "GROUP BY T.ID, T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME, T.CASHTICKETID "
								+ "ORDER BY R.DATENEW DESC, T.TICKETID",
						new String[] { "T.TICKETID", "T.CASHTICKETID", "PM.TOTAL", "R.DATENEW", "R.DATENEW", "P.NAME",
								"C.NAME" }),
				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.INT, Datas.OBJECT,
						Datas.DOUBLE, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT,
						Datas.STRING, Datas.OBJECT, Datas.STRING }),
				new SerializerReadClass(FindTicketsInfo.class));
	}

	// User list
	public final SentenceList getUserList() {
		return new StaticSentence(s, "SELECT ID, NAME FROM PEOPLE ORDER BY NAME", null, new SerializerRead() {
			public Object readValues(DataRead dr) throws BasicException {
				return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
			}
		});
	}

	// Listados para combo
	public final SentenceList getTaxList() {
		return new StaticSentence(s,
				"SELECT ID, NAME, CATEGORY, VALIDFROM, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER FROM TAXES ORDER BY NAME",
				null, new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new TaxInfo(dr.getString(1), dr.getString(2), dr.getString(3), dr.getTimestamp(4),
								dr.getString(5), dr.getString(6), dr.getDouble(7).doubleValue(),
								dr.getBoolean(8).booleanValue(), dr.getInt(9));
					}
				});
	}
	
	
	
	public final SentenceList getPriceZonesProductList() {
		return new StaticSentence(s,
				"SELECT pz.ID, NAME, ISACTIV, ISCUSTOMER, DATEFROM, DATETILL, LOCATION, PRODUCT, PRICESELLGROSS"
				+ " FROM PRICEZONES pz INNER JOIN PRICEZONES_PRICES pzp ON pz.ID = pzp.PRICEZONE"
				+ " WHERE pz.ISACTIV = 1 ",
				null, new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new PriceZoneProductInfo(dr.getString(1), dr.getString(2), dr.getInt(3), dr.getInt(4),
								dr.getTimestamp(5), dr.getTimestamp(6), dr.getString(7), dr.getString(8),
								dr.getDouble(9).doubleValue());
					}
				});
	}

	public final SentenceList getCategoriesList() {
		// return new StaticSentence(s, "SELECT ID, NAME, IMAGE, SORTORDER FROM
		// CATEGORIES ORDER BY NAME", null,
		// CategoryInfo.getSerializerRead());
		return new StaticSentence(s, "SELECT ID, NAME, IMAGE, BGCOLOR, SORTORDER, PRINTER FROM CATEGORIES ORDER BY SORTORDER",
				null, CategoryInfo.getSerializerRead());
	}
	
	
	public final SentenceList getCategoriesListSortedByName() {
		// return new StaticSentence(s, "SELECT ID, NAME, IMAGE, SORTORDER FROM
		// CATEGORIES ORDER BY NAME", null,
		// CategoryInfo.getSerializerRead());
		return new StaticSentence(s, "SELECT ID, NAME, IMAGE, BGCOLOR, SORTORDER, PRINTER FROM CATEGORIES ORDER BY NAME",
				null, CategoryInfo.getSerializerRead());
	}

	public final SentenceList getTaxCustCategoriesList() {
		return new StaticSentence(s, "SELECT ID, NAME FROM TAXCUSTCATEGORIES ORDER BY NAME", null,
				new SerializerRead() {
					public Object readValues(DataRead dr) throws BasicException {
						return new TaxCustCategoryInfo(dr.getString(1), dr.getString(2));
					}
				});
	}

	public final SentenceList getTaxCategoriesList() {
		return new StaticSentence(s, "SELECT ID, NAME FROM TAXCATEGORIES ORDER BY NAME", null, new SerializerRead() {
			public Object readValues(DataRead dr) throws BasicException {
				return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
			}
		});
	}

	public final SentenceList getAttributeSetList() {
		return new StaticSentence(s, "SELECT ID, NAME FROM ATTRIBUTESET ORDER BY NAME", null, new SerializerRead() {
			public Object readValues(DataRead dr) throws BasicException {
				return new AttributeSetInfo(dr.getString(1), dr.getString(2));
			}
		});
	}

	public final SentenceList getLocationsList() {
		return new StaticSentence(s, "SELECT ID, NAME, ADDRESS FROM LOCATIONS ORDER BY NAME", null,
				new SerializerReadClass(LocationInfo.class));
	}

	public final SentenceList getFloorsList() {
		return new StaticSentence(s, "SELECT ID, NAME FROM FLOORS ORDER BY SORTORDER,NAME", null,
				new SerializerReadClass(FloorsInfo.class));
	}

	public final SentenceList getPlacesList() {
		return new StaticSentence(s, "SELECT ID, NAME, FLOOR FROM PLACES ORDER BY NAME", null,
				new SerializerReadClass(FloorsInfo.class));
	}

	public CustomerInfoExt findCustomerExt(String card) throws BasicException {
		return (CustomerInfoExt) new PreparedSentence(s,
				"SELECT ID, TAXID, SEARCHKEY, NAME, CARD, TAXCATEGORY, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT"
						+ ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX"
						+ ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY, PRICES_ZONE, DISCOUNT"
						+ " FROM CUSTOMERS WHERE CARD = ? AND VISIBLE = " + s.DB.TRUE(),
				SerializerWriteString.INSTANCE, new CustomerExtRead()).find(card);
	}

	public CustomerInfoExt loadCustomerExt(String id) throws BasicException {
		return (CustomerInfoExt) new PreparedSentence(s,
				"SELECT ID, TAXID, SEARCHKEY, NAME, CARD, TAXCATEGORY, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT"
						+ ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX"
						+ ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY, PRICES_ZONE, DISCOUNT" + " FROM CUSTOMERS WHERE ID = ?",
				SerializerWriteString.INSTANCE, new CustomerExtRead()).find(id);
	}

	public CustomerInfoExt loadCustomerExtBySearchKey(String searchkey) throws BasicException {
		return (CustomerInfoExt) new PreparedSentence(s,
				"SELECT ID, TAXID, SEARCHKEY, NAME, CARD, TAXCATEGORY, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT"
						+ ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX"
						+ ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY, PRICES_ZONE, DISCOUNT" + " FROM CUSTOMERS WHERE SEARCHKEY = ?",
				SerializerWriteString.INSTANCE, new CustomerExtRead()).find(searchkey);
	}
	
	public final boolean isCashActive(String id) throws BasicException {

		return new PreparedSentence(s, "SELECT MONEY FROM CLOSEDCASH WHERE DATEEND IS NULL AND MONEY = ?",
				SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find(id) != null;
	}

	public final TicketInfo loadTicket(final Boolean isCashTicketId, final int ticketid, TaxesLogic taxlogic) throws BasicException {
		TicketInfo ticket = (TicketInfo) new PreparedSentence(s,
				"SELECT T.ID, T.TICKETTYPE, T.TICKETID, "
				+ "R.DATENEW, R.MONEY, R.ATTRIBUTES, "
				+ "P.ID, P.NAME, "
				+ "T.CUSTOMER, "
				+ "T.CASHTICKETID, T.SIGNATUREID, T.SIGNATUREVALUE, T.CASHSUMCOUNTER, "
				+ "T.ALGORITHMID, T.POSID, T.SIGNATUREOUTOFORDER, "
				+ "T.CHAINVALUE, T.CASHSUMCOUNTERENC, T.VALIDATION, T.MONTH "
				+ "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE (? IS NOT NULL AND T.TICKETID = ?) OR (? IS NOT NULL AND T.CASHTICKETID = ?)",
				SerializerWriteParams.INSTANCE, new SerializerReadClass(TicketInfo.class)).find(new DataParams() {
					public void writeValues() throws BasicException {
						setInt(1, isCashTicketId ? null : ticketid);
						setInt(2, isCashTicketId ? null : ticketid);
						setInt(3, isCashTicketId ? ticketid : null);
						setInt(4, isCashTicketId ? ticketid : null);
					}
				});
		if (ticket != null) {

			String customerid = ticket.getCustomerId();
			ticket.setCustomer(customerid == null ? null : loadCustomerExt(customerid));

			ticket.setLines(new PreparedSentence(s,
					"SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.VALIDFROM, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, L.ATTRIBUTES, L.UNIT, L.ATTR1, L.ATTR2, L.ATTR3, L.ATTR4 "
							+ "FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ? ORDER BY L.LINE",
					SerializerWriteString.INSTANCE, new SerializerReadClass(TicketLineInfo.class))
							.list(ticket.getId()));
			ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?",
					SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class))
							.list(ticket.getId()));
			
			
			// check if cash ticket
			// search for cash payment
			Boolean isCashTicket = ticket.getCashTicketId() != null;
			try {
				taxlogic.calculateTaxes(ticket);
				ticket.setQRCode("");
				if(isCashTicket)
				{
					ticket.setQRCode(ticket.getSigningClearText() + "_" + ticket.getSignatureValue());
				}
			} catch(Exception ex)
			{
				throw new BasicException("Error generating QR code", ex);
			}
		}
			
		return ticket;
	}

	public final Object saveTicket(final TicketInfo ticket, final String location, TaxesLogic taxlogic) throws BasicException, SignatureUnitException {

		SignatureModul sig = SignatureModul.getInstance();
		if(sig.GetIsActive() && sig.GetIsCriticalError())
		{
			throw new SignatureUnitException("Signature device in critical error sate!");
		}
		
		
		
		Transaction t = new Transaction(s) {
			public Object transact() throws BasicException, SignatureUnitException {

				// Set Receipt Id
				switch (ticket.getTicketType()) {
					case TicketInfo.RECEIPT_NORMAL:
						ticket.setTicketId(getNextTicketIndex().intValue());
						break;
					case TicketInfo.RECEIPT_REFUND:
						ticket.setTicketId(getNextTicketIndex().intValue());
						break;
					case TicketInfo.RECEIPT_PAYMENT:
						ticket.setTicketId(getNextTicketPaymentIndex().intValue());
						break;
					default:
						throw new BasicException();
				}
				
				// check if cash ticket
				// search for cash payment
				ticket.setQRCode("");
				Boolean isCashTicket = false;
				for (final PaymentInfo p : ticket.getPayments()) {
					if("cash".equals(p.getName()) 
							|| "cashrefund".equals(p.getName())
							|| "magcard".equals(p.getName())
							|| "magcardrefund".equals(p.getName())
					)
					{
						isCashTicket = true;
						break;
					}
				}
				
				if(isCashTicket 
					&& sig.GetIsActive()
					)
				{
					int cashTicketId = getNextCashTicketIndex();
					double sum = getLastCashTicketSum();
					sum = TicketInfo.round(sum, 2);
					sum = sum + ticket.getTotal2();
					sum = TicketInfo.round(sum, 2);
					
					String signatureId = sig.GetSignatureId();
					ticket.setCashTicketId(cashTicketId);
					ticket.setCashSumCounter(sum);
					ticket.setSignatureId(signatureId);
					ticket.setPosId(sig.GetPOSID());
					ticket.setAlgorithmId(1); // maybe in future use different algorithm 
					
					// trunover counter 
					String trunOverCounterCrypted = sig.getTurnOverCounterEncrypted(ticket.getAlgorithmId(), ticket.getCashSumCounter(), ticket.getPosId(), ticket.getCashTicketId());
					ticket.setCashSumCounterEnc(trunOverCounterCrypted);
					
					// chain value
					String lastCashTicketSignatureValue = null;
					String lastCashTicketPayload = null;
					if(cashTicketId > 1)
					{
						TicketInfo lastTicket = loadTicket(true, cashTicketId - 1, taxlogic);
						lastCashTicketPayload = lastTicket.getSigningClearText();
						lastCashTicketSignatureValue = lastTicket.getSignatureValue();
					}
					
					String chainValue = sig.calculateSignatureValuePreviousReceipt(ticket.getAlgorithmId(), ticket.getPosId(), lastCashTicketPayload, lastCashTicketSignatureValue);
					ticket.setChainValue(chainValue);
					
					
					String ticketSigningClearText = ticket.getSigningClearText();
					String signValue = sig.Sign(ticketSigningClearText);
					ticket.setSignatureValue(signValue);
					ticket.setSignatureOutOfOrder(sig.GetIsOutOfOrder());
					
					ticket.setQRCode(ticketSigningClearText + "_" + signValue);
				}

				// check if receipt id already exists
				Object existingTicketId = new StaticSentence(s, "SELECT TICKETID FROM TICKETS WHERE ID = ?",
						SerializerWriteString.INSTANCE,
						SerializerReadInteger.INSTANCE).find(ticket.getId());
				if(existingTicketId != null)
				{
					// load ticket from DB and return
					TicketInfo fromDB = loadTicket(false, (Integer)existingTicketId, taxlogic);
					return fromDB;
				}

				
				// new receipt
				new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, ATTRIBUTES) VALUES (?, ?, ?, ?)",
						SerializerWriteParams.INSTANCE).exec(new DataParams() {
					public void writeValues() throws BasicException {
						setString(1, ticket.getId());
						setString(2, ticket.getActiveCash());
						setTimestamp(3, ticket.getDate());
						try {
							ByteArrayOutputStream o = new ByteArrayOutputStream();
							ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
							setBytes(4, o.toByteArray());
						} catch (IOException e) {
							setBytes(4, null);
						}
					}
				});

				// new ticket
				new PreparedSentence(s,
						"INSERT INTO TICKETS (ID, TICKETTYPE, TICKETID, PERSON, CUSTOMER, "
						+ "CASHTICKETID, CASHSUMCOUNTER, SIGNATUREID, SIGNATUREVALUE, ALGORITHMID, POSID, SIGNATUREOUTOFORDER, CHAINVALUE, CASHSUMCOUNTERENC, VALIDATION, MONTH) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						SerializerWriteParams.INSTANCE).exec(new DataParams() {
					public void writeValues() throws BasicException {
						setString(1, ticket.getId());
						setInt(2, ticket.getTicketType());
						setInt(3, ticket.getTicketId());
						setString(4, ticket.getUser().getId());
						setString(5, ticket.getCustomerId());
						setInt(6, ticket.getCashTicketId());
						setDouble(7, ticket.getCashSumCounter());
						setString(8, ticket.getSignatureId());
						setBytes(9, ticket.getSignatureValueBlob());
						setInt(10, ticket.getAlgorithmId());
						setString(11, ticket.getPosId());
						setInt(12, ticket.getSignatureOutOfOrder() ? 1 : 0);
						setBytes(13, ticket.getChainValueBlob());
						setBytes(14, ticket.getCashSumCounterEncBlob());
						setInt(15, ticket.getValidation());
						setInt(16, ticket.getMonth());
					}
				});

				SentenceExec ticketlineinsert = new PreparedSentence(s,
						"INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PRICE, TAXID, ATTRIBUTES, UNIT, ATTR1, ATTR2, ATTR3, ATTR4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						SerializerWriteBuilder.INSTANCE);

				for (TicketLineInfo l : ticket.getLines()) {
					ticketlineinsert.exec(l);
					
					if (l.getProductID() != null) {
						// update the stock
						getStockDiaryInsert().exec(new Object[] { UUID.randomUUID().toString(), ticket.getDate(),
								l.getMultiply() < 0.0 ? MovementReason.IN_REFUND.getKey()
										: MovementReason.OUT_SALE.getKey(),
								location, l.getProductID(), l.getProductAttSetInstId(), new Double(-l.getMultiply()),
								new Double(l.getPrice()) });
					}
				}

				SentenceExec paymentinsert = new PreparedSentence(s,
						"INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG) VALUES (?, ?, ?, ?, ?, ?)",
						SerializerWriteParams.INSTANCE);
				for (final PaymentInfo p : ticket.getPayments()) {
					paymentinsert.exec(new DataParams() {
						public void writeValues() throws BasicException {
							setString(1, UUID.randomUUID().toString());
							setString(2, ticket.getId());
							setString(3, p.getName());
							setDouble(4, p.getTotal());
							setString(5, ticket.getTransactionID());
							setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
						}
					});

					if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

						// udate customer fields...
						ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());

						// save customer fields...
						getDebtUpdate().exec(new DataParams() {
							public void writeValues() throws BasicException {
								setDouble(1, ticket.getCustomer().getCurdebt());
								setTimestamp(2, ticket.getCustomer().getCurdate());
								setString(3, ticket.getCustomer().getId());
							}
						});
					}
				}

				SentenceExec taxlinesinsert = new PreparedSentence(s,
						"INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)",
						SerializerWriteParams.INSTANCE);
				if (ticket.getTaxes() != null) {
					for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
						taxlinesinsert.exec(new DataParams() {
							public void writeValues() throws BasicException {
								setString(1, UUID.randomUUID().toString());
								setString(2, ticket.getId());
								setString(3, tickettax.getTaxInfo().getId());
								setDouble(4, tickettax.getSubTotal());
								setDouble(5, tickettax.getTax());
							}
						});
					}
				}

				return null;
			}
		};
		return t.execute();
	}

	public final void deleteTicket(final TicketInfo ticket, final String location) throws BasicException, SignatureUnitException {

		Transaction t = new Transaction(s) {
			public Object transact() throws BasicException {

				// update the inventory
				Date d = new Date();
				for (int i = 0; i < ticket.getLinesCount(); i++) {
					if (ticket.getLine(i).getProductID() != null) {
						// Hay que actualizar el stock si el hay producto
						getStockDiaryInsert().exec(new Object[] { UUID.randomUUID().toString(), d,
								ticket.getLine(i).getMultiply() >= 0.0 ? MovementReason.IN_REFUND.getKey()
										: MovementReason.OUT_SALE.getKey(),
								location, ticket.getLine(i).getProductID(), ticket.getLine(i).getProductAttSetInstId(),
								new Double(ticket.getLine(i).getMultiply()),
								new Double(ticket.getLine(i).getPrice()) });
					}
				}

				// update customer debts
				for (PaymentInfo p : ticket.getPayments()) {
					if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

						// udate customer fields...
						ticket.getCustomer().updateCurDebt(-p.getTotal(), ticket.getDate());

						// save customer fields...
						getDebtUpdate().exec(new DataParams() {
							public void writeValues() throws BasicException {
								setDouble(1, ticket.getCustomer().getCurdebt());
								setTimestamp(2, ticket.getCustomer().getCurdate());
								setString(3, ticket.getCustomer().getId());
							}
						});
					}
				}

				// and delete the receipt
				new StaticSentence(s, "DELETE FROM TAXLINES WHERE RECEIPT = ?", SerializerWriteString.INSTANCE)
						.exec(ticket.getId());
				new StaticSentence(s, "DELETE FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE)
						.exec(ticket.getId());
				new StaticSentence(s, "DELETE FROM TICKETLINES WHERE TICKET = ?", SerializerWriteString.INSTANCE)
						.exec(ticket.getId());
				new StaticSentence(s, "DELETE FROM TICKETS WHERE ID = ?", SerializerWriteString.INSTANCE)
						.exec(ticket.getId());
				new StaticSentence(s, "DELETE FROM RECEIPTS WHERE ID = ?", SerializerWriteString.INSTANCE)
						.exec(ticket.getId());
				return null;
			}
		};
		t.execute();
	}

	public final Integer getNextTicketIndex() throws BasicException {
		// sell
		Object result = s.DB.getSequenceSentence(s, "TICKETSNUM").find();
		if (result == null)
			return 1; // first ticket
		return (Integer) result;
	}
	
	public final Integer getNextCashTicketIndex() throws BasicException {
		// sell
		Object result = s.DB.getCashSequenceSentence(s, "TICKETSNUM").find();
		if (result == null)
			return 1; // first ticket
		return (Integer) result;
	}
	
	public final Double getLastCashTicketSum() throws BasicException {
		// sell
		Object result = s.DB.getCashSumSentence(s).find();
		if (result == null)
			return 0.0; // first ticket
		return (Double) result;
	}

	public final Integer getNextTicketRefundIndex() throws BasicException {
		// refund
		// use same ticket id as for standard sell
		Object result = s.DB.getSequenceSentence(s, "TICKETSNUM").find();
		if (result == null)
			return 1;
		return (Integer) result;
	}

	public final Integer getNextTicketPaymentIndex() throws BasicException {
		// customer payment
		Object result = s.DB.getSequenceSentence(s, "TICKETSNUM_PAYMENT").find();
		if (result == null)
			return 1;
		return (Integer) result;
	}

	// deprecated because of order by
//	public final SentenceList getProductCatQBF() {
//		return new StaticSentence(s,
//				new QBFBuilder(
//						"SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAXCAT, P.ATTRIBUTESET_ID, P.IMAGE, P.STOCKCOST, P.STOCKVOLUME,  CASE WHEN C.PRODUCT IS NULL THEN "
//								+ s.DB.FALSE() + " ELSE " + s.DB.TRUE() + " END, C.CATORDER, P.ATTRIBUTES, P.UNIT "
//								+ "FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT "
//								+ "WHERE ?(QBF_FILTER) " + "ORDER BY P.REFERENCE",
//						new String[] { "P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE" }),
//				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
//						Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }),
//				productsRow.getSerializerRead());
//	}
	
	public final SentenceList getProductCatQBF() {
		return new StaticSentence(s,
				new QBFBuilder(
						"SELECT PID, PREFERENCE,PCODE, PNAME, PCOM, PSCALE, PPRICEBUY, PPRICESELL, PCATEGORY, PTAXCAT, PATTRIBUTESET_ID, PIMAGE, PBGCOLOR, PSTOCKCOST,PSTOCKVOLUME,PNULL, PSORTORDER, PATTRIBUTES, PUNIT, PATTR1, PATTR2, PATTR3 FROM "+
								"(SELECT P.ID AS PID, P.REFERENCE AS PREFERENCE, P.CODE AS PCODE, P.NAME AS PNAME, P.ISCOM AS PCOM, P.ISSCALE AS PSCALE, P.PRICEBUY AS PPRICEBUY, P.PRICESELL AS PPRICESELL, P.CATEGORY AS PCATEGORY, P.TAXCAT AS PTAXCAT, P.ATTRIBUTESET_ID AS PATTRIBUTESET_ID, P.IMAGE AS PIMAGE, P.BGCOLOR AS PBGCOLOR, P.STOCKCOST AS PSTOCKCOST, P.STOCKVOLUME AS PSTOCKVOLUME,"+ 
								"( CASE WHEN C.PRODUCT IS NULL THEN " + s.DB.FALSE() + " ELSE " + s.DB.TRUE() + " END) AS PNULL,"+ 
								"C.CATORDER AS PSORTORDER,P.ATTRIBUTES AS PATTRIBUTES, P.UNIT AS PUNIT, P.ATTR1 AS PATTR1, P.ATTR2 AS PATTR2, P.ATTR3 AS PATTR3, PC.PCODE2 "+
								"FROM PRODUCTS P "+
								"LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT "+
								"LEFT OUTER JOIN (SELECT PRODUCT, ',' || LISTAGG(CODE, ',') WITHIN GROUP (ORDER BY PRODUCT) || ',' AS PCODE2 FROM ( SELECT ID AS PRODUCT, CODE FROM PRODUCTS UNION ALL SELECT PRODUCT, CODE FROM PRODUCTS_CODES ) GROUP BY PRODUCT) PC ON P.ID = PC.PRODUCT "+
								") "+ 
								"LEFT JOIN "+ 
								"(SELECT ID AS CATID, NAME AS CATNAME, SORTORDER AS CATSORTORDER FROM CATEGORIES) "+
								"ON PCATEGORY = CATID "+
								"WHERE ?(QBF_FILTER) " +
								"ORDER BY CATSORTORDER, PSORTORDER, PNAME",
						new String[] { "PNAME", "PPRICEBUY", "PPRICESELL", "PCATEGORY", "PCODE2" }),
				new SerializerWriteBasic(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE,
						Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }),
				productsRow.getSerializerRead());
	}

	public final SentenceExec getProductCatInsert() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				Object[] values = (Object[]) params;
				int i = new PreparedSentence(s,
						"INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, BGCOLOR, STOCKCOST, STOCKVOLUME, ATTRIBUTES, UNIT, ATTR1, ATTR2, ATTR3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new SerializerWriteBasicExt(productsRow.getDatas(),
								new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21 })).exec(params);
				
				// refresh codes
				new PreparedSentence(s, "DELETE FROM PRODUCTS_CODES WHERE PRODUCT = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 }))
								.exec(params);
				
				for(String code : values[2].toString().split(","))
				{
					Object[] valuesCode = new Object[] {values[0], code};
			        Datas[] datasCode = new Datas[] {Datas.STRING, Datas.STRING};
			        
					new PreparedSentence(s, "INSERT INTO PRODUCTS_CODES (PRODUCT, CODE) VALUES (?,?)",
							new SerializerWriteBasicExt(datasCode, new int[] { 0, 1 }))
									.exec(valuesCode);
				}
				
				
				if (i > 0 && ((Boolean) values[15]).booleanValue()) {
					return new PreparedSentence(s, "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)",
							new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0, 16 })).exec(params);
				} else {
					return i;
				}
			}
		};
	}

	public final SentenceExec getProductCatUpdate() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				Object[] values = (Object[]) params;
				int i = new PreparedSentence(s,
						"UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, "
						+ "		PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAXCAT = ?, ATTRIBUTESET_ID = ?, IMAGE = ?, BGCOLOR = ?, "
						+ "		STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ?, UNIT=?, ATTR1=?, ATTR2=?, ATTR3=? WHERE ID = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(),
								new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 0 })).exec(params);
				if (i > 0) {
					if (((Boolean) values[15]).booleanValue()) {
						if (new PreparedSentence(s, "UPDATE PRODUCTS_CAT SET CATORDER = ? WHERE PRODUCT = ?",
								new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 16, 0 }))
										.exec(params) == 0) {
							new PreparedSentence(s, "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)",
									new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0, 16 }))
											.exec(params);
						}
					} else {
						new PreparedSentence(s, "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?",
								new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 })).exec(params);
					}
				}
				
				
				// refresh codes
				new PreparedSentence(s, "DELETE FROM PRODUCTS_CODES WHERE PRODUCT = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 }))
								.exec(params);
				
				for(String code : values[2].toString().split(","))
				{
					Object[] valuesCode = new Object[] {values[0], code};
			        Datas[] datasCode = new Datas[] {Datas.STRING, Datas.STRING};
			        
					new PreparedSentence(s, "INSERT INTO PRODUCTS_CODES (PRODUCT, CODE) VALUES (?,?)",
							new SerializerWriteBasicExt(datasCode, new int[] { 0, 1 }))
									.exec(valuesCode);
				}
				
				return i;
			}
		};
	}

	public final SentenceExec getProductCatDelete() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				new PreparedSentence(s, "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 })).exec(params);
				new PreparedSentence(s, "DELETE FROM PRODUCTS_CODES WHERE PRODUCT = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 }))
								.exec(params);
				return new PreparedSentence(s, "DELETE FROM PRODUCTS WHERE ID = ?",
						new SerializerWriteBasicExt(productsRow.getDatas(), new int[] { 0 })).exec(params);
				
			}
		};
	}

	public final SentenceExec getDebtUpdate() {

		return new PreparedSentence(s, "UPDATE CUSTOMERS SET CURDEBT = ?, CURDATE = ? WHERE ID = ?",
				SerializerWriteParams.INSTANCE);
	}

	public final SentenceExec getStockDiaryInsert() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				int updateresult = new PreparedSentence(s,
								"UPDATE STOCKCURRENT SET UNITS = (UNITS + ?) WHERE LOCATION = ? AND PRODUCT = ?",
								new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 6, 3, 4 })).exec(params);
				if (updateresult == 0) {
					new PreparedSentence(s,
							"INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, UNITS) VALUES (?, ?, ?)",
							new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 3, 4, 6 })).exec(params);
				}
				return new PreparedSentence(s,
						"INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PRICE) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
						new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 0, 1, 2, 3, 4, 5, 6, 7}))
								.exec(params);
			}
		};
	}

	public final SentenceExec getStockDiaryDelete() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				int updateresult = 
						new PreparedSentence(s,
								"UPDATE STOCKCURRENT SET UNITS = (UNITS - ?) WHERE LOCATION = ? AND PRODUCT = ?",
								new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 6, 3, 4 })).exec(params);

				if (updateresult == 0) {
					new PreparedSentence(s,
							"INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, UNITS) VALUES (?, ?, -(?))",
							new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 3, 4, 6 })).exec(params);
				}
				return new PreparedSentence(s, "DELETE FROM STOCKDIARY WHERE ID = ?",
						new SerializerWriteBasicExt(stockdiaryDatas, new int[] { 0 })).exec(params);
			}
		};
	}

	public final SentenceExec getPaymentMovementInsert() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW) VALUES (?, ?, ?)",
						new SerializerWriteBasicExt(paymenttabledatas, new int[] { 0, 1, 2 })).exec(params);
				return new PreparedSentence(s,
						"INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL,DESCRIPTION) VALUES (?, ?, ?, ?,?)",
						new SerializerWriteBasicExt(paymenttabledatas, new int[] { 3, 0, 4, 5, 6 })).exec(params);
			}
		};
	}

	public SentenceList getPaymentList(AppView app) throws BasicException {

		/*
		 * SELECT R.ID, R.MONEY, R.DATENEW, P.ID, P.PAYMENT,
		 * P.TOTAL,P.DESCRIPTION
		 * 
		 * FROM RECEIPTS R, PAYMENTS P WHERE R.ID = P.RECEIPT AND R.MONEY =
		 * s.<ACTIVEMONEYTICKET> AND R.MONEY NOT IN (SELECT C.MONEY from
		 * CLOSEDCASH DISTINCT)
		 * 
		 */

		return new StaticSentence(s,
				new QBFBuilder(
						"SELECT R.ID, R.MONEY, R.DATENEW, P.ID, P.PAYMENT, P.TOTAL, P.DESCRIPTION "
								+ "FROM RECEIPTS R, PAYMENTS P, CLOSEDCASH C WHERE R.MONEY = '"
								+ (app.getActiveCashIndex(false, true) == null ? "" : app.getActiveCashIndex(false, false)) + "' " + "AND R.ID = P.RECEIPT " + "AND C.MONEY = '"
								+ (app.getActiveCashIndex(false, true) == null ? "" : app.getActiveCashIndex(false, false)) + "' AND P.TRANSID IS NULL"
							    + " AND P.PAYMENT in ('cashin','cashout')",
						new String[] { "ID", "MONEY", "DATENEW", "ID2", "PAYMENT", "TOTAL", "DESCRIPTION" }),
				new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING,
						Datas.STRING, Datas.DOUBLE, Datas.STRING }),
				new SerializerRead() {

					@Override
					public Object readValues(DataRead dr) throws BasicException {
						Object[] payment = new Object[7];
						payment[0] = dr.getString(1);
						payment[1] = dr.getString(2);
						payment[2] = dr.getTimestamp(3);
						payment[3] = dr.getString(4);
						payment[4] = dr.getString(5);
						payment[5] = dr.getDouble(6);
						payment[6] = dr.getString(7);
						return payment;
					}

				});
	}

	public final SentenceExec getPaymentMovementDelete() {
		return new SentenceExecTransaction(s) {
			public int execInTransaction(Object params) throws BasicException {
				new PreparedSentence(s, "DELETE FROM PAYMENTS WHERE ID = ?",
						new SerializerWriteBasicExt(paymenttabledatas, new int[] { 3 })).exec(params);
				return new PreparedSentence(s, "DELETE FROM RECEIPTS WHERE ID = ?",
						new SerializerWriteBasicExt(paymenttabledatas, new int[] { 0 })).exec(params);
			}
		};
	}

	public final double findProductStock(String warehouse, String id, String attsetinstid) throws BasicException {

		PreparedSentence p = new PreparedSentence(s,
						"SELECT UNITS FROM STOCKCURRENT WHERE LOCATION = ? AND PRODUCT = ?",
						new SerializerWriteBasic(Datas.STRING, Datas.STRING), SerializerReadDouble.INSTANCE);

		Double d = (Double) p.find(warehouse, id, attsetinstid);
		return d == null ? 0.0 : d.doubleValue();
	}

	public final SentenceExec getCatalogCategoryAdd() {
		return new StaticSentence(s, "INSERT INTO PRODUCTS_CAT(PRODUCT, CATORDER) SELECT ID, " + s.DB.INTEGER_NULL()
				+ " FROM PRODUCTS WHERE CATEGORY = ?", SerializerWriteString.INSTANCE);
	}

	public final SentenceExec getCatalogCategoryDel() {
		return new StaticSentence(s,
				"DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ANY (SELECT ID FROM PRODUCTS WHERE CATEGORY = ?)",
				SerializerWriteString.INSTANCE);
	}

	public final TableDefinition getTableCategories() {
		return new TableDefinition(s, "CATEGORIES",
				new String[] { "ID", "NAME", "PARENTID", "IMAGE", "BGCOLOR", "SORTORDER", "PRINTER" },
				new String[] { "ID", AppLocal.getIntString("Label.Name"), "", AppLocal.getIntString("label.image"), "BGCOLOR",
						"SORTORDER", "PRINTER" },
				new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.STRING, Datas.INT, Datas.INT },
				new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.NULL, Formats.STRING, Formats.INT,
						Formats.INT },
				new int[] { 0 });
	}

	public final TableDefinition getTableTaxes() {
		return new TableDefinition(s, "TAXES",
				new String[] { "ID", "NAME", "CATEGORY", "VALIDFROM", "CUSTCATEGORY", "PARENTID", "RATE", "RATECASCADE",
						"RATEORDER" },
				new String[] { "ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.taxcategory"),
						AppLocal.getIntString("Label.ValidFrom"), AppLocal.getIntString("label.custtaxcategory"),
						AppLocal.getIntString("label.taxparent"), AppLocal.getIntString("label.dutyrate"),
						AppLocal.getIntString("label.cascade"), AppLocal.getIntString("label.order") },
				new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING,
						Datas.DOUBLE, Datas.BOOLEAN, Datas.INT },
				new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.TIMESTAMP, Formats.STRING,
						Formats.STRING, Formats.PERCENT, Formats.BOOLEAN, Formats.INT },
				new int[] { 0 });
	}

	public final TableDefinition getTableTaxCustCategories() {
		return new TableDefinition(s, "TAXCUSTCATEGORIES", new String[] { "ID", "NAME" },
				new String[] { "ID", AppLocal.getIntString("Label.Name") }, new Datas[] { Datas.STRING, Datas.STRING },
				new Formats[] { Formats.STRING, Formats.STRING }, new int[] { 0 });
	}

	public final TableDefinition getTableTaxCategories() {
		return new TableDefinition(s, "TAXCATEGORIES", new String[] { "ID", "NAME" },
				new String[] { "ID", AppLocal.getIntString("Label.Name") }, new Datas[] { Datas.STRING, Datas.STRING },
				new Formats[] { Formats.STRING, Formats.STRING }, new int[] { 0 });
	}

	public final TableDefinition getTableLocations() {
		return new TableDefinition(s, "LOCATIONS", new String[] { "ID", "NAME", "ADDRESS" },
				new String[] { "ID", AppLocal.getIntString("label.locationname"),
						AppLocal.getIntString("label.locationaddress") },
				new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING },
				new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING }, new int[] { 0 });
	}

	protected static class CustomerExtRead implements SerializerRead {
		public Object readValues(DataRead dr) throws BasicException {
			CustomerInfoExt c = new CustomerInfoExt(dr.getString(1));
			c.setTaxid(dr.getString(2));
			c.setSearchkey(dr.getString(3));
			c.setName(dr.getString(4));
			c.setCard(dr.getString(5));
			c.setTaxCustomerID(dr.getString(6));
			c.setNotes(dr.getString(7));
			c.setMaxdebt(dr.getDouble(8));
			c.setVisible(dr.getBoolean(9).booleanValue());
			c.setCurdate(dr.getTimestamp(10));
			c.setCurdebt(dr.getDouble(11));
			c.setFirstname(dr.getString(12));
			c.setLastname(dr.getString(13));
			c.setEmail(dr.getString(14));
			c.setPhone(dr.getString(15));
			c.setPhone2(dr.getString(16));
			c.setFax(dr.getString(17));
			c.setAddress(dr.getString(18));
			c.setAddress2(dr.getString(19));
			c.setPostal(dr.getString(20));
			c.setCity(dr.getString(21));
			c.setRegion(dr.getString(22));
			c.setCountry(dr.getString(23));
			c.setPrices_Zone(dr.getString(24));
			c.setDiscount(dr.getDouble(25));
			return c;
		}
	}

}
