package com.people.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.people.R;

public class WECActivity extends BaseActivity implements OnClickListener {


//	private Integer[] imageIds = { R.drawable.set_icon_0, R.drawable.set_icon_1, R.drawable.set_icon_2, R.drawable.set_icon_3, R.drawable.set_icon_problem , R.drawable.set_icon_msg , R.drawable.set_icon_4 };

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wec);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_water = (Button) findViewById(R.id.btn_water);
		btn_water.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_water:
			Intent intent_w = new Intent(WECActivity.this, WaterActivity.class);
			startActivity(intent_w);
			break;
		default:
			break;
		}

	}

	
}