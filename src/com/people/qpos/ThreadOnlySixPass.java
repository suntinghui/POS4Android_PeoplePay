package com.people.qpos;

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
	private String amountStr;

	public ThreadOnlySixPass(Handler mHandler, Context mContext, String amountStr) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.amountStr = amountStr;
	}

	@Override
	public void run() {
		// TCK
		QPOS.getCardReader().setDesKey(QPOS.fakekey);

		int resultCode = QPOS.getCardReader().doTradeEx(this.amountStr, 5, "123", "1000", 60);
		if (resultCode == CardReader.SUCCESS) {
			resultCode = QPOS.getCardReader().waitUser(1, 60);

			if (resultCode == CardReader.SUCCESS) {
				String pin = QPOS.getCardReader().getTradeResultCardPwd();
				String ms = QPOS.getCardReader().getTradeResultMacString();
				QPOS.HandData(mHandler, "PINBlock encrypted:" + pin + "\nMAC:" + ms, CardReader.SUCCESS);

			} else if (resultCode == CardReader.TIMEOUT) {
				QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);

			} else if (resultCode == CardReader.MACERROR) {
				QPOS.HandData(mHandler, "communication MAC ERROR", CardReader.MACERROR);

			} else if (resultCode == CardReader.USERCANCEL) {
				QPOS.HandData(mHandler, "User cancel password", CardReader.USERCANCEL);

			} else if (resultCode == CardReader.NOTSUPPORTED) {
				QPOS.HandData(mHandler, "Transaction mode does not support", CardReader.NOTSUPPORTED);

			} else if (resultCode == CardReader.CMDTIMEOUT) {
				QPOS.HandData(mHandler, "Command time out", CardReader.CMDTIMEOUT);

			} else if (resultCode == CardReader.UNKNOWNERROR) {
				QPOS.HandData(mHandler, "unknow error", CardReader.UNKNOWNERROR);

			}

		} else if (resultCode == CardReader.TIMEOUT) {
			QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);
		}
	}

}
