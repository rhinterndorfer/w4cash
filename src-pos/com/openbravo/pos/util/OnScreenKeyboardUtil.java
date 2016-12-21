package com.openbravo.pos.util;

import java.io.File;

public class OnScreenKeyboardUtil {
	public static void StartOSK()
	{
		File touchKeyboard = new File("c:\\\\Program Files\\Common Files\\microsoft shared\\ink\\TabTip.exe");
		if (touchKeyboard.exists()) {

			try {
				String fullPath = touchKeyboard.getAbsolutePath();
				java.lang.Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", fullPath });
			} catch (Exception ex) {
				// do nothing
			}

		} else {
			try {
				java.lang.Runtime.getRuntime().exec(new String[] { "cmd.exe", "c:\\windows\\sysnative\\cmd.exe /c cd \\ && osk.exe" });
			} catch (Exception ex) {
				// do nothing
			}
		}
	}

}
