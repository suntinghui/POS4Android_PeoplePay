package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.people.R;
import com.people.model.TradeModel;
import com.people.util.StringUtil;

// 流水详情
public class TransferDetailActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_transfer_detail);
		
		TradeModel model = (TradeModel) this.getIntent().getSerializableExtra("model");
		
		TextView tv_money = (TextView) findViewById(R.id.tv_money);
		TextView tv_date = (TextView) findViewById(R.id.tv_date);
		TextView tv_account = (TextView) findViewById(R.id.tv_account);
		TextView tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
		TextView tv_flow_num = (TextView) findViewById(R.id.tv_flow_num);
		tv_money.setText(StringUtil.String2SymbolAmount(model.getTxnamt()).substring(1));
		tv_date.setText(StringUtil.dateStringFormate(model.getSysDate()));
		tv_account.setText(StringUtil.formatAccountNo(model.getCrdNo()));
		tv_merchant_name.setText(model.getMerName());
		tv_flow_num.setText(model.getTxncd());
	}
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		default:
			break;
		}
	}
	
	public void backAction(View view){
		this.finish();
	}

}
