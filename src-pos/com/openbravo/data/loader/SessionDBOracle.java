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

import com.openbravo.basic.BasicException;

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
    	String ticketType;
    	if("TICKETSNUM".equals(sequence))
    		ticketType = "0,1";
    	else if("TICKETSNUM_REFUND".equals(sequence))
    		ticketType = "0,1";
    	else if("TICKETSNUM_PAYMENT".equals(sequence))
    		ticketType = "2";
		else
			throw new BasicException(String.format("Unkwnon sequence string %1", sequence));
    	
    	String sql=String.format("SELECT TICKETID+1 FROM TICKETS WHERE TICKETTYPE in (%1$s) AND TICKETID = (SELECT MAX(TICKETID) FROM TICKETS WHERE TICKETTYPE in (%1$s)) FOR UPDATE", ticketType);
    	return new StaticSentence(s, sql, null, SerializerReadInteger.INSTANCE);
    }
}