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

package com.openbravo.pos.sales;

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.ticket.TicketInfo;

/**
 *
 * @author adrianromero
 */
public class DataLogicReceipts extends BeanFactoryDataSingle {
    
    private Session s;
    private AppConfig appConfig;
    
    /** Creates a new instance of DataLogicReceipts */
    public DataLogicReceipts() {
    }
    
    public void init(Session s){
        this.s = s;
        appConfig = new AppConfig(new String[] {}); 
        appConfig.load();
    }
     
    public final void checkinSharedTicket(String Id) throws BasicException {
        
        if (Id == null) {
            return; 
        } else {
        	String hostname = appConfig.getHost();
        	
            Object[] values = new Object[] {Id, hostname};
            Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING};
            int affected = new PreparedSentence(s
                    , "UPDATE SHAREDTICKETS SET LOCKBY = null WHERE ID = ? AND LOCKBY = ?"
                    , new SerializerWriteBasicExt(datas, new int[] {0, 1})).exec(values);
        }
    }
    
    
    public final String checkoutSharedTicket(String Id) throws BasicException {
        
        if (Id == null) {
            return ""; 
        } else {
        	
        	String hostname = appConfig.getHost();
        	
        	
        	Object[] values = new Object[] {Id, hostname};
            Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING};
        	
            try {
            	PreparedSentence insert = new PreparedSentence(s
                    , "INSERT INTO SHAREDTICKETS (ID, NAME, LOCKBY) VALUES (?, ?, ?)"
                    , new SerializerWriteBasicExt(datas, new int[] {0, 1, 1}));
            	insert.noExceptionLogging = true;
            	insert.exec(values);
            } catch(Exception ex)
            {
            	// ignore
            	String m = ex.getMessage();
            }
            
            int affected = new PreparedSentence(s
                    , "UPDATE SHAREDTICKETS SET LOCKBY = ? WHERE ID = ? AND (LOCKBY = ? OR LOCKBY IS NULL)"
                    , new SerializerWriteBasicExt(datas, new int[] {1, 0, 1})).exec(values);

            if(affected > 0)
            	return null;
            else
            {
            	Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT LOCKBY FROM SHAREDTICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(Id);
            	return record == null ? "" : record[0].toString();
            }
            
            
        }
    }
    
    
    public final TicketInfo getSharedTicket(String Id) throws BasicException {
        
        if (Id == null) {
            return null; 
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT CONTENT FROM SHAREDTICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (TicketInfo) record[0];
        }
    } 
    
    @SuppressWarnings("unchecked")
	public final List<SharedTicketInfo> getSharedTicketList() throws BasicException {
        
        return (List<SharedTicketInfo>) new StaticSentence(s
                , "SELECT ID, NAME FROM SHAREDTICKETS ORDER BY ID"
                , null
                , new SerializerReadClass(SharedTicketInfo.class)).list();
    }
    
    @SuppressWarnings("unchecked")
	public final List<SharedTicketInfo> getSharedTicketListNoPlace() throws BasicException {
        
        return (List<SharedTicketInfo>) new StaticSentence(s
                , "SELECT SHAREDTICKETS.ID, SHAREDTICKETS.NAME FROM SHAREDTICKETS LEFT JOIN PLACES ON SHAREDTICKETS.ID = PLACES.ID WHERE PLACES.ID IS NULL ORDER BY SHAREDTICKETS.ID"
                , null
                , new SerializerReadClass(SharedTicketInfo.class)).list();
    }
    
    
    public final void updateSharedTicket(final String id, final TicketInfo ticket) throws BasicException {
         
        Object[] values = new Object[] {id, ticket.getNameWithExt(), ticket};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ? WHERE ID = ?"
                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 0})).exec(values);
    }
    
    public final void insertSharedTicket(final String id, final TicketInfo ticket) throws BasicException {
        
        Object[] values = new Object[] {id, ticket.getNameWithExt(), ticket};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE};
        
        new PreparedSentence(s
            , "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT) VALUES (?, ?, ?)"
            , new SerializerWriteBasicExt(datas, new int[] {0, 1, 2})).exec(values);
    }
    
    public final void deleteSharedTicket(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM SHAREDTICKETS WHERE ID = ?"
            , SerializerWriteString.INSTANCE).exec(id);      
    }    
}
