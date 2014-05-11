package com.people.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
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

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText usernameEdit = null;
	private EditText passwordEdit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		usernameEdit = (EditText) this.findViewById(R.id.et_user);
		usernameEdit.setText(ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		passwordEdit = (EditText) this.findViewById(R.id.et_pwd);
		passwordEdit.setText(ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kPASSWORD, ""));

		Button btn_login = (Button) this.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

		Button btn_forget_pwd = (Button) findViewById(R.id.btn_forget_pwd);
		btn_forget_pwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (checkValue()) {
				login();
			}
			break;

		case R.id.btn_forget_pwd:
			Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private boolean checkValue() {
		if ("".equals(usernameEdit.getText().toString().trim())) {
			Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(passwordEdit.getText().toString().trim())) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	// 登录
	private void login() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199002");
		tempMap.put("PHONENUMBER", usernameEdit.getText().toString().trim());
		tempMap.put("PASSWORD", passwordEdit.getText().toString().trim());
		tempMap.put("PCSIM", "不能获取");

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Login, tempMap, getLoginHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getLoginHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");

				if (RSPCOD.equals("000000")) {
					Editor editor = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).edit();
					editor.putString(Constants.kUSERNAME, PHONENUMBER);
					editor.putString(Constants.kPASSWORD, passwordEdit.getText().toString().trim());
					editor.commit();

					Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
					LoginActivity.this.startActivity(intent);
				} else {
					Toast.makeText(LoginActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

}
