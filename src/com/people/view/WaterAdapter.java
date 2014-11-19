package com.people.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.people.R;
import com.people.model.WaterModel;


public class WaterAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private ArrayList<WaterModel> array = new ArrayList<WaterModel>();
	private Context mContext;
	public WaterAdapter(Context context, int resource, ArrayList<WaterModel> objects) {

		mLayoutInflater = LayoutInflater.from(context);
		array = objects;
		mContext = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mLayoutInflater.inflate(R.layout.item_water, null);

			holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);

			holder.tv_customerName = (TextView) convertView.findViewById(R.id.tv_customerName);
			holder.tv_twqtcId = (TextView) convertView.findViewById(R.id.tv_twqtcId);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		WaterModel model = array.get(position);
		

		holder.tv_customerName.setText("zhangsan");
		holder.tv_twqtcId.setText("123");

		return convertView;
	}

	public void setData(ArrayList<WaterModel> data) {
		this.array = data;
	}

	public ArrayList<WaterModel> getData() {
		return array;
	}

	private class ViewHolder {
		public LinearLayout contentLayout;
		public TextView tv_customerName;
		public TextView tv_twqtcId;
	}

	@Override
	public int getCount() {
		return array.size();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}


}
