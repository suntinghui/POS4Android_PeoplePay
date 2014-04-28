package com.people.client;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.people.util.AESUtil;
import com.people.util.MD5Util;

import android.content.SharedPreferences;
import android.util.Log;

public class LKHttpRequest {
	
	private int tag;
	private HashMap<String, Object> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;
	
	public LKHttpRequest(HashMap<String, Object> requestMap, LKAsyncHttpResponseHandler handler){
		this.requestDataMap = requestMap;
		this.responseHandler = handler;
		client = new AsyncHttpClient();
		
		if (null != this.responseHandler){
			this.responseHandler.setRequest(this);
		}
	}
	
	public String getRequestURL(){
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String hostStr = pre.getString(Constants.kREALHOST, null);
		if (null == hostStr || hostStr.trim().equals("")){
			hostStr = pre.getString(Constants.kHOSTNAME, Constants.DEFAULTHOST);
		}
		
		return hostStr + requestDataMap.get(Constants.kWEBSERVICENAME);
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public LKHttpRequestQueue getRequestQueue(){
		return this.queue;
	}
	
	public void setRequestQueue(LKHttpRequestQueue queue){
		this.queue = queue;
	}
	
	public HashMap<String, Object> getRequestDataMap() {
		return requestDataMap;
	}
	
	public LKAsyncHttpResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public AsyncHttpClient getClient() {
		return client;
	}
	
	
	/****************************************/
	
	public void post(){
		Log.i("url--- ", TransferRequestTag.getRequestTagMap().get(this.getRequestDataMap().get(Constants.kMETHODNAME)));
		this.client.post(ApplicationEnvironment.getInstance().getApplication(),"http://211.147.87.22:8092/posm/"+ TransferRequestTag.getRequestTagMap().get(this.getRequestDataMap().get(Constants.kMETHODNAME)), this.getHttpEntity(this), "text/xml; charset=utf-8", this.responseHandler);
	}
	
	@SuppressWarnings("unchecked")
	private HttpEntity getHttpEntity(LKHttpRequest request){
		HashMap<String, Object> reqMap = request.getRequestDataMap();
		
		StringBuffer bodySB = new StringBuffer();
		bodySB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><EPOSPROTOCOL>");
		bodySB.append(this.param2String((HashMap<String, Object>)reqMap.get(Constants.kPARAMNAME)));
		bodySB.append("</EPOSPROTOCOL>");
		
		Log.e("reqest body:", bodySB.toString());
		
		String AESValue = "";
		try {
			 AESValue = AESUtil.encryptString(bodySB.toString(), MD5Util.MD5Crypto(Constants.AESKEY));
			 Log.e("REQUEST:", AESValue);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		HttpEntity entity = null;
		try {
			entity = new StringEntity(AESValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	private String param2String(HashMap<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		
		for	(String key : paramMap.keySet()){
			Object obj = paramMap.get(key);
			if (obj instanceof String){
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>)obj)).append("</").append(key).append(">");
			}
		}
		String PCSIM = MD5Util.MD5Crypto(sb.toString());
		int macLocation = sb.indexOf("<PACKAGEMAC>");
		sb.insert(macLocation+12, PCSIM);
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private String hashMap2XML(HashMap<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		for	(String key : paramMap.keySet()){
			Object obj = paramMap.get(key);
			if (obj instanceof String){
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>)obj)).append("</").append(key).append(">");
			}
		}
		return sb.toString();
	}
	
}
