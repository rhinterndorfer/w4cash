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

package com.openbravo.data.loader;

import java.util.Dictionary;

import com.openbravo.format.Formats;

/**
 *
 * @author  Raphael Hinterndorfer
 */
public class RenderStringParent implements IRenderString {
    
    private Formats[] m_aFormats;
    private int[] m_aiIndex;
    private Dictionary<String, String> m_parents;
    
    /** Creates a new instance of StringnizerBasic */
    public RenderStringParent(Formats[] fmts, int[] aiIndex, Dictionary<String, String> parents) {
        m_aFormats = fmts; 
        m_aiIndex = aiIndex;
        m_parents = parents;
    }
    
    public String getRenderString(Object value) {
        
        if (value == null) {
            return null; 
        } else {
            Object [] avalue = (Object[]) value;
            StringBuffer sb = new StringBuffer();

            if(avalue.length > 1)
            {
            	if(avalue[m_aiIndex[0]] != null && !m_parents.isEmpty()) {
            		sb.append(m_parents.get(avalue[m_aiIndex[0]]));
            		sb.append(" - ");
            	}
            	sb.append(m_aFormats[m_aiIndex[1]].formatValue(avalue[m_aiIndex[1]]));
            }
            
            return sb.toString();
        }
    }  
   
}
