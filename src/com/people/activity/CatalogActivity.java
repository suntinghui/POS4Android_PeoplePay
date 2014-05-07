package com.people.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.people.R;
import com.people.view.TabView;


// 主界面
public class CatalogActivity extends TabActivity {  
  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_catalog);//设置TabHost使用的布局文件  
          
        TabHost tabHost=getTabHost();  
          
        TabView view = null;  
          
        // 最近联系人   
        view = new TabView(this, R.drawable.icon_n_0, R.drawable.icon_s_0);  
        view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.icon_s_0));  
              
        TabSpec recentContactSpec=tabHost.newTabSpec("RecentContact");  
        recentContactSpec.setIndicator(view);  
        Intent recentContactIntent = new Intent(this, SwipCardActivity.class);  
        recentContactSpec.setContent(recentContactIntent);  
        // 联系人   
        view = new TabView(this, R.drawable.icon_n_1, R.drawable.icon_s_1);  
        view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.icon_s_1));  
          
        TabSpec contactBookSpec=tabHost.newTabSpec("ContactBook");  
        contactBookSpec.setIndicator(view);  
        Intent contactBookIntent = new Intent(this,CashFlowActivity.class);  
        contactBookSpec.setContent(contactBookIntent);  
          
        // 短信   
        view = new TabView(this, R.drawable.icon_n_2, R.drawable.icon_s_2);  
        view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.icon_s_2));  
          
        TabSpec smsMessageSpec = tabHost.newTabSpec("SmsMessage");  
        smsMessageSpec.setIndicator(view);  
        Intent smsMessageIntent = new Intent(this, MerchantActivity.class);  
        smsMessageSpec.setContent(smsMessageIntent);  
          
        //设置    
        view = new TabView(this, R.drawable.icon_n_3, R.drawable.icon_s_3);  
        view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.icon_s_3));  
          
        TabSpec settingSpec = tabHost.newTabSpec("Setting");  
        settingSpec.setIndicator(view);  
        Intent settingIntent = new Intent(this, ToolActivity.class);  
        settingSpec.setContent(settingIntent);  
          
        tabHost.addTab(recentContactSpec);  
        tabHost.addTab(contactBookSpec);  
        tabHost.addTab(smsMessageSpec);  
        tabHost.addTab(settingSpec);  
          
        tabHost.setCurrentTab(0);  
    }
}  