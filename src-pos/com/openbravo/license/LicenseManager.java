package com.openbravo.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.JRootGUI;
import com.openbravo.pos.forms.W4CashSha1;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LicenseManager {

	private int license;
	private DeviceInfo dev;

	public LicenseManager() {

	}

	public int getLicense() {
		return this.license;
	}

	public static String getMAC() {
		Map<String, String> buffer = new LinkedHashMap<>();
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec("ipconfig /all");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			Pattern pAddress = Pattern.compile(".*Physical Addres.*: (.*)");
			Pattern pName = Pattern.compile(".*Description.*: (.*)");

			String name = null;
			String line = "";
			while ((line = input.readLine()) != null) {
				Matcher mm = pName.matcher(line);
				if (mm.matches()) {
					name = mm.group(1);
				}

				mm = pAddress.matcher(line);
				if (mm.matches()) {
					buffer.put(name, mm.group(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String macaddress = null;

		try {
			NetworkInterface localhost = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());

			boolean isLoopback1 = localhost.isLoopback();
			// boolean isLoopback2 = loopback.isLoopback();

			// there is an active network interface
			if (!isLoopback1 && !localhost.equals(loopback)) {
				byte[] mac = localhost.getHardwareAddress();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
				macaddress = sb.toString();
			}
			// inactive nic
			else {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface adapter = interfaces.nextElement();
					// byte[] mac = adapter.getHardwareAddress();
					String mac2use = buffer.get(adapter.getDisplayName());

					if (mac2use != null) {
						macaddress = mac2use;
						break;
					}

				}
			}
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		return macaddress;
	}

	// private String getMotherboardSN() {
	// String result = "";
	// try {
	// File file = File.createTempFile("realhowto", ".vbs");
	// file.deleteOnExit();
	// FileWriter fw = new java.io.FileWriter(file);
	//
	// String vbs = "Set objWMIService =
	// GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
	// + "Set colItems = objWMIService.ExecQuery _ \n" + " (\"Select * from
	// Win32_BaseBoard\") \n"
	// + "For Each objItem in colItems \n" + " Wscript.Echo objItem.SerialNumber
	// \n"
	// + " exit for ' do the first cpu only! \n" + "Next \n";
	//
	// fw.write(vbs);
	// fw.close();
	// Process p = Runtime.getRuntime().exec("cscript //NoLogo " +
	// file.getPath());
	// BufferedReader input = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	// String line;
	// while ((line = input.readLine()) != null) {
	// result += line;
	// }
	// input.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result.trim();
	// }

	// private String getSerialNumber(String drive) {
	// String result = "";
	// try {
	// File file = File.createTempFile("realhowto", ".vbs");
	// file.deleteOnExit();
	// FileWriter fw = null;
	// try {
	// fw = new java.io.FileWriter(file);
	// String vbs = "Set objFSO =
	// CreateObject(\"Scripting.FileSystemObject\")\n"
	// + "Set colDrives = objFSO.Drives\n" + "Set objDrive = colDrives.item(\""
	// + drive + "\")\n"
	// + "Wscript.Echo objDrive.SerialNumber"; // see note
	// fw.write(vbs);
	// } finally {
	// if (fw != null) {
	// try {
	// fw.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// Process p = Runtime.getRuntime().exec("cscript //NoLogo " +
	// file.getPath());
	// BufferedReader input = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	// String line;
	// while ((line = input.readLine()) != null) {
	// result += line;
	// }
	// input.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result.trim();
	// }

	public DeviceInfo readDeviceInfo(AppView app)  {
		String host = app.getProperties().getHost();
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
			String host = app.getProperties().getHost();
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
			this.license = checklicense(app, root, this.dev);
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

	private int checklicense(AppView app, JRootGUI root, DeviceInfo info) {

		try {

			boolean validLicense = info.validateLicense();

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
