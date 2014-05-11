package com.people.qpos;

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

	public ThreadDeviceID(Handler mHandler, Context mContext) {
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	public void run() {
		int r = QPOS.getCardReader().doGetTerminalID();
		if (r == CardReader.SUCCESS) {
			String id = QPOS.getCardReader().getTerminalIDTid();
			QPOS.HandData(mHandler, id, CardReader.SUCCESS);
		} else {
			QPOS.HandData(mHandler, null, -1);
		}
	}

}
