package com.people.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.people.R;

// 刷卡主界面
public class SwipCardActivity extends BaseActivity implements OnClickListener {
	private Integer[] imageIds = { R.drawable.swip_icon_n_0, R.drawable.swip_icon_n_1, R.drawable.swip_icon_n_2, R.drawable.swip_icon_n_3, R.drawable.swip_icon_n_4, R.drawable.swip_icon_n_5, R.drawable.swip_icon_n_6, R.drawable.swip_icon_n_7, R.drawable.swip_icon_n_8 };

	private GridView gridView = null;
	private CatalogAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swip_card);

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

	// 点击事件
	private OnItemClickListener onclickcistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg2) {
			case 0: // 余额查询
				Intent intent0 = new Intent(SwipCardActivity.this, SearchAndSwapActivity.class);
				startActivity(intent0);
				break;

			case 1: // 参考信息
				// Intent intent1 = new Intent(SwipCardActivity.this,
				// PayListActivity.class);
				// startActivity(intent1);
				break;

			case 2: // 官方微博
				// Intent intent2 = new Intent(SwipCardActivity.this,
				// ReimbursementListActivity.class);
				// startActivity(intent2);
				break;

			case 3: // 卡卡转账
				// Intent intent3 = new Intent(SwipCardActivity.this,
				// TravelexpenseListActivity.class);
				// startActivity(intent3);
				break;

			case 4: // 交易分享
				// Intent intent4 = new Intent(SwipCardActivity.this,
				// OthersActivity.class);
				// startActivity(intent4);
				break;

			case 5: // 推荐我们
				// Intent intent5 = new Intent(SwipCardActivity.this,
				// SettingActivity.class);
				// startActivity(intent5);
				break;
			case 6: // 信用卡还款
				// Intent intent3 = new Intent(SwipCardActivity.this,
				// TravelexpenseListActivity.class);
				// startActivity(intent3);
				break;

			case 7: // 手机充值
				// Intent intent4 = new Intent(SwipCardActivity.this,
				// OthersActivity.class);
				// startActivity(intent4);
				break;

			case 8: // 支付宝充值
				// Intent intent5 = new Intent(SwipCardActivity.this,
				// SettingActivity.class);
				// startActivity(intent5);
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

}
