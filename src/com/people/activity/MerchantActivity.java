package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.qpos.QPOS;
import com.people.util.StringUtil;
import com.people.view.LKAlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

// 商户
public class MerchantActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layout_msg_blow;
	private Boolean isClicked = false;
	private ImageView iv_pull;

	private TextView tv_bank_no;
	private TextView tv_open_account_name;
	private TextView tv_open_account_bank;
	
	private long exitTimeMillis = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);

		RelativeLayout layout_msg_top = (RelativeLayout) findViewById(R.id.layout_msg_top);
		layout_msg_top.setOnClickListener(this);
		layout_msg_blow = (LinearLayout) findViewById(R.id.layout_msg_blow);

		RelativeLayout layout_modify_pwd = (RelativeLayout) findViewById(R.id.layout_modify_pwd);
		layout_modify_pwd.setOnClickListener(this);

		RelativeLayout layout_more_setting = (RelativeLayout) findViewById(R.id.layout_more_setting);
		layout_more_setting.setOnClickListener(this);

		RelativeLayout layout_connect = (RelativeLayout) findViewById(R.id.layout_connect);
		layout_connect.setOnClickListener(this);

		Button btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);

		iv_pull = (ImageView) findViewById(R.id.iv_pull);

		tv_bank_no = (TextView) findViewById(R.id.tv_bank_no);
		tv_open_account_name = (TextView) findViewById(R.id.tv_open_account_name);
		tv_open_account_bank = (TextView) findViewById(R.id.tv_open_account_bank);
		merchantQuery();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_msg_top:
			isClicked = !isClicked;
			if (isClicked) {
				layout_msg_blow.setVisibility(View.VISIBLE);
				iv_pull.setBackgroundResource(R.drawable.merchant_icon_push);
			} else {
				layout_msg_blow.setVisibility(View.GONE);
				iv_pull.setBackgroundResource(R.drawable.merchant_icon_pull);
			}
			break;
		case R.id.layout_modify_pwd:
			Intent intent1 = new Intent(MerchantActivity.this, ModifyLoginPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_more_setting:
			Intent intent2 = new Intent(MerchantActivity.this, SettingActivity.class);
			startActivity(intent2);
			break;
		case R.id.layout_connect:
			LKAlertDialog dialog = new LKAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("客服热线：4006269987");
			dialog.setCancelable(false);
			dialog.setPositiveButton("拨打", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006269987"));
					startActivity(intent);
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.create().show();
			break;
		case R.id.btn_exit:
			LKAlertDialog dialog1 = new LKAlertDialog(this);
			dialog1.setTitle("提示");
			dialog1.setMessage("你确定要退出吗？");
			dialog1.setCancelable(false);
			dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					MerchantActivity.this.finish();
				}
			});
			dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog1.create().show();
			break;
		default:
			break;
		}

	}

	// 商户信息查询
	private void merchantQuery() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199011");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.MerchantQuery, tempMap, getLoginHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getLoginHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				String ACTNO = (String) map.get("ACTNO");
				String ACTNAM = (String) map.get("ACTNAM");
				String OPNBNK = (String) map.get("OPNBNK");

				if (RSPCOD.equals("000000")) {
					tv_bank_no.setText(ACTNO == null ? "" : StringUtil.formatAccountNo(ACTNO));
					tv_open_account_name.setText(ACTNAM == null ? "" : ACTNAM);
					tv_open_account_bank.setText(OPNBNK == null ? "" : OPNBNK);
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	// 程序退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTimeMillis = System.currentTimeMillis();
			} else {
				ArrayList<BaseActivity> list = BaseActivity.getAllActiveActivity();
				for (BaseActivity activity : list) {
					activity.finish();
				}

				if (QPOS.getCardReader() != null) {
					QPOS.getCardReader().close();
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
