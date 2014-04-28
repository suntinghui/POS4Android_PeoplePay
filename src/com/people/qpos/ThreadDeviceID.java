package com.people.qpos;

import com.people.util.Utils;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;
import dspread.voicemodem.Tip;

/**
 * 获取设备终端号
 * 
 * @author Fncat
 * 
 */
public class ThreadDeviceID extends Thread {
	private Handler mHandler;
	private Context mContext;
	private CardReader c;

	public ThreadDeviceID(Handler mHandler, Context mContext, CardReader c) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
	}

	@Override
	public void run() {
		int r = c.doGetTerminalID();
		if (r == CardReader.SUCCESS) {
			String id = c.getTerminalIDTid();
			Utils.HandData(mHandler, "To:" + id, 0);
		} else {
			Utils.HandData(mHandler, "fail", 0);
		}
	}

}
