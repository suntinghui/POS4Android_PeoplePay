package com.people.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.people.R;
import com.people.qpos.QPOS;

import dspread.voicemodem.CardReader;

public class ChooseQPOSModeActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);
		
	}
	
	public void chooseMode(View view){
		if (view.getId() == R.id.choose_bluetooth_btn) {
			Log.e("MODE", "选择蓝牙连接方式...");
			
			QPOS.changeCardReader(CardReader.BLUETOOTHMODE);
			
		} else if (view.getId() == R.id.choose_line_btn) {
			Log.e("MODE", "选择数据线连接方式...");
			
			QPOS.changeCardReader(CardReader.PSKMODE);
		}
		
		Intent intent = new Intent(this, CatalogActivity.class);
		this.startActivity(intent);
		
		this.finish();
	}

}
