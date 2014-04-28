package com.people.util;

import android.os.Handler;
import android.os.Message;

public class Utils {
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static void HandData(Handler mHandler, Object mStr, int num) {
		Message messaged = mHandler.obtainMessage();
		messaged.obj = mStr;
		messaged.what = num;
		mHandler.sendMessage(messaged);
	}
}
