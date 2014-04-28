package com.people.qpos;

import com.people.util.Utils;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;

/**
 * 只输入六位密码
 * 
 * @author Fncat
 * 
 */
public class ThreadOnlySixPass extends Thread {
	private Handler mHandler;
	private Context mContext;
	private CardReader c;
	private byte[] fakekey;

	public ThreadOnlySixPass(Handler mHandler, Context mContext, CardReader c, byte[] fakekey) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
		this.fakekey = fakekey;
	}

	@Override
	public void run() {
		// TCK
		c.setDesKey(fakekey);
		int r = c.doTradeEx("100", 5, "123", "1000", 60); // please slide
		if (r == CardReader.SUCCESS) {// card,
			r = c.waitUser(1, 60);
			if (r == CardReader.TIMEOUT) {
				Utils.HandData(mHandler, "time out", 0);
			} else if (r == CardReader.SUCCESS) {

				String pin = c.getTradeResultCardPwd();
				String ms = c.getTradeResultMacString();
				Utils.HandData(mHandler, "PINBlock encrypted:" + pin + "\nMAC:" + ms, 0);
			} else if (r == CardReader.MACERROR) {

				Utils.HandData(mHandler, "communication MAC ERROR", 0);
			} else if (r == CardReader.USERCANCEL) {

				Utils.HandData(mHandler, "User cancel password", 0);
			} else if (r == CardReader.NOTSUPPORTED) {

				Utils.HandData(mHandler, "Transaction mode does not support", 0);
			} else if (r == CardReader.CMDTIMEOUT) {

				Utils.HandData(mHandler, "Command time out", 0);
			} else if (r == CardReader.UNKNOWNERROR) {
				Utils.HandData(mHandler, "unknow error", 0);
			}

		} else if (r == CardReader.TIMEOUT) {
			Utils.HandData(mHandler, "time out", 0);
		}
	}

}
