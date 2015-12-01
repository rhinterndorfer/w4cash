package com.openbravo.pos.mant;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.JTicketsBag;
import com.openbravo.pos.sales.TicketsEditor;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurantMap;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.sales.simple.JTicketsBagSimple;

public abstract class JPlacesBag extends JPanel {

	protected AppView m_App;
	protected DataLogicSales m_dlSales;
	protected PlacesEditor m_panelticket;

	/** Creates new form JTicketsBag */
	public JPlacesBag(AppView oApp/* , PlacesEditor panelticket */) {
		m_App = oApp;
		// m_panelticket = panelticket;
		m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
	}

	public abstract void activate();

	public abstract boolean deactivate();

	public abstract void deleteTicket();

	protected abstract JComponent getBagComponent();

	protected abstract JComponent getNullComponent();

	public abstract void ScaleButtons(int btnWidth, int btnHeight);
	
	protected void ScaleButtonIcon(javax.swing.JButton btn, int width, int height) {
		if (javax.swing.ImageIcon.class.isAssignableFrom(btn.getIcon().getClass())) {
			javax.swing.ImageIcon icon = javax.swing.ImageIcon.class.cast(btn.getIcon());
			double radio = icon.getIconWidth() / icon.getIconWidth();
			Image img = icon.getImage().getScaledInstance(radio > 1 ? width : -1, radio > 1 ? -1 : height,
					Image.SCALE_SMOOTH);
			ImageIcon icon2 = new javax.swing.ImageIcon(img);
			btn.setIcon(icon2);

			btn.setSize(width, height);
		}
	}

	public static JPlacesBag createPlacesBag(String sName, AppView app, PlacesEditor editor) {

		if ("standard".equals(sName)) {
			// return new JTicketsBagMulti(oApp, user, panelticket);
			return new JPlacesBagShared(app);
		} else if ("restaurant".equals(sName)) {
			return new JPlacesBagRestaurantMap(app, editor);
		} else { // "simple"
			return new JPlacesBagSimple(app);
		}
	}

	public abstract void selectPlace(String id);

	public abstract Place getPlace(int index);
}
