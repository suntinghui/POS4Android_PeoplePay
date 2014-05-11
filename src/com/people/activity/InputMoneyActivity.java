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
import android.widget.Button;
import android.widget.GridView;

import com.people.R;

public class InputMoneyActivity extends BaseActivity implements OnClickListener {
	private GridView gridView = null;
	private CatalogAdapter adapter = null;
	private String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "." };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputmoney);

		gridView = (GridView) findViewById(R.id.gridveiw);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(onclickcistener);

		adapter = new CatalogAdapter(this);
		gridView.setAdapter(adapter);

	}

	// 点击事件
	private OnItemClickListener onclickcistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg2) {
			case 0: //
				break;

			case 1: //
				break;

			case 2: //
				break;

			case 3: //
				break;

			case 4: //
				break;

			case 5: //
				break;
			case 6: //
				break;

			case 7: //
				break;

			case 8: //
				break;
			case 9: // 删除
				break;
			case 10: //
				break;

			case 11: // dot
				break;
			default:
				break;
			}

		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			// login();
			Intent intent = new Intent(InputMoneyActivity.this, CatalogActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_register:
			Intent intent1 = new Intent(InputMoneyActivity.this, ForgetLoginPwdActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}

	}

	public final class CatalogHolder {
		public Button btn_num;
	}

	public class CatalogAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CatalogAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return num.length;
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
				convertView = this.mInflater.inflate(R.layout.item_inputmoney, null);
				holder = new CatalogHolder();

				holder.btn_num = (Button) convertView.findViewById(R.id.btn_num);

				convertView.setTag(holder);
			} else {
				holder = (CatalogHolder) convertView.getTag();
			}

			holder.btn_num.setText(num[position]);
			return convertView;
		}
	}

}
