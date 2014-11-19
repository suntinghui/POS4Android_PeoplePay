package com.people.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.people.R;
import com.people.model.TradeModel;
import com.people.model.WaterModel;
import com.people.view.TransferAdapter;
import com.people.view.WaterAdapter;

public class WaterActivity extends BaseActivity implements OnClickListener {


//	private Integer[] imageIds = { R.drawable.set_icon_0, R.drawable.set_icon_1, R.drawable.set_icon_2, R.drawable.set_icon_3, R.drawable.set_icon_problem , R.drawable.set_icon_msg , R.drawable.set_icon_4 };

	private ListView list_water;
	private WaterAdapter adapter;
	
	private ArrayList<WaterModel> arrayWater = new ArrayList<WaterModel>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_water);

		WaterModel model0 = new WaterModel();
		model0.setCustomerName("张三");
		WaterModel model1 = new WaterModel();
		model1.setCustomerName("张三");
		WaterModel model2 = new WaterModel();
		model2.setCustomerName("张三");
		WaterModel model3 = new WaterModel();
		model3.setCustomerName("张三");
		WaterModel model4 = new WaterModel();
		model4.setCustomerName("张三");
		arrayWater.add(model0);
		arrayWater.add(model1);
		arrayWater.add(model2);
		arrayWater.add(model3);
		arrayWater.add(model4);
		
		list_water = (ListView) findViewById(R.id.listview);
		adapter = new WaterAdapter(this, 0, arrayWater);
		list_water.setAdapter(adapter);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}

	}

	
}