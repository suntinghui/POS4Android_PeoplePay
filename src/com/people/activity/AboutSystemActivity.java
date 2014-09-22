package com.people.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.people.R;

// 关于系统
public class AboutSystemActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_system);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		Button btn_access = (Button) this.findViewById(R.id.btn_access);
		btn_access.setOnClickListener(this);

		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		PackageManager pm = this.getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			tv_version.setText("众易付 For Android V" + pi.versionName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_access:
			Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.people2000.net/"));
			startActivity(viewIntent);
			break;
		default:
			break;
		}

	}

}
