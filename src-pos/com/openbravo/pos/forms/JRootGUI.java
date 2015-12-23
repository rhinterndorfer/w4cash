package com.openbravo.pos.forms;

import com.openbravo.license.LicenseManager;

public abstract class JRootGUI extends javax.swing.JFrame {

	private LicenseManager manager = new LicenseManager();

	public void initFrame(AppProperties app) {
		manager.init(getAppView(), this);

		JRootApp root = (JRootApp) getAppView();

		switch (manager.getLicense()) {
		case 0:
			// license exist
			break;
		default:
			int days = manager.getRemainingDaysOfLicense();
			root.setMessage("Demoversion - "+days+" verbleibend!");
			break;
		}

		

	}
	// 0 license good
	// 1 evaluation license (7 days)

	public int getLicense() {
		return manager.getLicense();
	}

	protected abstract AppView getAppView();
}
