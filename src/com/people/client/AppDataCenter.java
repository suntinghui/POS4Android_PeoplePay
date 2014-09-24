package com.people.client;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppDataCenter {

	private static String __ADDRESS = "UNKNOWN";
	
	// 取系统追踪号，6个字节数字定长域
	public static String getTraceAuditNum() {
		SharedPreferences preferences = ApplicationEnvironment.getInstance().getPreferences();

		int number = preferences.getInt(Constants.kTRACEAUDITNUM, 1);

		Editor editor = preferences.edit();
		editor.putInt(Constants.kTRACEAUDITNUM, (number + 1) > 999999 ? 1 : (number + 1));
		editor.commit();

		String no = String.format("%06d", number);
		return no;
	}

	
	public static void setAddress(String address) {
		__ADDRESS = address;
	}
	
	public static String getAddress(){
		return __ADDRESS;
	}
}
