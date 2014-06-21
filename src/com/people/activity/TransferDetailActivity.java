package com.people.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.model.TradeModel;
import com.people.util.DateUtil;
import com.people.util.StringUtil;

// 信用卡余额
public class TransferDetailActivity extends BaseActivity implements OnClickListener {
	
	private TradeModel model = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_creditcard_balance);

		model = (TradeModel) this.getIntent().getSerializableExtra("model");

		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		tv_status.setText(model.getStatus());

		TextView tv_money_before = (TextView) findViewById(R.id.tv_money_before);
		TextView tv_money_after = (TextView) findViewById(R.id.tv_money_after);
		String tmp = StringUtil.String2SymbolAmount(model.getTxnamt()).substring(1);
		tv_money_before.setText(tmp.substring(0, tmp.length()-3));
		tv_money_after.setText(tmp.substring(tmp.length()-3));
		
		TextView tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(StringUtil.dateStringFormate(model.getSysDate()));

		TextView tv_account = (TextView) findViewById(R.id.tv_account);
		tv_account.setText(StringUtil.formatCardId(StringUtil.formatAccountNo(model.getCardNo())));

		TextView tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
		tv_merchant_name.setText(model.getMerName());

		TextView tv_flow_num = (TextView) findViewById(R.id.tv_flow_num);
		tv_flow_num.setText(model.getLogNo());

		Button btn_revoke = (Button) findViewById(R.id.btn_revoke);
		btn_revoke.setOnClickListener(this);

		if (model.getTxncd().equals("0200000000") && model.getTxnsts().equalsIgnoreCase("S") && model.getLogDate().equalsIgnoreCase(DateUtil.formatDate2(new Date()))) {
			btn_revoke.setVisibility(View.VISIBLE);
		} else {
			btn_revoke.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_revoke:
			revokeTransfer();
			break;

		default:
			break;
		}
	}

	public void backAction(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
	
	private void revokeTransfer(){
		Intent intent = new Intent(this, SearchAndSwipeActivity.class);
		
		intent.putExtra("TYPE", TransferRequestTag.ConsumeCancel);
		intent.putExtra("TRANCODE", "199006");
		intent.putExtra("LOGNO", model.getLogNo()); // 流水号，撤销唯一凭证
		intent.putExtra("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
		intent.putExtra("PCSIM", "获取不到");
		intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());
		intent.putExtra("CTXNAT", model.getTxnamt());
		intent.putExtra("CRDNO", "");
		intent.putExtra("CHECKX", "0.0");
		intent.putExtra("APPTOKEN", "APPTOKEN");
		intent.putExtra("TTXNTM", DateUtil.getSystemTime());
		intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());
		
		startActivityForResult(intent, 0);
	}

}
