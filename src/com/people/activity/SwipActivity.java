package com.people.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.people.R;
import com.people.client.LKAsyncHttpResponseHandler;

public class SwipActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swip);

		ImageView iv_swip = (ImageView)this.findViewById(R.id.iv_swip);
		iv_swip.setBackgroundResource(R.anim.swipcard);
		AnimationDrawable animaition = (AnimationDrawable) iv_swip.getBackground();   
		
		animaition.setOneShot(false);   

		if(animaition.isRunning())//是否正在运行？

		{   
			animaition.stop();//停止

		}   
		animaition.start();//启动	

		
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
		default:
			break;
		}
		
	}
}
