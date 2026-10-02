package com.openbravo.pos.forms;



public abstract class JRootGUI extends javax.swing.JFrame {

	public Boolean initFrame(AppProperties app) {
		return true;
	}

	protected abstract AppView getAppView();
}
