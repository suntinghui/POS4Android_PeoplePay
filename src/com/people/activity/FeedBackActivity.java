package com.people.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.model.TradeModel;
import com.people.util.DateUtil;
import com.people.util.StringUtil;

// 意见反馈
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_feedback);


		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}


}
