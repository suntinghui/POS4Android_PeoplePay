package com.people.activity;

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

import com.people.R;
import com.people.view.LKAlertDialog;

// 卡卡转账
public class CardCardActivity extends BaseActivity implements OnClickListener {
	private EditText et_in_name;
	private EditText et_out_name;
	private EditText et_papers_num;
	private Spinner spinner;
	private EditText et_card_num;
	private EditText et_amount;
	private EditText et_phone;
	private String[] scope = {"身份证","军官证","护照","回乡证","台胞证","警官证","士兵证","其他证件类型"};
	private int positon = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardcard);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		
		et_in_name = (EditText) findViewById(R.id.et_in_name);
		et_out_name = (EditText) findViewById(R.id.et_out_name);
		et_papers_num = (EditText) findViewById(R.id.et_papers_num);
		et_card_num = (EditText) findViewById(R.id.et_card_num);
		et_amount = (EditText) findViewById(R.id.et_amount);
		et_phone = (EditText) findViewById(R.id.et_phone);
		
		Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.papers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    	positon = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
		
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
				
				LKAlertDialog dialog = new LKAlertDialog(this);
				dialog.setTitle("提示");
				dialog.setMessage("收款银行卡卡号\n"+et_card_num.getText().toString()+"\n转账金额		"+et_amount.getText().toString()+"元");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
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
