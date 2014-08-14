package com.people.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.people.R;
import com.people.client.Constants;
import com.people.qpos.ThreadCancel;

public class EIDSwipeActivity extends BaseActivity implements OnClickListener {

	private Button backBtn = null;
	private TextView titleView = null;

	private Intent intent = null;

	private AnimationDrawable animaition = null;

	private RelativeLayout layout_swip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_eid_search_swipe);


		backBtn = (Button) this.findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		

		titleView = (TextView) this.findViewById(R.id.titleView);

		layout_swip = (RelativeLayout) findViewById(R.id.layout_swip);

		titleView.setText("请刷卡");
		layout_swip.setVisibility(View.VISIBLE);
		ImageView iv_card = (ImageView) findViewById(R.id.iv_card);
		Animation myAnimation0 = AnimationUtils.loadAnimation(EIDSwipeActivity.this, R.anim.swip_card_anim);
		LinearInterpolator lir0 = new LinearInterpolator();
		myAnimation0.setInterpolator(lir0);
		iv_card.startAnimation(myAnimation0);
		
		Timer timer = new Timer();
		  TimerTask task = new TimerTask() {
		   @Override
		   public void run() {
		    success();
		   }
		  };
		timer.schedule(task, 1000 * 3); 
		
	}


	@Override
	public void onBackPressed() {

		this.backAction(null);
	}

	public void backAction(View view) {
		new ThreadCancel(null, this).start();

		this.finish();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bluetooth_btn) {
		} else if (view.getId() == R.id.btn_back) {
			this.backAction(null);
		}
	}


	public IntentFilter makeUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_QPOS_CANCEL);
		intentFilter.addAction(Constants.ACTION_QPOS_STARTSWIPE);
		intentFilter.addAction(Constants.ACTION_QPOS_SWIPEDONE);
		return intentFilter;
	}


	private void success(){
		Intent intent = new Intent(EIDSwipeActivity.this, TradeSuccessActivity.class);
		intent.putExtra("tips", "充值成功");
		startActivityForResult(intent, 100);
	}
	// ////////////////////////////////////////////////////////////////////////////////////////////////


		

		

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}
}
