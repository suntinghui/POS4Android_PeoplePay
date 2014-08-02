package com.people.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.people.R;
import com.people.view.LKAlertDialog;

//eid
public class EidCheckActivity extends BaseActivity implements OnClickListener {
	EditText et_name;
	EditText et_idcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eid_check);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		et_name = (EditText) findViewById(R.id.et_name);
		et_idcard = (EditText) findViewById(R.id.et_idcard);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
//			if (checkValue()) {
				Toast.makeText(this, "暂未开通", Toast.LENGTH_SHORT).show();
//				LKAlertDialog dialog = new LKAlertDialog(this);
//				dialog.setTitle("提示");
//				dialog.setMessage("EID验证成功！");
//				dialog.setCancelable(false);
//				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int arg1) {
//						dialog.dismiss();
//						EidCheckActivity.this.finish();
//					}
//				});
//
//				dialog.create().show();
//			}

			break;
		case R.id.btn_securitycode:
			if (et_name.getText().toString().length() == 0) {
				Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			} else if (et_idcard.getText().toString().length() == 0) {
				Toast.makeText(this, "身份证号不合法", Toast.LENGTH_SHORT).show();
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

		if (et_name.getText().length() == 0) {
			Toast.makeText(this, "请输入姓名!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (et_idcard.getText().length() == 0) {
			Toast.makeText(this, "请输入身份证号码!", Toast.LENGTH_SHORT).show();
			return false;
		}

		
		return true;
	}


}
