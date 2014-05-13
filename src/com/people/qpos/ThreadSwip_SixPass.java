package com.people.qpos;

import java.util.HashMap;

import com.people.util.StringUtil;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import dspread.voicemodem.CardReader;

/**
 * 刷卡+6位密码
 * 
 * @author Fncat
 * 
 */
public class ThreadSwip_SixPass extends Thread {
	private Handler mHandler;
	private Context mContext;
	private String amountStr;
	private String extraStr;

	public ThreadSwip_SixPass(Handler mHandler, Context mContext, String amountStr, String extraStr) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.amountStr = amountStr;
		this.extraStr = extraStr;
	}

	@Override
	public void run() {
		QPOS.getCardReader().setDesKey(QPOS.fakekey);
		
		int resultCode = QPOS.getCardReader().doTradeEx(this.amountStr, 1, null, this.extraStr, 60); 
		
		if (resultCode == CardReader.SUCCESS) {
			resultCode = QPOS.getCardReader().waitUser(1, 120);
			
			if (resultCode == CardReader.SUCCESS) {
				String ci = QPOS.getCardReader().getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String pin = QPOS.getCardReader().getTradeResultCardPwd();
				String macTemp = QPOS.getCardReader().getTradeResultMacString();
				String mac = StringUtil.hexToASCII(macTemp);
				// 磁道信息前两位表示长度，从长度后开始截取。
				String cardInfoTemp = QPOS.getCardReader().getTradeResultCardInfo();
				String cardInfo = formatCardInfo(cardInfoTemp);
				String psam = QPOS.getCardReader().getTradeResultPsamID();
				
				Log.e("===", "pin:"+pin);
				Log.e("===", "mac:"+macTemp);
				Log.e("===", "cardInfo:"+cardInfoTemp);
				Log.e("===", "psam:"+psam);
				
				HashMap <String, String> map = new HashMap<String, String>();
				map.put("PIN", pin);
				map.put("MAC", mac);
				map.put("CARD", cardInfo);
				map.put("PSAM", psam);
				
				
				QPOS.HandData(mHandler, map, CardReader.SUCCESS);
				
			} else if (resultCode == CardReader.TIMEOUT) {
				QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);
				
			}  else if (resultCode == CardReader.SUCCESSJS2) {
				String ci = QPOS.getCardReader().getTradeResultCardInfo();
				if (ci.equals(String.valueOf(CardReader.UNKNOWNERROR))) {
					ci = "data is UNKNOWNERROR";
				}
				String pin = QPOS.getCardReader().getTradeResultCardPwd();
				String ms = QPOS.getCardReader().getTradeResultMacString();
				QPOS.HandData(mHandler, "JS2 card\ntrackBlock encrypted:\n" + ci + "\nPINBlock encrypted:" + pin + "\nMAC:" + ms, CardReader.SUCCESSJS2);
				
			} else if (resultCode == CardReader.MACERROR) {
				QPOS.HandData(mHandler, "communication MAC ERROR", CardReader.MACERROR);
				
			} else if (resultCode == CardReader.USERCANCEL) {
				QPOS.HandData(mHandler, "Users to cancel the transaction", CardReader.USERCANCEL);
				
			} else if (resultCode == CardReader.NOTSUPPORTED) {
				QPOS.HandData(mHandler, "Transaction mode does not support", CardReader.NOTSUPPORTED);
				
			} else if (resultCode == CardReader.CMDTIMEOUT) {
				QPOS.HandData(mHandler, "Command time out", CardReader.CMDTIMEOUT);
				
			} else if (resultCode == CardReader.UNKNOWNERROR) {
				QPOS.HandData(mHandler, "unknow error", CardReader.UNKNOWNERROR);
				
			} else {
				QPOS.HandData(mHandler, "waitUser奇怪的返回：" + resultCode, -1);
				
			}

		} else if (resultCode == CardReader.TIMEOUT) {
			QPOS.HandData(mHandler, "time out", CardReader.TIMEOUT);
			
		} else {
			QPOS.HandData(mHandler, "doTradeEx奇怪的返回：" + resultCode, -1);
		}

	}
	
	private String formatCardInfo(String cardInfo){
		int len = Integer.parseInt(cardInfo.substring(0, 2), 16);
		String temp = cardInfo.substring(2, len*2+2);
		return temp;
	}
}
