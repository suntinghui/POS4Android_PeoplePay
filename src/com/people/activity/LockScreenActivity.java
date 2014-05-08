package com.people.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.people.R;
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
		gv.setKey("0124678"); // Z 字型
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
}
