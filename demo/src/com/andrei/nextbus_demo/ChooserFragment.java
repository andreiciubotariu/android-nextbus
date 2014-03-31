package com.andrei.nextbus_demo;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andrei.nextbus.library.commands.OnlineCommands;
import com.andrei.nextbus.library.objects.BaseInfoObj;

//agency -> routes -> directions -> stops -> stopFragment
//  0         1            2          3           4
//----------routes -> directions -> stops -> stopFragment
public class ChooserFragment extends ListFragment{
	protected static final String KEY_STEP = "step";
	protected static final String KEY_AGENCY_TAG = "agency_tag";
	protected static final String KEY_AGENCY_TITLE = "agency_title";
	protected static final String KEY_ROUTE_TAG = "route_tag";
	protected static final String KEY_ROUTE_TITLE = "route_title";
	protected static final String KEY_DIR_TAG = "dir_tag";
	protected static final String KEY_DIR_TITLE = "dir_title";
	protected static final String KEY_STOP_TAG = "stop_tag";
	protected static final String KEY_STOP_TITLE = "stop_title";


	private static final int AGENCIES = 0;
	private static final int ROUTES = AGENCIES + 1;
	private static final int DIRS = ROUTES + 1;
	private static final int STOPS = DIRS + 1;
	//private static final int CHOSEN_STOP = STOPS + 1;

	private List <? extends BaseInfoObj> objects;
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
		getChildFragmentManager().beginTransaction().add(new TaskFragment(), "A").commit();
		objects = getObjects();
		createAdapter (objects);
		System.out.println ("created");
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
		Fragment f = null;
		if (newStep <= STOPS){
			f = getInstance(newStep,getArguments());
		}
		else {
			f = StopDisplayFragment.getInstance(getArguments());
		}
		getActivity().getSupportFragmentManager().beginTransaction().replace (R.id.content,f,String.valueOf(newStep)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(String.valueOf(newStep)).commit();

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

	
	@Override
	public void onDestroyView(){
		
		System.out.println ("destroying view");
		super.onDestroyView();
	}
}