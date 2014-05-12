package com.people.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.people.R;
import com.people.view.RefreshListView.RefreshListener;

// 流水
public class TransferListActivity extends BaseActivity implements OnClickListener, RefreshListener {
	
	private Button btn_back = null;
	private ListView listView = null;
	private Adapter adapter = null;
	
	private int totalPage;
	private int currentPage = 0;
	
	
//	private ArrayList<TransferDetailModel> modelList = new ArrayList<TransferDetailModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_transfer_list);
		
		listView = (ListView)this.findViewById(R.id.listview);
		
		adapter = new Adapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(TransferListActivity.this, TransferDetailActivity.class);
				startActivity(intent);
			}
			
		});
//		refresh();

	}
	
	public final class ViewHolder{
		public LinearLayout contentLayout;
		
		public TextView tv_date;
		public TextView tv_amount;
		public TextView tv_cardnum;
		
	}
	
	public class Adapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public Adapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		
		public int getCount(){
			return 10;
//			if (currentPage < totalPage){
//				return modelList.size() + 1;
//			} else {
//				return modelList.size();
//			}
		}
		
		public Object getItem(int arg0){
			return 0;
		}
		
		public long getItemId(int arg0){
			return arg0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			ViewHolder holder = null;
			if (null == convertView){
				holder = new ViewHolder();
				
				convertView = mInflater.inflate(R.layout.list_item_transfer, null);
				
				holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);
				
				holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tv_cardnum = (TextView) convertView.findViewById(R.id.tv_cardnum);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			return convertView;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public Object refreshing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshed(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void more() {
		// TODO Auto-generated method stub
		
	}
}