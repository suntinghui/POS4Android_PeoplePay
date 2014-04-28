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
public class ThreadCancel extends Thread {
	private Handler mHandler;
	private Context mContext;
	private CardReader c;

	public ThreadCancel(Handler mHandler, Context mContext, CardReader c) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			int r = c.doTradeEx("600.56", 255, null, "", 60); // please
		} catch (Exception e) {
			e.printStackTrace();
		}

		// // slide
		// if (r == CardReader.SUCCESS) {// card,
		// Utils.HandData(mHandler, "取消成功！", 0xDD);
		// } else if (r == CardReader.TIMEOUT) {
		// Utils.HandData(mHandler, "取消超时！", 0);
		// } else {
		// Utils.HandData(mHandler, "doTradeEx奇怪的返回：" + r, 0);
		// }
	}

}
