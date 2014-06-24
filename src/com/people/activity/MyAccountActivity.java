package com.people.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;

// 我的账户
public class MyAccountActivity extends BaseActivity implements OnClickListener {
	TextView tv_balance;
	EditText et_amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		Button btn_nomal = (Button) this.findViewById(R.id.btn_nomal);
		btn_nomal.setOnClickListener(this);
		Button btn_fast = (Button) this.findViewById(R.id.btn_fast);
		btn_fast.setOnClickListener(this);

		tv_balance = (TextView) this.findViewById(R.id.tv_balance);
		et_amount = (EditText) findViewById(R.id.et_amount);
		
		myAccount();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fast:
			if (checkValue()) {

			}

			break;
		case R.id.btn_nomal:
			if (checkValue()) {

			}

			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	private Boolean checkValue() {
		if (et_amount.getText().length() == 0) {
			Toast.makeText(this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 5) {
			this.finish();
		}

	}

	// 我的账户
	private void myAccount() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199026");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.MyAccount, tempMap, getMyAccountHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}
		});
	}

	private LKAsyncHttpResponseHandler getMyAccountHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				HashMap<String, String> map = (HashMap<String, String>) obj;
				if (map.get("RSPCOD") != null && map.get("RSPCOD").equals("00")) {

					tv_balance.setText("￥"+map.get("CASHACBAL"));

				} else {
					Toast.makeText(MyAccountActivity.this, map.get("RSPMSG"), Toast.LENGTH_SHORT).show();
				}
			}

		};
	}
}
