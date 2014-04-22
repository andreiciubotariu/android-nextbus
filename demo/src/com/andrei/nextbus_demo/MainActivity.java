package com.andrei.nextbus_demo;

import com.andrei.nextbus_demo.wizard.StopPickerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (getSupportFragmentManager().findFragmentByTag("main") == null){
			getSupportFragmentManager().beginTransaction().replace(R.id.content, new SavedStopsFragment(),"main").commit();
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
