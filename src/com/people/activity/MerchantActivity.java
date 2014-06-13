package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.BitmapUtil;
import com.people.util.StringUtil;
import com.people.view.CircularImage;
import com.people.view.LKAlertDialog;

// 商户
public class MerchantActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layout_msg_blow;
	private Boolean isClicked = false;
	private ImageView iv_pull;

	private TextView tv_bank_no;
	private TextView tv_open_account_name;
	private TextView tv_open_account_bank;

	private long exitTimeMillis = 0;
	private TextView tv_head;

	private CircularImage ibtn_head;
	private String bitmap_str = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);

		layout_msg_blow = (LinearLayout) findViewById(R.id.layout_msg_blow);
		layout_msg_blow.setOnClickListener(this);
		LinearLayout layout_msg_top = (LinearLayout) findViewById(R.id.layout_msg_top);
		layout_msg_top.setOnClickListener(this);
		LinearLayout layout_1 = (LinearLayout) findViewById(R.id.layout_1);
		layout_1.setOnClickListener(this);
		LinearLayout layout_upload_image = (LinearLayout) findViewById(R.id.layout_upload_image);
		layout_upload_image.setOnClickListener(this);
		
		LinearLayout layout_2 = (LinearLayout) findViewById(R.id.layout_2);
		layout_2.setOnClickListener(this);
		LinearLayout layout_3 = (LinearLayout) findViewById(R.id.layout_3);
		layout_3.setOnClickListener(this);

		Button btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);

		ibtn_head = (CircularImage) findViewById(R.id.ibtn_head);
		ibtn_head.setOnClickListener(this);

		tv_head = (TextView) findViewById(R.id.tv_head);
		iv_pull = (ImageView) findViewById(R.id.iv_pull);

		tv_bank_no = (TextView) findViewById(R.id.tv_bank_no);
		tv_open_account_name = (TextView) findViewById(R.id.tv_open_account_name);
		tv_open_account_bank = (TextView) findViewById(R.id.tv_open_account_bank);

		getData();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ibtn_head:
			// loadUpHead();
			actionCamera();
			break;
		case R.id.layout_msg_top:
			isClicked = !isClicked;
			if (isClicked) {
				Animation myAnimation = AnimationUtils.loadAnimation(this,
						R.anim.merchant_anim);
				layout_msg_blow.startAnimation(myAnimation);
				layout_msg_blow.setVisibility(View.VISIBLE);
				iv_pull.setBackgroundResource(R.drawable.merchant_icon_push);
			} else {
				layout_msg_blow.setVisibility(View.GONE);
				iv_pull.setBackgroundResource(R.drawable.merchant_icon_pull);
			}
			break;
		case R.id.layout_msg_blow:
			getData();
			break;
		case R.id.layout_1:
			Intent intent1 = new Intent(MerchantActivity.this,
					ModifyLoginPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_upload_image:
			Intent intentUpload = new Intent(MerchantActivity.this,
					UploadImagesActivity.class);
			startActivity(intentUpload);
			break;
		case R.id.layout_2:
			Intent intent2 = new Intent(MerchantActivity.this,
					SettingActivity.class);
			startActivity(intent2);
			break;
		case R.id.layout_3:
			LKAlertDialog dialog = new LKAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("客服热线：4006269987");
			dialog.setCancelable(false);
			dialog.setPositiveButton("拨打",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + "4006269987"));
							startActivity(intent);
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
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
			dialog1.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
							MerchantActivity.this.finish();
						}
					});
			dialog1.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
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

	
	public void getData(){
		
		
		HashMap<String, Object> tempMap1 = new HashMap<String, Object>();
		tempMap1.put("TRANCODE", "199011");
		tempMap1.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(
				TransferRequestTag.MerchantQuery, tempMap1, getLoginHandler());
		HashMap<String, Object> tempMap2 = new HashMap<String, Object>();
		tempMap2.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req2 = new LKHttpRequest(TransferRequestTag.GetDownLoadHead,
				tempMap2, getDownLoadHeadHandler());
		new LKHttpRequestQueue().addHttpRequest(req1, req2)
		.executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {
			@Override
			public void onComplete() {
				super.onComplete();
			}

		});
	}
	// 商户信息查询
	private void merchantQuery() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199011");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(
				TransferRequestTag.MerchantQuery, tempMap, getLoginHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...",
				new LKHttpRequestQueueDone() {

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
				String MERNAM = (String) map.get("MERNAM");

				if (RSPCOD.equals("000000")) {
					tv_bank_no.setText(ACTNO == null ? "" : StringUtil
							.formatCardId(StringUtil.formatAccountNo(ACTNO)));
					tv_open_account_name.setText(ACTNAM == null ? "" : ACTNAM);
					tv_open_account_bank.setText(OPNBNK == null ? "" : OPNBNK);
					tv_head.setText(MERNAM == null ? "" : MERNAM);
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG,
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	// 程序退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTimeMillis = System.currentTimeMillis();
			} else {
				ArrayList<BaseActivity> list = BaseActivity
						.getAllActiveActivity();
				for (BaseActivity activity : list) {
					activity.finish();
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 上传图像
	private void loadUpHead() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("HEADIMG", bitmap_str);
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.LoadUpHead,
				tempMap, getLoadUpHeadHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...",
				new LKHttpRequestQueueDone() {

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}

	private LKAsyncHttpResponseHandler getLoadUpHeadHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");

				if (RSPCOD.equals("000000")) {
					Toast.makeText(MerchantActivity.this, "头像设置成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG,
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	// 下载图像
	private void getDownLoadHead() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("HEADIMG", bitmap_str);
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance()
				.getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.GetDownLoadHead,
				tempMap, getDownLoadHeadHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...",
				new LKHttpRequestQueueDone() {

					@Override
					public void onComplete() {
						super.onComplete();
					}
				});
	}

	private LKAsyncHttpResponseHandler getDownLoadHeadHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");
				
				if (RSPCOD.equals("000000")) {
					
					if((String) map.get("HEADIMG") != null){
						ibtn_head.setImageBitmap(BitmapUtil.convertStringToBitmap((String) map.get("HEADIMG")));
					}
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG,
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	private void actionCamera() {
		Intent getImageByCamera = new Intent(
				"android.media.action.IMAGE_CAPTURE");
		startActivityForResult(getImageByCamera, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			Bitmap bm = null;
			try {
				Bundle extras = data.getExtras();
				bm = (Bitmap) extras.get("data");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bm != null) {
				Log.i("bm0:", bm.toString());
				bitmap_str = BitmapUtil.bitmaptoBase64(bm);
				ibtn_head.setImageBitmap(bm);
				BitmapUtil.saveMyBitmap(bm);
				loadUpHead();

			}
		}
	}
	
}
