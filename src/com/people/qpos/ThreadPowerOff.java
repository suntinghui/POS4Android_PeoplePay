package com.people.qpos;

import android.content.Context;
import android.os.Handler;

/**
 * 重启刷卡器
 * 
 * @author Fncat
 * 
 */
public class ThreadPowerOff extends Thread {
	private Handler mHandler;
	private Context mContext;

	public ThreadPowerOff(Handler mHandler, Context mContext) {
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	public void run() {
		try {
			QPOS.getCardReader().powerOff();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
