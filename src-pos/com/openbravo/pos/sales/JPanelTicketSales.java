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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.catalog.CatalogSelector;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.PropertyUtil;

import java.awt.Dimension;

public class JPanelTicketSales extends JPanelTicket {

	private CatalogSelector m_cat;

	/** Creates a new instance of JPanelTicketSales */
	public JPanelTicketSales() {
	}

	@Override
	public void init(AppView app) {
		super.init(app);
		m_ticketlines.addListSelectionListener(new CatalogSelectionListener());
	}

	public String getTitle() {
		return null;
	}

	protected Component getSouthComponent() {
		m_cat = new JCatalog(m_App,dlSales, "true".equals(m_jbtnconfig.getProperty("pricevisible")),
				"true".equals(m_jbtnconfig.getProperty("taxesincluded")),
				Integer.parseInt(m_jbtnconfig.getProperty("product-img-width", "64")),
				Integer.parseInt(m_jbtnconfig.getProperty("product-img-height", "54")),
				Integer.parseInt(m_jbtnconfig.getProperty("category-img-width", "32")),
				Integer.parseInt(m_jbtnconfig.getProperty("category-img-height", "32")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-product-fontsize", "20")),
				Integer.parseInt(m_jbtnconfig.getProperty("button-category-fontsize", "20"))
		);
		m_cat.addActionListener(new CatalogListener());
		m_cat.setCatWidth(Integer.parseInt(m_jbtnconfig.getProperty("cat-width", "275")));
		m_cat.getComponent()
				.setPreferredSize(new Dimension(0, Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));

		m_cat.ScaleButtonIcons();

		return m_cat.getComponent();
	}

	protected void resetSouthComponent() {
		// do not reset current catalog selection
		m_cat.showCatalogPanel("");
	}

	protected JTicketsBag getJTicketsBag() {
		return JTicketsBag.createTicketsBag(m_App.getProperties().getProperty("machine.ticketsbag"), m_App, this);
	}

	@Override
	public void activate() throws BasicException {
		super.activate();
		m_cat.loadCatalog();
	}

	private class CatalogListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			buttonTransition((ProductInfoExt) e.getSource());
		}
	}

	private class CatalogSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {

			if (!e.getValueIsAdjusting()) {
				int i = m_ticketlines.getSelectedIndex();

				if (i >= 0) {
					// Look for the first non auxiliar product.
					while (i >= 0 && m_oTicket.getLine(i).isProductCom()) {
						i--;
					}

					// Show the accurate catalog panel...
					if (i >= 0) {
						m_cat.showCatalogPanel(m_oTicket.getLine(i).getProductID());
					} else {
						m_cat.showCatalogPanel(null);
					}
				}
			}
		}
	}
}
