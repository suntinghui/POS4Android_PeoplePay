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

import com.people.R;

// 账户信息
public class EIDUpLoadSecondActivity extends BaseActivity implements OnClickListener {
	private EditText et_address;
	private String[] scope = {"服装","3c家电","美容化妆、健身养身","品牌直销","办公用品印刷","家居建材家具","商业服务、成人教育","生活服务","箱包皮具服饰","食品饮料烟酒零售","文化体育休闲玩意","杂货超市","餐饮娱乐、休闲度假",
			"汽车、自行车","珠宝工艺、古董花鸟","彩票充值票务旅游","药店及医疗服务","物流、租赁","公益类"};
	private int positon = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eid_upload_second);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		
		et_address = (EditText) findViewById(R.id.et_address);
		
		Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.scope, android.R.layout.simple_spinner_item);
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
			Intent intent = new Intent(this, EIDUploadImagesActivity.class);
			startActivityForResult(intent, 10);

			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			this.setResult(RESULT_OK);
			finish();
		}
	}
}
