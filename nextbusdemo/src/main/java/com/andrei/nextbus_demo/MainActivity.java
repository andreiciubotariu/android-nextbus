package com.andrei.nextbus_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.andrei.nextbus_demo.wizard.StopPickerActivity;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_simple);
		
		if (getSupportFragmentManager().findFragmentByTag("main") == null){
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SavedStopsFragment(),"main").commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case R.id.action_choose_stop:
			startActivity (new Intent(this,StopPickerActivity.class));
		default :
			return super.onOptionsItemSelected(item);
		}
	}

}
