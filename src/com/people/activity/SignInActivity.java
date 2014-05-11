package com.people.activity;

import java.util.HashMap;

import com.people.R;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


// 修改登录密码
public class SignInActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_login_pwd);
		
		Button btn_register = (Button)this.findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_register:
			actionRegister();
			break;

		default:
			break;
		}
		
	}

	private void actionRegister(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199004");
		tempMap.put("PHONENUMBER", "13838387438");
		tempMap.put("MESSAGECODE", "12323");
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.ForgetLoginPwd, tempMap, getRegisterHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在找回请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
				
			}
			
		});	
	}
	
	private LKAsyncHttpResponseHandler getRegisterHandler(){
		 return new LKAsyncHttpResponseHandler(){
			 
			@Override
			public void successAction(Object obj) {
				Log.e("success:", obj.toString());
				
				if (obj instanceof HashMap){
					Log.e("success:", obj.toString());
					if(((HashMap<?, ?>) obj).get("RSPCOD").toString().equals("000000")){
						Toast.makeText(getApplicationContext(), "密码修改成功",
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

}
