package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.people.R;

// 忘记密码
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);

		Button btn_back = (Button)this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:

			break;
		default:
			break;
		}
		
	}
}
