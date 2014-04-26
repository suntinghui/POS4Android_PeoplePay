package com.people.util;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.people.R;
import com.people.activity.BaseActivity;


public class ActivityUtil {
	
	public static int getIconResId(String resourceName) {
		int resourceId = BaseActivity.getTopActivity().getResources().getIdentifier(resourceName, "drawable", BaseActivity.getTopActivity().getPackageName());
		return resourceId;
	}
	
	public static void setEmptyView(ListView listView){
		ImageView emptyView = new ImageView(BaseActivity.getTopActivity());
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		emptyView.setLayoutParams(lp);
		
		emptyView.setImageResource(R.drawable.nodata);
		emptyView.setScaleType(ScaleType.CENTER_INSIDE);
		
		((ViewGroup)listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
		
	}

}
