package com.openbravo.license;

import java.io.IOException;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.hbsoft.w4cash.license.LicenseTool;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.JRootGUI;


public class LicenseManager {

	private int license;
	private DeviceInfo dev;

	public LicenseManager() {

	}

	public int getLicense() {
		return this.license;
	}

	public static String getHost() {
		String hostname = System.getenv("COMPUTERNAME"); 
		if(hostname == null)
			hostname = System.getenv("HOSTNAME");
		
		return hostname;
	}
	
	public static String getMAC() {
		String macaddress = null;
		List<byte[]> macs = new ArrayList<byte[]>();

		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface adapter = interfaces.nextElement();

				if (!adapter.isPointToPoint() && !adapter.isVirtual() && !adapter.isLoopback()
						&& adapter.getHardwareAddress() != null
						&& adapter.getDisplayName() != null
						&& !adapter.getDisplayName().contains("TeamViewer") // exclude TeamViewer virtual network adapters
						) {
					byte[] mac = adapter.getHardwareAddress();
					macs.add(mac);
				}
			}

			if(macs.size() > 0)
			{
				StringBuffer sb = new StringBuffer();
				byte[] mac = macs.get(0);
				for (int i = 0; i < mac.length; i++) {
					byte single = 1;
					for (int j = 0; j < macs.size(); j++) {
						// xor all macs
						single = (byte)((int)macs.get(j)[i] * (int)single);
					}
					sb.append(String.format("%02X%s", single, (i < mac.length - 1) ? "-" : ""));
				}
			
			macaddress = sb.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (macaddress == null)
			macaddress = "00-00-00-00-00-00";

		return macaddress;
	}

	public DeviceInfo readDeviceInfo(AppView app) {
		String host = getHost();
		String mac = getMAC();
		String common = host + "/" + mac;
		String startup = "" + System.currentTimeMillis();

		List<DeviceInfo> devices = new ArrayList<>();
		try {
			devices = getDeviceInfo(app, common, startup);
		} catch (BasicException e) {
			e.printStackTrace();
		}

		DeviceInfo devinfo = null;
		for (Object device : devices) {
			String id = ((DeviceInfo) device).getDeviceId();

			if ((common).equals(id)) {
				devinfo = (DeviceInfo) device;
				break;
			}
		}
		return devinfo;
	}

	public int getRemainingDaysOfLicense() {
		long install = Long.parseLong(this.dev.getInstall());
		long current = System.currentTimeMillis();

		long dif = current - install;
		int day = (int) (dif / DeviceInfo.R_DAY);

		if (day > 7) {
			day = 7;
		}

		return DeviceInfo.DURATION_DEMO_DAYS - day;
	}

	public void init(AppView app, JRootGUI root) {
		try {
			// String host = app.getProperties().getHost();
			String host = getHost();
			String mac = getMAC();
			String common = host + "/" + mac;
			String startup = "" + System.currentTimeMillis();

			List<DeviceInfo> devices = getDeviceInfo(app, common, startup);
			this.dev = null;
			for (Object device : devices) {
				String id = ((DeviceInfo) device).getDeviceId();

				if ((common).equals(id)) {
					this.dev = (DeviceInfo) device;
					break;
				}
			}
			// write into table device
			if (this.dev == null) {
				this.dev = writeDeviceInfo(app, host, mac, startup);
			}
			this.license = checklicense(app, root, this.dev, host, mac);
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	public static String toHexString(byte[] ba) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < ba.length; i++)
			str.append(String.format("%x", ba[i]));
		return str.toString();
	}

	public static String wrapKey(String deviceIdentification) {
		byte[] id = deviceIdentification.getBytes();
		BigInteger bi = new BigInteger(id);
		String hexString = bi.toString(16);

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < hexString.length(); i += 2) {
			str.append((char) Integer.parseInt(hexString.substring(i, i + 2), 16));
		}
		String result = str.toString();

		String b = toHexString(deviceIdentification.getBytes());
		System.out.println(b);
		int length = deviceIdentification.length() / 2;

		return b;
	}

	private int checklicense(AppView app, JRootGUI root, DeviceInfo info, String currenthost, String currentmac) {

		try {
			
			String[] currentLicenseKeyParts = LicenseTool.genLicenseUser(currenthost, currentmac);
			String currentLicenseKey = LicenseTool.formatKey(currentLicenseKeyParts, LicenseTool.DEFAULT_DELIMITER);
			String license2match = LicenseTool.genLicenseApplication(currentLicenseKey, LicenseTool.DEFAULT_DELIMITER);
			boolean validLicense = info.validateLicense(license2match);

			if (!validLicense) {
				throw new IOException();
			}

			return 0;
		} catch (IOException e) {
			// check for demo version
			JLicenseDialog dialog = JLicenseDialog.showDialog(app, root, AppLocal.getIntString("Button.License"));
			// license generated successful
			if (JLicenseDialog.OK == dialog.getReturnCode()) {
				try {
					info.writeDeviceInfoLicense(app, dialog.getLicense());
				} catch (BasicException e1) {
					e1.printStackTrace();
				}
				return 0;
			}
			// demo license
			int result = info.evaluationLicense();
			// expired demo version
			if (result < 0) {
				System.exit(0);
			}

		}
		return 1;
	}

	// private void showLicenseDialog(AppView app, JRootGUI root) {
	//
	// int code = JLicenseDialog.showDialog(app, root, "License", "license");
	// if (JLicenseDialog.CANCEL == code) {
	// System.exit(0);
	// }
	// }

	private List getDeviceInfo(AppView app, String id, String startup) throws BasicException {
		StaticSentence deviceList = new StaticSentence(app.getSession(),
				"SELECT ID, INSTALL, LICENSE FROM DEVICE WHERE ID='" + id + "'", SerializerWriteString.INSTANCE,
				DeviceInfo.getSerializerRead());

		List devices = deviceList.list();
		return devices;
	}

	private DeviceInfo writeDeviceInfo(AppView app, String host, String id, String startup) throws BasicException {
		String common = host + "/" + id;
		int deviceUpdate = new StaticSentence(app.getSession(), "INSERT INTO DEVICE(ID, INSTALL) values (?,?)",
				SerializerWriteParams.INSTANCE).exec(new DataParams() {
					public void writeValues() throws BasicException {
						setString(1, common);
						setString(2, startup);
					}
				});
		DeviceInfo info = new DeviceInfo();
		info.setDeviceId(common);
		info.setInstallDate(startup);
		return info;
	}

}
