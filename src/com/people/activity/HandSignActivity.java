package com.people.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.people.R;
import com.people.client.Constants;
import com.people.util.StringUtil;

public class HandSignActivity extends BaseActivity implements OnClickListener {

	private TextView amountText = null;
	private Bitmap bitmap = null;

	private Button okButton = null;
	private Button clearButton = null;
	private LinearLayout paintLayout = null;

	private PaintView paintView = null;

	private boolean hasSign = false; // 简单判断用户是否有签名

	private String signImageName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_handsign);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置成全屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏

		amountText = (TextView) this.findViewById(R.id.amount);
		amountText.setText(StringUtil.String2SymbolAmount(getIntent().getStringExtra("AMOUNT")));

		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
		clearButton = (Button) this.findViewById(R.id.clearButton);
		clearButton.setOnClickListener(this);

		paintLayout = (LinearLayout) this.findViewById(R.id.paintLayout);
	}

	// 当activity显示出来的时候执行此方法
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus && null == paintView) {
			// 首先创建一个新的位图
			bitmap = Bitmap.createBitmap(paintLayout.getWidth(), paintLayout.getHeight(), Config.ARGB_8888);

			paintView = new PaintView(this);
			paintLayout.addView(paintView, new LayoutParams(paintLayout.getWidth(), paintLayout.getHeight()));
			paintView.requestFocus();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.okButton:
			if (hasSign) {
				Intent intent = new Intent(HandSignActivity.this, TradeSuccessActivity.class);
				startActivityForResult(intent, 0);

			} else {
				Toast.makeText(this, "您还没有签名，请先完成签名", Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.clearButton:
			if (hasSign) {
				paintView.clear();
			}

			break;
		}
	}

	private Bitmap scaleBitmap() {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 设置想要的大小
		int newWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		int newHeight = height;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth); // 使其等比缩放
		// 得到新的图片
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.parseColor("#fafad2"));
		canvas.drawBitmap(bitmap, 0, 0, null);

		return newBitmap;
	}

	private void saveBitmapToFile(Bitmap mBitmap, String bitName) {
		File mWorkingPath = new File(Constants.SIGNIMAGESPATH);
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		File f = new File(Constants.SIGNIMAGESPATH + bitName + ".JPEG");
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Path path;

		public PaintView(Context context) {
			super(context);

			initView();
		}

		private void initView() {
			// 首先打印水印
			// drawMD5OnView();
			this.setBackgroundDrawable(new BitmapDrawable(bitmap));

			// 初始化画笔，准备签名
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(5);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cacheCanvas = new Canvas(bitmap);

		}

		public void clear() {
			hasSign = false;

			if (cacheCanvas != null) {
				paintLayout.removeAllViews();

				// 首先创建一个新的位图
				bitmap = Bitmap.createBitmap(paintLayout.getWidth(), paintLayout.getHeight(), Config.ARGB_8888);

				paintView = new PaintView(HandSignActivity.this);
				paintLayout.addView(paintView, new LayoutParams(paintLayout.getWidth(), paintLayout.getHeight()));
				paintLayout.invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(bitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = bitmap != null ? bitmap.getWidth() : 0;
			int curH = bitmap != null ? bitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (bitmap != null) {
				newCanvas.drawBitmap(bitmap, 0, 0, null);
			}
			bitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				// 简单认为用户移动了手指即有签名
				hasSign = true;

				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}

			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
			}

			invalidate();

			return true;
		}
	}

	class SaveImageTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			HandSignActivity.this.showDialog(BaseActivity.PROGRESS_DIALOG, "正在保存签名信息");
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			// 保存图片
			saveBitmapToFile(scaleBitmap(), signImageName);

			/***
			 * TransferSuccessDBHelper helper = new TransferSuccessDBHelper(); helper.updateATransfer(tracenum, signImageName, phoneNum);
			 *****/
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			HandSignActivity.this.hideDialog(BaseActivity.PROGRESS_DIALOG);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 此处写处理的事件
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
