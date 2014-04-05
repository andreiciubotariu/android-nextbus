package com.andrei.nextbus_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.andrei.nextbus_demo.wizard.StopPickerActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//System.out.println ("PREDICTION: " + new OnlineCommands().getPredictionsForStop("ttc", "104", "14619", true).getDirectionsServed().get(0).getPredictions().get(0).getMinutes());
		startActivity(new Intent(this,StopPickerActivity.class));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
