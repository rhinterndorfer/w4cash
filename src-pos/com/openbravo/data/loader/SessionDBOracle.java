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

package com.openbravo.data.loader;

import java.sql.SQLException;
import java.sql.Statement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.JDBCSentence.JDBCDataResultSet;
import com.openbravo.pos.util.Log;

/**
 *
 * @author adrianromero
 */
public class SessionDBOracle implements SessionDB {

    public String TRUE() {
        return "1";
    }
    public String FALSE() {
        return "0";
    }
    public String INTEGER_NULL() {
        return "CAST(NULL AS INTEGER)";
    }
    public String CHAR_NULL() {
        return "CAST(NULL AS CHAR)";
    }

    public String getName() {
        return "Oracle";
    }

    public SentenceFind getSequenceSentence(Session s, String sequence) throws BasicException {
    	StaticSentence lock = new StaticSentence(s, "LOCK TABLE TICKETS IN EXCLUSIVE MODE", null, null);
    	try {
    		lock.exec();
    	}
    	finally {
    		lock.closeExec();
    	}
    	
    	String sql=String.format("SELECT MAX(TICKETID)+1 FROM TICKETS WHERE TICKETID IS NOT NULL");
    	return new StaticSentence(s, sql, null, SerializerReadInteger.INSTANCE);
    }
    
    
    public SentenceFind getCashSequenceSentence(Session s, String sequence) throws BasicException {
    	StaticSentence lock = new StaticSentence(s, "LOCK TABLE TICKETS IN EXCLUSIVE MODE", null, null);
    	try {
    		lock.exec();
    	}
    	finally {
    		lock.closeExec();
    	}
    	
    	String sql=String.format("SELECT MAX(CASHTICKETID)+1 FROM TICKETS WHERE CASHTICKETID IS NOT NULL");
    	return new StaticSentence(s, sql, null, SerializerReadInteger.INSTANCE);
    }
    
    public SentenceFind getCashSumSentence(Session s) throws BasicException
    {
    	StaticSentence lock = new StaticSentence(s, "LOCK TABLE TICKETS IN EXCLUSIVE MODE", null, null);
    	try {
    		lock.exec();
    	}
    	finally {
    		lock.closeExec();
    	}
    	
    	
    	String sql="SELECT CASHSUMCOUNTER FROM TICKETS WHERE CASHTICKETID = (SELECT MAX(CASHTICKETID) FROM TICKETS WHERE CASHTICKETID IS NOT NULL)";
    	return new StaticSentence(s, sql, null, SerializerReadDouble.INSTANCE);
    }
    
    public Boolean checkConnection(Session s) throws BasicException{
    	String sql="SELECT 1 FROM DUAL";
    	Statement m_Stmt = null;
    	try {
    		m_Stmt = s.getConnectionNoCheck().createStatement();
	        m_Stmt.setQueryTimeout(5);
	        m_Stmt.execute(sql);
	        
	    } catch (SQLException eSQL) {
	    	Log.Exception("Error executing static SQL: " + sql, eSQL);
	    	throw new BasicException(eSQL);
	    }
    	finally {
    		if(m_Stmt != null) {
    			try {
    				m_Stmt.close();
    			} catch (SQLException eSQL) {
    				// do nothing
    		    }
    		}
    	}
    	return true;
    }
    
}