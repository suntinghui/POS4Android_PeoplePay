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
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Register, tempMap, getRegisterHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在注册请稍候...", new LKHttpRequestQueueDone(){

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
						Toast.makeText(getApplicationContext(), "注册成功",
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
