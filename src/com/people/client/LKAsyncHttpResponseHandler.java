package com.people.client;

import org.apache.http.client.HttpResponseException;

import android.content.DialogInterface;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.people.activity.BaseActivity;
import com.people.view.LKAlertDialog;

public abstract class LKAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
	
	private LKHttpRequest request;
	
	public void setRequest(LKHttpRequest request){
		this.request = request;
	}
	
	public abstract void successAction(Object obj);
	
	public void failureAction(Throwable error, String content){
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onSuccess(String content) {
		super.onSuccess(content);
		
		if (null == content || content.length() == 0){
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "对�??起�??系�??�?常�??请�?��????��??�????");
			return;
		}
		
		int tag = (Integer)request.getRequestDataMap().get(Constants.kMETHODNAME);
		Object obj = ParseResponseXML.parseXML(tag, content);
		Log.e("success", "try to do success action..." + TransferRequestTag.getRequestTagMap().get(tag));
		
		successAction(obj);
		
		try{
			// �????�????�????????????????起�??�?LKHttpRequest请�?????�?导�?��??常�??
			if (null != this.request.getRequestQueue()){
				// �???��??�????�???��????��???????��??�???????
				this.request.getRequestQueue().updateCompletedTag(this.request.getTag());
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onFailure(final Throwable error, final String content) {
		super.onFailure(error, content);
		
		try{
			HttpResponseException exception = (HttpResponseException) error;
			Log.e("Status Code","" + exception.getStatusCode());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			Log.e("error content:", content);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			Log.e("failure:", error.getCause().toString());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			Log.e("failure message:", error.getCause().getMessage());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		// �????请�??失败?????��??�?�?止�????????请�??
		//LKHttpRequestQueue.sharedClient().cancelRequests(ApplicationEnvironment.getInstance().getApplication(), true);
		//LKHttpRequestQueue.sharedClient().getHttpClient().getConnectionManager().shutdown();
		
		try{
			if (null != this.request.getRequestQueue()){
				this.request.getRequestQueue().cancel();
			} 
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		BaseActivity.getTopActivity().hideDialog(BaseActivity.ALL_DIALOG);
		
		
		LKAlertDialog dialog = new LKAlertDialog(BaseActivity.getTopActivity());
		dialog.setTitle("???�?");
		
		try{
			dialog.setMessage(getErrorMsg(error.toString()));
		} catch(Exception e){
			dialog.setMessage(getErrorMsg(null));
		}
		
		dialog.setCancelable(false);
		dialog.setPositiveButton("�?�?", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				
				failureAction(error, content);
			}
		});
		dialog.create().show();
		
		Log.e("error:", error.toString() );
	}
	
	@Override
	public void onFinish() {
		super.onFinish();
		
		try{
			if (null != this.request.getRequestQueue()){
				this.request.getRequestQueue().updataFinishedTag(this.request.getTag());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private String getErrorMsg(String content){
		if (null == content)
			return "?????��?��??常�??请�??管�????????系�??�???????�????";
		
		//  org.apache.http.conn.ConnectTimeoutException: Connect to /124.205.53.178:9596 timed out
		if (content.contains("ConnectTimeoutException") || content.contains("SocketTimeoutException") ) { 
			return "�???��????��?��????��??请�????��?��??�?�??????��??�???????�????";
		} else if(content.contains("HttpHostConnectException") || content.contains("ConnectException")){
			return "�???��????��?��????��??请�????��?��??�?�??????��??�???????�????";
		} else if(content.contains("Bad Request")){ // org.apache.http.client.HttpResponseException: Bad Request
			return "?????��?��?��??????????��?��??请�??管�????????系�??�???????�????";
		} else if (content.contains("time out")){ // socket time out
			return "�???��????��?��????��??请�??�????";
		} else if (content.contains("can't resolve host") || content.contains("400 Bad Request")) {
			return "�???��????��?��?��??�?请确�???��??�???��????��?��?��????????正确???";
		} else if (content.contains("UnknownHostException")){ // java.net.UnknownHostException: Unable to resolve host "oagd.crbcint.com": No address associated with hostname
			return "�?�?�?常�?????�?�???��????��?��??";
		}
		
		//return "�????请�????��??[" + content +"]";
		return "?????��?��??�?�?�?,请�????��??�????";
	}

}
