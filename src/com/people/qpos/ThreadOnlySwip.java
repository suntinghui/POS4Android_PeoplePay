package com.people.qpos;

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
	private String amountStr;

	public ThreadOnlySwip(Handler mHandler, Context mContext, String amountStr) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.amountStr = amountStr;
	}

	@Override
	public void run() {
		// TCK
		QPOS.getCardReader().setDesKey(QPOS.fakekey);
		
		int r = QPOS.getCardReader().doTradeEx(this.amountStr, 4, null, "1990050000000001000000010427011209323031303130303030313131", 60); 
		
		if (r == CardReader.SUCCESS) {
			r = QPOS.getCardReader().waitUser(1, 60);
			
			if (r == CardReader.SUCCESS) {
				String ci = QPOS.getCardReader().getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String ms = QPOS.getCardReader().getTradeResultMacString();
				QPOS.HandData(mHandler, "ISO card\ntrackBlock encrypted:\n" + ci + "\nMAC:" + ms, CardReader.SUCCESS);
				
			} else if (r == CardReader.TIMEOUT) {
				QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);
				
			}  else if (r == CardReader.SUCCESSJS2) {
				String ci = QPOS.getCardReader().getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String ms = QPOS.getCardReader().getTradeResultMacString();
				QPOS.HandData(mHandler, "JS2 card\ntrackBlock encrypted:\n" + ci + "\nMAC:" + ms, CardReader.SUCCESSJS2);
				
			} else if (r == CardReader.MACERROR) {
				QPOS.HandData(mHandler, "communication MAC ERROR", CardReader.MACERROR);
				
			} else if (r == CardReader.USERCANCEL) {
				QPOS.HandData(mHandler, "swipe card user cancels", CardReader.USERCANCEL);
				
			} else if (r == CardReader.NOTSUPPORTED) {
				QPOS.HandData(mHandler, "Transaction mode does not support", CardReader.NOTSUPPORTED);
				
			} else if (r == CardReader.CMDTIMEOUT) {
				QPOS.HandData(mHandler, "Command time out", CardReader.CMDTIMEOUT);
				
			} else if (r == CardReader.UNKNOWNERROR) {
				QPOS.HandData(mHandler, "unknow error", CardReader.UNKNOWNERROR);
				
			}else {
				QPOS.HandData(mHandler, "waitUser奇怪的返回：" + r, -1);
				
			}
		} else if (r == CardReader.TIMEOUT) {
			QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);
		}else {
			QPOS.HandData(mHandler, "doTradeEx奇怪的返回：" + r, -1);
		}

	}
}
