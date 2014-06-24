package com.people.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.qpos.QPOS;
import com.people.qpos.ThreadCancel;
import com.people.qpos.ThreadPowerOff;
import com.people.view.SlidingDrawerEx;
import com.people.view.beginguide.CircleFlowIndicator;
import com.people.view.beginguide.ViewFlow;
import com.people.view.beginguide.ViewFlow.ViewSwitchListener;

import dspread.voicemodem.CardReader;

@SuppressWarnings("deprecation")
public class ChooseQPOSModeActivity extends BaseActivity implements OnDrawerOpenListener, OnDrawerCloseListener {

	public static final int FROM_LOGINACTIVITY = 0;
	public static final int FROM_SETTINGACTIVITY = 1;

	private int from;

	private int[] resIds = new int[] { R.drawable.beginguide01, R.drawable.beginguide02, R.drawable.beginguide03, R.drawable.beginguide04 };

	private SlidingDrawerEx slideDrawer = null;

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
			
			// TODO 关闭设备有问题。。。
			/*
			new ThreadPowerOff(null, this).start();
			Toast.makeText(this, "设备已关机，请重新启动", Toast.LENGTH_SHORT).show();
			*/

		} else if (view.getId() == R.id.choose_line_btn) {
			Log.e("MODE", "选择数据线连接方式...");

			QPOS.changeCardReader(CardReader.PSKMODE);
			// new ThreadPowerOff(null, this).start();
			// Toast.makeText(this, "设备已关机，请重新启动", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && ApplicationEnvironment.getInstance().getPreferences().getBoolean(Constants.NEWAPP, true)) {
			Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
			editor.putBoolean(Constants.NEWAPP, false);
			editor.commit();

			this.showBeginnerGuide();
		}
	}

	private void showBeginnerGuide() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View view = getLayoutInflater().inflate(R.layout.beginnerguide, null);

		FrameLayout layout = (FrameLayout) view.findViewById(R.id.content);
		final PopupWindow popup = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		popup.setAnimationStyle(R.style.PopupAnimation);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setFocusable(true);
		popup.update();
		popup.showAtLocation(findViewById(R.id.layout), Gravity.CENTER, 0, 0);

		final Button btn = (Button) view.findViewById(R.id.beginguide_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popup.dismiss();
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		});

		ViewFlow viewFlow = (ViewFlow) view.findViewById(R.id.guide_gallery);
		viewFlow.setAdapter(new ImageAdapter(ChooseQPOSModeActivity.this));
		viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {
			@Override
			public void onSwitched(View view, int position) {
				if (position == resIds.length - 1) {
					btn.setVisibility(View.VISIBLE);
				} else {
					btn.setVisibility(View.GONE);
				}
			}
		});

		CircleFlowIndicator indic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
	}

	// SlideDrawer
	@Override
	public void onDrawerOpened() {
		slideDrawer.setSelected(true);
		slideDrawer.getHandle().setBackgroundResource(R.drawable.handle_selected);
		ViewFlow viewFlow = (ViewFlow) findViewById(R.id.guide_gallery);
		viewFlow.setAdapter(new ImageAdapter(ChooseQPOSModeActivity.this));
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);

		LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
		topLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	}

	@Override
	public void onDrawerClosed() {
		slideDrawer.setSelected(false);
		LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
		topLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

		slideDrawer.getHandle().setBackgroundResource(R.drawable.handle_normal);
		System.gc();
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
		}

		// 返回图像总数
		public int getCount() {
			return resIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// 返回具体位置的ImageView对象
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			// 设置当前图像的图像（position为当前图像列表的位置）
			imageView.setImageResource(resIds[position]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));
			return imageView;
		}

	}
}
