package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.people.R;

public class TestActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test);

		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		
		iv_test = (ImageView) findViewById(R.id.iv_test);
		Animation myAnimation= AnimationUtils.loadAnimation(this,R.anim.check_device_anim);
		LinearInterpolator lir = new LinearInterpolator();  
		myAnimation.setInterpolator(lir); 
		iv_test.startAnimation(myAnimation);
		
		TextView tv_test = (TextView) findViewById(R.id.tv_test);
		Animation myAnimation0= AnimationUtils.loadAnimation(this,R.anim.swip_card_anim);
		LinearInterpolator lir0 = new LinearInterpolator();  
		myAnimation0.setInterpolator(lir0); 
		tv_test.startAnimation(myAnimation0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			TestActivity.this.setResult(RESULT_OK);
			this.finish();
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		TestActivity.this.setResult(RESULT_OK);
		this.finish();
	}
	
	

}
