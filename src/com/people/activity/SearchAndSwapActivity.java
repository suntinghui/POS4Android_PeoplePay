package com.people.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.people.R;

public class SearchAndSwapActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swip);

		ImageView iv_swip = (ImageView) this.findViewById(R.id.iv_swip);
		iv_swip.setBackgroundResource(R.anim.swipcard);
		AnimationDrawable animaition = (AnimationDrawable) iv_swip.getBackground();

		animaition.setOneShot(false);

		if (animaition.isRunning())// 是否正在运行？

		{
			animaition.stop();// 停止

		}
		animaition.start();// 启动

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}

	}
}
