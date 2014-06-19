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


public class MobileChargeActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {
	
	private EditText et_phone;
	
    private Spinner spinner = null;  
    
    private String[] amount = {"10","50","100","200","300"};
    
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
	   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
               this, R.array.amount, android.R.layout.simple_spinner_item);
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
		if (null != data){
			Bundle bundle = data.getExtras();
		    String str = bundle.getString("phoneNumber");
		    if(null != str){
		    	et_phone.setText(str);
		    }
		}
		
	}
	
	private boolean checkValue(){
		if ("".equals(et_phone.getText())){
			Toast.makeText(this, "请输入要充值手机号码", Toast.LENGTH_SHORT).show();
			return false;
		} 
		
		return true;
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_confirm:
			if(checkValue()){
				
			}
			break;
		case R.id.btn_phone:
			Intent intent_p = new Intent(MobileChargeActivity.this, ContactsActivity.class);
			startActivityForResult(intent_p, 100);
			break;
		}
	}
	
}