package com.people.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.people.R;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private ListView listView = null;
	private Adapter adapter = null;

	private Integer[] imageIds = { R.drawable.set_icon_0, R.drawable.set_icon_1, R.drawable.set_icon_2, R.drawable.set_icon_3, R.drawable.set_icon_4 };

	private String[] titles = { "设置锁屏手势", "关于系统", "意见反馈", "检查更新", "帮助" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		listView = (ListView) this.findViewById(R.id.listview);
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		adapter = new Adapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				case 0:
					Intent intent0 = new Intent(SettingActivity.this, LockScreenSettingActivity.class);
					SettingActivity.this.startActivity(intent0);
					break;

				case 1:
					Intent intent1 = new Intent(SettingActivity.this, AboutSystemActivity.class);
					SettingActivity.this.startActivity(intent1);
					break;
				case 2:
//					Intent intent1 = new Intent(SettingActivity.this, FeedBackActivity.class);
//					SettingActivity.this.startActivity(intent1);
					break;
				case 3:
//					Intent intent1 = new Intent(SettingActivity.this, FeedBackActivity.class);
//					SettingActivity.this.startActivity(intent1);
					break;
				default:
					break;
				}
			}

		});


	}


	public final class ViewHolder {
		public RelativeLayout contentLayout;

		public TextView tv_content;
		public ImageView iv_left;
	}

	public class Adapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public Adapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {

			return titles.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (null == convertView) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item_setting, null);

				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
				holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.iv_left.setBackgroundResource(imageIds[position]);
			holder.tv_content.setText(titles[position]);

			return convertView;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}

	}

}