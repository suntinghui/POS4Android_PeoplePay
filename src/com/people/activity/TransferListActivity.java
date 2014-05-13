package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.model.TradeModel;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.StringUtil;

// 流水
public class TransferListActivity extends BaseActivity implements OnClickListener {

	private ListView listView = null;
	private Adapter adapter = null;

	private ArrayList<TradeModel> array = new ArrayList<TradeModel>();

	private TextView tv_totalnum;
	private TextView tv_totalmoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_transfer_list);

		Button btn_refresh = (Button) findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(this);

		tv_totalnum = (TextView) findViewById(R.id.tv_totalnum);
		tv_totalmoney = (TextView) findViewById(R.id.tv_totalmoney);
		listView = (ListView) this.findViewById(R.id.listview);

		adapter = new Adapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(TransferListActivity.this, TransferDetailActivity.class);
				TradeModel model = array.get(arg2);
				intent.putExtra("model", model);
				startActivity(intent);
			}

		});
		queryHistory();

	}

	public final class ViewHolder {
		public LinearLayout contentLayout;

		public TextView tv_date;
		public TextView tv_week;
		public TextView tv_amount;
		public TextView tv_cardnum;
		public TextView tv_revoke;

	}

	public class Adapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public Adapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return array.size();
		}

		public Object getItem(int arg0) {
			return 0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (null == convertView) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.list_item_transfer, null);

				holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.contentLayout);

				holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
				holder.tv_week = (TextView) convertView.findViewById(R.id.tv_week);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tv_cardnum = (TextView) convertView.findViewById(R.id.tv_cardnum);
				holder.tv_revoke = (TextView) convertView.findViewById(R.id.tv_revoke);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			TradeModel model = array.get(position);
			String cardNum = model.getCardNo();
			if (cardNum != null) {
				holder.tv_cardnum.setText(StringUtil.formatAccountNo(cardNum));
			}
			String amount = model.getTxnamt();
			if (amount != null) {
				holder.tv_amount.setText(StringUtil.String2SymbolAmount(amount));
			}
			holder.tv_date.setText(model.getSysDate() == null ? "" : model.getSysDate());
			holder.tv_revoke.setText(model.formatTxnsts());
			return convertView;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_refresh:
			array.clear();
			queryHistory();
			
			break;
		default:
			break;
		}
	}

	// 查询交易明细
	private void queryHistory() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199008");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(TransferListActivity.this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.FlowQuery, tempMap, queryHistoryHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}

	private LKAsyncHttpResponseHandler queryHistoryHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
						float totalAmount = 0;
						array.addAll((ArrayList<TradeModel>) ((HashMap) obj).get("list"));
						for (int i = 0; i < array.size(); i++) {
							TradeModel model = array.get(i);
							String amount = StringUtil.String2SymbolAmount(model.getTxnamt()).substring(1);
							totalAmount += Float.valueOf(amount);
						}
						tv_totalmoney.setText("￥" + totalAmount);
						tv_totalnum.setText(array.size() + "");
						adapter.notifyDataSetChanged();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}
}
