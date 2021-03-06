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

import java.util.Date;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.scale.DeviceScale;
import com.openbravo.pos.scanpal2.DeviceScanner;

/**
 *
 * @author adrianromero
 */
public interface AppView {
    
    public DeviceScale getDeviceScale();
    public DeviceTicket getDeviceTicket();
    public DeviceScanner getDeviceScanner();
      
    public Session getSession();
    public AppProperties getProperties();
    public Object getBean(String beanfactory) throws BeanFactoryException;
     

    public String getLastCashIndex() throws BasicException;
    public String getActiveCashIndex(Boolean openNew, Boolean ignoreCache) throws BasicException;
    public int getActiveCashSequence();
    public Date getActiveCashDateStart() throws BasicException;
    public Date getActiveCashDateEnd() throws BasicException;
    public void setActiveCashDateEnd(Date dateEnd);
    
    public String getInventoryLocation();
    public String getHost();
    public String getWindowsHost();
    
    public void waitCursorBegin();
    public void waitCursorEnd();
    
    public AppUserView getAppUserView();
    
    void closeCashIndex();
}

