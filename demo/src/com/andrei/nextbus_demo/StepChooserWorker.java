package com.andrei.nextbus_demo;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;

import com.andrei.nextbus.library.commands.OnlineCommands;
import com.andrei.nextbus.library.objects.BaseInfoObj;
import com.andrei.nextbus_demo.workers.TaskContainerFragment;

public class StepChooserWorker extends TaskContainerFragment<List<? extends BaseInfoObj>> {
	
	public static StepChooserWorker getInstance(Bundle info){
		StepChooserWorker s = new StepChooserWorker();
		s.setArguments(info);
		return s;
	}


	private List <?  extends BaseInfoObj> getObjects (){
		OnlineCommands c = new OnlineCommands();
		Bundle a = getArguments();
		int step = a.getInt(ChooserFragment.KEY_STEP);
		switch (step){
		case ChooserFragment.AGENCIES:
			return c.getAgencies();
		case ChooserFragment.ROUTES:	
			return c.getRoutes(a.getString(ChooserFragment.KEY_AGENCY_TAG));
		case ChooserFragment.DIRS:
			return c.getDirections(a.getString(ChooserFragment.KEY_AGENCY_TAG), a.getString(ChooserFragment.KEY_ROUTE_TAG));
		case ChooserFragment.STOPS:
			return c.getAllStopsForDirection(a.getString(ChooserFragment.KEY_AGENCY_TAG), a.getString(ChooserFragment.KEY_ROUTE_TAG), a.getString(ChooserFragment.KEY_DIR_TAG));
		}
		return null;
	}


	@Override
	protected AsyncTask<Void, Void, List<? extends BaseInfoObj>> getAsyncTask() {

		return new StoredResultTask (){

			@Override
			protected List<? extends BaseInfoObj> doInBackground(Void... ignore) {
				return getObjects();
			}
		};
	}


}
