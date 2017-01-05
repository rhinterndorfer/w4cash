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

package com.openbravo.pos.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.sales.restaurant.PlaceSplit;

/**
 *
 * @author adrianromero
 */
public class DataLogicCustomers extends BeanFactoryDataSingle {
    
    protected Session s;
    private TableDefinition tcustomers;
    private static Datas[] customerdatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.BOOLEAN, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.INT};
    
    public void init(AppView app){
        
        this.s = app.getSession();
        tcustomers = new TableDefinition(s
            , "CUSTOMERS"
            , new String[] { "ID", "TAXID", "SEARCHKEY", "NAME", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"
                           , "FIRSTNAME", "LASTNAME", "EMAIL", "PHONE", "PHONE2", "FAX"
                           , "ADDRESS", "ADDRESS2", "POSTAL", "CITY", "REGION", "COUNTRY"
                           , "TAXCATEGORY" }
            , new String[] { "ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.searchkey"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")
                           , AppLocal.getIntString("label.firstname"), AppLocal.getIntString("label.lastname"), AppLocal.getIntString("label.email"), AppLocal.getIntString("label.phone"), AppLocal.getIntString("label.phone2"), AppLocal.getIntString("label.fax")
                           , AppLocal.getIntString("label.address"), AppLocal.getIntString("label.address2"), AppLocal.getIntString("label.postal"), AppLocal.getIntString("label.city"), AppLocal.getIntString("label.region"), AppLocal.getIntString("label.country")
                           , "TAXCATEGORY"}
            , new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING}
            , new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING}
            , new int[] {0}
        );   
        
    }
    
    // CustomerList list
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, SEARCHKEY, NAME FROM CUSTOMERS WHERE VISIBLE = " + s.DB.TRUE() + " AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"TAXID", "SEARCHKEY", "NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        CustomerInfo c = new CustomerInfo(dr.getString(1));
                        c.setTaxid(dr.getString(2));
                        c.setSearchkey(dr.getString(3));
                        c.setName(dr.getString(4));
                        return c;
                    }                
                });
    }
       
    public int updateCustomerExt(final CustomerInfoExt customer) throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET NOTES = ? WHERE ID = ?"
                , SerializerWriteParams.INSTANCE      
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, customer.getNotes());
                        setString(2, customer.getId());
                }});        
    }
    
    public final SentenceList getReservationsList() {
        return new PreparedSentence(s
            , "SELECT R.ID, R.CREATED, R.DATENEW, C.CUSTOMER, CUSTOMERS.TAXID, CUSTOMERS.SEARCHKEY, "
            		+ "COALESCE(CUSTOMERS.NAME, R.TITLE),  R.CHAIRS, "
            		+ "R.ISDONE, R.DESCRIPTION, R.DATETILL, "
            		+ "(SELECT LISTAGG(p.ID || ',' || p.Name, ';') WITHIN GROUP (ORDER BY p.Name) " 
            		+ " FROM RESERVATION_PLACES rp inner join PLACES p on rp.PLACE = p.ID "
            		+ " WHERE rp.ID = R.ID) as PLACES, "
            		+ "(SELECT COUNT(*) from "
            		+ " Reservations r1 "
            		+ " INNER JOIN "
            		+ " Reservations r2 "
            		+ " on ((r1.DATENEW >= r2.DATENEW AND r1.DATENEW < r2.DATETILL) OR (r1.DATETILL > r2.DATENEW AND r1.DATETILL <= r2.DATETILL)) "
              		+ "     AND r1.ID != r2.ID "
            		+ " INNER JOIN RESERVATION_PLACES rp1 "
            		+ " on r1.ID = rp1.ID "
            		+ " INNER JOIN RESERVATION_PLACES rp2 "
            		+ " on r2.ID = rp2.ID "
            		+ " where rp1.PLACE = rp2.PLACE and rp1.ID=R.ID) as Conflicts "
              + "FROM RESERVATIONS R "
              + "LEFT OUTER JOIN RESERVATION_CUSTOMERS C ON R.ID = C.ID "
              + "LEFT OUTER JOIN CUSTOMERS ON C.CUSTOMER = CUSTOMERS.ID "
              + "WHERE R.DATENEW >= ? OR R.DATETILL > ? OR R.ISDONE = 0 "
              + "ORDER BY R.DATENEW, R.DATETILL"
            , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP})
            , new SerializerReadBasic(customerdatas));             
    }
    
    public final SentenceExec getReservationsUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                
                new PreparedSentence(s
                        , "DELETE FROM RESERVATION_PLACES WHERE ID = ?"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                if (((Object[]) params)[11] != null) {
                    
                	List<String> placeIds = getPlaceIds(((Object[])params)[11]);
                	for(String placeId : placeIds)
                	{
                		Object[] paramPlace = new Object[] {((Object[])params)[0], placeId};
	                	
	                	new PreparedSentence(s
	                		, "INSERT INTO RESERVATION_PLACES (ID, PLACE) VALUES (?, ?)"
	                        , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING})).exec(paramPlace);
                	}
                }
                
                
                return new PreparedSentence(s
                    , "UPDATE RESERVATIONS SET ID = ?, CREATED = ?, DATENEW = ?, TITLE = ?, CHAIRS = ?, ISDONE = ?, DESCRIPTION = ?, DATETILL = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9, 10, 0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                new PreparedSentence(s
                        , "DELETE FROM RESERVATION_PLACES WHERE ID = ?"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM RESERVATIONS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                int i = new PreparedSentence(s
                    , "INSERT INTO RESERVATIONS (ID, CREATED, DATENEW, TITLE, CHAIRS, ISDONE, DESCRIPTION, DATETILL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9, 10})).exec(params);

                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                
                if (((Object[]) params)[11] != null) {
                    
                	List<String> placeIds = getPlaceIds(((Object[])params)[11]);
                	for(String placeId : placeIds)
                	{
                		Object[] paramPlace = new Object[] {((Object[])params)[0], placeId};
	                	
	                	new PreparedSentence(s
	                		, "INSERT INTO RESERVATION_PLACES (ID, PLACE) VALUES (?, ?)"
	                        , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING})).exec(paramPlace);
                	}
                }
                
                return i;
            }
        };
    }
    
    private List<String> getPlaceIds(Object param)
    {
    	List<String> list = new ArrayList<String>();
    	String placeString = param.toString();
    	for(String place : placeString.split(";"))
    	{
    		if(place != null && !"".equals(place))
    		{
    			list.add(place.split(",")[0]);
    		}
    	}
    	return list;
    }
    
    @SuppressWarnings("unchecked")
	public List<PlaceSplit> getAvailablePlaces(Date start, Date end) throws BasicException
	{
		SentenceList sent = new StaticSentence(s,
				"SELECT p.ID, p.NAME, f.Name as FloorName, 0 as Occupied "
				+ "FROM "
				+ "PLACES p "
				+ "INNER JOIN FLOORS f "
				+ "ON p.FLOOR=f.ID "
				+ "LEFT JOIN SHAREDTICKETS st "
				+ "ON st.Id = p.Id "
				+ "LEFT JOIN RESERVATION_PLACES rp "
				+ "ON p.ID = rp.PLACE "
				+ "LEFT JOIN RESERVATIONS r "
				+ "ON r.ID = rp.ID "
				+ "  AND ((r.DATENEW >= ? AND r.DATENEW < ?) OR (r.DATETILL > ? AND r.DATETILL <= ?)) "
				+ "WHERE r.ID is null "
				+ "ORDER BY f.SORTORDER, f.Name, p.Name",
				new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.TIMESTAMP}),
				new SerializerReadClass(PlaceSplit.class));
		List<PlaceSplit> placesSplit = sent.list(new Object[] {start, end, start, end});
		return placesSplit;
	}
    
    @SuppressWarnings("unchecked")
	public List<PlaceSplit> getReservationPlaces(String ReservationID) throws BasicException
	{
		SentenceList sent = new StaticSentence(s,
				"SELECT p.ID, p.NAME, f.Name as FloorName, 0 as Occupied "
				+ "FROM "
				+ "PLACES p "
				+ "INNER JOIN FLOORS f "
				+ "ON p.FLOOR=f.ID "
				+ "LEFT JOIN SHAREDTICKETS st "
				+ "ON st.Id = p.Id "
				+ "INNER JOIN RESERVATION_PLACES rp "
				+ "ON p.ID = rp.PLACE "
				+ "INNER JOIN RESERVATIONS r "
				+ "ON r.ID = rp.ID "
				+ "WHERE r.ID = ? "
				+ "ORDER BY f.SORTORDER, f.Name, p.Name",
				new SerializerWriteBasic(new Datas[] {Datas.STRING}),
				new SerializerReadClass(PlaceSplit.class));
		List<PlaceSplit> placesSplit = sent.list(new Object[] {ReservationID});
		return placesSplit;
	}
    
    
    public final TableDefinition getTableCustomers() {
        return tcustomers;
    }  
}
