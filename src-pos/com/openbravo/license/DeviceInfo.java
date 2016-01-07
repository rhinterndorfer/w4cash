package com.openbravo.license;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppView;

public class DeviceInfo {
	private static final long DIF_WEEK = 24 * 60 * 60 * 1000 * 7;
	public static final long R_DAY = 25 * 60 * 60 * 1000;
	public static final int DURATION_DEMO_DAYS = 7;

	private String m_DeviceId;
	private String m_InstallDate;
	private String m_License;

	public DeviceInfo() {
		m_DeviceId = null;
		m_InstallDate = null;
	}

	public int evaluationLicense() {
		long startdate = Long.parseLong(this.m_InstallDate);
		long date_current = System.currentTimeMillis();

		if (startdate + DIF_WEEK < date_current) {
			return -1;
		}

		return 1;
	}

	public String getDeviceId() {
		return m_DeviceId;
	}

	public String getInstall() {
		return m_InstallDate;
	}

	public String getLicense() {
		return this.m_License;
	}

	public void setDeviceId(String DeviceId) {
		this.m_DeviceId = DeviceId;
	}

	public void setInstallDate(String Install) {
		this.m_InstallDate = Install;
	}

	public static SerializerRead getSerializerRead() {
		return new SerializerRead() {

			public Object readValues(DataRead dr) throws BasicException {
				DeviceInfo device = new DeviceInfo();
				device.m_DeviceId = dr.getString(1);
				device.m_InstallDate = dr.getString(2);
				device.m_License = dr.getString(3);
				return device;
			}
		};
	}

	public int writeDeviceInfoLicense(AppView app, String license) throws BasicException {

		int sql = new StaticSentence(app.getSession(), "UPDATE DEVICE SET LICENSE = ? WHERE ID = ?",
				SerializerWriteParams.INSTANCE).exec(new DataParams() {
					public void writeValues() throws BasicException {
						setString(1, license);
						setString(2, m_DeviceId);
					}
				});
		if (sql == 0) {
			sql = new StaticSentence(app.getSession(), "INSERT INTO DEVICE(ID, INSTALL, LICENSE) values (?,?,?)",
					SerializerWriteParams.INSTANCE).exec(new DataParams() {
						public void writeValues() throws BasicException {
							setString(1, m_DeviceId);
							setString(2, m_InstallDate);
							setString(3, license);
						}
					});
		}
		this.m_License = license;

		return sql;
	}

	public boolean validateLicense(String currentLizense) {
		if (currentLizense == null || this.m_License == null || !currentLizense.equals(this.m_License)) {
			return false;
		}
		return true;
	}
}
