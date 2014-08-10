package com.people.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.people.R;
import com.people.model.CashModel;
import com.people.model.TradeModel;
import com.people.util.ByteUtil;
import com.people.util.StringUtil;
import com.people.view.CashAdapter;
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

	private ImageView iv_nodata;

	private Boolean isCurrentList = true; // true: 交易流水 false:现金流水

	private boolean clickFlag = false;
	private ScrollView scrollView;
	private LinearLayout layout_right;

	private NfcAdapter mAdapter;
	private static String TAG = "NFC";
	
	private TextView rqsyView = null;
	private TextView wjsyView = null;
	private TextView rqxxView = null;
	private TextView ztxxView = null;
	private TextView ztnlView = null;
	private TextView micxView = null;
	private TextView micxsm2View = null;
	
	private TextView accountNoView = null;
	private TextView balanceView = null;

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

		// FOR NFC
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intent = getIntent();
		onNewIntent(intent);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if (mAdapter == null || !mAdapter.isEnabled())
			return;
		
		String action = intent.getAction();
		Log.e(TAG, "Discovered tag with intent: " + action);
		
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			try {
				IsoDep isodep = IsoDep.get(tagFromIntent);
				isodep.connect();

				// 选择ADF_EID
				byte[] cmd1 = isodep.transceive(ByteUtil.hexStringToBytes("00A404000E315041592E5359532E4444463031"));
				Log.e(TAG, "选择ADF_EID:" + ByteUtil.byteArr2HexStr(cmd1));

				// 选择容器索引文件
				byte[] cmd2 = isodep.transceive(ByteUtil.hexStringToBytes("00A4000002FFFD"));
				Log.e(TAG, "选择容器索引文件:" + ByteUtil.byteArr2HexStr(cmd2));

				// 读取容器索引文件
				byte[] cmd3 = isodep.transceive(ByteUtil.hexStringToBytes("00B6000002"));
				Log.e(TAG, "读取容器索引文件:" + ByteUtil.byteArr2HexStr(cmd3));
				
				// 选择文件索引文件
				byte[] cmd4 = isodep.transceive(ByteUtil.hexStringToBytes("00A4000002FFFE"));
				Log.e(TAG, "选择文件索引文件:" + ByteUtil.byteArr2HexStr(cmd4));
				
				// 读取文件索引文件
				byte[] cmd5 = isodep.transceive(ByteUtil.hexStringToBytes("00B6000018"));
				Log.e(TAG, "读取文件索引文件:" + ByteUtil.byteArr2HexStr(cmd5));
				
				// 选择容器信息文件
				byte[] cmd6 = isodep.transceive(ByteUtil.hexStringToBytes("00A4000002FFFF"));
				Log.e(TAG, "选择容器信息文件:" + ByteUtil.byteArr2HexStr(cmd6));
				
				// 读取容器信息文件
				byte[] cmd7 = isodep.transceive(ByteUtil.hexStringToBytes("00B60000FA"));
				Log.e(TAG, "读取容器信息文件:" + ByteUtil.byteArr2HexStr(cmd7));
				
				// 读取容器信息文件
				byte[] cmd8 = isodep.transceive(ByteUtil.hexStringToBytes("00B600FA6E"));
				Log.e(TAG, "读取容器信息文件:" + ByteUtil.byteArr2HexStr(cmd8));
				
				// 选择载体信息文件
				byte[] cmd9 = isodep.transceive(ByteUtil.hexStringToBytes("00A40000024001"));
				Log.e(TAG, "选择载体信息文件:" + ByteUtil.byteArr2HexStr(cmd9));
				
				// 读取载体信息文件
				byte[] cmd10 = isodep.transceive(ByteUtil.hexStringToBytes("00B6000040"));
				Log.e(TAG, "读取载体信息文件:" + ByteUtil.byteArr2HexStr(cmd10));
				
				// 选择载体能力文件
				byte[] cmd11 = isodep.transceive(ByteUtil.hexStringToBytes("00A40000024000"));
				Log.e(TAG, "选择载体能力文件:" + ByteUtil.byteArr2HexStr(cmd11));
				
				// 读取载体能力文件
				byte[] cmd12 = isodep.transceive(ByteUtil.hexStringToBytes("00B6000040"));
				Log.e(TAG, "读取载体能力文件:" + ByteUtil.byteArr2HexStr(cmd12));
				
				// 选择EID MICX RSA2048公钥文件
				byte[] cmd13 = isodep.transceive(ByteUtil.hexStringToBytes("00A400000221D3"));
				Log.e(TAG, "选择EID MICX RSA2048公钥文件:" + ByteUtil.byteArr2HexStr(cmd13));
				
				// 读取EID MICX RSA2048公钥文件
				byte[] cmd14 = isodep.transceive(ByteUtil.hexStringToBytes("00B60000FF"));
				Log.e(TAG, "读取EID MICX RSA2048公钥文件:" + ByteUtil.byteArr2HexStr(cmd14));
				
				// 读取EID MICX RSA2048公钥文件
				byte[] cmd15 = isodep.transceive(ByteUtil.hexStringToBytes("00B600FF0D"));
				Log.e(TAG, "读取EID MICX RSA2048公钥文件:" + ByteUtil.byteArr2HexStr(cmd15));
				
				// 选择EID MICX SM2公钥文件
				byte[] cmd16 = isodep.transceive(ByteUtil.hexStringToBytes("00A400000222F3"));
				Log.e(TAG, "选择EID MICX SM2公钥文件:" + ByteUtil.byteArr2HexStr(cmd16));
				
				// 读取EID MICX SM2公钥文件
				byte[] cmd17 = isodep.transceive(ByteUtil.hexStringToBytes("00B6000042"));
				Log.e(TAG, "读取EID MICX SM2公钥文件:" + ByteUtil.byteArr2HexStr(cmd17));
				
				////////////////////////////////////////////////
				
				// 选择ADF_PPSE
				byte[] cmd201 = isodep.transceive(ByteUtil.hexStringToBytes("00A404000E315041592E5359532E4444463031"));
				Log.e(TAG, "选择ADF_PPSE:" + ByteUtil.byteArr2HexStr(cmd201));
				
				// 选择借记账户
				byte[] cmd202 = isodep.transceive(ByteUtil.hexStringToBytes("00A4040007A0000003330101"));
				Log.e(TAG, "选择借记账户:" + ByteUtil.byteArr2HexStr(cmd202));
				
				// 读卡片账号预读
				byte[] cmd203 = isodep.transceive(ByteUtil.hexStringToBytes("00B2011C00"));
				Log.e(TAG, "读卡片账号预读:" + ByteUtil.byteArr2HexStr(cmd203));
				
				// 读卡片账号
				byte[] cmd204 = isodep.transceive(ByteUtil.hexStringToBytes("00B2011C"));
				String accountNoStr = ByteUtil.byteArr2HexStr(cmd204);
				Log.e(TAG, "读卡片账号:" + accountNoStr);
				
				// 读取电子现金余额   805C000204   
				byte[] cmd205 = isodep.transceive(ByteUtil.hexStringToBytes("80CA9F7900"));
				String balanceStr = ByteUtil.byteArr2HexStr(cmd205);
				Log.e(TAG, "读取电子现金余额:" + balanceStr);
				
				
				isodep.close();
				
				// EID
				rqsyView.setText("3F00");
				wjsyView.setText("010003000300000000000100010001000300000001000100");
				rqxxView.setText("7b 34 41 37 41 32 36 42 31 2d 41 42 41 35 2d 34 38 65 66 2d 38 42 36 41 2d 32 34 41 34 42 41 34 32 45 37 38 37 7d 00 00 00 01 00 00 00 00 00 00 11 10 12 10 13 10 00 00 00 00 00 00 7b 39 42 42 41 33 36 41 34 2d 34 45 35 44 2d 34 34 65 33 2d 38 38 33 33 2d 43 43 34 44 42 46 45 41 30 38 38 36 7d 00 00 00 20 00 00 00 00 00 00 00 00 12 13 13 13 00 00 00 00 00 00 7b 43 42 42 35 30 33 43 33 2d 45 30 43 36 2d 34 61 65 39 2d 39 35 43 39 2d 32 45 37 44 33 39 42 45 42 43 46 34 7d 00 00 00 02 00 00 00 00 00 00 00 00 00 00 d3 21 00 00 00 00 00 00 7b 32 32 39 39 39 32 44 38 2d 34 30 37 42 2d 34 64 37 35 2d 41 45 44 37 2d 30 46 34 44 33 34 41 45 35 46 38 38 7d 00 00 00 01 00 00 00 00 00 00 00 00 f2 a0 f3 a0 00 00 00 00 00 00 7b 41 36 37 37 44 38 32 42 2d 33 35 46 35 2d 34 30 39 34 2d 42 43 34 31 2d 38 38 33 45 43 30 46 36 37 44 37 39 7d 00 00 00 04 00 00 00 00 00 00 11 12 12 12 13 12 00 00 00 00 00 00 7b 44 41 38 46 38 43 44 39 2d 30 32 43 36 2d 34 39 31 32 2d 39 41 34 39 2d 37 38 43 32 31 46 36 38 37 41 42 46 7d 00 00 00 04 00 00 00 00 00 00 00 00 00 00 f3 22 00 00 00 00 00 00".replace(" ", ""));
				ztxxView.setText("000000000000"); // 全0
				ztnlView.setText("A0FF98010003000300000000000100010001000300000001000100"); // 非全0
				micxView.setText("805C000204010003000300");
				micxsm2View.setText("6F00A404000E315041592E5359532E4444463031");
				
				// 账号
				int off = accountNoStr.indexOf("5A");
				String lengthStr = accountNoStr.substring(off+2, off+4);
				int accountLen = Integer.parseInt(lengthStr, 16);
				String account = accountNoStr.substring(off+4, off+4+accountLen*2).replace("F", "");
				accountNoView.setText(account);
				
				// 余额
				String balance = StringUtil.String2SymbolAmount(balanceStr.substring(6, 18));
				balanceView.setText(balance);
				
			} catch (Exception e) {
				Log.e(TAG, "ERROR:" + e.getMessage());
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (mAdapter == null || !mAdapter.isEnabled())
			return;
		
		mAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (mAdapter == null || !mAdapter.isEnabled())
			return;
		
		PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		IntentFilter[] mFilters = new IntentFilter[] { ndef, };
		String[][] mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
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
		
		// EID
		rqsyView = (TextView) this.findViewById(R.id.tv_rqsy);
		wjsyView = (TextView) this.findViewById(R.id.tv_wjsy);
		rqxxView = (TextView) this.findViewById(R.id.tv_rqxx);
		ztxxView = (TextView) this.findViewById(R.id.tv_ztxx);
		ztnlView = (TextView) this.findViewById(R.id.tv_ztnl);
		micxView = (TextView) this.findViewById(R.id.tv_micx);
		micxsm2View = (TextView) this.findViewById(R.id.tv_micxsm2);
		
		accountNoView = (TextView) this.findViewById(R.id.tv_accountNo);
		balanceView = (TextView) this.findViewById(R.id.tv_balance);
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
