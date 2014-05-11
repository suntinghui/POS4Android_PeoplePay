package com.people.qpos;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;

/**
 * 关闭刷卡器
 * 
 * @author Fncat
 * 
 */
public class ThreadCloseSwip extends Thread {
	private Handler mHandler;
	private Context mContext;

	public ThreadCloseSwip(Handler mHandler, Context mContext) {
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	public void run() {
		QPOS.getCardReader().powerOff();
		QPOS.HandData(mHandler, "close Device Success", 0);
	}

}
