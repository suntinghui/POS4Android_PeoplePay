package com.people.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.util.DateUtil;
import com.people.util.StringUtil;

public class MobileChargeActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	private EditText et_phone;

	private Spinner spinner = null;

	private String[] amount = { "10", "50", "100", "200", "300" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recharge);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		Button btn_phone = (Button) findViewById(R.id.btn_phone);
		btn_phone.setOnClickListener(this);

		et_phone = (EditText) findViewById(R.id.et_phone);

		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.amount, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != data) {
			Bundle bundle = data.getExtras();
			String str = bundle.getString("phoneNumber");
			if (null != str) {
				et_phone.setText(str);
			}
		}

	}

	private boolean checkValue() {
		if ("".equals(et_phone.getText())) {
			Toast.makeText(this, "请输入要充值手机号码", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_confirm:
			confirmAction();
			if (checkValue()) {
				confirmAction();
			}
			break;
		case R.id.btn_phone:
			Intent intent_p = new Intent(MobileChargeActivity.this, ContactsActivity.class);
			startActivityForResult(intent_p, 100);
			break;
		}
	}

	// 充值
	private void confirmAction() {

		Intent intent = new Intent(this, SearchAndSwipeActivity.class);

		intent.putExtra("TYPE", TransferRequestTag.PhoneRecharge);
		intent.putExtra("TRANCODE", "708110");
		intent.putExtra("SELLTEL_B", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
		intent.putExtra("phoneNumber_B", "15011302093");
		intent.putExtra("TXNAMT_B", StringUtil.amount2String(String.format("%1$.2f", Double.valueOf("100"))));
		intent.putExtra("CHECKX_B", "0.0");
		intent.putExtra("POSTYPE_B", "1");
		intent.putExtra("CHECKY_B", "0.0");
		intent.putExtra("TTXNTM", DateUtil.getSystemTime());
		intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());
		intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());

		startActivity(intent);
	}

}