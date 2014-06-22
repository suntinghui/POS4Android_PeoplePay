package com.people.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.DownloadFileRequest;
import com.people.view.LKAlertDialog;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private ListView listView = null;
	private Adapter adapter = null;

	private Integer[] imageIds = {R.drawable.set_icon_0, R.drawable.set_icon_1, R.drawable.set_icon_2, R.drawable.set_icon_3, R.drawable.set_icon_4 };

	private String[] titles = { "修改密码", "关于系统", "意见反馈", "检查更新", "帮助" };
	private ImageButton ibtn_gesture;
	private Boolean isOpen = false;

	private String downloadAPKURL;
	private int serviceVersion;
	private String descrition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		isOpen = ApplicationEnvironment.getInstance().getPreferences(this).getBoolean(Constants.kGESTRUECLOSE, false);

		ibtn_gesture = (ImageButton) findViewById(R.id.ibtn_gesture);
		ibtn_gesture.setOnClickListener(this);
		if (isOpen) {
			ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_on);
		} else {
			ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_off);
		}

		// 手势
		LinearLayout layout_gesture = (LinearLayout) findViewById(R.id.layout_gesture);
		layout_gesture.setOnClickListener(this);

		// POS链接方式
		LinearLayout layout_pos = (LinearLayout) findViewById(R.id.layout_pos);
		layout_pos.setOnClickListener(this);

		// 其他
		listView = (ListView) this.findViewById(R.id.listview);
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		adapter = new Adapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				
				case 0: // 修改密码
					Intent intent0 = new Intent(SettingActivity.this, ModifyLoginPwdActivity.class);
					startActivity(intent0);
					break;
				
				case 1: // 关于系统
					Intent intent1 = new Intent(SettingActivity.this, AboutSystemActivity.class);
					SettingActivity.this.startActivity(intent1);
					break;
					
				case 2: // 意见反馈
					Intent intent2 = new Intent(SettingActivity.this, FeedBackActivity.class);
					SettingActivity.this.startActivity(intent2);
					break;
					
				case 3: // 检查更新
					checkUpdate();
					break;
					
				case 4: // 帮助
					Intent intent4 = new Intent(SettingActivity.this, HelpActivity.class);
					SettingActivity.this.startActivity(intent4);
					break;
					
				default:
					break;

				}
			}

		});
	}

	private void checkUpdate() {
		try {
			this.showDialog(BaseActivity.PROGRESS_DIALOG, "正在检查更新");

			URL myURL = new URL(Constants.DOWNLOADURL);
			URLConnection ucon = myURL.openConnection();
			ucon.setConnectTimeout(20000);
			ucon.setReadTimeout(20000);
			InputStream is = ucon.getInputStream();

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						this.hideDialog(BaseActivity.PROGRESS_DIALOG);

						serviceVersion = Integer.parseInt(parser.nextText());

					} else if ("url".equals(parser.getName())) {
						downloadAPKURL = parser.nextText();
					} else if ("des".equals(parser.getName())) {
						descrition = parser.nextText();
						if (descrition == null || descrition.trim().equals("")) {
							descrition = "发现新版本，是否立即更新？";
						}
					}

					break;

				case XmlPullParser.END_TAG:
					if ("root".equals(parser.getName())) {
						if (serviceVersion > Constants.VERSION) {
							showUpdateDialog();
						} else {
							showNoUpdateDialog();
						}
					}
					break;
				}

				event = parser.next();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (IOException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (Exception e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "连接服务器超时，请稍候再试。");

		} finally {
			this.hideDialog(BaseActivity.PROGRESS_DIALOG);
		}
	}

	private void showUpdateDialog() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("有新版本");
		dialog.setMessage(descrition);
		dialog.setCancelable(false);
		dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Update();
			}
		});
		dialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog.create().show();
	}

	private void showNoUpdateDialog() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("当前版本已是最新版本");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	private void Update() {
		DownloadFileRequest.sharedInstance().downloadAndOpen(this, downloadAPKURL, "PeoplePayV" + this.serviceVersion + ".apk");
	}

	public final class ViewHolder {
		public RelativeLayout contentLayout;

		public TextView tv_content;
		public ImageView iv_left;
	}

	public class Adapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public Adapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {

			return titles.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (null == convertView) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item_setting, null);

				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
				holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.iv_left.setBackgroundResource(imageIds[position]);
			holder.tv_content.setText(titles[position]);

			return convertView;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		case R.id.ibtn_gesture:
			isOpen = !isOpen;
			SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
			Editor editor = pre.edit();

			if (!isOpen) {
				editor.putString(Constants.kLOCKKEY, "");
				editor.putBoolean(Constants.kGESTRUECLOSE, false);
			}
			
			editor.commit();

			if (isOpen) {
				ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_on);
				Intent intent = new Intent(SettingActivity.this, LockScreenSettingActivity.class);
				startActivityForResult(intent, 0);
			} else {
				ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_off);
				this.getApplication().stopService(new Intent("com.dhc.timeoutService"));
			}
			break;

		case R.id.layout_gesture:
			if (isOpen) {
				Intent intent = new Intent(SettingActivity.this, LockScreenSettingActivity.class);
				startActivityForResult(intent, 0);
			} else {
				Toast.makeText(SettingActivity.this, "请先开启锁屏手势功能", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.layout_pos:
			Intent intent = new Intent(SettingActivity.this, ChooseQPOSModeActivity.class);
			intent.putExtra("FROM", ChooseQPOSModeActivity.FROM_SETTINGACTIVITY);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			isOpen = data.getBooleanExtra("isOpen", false);
			if (isOpen) {
				ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_on);
			} else {
				ibtn_gesture.setBackgroundResource(R.drawable.btn_toggle_off);
			}

		}
	}

}