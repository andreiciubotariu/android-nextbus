package com.andrei.nextbus_demo;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andrei.nextbus.library.commands.OnlineCommands;
import com.andrei.nextbus.library.objects.BaseInfoObj;

public class StopPicker extends FragmentActivity { //implement actionbar after basic functionality

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop_picker);
		ChooserFragment c = ChooserFragment.getInstance(0,new Bundle());
		getSupportFragmentManager().beginTransaction().replace (R.id.content,c,String.valueOf(0)).commit();
		System.out.println (new OnlineCommands().getAllStopsForDirection("ttc", "104", "104_1_104Sun").size());
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

	//agency -> routes -> directions -> stops -> stopFragment
	//  0         1            2          3           4
	//----------routes -> directions -> stops -> stopFragment
	public static class ChooserFragment extends ListFragment{
		private static String KEY_STEP = "step";
		private static String KEY_AGENCY_TAG = "agency_tag";
		private static String KEY_AGENCY_TITLE = "agency_title";
		private static String KEY_ROUTE_TAG = "route_tag";
		private static String KEY_ROUTE_TITLE = "route_title";
		private static String KEY_DIR_TAG = "dir_tag";
		private static String KEY_DIR_TITLE = "dir_title";
		private static String KEY_STOP_TAG = "stop_tag";
		private static String KEY_STOP_TITLE = "stop_title";
		
		
		private static final int AGENCIES = 0;
		private static final int ROUTES = AGENCIES + 1;
		private static final int DIRS = ROUTES + 1;
		private static final int STOPS = DIRS + 1;
		
		List <? extends BaseInfoObj> objects;
		public static ChooserFragment getInstance(int step, Bundle prevData){
			ChooserFragment c = new ChooserFragment();
			Bundle args = new Bundle();
			args.putAll(prevData);
			args.putInt(KEY_STEP, step);
			c.setArguments(args);
			return c;
		}
		
		public void onCreate (Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			objects = getObjects();
			createAdapter (objects);
		}
		
		private static void addToBundle(Bundle b, BaseInfoObj o,String tag, String title){
			b.putString(tag, o.getTag());
			b.putString(title, o.getTitle());
		}
		
		private void addInfo (int clickedPos){
			Bundle b = getArguments();
			int step = b.getInt(KEY_STEP);
			switch (step){
			case AGENCIES:
				addToBundle(b, objects.get(clickedPos), KEY_AGENCY_TAG, KEY_AGENCY_TITLE);
				break;
			case ROUTES:	
				addToBundle(b, objects.get(clickedPos), KEY_ROUTE_TAG, KEY_ROUTE_TITLE);
				break;
			case DIRS:
				addToBundle(b, objects.get(clickedPos), KEY_DIR_TAG, KEY_DIR_TITLE);
				break;
			case STOPS:
				addToBundle(b, objects.get(clickedPos), KEY_STOP_TAG, KEY_STOP_TITLE);
				break;
			}
		}
		
		public <T extends BaseInfoObj> void createAdapter(List <T> list){
//			if (list == null){
//				return;
//			}
			System.out.println (list.size());
			ArrayAdapter <T> adapter = new ArrayAdapter <T> (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, list);
			setListAdapter(adapter);
		}
		
		@Override
		public void onListItemClick (ListView l, View v, int position, long id){
			addInfo (position);
			int newStep = getArguments().getInt(KEY_STEP)+1;
			ChooserFragment c = getInstance(newStep,getArguments());
			getActivity().getSupportFragmentManager().beginTransaction().replace (R.id.content,c,String.valueOf(newStep)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(String.valueOf(newStep)).commit();
			
		}
		private List <?  extends BaseInfoObj> getObjects (){
			OnlineCommands c = new OnlineCommands();
			Bundle a = getArguments();
			int step = a.getInt(KEY_STEP);
			switch (step){
			case AGENCIES:
				return c.getAgencies();
			case ROUTES:	
				return c.getRoutes(a.getString(KEY_AGENCY_TAG));
			case DIRS:
				return c.getDirections(a.getString(KEY_AGENCY_TAG), a.getString(KEY_ROUTE_TAG));
			case STOPS:
				return c.getAllStopsForDirection(a.getString(KEY_AGENCY_TAG), a.getString(KEY_ROUTE_TAG), a.getString(KEY_DIR_TAG));
			}
			return null;
		}

	}
}
