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
		
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}
	}

}
