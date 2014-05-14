package com.people.activity;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
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
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.qpos.QPOS;
import com.people.qpos.ThreadCancel;
import com.people.qpos.ThreadDeviceID;
import com.people.qpos.ThreadSwip_SixPass;
import com.people.qpos.ThreadUpDataKey;
import com.people.util.DateUtil;
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

		animImageView.setBackgroundResource(R.anim.find_device);

		animaition = (AnimationDrawable) animImageView.getBackground();
		animaition.setOneShot(false);
		animaition.start();// 启动

		intent = this.getIntent();

		if (!Constants.HASSETBLUETOOTH && QPOS.getCardReader().getMode() == CardReader.BLUETOOTHMODE) {
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
		String preDate = ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kPRESIGNDATE, "0101");
		String nowDate = DateUtil.getSystemMonthDay();
		if (!preDate.equals(nowDate)) { 
			new Sign().doAction();

		} else {
			int type = intent.getIntExtra("TYPE", 0);

			if (type == TransferRequestTag.Consume) {
				new ConsumeAction().doAction();
				
			} else if (type == TransferRequestTag.ConsumeCancel) {
				new ConsumeCancelAction().doAction();
				
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		this.backAction(null);
	}

	public void backAction(View view) {
		new ThreadCancel(null, this).start();
		
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

				bluetoothBtn.setVisibility(View.GONE);
				titleView.setText("请刷卡");
				animImageView.setBackgroundResource(R.anim.swipcard);

				animaition = (AnimationDrawable) animImageView.getBackground();
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

	// 签到
	class Sign {

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

					signAction();

					break;
				}
			}
		};

		private void signAction() {
			Toast.makeText(SearchAndSwipeActivity.this, "正在签到请稍候...", Toast.LENGTH_LONG).show();
			
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", "199020");
			tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(SearchAndSwipeActivity.this).getString(Constants.kUSERNAME, "")); // 手机号
			tempMap.put("TERMINALNUMBER", tid);
			tempMap.put("PSAMCARDNO", pid);
			tempMap.put("TERMINALSERIANO", AppDataCenter.getTraceAuditNum());
			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.SignIn, tempMap, signHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue(null, new LKHttpRequestQueueDone() {

				@Override
				public void onComplete() {
					super.onComplete();
				}
			});

		}

		private LKAsyncHttpResponseHandler signHandler() {
			return new LKAsyncHttpResponseHandler() {

				@Override
				public void successAction(Object obj) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) obj;
					if (map.get("RSPCOD").equals("00")) { // 签到成功
						String desKey = map.get("ENCRYPTKEY");
						String pinKey = map.get("PINKEY");
						String macKey = map.get("MACKEY");

						new ThreadUpDataKey(mHandler, SearchAndSwipeActivity.this, desKey + pinKey + macKey).start();
						
					} else { // 签到失败
						Toast.makeText(SearchAndSwipeActivity.this, map.get("RSPMSG"), Toast.LENGTH_SHORT).show();
						SearchAndSwipeActivity.this.finish();
					}
					
				}
			};
		}

		private Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CardReader.SUCCESS:
					Editor editor = ApplicationEnvironment.getInstance().getPreferences(SearchAndSwipeActivity.this).edit();
					editor.putString(Constants.kPRESIGNDATE, DateUtil.getSystemMonthDay());
					editor.commit();

					SearchAndSwipeActivity.this.doAction();
					break;
					
				case CardReader.TIMEOUT:
					Toast.makeText(SearchAndSwipeActivity.this, "签到超时，请重试", Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;
					
				case CardReader.FAILURE:
					Toast.makeText(SearchAndSwipeActivity.this, "签到失败，请重试", Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;

				default:

					break;
				}
			}
		};

	}

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
					
				case CardReader.USERCANCEL:
					Toast.makeText(SearchAndSwipeActivity.this, "用户取消操作", Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
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

				@Override
				public void successAction(Object obj) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) obj;
					
					if (map.get("RSPCOD").equals("000000")) {
						Intent intent0 = new Intent(SearchAndSwipeActivity.this, HandSignActivity.class);
						intent0.putExtra("AMOUNT", intent.getStringExtra("CTXNAT"));
						startActivityForResult(intent0, 0);
						
						Toast.makeText(BaseActivity.getTopActivity(), "交易成功", Toast.LENGTH_SHORT).show();
						
					} else if (map.get("RSPMSG").toString() != null) {
						Toast.makeText(BaseActivity.getTopActivity(), map.get("RSPMSG"), Toast.LENGTH_SHORT).show();
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
					
				case CardReader.USERCANCEL:
					Toast.makeText(SearchAndSwipeActivity.this, "用户取消操作", Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
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

				@Override
				public void successAction(Object obj) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) obj;
					if (map.get("RSPCOD").equals("000000")) {
						Intent intent0 = new Intent(SearchAndSwipeActivity.this, HandSignActivity.class);
						intent0.putExtra("AMOUNT", intent.getStringExtra("CTXNAT"));
						startActivityForResult(intent0, 0);
						
					} else if (map.get("RSPMSG") != null ) {
						Toast.makeText(BaseActivity.getTopActivity(), map.get("RSPMSG"), Toast.LENGTH_SHORT).show();
					}
				}

			};
		}

	}

}
