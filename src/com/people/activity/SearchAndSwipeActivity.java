package com.people.activity;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.qpos.QPOS;
import com.people.qpos.ThreadDeviceID;
import com.people.qpos.ThreadSwip_SixPass;
import com.people.util.StringUtil;
import com.people.view.BLDeviceDialog;
import com.people.view.BLDeviceDialog.OnSelectBLListener;

import dspread.voicemodem.CardReader;

public class SearchAndSwipeActivity extends BaseActivity implements OnClickListener {

	private Button bluetoothBtn = null;
	private TextView titleView = null;
	private ImageView animImageView = null;

	private Intent intent = null;

	private AnimationDrawable animaition = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_swipe);

		this.registerReceiver(mQPOSUpdateReceiver, makeUpdateIntentFilter());

		bluetoothBtn = (Button) this.findViewById(R.id.bluetooth_btn);
		bluetoothBtn.setOnClickListener(this);
		if (QPOS.getCardReader().getMode() == CardReader.BLUETOOTHMODE) {
			bluetoothBtn.setVisibility(View.VISIBLE);
		} else {
			bluetoothBtn.setVisibility(View.GONE);
		}

		titleView = (TextView) this.findViewById(R.id.titleView);
		titleView.setText("检测设备");
		animImageView = (ImageView) this.findViewById(R.id.iv_swipe);

		animImageView.setBackgroundResource(R.anim.finddevice);

		animaition = (AnimationDrawable) animImageView.getBackground();
		animaition.setOneShot(false);
		animaition.start();// 启动

		intent = this.getIntent();

		if (!Constants.HASSETBLUETOOTH) {
			showBLDialog();

			Constants.HASSETBLUETOOTH = true;

		} else {
			doAction();
		}
	}

	private void showBLDialog() {
		BLDeviceDialog dialog = new BLDeviceDialog(this);
		dialog.setOnSelectBLListener(selectBLListener);
		dialog.create().show();
	}

	private OnSelectBLListener selectBLListener = new OnSelectBLListener() {
		@Override
		public void onSelect() {
			doAction();
		}
	};

	private void doAction() {
		int type = intent.getIntExtra("TYPE", 0);
		if (type == TransferRequestTag.Consume) {
			new ConsumeAction().doAction();
		} else if (type == TransferRequestTag.ConsumeCancel) {
			new ConsumeCancelAction().doAction();
		}
	}

	public void backAction(View view) {
		this.finish();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.bluetooth_btn) {
			showBLDialog();
		}
	}

	public BroadcastReceiver mQPOSUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (Constants.ACTION_QPOS_CANCEL.equals(action)) {

			} else if (Constants.ACTION_QPOS_STARTSWIPE.equals(action)) {

				titleView.setText("请刷卡");
				animImageView.setBackgroundResource(R.anim.swipcard);

				animaition.setOneShot(false);
				animaition.start();// 启动

			} else if (Constants.ACTION_QPOS_SWIPEDONE.equals(action)) {

			}
		}
	};

	public IntentFilter makeUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_QPOS_CANCEL);
		intentFilter.addAction(Constants.ACTION_QPOS_STARTSWIPE);
		intentFilter.addAction(Constants.ACTION_QPOS_SWIPEDONE);
		return intentFilter;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////

	// 消费
	class ConsumeAction {

		private String tid = "";
		private String pid = "";

		public void doAction() {
			new ThreadDeviceID(getDeviceIDHandler, SearchAndSwipeActivity.this).start();
		}

		private Handler getDeviceIDHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CardReader.SUCCESS:
					HashMap<String, String> map = (HashMap<String, String>) msg.obj;
					tid = map.get("TID");
					pid = map.get("PID");

					String amountStr = StringUtil.String2AmountFloat4QPOS(intent.getStringExtra("CTXNAT")) + "";

					new ThreadSwip_SixPass(swipeHandler, SearchAndSwipeActivity.this, amountStr, getExtraString()).start();

					break;
				}
			}
		};

		private Handler swipeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CardReader.SUCCESS:
					HashMap<String, String> map = (HashMap<String, String>) msg.obj;

					transfer(map);

					break;

				default:
					break;
				}
			}
		};

		private String getExtraString() {
			StringBuffer sb = new StringBuffer();
			sb.append(intent.getStringExtra("TRANCODE"));
			sb.append(intent.getStringExtra("CTXNAT"));
			sb.append(intent.getStringExtra("TSEQNO"));
			sb.append(intent.getStringExtra("TTXNTM"));
			sb.append(intent.getStringExtra("TTXNDT"));
			sb.append(StringUtil.asciiToHex(pid.replace("UN", ""))); // UN201410000046->201410000046

			return sb.toString();
		}

		private void transfer(HashMap<String, String> map) {
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", intent.getStringExtra("TRANCODE"));
			tempMap.put("PHONENUMBER", intent.getStringExtra("PHONENUMBER")); // 手机号
			tempMap.put("TERMINALNUMBER", tid); // 终端号
			tempMap.put("PCSIM", intent.getStringExtra("PCSIM"));
			tempMap.put("TRACK", map.get("CARD"));
			tempMap.put("TSEQNO", intent.getStringExtra("TSEQNO")); // 终端流水号
			tempMap.put("CTXNAT", intent.getStringExtra("CTXNAT")); // 消费金额
			tempMap.put("TPINBLK", map.get("PIN")); // 支付密码???
			tempMap.put("CRDNO", intent.getStringExtra("CRDNO")); // 卡号
			tempMap.put("CHECKX", intent.getStringExtra("CHECKX")); // 横坐标
			tempMap.put("APPTOKEN", intent.getStringExtra("APPTOKEN"));
			tempMap.put("TTXNTM", intent.getStringExtra("TTXNTM")); // 交易时间
			tempMap.put("TTXNDT", intent.getStringExtra("TTXNDT")); // 交易日期
			tempMap.put("PSAMCARDNO", pid); // PSAM卡号 "UN201410000046"
			tempMap.put("MAC", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.Consume, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

				@Override
				public void onComplete() {
					super.onComplete();

				}

			});
		}

		private LKAsyncHttpResponseHandler transferHandler() {
			return new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("rawtypes")
				@Override
				public void successAction(Object obj) {

					if (obj instanceof HashMap) {
						if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
							Toast.makeText(getApplicationContext(), "交易成功", Toast.LENGTH_SHORT).show();
						} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
							Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
						}
					} else {
					}

				}

			};
		}

	}

	// 消费撤销
	class ConsumeCancelAction {

		private String tid = "";
		private String pid = "";

		public void doAction() {
			new ThreadDeviceID(getDeviceIDHandler, SearchAndSwipeActivity.this).start();
			;
		}

		private Handler getDeviceIDHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CardReader.SUCCESS:
					HashMap<String, String> map = (HashMap<String, String>) msg.obj;
					tid = map.get("TID");
					pid = map.get("PID");

					String amountStr = StringUtil.String2AmountFloat4QPOS(intent.getStringExtra("CTXNAT")) + "";

					new ThreadSwip_SixPass(swipeHandler, SearchAndSwipeActivity.this, amountStr, getExtraString()).start();

					break;
				}
			}
		};

		private Handler swipeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CardReader.SUCCESS:
					HashMap<String, String> map = (HashMap<String, String>) msg.obj;

					transfer(map);

					break;

				default:
					break;
				}
			}
		};

		private String getExtraString() {
			StringBuffer sb = new StringBuffer();
			sb.append(intent.getStringExtra("TRANCODE"));
			sb.append(intent.getStringExtra("CTXNAT"));
			sb.append(intent.getStringExtra("TSEQNO"));
			sb.append(intent.getStringExtra("TTXNTM"));
			sb.append(intent.getStringExtra("TTXNDT"));
			sb.append(StringUtil.asciiToHex(pid.replace("UN", ""))); // UN201410000046->201410000046

			return sb.toString();
		}

		private void transfer(HashMap<String, String> map) {
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", intent.getStringExtra("TRANCODE"));
			tempMap.put("LOGNO", intent.getStringExtra("LOGNO")); // 消费撤销唯一凭证
			tempMap.put("PHONENUMBER", intent.getStringExtra("PHONENUMBER")); // 手机号
			tempMap.put("TERMINALNUMBER", tid); // 终端号
			tempMap.put("PCSIM", intent.getStringExtra("PCSIM"));
			tempMap.put("TRACK", map.get("CARD"));
			tempMap.put("TSEQNO", intent.getStringExtra("TSEQNO")); // 终端流水号
			tempMap.put("CTXNAT", intent.getStringExtra("CTXNAT")); // 消费金额
			tempMap.put("TPINBLK", map.get("PIN")); // 支付密码???
			tempMap.put("CRDNO", intent.getStringExtra("CRDNO")); // 卡号
			tempMap.put("CHECKX", intent.getStringExtra("CHECKX")); // 横坐标
			tempMap.put("APPTOKEN", intent.getStringExtra("APPTOKEN"));
			tempMap.put("TTXNTM", intent.getStringExtra("TTXNTM")); // 交易时间
			tempMap.put("TTXNDT", intent.getStringExtra("TTXNDT")); // 交易日期
			tempMap.put("PSAMCARDNO", pid); // PSAM卡号 "UN201410000046"
			tempMap.put("MAC", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.ConsumeCancel, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在提交...", new LKHttpRequestQueueDone() {

				@Override
				public void onComplete() {
					super.onComplete();

				}

			});
		}

		private LKAsyncHttpResponseHandler transferHandler() {
			return new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("rawtypes")
				@Override
				public void successAction(Object obj) {

					if (obj instanceof HashMap) {
						if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {
							Toast.makeText(getApplicationContext(), "交易成功", Toast.LENGTH_SHORT).show();
						} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
							Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
						}
					} else {
					}

				}

			};
		}

	}

}
