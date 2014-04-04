package com.andrei.nextbus_demo.stop_display;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus_demo.workers.AsyncActivity;

public class StopDisplayActivity extends AsyncActivity<List <Prediction>> {
	private static final String FRAGMENT_TAG = "prediction_fragment";
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Fragment f = getSupportFragmentManager().findFragmentByTag (FRAGMENT_TAG);
		if (f == null){		
			f =	PredictionDisplayFragment.getInstance(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().replace(android.R.id.content, f, FRAGMENT_TAG).commit();
		}
	}
}
