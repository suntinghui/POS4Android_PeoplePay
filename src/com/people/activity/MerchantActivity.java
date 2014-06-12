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
import com.people.util.BitmapUtil;
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
		// String fileName = "test.txt"; //文件名字
		//
		// String res="";
		//
		// try{
		//
		// InputStream in = getResources().getAssets().open(fileName);
		//
		// // \Test\assets\yan.txt这里有这样的文件存在
		//
		// int length = in.available();
		//
		// byte [] buffer = new byte[length];
		//
		// in.read(buffer);
		//
		// res = EncodingUtils.getString(buffer, "UTF-8");
		//
		// }catch(Exception e){
		//
		// e.printStackTrace();
		//
		// }
		
		Constants.IMAGEUPLOAD = true;

		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("TRANCODE", "199021");
		tempMap.put("PHONENUMBER", "13917662264");//
		tempMap.put("FILETYPE", "MYPIC"); // MYPIC、IDPIC、IDPIC2、CARDPIC
		tempMap.put("PHOTOS", "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");// bitmap_zoom imgToBase64(mImagePath)
		LKHttpRequest req1 = new LKHttpRequest(TransferRequestTag.UpLoadImage, tempMap, getUpLoadImageHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在获取数据请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();

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
