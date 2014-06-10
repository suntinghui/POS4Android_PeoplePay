package com.people.activity;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.DateUtil;
import com.people.util.StringUtil;
import com.people.view.LKAlertDialog;

// 手机充值
public class PhoneRechargeActivity extends BaseActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_recharge);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_next = (Button) this.findViewById(R.id.btn_confirm);
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			confirmAction();
			
			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	
	// 充值
		private void confirmAction() {
//			HashMap<String, Object> tempMap = new HashMap<String, Object>();
//			tempMap.put("TRANCODE", "708110");
//
//			LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.PhoneRecharge,
//					tempMap, forgetPwdHandler());
//
//			new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交...",
//					new LKHttpRequestQueueDone() {
//
//						@Override
//						public void onComplete() {
//							super.onComplete();
//
//						}
//
//					});
			
			Intent intent = new Intent(this, SearchAndSwipeActivity.class);

			intent.putExtra("TYPE", TransferRequestTag.PhoneRecharge);
			intent.putExtra("TRANCODE", "708110");
			intent.putExtra("SELLTEL_B", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
			intent.putExtra("phoneNumber_B", "15011302093");
			intent.putExtra("TXNAMT_B", StringUtil.amount2String(String.format("%1$.2f", Double.valueOf("100"))));
			intent.putExtra("CARDNOJLN_B", "123456");
			intent.putExtra("CHECKX_B", "0.0");
			intent.putExtra("POSTYPE_B", "1");
			intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());
			intent.putExtra("CRDNO", "");
			intent.putExtra("CHECKY_B", "0.0");
			intent.putExtra("APPTOKEN", "APPTOKEN");
			intent.putExtra("TTXNTM", DateUtil.getSystemTime());
			intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());

			startActivity(intent);
		}

		
//		public Boolean checkValue(){
//			
//			if(et_phone.getText().length() == 0){
//				Toast.makeText(this, "请输入手机号!", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//			
//			if(et_security_code.getText().length() == 0){
//				Toast.makeText(this, "请输入验证码!", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//			return true;
//		}
}
