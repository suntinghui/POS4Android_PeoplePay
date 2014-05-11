package com.people.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.people.R;

public class SearchAndSwipeActivity extends BaseActivity {
	
	private TextView titleView = null;
	private ImageView animImageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_swipe);
		
		titleView = (TextView) this.findViewById(R.id.titleView);
		animImageView = (ImageView) this.findViewById(R.id.iv_swipe);

	}
	
	public void backAction(View view){
		this.finish();
	}

}
