package com.people.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.model.CashModel;
import com.people.model.TradeModel;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.view.CashAdapter;
import com.people.view.DateSlider;
import com.people.view.LKAlertDialog;
import com.people.view.MonthYearDateSlider;
import com.people.view.ScrollLayout;
import com.people.view.TransferAdapter;

// eid
public class EidMsgActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	static final int MONTHYEARDATESELECTOR_ID = 4;

	private static final int INDEX_CONTENT = 0;
	private static final int INDEX_PATH = 1;

	protected LayoutInflater mLayoutInflater;

	private View mParentContent, mParentPath;
	private TextView mTvContent, mTvPath;
	private View mLineContent, mLinePath;

	private int mTextColorSelected;
	private int mTextColorUnselected;

	private ViewPager mViewPager;
	private ListView mContentLv, mCashLv;
	private TransferAdapter mContentAdapter;
	private CashAdapter mCashAdapter;

	private ArrayList<TradeModel> arrayTransfer = new ArrayList<TradeModel>();
	private ArrayList<CashModel> arrayCash = new ArrayList<CashModel>();


	private long exitTimeMillis = 0;

	private ImageView iv_nodata;

	private Boolean isCurrentList = true; // true: 交易流水 false:现金流水

	private int count;


	private boolean clickFlag = false;
	private ScrollView scrollView;
	private LinearLayout layout_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_eid_msg);

		mLayoutInflater = LayoutInflater.from(this);

		Resources res = getResources();
		mTextColorSelected = res.getColor(R.color.tab_text_selected);
		mTextColorUnselected = res.getColor(R.color.tab_text_unselected);

		iv_nodata = (ImageView) this.findViewById(R.id.iv_nodata);


		scrollView = (ScrollView) findViewById(R.id.scrollView);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		findViews();

		setupViews();

		mContentAdapter = new TransferAdapter(EidMsgActivity.this, 0, arrayTransfer);
		mContentLv.setAdapter(mContentAdapter);

		mCashAdapter = new CashAdapter(EidMsgActivity.this, 0, arrayCash);
		mCashLv.setAdapter(mCashAdapter);

	}

	protected void findViews() {

		mParentContent = findViewById(R.id.vg_content);
		mParentPath = findViewById(R.id.vg_path);

		mTvContent = (TextView) findViewById(R.id.tv_content);
		mTvPath = (TextView) findViewById(R.id.tv_path);

		mLineContent = findViewById(R.id.v_content_line_selected);
		mLinePath = findViewById(R.id.v_path_line_selected);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mContentLv = (ListView) mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mCashLv = (ListView) mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mContentLv.setOnItemClickListener(this);
		mCashLv.setOnItemClickListener(this);

	}

	protected void setupViews() {

		mParentContent.setOnClickListener(this);
		mParentPath.setOnClickListener(this);

		List<View> list = new ArrayList<View>();
		list.add(mContentLv);
		list.add(mCashLv);
		mViewPager.setAdapter(new MyPagerAdapter(list));
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void switchTo(int activeIdx) {

		if (activeIdx == INDEX_CONTENT) {
			isCurrentList = true;
			
			scrollView.setVisibility(View.VISIBLE);
			layout_right.setVisibility(View.GONE);
			mTvContent.setTextColor(mTextColorSelected);
			mTvPath.setTextColor(mTextColorUnselected);
			mLineContent.setVisibility(View.VISIBLE);
			mLinePath.setVisibility(View.GONE);

			mContentAdapter.setData(arrayTransfer);
			mContentAdapter.notifyDataSetChanged();

		} else if (activeIdx == INDEX_PATH) {
			isCurrentList = false;
			
			scrollView.setVisibility(View.GONE);
			layout_right.setVisibility(View.VISIBLE);
			
			mTvPath.setTextColor(mTextColorSelected);
			mTvContent.setTextColor(mTextColorUnselected);
			mLinePath.setVisibility(View.VISIBLE);
			mLineContent.setVisibility(View.GONE);
			mCashAdapter.setData(arrayCash);
			mCashAdapter.notifyDataSetChanged();

		}
		mViewPager.setCurrentItem(activeIdx);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		case R.id.vg_content:
			clickFlag = true;
			// 交易流水
			switchTo(INDEX_CONTENT);
			break;

		case R.id.vg_path:
			clickFlag = true;
			// 现金流水
			switchTo(INDEX_PATH);
			break;
		default:
			break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (isCurrentList) {// 交易列表
			Intent intent = new Intent(EidMsgActivity.this, TransferDetailActivity.class);
			intent.putExtra("model", arrayTransfer.get(arg2));
			startActivityForResult(intent, 0);
		} else {// 现金列表

		}

	}

	private class MyPagerAdapter extends PagerAdapter {
		List<View> mViews = null;

		public MyPagerAdapter(List<View> views) {
			mViews = views;
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View view = mViews.get(arg1);
			mViewPager.addView(view, 0);
			return mViews.get(arg1);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			mViewPager.removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			if (!clickFlag) {
				switchTo(index);
			}

			clickFlag = false;
		}

	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

	}

	
}
