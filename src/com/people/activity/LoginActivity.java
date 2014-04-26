package com.people.activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.people.R;
import com.people.client.Constants;
import com.people.client.LKAsyncHttpResponseHandler;
import com.people.client.LKHttpRequest;
import com.people.client.LKHttpRequestQueue;
import com.people.client.LKHttpRequestQueueDone;
import com.people.client.TransferRequestTag;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		
		login();
		return true;
	}

	private void login(){
		
	
	HashMap<String, Object> map = new HashMap<String, Object>();
	map.put(Constants.kMETHODNAME, TransferRequestTag.Login);
	
	HashMap<String, Object> tempMap = new HashMap<String, Object>();
	tempMap.put("TRANCODE", "199002");
	tempMap.put("PHONENUMBER", "15900715775");
	tempMap.put("PASSWORD", "ASdf1234");
	tempMap.put("PCSIM", "PCSIM");
	tempMap.put("PACKAGEMAC", "DAC120F8A425F9E4B42A2E24BCE7414A");
	
	map.put(Constants.kPARAMNAME, tempMap);
	
	LKHttpRequest req1 = new LKHttpRequest(map, getLoginHandler());
	
	new LKHttpRequestQueue().addHttpRequest(req1)
	.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

		@Override
		public void onComplete() {
			super.onComplete();
			
		}
		
	});	

}

private LKAsyncHttpResponseHandler getLoginHandler(){
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
