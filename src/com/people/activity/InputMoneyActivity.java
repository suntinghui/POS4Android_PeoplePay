package com.people.activity;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.util.DateUtil;
import com.people.util.StringUtil;

public class InputMoneyActivity extends BaseActivity {
	private GridView gridView = null;
	private CatalogAdapter adapter = null;
	private String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "." };

	private TextView tv_show_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputmoney);

		tv_show_money = (TextView) findViewById(R.id.tv_show_money);
		gridView = (GridView) findViewById(R.id.gridveiw);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new CatalogAdapter(this);
		gridView.setAdapter(adapter);

		RelativeLayout layout_swip = (RelativeLayout) findViewById(R.id.layout_swip);
		layout_swip.setOnClickListener(listener);

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
				holder.btn_num.setTag(1000 + position);
				holder.btn_num.setOnClickListener(listener);
				convertView.setTag(holder);

			} else {
				holder = (CatalogHolder) convertView.getTag();
			}

			holder.btn_num.setText(num[position]);

			return convertView;
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.layout_swip) {
				if(tv_show_money.getText().toString().equals("0") || tv_show_money.getText().toString().equals("0.0") || tv_show_money.getText().toString().equals("0.0")){
					Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额不能为空", 2);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();
				}else{
					Intent intent = new Intent(InputMoneyActivity.this, SearchAndSwipeActivity.class);
					
					intent.putExtra("TYPE", TransferRequestTag.Consume);
					intent.putExtra("TRANCODE", "199005");
					intent.putExtra("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(InputMoneyActivity.this).getString(Constants.kUSERNAME, ""));
					intent.putExtra("PCSIM", "获取不到");
					intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());
					intent.putExtra("CTXNAT", StringUtil.amount2String(tv_show_money.getText().toString()));
					intent.putExtra("CRDNO", "");
					intent.putExtra("CHECKX", "0.0");
					intent.putExtra("APPTOKEN", "APPTOKEN");
					intent.putExtra("TTXNTM", DateUtil.getSystemTime());
					intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());
					
					startActivityForResult(intent, 0);					
				}
				
			} else {
				String tmp = "";
				String tv_str = tv_show_money.getText().toString();
				switch ((Integer) arg0.getTag()) {
				case 1000:
				case 1001:
				case 1002:
				case 1003:
				case 1004:
				case 1005:
				case 1006:
				case 1007:
				case 1008:
					if (tv_str.length() > 11) {
						break;
					}

					if (tv_show_money.getText().toString().contains(".")) {
						int index = tv_str.indexOf(".");
						if (tv_str.length() - index == 3) {
							break;
						}
					}
					tmp = (Integer) arg0.getTag() - 1000 + 1 + "";
					if (tv_str.length() == 1 && tv_str.equals("0")) {
						tv_show_money.setText("");
					}
					tv_show_money.setText(tv_show_money.getText() + tmp);
					break;
					
				case 1009: // 删除

					if (tv_str.length() == 1) {
						tv_show_money.setText("0");

					} else {
						tv_show_money.setText(tv_str.toString().substring(0, tv_str.length() - 1));
					}
					break;
					
				case 1010: // 0
					if (tv_str.length() > 11 || tv_str.equals("0") || tv_str.equals("0.0") || tv_str.equals("0.00")) {
						break;
					}

					if (tv_show_money.getText().toString().contains(".")) {
						int index = tv_str.indexOf(".");
						if (tv_str.length() - index == 3) {
							break;
						}
					}
					tv_show_money.setText(tv_show_money.getText() + "0");
					break;

				case 1011: // dot
					if (tv_str.length() > 11) {
						break;
					}

					if (tv_show_money.getText().toString().contains(".")) {

					} else {
						tv_show_money.setText(tv_str + ".");
					}
					break;
					
				default:
					break;
				}
			}

		}

	};

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$/g");
		return pattern.matcher(str).matches();
	}
}
