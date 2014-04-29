package com.people.activity;

import java.util.HashMap;

import com.people.R;
import com.people.client.LKAsyncHttpResponseHandler;
import com.people.client.LKHttpRequest;
import com.people.client.LKHttpRequestQueue;
import com.people.client.LKHttpRequestQueueDone;
import com.people.client.TransferRequestTag;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// 注册
public class RegisterActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
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
		tempMap.put("TRANCODE", "199001");
		tempMap.put("PHONENUMBER", "18811068526");
		tempMap.put("PASSWORD", "1234qwer");
		tempMap.put("BUSINESSTYPE", "01");
		tempMap.put("BUSINESSTYPESORT", "211");
		tempMap.put("IDNUMBER", "410521199005065118");
		tempMap.put("BUSINESSADDRESS", "上海市浦东新区张江");
		tempMap.put("TERMINALADDRESS", "上海市浦东新区张江");
		tempMap.put("PROVINCEID", "21");
		tempMap.put("CITYID", "211");
		tempMap.put("AREAID", "2111");
		tempMap.put("RECEIVEBANK", "工商银行");
		tempMap.put("BRANKBRANCH", "上海市浦东新区张江高科工商");
		tempMap.put("ACCOUNTNAME", "周云峰");
		tempMap.put("BANKACCOUNT", "6001001108739018");
		tempMap.put("PACKAGEMAC", "");
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Register, tempMap, getRegisterHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

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
					// 登录成功
					Log.e("success:", obj.toString());
				} else {
				}
				
			}

		};
		}

}
