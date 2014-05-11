package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.people.R;

// 流水详情
public class TransferDetailActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_transfer_detail);
		
	}
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		default:
			break;
		}
	}
	
	public void backAction(View view){
		this.finish();
	}

}
