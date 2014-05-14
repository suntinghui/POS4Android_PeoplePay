package com.people.view;

import com.people.activity.BaseActivity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;


public class ShowProgressHudTask extends AsyncTask<String, String, Void> implements OnCancelListener {

	ProgressHUD mProgressHUD;

	@Override
	protected void onPreExecute() {
		mProgressHUD = ProgressHUD.show(BaseActivity.getTopActivity(), "请稍候", true, true, this);
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			publishProgress(params[1]);
			Thread.sleep(Integer.parseInt(params[0]));
			publishProgress("");

			/**
			 * publishProgress("Connecting"); Thread.sleep(2000); publishProgress("Downloading"); Thread.sleep(5000); publishProgress("Done");
			 **/
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		mProgressHUD.setMessage(values[0]);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		mProgressHUD.dismiss();
		super.onPostExecute(result);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
		mProgressHUD.dismiss();
	}
}
