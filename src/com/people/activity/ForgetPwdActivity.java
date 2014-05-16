package com.people.activity;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.view.LKAlertDialog;

// 忘记密码
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	EditText et_phone ;
	EditText et_security_code ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_next = (Button) this.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		Button btn_securitycode = (Button) findViewById(R.id.btn_securitycode);
		btn_securitycode.setOnClickListener(this);
		et_phone = (EditText) findViewById(R.id.et_account);
		et_security_code = (EditText) findViewById(R.id.et_securitycode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			if(checkValue()){
				nextAction();	
			}
			
			break;
		case R.id.btn_securitycode:
			if(et_phone.getText().toString().length() == 0){
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			}else if(et_phone.getText().toString().length() != 11){
				Toast.makeText(this, "手机号不合法", Toast.LENGTH_SHORT).show();
			}else{
				sendSMS();	
			}
			
			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	// 发送短信
	private void sendSMS() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199018");
		tempMap.put("PHONENUMBER", et_phone.getText().toString().trim());
		tempMap.put("TOPHONENUMBER ", et_phone.getText().toString().trim());
		tempMap.put("TYPE", "100002"); // 100001－注册 100002－忘记密码

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.SmsSend,
				tempMap, sendSMSHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取短信验证码...",
				new LKHttpRequestQueueDone() {

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
					if (((HashMap) obj).get("RSPCOD").toString()
							.equals("000000")) {
						Toast.makeText(getApplicationContext(), "短信发送成功，请注意查收！",
								Toast.LENGTH_SHORT).show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null
							&& ((HashMap) obj).get("RSPMSG").toString()
									.length() != 0) {
						Toast.makeText(getApplicationContext(),
								((HashMap) obj).get("RSPMSG").toString(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	// 忘记密码
		private void nextAction() {
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", "199004");
			tempMap.put("MESSAGECODE", et_security_code.getText().toString().trim());
			tempMap.put("PHONENUMBER ", et_phone.getText().toString().trim());

			LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.ForgetLoginPwd,
					tempMap, forgetPwdHandler());

			new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...",
					new LKHttpRequestQueueDone() {

						@Override
						public void onComplete() {
							super.onComplete();

						}

					});
		}

		private LKAsyncHttpResponseHandler forgetPwdHandler() {
			return new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("rawtypes")
				@Override
				public void successAction(Object obj) {
					if (obj instanceof HashMap) {
						if (((HashMap) obj).get("RSPCOD").toString()
								.equals("00000")) {
							LKAlertDialog dialog = new LKAlertDialog(ForgetPwdActivity.this);
							dialog.setTitle("提示");
							dialog.setMessage("请牢记密码："+((HashMap) obj).get("NEWPASSWD").toString());
							dialog.setCancelable(false);
							dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int arg1) {
									dialog.dismiss();
								}
							});
							dialog.create().show();
						} else if (((HashMap) obj).get("RSPMSG").toString() != null
								&& ((HashMap) obj).get("RSPMSG").toString()
										.length() != 0) {
							Toast.makeText(getApplicationContext(),
									((HashMap) obj).get("RSPMSG").toString(),
									Toast.LENGTH_SHORT).show();
						}
					} else {
					}

				}

			};
		}
		
		public Boolean checkValue(){
			
			if(et_phone.getText().length() == 0){
				Toast.makeText(this, "请输入手机号!", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if(et_security_code.getText().length() == 0){
				Toast.makeText(this, "请输入验证码!", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
}
