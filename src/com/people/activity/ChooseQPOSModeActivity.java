package com.people.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.people.R;
import com.people.qpos.QPOS;

import dspread.voicemodem.CardReader;

public class ChooseQPOSModeActivity extends BaseActivity {

	public static final int FROM_LOGINACTIVITY = 0;
	public static final int FROM_SETTINGACTIVITY = 1;

	private int from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);

		from = getIntent().getIntExtra("FROM", FROM_LOGINACTIVITY);

		Button backBtn = (Button) this.findViewById(R.id.btn_back);
		backBtn.setVisibility(from == FROM_SETTINGACTIVITY ? View.VISIBLE : View.GONE);
	}

	public void chooseMode(View view) {
		if (view.getId() == R.id.choose_bluetooth_btn) {
			Log.e("MODE", "选择蓝牙连接方式...");

			QPOS.changeCardReader(CardReader.BLUETOOTHMODE);

		} else if (view.getId() == R.id.choose_line_btn) {
			Log.e("MODE", "选择数据线连接方式...");

			QPOS.changeCardReader(CardReader.PSKMODE);
		}

		if (from == FROM_LOGINACTIVITY) {
			Intent intent = new Intent(this, CatalogActivity.class);
			this.startActivity(intent);

			this.finish();

		} else if (from == FROM_SETTINGACTIVITY) {
			this.finish();
		}

	}
	
	public void backAction(View view) {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		if (from == FROM_SETTINGACTIVITY) {
			this.finish();
		}
	}

}
