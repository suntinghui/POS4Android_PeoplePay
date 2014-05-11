package com.people.qpos;

import android.os.Handler;
import android.os.Message;

import com.people.activity.BaseActivity;

import dspread.voicemodem.CardReader;
import dspread.voicemodem.util;

public class QPOS {

	public static final byte[] fakekey = { (byte) 0x00, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };

	private static CardReader reader = null;

	public static CardReader getCardReader() {
		if (null == reader) {
			// 默认蓝牙
			reader = CardReader.getInstance(BaseActivity.getTopActivity(), CardReader.BLUETOOTHMODE);
			util.turnUpVolume(BaseActivity.getTopActivity().getBaseContext(), 8);
		}

		return reader;
	}

	public static CardReader resetCardReader(int model) {
		reader = CardReader.getInstance(BaseActivity.getTopActivity(), model);
		util.turnUpVolume(BaseActivity.getTopActivity().getBaseContext(), 8);
		return reader;
	}
	
	public static void HandData(Handler mHandler, Object mStr, int num) {
		Message message = mHandler.obtainMessage();
		message.obj = mStr;
		message.what = num;
		mHandler.sendMessage(message);
	}

}
