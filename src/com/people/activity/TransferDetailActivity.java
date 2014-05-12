package com.people.activity;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.util.Log;
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
		
		String TXNSTS = model.getTxnsts();
		if(TXNSTS.equalsIgnoreCase("S")){
			TXNSTS = "交易成功";
		}else if(TXNSTS.equalsIgnoreCase("R")){
			TXNSTS = "撤销成功";
		}else if(TXNSTS.equalsIgnoreCase("0")){
			TXNSTS = "预计";
		}else if(TXNSTS.equalsIgnoreCase("C")){
			TXNSTS = "冲正";
		}else if(TXNSTS.equalsIgnoreCase("T")){
			TXNSTS = "超时";
		}else if(TXNSTS.equalsIgnoreCase("F")){
			TXNSTS = "失败";
		}else if(TXNSTS.equalsIgnoreCase("E")){
			TXNSTS = "完成";
		}
		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		tv_status.setText(TXNSTS);
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
		
		Button btn_revoke = (Button) findViewById(R.id.btn_revoke);
		btn_revoke.setOnClickListener(this);
		
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
	    String date=sdf.format(new java.util.Date());  
		if(model.getTxnsts().equalsIgnoreCase("S") && model.getSysDate().substring(0, 9).equalsIgnoreCase(date)){
			btn_revoke.setVisibility(View.VISIBLE);
		}else{
			btn_revoke.setVisibility(View.GONE);
		}
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
