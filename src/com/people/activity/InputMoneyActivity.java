package com.people.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.AppDataCenter;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.model.RateModel;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.DateUtil;
import com.people.util.StringUtil;
import com.people.view.LKAlertDialog;

public class InputMoneyActivity extends BaseActivity implements SensorEventListener {
	private GridView gridView = null;
	private CatalogAdapter adapter = null;
	private String[] num = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "." };

	private TextView tv_show_money;
	private TextView tv_tip;
	private ImageView iv_hand;
	private RelativeLayout layout_swip;

	private long exitTimeMillis = 0;
	private long common_btnn = 0;
	private ArrayList<RateModel> rateList;
	private ArrayList<String> rates = new ArrayList<String>();

	Button btn_calculator;
	private SensorManager sensorManager = null;
	// 速度阈值，当摇晃速度达到这值后产生作用  
    private static final int SPEED_SHRESHOLD = 350;  
    // 两次检测的时间间隔  
    private static final int UPTATE_INTERVAL_TIME = 70; 
 // 手机上一个位置时重力感应坐标  
    private float lastX;  
    private float lastY;  
    private float lastZ;  
    // 上次检测时间  
    private long lastUpdateTime;  
    
	private Boolean rateShowed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputmoney);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		tv_show_money = (TextView) findViewById(R.id.tv_show_money);
		iv_hand = (ImageView) findViewById(R.id.iv_hand);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		
		gridView = (GridView) findViewById(R.id.gridveiw);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new CatalogAdapter(this);
		gridView.setAdapter(adapter);

		layout_swip = (RelativeLayout) findViewById(R.id.layout_swip);
		layout_swip.setOnClickListener(listener);

		Button btn_cash = (Button) findViewById(R.id.btn_cash);
		btn_cash.setOnClickListener(listener);
		
		Button btn_help = (Button) findViewById(R.id.btn_help);
		btn_help.setOnClickListener(listener);

		btn_calculator = (Button) findViewById(R.id.btn_calculator);
		btn_calculator.setOnClickListener(listener);
		
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	
	public final class CatalogHolder {
		public Button btn_num;
	}

	public class CatalogAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CatalogAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return num.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			CatalogHolder holder = null;

			if (null == convertView) {
				convertView = this.mInflater.inflate(R.layout.item_inputmoney, null);
				holder = new CatalogHolder();

				holder.btn_num = (Button) convertView.findViewById(R.id.btn_num);
				holder.btn_num.setTag(1000 + position);
				holder.btn_num.setOnClickListener(listener);
				holder.btn_num.setOnTouchListener(touchLister);
				holder.btn_num.setOnLongClickListener(longListener);
				convertView.setTag(holder);

			} else {
				holder = (CatalogHolder) convertView.getTag();
			}

			holder.btn_num.setText(num[position]);
			if (position == 9) {
				holder.btn_num.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.orange));
			}

			return convertView;
		}
	}

	private OnTouchListener touchLister = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			Button btn = (Button) arg0;

			if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {

				btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.blue_1));
			} else {
				if ((Integer) arg0.getTag() == 1009) {
					btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.orange));
				} else {
					btn.setTextColor(InputMoneyActivity.this.getResources().getColor(R.color.gray_1));
				}

			}
			return false;
		}
	};

	private OnLongClickListener longListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			switch ((Integer) (arg0.getTag())) {
			case 1009:// 长按删除
				tv_show_money.setText("0");
				break;

			default:
				break;
			}
			return false;
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.layout_swip) { // 点击刷卡
				if ((System.nanoTime() - common_btnn) / 1000000 < 1000) {
					return;
				}
				common_btnn = System.nanoTime();

				layout_swip.startAnimation(AnimationUtils.loadAnimation(InputMoneyActivity.this, R.anim.inputmoney_anim));

				if (String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString().replace(",", ""))).equals("0.00")) {
					Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额无效", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();

				} else {
					if(!rateShowed){
						rateAction();
						rateShowed = true;
					}
					
				}

			} else if (arg0.getId() == R.id.btn_cash) { // 现金记账
				if (String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString().replace(",", ""))).equals("0.00")) {
					Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额无效", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
					toast.show();

				} else {
					cashCharge();

				}

			} else if (arg0.getId() == R.id.btn_help) {
				Intent intent_h = new Intent(InputMoneyActivity.this, RateInstructionActivity.class);
				startActivity(intent_h);
			} else if (arg0.getId() == R.id.btn_calculator) {
				Animation myAnimation = AnimationUtils.loadAnimation(InputMoneyActivity.this, R.anim.calculator_scale_anim);
				btn_calculator.startAnimation(myAnimation);
				Intent intentc = new Intent(InputMoneyActivity.this, CalculatorActivity.class);
				startActivityForResult(intentc, 0);

			} else {
				String tmp = "";
				String tv_str = tv_show_money.getText().toString().replace(",", "");
				switch ((Integer) arg0.getTag()) {
				case 1000:
				case 1001:
				case 1002:
				case 1003:
				case 1004:
				case 1005:
				case 1006:
				case 1007:
				case 1008:
					ivHandAnimation();
					String temp = String.format("%1$.2f", Double.valueOf(tv_str));
					if (temp.length() > 10) {
						break;
					}

					if (tv_str.contains(".")) {
						int index = tv_str.indexOf(".");
						if (tv_str.length() - index == 3) {
							break;
						}
					}
					tmp = (Integer) arg0.getTag() - 1000 + 1 + "";
					if (tv_str.length() == 1 && tv_str.equals("0")) {
						tv_show_money.setText("");
					}
					String t = StringUtil.addCommaDouble(Double.valueOf(tv_str + tmp));
					tv_show_money.setText(t);
					break;

				case 1009: // 删除

					if (tv_str.length() == 1) {
						iv_hand.clearAnimation();
						iv_hand.setVisibility(View.GONE);
						tv_show_money.setText("0");

					} else {
						String t9 = "";
						if (tv_str.contains(".")) {
							t9 = tv_show_money.getText().toString().substring(0, tv_show_money.getText().toString().length() - 1);
						} else {
							t9 = StringUtil.addCommaDouble(Double.valueOf(tv_str.toString().substring(0, tv_str.length() - 1)));
						}
						tv_show_money.setText(t9);
					}
					break;

				case 1010: // 0
					String temp0 = String.format("%1$.2f", Double.valueOf(tv_str));
					if (temp0.length() > 10 || tv_str.equals("0") || tv_str.equals("0.0") || tv_str.equals("0.00")) {
						break;
					}

					String t0 = "";
					if (tv_str.contains(".")) {
						int index = tv_str.indexOf(".");
						if (tv_str.length() - index == 3) {
							break;
						}
						t0 = tv_show_money.getText().toString() + "0";
					} else {
						t0 = StringUtil.addCommaDouble(Double.valueOf(tv_str + "0"));
					}
					tv_show_money.setText(t0);
					break;

				case 1011: // dot
					if (tv_str.length() > 11) {
						break;
					}

					if (tv_str.contains(".")) {

					} else {
						tv_show_money.setText(tv_show_money.getText().toString() + ".");
					}
					break;

				default:
					break;
				}
			}

		}

	};

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$/g");
		return pattern.matcher(str).matches();
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
				if (list != null) {
					for (BaseActivity activity : list) {
						activity.finish();
					}
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 记账
	private void cashCharge() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("transType", "01");
		tempMap.put("curType", "CNY");
		tempMap.put("transAmt", tv_show_money.getText().toString().replace(",", ""));
		tempMap.put("phoneNum", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.CashCharge, tempMap, cashChargeHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在记账...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}

	private LKAsyncHttpResponseHandler cashChargeHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("000000")) {

						LKAlertDialog dialog = new LKAlertDialog(InputMoneyActivity.this);
						dialog.setTitle("提示");
						dialog.setMessage("记账成功！金额： ￥" + tv_show_money.getText().toString());
						dialog.setCancelable(false);
						dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
								tv_show_money.setText("0");
							}
						});

						dialog.create().show();
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}

	// 扣率
	private void rateAction() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199038");
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.RateType, tempMap, rateHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(null, new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

			}

		});
	}

	private LKAsyncHttpResponseHandler rateHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public void successAction(Object obj) {
				if (obj instanceof HashMap) {
					if (((HashMap) obj).get("RSPCOD").toString().equals("00")) {

						rateList = (ArrayList<RateModel>) ((HashMap) obj).get("list");
						showListDialog();
						
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// super.onActivityResult(requestCode, resultCode, data);
		rateShowed = false;
		if (resultCode == RESULT_OK) {
			Bundle b = data.getExtras(); // data为B中回传的Intent
			String str = b.getString("amount");
			tv_show_money.setText(str);
		}else {
			iv_hand.clearAnimation();
			iv_hand.setVisibility(View.GONE);
		}
	}

	private void showListDialog()

	{
		rates.clear();
		
		for(int i = 0; i<rateList.size(); i++){
			rates.add(rateList.get(i).getIDFCHANNEL());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface arg0) {
				rateShowed = false;
				
			}
		});
		builder.setTitle("请选择扣率");
		builder.setItems(rates.toArray(new CharSequence[rates.size()]), new DialogInterface.OnClickListener()

		{

			@Override
			public void onClick(DialogInterface dialog, int which)

			{
				rateShowed = false;
				String selectType = rateList.get(which).getIDFID();
				swipAction(selectType);
			}

		});

		builder.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				 if (arg1 == KeyEvent.KEYCODE_BACK  
	                        && arg2.getRepeatCount() == 0) {  
					 rateShowed = false;	                }  
	                return false;
			}
			
		});
		builder.create().show();

	}
	
	private void swipAction(String selectType){
		Intent intent = new Intent(InputMoneyActivity.this, SearchAndSwipeActivity.class);

		intent.putExtra("TYPE", TransferRequestTag.Consume);
		intent.putExtra("TRANCODE", "199053");
		intent.putExtra("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(InputMoneyActivity.this).getString(Constants.kUSERNAME, ""));
		intent.putExtra("PCSIM", "获取不到");
		intent.putExtra("TSEQNO", AppDataCenter.getTraceAuditNum());
		intent.putExtra("CTXNAT", StringUtil.amount2String(String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString().replace(",", "")))));
		intent.putExtra("CRDNO", "");
		intent.putExtra("CHECKX", "0.0");
		intent.putExtra("CHECKY", "0.0");
		intent.putExtra("APPTOKEN", "APPTOKEN");
		intent.putExtra("IDFID", selectType);
		intent.putExtra("TTXNTM", DateUtil.getSystemTime());
		intent.putExtra("TTXNDT", DateUtil.getSystemMonthDay());

		startActivityForResult(intent, 10);
		tv_show_money.setText("0");
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// 现在检测时间  
        long currentUpdateTime = System.currentTimeMillis();  
        // 两次检测的时间间隔  
        long timeInterval = currentUpdateTime - lastUpdateTime;  
        // 判断是否达到了检测时间间隔  
        if (timeInterval < UPTATE_INTERVAL_TIME)  
            return;  
        // 现在的时间变成last时间  
        lastUpdateTime = currentUpdateTime;  
  
        // 获得x,y,z坐标  
        float x = arg0.values[0];  
        float y = arg0.values[1];  
        float z = arg0.values[2];  
  
        // 获得x,y,z的变化值  
        float deltaX = x - lastX;  
        float deltaY = y - lastY;  
        float deltaZ = z - lastZ;  
  
        // 将现在的坐标变成last坐标  
        lastX = x;  
        lastY = y;  
        lastZ = z;  
  
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ  
                * deltaZ)  
                / timeInterval * 10000;  
        // 达到速度阀值，发出提示  
        if (speed >= SPEED_SHRESHOLD) { 
        	if (String.format("%1$.2f", Double.valueOf(tv_show_money.getText().toString().replace(",", ""))).equals("0.00")) {
				Toast toast = Toast.makeText(InputMoneyActivity.this, "输入金额无效", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
				toast.show();

			} else {
				if(!rateShowed){
					rateAction();	
					rateShowed = true;
				}
				
			}
        }
		
	}
	
	private void ivHandAnimation(){
		iv_hand.setVisibility(View.VISIBLE);
		Animation myAnimation1 = AnimationUtils.loadAnimation(this, R.anim.swip_scale_anim);
		LinearInterpolator lir1 = new LinearInterpolator();
		myAnimation1.setInterpolator(lir1);
		iv_hand.startAnimation(myAnimation1);
	}
}
