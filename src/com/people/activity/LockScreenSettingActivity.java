package com.people.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.view.GestureLockView;
import com.people.view.GestureLockView.OnGestureFinishListener;

// 锁屏 设置
@SuppressLint("ResourceAsColor")
public class LockScreenSettingActivity extends Activity implements
		OnClickListener {
	private TextView tv_tips;
	private GestureLockView gv;
	private int drawCount = 0;
	private String firstKey = "";
	private String secondKey = "";
	private LinearLayout layout_lock;
	private RadioGroup radioGroup = null;
	private RadioButton radioOpen = null;
	private RadioButton radioClose = null;

	private int gender = 1;// 0: 关 1: 开

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen_setting);

		layout_lock = (LinearLayout) findViewById(R.id.layout_lock);

		tv_tips = (TextView) findViewById(R.id.tv_tips);
		tv_tips.setText("请绘制新手势");
		
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioOpen = (RadioButton) this.findViewById(R.id.radioOpen);
		radioClose = (RadioButton) this.findViewById(R.id.radioClose);
		
		Boolean gestureClose = ApplicationEnvironment.getInstance().getPreferences().getBoolean(Constants.kGESTRUECLOSE, false);
		if(gestureClose){
			radioGroup.check(R.id.radioClose);
			layout_lock.setVisibility(View.GONE);
		}else{
			radioGroup.check(R.id.radioOpen);
		}
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) LockScreenSettingActivity.this
						.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if (text.equals("关")) {
					
					gender = 1;
					SharedPreferences pre = ApplicationEnvironment
							.getInstance().getPreferences();
					Editor editor = pre.edit();
					editor.putString(Constants.kLOCKKEY, "");
					editor.putBoolean(Constants.kGESTRUECLOSE, true);
					editor.commit();
					layout_lock.setVisibility(View.GONE);
					ApplicationEnvironment.getInstance().getApplication().stopService(new Intent("com.people.timeoutService"));
				} else {
					gender = 0;
					SharedPreferences pre = ApplicationEnvironment
							.getInstance().getPreferences();
					Editor editor = pre.edit();
					editor.putBoolean(Constants.kGESTRUECLOSE, false);
					editor.commit();
					tv_tips.setText("请绘制新手势");
					tv_tips.setTextColor(LockScreenSettingActivity.this
							.getResources().getColor(R.color.white));
					layout_lock.setVisibility(View.VISIBLE);
				}
			}

		});

		tv_tips = (TextView) findViewById(R.id.tv_tips);
		gv = (GestureLockView) findViewById(R.id.gv);
		gv.isSetting = true;
		gv.setOnGestureFinishListener(new OnGestureFinishListener() {
			@Override
			public void OnGestureFinish(boolean success) {
				if (drawCount++ == 0) {
					firstKey = gv.getCurrentKey();
					tv_tips.setText("请再次绘制新手势");
				} else {
					secondKey = gv.getCurrentKey();
					if (firstKey.equals(secondKey)) {
						// 手势设置成功
						SharedPreferences pre = ApplicationEnvironment
								.getInstance().getPreferences();
						Editor editor = pre.edit();
						editor.putString(Constants.kLOCKKEY, secondKey);
						editor.putBoolean(Constants.kGESTRUECLOSE, false);
						editor.commit();
						gv.setKey(secondKey);
						tv_tips.setText("修改成功");
						tv_tips.setTextColor(LockScreenSettingActivity.this
								.getResources().getColor(R.color.white));

						// 启动超时退出服务
						Intent intent = new Intent(BaseActivity
								.getTopActivity(), TimeoutService.class);
						BaseActivity.getTopActivity().startService(intent);

						LockScreenSettingActivity.this.finish();
					} else {
						// 手势设置失败
						tv_tips.setText("与上一次绘制不一致，请重新绘制");
						tv_tips.setTextColor(LockScreenSettingActivity.this
								.getResources().getColor(R.color.red));
						Animation shakeAnim = AnimationUtils.loadAnimation(
								LockScreenSettingActivity.this, R.anim.shake_x);
						tv_tips.startAnimation(shakeAnim);
					}
				}

			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}