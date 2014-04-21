package com.andrei.nextbus_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.andrei.nextbus.library.objects.Point;
import com.andrei.nextbus.library.objects.Stop;
import com.andrei.nextbus_demo.wizard.StopPickerActivity;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (getSupportFragmentManager().findFragmentByTag("main") == null){
			getSupportFragmentManager().beginTransaction().replace(R.id.content, new SavedStopsFragment(),"main").commit();
		}
		//System.out.println ("PREDICTION: " + new OnlineCommands().getPredictionsForStop("ttc", "104", "14619", true).getDirectionsServed().get(0).getPredictions().get(0).getMinutes());
		Stop s = new Stop();
		s.setLocation(new Point ("1","2"));
		s.setTag("TAG");
		s.setTitle("A stop!");
		s.setStopId("an id");
		Intent i = new Intent (this, StopPickerActivity.class);
		i.putExtra("a",s);
		startActivity(i);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
