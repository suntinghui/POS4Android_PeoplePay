package com.people.client;

import android.app.Application;
import android.content.Context;

public class AppInit extends Application {

	public static Context context;

	@Override
	public void onCreate() {
		context = getApplicationContext();
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
