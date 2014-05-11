package com.people.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.view.GestureLockView;
import com.people.view.GestureLockView.OnGestureFinishListener;


// 锁屏
public class LockScreenActivity  extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);
		GestureLockView gv = (GestureLockView) findViewById(R.id.gv);
		gv.setKey(ApplicationEnvironment.getInstance().getPreferences().getString(Constants.kLOCKKEY, "")); // Z 字型
		gv.setOnGestureFinishListener(new OnGestureFinishListener() {
			@Override
			public void OnGestureFinish(boolean success) {
				Toast.makeText(LockScreenActivity.this, String.valueOf(success), Toast.LENGTH_SHORT).show();
				if(success){
					LockScreenActivity.this.finish();
					// 启动超时退出服务
					Intent intent = new Intent(BaseActivity.getTopActivity(), TimeoutService.class);
					BaseActivity.getTopActivity().startService(intent);
				}
			}
		});
	}
	
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	   //此处写处理的事件
	   return true;
	  }
	  return super.onKeyDown(keyCode, event);
	 }
}
