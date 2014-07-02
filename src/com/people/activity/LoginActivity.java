package com.people.activity;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.push.BPushUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView logoImageView = null;
	private EditText usernameEdit = null;
	private EditText passwordEdit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

//		Log.i("test----", getSer());
		logoImageView = (ImageView) this.findViewById(R.id.logoImageView);
		Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.login_logo_anim);
		logoImageView.startAnimation(myAnimation);

		usernameEdit = (EditText) this.findViewById(R.id.et_user);
		usernameEdit.setText(ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
		usernameEdit.setSelection(usernameEdit.getText().toString().length());

		passwordEdit = (EditText) this.findViewById(R.id.et_pwd);
		passwordEdit.setText(ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kPASSWORD, ""));

		Button btn_login = (Button) this.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

		Button btn_forget_pwd = (Button) findViewById(R.id.btn_forget_pwd);
		btn_forget_pwd.setOnClickListener(this);
		
		Button btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);

		startPushService();
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
		case R.id.btn_register:
			register();
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

				passwordEdit.setText("");
			}
		});
	}

	private LKAsyncHttpResponseHandler getLoginHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				Boolean isOpen = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).getBoolean(Constants.kGESTRUECLOSE, false);
				// 启动超时退出服务
				Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
				BaseActivity.getTopActivity().startService(intent);

				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");
//				Constants.APPTOKEN = (String) map.get("APPTOKEN");

				if (RSPCOD.equals("00")) {
					Editor editor = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).edit();
					editor.putString(Constants.kUSERNAME, PHONENUMBER);
					editor.putString(Constants.kPASSWORD, passwordEdit.getText().toString().trim());
					editor.commit();

					Intent intent0 = new Intent(LoginActivity.this, ChooseQPOSModeActivity.class);
					intent.putExtra("FROM", ChooseQPOSModeActivity.FROM_SETTINGACTIVITY);
					LoginActivity.this.startActivity(intent0);
				} else {
					Toast.makeText(LoginActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	private void startPushService() {
		try {
			// 以apikey的方式登录，开启推送。
			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, BPushUtil.getMetaValue(this, "api_key"));
			PushSettings.enableDebugMode(getApplicationContext(), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 注册
	private void register() {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);

	}

	

	// 获取银行支行
	private void getBankBranch() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199034");
		tempMap.put("CITYCOD", "6510");
		tempMap.put("BBANKCOD", "303");
		// BankNo: 303651000870
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.GetBankBranch, tempMap, getBankBranchHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

				passwordEdit.setText("");
			}
		});
	}

	private LKAsyncHttpResponseHandler getBankBranchHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				Boolean isOpen = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).getBoolean(Constants.kGESTRUECLOSE, false);
				// 启动超时退出服务
				Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
				BaseActivity.getTopActivity().startService(intent);

				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");

			}

		};
	}

	// 上传图片
	private void getUpLoadImage() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199021");
		tempMap.put("PHONENUMBER", "18500612529");
		tempMap.put("FILETYPE", "MYPIC"); // MYPIC、IDPIC、IDPIC2、CARDPIC
		tempMap.put("PHOTOS", "MYPIC");
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.UpLoadImage, tempMap, getUpLoadImageHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

				passwordEdit.setText("");
			}
		});
	}

	private LKAsyncHttpResponseHandler getUpLoadImageHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				Boolean isOpen = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).getBoolean(Constants.kGESTRUECLOSE, false);
				// 启动超时退出服务
				Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
				BaseActivity.getTopActivity().startService(intent);

				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");

			}

		};
	}

	// 查看交易小票
	private void checkTicket() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199036");
		tempMap.put("LOGNO", "14000554");
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.CheckTicket, tempMap, getCheckTicketHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

				passwordEdit.setText("");
			}
		});
	}

	private LKAsyncHttpResponseHandler getCheckTicketHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				Boolean isOpen = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).getBoolean(Constants.kGESTRUECLOSE, false);
				// 启动超时退出服务
				Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
				BaseActivity.getTopActivity().startService(intent);

				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");

			}

		};
	}

	// 发送交易小票
	private void sendTicket() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199037");
		tempMap.put("PHONENUMBER", "13945621452");
		tempMap.put("LOGNO", "14000554");
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.SendTicket, tempMap, getSendTicketHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

				passwordEdit.setText("");
			}
		});
	}

	private LKAsyncHttpResponseHandler getSendTicketHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				Boolean isOpen = ApplicationEnvironment.getInstance().getPreferences(LoginActivity.this).getBoolean(Constants.kGESTRUECLOSE, false);
				// 启动超时退出服务
				Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
				BaseActivity.getTopActivity().startService(intent);

				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String PHONENUMBER = (String) map.get("PHONENUMBER");

			}

		};
	}

	private String getSer() {
		String serialnum = "";
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		
		return serialnum;
	}
	
}
