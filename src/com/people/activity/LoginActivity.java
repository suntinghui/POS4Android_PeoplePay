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
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;

public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button btn_login = (Button) this.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		Button btn_forget_pwd = (Button) findViewById(R.id.btn_forget_pwd);
		btn_forget_pwd.setOnClickListener(this);
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
						Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
						LoginActivity.this.startActivity(intent);
						
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			break;
			
		case R.id.btn_forget_pwd:
			Intent intent1 = new Intent(LoginActivity.this, ForgetPwdActivity.class);
			startActivity(intent1);
			break;
			
		default:
			break;
		}

	}
}
