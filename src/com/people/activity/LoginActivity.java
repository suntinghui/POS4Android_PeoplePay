package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.Constants;
import com.people.client.LKAsyncHttpResponseHandler;
import com.people.client.LKHttpRequest;
import com.people.client.LKHttpRequestQueue;
import com.people.client.LKHttpRequestQueueDone;
import com.people.client.TransferRequestTag;
import com.people.qpos.ThreadCancel;
import com.people.qpos.ThreadCloseSwip;
import com.people.qpos.ThreadDeviceID;
import com.people.qpos.ThreadOnlySixPass;
import com.people.qpos.ThreadOnlySwip;
import com.people.qpos.ThreadSwip_SixPass;
import com.people.qpos.ThreadUpDataKey;

import dspread.voicemodem.CardReader;
import dspread.voicemodem.DeviceBean;
import dspread.voicemodem.onPOSListener;
import dspread.voicemodem.util;

public class LoginActivity extends BaseActivity implements OnKeyListener, OnDismissListener, OnItemClickListener {
	private Thread mSthread, mCthread;
	// QPos
	private CardReader c = null;
	// 输入和显示
	private EditText sleeptime;
	private TextView text1;
	private Button dialog_data_load_btn;
	private boolean plugin;
	private long exitTime, common_btnn;
	// 弹出框
	private LinearLayout dialog_data_load;
	private TextView dialog_data_load_txt;
	// final byte[] fakekey = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte)
	// 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	// TODO 给李爽小盒专用，否则一般开发机用上面的
	final byte[] fakekey = { (byte) 0x00, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };

	// final byte[] fakekey = { (byte) 0x7D, (byte) 0xF3, (byte)
	// 0x35,(byte)0xE8, (byte) 0x0C, (byte) 0x5C, (byte) 0x35, (byte) 0x05 };
	private DeviceBean mRemoteDevice;// 远程蓝牙
	private TextView status, deviceaddress, device_txt;
	private Button device_btn;
	private Dialog mChooseDialog;
	private ProgressBar device_progress;
	private ListView device_list;
	// 搜索适配
	public Device_List_Adapter mDevice_List_Adapter;
	public ArrayList<DeviceBean> mDevices, mBondedDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		status = (TextView) findViewById(R.id.status);
		deviceaddress = (TextView) findViewById(R.id.deviceaddress);
		sleeptime = (EditText) findViewById(R.id.sleeptime);
		text1 = (TextView) findViewById(R.id.text1);
		dialog_data_load = (LinearLayout) findViewById(R.id.dialog_data_load);
		dialog_data_load.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		dialog_data_load_txt = (TextView) findViewById(R.id.dialog_data_load_txt);
		dialog_data_load_btn = (Button) findViewById(R.id.dialog_data_load_btn);

		// 选择弹出框
		mChooseDialog = new Dialog(this, R.style.ContentOverlay);
		mChooseDialog.setContentView(R.layout.choose_device);
		mChooseDialog.setCanceledOnTouchOutside(false);
		mChooseDialog.setOnDismissListener(this);
		// 弹出框布局
		device_txt = (TextView) mChooseDialog.findViewById(R.id.device_txt);
		device_progress = (ProgressBar) mChooseDialog.findViewById(R.id.device_progress);
		device_list = (ListView) mChooseDialog.findViewById(R.id.device_list);
		device_btn = (Button) mChooseDialog.findViewById(R.id.device_btn);
		device_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (device_progress.getVisibility() != View.VISIBLE) {
					if (c.startDiscovery()) {
						device_progress.setVisibility(View.VISIBLE);
						status.setText("准备搜索设备，请稍等！");
						device_txt.setText("准备搜索设备，请稍等...");
						device_btn.setText("取消扫描");
					} else {
						status.setText("开启扫描设备失败,请检测蓝牙是否打开！");
						device_txt.setText("开启扫描设备失败,请检测蓝牙是否打开！");
						Toast.makeText(LoginActivity.this, "开启扫描设备失败,请检测蓝牙是否打开！", Toast.LENGTH_SHORT).show();
					}
				} else {
					if (c.cancelDiscovery()) {
						device_progress.setVisibility(View.GONE);
						status.setText("已关闭扫描设备！");
						if (mDevices.size() == 0)
							device_txt.setText("设备列表:暂无设备");
						else
							device_txt.setText("设备列表：");
						device_btn.setText("扫描设备");
					} else {
						status.setText("关闭扫描设备失败,请检测蓝牙是否打开！");
						device_txt.setText("关闭扫描设备失败,请检测蓝牙是否打开！");
						Toast.makeText(LoginActivity.this, "关闭扫描设备失败,请检测蓝牙是否打开！", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		c = CardReader.getInstance(this, CardReader.BLUETOOTHMODE);
		util.turnUpVolume(getBaseContext(), 8);

		// 查询到的设备List
		mDevices = new ArrayList<DeviceBean>();// 显示的List
		mBondedDevices = new ArrayList<DeviceBean>();// 已配对的List
		mDevice_List_Adapter = new Device_List_Adapter(this, mDevices);
		device_list.setAdapter(mDevice_List_Adapter);
		device_list.setOnItemClickListener(this);

		c.setListener(mBtl);// 监听必须放到界面初始化完毕，否则回调则会界面无初始化导致崩溃
		
		login();
	}

	// 蓝牙监听
	private onPOSListener mBtl = new onPOSListener() {

		@Override
		public void onState(int state) {
			switch (state) {
			case CardReader.STATE_TURNING_ON:// 正在打开
				status.setText("本机蓝牙正在打开！");
				dialog_data_load_txt.setText("本机蓝牙正在打开！");
				break;
			case CardReader.STATE_ON:// 打开
				status.setText("本机蓝牙已打开！");
				dialog_data_load.setVisibility(View.GONE);
				break;
			case CardReader.STATE_TURNING_OFF:// 正在关闭
				status.setText("本机蓝牙正在关闭！");
				dialog_data_load_txt.setText("本机蓝牙正在关闭！");
				break;
			case CardReader.STATE_OFF:// 关闭
				status.setText("本机蓝牙已关闭！");
				dialog_data_load.setVisibility(View.GONE);
				break;
			case CardReader.DISCOVERY_STARTED:// 正在搜索,OK
				status.setText("正在搜索，一般情况大约12秒结束！");
				device_txt.setText("正在搜索，一般情况大约12秒结束...");
				break;
			case CardReader.DISCOVERY_FINISHED:// 搜索完毕,OK
				if (device_progress.getVisibility() != View.GONE) {
					device_progress.setVisibility(View.GONE);
					status.setText("搜索结束！");
					if (mDevices.size() == 0)
						device_txt.setText("设备列表:暂无设备");
					else
						device_txt.setText("设备列表：");
					device_btn.setText("扫描设备");
				}
				break;
			default:
				status.setText("本机蓝牙未知状态！");
				Toast.makeText(LoginActivity.this, "本机蓝牙未知状态！", Toast.LENGTH_SHORT).show();
				break;
			}
		}

		@Override
		public void onFoundBluetooth(DeviceBean device) {
			boolean isExist = false;
			for (int i = 0; i < mDevices.size(); i++) {
				if (device.getAddress().equals(mDevices.get(i).getAddress())) {
					mDevices.get(i).setName(device.getName());
					mDevices.get(i).setBondState(device.getBondState());
					mDevices.get(i).setRssi(device.getRssi());
					isExist = true;
					break;
				}
			}
			if (!isExist)
				mDevices.add(device);
			mDevice_List_Adapter.notifyDataSetChanged();
		}

		@Override
		public void onError(int errCode) {
			switch (errCode) {
			case CardReader.STATE_UNKNOW:
				status.setText("蓝牙状态未知！");
				Toast.makeText(LoginActivity.this, "蓝牙状态未知！", Toast.LENGTH_SHORT).show();
				break;
			default:
				status.setText("未知错误！");
				Toast.makeText(LoginActivity.this, "未知错误！", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	public void fun_click(View v) {
		if ((System.nanoTime() - common_btnn) / 1000000 < 1000) {
			Toast.makeText(this, "点击间隔时间过短！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mCthread != null && mCthread.isAlive()) {
			Toast.makeText(this, "稍等，还未取消完毕！", Toast.LENGTH_SHORT).show();
			return;
		}
		mCthread = null;
		common_btnn = System.nanoTime();
		if (dialog_data_load.getVisibility() != View.VISIBLE)
			dialog_data_load.setVisibility(View.VISIBLE);
		if (dialog_data_load_btn.getVisibility() != View.GONE)
			dialog_data_load_btn.setVisibility(View.GONE);
		text1.setText("");
		switch (v.getId()) {
		case R.id.opendevice:// 打开蓝牙
			if (c.open()) {
				dialog_data_load_txt.setText("本机蓝牙正在打开！");
				status.setText("本机蓝牙正在打开！");
			} else {
				dialog_data_load.setVisibility(View.GONE);
				status.setText("打开蓝牙失败！");
				Toast.makeText(this, "打开蓝牙失败！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.closedevice:// 关闭蓝牙
			if (c.close()) {
				dialog_data_load_txt.setText("本机蓝牙正在关闭！s");
				status.setText("本机蓝牙正在关闭！");
			} else {
				dialog_data_load.setVisibility(View.GONE);
				status.setText("关闭蓝牙失败！");
				Toast.makeText(this, "关闭蓝牙失败！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.choosedevice:// 选择蓝牙
			dialog_data_load.setVisibility(View.GONE);
			if (mChooseDialog != null && !mChooseDialog.isShowing()) {
				if (mDevices.size() != 0) {
					mDevices.clear();
				}
				if (mBondedDevices.size() != 0) {
					mBondedDevices.clear();// 若其他操作适配了有广播？
				}
				mBondedDevices.addAll(c.getBondedDevices());
				mDevices.addAll(mBondedDevices);// 还有一种情况，没配对而且也不再范围内的也会显示，扫描完后第二次显示
				mDevice_List_Adapter.notifyDataSetChanged();
			}
			if (mDevices.size() == 0)
				device_txt.setText("设备列表:暂无设备");
			else
				device_txt.setText("设备列表：");
			mChooseDialog.show();
			break;

		case R.id.btnPowerOff:// 关闭刷卡器
			dialog_data_load_txt.setText("关闭刷卡器，请稍等！");
			new ThreadCloseSwip(mHandler, this, c).start();
			break;
		case R.id.btnUpgradeKey:// 更新密钥
			dialog_data_load_txt.setText("更新密钥，请稍等！");
			new ThreadUpDataKey(mHandler, this, c).start();
			break;
		case R.id.btn1:// 获取终端号
			dialog_data_load_txt.setText("获取终端号，请稍等！");
			new ThreadDeviceID(mHandler, this, c).start();
			break;
		case R.id.btnMag:// 只刷卡
			dialog_data_load_txt.setText("只刷卡，请稍等！");
			if (dialog_data_load_btn.getVisibility() != View.VISIBLE)
				dialog_data_load_btn.setVisibility(View.VISIBLE);
			mSthread = new ThreadOnlySwip(mHandler, this, c, fakekey);
			mSthread.start();
			break;
		case R.id.btnPwd:// 只输入6位密码
			dialog_data_load_txt.setText("只输入6位密码，请稍等！");
			if (dialog_data_load_btn.getVisibility() != View.VISIBLE)
				dialog_data_load_btn.setVisibility(View.VISIBLE);
			mSthread = new ThreadOnlySixPass(mHandler, this, c, fakekey);
			mSthread.start();
			break;
		case R.id.btnMagPwd:// 刷卡+6位密码
			dialog_data_load_txt.setText("刷卡+6位密码，请稍等！");
			if (dialog_data_load_btn.getVisibility() != View.VISIBLE)
				dialog_data_load_btn.setVisibility(View.VISIBLE);
			mSthread = new ThreadSwip_SixPass(mHandler, this, c, fakekey);
			mSthread.start();
			break;
		case R.id.dialog_data_load_btn:
			dialog_data_load_txt.setText("正在取消！");
			mCthread = new ThreadCancel(mHandler, this, c);
			mSthread.interrupt();
			mCthread.start();
			break;
		}
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dialog_data_load.setVisibility(View.GONE);
			switch (msg.what) {
			default:
				text1.setText("" + msg.obj);
				break;
			}
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - exitTime) > 1500) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (c != null) {
			c.exit();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	private void login(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199002");
		tempMap.put("PHONENUMBER", "13838387438");
		tempMap.put("PASSWORD", "88888888");
		tempMap.put("PCSIM", "不能获取");
		
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.Login, tempMap, getLoginHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
				
			}
			
		});	

	}

	private LKAsyncHttpResponseHandler getLoginHandler(){
	 return new LKAsyncHttpResponseHandler(){
		 
		@Override
		public void successAction(Object obj) {
			Log.e("success:", obj.toString());
			
			if (obj instanceof HashMap){
				// 登录成功
				Log.e("success:", obj.toString());
			} else {
			}
			
		}

	};
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mRemoteDevice = mDevices.get(arg2);
		c.set_peer_address(mRemoteDevice.getAddress());
		deviceaddress.setText(mRemoteDevice.getName() + " ~~Mac：" + mRemoteDevice.getAddress());
		if (mChooseDialog != null && mChooseDialog.isShowing())
			mChooseDialog.dismiss();
		
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		if (device_progress.getVisibility() != View.GONE || c.isDiscovering()) {
			c.cancelDiscovery();
			device_progress.setVisibility(View.GONE);
			status.setText("已关闭扫描设备！");
			device_btn.setText("扫描设备");
		}
		
	}

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return true;
	}
}