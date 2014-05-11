package com.people.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.people.model.TradeModel;

public class ParseResponseXML {

	private static InputStream inStream = null;

	public static Object parseXML(int reqType, String responseStr) {
		Log.e("response:", responseStr);

		try {
			inStream = new ByteArrayInputStream(responseStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			switch (reqType) {
			case TransferRequestTag.Login: // 登录
				return login();
				
			case TransferRequestTag.Register:
				return register();
			case TransferRequestTag.ModifyLoginPwd:
				return modifyLoginPwd();
				
			case TransferRequestTag.ForgetLoginPwd:
				return forgetLoginPwd();
				
			case TransferRequestTag.SignIn:
				return signIn();
				
			case TransferRequestTag.Consume:
				return consume();
				
			case TransferRequestTag.ConsumeCancel:
				return consumeCancel();
				
			case TransferRequestTag.BalanceQuery:
				return balanceQuery();
				
			case TransferRequestTag.FlowQuery:
				return flowQuery();
				
			case TransferRequestTag.CreditCardApply:
				return creditCardApply();
				
			case TransferRequestTag.ClearQuery:
				return clearQuery();
				
			case TransferRequestTag.AppCommend:
				return appCommend();
				
			case TransferRequestTag.ReferenceMsg:
				return referenceMsg();
				
			case TransferRequestTag.ShareTransfer:
				return shareTransfer();
				
			case TransferRequestTag.ExaminePhone:
				return examinePhone();
				
			case TransferRequestTag.SmsSend:
				return smsSend();
				
			case TransferRequestTag.SmsCheck:
				return smsCheck();
				
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (null != inStream)
					inStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	// 登录
	private static HashMap<String, Object> login() throws XmlPullParserException, IOException {
		HashMap<String, Object> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, Object>();
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("APPTOKEN".equalsIgnoreCase(parser.getName())) {
					respMap.put("APPTOKEN", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object register() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object modifyLoginPwd() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object forgetLoginPwd() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("NEWPASSWD".equalsIgnoreCase(parser.getName())) {
					respMap.put("NEWPASSWD", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object signIn() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("TERMINALNUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("TERMINALNUMBER", parser.nextText());
				} else if ("PSAMCARDNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("PSAMCARDNO", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PINKEY".equalsIgnoreCase(parser.getName())) {
					respMap.put("PINKEY", parser.nextText());
				} else if ("MACKEY".equalsIgnoreCase(parser.getName())) {
					respMap.put("MACKEY", parser.nextText());
				} else if ("ENCRYPTKEY".equalsIgnoreCase(parser.getName())) {
					respMap.put("ENCRYPTKEY", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object consume() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("LOGNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("LOGNO", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object consumeCancel() throws XmlPullParserException, IOException {

		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("LOGNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("LOGNO", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object balanceQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object flowQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					// TODO
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object creditCardApply() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				}
				// TODO
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object clearQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				}
				// TODO
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object appCommend() throws XmlPullParserException, IOException {
		HashMap<String, Object> respMap = null;
		ArrayList<TradeModel> tradeList = null;
		TradeModel trade = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, Object>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					tradeList = new ArrayList<TradeModel>();
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					trade = new TradeModel();
				} else if ("SYSDAT".equalsIgnoreCase(parser.getName())) {
					trade.setSysDate(parser.nextText());
				} else if ("MERNAM".equalsIgnoreCase(parser.getName())) {
					trade.setMerName(parser.nextText());
				} else if ("SYSDAT".equalsIgnoreCase(parser.getName())) {
					trade.setSysDate(parser.nextText());
				} else if ("LOGDAT".equalsIgnoreCase(parser.getName())) {
					trade.setLogDate(parser.nextText());
				} else if ("LOGNO".equalsIgnoreCase(parser.getName())) {
					trade.setLogNo(parser.nextText());
				} else if ("TXNCD".equalsIgnoreCase(parser.getName())) {
					trade.setTxncd(parser.nextText());
				} else if ("TXNSTS".equalsIgnoreCase(parser.getName())) {
					trade.setTxnsts(parser.nextText());
				} else if ("TXNAMT".equalsIgnoreCase(parser.getName())) {
					trade.setAmount(parser.nextText());
				} else if ("CRDNO".equalsIgnoreCase(parser.getName())) {
					trade.setCardNo(parser.nextText());
				}

				break;

			case XmlPullParser.END_TAG:
				if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("TRANDETAILS", tradeList);
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					tradeList.add(trade);
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object referenceMsg() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("REFINFO".equalsIgnoreCase(parser.getName())) {
					respMap.put("REFINFO", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object shareTransfer() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object examinePhone() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object smsSend() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object smsCheck() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} else if ("RSPCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPCOD", parser.nextText());
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

}
