package com.people.client;

import android.annotation.SuppressLint;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class TransferRequestTag {
	
	public static final int Login						= 1 ;
	public static final int Register						= 2 ;
	
	
	private static HashMap<Integer, String> requestTagMap 	= null;
	
	public static HashMap<Integer, String> getRequestTagMap(){
		if (null == requestTagMap) {
			requestTagMap = new HashMap<Integer, String>();
			
			requestTagMap.put(Login, "posm/199002.tran5");
			requestTagMap.put(Register, "posm/199001.tran5");
			
		}
		
		return requestTagMap;
	}

}
