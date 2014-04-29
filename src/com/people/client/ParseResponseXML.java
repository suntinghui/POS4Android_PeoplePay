package com.people.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Xml;

public class ParseResponseXML {

	private static InputStream inStream = null;

	public static Object parseXML(int reqType, String responseStr) {
		// 在项目管理与资金管理的报文中，在内部又有XML的头标签，需要去掉，否则解析出错。是不是觉得很恶心。。。
		responseStr = responseStr.replace("&lt;", "<").replace("&gt;", ">")
				.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
				.replace("<?xml version=\'1.0\' encoding=\'UTF-8\'?>", "");

		Log.e("response:", responseStr);

		try {
			inStream = new ByteArrayInputStream(responseStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			switch (reqType) {
			case TransferRequestTag.Login:
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

	
	private static Object login() throws XmlPullParserException, IOException {
		// 如果程序存在Qry标签，则说明登录成功，返回HASHMAP；如果没有Qry标签，则说明登录失败，则返回String，代表错误码。
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
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

			case XmlPullParser.TEXT:
				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}

	private static Object register() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object modifyLoginPwd() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object forgetLoginPwd() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object signIn() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object consume() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object consumeCancel() throws XmlPullParserException, IOException {
		
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object balanceQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} 
				break;

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object flowQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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
					//TODO
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object creditCardApply() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				}
				//TODO
				break;

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object clearQuery() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, String>();
				} 
				//TODO
				break;

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object appCommend() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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
					//TODO
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object referenceMsg() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object shareTransfer() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object examinePhone() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object smsSend() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
	private static Object smsCheck() throws XmlPullParserException, IOException {
		HashMap<String, String> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
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

			case XmlPullParser.TEXT:

				break;

			case XmlPullParser.END_TAG:
				return respMap;
			}

			eventType = parser.next();
		}

		return null;
	}
	
}
