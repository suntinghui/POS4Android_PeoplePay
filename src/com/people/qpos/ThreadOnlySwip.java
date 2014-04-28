package com.people.qpos;

import com.people.util.Utils;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;

/**
 * 只刷卡
 * 
 * @author Fncat
 * 
 */
public class ThreadOnlySwip extends Thread {
	private Handler mHandler;
	private Context mContext;
	private CardReader c;
	private byte[] fakekey;

	public ThreadOnlySwip(Handler mHandler, Context mContext, CardReader c, byte[] fakekey) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
		this.fakekey = fakekey;
	}

	@Override
	public void run() {
		// TCK
		c.setDesKey(fakekey);
		int r = c.doTradeEx("100", 4, null, "1990050000000001000000010427011209323031303130303030313131", 60); // please
																												// slide
		if (r == CardReader.SUCCESS) {// card,
			r = c.waitUser(1, 60);
			if (r == CardReader.TIMEOUT) {
				Utils.HandData(mHandler, "time out", 0);
			} else if (r == CardReader.SUCCESS) {
				String ci = c.getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String ms = c.getTradeResultMacString();
				Utils.HandData(mHandler, "ISO card\ntrackBlock encrypted:\n" + ci + "\nMAC:" + ms, 0);
			} else if (r == CardReader.SUCCESSJS2) {
				String ci = c.getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String ms = c.getTradeResultMacString();
				Utils.HandData(mHandler, "JS2 card\ntrackBlock encrypted:\n" + ci + "\nMAC:" + ms, 0);
			} else if (r == CardReader.MACERROR) {

				Utils.HandData(mHandler, "communication MAC ERROR", 0);
			} else if (r == CardReader.USERCANCEL) {

				Utils.HandData(mHandler, "swipe card user cancels", 0);
			} else if (r == CardReader.NOTSUPPORTED) {

				Utils.HandData(mHandler, "Transaction mode does not support", 0);
			} else if (r == CardReader.CMDTIMEOUT) {

				Utils.HandData(mHandler, "Command time out", 0);
			} else if (r == CardReader.UNKNOWNERROR) {
				Utils.HandData(mHandler, "unknow error", 0);
			}else {
				Utils.HandData(mHandler, "waitUser奇怪的返回：" + r, 0);
			}
		} else if (r == CardReader.TIMEOUT) {
			Utils.HandData(mHandler, "time out", 0);
		}else {
			Utils.HandData(mHandler, "doTradeEx奇怪的返回：" + r, 0);
		}

	}
}
