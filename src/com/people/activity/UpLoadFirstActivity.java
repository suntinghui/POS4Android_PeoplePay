package com.people.activity;

import java.util.HashMap;

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
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.StringUtil;

// 基本信息
public class UpLoadFirstActivity extends BaseActivity implements OnClickListener {
	private EditText et_name;
	private EditText et_id;
	private EditText et_merchant_name;
	private EditText et_address;
	private EditText et_serial;
	private String[] scope = { "服装","3c家电", "美容化妆、健身养身", "品牌直销", "办公用品印刷", "家居建材家具", "商业服务、成人教育", "生活服务", "箱包皮具服饰", "食品饮料烟酒零售", "文化体育休闲玩意", "杂货超市", "餐饮娱乐、休闲度假", "汽车、自行车", "珠宝工艺、古董花鸟", "彩票充值票务旅游", "药店及医疗服务", "物流、租赁", "公益类" };
	private int positon = 0;
	private Spinner s;
	private HashMap<String, Object> initMap ;
	private ArrayAdapter<CharSequence> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_first);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

		et_name = (EditText) findViewById(R.id.et_apply_name);
		et_id = (EditText) findViewById(R.id.et_id);
		et_merchant_name = (EditText) findViewById(R.id.et_merchant_name);
		et_address = (EditText) findViewById(R.id.et_address);
		et_serial = (EditText) findViewById(R.id.et_serial);

		s = (Spinner) findViewById(R.id.spinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.scope, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		
		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				positon = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		getMsg();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (checkValue()) {
				if(Constants.AuthenticationIsEdit){
					//可编辑
					uploadMsg();
				}else{
					//不可编辑
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("USERNAME", et_name.getText().toString());
					map.put("IDNUMBER", et_id.getText().toString());
					map.put("MERNAME", et_merchant_name.getText().toString());
					map.put("SCOBUS", scope[positon]);
					map.put("MERADDRESS", et_address.getText().toString());
					map.put("TERMID", et_serial.getText().toString());
					
					map.put("BANKUSERNAME", initMap.get("ACTNAM")); //  ACTNAM 开户名
					map.put("BANKAREA", initMap.get("BANKAREA"));
					map.put("BIGBANKCOD", initMap.get("BIGBANKCOD"));
					map.put("BIGBANKNAM", initMap.get("BIGBANKNAM"));
					map.put("BANKNO", initMap.get("BANKNO"));
					map.put("BANKNAM", initMap.get("OPNBNK"));
					map.put("BANKACCOUNT", initMap.get("ACTNO"));
					map.put("PROCOD", initMap.get("PROCOD"));
					
					Intent intent = new Intent(UpLoadFirstActivity.this, UpLoadSecondActivity.class);
					intent.putExtra("map", map);
					startActivityForResult(intent, 10);
				}
				

			}

			break;
		case R.id.btn_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	// 上传基本信息
	private void uploadMsg() {
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("TRANCODE", "P77024");
		map.put("TERMID", et_serial.getText().toString());

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.CheckTermId, map, checkTermIdHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据，请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}

	private LKAsyncHttpResponseHandler checkTermIdHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("00")) {

						toSecondActivity();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}


	//跳转到基本信息界面2
	private void toSecondActivity(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("USERNAME", et_name.getText().toString());
		map.put("IDNUMBER", et_id.getText().toString());
		map.put("MERNAME", et_merchant_name.getText().toString());
		map.put("SCOBUS", scope[positon]);
		map.put("MERADDRESS", et_address.getText().toString());
		map.put("TERMID", et_serial.getText().toString());
		
		// 分两种情况： 1、  状态为6时初始化 无须赋初值   2、状态不为6时 赋初值
		if(Constants.STATUS.equals("6")){
			
		}else{
			map.put("BANKUSERNAME", initMap.get("ACTNAM")); //  ACTNAM 开户名
			map.put("BANKAREA", initMap.get("BANKAREA"));
			map.put("BIGBANKCOD", initMap.get("BIGBANKCOD"));
			map.put("BIGBANKNAM", initMap.get("BIGBANKNAM"));
			if(initMap.get("BANKNO") != null){
				map.put("BANKNO", initMap.get("BANKNO"));	
			}
			
			map.put("BANKNAM", initMap.get("OPNBNK"));
			map.put("BANKACCOUNT", initMap.get("ACTNO"));
			
			if(initMap.get("PROCOD") != null){
				map.put("PROCOD", initMap.get("PROCOD"));
			}
			
		}
		
		
		Intent intent = new Intent(UpLoadFirstActivity.this, UpLoadSecondActivity.class);
		intent.putExtra("map", map);
		startActivityForResult(intent, 106);
	}
	
	// 获取实名认证信息
	private void getMsg() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "P77023");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.GetMsg, tempMap, getMsgHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据，请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}

	private LKAsyncHttpResponseHandler getMsgHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("00") || ((HashMap) obj).get("RSPCOD").toString().equals("000005")) {
						initMap = (HashMap) obj;
						initValue(initMap);
						
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}

	private void initValue(HashMap<String, Object> map){
		Constants.STATUS = (String) map.get("STATUS");
		String MERCNAM = (String) map.get("MERCNAM");
		String BUSNAM = (String) map.get("BUSNAM");
		String CORPORATEIDENTITY = (String) map.get("CORPORATEIDENTITY");
		String SCOBUS = (String) map.get("SCOBUS");
		String TERMNO = (String) map.get("TERMNO");
		String ADDRESS = (String) map.get("ADDRESS");
		
		//Status 认证状态 0开通 1关闭 2审核通过3审核未通过4黑名单5审核中6初始状态7只提交了文本信息
		if(Constants.STATUS.equals("3") || Constants.STATUS.equals("7")){
			Constants.AuthenticationIsEdit = true;
			et_name.setText(BUSNAM);
			et_merchant_name.setText(MERCNAM);
			et_id.setText(CORPORATEIDENTITY);
			for(int i = 0; i<scope.length; i++){
				if(scope[i].equals(SCOBUS)){
					
					s.setSelection(i);
				}
			}
			et_address.setText(ADDRESS);
			et_serial.setText(TERMNO);
			
		}else if(Constants.STATUS.equals("6")){
			Constants.AuthenticationIsEdit = true;
		}else{
			Constants.AuthenticationIsEdit = false;
			et_name.setText(BUSNAM);
			et_merchant_name.setText(MERCNAM);
			et_id.setText(CORPORATEIDENTITY);
			for(int i = 0; i<scope.length; i++){
				if(scope[i].equals(SCOBUS)){
					s.setSelection(i);
				}
			}
			et_address.setText(ADDRESS);
			et_serial.setText(TERMNO);
			
		}
		et_address.setEnabled(Constants.AuthenticationIsEdit);
		et_id.setEnabled(Constants.AuthenticationIsEdit);
		et_merchant_name.setEnabled(Constants.AuthenticationIsEdit);
		et_name.setEnabled(Constants.AuthenticationIsEdit);
		et_serial.setEnabled(Constants.AuthenticationIsEdit);
		s.setEnabled(Constants.AuthenticationIsEdit);
	}
	public Boolean checkValue() {
		if (et_name.getText().length() == 0) {
			Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_id.getText().length() == 0) {
			Toast.makeText(this, "身份证不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_merchant_name.getText().length() == 0) {
			Toast.makeText(this, "商户名称不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_address.getText().length() == 0) {
			Toast.makeText(this, "地址不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (et_serial.getText().length() == 0) {
			Toast.makeText(this, "机器序列号不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.checkIdCard(et_id.getText().toString())) {
			Toast.makeText(this, "身份证号码不合法！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
}
