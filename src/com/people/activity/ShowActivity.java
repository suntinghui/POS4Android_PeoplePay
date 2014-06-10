package com.people.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.people.R;
import com.people.view.SlidingDrawerEx;
import com.people.view.beginguide.CircleFlowIndicator;
import com.people.view.beginguide.ViewFlow;
import com.people.view.beginguide.ViewFlow.ViewSwitchListener;

@SuppressWarnings("deprecation")
public class ShowActivity extends BaseActivity implements OnDrawerOpenListener, OnDrawerCloseListener{

	private int[] resIds;
	private int[] resBgIds = new int[] { R.drawable.lotteryticket_1,
			R.drawable.game_1, R.drawable.taxi_1,
			R.drawable.flights_1, R.drawable.fixed_1,
			R.drawable.qq_1, R.drawable.card_1,
			R.drawable.phone_1, R.drawable.donate_1
			};

	private PopupWindow popup;
	private SlidingDrawerEx slideDrawer = null;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_images);

		
		Intent intent = this.getIntent();
		int index = intent.getIntExtra("index", 0);
		
		layout = (LinearLayout) findViewById(R.id.layout);
		layout.setBackgroundResource(resBgIds[index]);
		
		switch (index) {
		case 0://彩票
			resIds = new int[] { R.drawable.lotteryticket_1,
					R.drawable.lotteryticket_2, R.drawable.lotteryticket_3,
					R.drawable.lotteryticket_4, R.drawable.lotteryticket_5,
					};
			break;
		case 1://游戏点卡
			resIds = new int[] { R.drawable.game_1,
					R.drawable.game_2, R.drawable.game_3};
			break;
		case 2:
			resIds = new int[] { R.drawable.taxi_1,
					R.drawable.taxi_2, R.drawable.taxi_3};
			break;
		case 3:
			resIds = new int[] { R.drawable.flights_1,
					R.drawable.flights_2, R.drawable.flights_3,
					R.drawable.flights_4, R.drawable.flights_5,
					R.drawable.flights_6 };
			break;
		case 4:
			resIds = new int[] { R.drawable.fixed_1,
					R.drawable.fixed_2, R.drawable.fixed_3};
			break;
		case 5:
			resIds = new int[] { R.drawable.qq_1,
					R.drawable.qq_2, R.drawable.qq_3,
					R.drawable.qq_4, R.drawable.qq_5};
			break;
		case 6:
			resIds = new int[] { R.drawable.card_1,
					R.drawable.card_2, R.drawable.card_3,
					R.drawable.card_4, R.drawable.card_5};
			break;
		case 7:
			resIds = new int[] { R.drawable.phone_1,
					R.drawable.phone_2, R.drawable.phone_3};
			break;
		case 8:
			resIds = new int[] { R.drawable.donate_1,
					R.drawable.donate_2, R.drawable.donate_3,
					R.drawable.donate_4, R.drawable.donate_5,
					R.drawable.donate_6 };
			break;

		default:
			break;
		}
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		this.showImages();
	}
	private void showImages() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View view = getLayoutInflater().inflate(R.layout.show_images, null);

		FrameLayout layout = (FrameLayout) view.findViewById(R.id.content);
		popup = new PopupWindow(layout,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		popup.setAnimationStyle(R.style.PopupAnimation);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setFocusable(true);
		popup.update();
		popup.showAtLocation(findViewById(R.id.layout), Gravity.CENTER, 0,
				0);

		final Button btn = (Button) view.findViewById(R.id.beginguide_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popup.dismiss();
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				ShowActivity.this.finish();
			}
		});

		ViewFlow viewFlow = (ViewFlow) view.findViewById(R.id.guide_gallery);
		viewFlow.setAdapter(new ImageAdapter(ShowActivity.this));
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

		CircleFlowIndicator indic = (CircleFlowIndicator) view
				.findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
	}

	// SlideDrawer
	@Override
	public void onDrawerOpened() {
//		slideDrawer.setSelected(true);
//		slideDrawer.getHandle().setBackgroundResource(
//				R.drawable.handle_selected);
//		ViewFlow viewFlow = (ViewFlow) findViewById(R.id.guide_gallery);
//		viewFlow.setAdapter(new ImageAdapter(ShowActivity.this));
//		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
//		viewFlow.setFlowIndicator(indic);
//
//		LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
//		topLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	}

	@Override
	public void onDrawerClosed() {
//		slideDrawer.setSelected(false);
//		LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
//		topLayout.setGravity(Gravity.CENTER_HORIZONTAL
//				| Gravity.CENTER_VERTICAL);
//
//		slideDrawer.getHandle().setBackgroundResource(R.drawable.handle_normal);
//		System.gc();
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
			imageView.setLayoutParams(new LinearLayout.LayoutParams(
					WindowManager.LayoutParams.FILL_PARENT,
					WindowManager.LayoutParams.FILL_PARENT));
			return imageView;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			popup.dismiss();
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			ShowActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
