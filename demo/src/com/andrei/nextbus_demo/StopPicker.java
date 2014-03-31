package com.andrei.nextbus_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class StopPicker extends FragmentActivity { //implement actionbar after basic functionality

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop_picker);
		if (getSupportFragmentManager().findFragmentByTag(String.valueOf(0))==null){
			ChooserFragment c = ChooserFragment.getInstance(0,new Bundle());
			getSupportFragmentManager().beginTransaction().replace (R.id.content,c,String.valueOf(0)).commit();
		}
	}

	@Override
	public void onBackPressed(){
		System.out.println (getSupportFragmentManager().getBackStackEntryCount());
		if (getSupportFragmentManager().getBackStackEntryCount() == 0){
			finish();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stop_picker, menu);
		return true;
	}
}
