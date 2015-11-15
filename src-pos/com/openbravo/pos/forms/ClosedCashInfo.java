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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class ClosedCashInfo implements SerializableRead, SerializableWrite, IKeyed {
    
    private String id;
    private String host;
    private Integer sequence;
    private Date datestart;
    private Date dateend;
    
    /** Creates a new instance of SharedTicketInfo */
    public ClosedCashInfo() {
    }
    
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        host = dr.getString(2);
        sequence = dr.getInt(3);
        datestart = dr.getTimestamp(4);
        dateend = dr.getTimestamp(5);
    }   
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, host);
        dp.setInt(3, sequence);
        dp.setTimestamp(4, datestart);
        dp.setTimestamp(5, dateend);
    }
    
    public String getId() {
        return id;
    }
    
    public String getHost() {
        return host;
    }
    
    public Integer getSequence() {
        return sequence;
    }
    
    public Date getDateStart() {
        return datestart;
    }
    
    public Date getDateEnd() {
    	return dateend;
    }

	@Override
	public Object getKey() {
		return sequence;
	}
	
	@Override
    public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		StringBuilder sb = new StringBuilder();
		sb.append(sequence.toString());
		sb.append(": ");
        if(datestart != null)
        	sb.append(sdf.format(datestart));
        sb.append(" - ");
        if(dateend != null)
        	sb.append(sdf.format(dateend));
        
        return sb.toString();
    }
    
}
