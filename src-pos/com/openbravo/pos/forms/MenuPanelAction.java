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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import com.openbravo.pos.util.PropertyUtil;

/**
 *
 * @author adrianromero
 */
public class MenuPanelAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppView m_App;
    private String m_sMyView;

    /** Creates a new instance of MenuPanelAction */
    public MenuPanelAction(AppView app, String icon, String keytext, String sMyView) {
    	ImageIcon im = new ImageIcon(JPrincipalApp.class.getResource(icon));
		PropertyUtil.ScaleIconImage(app, im,"main-submenu-image-width" ,"main-submenu-image-height" , "22", "22");
		putValue(Action.SMALL_ICON, im);
        putValue(Action.NAME, AppLocal.getIntString(keytext));
        putValue(AppUserView.ACTION_TASKNAME, sMyView);
        m_App = app;
        m_sMyView = sMyView;
    }
    
    public void actionPerformed(ActionEvent evt) {
    	LoadingIndicator loadingIndicator = LoadingIndicator.start();
    	Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				m_App.getAppUserView().showTask(m_sMyView);
				loadingIndicator.done();
			}

		});
    	t.start();
        
    }    
}
