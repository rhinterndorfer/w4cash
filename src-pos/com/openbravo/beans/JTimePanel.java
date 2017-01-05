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

package com.openbravo.beans;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.PropertyUtil;

public class JTimePanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 8113303906519658408L;
	public final static int BUTTONS_ALL = 3;
	public final static int BUTTONS_HOUR = 1;
	public final static int BUTTONS_MINUTE = 2;

	private DateFormat fmtTime = DateFormat.getTimeInstance(DateFormat.SHORT);

	private JClockPanel m_jclock;
	private Date m_dMinDate;
	private Date m_dMaxDate;

	private JButtonDate m_jbtnplushour = null;
	private JButtonDate m_jbtnminushour = null;
	private JButtonDate m_jbtnplusfifteen = null;
	private JButtonDate m_jbtnminusfifteen = null;
	private JButtonDate m_jbtnplusminute = null;
	private JButtonDate m_jbtnminusminute = null;
	private AppView m_App;

	/** Creates new form JTimePanel */
	public JTimePanel(AppView app) {
		this(app, null, BUTTONS_ALL);
	}

	public JTimePanel(AppView app, Date dDate) {
		this(app, dDate, BUTTONS_ALL);
	}

	public JTimePanel(AppView app, Date dDate, int iButtons) {
		super();

		m_App = app;

		initComponents();

		m_jclock = new JClockPanel(false);
		jPanel2.add(m_jclock, BorderLayout.CENTER);

		GregorianCalendar c;
		DateFormat f = new SimpleDateFormat("H:mm");

		ActionListener dateclick = new DateClick();

		if ((iButtons & BUTTONS_HOUR) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 1, 0);
			m_jbtnplushour = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png")), dateclick);
			m_jactions.add(m_jbtnplushour);
		}

		if ((iButtons & BUTTONS_MINUTE) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 0, 15);
			m_jbtnplusfifteen = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png")), dateclick);
			m_jactions.add(m_jbtnplusfifteen);
		}

		if ((iButtons & BUTTONS_MINUTE) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 0, 1);
			m_jbtnplusminute = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/uparrow.png")), dateclick);
			m_jactions.add(m_jbtnplusminute);
		}
		// c = new GregorianCalendar(1900, 0, 0, 0, 0);
		// m_jbtnmidnight = new JButtonDate(f.format(c.getTime()), dateclick);
		// m_jactions.add(m_jbtnmidnight);

		if ((iButtons & BUTTONS_MINUTE) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 0, 1);
			m_jbtnminusminute = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png")), dateclick);
			m_jactions.add(m_jbtnminusminute);
		}

		if ((iButtons & BUTTONS_MINUTE) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 0, 15);
			m_jbtnminusfifteen = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png")), dateclick);
			m_jactions.add(m_jbtnminusfifteen);
		}

		if ((iButtons & BUTTONS_HOUR) > 0) {
			c = new GregorianCalendar(1900, 0, 0, 1, 0);
			m_jbtnminushour = new JButtonDate(f.format(c.getTime()),
					new ImageIcon(getClass().getResource("/com/openbravo/images/downarrow.png")), dateclick);
			m_jactions.add(m_jbtnminushour);
		}

		m_dMinDate = null;
		m_dMaxDate = null;
		m_jclock.setTime(dDate);
		renderTime();

		ScaleButtons();

	}

	public void setDateMidNight() {
		setDate(new GregorianCalendar(1900, 0, 0, 0, 0).getTime());
	}

	public void setDate(Date dNewDate) {

		Date dOldDate = m_jclock.getTime();
		if (((dNewDate == null && dOldDate != null) || (dNewDate != null && !dNewDate.equals(dOldDate)))
				&& checkDates(dNewDate)) {
			m_jclock.setTime(dNewDate);
			renderTime();
			firePropertyChange("Date", dOldDate, dNewDate); // decimos al mundo
															// que ha cambiado
															// la propiedad
															// fecha
		}
	}

	private boolean checkDates(Date dDate) {
		return dDate == null || (m_dMaxDate == null || m_dMaxDate.compareTo(dDate) > 0)
				&& (m_dMinDate == null || m_dMinDate.compareTo(dDate) <= 0);
	}

	public Date getDate() {
		return m_jclock.getTime();
	}

	public void setCheckDates(Date dMinDate, Date dMaxDate) {

		m_dMinDate = dMinDate;
		m_dMaxDate = dMaxDate;
		setDate(null);
		renderTime(); // este quiza sobra.
	}

	public void setEnabled(boolean bValue) {

		super.setEnabled(bValue);
		renderTime();
	}

	public void setPeriod(long period) {
		m_jclock.setPeriod(period);
		renderTime();
	}

	private void renderTime() {

		Date dDate = m_jclock.getTime();
		if (dDate == null) {
			if (m_jbtnplushour != null)
				m_jbtnplushour.setEnabled(false);
			if (m_jbtnminushour != null)
				m_jbtnminushour.setEnabled(false);
			if (m_jbtnplusfifteen != null)
				m_jbtnplusfifteen.setEnabled(false);
			if (m_jbtnminusfifteen != null)
				m_jbtnminusfifteen.setEnabled(false);
			if (m_jbtnplusminute != null)
				m_jbtnplusminute.setEnabled(false);
			if (m_jbtnminusminute != null)
				m_jbtnminusminute.setEnabled(false);
			m_jlblTime.setText("  ");
			m_jlblSeparator.setVisible(false);
			m_jlblTime2.setVisible(false);
			m_jtime.revalidate();
		} else {
			GregorianCalendar oCalRender = new GregorianCalendar();
			oCalRender.setTime(dDate);
			// int iDay = oCalRender.get(Calendar.DAY_OF_MONTH);

			oCalRender.add(Calendar.HOUR_OF_DAY, 1);
			if (m_jbtnplushour != null) {
				m_jbtnplushour.DateInf = oCalRender.getTime();
				m_jbtnplushour.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}
			oCalRender.add(Calendar.HOUR_OF_DAY, -2);
			if (m_jbtnminushour != null) {
				m_jbtnminushour.DateInf = oCalRender.getTime();
				m_jbtnminushour.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}

			oCalRender.setTime(dDate);

			oCalRender.add(Calendar.MINUTE, 15);
			if (m_jbtnplusfifteen != null) {
				m_jbtnplusfifteen.DateInf = oCalRender.getTime();
				m_jbtnplusfifteen.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}
			oCalRender.add(Calendar.MINUTE, -30);
			if (m_jbtnminusfifteen != null) {
				m_jbtnminusfifteen.DateInf = oCalRender.getTime();
				m_jbtnminusfifteen.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}
			oCalRender.setTime(dDate);

			oCalRender.add(Calendar.MINUTE, 1);
			if (m_jbtnplusminute != null) {
				m_jbtnplusminute.DateInf = oCalRender.getTime();
				m_jbtnplusminute.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}
			oCalRender.add(Calendar.MINUTE, -2);
			if (m_jbtnminusminute != null) {
				m_jbtnminusminute.DateInf = oCalRender.getTime();
				m_jbtnminusminute.setEnabled(isEnabled() && checkDates(oCalRender.getTime()));
			}

			if (m_jclock.getPeriod() > 0L) {
				// damos el periodo
				m_jlblTime.setText(fmtTime.format(dDate));
				m_jlblTime2.setText(fmtTime.format(new Date(dDate.getTime() + m_jclock.getPeriod())));
				m_jlblSeparator.setVisible(true);
				m_jlblTime2.setVisible(true);
				m_jtime.revalidate();
			} else {
				// es una hora normal
				m_jlblTime.setText(fmtTime.format(dDate));
				m_jlblSeparator.setVisible(false);
				m_jlblTime2.setVisible(false);
				m_jtime.revalidate();
			}
		}

		m_jclock.setEnabled(isEnabled());
	}

	private class DateClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JButtonDate oLbl = (JButtonDate) e.getSource();
			if (oLbl.DateInf != null) {
				setDate(oLbl.DateInf);
			}
		}
	}

	private static class JButtonDate extends JButton {

		private static final long serialVersionUID = -2034472897831561388L;
		public Date DateInf;

		@SuppressWarnings("unused")
		public JButtonDate(ActionListener datehandler) {
			super();
			initComponent();
			addActionListener(datehandler);
		}

		@SuppressWarnings("unused")
		public JButtonDate(String sText, ActionListener datehandler) {
			super(sText);
			initComponent();
			addActionListener(datehandler);
		}

		public JButtonDate(String sText, Icon ico, ActionListener datehandler) {
			super(sText, ico);
			initComponent();
			addActionListener(datehandler);
		}

		private void initComponent() {
			DateInf = null;
			setRequestFocusEnabled(false);
			setFocusPainted(false);
			setFocusable(false);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		m_jactions = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		m_jtime = new javax.swing.JPanel();
		m_jlblTime = new javax.swing.JLabel();
		m_jlblSeparator = new javax.swing.JLabel();
		m_jlblTime2 = new javax.swing.JLabel();

		setLayout(new java.awt.BorderLayout());

		jPanel1.setLayout(new java.awt.BorderLayout());

		m_jactions.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		m_jactions.setLayout(new java.awt.GridLayout(0, 1, 0, 5));
		jPanel1.add(m_jactions, java.awt.BorderLayout.NORTH);

		add(jPanel1, java.awt.BorderLayout.LINE_END);

		jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel2.setLayout(new java.awt.BorderLayout());

		m_jtime.add(m_jlblTime);

		m_jlblSeparator.setText(" - ");
		m_jtime.add(m_jlblSeparator);
		m_jtime.add(m_jlblTime2);

		jPanel2.add(m_jtime, java.awt.BorderLayout.NORTH);

		add(jPanel2, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

	private void ScaleButtons() {
		int menuwidth = Integer.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-width", "48"));
		int menuheight = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-touchsmall-heigh", "48"));
		int fontsize = Integer
				.parseInt(PropertyUtil.getProperty(m_App, "Ticket.Buttons", "button-small-fontsize", "16"));

		if (m_jbtnplushour != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnplushour, menuwidth, menuheight, JButton.RIGHT, fontsize);
		if (m_jbtnminushour != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnminushour, menuwidth, menuheight, JButton.RIGHT, fontsize);
		if (m_jbtnplusfifteen != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnplusfifteen, menuwidth, menuheight, JButton.RIGHT, fontsize);
		if (m_jbtnminusfifteen != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnminusfifteen, menuwidth, menuheight, JButton.RIGHT, fontsize);
		if (m_jbtnplusminute != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnplusminute, menuwidth, menuheight, JButton.RIGHT, fontsize);
		if (m_jbtnminusminute != null)
			PropertyUtil.ScaleButtonIcon(m_jbtnminusminute, menuwidth, menuheight, JButton.RIGHT, fontsize);
		
		PropertyUtil.ScaleLabelFontsize(m_App, m_jlblTime2, "common-small-fontsize", "32");
		PropertyUtil.ScaleLabelFontsize(m_App, m_jlblTime, "common-small-fontsize", "32");
		
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel m_jactions;
	private javax.swing.JLabel m_jlblSeparator;
	private javax.swing.JLabel m_jlblTime;
	private javax.swing.JLabel m_jlblTime2;
	private javax.swing.JPanel m_jtime;
	// End of variables declaration//GEN-END:variables

}
