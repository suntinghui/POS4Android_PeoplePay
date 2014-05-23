package com.people.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.android.common.logging.Log;
import com.people.R;
import com.people.qpos.QPOS;

import dspread.voicemodem.BlueToothConnection;
import dspread.voicemodem.CardReader;
import dspread.voicemodem.DeviceBean;
import dspread.voicemodem.onPOSListener;

public class BLDeviceDialog extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener {

	private Context context;

	private Button searchButton = null;
	private Button cancelButton = null;
	private Button openButton = null;

	private ProgressBar progressBar = null;
	private TextView stateView = null;

	private ListView listView = null;

	public Device_List_Adapter mDevice_List_Adapter;
	public ArrayList<DeviceBean> mDevices, mBondedDevices;
	
	private OnSelectBLListener onSelectBLListener = null;

	public BLDeviceDialog(Context context) {
		super(context, R.style.Dialog);
		this.context = context;

	}

	public BLDeviceDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	
	public void setOnSelectBLListener(OnSelectBLListener listener){
		this.onSelectBLListener = listener;
	}

	public BLDeviceDialog create() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.layout_bluetooth_dialog, null);
		this.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		this.progressBar = (ProgressBar) layout.findViewById(R.id.device_progress);
		this.stateView = (TextView) layout.findViewById(R.id.state);

		this.openButton = (Button) layout.findViewById(R.id.openButton);
		this.openButton.setOnClickListener(this);

		this.searchButton = (Button) layout.findViewById(R.id.searchButton);
		this.searchButton.setOnClickListener(this);

		this.cancelButton = (Button) layout.findViewById(R.id.cancelButton);
		this.cancelButton.setOnClickListener(this);

		this.listView = (ListView) layout.findViewById(R.id.listview);

		mDevices = new ArrayList<DeviceBean>();// 显示的List
		mBondedDevices = new ArrayList<DeviceBean>();// 已配对的List

		mBondedDevices.clear();// 若其他操作适配了有广播？
		mBondedDevices.addAll(QPOS.getCardReader().getBondedDevices());
		mDevices.addAll(mBondedDevices);// 还有一种情况，没配对而且也不再范围内的也会显示，扫描完后第二次显示

		mDevice_List_Adapter = new Device_List_Adapter(this.context, mDevices);
		listView.setAdapter(mDevice_List_Adapter);
		listView.setOnItemClickListener(this);

		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		this.setOnDismissListener(this);

		QPOS.getCardReader().setListener(mBtl);

		return this;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.openButton) { // 打开蓝牙
			boolean flag = QPOS.getCardReader().open();
			if (flag) {
				this.openButton.setVisibility(View.GONE);
				this.cancelButton.setVisibility(View.GONE);
				this.searchButton.setVisibility(View.VISIBLE);
			} else {
				this.openButton.setVisibility(View.VISIBLE);
				this.cancelButton.setVisibility(View.GONE);
				this.searchButton.setVisibility(View.GONE);
			}

		} else if (v.getId() == R.id.searchButton) { // 扫描设备
			progressBar.setVisibility(View.VISIBLE);
			stateView.setText("正在搜索，一般情况大约12秒结束！");

			boolean flag = QPOS.getCardReader().startDiscovery();
			
			if (flag) {
				Log.i("flag:", "yes");
				this.searchButton.setVisibility(View.GONE);
				this.cancelButton.setVisibility(View.VISIBLE);
				this.openButton.setVisibility(View.GONE);

				mDevices.clear();
				mBondedDevices.clear();

				mBondedDevices.addAll(QPOS.getCardReader().getBondedDevices());
				Log.i("mBondedDevices:", mBondedDevices.size()+"");
				mDevices.addAll(mBondedDevices);


			} else {
				Log.i("flag:", "no");
				stateView.setText("开启扫描设备失败,请检测蓝牙是否打开！");

				this.searchButton.setVisibility(View.VISIBLE);
				this.cancelButton.setVisibility(View.GONE);
				this.openButton.setVisibility(View.GONE);
			}

		} else if (v.getId() == R.id.cancelButton) { // 取消扫描设备
			progressBar.setVisibility(View.GONE);

			if (mDevices.size() == 0) {
				stateView.setText("已停止扫描！没有找到配对的蓝牙设备");
			} else {
				stateView.setText("已停止扫描！");
			}

			boolean flag = QPOS.getCardReader().cancelDiscovery();
			if (flag) {
				this.searchButton.setVisibility(View.VISIBLE);
				this.cancelButton.setVisibility(View.GONE);
				this.openButton.setVisibility(View.GONE);
			} else {
				stateView.setText("关闭扫描设备失败,请检测蓝牙是否打开！");
			}
		}

	}
	
	@Override
	public void onDismiss(DialogInterface arg0) {
		QPOS.getCardReader().cancelDiscovery();
		//this.context.unregisterReceiver(BlueToothConnection.getInstance(this.context));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		DeviceBean mRemoteDevice = mDevices.get(arg2);
		QPOS.getCardReader().set_peer_address(mRemoteDevice.getAddress());
		if (this.isShowing()) {
			this.dismiss();
		}
		
		if (this.onSelectBLListener != null){
			this.onSelectBLListener.onSelect();
		}
		
	}

	// 蓝牙监听
	private onPOSListener mBtl = new onPOSListener() {

		@Override
		public void onError(int errCode) {
			switch (errCode) {
			case CardReader.STATE_UNKNOW: // 蓝牙状态未知
				stateView.setText("蓝牙状态未知！");
				break;

			default:
				stateView.setText("未知错误！");
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
		public void onState(int state) {
			switch (state) {
			case CardReader.STATE_TURNING_ON:// 正在打开
				stateView.setText("本机蓝牙正在打开！");
				searchButton.setVisibility(View.VISIBLE);
				cancelButton.setVisibility(View.GONE);
				openButton.setVisibility(View.GONE);

				break;

			case CardReader.STATE_ON:// 已打开
				stateView.setText("本机蓝牙已打开！");
				searchButton.setVisibility(View.VISIBLE);
				cancelButton.setVisibility(View.GONE);
				openButton.setVisibility(View.GONE);

				break;

			case CardReader.STATE_TURNING_OFF:// 正在关闭
				stateView.setText("本机蓝牙正在关闭！");
				searchButton.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				openButton.setVisibility(View.VISIBLE);

				break;

			case CardReader.STATE_OFF:// 已关闭
				stateView.setText("本机蓝牙已关闭！");
				searchButton.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				openButton.setVisibility(View.VISIBLE);

				break;

			case CardReader.DISCOVERY_STARTED:// 正在搜索,OK 正在搜索，一般情况大约12秒结束！
				progressBar.setVisibility(View.VISIBLE);
				stateView.setText("正在搜索，一般情况大约12秒结束！");
				break;

			case CardReader.DISCOVERY_FINISHED:// 搜索完毕,OK
				searchButton.setVisibility(View.VISIBLE);
				cancelButton.setVisibility(View.GONE);
				openButton.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);

				if (mDevices.size() == 0) {
					stateView.setText("搜索完毕，没有找到配对的蓝牙设备。");
				} else {
					stateView.setText("搜索完毕。");
				}

				break;

			default: // 本机蓝牙未知状态！
				stateView.setText("本机蓝牙未知状态！");
				break;
			}
		}

	};
	
	public interface OnSelectBLListener{
		public void onSelect();
	}

}

