package com.people.qpos;

import com.people.util.Utils;

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
	private CardReader c;

	public ThreadCloseSwip(Handler mHandler, Context mContext, CardReader c) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
	}

	@Override
	public void run() {
		c.powerOff();
		Utils.HandData(mHandler, "close Device Success", 0);
	}

}
