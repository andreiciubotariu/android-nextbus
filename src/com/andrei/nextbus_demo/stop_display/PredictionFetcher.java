package com.andrei.nextbus_demo.stop_display;

import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;

import com.andrei.nextbus.library.commands.OnlineCommands;
import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus_demo.wizard.ChooserFragment;
import com.andrei.nextbus_demo.workers.TaskContainerFragment;

public class PredictionFetcher extends TaskContainerFragment<List <Prediction>> {

	public static PredictionFetcher getInstance (Bundle args){
		PredictionFetcher f = new PredictionFetcher();
		f.setArguments(args);
		return f;
	}
	
	@Override
	protected AsyncTask<Void, Void, List <Prediction>> getAsyncTask() {
		return new StoredResultTask() {
			
			@Override
			protected List <Prediction> doInBackground(Void... ignore) {
				List<Prediction> list = new OnlineCommands().getPredictionsForStop(getArguments().getString(ChooserFragment.KEY_AGENCY_TAG), getArguments().getString(ChooserFragment.KEY_ROUTE_TAG), getArguments().getString(ChooserFragment.KEY_STOP_TAG));
				Collections.sort(list);
				return list;
			}
		};
	}	

}
