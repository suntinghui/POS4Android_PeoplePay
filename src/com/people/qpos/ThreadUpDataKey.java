package com.people.qpos;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;
import dspread.voicemodem.util;

/**
 * 更新密钥
 * 
 * @author Fncat
 * 
 */
public class ThreadUpDataKey extends Thread {
	private Handler mHandler;
	private Context mContext;
	private String keyStr;

	public ThreadUpDataKey(Handler mHandler, Context mContext, String keyStr) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.keyStr = keyStr;
	}

	@Override
	public void run() {
		
		//byte[] cmdBytes = util.HexStringToByteArray(this.keyStr);
		//int r = QPOS.getCardReader().doSecurityCommand(cmdBytes);
		int r = QPOS.getCardReader().doSigIn(this.keyStr);
		
		if (r == CardReader.SUCCESS) {
			QPOS.HandData(mHandler, "Update key success", CardReader.SUCCESS);
			
		} else if (r == CardReader.TIMEOUT) {
			QPOS.HandData(mHandler, "签到超时，请重试", CardReader.TIMEOUT);
			
		} else if (r == CardReader.FAILURE) {
			QPOS.HandData(mHandler, "签到失败，请重试", CardReader.FAILURE);
			
		}
	}

}
