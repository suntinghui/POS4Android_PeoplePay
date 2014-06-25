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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
	private Button backBtn = null;
	private TextView titleView = null;

	private Intent intent = null;

	private AnimationDrawable animaition = null;

	private RelativeLayout layout_search;
	private RelativeLayout layout_swip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_swipe);

		this.registerReceiver(mQPOSUpdateReceiver, makeUpdateIntentFilter());

		backBtn = (Button) this.findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		bluetoothBtn = (Button) this.findViewById(R.id.bluetooth_btn);
		bluetoothBtn.setOnClickListener(this);
		if (QPOS.getCardReader().getMode() == CardReader.BLUETOOTHMODE) {
			bluetoothBtn.setVisibility(View.VISIBLE);
		} else {
			bluetoothBtn.setVisibility(View.GONE);
		}

		titleView = (TextView) this.findViewById(R.id.titleView);
		titleView.setText("检测设备");

		layout_search = (RelativeLayout) findViewById(R.id.layout_search);
		layout_swip = (RelativeLayout) findViewById(R.id.layout_swip);

		ImageView iv_blue = (ImageView) findViewById(R.id.iv_blue);
		Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.check_device_anim);
		LinearInterpolator lir = new LinearInterpolator();
		myAnimation.setInterpolator(lir);
		iv_blue.startAnimation(myAnimation);

		ImageView iv_device = (ImageView) findViewById(R.id.iv_device);
		Animation myAnimation1 = AnimationUtils.loadAnimation(this, R.anim.swip_scale_anim);
		LinearInterpolator lir1 = new LinearInterpolator();
		myAnimation1.setInterpolator(lir1);
		iv_device.startAnimation(myAnimation1);

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

			} else if (type == TransferRequestTag.PhoneRecharge) {
				new PhoneRechargeAction().doAction();
			} else if (type == TransferRequestTag.CardCard) {
				new CardCardAction().doAction();
			} else if (type == TransferRequestTag.CreditCard) {
				new CreditCardAction().doAction();
			}
		}
	}

	@Override
	public void onBackPressed() {

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
		} else if (view.getId() == R.id.btn_back) {
			this.backAction(null);
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
				layout_search.setVisibility(View.GONE);
				layout_swip.setVisibility(View.VISIBLE);
				ImageView iv_card = (ImageView) findViewById(R.id.iv_card);
				Animation myAnimation0 = AnimationUtils.loadAnimation(SearchAndSwipeActivity.this, R.anim.swip_card_anim);
				LinearInterpolator lir0 = new LinearInterpolator();
				myAnimation0.setInterpolator(lir0);
				iv_card.startAnimation(myAnimation0);

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

	private void gotoTradeFailureActivity(String msg) {
		if (msg == null || msg.trim().equals("")) {
			msg = "交易失败";
		}

		Intent intent = new Intent(this, TradeFaiureActivity.class);
		intent.putExtra("MESSAGE", msg);
		startActivityForResult(intent, 0);
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

				default:
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;
				}
			}
		};

		private void signAction() {
			Toast.makeText(SearchAndSwipeActivity.this, "正在签到请稍候...", Toast.LENGTH_LONG).show();

			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", "199020");
			tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(SearchAndSwipeActivity.this).getString(Constants.kUSERNAME, "")); // 手机号
			tempMap.put("TERMINALNUMBER", tid);// tid
			tempMap.put("PSAMCARDNO", pid);//
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
						gotoTradeFailureActivity(map.get("RSPMSG"));
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

				default:
					gotoTradeFailureActivity((String) msg.obj);
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

				default:
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
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
			tempMap.put("CHECKY", intent.getStringExtra("CHECKY")); // 纵坐标
			tempMap.put("TTXNTM", intent.getStringExtra("TTXNTM")); // 交易时间
			tempMap.put("TTXNDT", intent.getStringExtra("TTXNDT")); // 交易日期
			tempMap.put("PSAMCARDNO", pid); // PSAM卡号 "UN201410000046"
			tempMap.put("MAC", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.Consume, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在交易请稍候...", new LKHttpRequestQueueDone() {

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

					if (map.get("RSPCOD").equals("00")) {
						Intent intent0 = new Intent(SearchAndSwipeActivity.this, HandSignActivity.class);
						intent0.putExtra("AMOUNT", intent.getStringExtra("CTXNAT"));
						intent0.putExtra("LOGNO", map.get("LOGNO"));
						startActivityForResult(intent0, 0);

					} else {
						gotoTradeFailureActivity(map.get("RSPMSG"));
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

				default:
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
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

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在交易，请稍候...", new LKHttpRequestQueueDone() {

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
					if (map.get("RSPCOD").equals("00")) {
						Intent intent0 = new Intent(SearchAndSwipeActivity.this, HandSignActivity.class);
						intent0.putExtra("AMOUNT", intent.getStringExtra("CTXNAT"));
						intent0.putExtra("LOGNO", map.get("LOGNO"));
						startActivityForResult(intent0, 0);

					} else {
						gotoTradeFailureActivity(map.get("RSPMSG"));
					}
				}

			};
		}

	}

	// 手机充值
	class PhoneRechargeAction {
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

					HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
					String amountStr = StringUtil.String2AmountFloat4QPOS(intentMap.get("TXNAMT_B")) + "";

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
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;
				}
			}
		};

		private String getExtraString() {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			StringBuffer sb = new StringBuffer();
			sb.append(intentMap.get("TRANCODE"));
			sb.append(intentMap.get("TXNAMT_B"));
			sb.append(intentMap.get("TSeqNo_B"));
			sb.append(intentMap.get("TTxnTm_B"));
			sb.append(intentMap.get("TTxnDt_B"));
			sb.append(StringUtil.asciiToHex(pid.replace("UN", ""))); // UN201410000046->201410000046

			return sb.toString();
		}

		private void transfer(HashMap<String, String> map) {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", intentMap.get("TRANCODE"));
			tempMap.put("SELLTEL_B", intentMap.get("SELLTEL_B")); // 消费撤销唯一凭证
			
			tempMap.put("phoneNumber_B", intentMap.get("phoneNumber_B"));// 接收信息手机号
			tempMap.put("Track2_B", map.get("CARD"));
			tempMap.put("CRDNOJLN_B", map.get("PIN")); // 支付密码???
			tempMap.put("TXNAMT_B", intentMap.get("TXNAMT_B"));
			tempMap.put("TSeqNo_B", intentMap.get("TSeqNo_B"));
			tempMap.put("TTxnTm_B", intentMap.get("TTxnTm_B"));
			tempMap.put("TTxnDt_B", intentMap.get("TTxnDt_B"));
			
			tempMap.put("POSTYPE_B", intentMap.get("POSTYPE_B"));
//			tempMap.put("RAND_B", "");
			tempMap.put("CHECKX_B", intentMap.get("CHECKX_B"));
			tempMap.put("CHECKY_B", intentMap.get("CHECKY_B"));
			tempMap.put("TERMINALNUMBER_B", pid);
			tempMap.put("MAC_B", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.PhoneRecharge, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在交易，请稍候...", new LKHttpRequestQueueDone() {

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
					if (map.get("RSPCOD") != null && map.get("RSPCOD").equals("00")) {
						Intent intent = new Intent(SearchAndSwipeActivity.this, TradeSuccessActivity.class);
						intent.putExtra("tips", "充值成功");
						startActivityForResult(intent, 102);

					} else {
						gotoTradeFailureActivity(map.get("RSPMSG"));
					}
				}

			};
		}

	}

	// 卡卡转账
	class CardCardAction {
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
					HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
					String amountStr = StringUtil.String2AmountFloat4QPOS(intentMap.get("TXNAMT_B")) + "";

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
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;
				}
			}
		};

		private String getExtraString() {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			StringBuffer sb = new StringBuffer();
			sb.append(intentMap.get("TRANCODE"));
			sb.append(intentMap.get("TXNAMT_B"));
			sb.append(intentMap.get("TSeqNo_B"));
			sb.append(intentMap.get("TTxnTm_B"));
			sb.append(intentMap.get("TTxnDt_B"));
			sb.append(StringUtil.asciiToHex(pid.replace("UN", ""))); // UN201410000046->201410000046

			return sb.toString();
		}

		private void transfer(HashMap<String, String> map) {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", intentMap.get("TRANCODE"));
			tempMap.put("SELLTEL_B", intentMap.get("SELLTEL_B")); // 消费撤销唯一凭证
			tempMap.put("CRDNO1_B", intentMap.get("CARDNO1_B"));// 信用卡卡号
			
			tempMap.put("INCARDNAM_B", intentMap.get("INCARDNAM_B"));
			tempMap.put("OUTCARDNAM_B", intentMap.get("OUTCARDNAM_B"));
			tempMap.put("OUT_IDTYP_B", intentMap.get("OUT_IDTYP_B"));
			tempMap.put("OUT_IDTYPNAM_B", intentMap.get("OUT_IDTYPNAM_B"));
			tempMap.put("OUT_IDCARD_B", intentMap.get("OUT_IDCARD_B"));
			
			tempMap.put("phoneNumber_B", intentMap.get("phoneNumber_B"));// 接收信息手机号
			tempMap.put("Track2_B", map.get("CARD"));
			tempMap.put("CRDNOJLN_B", map.get("PIN")); // 支付密码???
			tempMap.put("TXNAMT_B", intentMap.get("TXNAMT_B"));
			tempMap.put("TSeqNo_B", intentMap.get("TSeqNo_B"));
			tempMap.put("TTxnTm_B", intentMap.get("TTxnTm_B"));
			tempMap.put("TTxnDt_B", intentMap.get("TTxnDt_B"));
			
			tempMap.put("POSTYPE_B", intentMap.get("POSTYPE_B"));
			tempMap.put("RAND_B", "");
			tempMap.put("CHECKX_B", intentMap.get("CHECKX_B"));
			tempMap.put("CHECKY_B", intentMap.get("CHECKY_B"));
			tempMap.put("TERMINALNUMBER_B", pid); // PSAM卡号 "UN201410000046"
			tempMap.put("MAC_B", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.CardCard, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在交易，请稍候...", new LKHttpRequestQueueDone() {

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
					if (map.get("RSPCOD").equals("00")) {
						Intent intent = new Intent(SearchAndSwipeActivity.this, TradeSuccessActivity.class);
						intent.putExtra("tips", "转账成功");
						startActivityForResult(intent, 101);

					} else {
						gotoTradeFailureActivity(map.get("RSPMSG"));
					}
				}

			};
		}

	}

	// 信用卡还款
	class CreditCardAction {
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

					HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
					String amountStr = StringUtil.String2AmountFloat4QPOS(intentMap.get("TXNAMT_B")) + "";

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
					Toast.makeText(SearchAndSwipeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
					SearchAndSwipeActivity.this.finish();
					break;
				}
			}
		};

		private String getExtraString() {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			StringBuffer sb = new StringBuffer();
			sb.append(intentMap.get("TRANCODE"));
			sb.append(intentMap.get("TXNAMT_B"));
			sb.append(intentMap.get("TSeqNo_B"));
			sb.append(intentMap.get("TTxnTm_B"));
			sb.append(intentMap.get("TTxnDt_B"));
			sb.append(StringUtil.asciiToHex(pid.replace("UN", ""))); // UN201410000046->201410000046

			return sb.toString();
		}

		private void transfer(HashMap<String, String> map) {
			HashMap<String, String> intentMap = (HashMap<String, String>) intent.getSerializableExtra("map");
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("TRANCODE", intentMap.get("TRANCODE"));
			tempMap.put("SELLTEL_B", intentMap.get("SELLTEL_B")); // 消费撤销唯一凭证
			tempMap.put("CRDNO1_B", intentMap.get("CARDNO1_B"));// 信用卡卡号
			
			tempMap.put("phoneNumber_B", intentMap.get("phoneNumber_B"));// 接收信息手机号
			tempMap.put("Track2_B", map.get("CARD"));
			tempMap.put("CRDNOJLN_B", map.get("PIN")); // 支付密码???
			tempMap.put("TXNAMT_B", intentMap.get("TXNAMT_B"));
			tempMap.put("TSeqNo_B", intentMap.get("TSeqNo_B"));
			tempMap.put("TTxnTm_B", intentMap.get("TTxnTm_B"));
			tempMap.put("TTxnDt_B", intentMap.get("TTxnDt_B"));
			
			tempMap.put("POSTYPE_B", intentMap.get("POSTYPE_B"));
			tempMap.put("CHECKX_B", intentMap.get("CHECKX_B"));
			tempMap.put("CHECKY_B", intentMap.get("CHECKY_B"));
			tempMap.put("TERMINALNUMBER_B", pid);
			tempMap.put("MAC_B", map.get("MAC")); // MAC

			LKHttpRequest req = new LKHttpRequest(TransferRequestTag.CreditCard, tempMap, transferHandler());

			new LKHttpRequestQueue().addHttpRequest(req).executeQueue("正在交易，请稍候...", new LKHttpRequestQueueDone() {

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
					if (map.get("RSPCOD") != null && map.get("RSPCOD").equals("00")) {
						Intent intent = new Intent(SearchAndSwipeActivity.this, TradeSuccessActivity.class);
						intent.putExtra("tips", "还款成功");
						startActivityForResult(intent, 100);

					} else {
						gotoTradeFailureActivity(map.get("RSPMSG"));
					}
				}

			};
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}
}
