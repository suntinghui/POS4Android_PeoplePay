package com.people.view;

import java.util.ArrayList;

import com.people.R;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dspread.voicemodem.DeviceBean;

/**
 * 设备列表,OK
 * 
 * @author Fncat
 * 
 */
public class Device_List_Adapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<DeviceBean> mList;

	public Device_List_Adapter(Context mContext, ArrayList<DeviceBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		TextView device_name;
		TextView device_address;
		TextView device_bonded;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.device_item, null);
			holder = new ViewHolder();
			holder.device_name = (TextView) convertView.findViewById(R.id.device_name);
			holder.device_address = (TextView) convertView.findViewById(R.id.device_address);
			holder.device_bonded = (TextView) convertView.findViewById(R.id.device_bonded);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.device_name.setText(mList.get(position).getName() + "");
		holder.device_address.setText(mList.get(position).getAddress() + "");

		if (mList.get(position).getBondState() == BluetoothDevice.BOND_BONDED) {
			holder.device_bonded.setText("已匹配");
		} else if (mList.get(position).getBondState() == BluetoothDevice.BOND_BONDING) {
			holder.device_bonded.setText("正在匹配");
		} else if (mList.get(position).getBondState() == BluetoothDevice.BOND_NONE) {
			holder.device_bonded.setText("未匹配");
		} else
			holder.device_bonded.setText("未知状态");

		return convertView;
	}

}
