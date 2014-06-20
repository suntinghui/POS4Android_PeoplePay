package com.people.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.people.R;

// 信用卡转账
public class CreditCardActivity extends BaseActivity implements OnClickListener {
	private EditText et_card_num;
	private EditText et_amount;
	private EditText et_phone;
	private int positon = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creditcard);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		
		et_card_num = (EditText) findViewById(R.id.et_card_num);
		et_amount = (EditText) findViewById(R.id.et_amount);
		et_phone = (EditText) findViewById(R.id.et_phone);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			
			if (checkValue()) {
//				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("SELLTEL_B", "SELLTEL_B");//
//				map.put("CARDNO1_B", );//接收转账卡号
//				map.put("phoneNumber_B", );//接收信息手机号
//				map.put("Track2_B", );//磁道信息
//				map.put("CARDNOJLN_B", );//交易密码
//				map.put("TXNAMT_B", );//交易金额
//				map.put("POSTYPE_B", );//POSTYPE_B   1 普通刷卡器 2 小刷卡器
//				map.put("RAND_B", );//RAND_B
//				map.put("CHECKX_B", );//当前经度
//				map.put("CHECKY_B", );//当前纬度
//				map.put("TERMINALNUMBER_B", );//机器 PSAM 号
//				
//				Intent intent = new Intent(CardCardActivity.this, UpLoadSecondActivity.class);
//				intent.putExtra("map", map);
//				startActivity(intent);
				
			}

			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}


	public Boolean checkValue() {
//		if(et_name.getText().length() == 0){
//			Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(et_id.getText().length() == 0){
//			Toast.makeText(this, "身份证不能为空！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(et_merchant_name.getText().length() == 0){
//			Toast.makeText(this, "商户名称不能为空！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(et_address.getText().length() == 0){
//			Toast.makeText(this, "地址不能为空！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(et_serial.getText().length() == 0){
//			Toast.makeText(this, "机器序列号不能为空！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(StringUtil.checkIdCard(et_id.getText().toString())){
//			Toast.makeText(this, "身份证号码不合法！", Toast.LENGTH_SHORT).show();
//			return false;
//		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == 6){
			finish();
		}
	}
}
