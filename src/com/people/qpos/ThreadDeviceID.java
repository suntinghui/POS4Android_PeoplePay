package com.people.qpos;

import java.util.HashMap;

import com.people.util.StringUtil;

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
			String tid = QPOS.getCardReader().getTerminalIDTid();
			String pid = QPOS.getCardReader().getTerminalIDPid();
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("TID", tid); // TerminalID
			// TODO
			map.put("PID", StringUtil.hexToASCII(pid)); // PSAMID
			
			QPOS.HandData(mHandler, map, CardReader.SUCCESS);
		} else {
			QPOS.HandData(mHandler, null, -1);
		}
	}

}
