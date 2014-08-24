package com.people.activity;

import java.util.HashMap;

import android.content.DialogInterface;
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
import com.people.util.DateUtil;
import com.people.view.LKAlertDialog;

// 我的账户
public class MyAccountActivity extends BaseActivity implements OnClickListener {
	TextView tv_balance;
	EditText et_amount;
	String amount;
	String ACSTATUS;
	Button btn_nomal;
	Button btn_fast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		btn_nomal = (Button) this.findViewById(R.id.btn_nomal);
		btn_nomal.setOnClickListener(this);
		btn_fast = (Button) this.findViewById(R.id.btn_fast);
		btn_fast.setOnClickListener(this);

		tv_balance = (TextView) this.findViewById(R.id.tv_balance);
		et_amount = (EditText) findViewById(R.id.et_amount);
		
		myAccount();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_nomal:
			if(ACSTATUS.equals("2")){
				Toast.makeText(this, "账户已冻结", Toast.LENGTH_SHORT).show();
			}else{
				if (checkValueNomal()) {

					String PAYDATE = DateUtil.getSystemDate2();

					Intent intent_n = new Intent(MyAccountActivity.this, WithdrawalCashActivity.class);
					intent_n.putExtra("PAYAMT", et_amount.getText().toString());
					intent_n.putExtra("PAYTYPE", "2");
					intent_n.putExtra("PAYDATE", PAYDATE);
					startActivity(intent_n);
				}
			}
			

			break;
		case R.id.btn_fast:
			if(ACSTATUS.equals("2")){
				Toast.makeText(this, "账户已冻结", Toast.LENGTH_SHORT).show();
			}else{
				if (checkValueFast()) {
					String PAYDATE = DateUtil.getSystemDate2();

					Intent intent_n = new Intent(MyAccountActivity.this, WithdrawalCashActivity.class);
					intent_n.putExtra("PAYAMT", et_amount.getText().toString());
					intent_n.putExtra("PAYTYPE", "1");
					intent_n.putExtra("PAYDATE", PAYDATE);
					startActivityForResult(intent_n, 10);
				}
			}
			

			break;
		
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	private Boolean checkValueNomal() {
		if (et_amount.getText().length() == 0) {
			Toast.makeText(this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (Double.valueOf(et_amount.getText().toString()) < 100) {
			Toast.makeText(this, "提现金额不能低于100", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(amount == null || "".equals(amount) || Double.valueOf(amount) == 0.00 ||Double.valueOf(amount)< Double.valueOf(et_amount.getText().toString())){
			Toast.makeText(this, "账户余额不足", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private Boolean checkValueFast() {
		if (et_amount.getText().length() == 0) {
			Toast.makeText(this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (Integer.valueOf(et_amount.getText().toString()) < 100) {
			Toast.makeText(this, "提现金额不能低于100", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (Double.valueOf(et_amount.getText().toString()) > 10000) {
			Toast.makeText(this, "提现金额不能高于10000", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(amount == null || "".equals(amount) || Double.valueOf(amount) == 0.00 ||Double.valueOf(amount)< Double.valueOf(et_amount.getText().toString())){
			Toast.makeText(this, "账户余额不足", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
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
					amount =  map.get("CASHACBAL");
					tv_balance.setText("￥" + map.get("CASHACBAL"));
					ACSTATUS = map.get("ACSTATUS");
					if(ACSTATUS.equals("2")){
						btn_nomal.setEnabled(false);
						btn_fast.setEnabled(false);
						
						LKAlertDialog dialog = new LKAlertDialog(MyAccountActivity.this);
						dialog.setTitle("提示");
						dialog.setMessage("账户已冻结");
						dialog.setCancelable(false);
						dialog.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
						});

						dialog.create().show();
					}

				} else {
					Toast.makeText(MyAccountActivity.this, map.get("RSPMSG"), Toast.LENGTH_SHORT).show();
				}
			}

		};
	}
}
