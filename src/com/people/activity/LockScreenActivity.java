package com.people.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.BitmapUtil;
import com.people.view.CircularImage;
import com.people.view.GestureLockView;
import com.people.view.GestureLockView.OnGestureFinishListener;

// 锁屏
public class LockScreenActivity extends BaseActivity implements OnClickListener {
	private CircularImage ibtn_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);

		Button btn_forget = (Button) findViewById(R.id.btn_forget);
		btn_forget.setOnClickListener(this);

		ibtn_head = (CircularImage) findViewById(R.id.ibtn_head);
		ibtn_head.setOnClickListener(this);

		GestureLockView gv = (GestureLockView) findViewById(R.id.gv);
		gv.setKey(ApplicationEnvironment.getInstance().getPreferences()
				.getString(Constants.kLOCKKEY, "")); // Z 字型
		gv.setOnGestureFinishListener(new OnGestureFinishListener() {
			@Override
			public void OnGestureFinish(boolean success) {
				if (success) {
					LockScreenActivity.this.finish();
					// 启动超时退出服务
					Intent intent = new Intent(BaseActivity.getTopActivity(),
							TimeoutService.class);
					BaseActivity.getTopActivity().startService(intent);
				}
			}
		});
		getDownLoadHead();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 此处写处理的事件
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		this.goLoginActivity();

	}

	public void goLoginActivity() {
		while (!(BaseActivity.getTopActivity() instanceof LoginActivity)) {
			BaseActivity.getTopActivity().finish();
		}
	}

	// 下载图像
	private void getDownLoadHead() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(
				TransferRequestTag.GetDownLoadHead, tempMap,
				getDownLoadHeadHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(null,
				new LKHttpRequestQueueDone() {

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}

	private LKAsyncHttpResponseHandler getDownLoadHeadHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");

				if (RSPCOD.equals("000000")) {

					if ((String) map.get("HEADIMG") != null) {
						ibtn_head.setImageBitmap(BitmapUtil
								.convertStringToBitmap((String) map
										.get("HEADIMG")));
					}
				} else {
					Toast.makeText(LockScreenActivity.this, RSPMSG,
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}
}
