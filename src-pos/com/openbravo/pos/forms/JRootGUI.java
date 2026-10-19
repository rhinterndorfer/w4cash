package com.openbravo.pos.forms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public abstract class JRootGUI extends javax.swing.JFrame {

	public Boolean initFrame(AppProperties app, CommandLine cmd) {
		return true;
	}

	protected abstract AppView getAppView();
}
