package com.people.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.people.R;
import com.people.client.ApplicationEnvironment;
import com.people.client.Constants;
import com.people.client.TransferRequestTag;
import com.people.network.LKAsyncHttpResponseHandler;
import com.people.network.LKHttpRequest;
import com.people.network.LKHttpRequestQueue;
import com.people.network.LKHttpRequestQueueDone;
import com.people.util.AESUtil;
import com.people.util.BitmapUtil;
import com.people.util.MD5Util;
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

	private Bitmap myBitmap;
	private String mImagePath;
	private byte[] mContent;

	private AlertDialog dialog;

	private long exitTimeMillis = 0;
	private TextView tv_head;

	private CircularImage ibtn_head;
	private String bitmap_str = null;

	private String STATUS = "";
	private File sdcardTempFile;
	public static final String IMAGE_UNSPECIFIED = "image/*";

	private String MERNAM = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_merchant);

		sdcardTempFile = new File("/mnt/sdcard/", "head_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
		layout_msg_blow = (LinearLayout) findViewById(R.id.layout_msg_blow);
		layout_msg_blow.setOnClickListener(this);

		RelativeLayout layout_msg_top = (RelativeLayout) findViewById(R.id.layout_msg_top);
		layout_msg_top.setOnClickListener(this);


		RelativeLayout la = (RelativeLayout) this.findViewById(R.id.layout_1);
		la.setOnClickListener(this);
		
		RelativeLayout layout_upload_image = (RelativeLayout) findViewById(R.id.layout_upload_image);
		layout_upload_image.setOnClickListener(this);

		RelativeLayout layout_2 = (RelativeLayout) findViewById(R.id.layout_2);
		layout_2.setOnClickListener(this);

		RelativeLayout layout_3 = (RelativeLayout) findViewById(R.id.layout_3);
		layout_3.setOnClickListener(this);
		
		RelativeLayout layout_qr = (RelativeLayout) findViewById(R.id.layout_qr);
		layout_qr.setOnClickListener(this);

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
			// actionCamera();
			showDialog();
			break;

		case R.id.layout_msg_top:
			isClicked = !isClicked;
			if (isClicked) {
				Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.merchant_anim);
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

		case R.id.layout_1: // 提现
			Intent intent1 = new Intent(MerchantActivity.this, MyAccountActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_qr:
//			Toast.makeText(this, "正在生成二维码，请稍等...", Toast.LENGTH_SHORT).show();
			QRAction();
			break;
		case R.id.layout_upload_image: // 实名认证

			Intent intent0 = new Intent(MerchantActivity.this, UpLoadFirstActivity.class);
			startActivity(intent0);
			break;

		case R.id.layout_2:
			Intent intent2 = new Intent(MerchantActivity.this, SettingActivity.class);
			startActivity(intent2);
			break;

		case R.id.layout_3:
			LKAlertDialog dialog = new LKAlertDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("客服热线：400-616-8529");
			dialog.setCancelable(false);
			dialog.setPositiveButton("拨打", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006168529"));
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

	private void QRAction() {
		try {
			
			String AESValue = "";
			String contentString = ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, "") + "|" + MERNAM;
			AESValue = AESUtil.encryptString(contentString, MD5Util.MD5Crypto(Constants.AESKEY));
			if (!contentString.equals("")) {
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(AESValue, 350);
				showQRCode(qrCodeBitmap);
			} else {
				Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT).show();
			}

		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showQRCode(Bitmap bitMap) {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_qr);
		ImageView iv_qr = (ImageView) window.findViewById(R.id.iv_qr);
		iv_qr.setImageBitmap(bitMap);

	}

	public void getData() {

		HashMap<String, Object> tempMap1 = new HashMap<String, Object>();
		tempMap1.put("TRANCODE", "199022");
		tempMap1.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.MerchantQuery, tempMap1, getMerchantQueryHandler());
		HashMap<String, Object> tempMap2 = new HashMap<String, Object>();
		tempMap2.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req2 = new LKHttpRequest(TransferRequestTag.GetDownLoadHead, tempMap2, getDownLoadHeadHandler());
		new LKHttpRequestQueue().addHttpRequest(req1, req2).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {
			@Override
			public void onComplete() {
				super.onComplete();
			}

		});
	}

	private LKAsyncHttpResponseHandler getMerchantQueryHandler() {
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
				MERNAM = (String) map.get("MERNAM");
				STATUS = (String) map.get("STATUS");

				if (RSPCOD.equals("00")) {
					tv_bank_no.setText(ACTNO == null ? "" : StringUtil.formatCardId(StringUtil.formatAccountNo(ACTNO)));
					tv_open_account_name.setText(ACTNAM == null ? "" : ACTNAM);
					tv_open_account_bank.setText(OPNBNK == null ? "" : OPNBNK);
					tv_head.setText(MERNAM == null ? "" : MERNAM);
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
		tempMap.put("HEADIMG", imgToBase64(mImagePath));
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.LoadUpHead, tempMap, getLoadUpHeadHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

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
					Toast.makeText(MerchantActivity.this, "头像设置成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
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

					if ((String) map.get("HEADIMG") != null) {
						ibtn_head.setImageBitmap(BitmapUtil.convertStringToBitmap((String) map.get("HEADIMG")));
					}
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	private void actionCamera() {
		Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(getImageByCamera, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == 1) {
		// Bitmap bm = null;
		// try {
		// Bundle extras = data.getExtras();
		// bm = (Bitmap) extras.get("data");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (bm != null) {
		// bitmap_str = BitmapUtil.bitmaptoBase64(bm);
		// ibtn_head.setImageBitmap(bm);
		// BitmapUtil.saveMyBitmap(bm);
		// loadUpHead();
		//
		// }
		// }

		if (requestCode == 100 || null != data) {

			mImagePath = sdcardTempFile.getAbsolutePath();
			File file = new File(mImagePath);
			myBitmap = decodeFile(file);

		}
		if (requestCode == 101 || null != data) {

			try {
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();// 取数据

				mContent = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = true;
				opt.inDither = false;
				opt.inPurgeable = true;
				opt.inSampleSize = 3;
				// opt.inTempStorage = new byte[12 * 1024];
				opt.inJustDecodeBounds = false;
				// 将字节数组转换为ImageView可调用的Bitmap对象
				myBitmap = getPicFromBytes(mContent, opt);

				mImagePath = getPath(originalUri);
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}

		}

		bitmap_str = BitmapUtil.bitmaptoBase64(myBitmap);
		ibtn_head.setImageBitmap(myBitmap);
		loadUpHead();
	}

	private String getPath(Uri originalUri) {
		String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
		Cursor cursor = this.managedQuery(originalUri, imgs, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(index);
	}

	private void showDialog() {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(this).setItems(new String[] { "相机", "相册" }, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String status = Environment.getExternalStorageState();
						if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
							Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							try {
								sdcardTempFile.createNewFile();
								i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));// 将拍摄的照片信息存到capturefile中
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							startActivityForResult(i, 100);// 用户点击了从照相机获取
						} else {
							showToast("没有SD卡");
						}

					} else {

						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
						startActivityForResult(intent, 101);

					}
				}
			}).create();
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	/*
	 * 压缩图片，避免内存不足报错
	 */
	private Bitmap decodeFile(File f) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > 100 || o.outWidth > 100) {
				scale = (int) Math.pow(2, (int) Math.round(Math.log(100 / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	// 相机调用的方法getPicFromBytes，将字节数组转换为ImageView可调用的Bitmap对象
	public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	// 相机调用readStream，将图片内容解析成字节数组
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	public String imgToBase64(String imgPath) {
		WindowManager windowManager0 = getWindowManager();
		Display display = windowManager0.getDefaultDisplay();
		int height = display.getHeight();
		int width = display.getWidth();

		Bitmap bitmap = null;
		if (imgPath != null && imgPath.length() > 0) {
			FileDescriptor fd;
			try {
				fd = new FileInputStream(imgPath).getFD();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(imgFile, options);
				BitmapFactory.decodeFileDescriptor(fd, null, options);

				int bmpheight = options.outHeight;
				int bmpWidth = options.outWidth;
				int inSampleSize = bmpheight / height > bmpWidth / width ? bmpheight / height : bmpWidth / width;

				if (inSampleSize > 1)
					if (inSampleSize == 2) {// 小米3
						options.inSampleSize = inSampleSize * 6;// 设置缩放比例
					} else if (inSampleSize == 3) {// 华为C8220
						if (height > 800) {
							options.inSampleSize = inSampleSize * 4;
						} else {
							options.inSampleSize = inSampleSize * 2;// 设置缩放比例
						}
					} else if (inSampleSize == 4) {
						options.inSampleSize = inSampleSize * 2;
					} else if (inSampleSize == 5) {
						options.inSampleSize = inSampleSize * 2;
					} else {
						options.inSampleSize = inSampleSize * 2;
					}
				// 这里一定要将其设置回false，因为之前我们将其设置成了true
				// 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(imgPath, options);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
					if (((HashMap) obj).get("RSPCOD").toString().equals("00")) {

						// TODO
					} else if (((HashMap) obj).get("RSPMSG").toString() != null && ((HashMap) obj).get("RSPMSG").toString().length() != 0) {
						Toast.makeText(getApplicationContext(), ((HashMap) obj).get("RSPMSG").toString(), Toast.LENGTH_SHORT).show();
					}
				} else {
				}

			}

		};
	}

	static class EncodingHandler {
		private static final int BLACK = 0xff000000;

		public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		}
	}
}
