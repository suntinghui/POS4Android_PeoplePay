package com.people.activity;

import java.util.HashMap;

import android.content.DialogInterface;
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
import com.people.view.LKAlertDialog;

public class EIDMobileChargeActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	private EditText et_phone;

	private Spinner spinner = null;

	private String[] amount = { "50", "100", "200", "300" };
	private String currentAmount = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_eid_recharge);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_eid_confirm = (Button) findViewById(R.id.btn_eid_confirm);
		btn_eid_confirm.setOnClickListener(this);
		Button btn_phone = (Button) findViewById(R.id.btn_phone);
		btn_phone.setOnClickListener(this);

		currentAmount = amount[0];
		et_phone = (EditText) findViewById(R.id.et_phone);

		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.amount, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		currentAmount = amount[arg2];
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 110 && null != data) {
			Bundle bundle = data.getExtras();
			String str = bundle.getString("phoneNumber");
			if (null != str) {
				et_phone.setText(str);
			}
		}
		if(requestCode == 100){
			if(resultCode == RESULT_OK){
				finish();
			}else if(resultCode == RESULT_CANCELED){
				
			}
		}

	}

	private boolean checkValue() {
		if ("".equals(et_phone.getText().toString())) {
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
		case R.id.btn_eid_confirm:
			if (checkValue()) {
				LKAlertDialog dialog = new LKAlertDialog(this);
				dialog.setTitle("提示");
				dialog.setMessage("手机号		" +et_phone.getText().toString()+"\n充值金额		"+currentAmount+"元"+"\n实际支付元  "+Integer.valueOf(currentAmount)*0.95+"元");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						rechargeAction();
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dialog.create().show();
			}
			break;
		case R.id.btn_phone:
			Intent intent_p = new Intent(EIDMobileChargeActivity.this, ContactsActivity.class);
			startActivityForResult(intent_p, 110);
			break;
		}
	}

	// 充值
	private void rechargeAction(){
		Intent intent = new Intent(this, EIDSwipeActivity.class);
		startActivityForResult(intent, 100);
	}

}