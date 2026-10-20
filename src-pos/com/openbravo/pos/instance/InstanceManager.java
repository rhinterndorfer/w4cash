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

package com.openbravo.pos.instance;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.commons.cli.CommandLine;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;

/**
 *
 * @author adrianromero
 */
public class InstanceManager {
    
    /** Creates a new instance of InstanceManager */
    public InstanceManager(CommandLine cmd) throws Exception {
    	Boolean isServer = cmd == null ? false : cmd.hasOption('s');
    	String lockFileName = AppLocal.APP_ID + ".lock";
    	if(isServer) {
    		lockFileName = AppLocal.APP_ID + ".server.lock";
    	}
    	File lockFile = new File(new File(System.getProperty("user.home")), lockFileName);
    	if(!lockFile.exists())
    	{
			lockFile.createNewFile();
    	}	
    	
    	RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
    	FileChannel channel = raf.getChannel();
    	
    	// Ensure file has at least 1 byte
    	if (channel.size() == 0) {
    		raf.write(0);;
    	}
    	
    	FileLock lock = channel.tryLock(0, 1, false);
    	if(lock == null || !lock.isValid())
    		throw new BasicException("No exclusive lock");
    }    
}
