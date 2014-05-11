package com.people.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.loopj.android.http.AsyncHttpClient;
import com.people.util.AESUtil;
import com.people.util.MD5Util;

import android.content.SharedPreferences;
import android.util.Log;

public class LKHttpRequest {

	private int tag;
	private int methodTag;
	private HashMap<String, Object> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;

	public LKHttpRequest(int methodTag, HashMap<String, Object> requestMap, LKAsyncHttpResponseHandler handler) {
		this.methodTag = methodTag;
		this.requestDataMap = requestMap;
		this.responseHandler = handler;
		client = new AsyncHttpClient();

		if (null != this.responseHandler) {
			this.responseHandler.setRequest(this);
		}
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getMethodTag() {
		return methodTag;
	}

	public LKHttpRequestQueue getRequestQueue() {
		return this.queue;
	}

	public void setRequestQueue(LKHttpRequestQueue queue) {
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

	public void post() {
		this.client.post(ApplicationEnvironment.getInstance().getApplication(), TransferRequestTag.getRequestTagMap().get(this.getMethodTag()), this.getHttpEntity(this), null, this.responseHandler);
	}

	private HttpEntity getHttpEntity(LKHttpRequest request) {

		StringBuffer bodySB = new StringBuffer();
		bodySB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><EPOSPROTOCOL>");
		bodySB.append(this.param2String(request.getRequestDataMap()));
		bodySB.append("</EPOSPROTOCOL>");
		String temp = bodySB.toString();
		
		String PCSIM = MD5Util.MD5Crypto(temp);
		String PCSIMXML = "<PACKAGEMAC>" + PCSIM + "</PACKAGEMAC>";
		
		String result = temp.replace("</EPOSPROTOCOL>", PCSIMXML + "</EPOSPROTOCOL>");

		Log.e("reqest body:", result);

		String AESValue = "";
		try {
			AESValue = AESUtil.encryptString(result, MD5Util.MD5Crypto(Constants.AESKEY));
			Log.e("REQUEST:", AESValue);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HttpEntity entity = null;
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
            nameValuePairs.add(new BasicNameValuePair("requestParam", AESValue)); 
            entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	private String param2String(HashMap<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();

		for (String key : paramMap.keySet()) {
			Object obj = paramMap.get(key);
			if (obj instanceof String) {
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>) obj)).append("</").append(key).append(">");
			}
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private String hashMap2XML(HashMap<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		for (String key : paramMap.keySet()) {
			Object obj = paramMap.get(key);
			if (obj instanceof String) {
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>) obj)).append("</").append(key).append(">");
			}
		}
		return sb.toString();
	}

}
