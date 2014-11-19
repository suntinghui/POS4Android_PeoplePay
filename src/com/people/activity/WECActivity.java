package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.people.R;

public class WECActivity extends BaseActivity implements OnClickListener {


//	private Integer[] imageIds = { R.drawable.set_icon_0, R.drawable.set_icon_1, R.drawable.set_icon_2, R.drawable.set_icon_3, R.drawable.set_icon_problem , R.drawable.set_icon_msg , R.drawable.set_icon_4 };

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wec);

		
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