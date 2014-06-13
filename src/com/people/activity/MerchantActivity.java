package com.people.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.people.util.AESUtil;
import com.people.util.BitmapUtil;
import com.people.util.MD5Util;
import com.people.util.MyLog;
import com.people.util.StringUtil;
import com.people.view.CircularImage;
import com.people.view.LKAlertDialog;

// 商户
public class MerchantActivity extends BaseActivity implements OnClickListener {
	public static final String IMAGE_UNSPECIFIED = "image/*";

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

	private ImageView iv_top;
	private Boolean isHead = false;

	private AlertDialog dialog;
	private File sdcardTempFile;
	private int crop = 180;

	private String bitmap_zoom;

	private byte[] mContent;
	private Bitmap myBitmap;
	private String mImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);

		iv_top = (ImageView) findViewById(R.id.iv_top);
		iv_top.setOnClickListener(this);

		sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");

		layout_msg_blow = (LinearLayout) findViewById(R.id.layout_msg_blow);
		layout_msg_blow.setOnClickListener(this);
		LinearLayout layout_msg_top = (LinearLayout) findViewById(R.id.layout_msg_top);
		layout_msg_top.setOnClickListener(this);
		LinearLayout layout_1 = (LinearLayout) findViewById(R.id.layout_1);
		layout_1.setOnClickListener(this);
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
			isHead = true;
			actionCamera();
			break;
		case R.id.iv_top:
			isHead = false;
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
		case R.id.layout_1:
			// Intent intent1 = new Intent(MerchantActivity.this,
			// ModifyLoginPwdActivity.class);
			// startActivity(intent1);
			getUpLoadImage();
			break;
		case R.id.layout_2:
			Intent intent2 = new Intent(MerchantActivity.this, SettingActivity.class);
			startActivity(intent2);
			break;
		case R.id.layout_3:
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

	private void showDialog() {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(this).setItems(new String[] { "相机", "相册" }, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						intent.putExtra("output", Uri.fromFile(sdcardTempFile));
						intent.putExtra("crop", "true");
						intent.putExtra("aspectX", 1);// 裁剪框比例
						intent.putExtra("aspectY", 1);
						intent.putExtra("outputX", 80);// 输出图片大小
						intent.putExtra("outputY", 80);
						startActivityForResult(intent, 100);
					} else {
						// Intent intent = new Intent(
						// "android.intent.action.PICK");
						// intent.setDataAndType(
						// MediaStore.Images.Media.INTERNAL_CONTENT_URI,
						// "image/*");
						// intent.putExtra("output",
						// Uri.fromFile(sdcardTempFile));
						// intent.putExtra("crop", "true");
						// intent.putExtra("aspectX", 2);// 裁剪框比例
						// intent.putExtra("aspectY", 1.5);
						// intent.putExtra("outputX", 320);// 输出图片大小
						// intent.putExtra("outputY", 150);
						// startActivityForResult(intent, 101);

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

	public void getData() {

		HashMap<String, Object> tempMap1 = new HashMap<String, Object>();
		tempMap1.put("TRANCODE", "199011");
		tempMap1.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.MerchantQuery, tempMap1, getMerchantHandler());
		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(null, new LKHttpRequestQueueDone() {
			@Override
			public void onComplete() {
				super.onComplete();
			}

		});

		HashMap<String, Object> tempMap2 = new HashMap<String, Object>();
		tempMap2.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req2 = new LKHttpRequest(TransferRequestTag.GetDownLoadHead, tempMap2, getDownLoadHeadHandler());
		new LKHttpRequestQueue().addHttpRequest(req2).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {
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
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.MerchantQuery, tempMap, getMerchantHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getMerchantHandler() {
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

	// 上传头像
	private void loadUpHead() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("HEADIMG", bitmap_str);
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
					Toast.makeText(MerchantActivity.this, "街景设置成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	// 上传街景
	private void loadUpStreet() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("HEADIMG", bitmap_str);
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.LoadUpStreetImg, tempMap, getLoadUpStreetImgHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getLoadUpStreetImgHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				String RSPCOD = (String) map.get("RSPCOD");
				String RSPMSG = (String) map.get("RSPMSG");

				if (RSPCOD.equals("000000")) {
					Toast.makeText(MerchantActivity.this, "街景设置成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MerchantActivity.this, RSPMSG, Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	// 下载图像
	private void getDownLoadHead() {
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("HEADIMG", bitmap_str);
		tempMap.put("PHONENUMBER", ApplicationEnvironment.getInstance().getPreferences(this).getString(Constants.kUSERNAME, ""));

		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.GetDownLoadHead, tempMap, getDownLoadHeadHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在请求数据...", new LKHttpRequestQueueDone() {

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

					if ((String) map.get("HEADIMG") != null) {
						ibtn_head.setImageBitmap(BitmapUtil.convertStringToBitmap((String) map.get("HEADIMG")));
					}
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
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 || requestCode == 101) {
			Bitmap bm = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
			// Bitmap bm = null;
			try {
				Bundle extras = data.getExtras();
				// if(isHead){
				// bm = ImageCrop((Bitmap) extras.get("data"));
				// }else{
				// bm = (Bitmap) extras.get("data");
				// }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if (bm != null) {
			// bitmap_str = BitmapUtil.bitmaptoBase64(bm);
			// bitmap_zoom = BitmapUtil.bitmaptoBase64(bitmapZoom(bm));

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

			// if (isHead) {
			// ibtn_head.setImageBitmap(bm);
			// BitmapUtil.saveMyBitmap(bm);
			// loadUpHead();
			// } else {
			// iv_top.setImageBitmap(bm);
			// loadUpStreet();
			// }

		}
		// }
	}

	private String getPath(Uri originalUri) {
		String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
		Cursor cursor = this.managedQuery(originalUri, imgs, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(index);
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

	/**
	 * 按正方形裁切图片
	 */
	public static Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();

		int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

		int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
		int retY = w > h ? 0 : (h - w) / 2;

		// 下面这句是关键
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}

	// 上传图片
	private void getUpLoadImage() {
		Constants.IMAGEUPLOAD = true;
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199021");
		tempMap.put("PHONENUMBER", "13917662264");//
		tempMap.put("FILETYPE", "MYPIC"); // MYPIC、IDPIC、IDPIC2、CARDPIC
		tempMap.put("PHOTOS", imgToBase64(mImagePath));// bitmap_zoom imgToBase64(mImagePath)
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.UpLoadImage, tempMap, getUpLoadImageHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
				Constants.IMAGEUPLOAD = false;

			}
		});
	}

	private LKAsyncHttpResponseHandler getUpLoadImageHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {

			}

		};
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
				 }
				 else if(inSampleSize == 5){
				 options.inSampleSize = inSampleSize * 2;
				 }
				 else{
				 options.inSampleSize = inSampleSize * 2;
				 }
				options.inSampleSize = 40;
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
			Log.i("image size: ", Base64.encodeToString(imgBytes, Base64.DEFAULT).length() + "");
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

	private Bitmap bitmapZoom(Bitmap bm) {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int height = display.getHeight();
		int width = display.getWidth();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		String path = sdcardTempFile.getPath();
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bm为空
		options.inJustDecodeBounds = false;
		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / height > bmpWidth / width ? bmpheight / height : bmpWidth / width;
		Log.i("inSampleSize:", inSampleSize + "");
		options.inSampleSize = 30;
		// if (inSampleSize > 1)
		// if (inSampleSize == 2) {// 小米3
		// options.inSampleSize = inSampleSize * 6;// 设置缩放比例
		// } else if (inSampleSize == 3) {// 华为C8220
		// if (height > 800) {
		// options.inSampleSize = inSampleSize * 4;
		// } else {
		// options.inSampleSize = inSampleSize * 2;// 设置缩放比例
		// }
		// } else if (inSampleSize == 4) {
		// options.inSampleSize = inSampleSize * 2;
		// }
		// else if(inSampleSize == 5){
		// options.inSampleSize = inSampleSize * 2;
		// }
		// else{
		// options.inSampleSize = inSampleSize * 2;
		// }
		Log.i("options.inSampleSize:", options.inSampleSize + "");
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(path, options);
		// return compressImage(bitmap);//压缩好比例大小后再进行质量压缩

		return bitmap;

		// // 获得图片的宽高
		// int width = bm.getWidth();
		// int height = bm.getHeight();
		// // 设置想要的大小
		// int newWidth = 120;
		// int newHeight = 280;
		// // 计算缩放比例
		// float scaleWidth = ((float) newWidth) / width;
		// float scaleHeight = ((float) newHeight) / height;
		// // 取得想要缩放的matrix参数
		// Matrix matrix = new Matrix();
		// matrix.postScale(scaleWidth, scaleHeight);
		// // 得到新的图片
		// Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
		// true);
		//
		// return newbm;
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

}
