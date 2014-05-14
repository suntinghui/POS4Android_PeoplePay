package com.people.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.people.R;
import com.people.qpos.QPOS;

// 工具
public class ToolsActivity extends BaseActivity implements OnClickListener {
	private Integer[] imageIds = { R.drawable.swip_icon_n_0_no, R.drawable.swip_icon_n_1_no, R.drawable.swip_icon_n_2, R.drawable.swip_icon_n_3_no, R.drawable.swip_icon_n_4_no, R.drawable.swip_icon_n_5, R.drawable.swip_icon_n_6_no, R.drawable.swip_icon_n_7_no, R.drawable.swip_icon_n_8_no };

	private GridView gridView = null;
	private CatalogAdapter adapter = null;

	private long exitTimeMillis = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);

		gridView = (GridView) findViewById(R.id.gridveiw);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(onclickcistener);

		adapter = new CatalogAdapter(this);
		gridView.setAdapter(adapter);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		default:
			break;
		}

	}

	private OnItemClickListener onclickcistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg2) {
			case 0: // 余额查询
				break;

			case 1: // 参考信息
				break;

			case 2: // 官方微博
				break;

			case 3: // 卡卡转账
				break;

			case 4: // 交易分享
				break;

			case 5: // 推荐我们
				Intent intent5 = new Intent(ToolsActivity.this, ShareSettingActivity.class);
				ToolsActivity.this.startActivity(intent5);
				
				break;
			case 6: // 信用卡还款
				break;

			case 7: // 手机充值
				break;

			case 8: // 支付宝充值
				break;
			default:
				break;
			}

		}

	};

	public final class CatalogHolder {
		public ImageView CatalogCellImage;
	}

	public class CatalogAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CatalogAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return imageIds.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			CatalogHolder holder = null;

			if (null == convertView) {
				convertView = this.mInflater.inflate(R.layout.item_catalog, null);
				holder = new CatalogHolder();

				holder.CatalogCellImage = (ImageView) convertView.findViewById(R.id.catalogCellImage);

				convertView.setTag(holder);
			} else {
				holder = (CatalogHolder) convertView.getTag();
			}

			holder.CatalogCellImage.setImageResource(imageIds[position]);

			return convertView;
		}
	}

	// 程序退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTimeMillis = System.currentTimeMillis();
			} else {
				ArrayList<BaseActivity> list = BaseActivity.getAllActiveActivity();
				for (BaseActivity activity : list) {
					activity.finish();
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
