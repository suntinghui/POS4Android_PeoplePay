package com.people.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.people.model.Bank;
import com.people.model.CashModel;
import com.people.model.CityModel;
import com.people.model.Province;
import com.people.model.RateModel;
import com.people.model.TradeModel;
import com.people.util.StringUtil;

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

			case TransferRequestTag.RateType:
				return rateType();

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

			case TransferRequestTag.CheckTermId:
				return termIdCheck();

			case TransferRequestTag.UploadMsgTwo:
				return uploadMsgTwo();

			case TransferRequestTag.UpdateVersion:
				return updateVersion();

			case TransferRequestTag.MerchantQuery:
				return merchantQuery();

			case TransferRequestTag.LoadUpHead:
				return loadUpHead();

			case TransferRequestTag.GetDownLoadHead:
				return downLoadHead();

			case TransferRequestTag.LoadUpStreetImg:
				return loadUpStreetImg();

			case TransferRequestTag.CashCharge:
				return cashCharge();

			case TransferRequestTag.GetCashCharge:
				return getCashChargeList();

			case TransferRequestTag.CashDelete:
				return cashDelete();

			case TransferRequestTag.GetProvinceName:
				return getProvinceName();

			case TransferRequestTag.GetCityName:
				return getCityName();

			case TransferRequestTag.GetBank:
				return getBank();

			case TransferRequestTag.GetBankBranch:
				return getBranchBank();

			case TransferRequestTag.UpLoadImage:
				return upLoadImages(responseStr);

			case TransferRequestTag.UpLoadImage2:
				return upLoadImages2(responseStr);

			case TransferRequestTag.Authentication:
				return authentication();

			case TransferRequestTag.Authentication2:
				return authentication();

			case TransferRequestTag.CardCard:
				return cardCardAction();

			case TransferRequestTag.CreditCard:
				return creditCardAction();

			case TransferRequestTag.PhoneRecharge:
				return phoneRechargeAction();

			case TransferRequestTag.MyAccount:
				return myAccountAction();

			case TransferRequestTag.GetMsg:
				return getMsgAction();

			case TransferRequestTag.DrawMoney:
				return drawMoneyAction();

			case TransferRequestTag.SendTicket:
				return sendTicket();

			case TransferRequestTag.CheckTicket:
				return checkTicketAction();

			case TransferRequestTag.UploadSignImage:
				return upLoadSignImageAction();

			case TransferRequestTag.Login_adr:
				return login_adr();

			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (JSONException e) {
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
	private static HashMap<String, Object> login()
			throws XmlPullParserException, IOException {
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

	private static Object authentication() throws XmlPullParserException,
			IOException {
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

	private static Object getProvinceName() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<Province> list = new ArrayList<Province>();
		Province model = null;
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
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new Province();
				} else if ("AREACOD".equalsIgnoreCase(parser.getName())) {
					model.setCode(Integer.valueOf(parser.nextText()));
				} else if ("AREANAM".equalsIgnoreCase(parser.getName())) {
					model.setName(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object getCityName() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<CityModel> list = new ArrayList<CityModel>();
		CityModel model = null;
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
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new CityModel();
				} else if ("AREACOD".equalsIgnoreCase(parser.getName())) {
					model.setCode(Integer.valueOf(parser.nextText()));
				} else if ("AREANAM".equalsIgnoreCase(parser.getName())) {
					model.setName(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object getBank() throws XmlPullParserException, IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<Bank> list = new ArrayList<Bank>();
		Bank model = null;
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
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new Bank();
				} else if ("BANKCOD".equalsIgnoreCase(parser.getName())) {
					model.setCode(Integer.valueOf(parser.nextText()));
				} else if ("BANKNAM".equalsIgnoreCase(parser.getName())) {
					model.setName(parser.nextText());
				} else if ("SHOWBANKNAME".equalsIgnoreCase(parser.getName())) {
					model.setShowBankName(parser.nextText());
				} else if ("BANKNO".equalsIgnoreCase(parser.getName())) {
					model.setBankNo(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object getBranchBank() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<Bank> list = new ArrayList<Bank>();
		Bank model = null;
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
				} else if ("RSPMSG".equalsIgnoreCase(parser.getName())) {
					respMap.put("RSPMSG", parser.nextText());
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new Bank();
				} else if ("BANKCOD".equalsIgnoreCase(parser.getName())) {
					model.setCode(Integer.valueOf(parser.nextText()));
				} else if ("BANKNAM".equalsIgnoreCase(parser.getName())) {
					model.setName(parser.nextText());
				} else if ("BANKNO".equalsIgnoreCase(parser.getName())) {
					model.setName(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object upLoadImages(String reponseStr) throws JSONException {
		HashMap<String, String> respMap = StringUtil
				.JSONObject2Map(new JSONObject(reponseStr));

		return respMap;
	}

	private static Object upLoadImages2(String reponseStr)
			throws XmlPullParserException, IOException {
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
				}
				break;
			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object modifyLoginPwd() throws XmlPullParserException,
			IOException {
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

	private static Object forgetLoginPwd() throws XmlPullParserException,
			IOException {
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

	private static Object creditCardAction() throws XmlPullParserException,
			IOException {
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

	private static Object phoneRechargeAction() throws XmlPullParserException,
			IOException {
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

	private static Object drawMoneyAction() throws XmlPullParserException,
			IOException {
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

	private static Object upLoadSignImageAction()
			throws XmlPullParserException, IOException {
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

	private static Object checkTicketAction() throws XmlPullParserException,
			IOException {
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

	private static Object cardCardAction() throws XmlPullParserException,
			IOException {
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

	private static Object myAccountAction() throws XmlPullParserException,
			IOException {
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
				} else if ("CASHBAL".equalsIgnoreCase(parser.getName())) {
					respMap.put("CASHBAL", parser.nextText());
				} else if ("CASHACBAL".equalsIgnoreCase(parser.getName())) {
					respMap.put("CASHACBAL", parser.nextText());
				} else if ("ACTNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACTNO", parser.nextText());
				} else if ("ACSTATUS".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACSTATUS", parser.nextText());
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

	private static Object getMsgAction() throws XmlPullParserException,
			IOException {
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
				} else if ("MERCNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("MERCNAM", parser.nextText());
				} else if ("ADDRESS".equalsIgnoreCase(parser.getName())) {
					respMap.put("ADDRESS", parser.nextText());
				} else if ("POSADDRESS".equalsIgnoreCase(parser.getName())) {
					respMap.put("POSADDRESS", parser.nextText());
				} else if ("ACTNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACTNAM", parser.nextText());
				} else if ("BANKCODE".equalsIgnoreCase(parser.getName())) {
					respMap.put("BANKCODE", parser.nextText());
				} else if ("OPNBNK".equalsIgnoreCase(parser.getName())) {
					respMap.put("OPNBNK", parser.nextText());
				} else if ("OPNBNKNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("OPNBNKNAM", parser.nextText());
				} else if ("ACTNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACTNO", parser.nextText());
				} else if ("BUSNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("BUSNAM", parser.nextText());
				} else if ("AREA".equalsIgnoreCase(parser.getName())) {
					respMap.put("AREA", parser.nextText());
				} else if ("PROCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("PROCOD", parser.nextText());
				} else if ("BANKAREA".equalsIgnoreCase(parser.getName())) {
					respMap.put("BANKAREA", parser.nextText());
				} else if ("PRONAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("PRONAM", parser.nextText());
				} else if ("AREANAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("AREANAM", parser.nextText());
				} else if ("CORPORATEIDENTITY".equalsIgnoreCase(parser
						.getName())) {
					respMap.put("CORPORATEIDENTITY", parser.nextText());
				} else if ("SCOBUS".equalsIgnoreCase(parser.getName())) {
					respMap.put("SCOBUS", parser.nextText());
				} else if ("BIGBANKCOD".equalsIgnoreCase(parser.getName())) {
					respMap.put("BIGBANKCOD", parser.nextText());
				} else if ("BIGBANKNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("BIGBANKNAM", parser.nextText());
				} else if ("STATUS".equalsIgnoreCase(parser.getName())) {
					respMap.put("STATUS", parser.nextText());
				} else if ("TERMNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("TERMNO", parser.nextText());
				} else if ("SHOWBANKNAME".equalsIgnoreCase(parser.getName())) {
					respMap.put("SHOWBANKNAME", parser.nextText());
				} else if ("BANKNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("BANKNO", parser.nextText());
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

	private static Object consumeCancel() throws XmlPullParserException,
			IOException {

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

	private static Object balanceQuery() throws XmlPullParserException,
			IOException {
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

	private static Object flowQuery() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<TradeModel> list = new ArrayList<TradeModel>();
		TradeModel model = null;
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
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new TradeModel();
				} else if ("SYSDAT".equalsIgnoreCase(parser.getName())) {
					model.setSysDate(parser.nextText());
				} else if ("MERNAM".equalsIgnoreCase(parser.getName())) {
					model.setMerName(parser.nextText());
				} else if ("LOGDAT".equalsIgnoreCase(parser.getName())) {
					model.setLogDate(parser.nextText());
				} else if ("LOGNO".equalsIgnoreCase(parser.getName())) {
					model.setLogNo(parser.nextText());
				} else if ("TXNCD".equalsIgnoreCase(parser.getName())) {
					model.setTxncd(parser.nextText());
				} else if ("TXNSTS".equalsIgnoreCase(parser.getName())) {
					model.setTxnsts(parser.nextText());
				} else if ("TXNAMT".equalsIgnoreCase(parser.getName())) {
					model.setTxnamt(parser.nextText());
				} else if ("CRDNO".equalsIgnoreCase(parser.getName())) {
					model.setCardNo(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(0, model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}
		return respMap;
	}

	private static Object rateType() throws XmlPullParserException, IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<RateModel> list = new ArrayList<RateModel>();
		RateModel model = null;
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
				} else if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					model = new RateModel();
				} else if ("IDFCHANNEL".equalsIgnoreCase(parser.getName())) {
					model.setIDFCHANNEL(parser.nextText());
				} else if ("IDFID".equalsIgnoreCase(parser.getName())) {
					model.setIDFID(parser.nextText());
				} else if ("DFEERAT".equalsIgnoreCase(parser.getName())) {
					model.setDFEERAT(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("TRANDETAIL".equalsIgnoreCase(parser.getName())) {
					list.add(0, model); // 服务器返回顺序不对，这里进行倒序
				} else if ("TRANDETAILS".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}
		return respMap;
	}

	private static Object getCashChargeList() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		ArrayList<CashModel> list = new ArrayList<CashModel>();
		CashModel model = null;
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
				} else if ("TOTALPAGE".equalsIgnoreCase(parser.getName())) {
					respMap.put("TOTALPAGE", parser.nextText());
				} else if ("TOTALTRANSAMT".equalsIgnoreCase(parser.getName())) {
					respMap.put("TOTALTRANSAMT", parser.nextText());
				} else if ("CURRENTROWNUMS".equalsIgnoreCase(parser.getName())) {
					respMap.put("CURRENTROWNUMS", parser.nextText());
				} else if ("TOTALROWNUMS".equalsIgnoreCase(parser.getName())) {
					respMap.put("TOTALROWNUMS", parser.nextText());
				} else if ("ROW".equalsIgnoreCase(parser.getName())) {
					model = new CashModel();
				} else if ("TRANSDATE".equalsIgnoreCase(parser.getName())) {
					model.setDate(parser.nextText());
				} else if ("TRANSTIME".equalsIgnoreCase(parser.getName())) {
					model.setTime(parser.nextText());
				} else if ("TRANSTYPE".equalsIgnoreCase(parser.getName())) {
					model.setType(parser.nextText());
				} else if ("TRANSAMT".equalsIgnoreCase(parser.getName())) {
					model.setAmount(parser.nextText());
				} else if ("TRANSID".equalsIgnoreCase(parser.getName())) {
					model.setTransId(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("ROW".equalsIgnoreCase(parser.getName())) {
					list.add(model); // 服务器返回顺序不对，这里进行倒序
				} else if ("LIST".equalsIgnoreCase(parser.getName())) {
					respMap.put("list", list);
				}
				break;
			}

			eventType = parser.next();
		}
		return respMap;
	}

	private static Object creditCardApply() throws XmlPullParserException,
			IOException {
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

	private static Object appCommend() throws XmlPullParserException,
			IOException {
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

	private static Object clearQuery() throws XmlPullParserException,
			IOException {
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

	private static Object referenceMsg() throws XmlPullParserException,
			IOException {
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

	private static Object shareTransfer() throws XmlPullParserException,
			IOException {
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

	private static Object examinePhone() throws XmlPullParserException,
			IOException {
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

	private static Object sendTicket() throws XmlPullParserException,
			IOException {
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

	private static Object loadUpHead() throws XmlPullParserException,
			IOException {
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

	private static Object loadUpStreetImg() throws XmlPullParserException,
			IOException {
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

	private static Object downLoadHead() throws XmlPullParserException,
			IOException {
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
				} else if ("HEADIMG".equalsIgnoreCase(parser.getName())) {
					respMap.put("HEADIMG", parser.nextText());
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

	private static Object cashCharge() throws XmlPullParserException,
			IOException {
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
				} else if ("PACKAGEMAC".equalsIgnoreCase(parser.getName())) {
					respMap.put("PACKAGEMAC", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object cashDelete() throws XmlPullParserException,
			IOException {
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

	private static Object termIdCheck() throws XmlPullParserException,
			IOException {
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

	private static Object uploadMsgTwo() throws XmlPullParserException,
			IOException {
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

	private static Object updateVersion() throws XmlPullParserException,
			IOException {
		HashMap<String, Object> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("root".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, Object>();
				}

				if ("version".equals(parser.getName())) {
					respMap.put("version", Integer.parseInt(parser.nextText()));

				} else if ("url".equals(parser.getName())) {
					respMap.put("url", parser.nextText());
				} else if ("des".equals(parser.getName())) {
					respMap.put("des", parser.nextText());

				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	private static Object merchantQuery() throws XmlPullParserException,
			IOException {
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
				} else if ("PHONENUMBER".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUMBER", parser.nextText());
				} else if ("STATUS".equalsIgnoreCase(parser.getName())) {
					respMap.put("STATUS", parser.nextText());
				} else if ("MERNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("MERNAM", parser.nextText());
				} else if ("ACTNO".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACTNO", parser.nextText());
				} else if ("ACTNAM".equalsIgnoreCase(parser.getName())) {
					respMap.put("ACTNAM", parser.nextText());
				} else if ("OPNBNK".equalsIgnoreCase(parser.getName())) {
					respMap.put("OPNBNK", parser.nextText());
				}
				break;

			}

			eventType = parser.next();
		}

		return respMap;
	}

	// 登录
	private static HashMap<String, Object> login_adr()
			throws XmlPullParserException, IOException {
		HashMap<String, Object> respMap = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("EPOSPROTOCOL".equalsIgnoreCase(parser.getName())) {
					respMap = new HashMap<String, Object>();
				} else if ("PHONENUM".equalsIgnoreCase(parser.getName())) {
					respMap.put("PHONENUM", parser.nextText());
				} else if ("LATITUDE".equalsIgnoreCase(parser.getName())) {
					respMap.put("LATITUDE", parser.nextText());
				} else if ("LONGITUDE".equalsIgnoreCase(parser.getName())) {
					respMap.put("LONGITUDE", parser.nextText());
				} else if ("LOGIN_ADR".equalsIgnoreCase(parser.getName())) {
					respMap.put("LOGIN_ADR", parser.nextText());
				}
				break;
			}
			eventType = parser.next();
		}

		return respMap;
	}

}
