package com.people.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.people.R;
import com.people.client.LKAsyncHttpResponseHandler;
import com.people.client.LKHttpRequest;
import com.people.client.LKHttpRequestQueue;
import com.people.client.LKHttpRequestQueueDone;
import com.people.client.TransferRequestTag;

public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button btn_login = (Button) this.findViewById(R.id.btn_login);
		// Button btn_register = (Button)this.findViewById(R.id.btn_register);
		btn_login.setOnClickListener(this);
		// btn_register.setOnClickListener(this);

	}

	// 登录
	private void login() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199002");
		tempMap.put("PHONENUMBER", "18811068526");
		tempMap.put("PASSWORD", "1234qwer");
		tempMap.put("PCSIM", "不能获取");

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Login, tempMap, getLoginHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});

	}

	@SuppressLint("ShowToast")
	private LKAsyncHttpResponseHandler getLoginHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				Log.e("success:", obj.toString());

				if (obj instanceof HashMap) {
					// 登录成功
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
						Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	// 修改登录密码
	private void modifyLoginPwd(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199003");
		tempMap.put("PHONENUMBER", "18811068526");
		tempMap.put("PASSWORD", "88888888");
		tempMap.put("PASSWORDNEW", "123456");

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.ModifyLoginPwd, tempMap, getModifyLoginPwdHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	private LKAsyncHttpResponseHandler getModifyLoginPwdHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				Log.e("success:", obj.toString());

				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
						Toast.makeText(getApplicationContext(), "登录密码修改成功", Toast.LENGTH_SHORT).show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	// 签到
	private void signin(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199020");
		tempMap.put("PHONENUMBER", "18811068526");
		tempMap.put("TERMINALNUMBER", "00000100000200000046");// 终端号（设备编号）
		tempMap.put("PSAMCARDNO", "UN201410000046"); // PSAM卡号
		tempMap.put("TERMINALSERIANO", "000002"); // 终端流水号

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.SignIn, tempMap, getModifyLoginPwdHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	// 发送短信
	private void sendSMS(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199018");
		tempMap.put("PHONENUMBER", "18811068526");
		tempMap.put("TOPHONENUMBER ", "18500972879");
		tempMap.put("TYPE", "100002"); // 100001－注册  100002－忘记密码

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.SmsSend, tempMap, sendSMSHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	private LKAsyncHttpResponseHandler sendSMSHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
						Toast.makeText(getApplicationContext(), "短信发送成功", Toast.LENGTH_SHORT).show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	// 交易
	private void transfer(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199005");
		tempMap.put("PHONENUMBER", "18811068526"); // 手机号
		tempMap.put("TERMINALNUMBER", "00000100000200000046"); // 终端号
		tempMap.put("PCSIM", "获取不到");
		tempMap.put("TRACK", "20F7459D3FBA79BA1940D1A0A9A4011AEFE97D4DEB4244E806E97D4DEB4244E80611930B680F404FEE554E201410000046000001000002000000464541444133384441");
		tempMap.put("TSEQNO", "000009"); // 终端流水号
		tempMap.put("CTXNAT", "000000000100"); // 消费金额
		tempMap.put("TPINBLK", "11930B680F404FEE554E2014"); // 支付密码???
		tempMap.put("CRDNO", "");  // 卡号
		tempMap.put("CHECKX", "0.0");  // 横坐标
		tempMap.put("APPTOKEN", "apptoken");
		tempMap.put("TTXNTM", "143030");  // 交易时间
		tempMap.put("TTXNDT", "0509"); // 交易日期
		tempMap.put("PSAMCARDNO", "UN123451234582"); // PSAM卡号 ???
		tempMap.put("MAC", "5A80F34A"); // MAC ???


		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Consume, tempMap, transferHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	
	private LKAsyncHttpResponseHandler transferHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {

				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
						Toast.makeText(getApplicationContext(), "交易成功", Toast.LENGTH_SHORT).show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	// 取得商户信息
	
	
	// 查询交易明细
	private void queryHistory(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199008");
		tempMap.put("PHONENUMBER", "18811068526");

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.FlowQuery, tempMap, sendSMSHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	// 清算
	private void queryClear(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199009");
		tempMap.put("PHONENUMBER", "18811068526");

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.ClearQuery, tempMap, sendSMSHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			
			
			
			
			// Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
			// startActivity(intent);
			break;
		case R.id.btn_register:
			Intent intent1 = new Intent(LoginActivity.this, ForgetLoginPwdActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}

	}
}
