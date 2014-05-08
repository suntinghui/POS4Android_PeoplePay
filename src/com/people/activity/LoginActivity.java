package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.LKAsyncHttpResponseHandler;
import com.people.client.LKHttpRequest;
import com.people.client.LKHttpRequestQueue;
import com.people.client.LKHttpRequestQueueDone;
import com.people.client.TransferRequestTag;
import com.people.qpos.ThreadCancel;
import com.people.qpos.ThreadCloseSwip;
import com.people.qpos.ThreadDeviceID;
import com.people.qpos.ThreadOnlySixPass;
import com.people.qpos.ThreadOnlySwip;
import com.people.qpos.ThreadSwip_SixPass;
import com.people.qpos.ThreadUpDataKey;

import dspread.voicemodem.CardReader;
import dspread.voicemodem.DeviceBean;
import dspread.voicemodem.onPOSListener;
import dspread.voicemodem.util;

public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button btn_login = (Button)this.findViewById(R.id.btn_login);
//		Button btn_register = (Button)this.findViewById(R.id.btn_register);
		btn_login.setOnClickListener(this);
//		btn_register.setOnClickListener(this);
		
		
		
	}

	private void login(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199002");
		tempMap.put("PHONENUMBER", "13838387438");
		tempMap.put("PASSWORD", "88888888");
		tempMap.put("PCSIM", "不能获取");
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Login, tempMap, getLoginHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
				
			}
			
		});	

	}

	@SuppressLint("ShowToast")
	private LKAsyncHttpResponseHandler getLoginHandler(){
	 return new LKAsyncHttpResponseHandler(){
		 
		@SuppressWarnings("rawtypes")
		@Override
		public void successAction(Object obj) {
			Log.e("success:", obj.toString());
			
			if (obj instanceof HashMap){
				// 登录成功
				Log.e("success:", obj.toString());
				if(((HashMap) obj).get("RSPCOD").toString().equals("000000")){
					Toast.makeText(getApplicationContext(), "登录成功",
						     Toast.LENGTH_SHORT).show();
				}else if(((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0){
					Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(),
						     Toast.LENGTH_SHORT).show();
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
//			login();
			Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
			startActivity(intent);
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
