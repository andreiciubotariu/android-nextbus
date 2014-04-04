package com.andrei.nextbus_demo.wizard;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;

import com.andrei.nextbus.library.objects.BaseInfoObj;
import com.andrei.nextbus_demo.R;
import com.andrei.nextbus_demo.R.id;
import com.andrei.nextbus_demo.R.layout;
import com.andrei.nextbus_demo.R.menu;
import com.andrei.nextbus_demo.workers.AsyncActivity;

public class StopPickerActivity extends AsyncActivity <List <? extends BaseInfoObj>> { //implement actionbar after basic functionality

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
