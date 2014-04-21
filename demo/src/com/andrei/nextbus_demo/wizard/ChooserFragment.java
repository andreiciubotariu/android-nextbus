package com.andrei.nextbus_demo.wizard;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.andrei.nextbus.library.objects.BaseInfoObj;
import com.andrei.nextbus_demo.R;
import com.andrei.nextbus_demo.stop_display.StopDisplayActivity;
import com.andrei.nextbus_demo.workers.ResultListener;
import com.andrei.nextbus_demo.workers.TaskContainerFragment;

//agency -> routes -> directions -> stops -> stopFragment
//  0         1            2          3           4
//----------routes -> directions -> stops -> stopFragment
public class ChooserFragment extends ListFragment implements ResultListener{
	public static final String KEY_STEP = "step";
	public static final String KEY_AGENCY_TAG = "agency_tag";
	public static final String KEY_AGENCY_TITLE = "agency_title";
	public static final String KEY_ROUTE_TAG = "route_tag";
	public static final String KEY_ROUTE_TITLE = "route_title";
	public static final String KEY_DIR_TAG = "dir_tag";
	public static final String KEY_DIR_TITLE = "dir_title";
	public static final String KEY_STOP_TAG = "stop_tag";
	public static final String KEY_STOP_TITLE = "stop_title";


	protected static final int AGENCIES = 0;
	protected static final int ROUTES = AGENCIES + 1;
	protected static final int DIRS = ROUTES + 1;
	protected static final int STOPS = DIRS + 1;
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
		
		FragmentManager manager = getFragmentManager();
		String workerTag = getTag() + TaskContainerFragment.WORKER_SUFFIX;
		StepChooserWorker workerFragment = (StepChooserWorker) manager.findFragmentByTag(workerTag);
		if (workerFragment == null || savedInstanceState == null){
			workerFragment = StepChooserWorker.getInstance(getArguments());
			manager.beginTransaction().add(workerFragment, workerTag).commit();
		}
		else {
			workerFragment.start(false);
		}
		//getChildFragmentManager().beginTransaction().add(new TaskFragment(), "A").commit();
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
//					if (list == null){
//							return;
//					}
		System.out.println ("List size is " + list.size());
		ArrayAdapter <T> adapter = new ArrayAdapter <T> (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, list);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick (ListView l, View v, int position, long id){
		addInfo (position);
		int newStep = getArguments().getInt(KEY_STEP)+1;
		if (newStep <= STOPS){
			Fragment f = getInstance(newStep,getArguments());
			getActivity().getSupportFragmentManager().beginTransaction().replace (R.id.content,f,String.valueOf(newStep)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(String.valueOf(newStep)).commit();
		}
		else {
			//f = StopDisplayFragment.getInstance(getArguments());
			Intent i = new Intent (getActivity(),StopDisplayActivity.class);
			i.putExtras(getArguments());
			startActivity(i);
		}
	}
	
	@Override
	public void onResultObtained (Object result){
		System.out.println ("Obtained result");
		objects = (List<? extends BaseInfoObj>) result;
		if (result == null){
			Toast.makeText(getActivity(),"Result is null", Toast.LENGTH_SHORT).show();
		}
		createAdapter (objects);
	}
	
	@Override
	public void onDestroyView(){
		
		System.out.println ("destroying view");
		super.onDestroyView();
	}
}