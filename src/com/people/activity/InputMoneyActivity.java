package com.people.activity;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.people.qpos.QPOS;
import com.people.util.DateUtil;
import com.people.util.StringUtil;

public class InputMoneyActivity extends BaseActivity {
	private GridView gridView = null;
	private CatalogAdapter adapter = null;
	private String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "." };

	private TextView tv_show_money;
	
	private long exitTimeMillis = 0;

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
		
		Button btn_cash = (Button) findViewById(R.id.btn_cash);
		btn_cash.setOnClickListener(listener);

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
				holder.btn_num.setOnTouchListener(touchLister);
				convertView.setTag(holder);

			} else {
				holder = (CatalogHolder) convertView.getTag();
			}

			holder.btn_num.setText(num[position]);
			if(position == 9){
				holder.btn_num.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.orange));
			}

			return convertView;
		}
	}

	private OnTouchListener touchLister = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			Button btn = (Button) arg0;
			
			if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
				
				btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.blue_1));
            }else{
            	if((Integer) arg0.getTag() == 1009){
					btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.orange));
				}else{
					btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.gray_1));	
				}
            	
            }
			return false;
		}
	};
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.layout_swip) {
				if (String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString())).equals("0.00")) {
					Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额无效", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();
					
				} else {
					Intent intent = new Intent(InputMoneyActivity.this, SearchAndSwipeActivity.class);

					intent.putExtra("TYPE", TransferRequestTag.Consume);
					intent.putExtra("TRANCODE", "199005");
					intent.putExtra("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(InputMoneyActivity.this).getString(Constants.kUSERNAME, ""));
					intent.putExtra("PCSIM", "获取不到");
					intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());
					intent.putExtra("CTXNAT", StringUtil.amount2String(String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString()))));
					intent.putExtra("CRDNO", "");
					intent.putExtra("CHECKX", "0.0");
					intent.putExtra("APPTOKEN", "APPTOKEN");
					intent.putExtra("TTXNTM", DateUtil.getSystemTime());
					intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());

					startActivity(intent);
				}

			} else if(arg0.getId() == R.id.btn_cash) {
				if (String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString())).equals("0.00")) {
					Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额无效", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();
					
				} else {
					Toast toast = Toast.makeText(InputMoneyActivity.this, "现金记账成功", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();
				}
				
			}else {
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
					String temp = String.format("%1$.2f", Double.valueOf(tv_str));
					if (temp.length() > 10) {
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
