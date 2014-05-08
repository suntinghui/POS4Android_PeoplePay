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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


// 商户
public class MerchantActivity extends BaseActivity implements OnClickListener{
	private LinearLayout layout_msg_blow ;
	private Boolean isClicked = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);
		
		RelativeLayout layout_msg_top = (RelativeLayout) findViewById(R.id.layout_msg_top);
		layout_msg_top.setOnClickListener(this);
		layout_msg_blow = (LinearLayout) findViewById(R.id.layout_msg_blow);
		
		
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_msg_top:
			isClicked = !isClicked;
			if (isClicked) {
				layout_msg_blow.setVisibility(View.VISIBLE);
			}else{
				layout_msg_blow.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		
	}

	private void actionRegister(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199003");
		tempMap.put("PHONENUMBER", "13838387438");
		tempMap.put("PASSWORD", "1234qwer");
		tempMap.put("PASSWORDNEW", "Asdf1234");
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.ModifyLoginPwd, tempMap, getRegisterHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在修改请稍候...", new LKHttpRequestQueueDone(){

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
