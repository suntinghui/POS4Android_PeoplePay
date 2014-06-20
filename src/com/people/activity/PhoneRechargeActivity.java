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
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.DateUtil;
import com.people.util.StringUtil;
import com.people.view.LKAlertDialog;

// 手机充值
public class PhoneRechargeActivity extends BaseActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_recharge);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_next = (Button) this.findViewById(R.id.btn_confirm);
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			confirmAction();
			
			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	
	

		
//		public Boolean checkValue(){
//			
//			if(et_phone.getText().length() == 0){
//				Toast.makeText(this, "请输入手机号!", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//			
//			if(et_security_code.getText().length() == 0){
//				Toast.makeText(this, "请输入验证码!", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//			return true;
//		}
}
