package com.people.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.people.R;
import com.people.qpos.ThreadSwip_SixPass;
import com.people.view.BLDeviceDialog;
import com.people.view.BLDeviceDialog.OnSelectBLListener;

public class SearchAndSwipeActivity extends BaseActivity {
	
	private TextView titleView = null;
	private ImageView animImageView = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_swipe);
		
		titleView = (TextView) this.findViewById(R.id.titleView);
		animImageView = (ImageView) this.findViewById(R.id.iv_swipe);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		BLDeviceDialog dialog = new BLDeviceDialog(this);
		dialog.setOnSelectBLListener(selectBLListener);
		dialog.create().show();
	}
	
	private OnSelectBLListener selectBLListener = new OnSelectBLListener() {
		@Override
		public void onSelect() {
			new ThreadSwip_SixPass(mHandler, SearchAndSwipeActivity.this, "1","").start();;
		}
	};
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				Log.e("---", "" + msg.obj);
				break;
			}
		}
	};



	public void backAction(View view){
		this.finish();
	}

}
