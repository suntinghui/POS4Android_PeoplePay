package com.people.qpos;

import dspread.voicemodem.CardReader;
import android.content.Context;
import android.os.Handler;

/**
 * 关闭刷卡器
 * 
 * @author Fncat
 * 
 */
public class ThreadCancel extends Thread {
	private Handler mHandler;
	private Context mContext;

	public ThreadCancel(Handler mHandler, Context mContext) {
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			int resultCode = QPOS.getCardReader().doTradeEx("100", 255, null, "", 20); 
			
			if (resultCode == CardReader.SUCCESS) {
				QPOS.HandData(mHandler, "取消成功！", CardReader.SUCCESS);
				
			} else if (resultCode == CardReader.TIMEOUT) {
				QPOS.HandData(mHandler, "取消超时！", CardReader.TIMEOUT);
				
			} else {
				QPOS.HandData(mHandler, "doTradeEx奇怪的返回：" + resultCode, -1);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
