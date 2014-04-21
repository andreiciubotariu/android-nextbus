package com.andrei.nextbus_demo.stop_display;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus_demo.R;
import com.andrei.nextbus_demo.providers.SavedStops;
import com.andrei.nextbus_demo.wizard.ChooserFragment;
import com.andrei.nextbus_demo.workers.ResultListener;
import com.andrei.nextbus_demo.workers.TaskContainerFragment;

public class PredictionDisplayFragment extends Fragment implements ResultListener{

	public static PredictionDisplayFragment getInstance (Bundle args){
		PredictionDisplayFragment f = new PredictionDisplayFragment();
		f.setArguments(args);
		return f;
	}

	private static class PredictionsAdapter extends ArrayAdapter <Prediction>{
		private LayoutInflater mInflater;
		private int mLayoutRes;
		private int mTextRes;

		public PredictionsAdapter(Context context, List<Prediction> objects) {
			super(context, android.R.layout.simple_expandable_list_item_1,android.R.id.text1, objects);
			mLayoutRes = android.R.layout.simple_expandable_list_item_1;
			mTextRes = android.R.id.text1;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent){
			Prediction p = getItem(pos);
			View v = convertView;
			if (v == null){
				v = mInflater.inflate(mLayoutRes, parent,false);
			}
			((TextView)v.findViewById(mTextRes)).setText(p.getMinutes() + " minutes");
			return v;
		}
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_prediction_list, root, false);
		//requestTransparentRegion is needed since gingerbread does NOT refresh the view until only after the activity is recreated
		root.requestTransparentRegion(v);
		return v;
	}
	
	@Override
	public void onViewCreated (View v, Bundle savedInstanceState){
		FragmentManager manager = getFragmentManager();
		String workerTag = getTag() + TaskContainerFragment.WORKER_SUFFIX;
		PredictionFetcher workerFragment = (PredictionFetcher) manager.findFragmentByTag(workerTag);
		if (workerFragment == null || savedInstanceState == null){
			workerFragment = PredictionFetcher.getInstance(getArguments());
			manager.beginTransaction().add(workerFragment, workerTag).commit();
		}
		else {
			workerFragment.start(false);
		}
		
		Bundle b = getArguments();
		ContentValues c = new ContentValues();
		addToValues(c, b, ChooserFragment.KEY_AGENCY_TAG);
		addToValues(c, b, ChooserFragment.KEY_AGENCY_TITLE);
		addToValues(c, b, ChooserFragment.KEY_ROUTE_TAG);
		addToValues(c, b, ChooserFragment.KEY_ROUTE_TITLE);
		addToValues(c, b, ChooserFragment.KEY_STOP_TAG);
		addToValues(c, b, ChooserFragment.KEY_STOP_TITLE);
		addToValues(c, b, ChooserFragment.KEY_DIR_TAG);
		addToValues(c, b, ChooserFragment.KEY_DIR_TITLE);
		
		getActivity().getContentResolver().insert(SavedStops.CONTENT_URI,c);
		
		System.out.println ("inserted");
	}
	
	public void addToValues (ContentValues c, Bundle b, String key){
		c.put(key, b.getString(key));
	}
	
	@Override
	public void onResultObtained(Object result) {
		List <Prediction> predictionList = (List <Prediction>) result;
		if (result == null || getView() == null || !isAdded()){return;}
		TextView closestTime = (TextView) getView().findViewById(R.id.closest_time);
		ListView nextBusTimes = (ListView) getView().findViewById(R.id.next_bus_times);
		TextView nextBusesHeader = (TextView) getView().findViewById(R.id.next_buses_header);
		if (!predictionList.isEmpty()){
			closestTime.setText(predictionList.get(0).getMinutes() + " minutes");
			//predictionList.remove(0);
			if (predictionList.size() > 0){
				predictionList = predictionList.subList(1, predictionList.size());
			}
			nextBusesHeader.setVisibility(View.VISIBLE);
			nextBusTimes.setVisibility(View.VISIBLE);
		}
		ArrayAdapter <Prediction> adapter = new PredictionsAdapter(getActivity(), predictionList);
		nextBusTimes.setAdapter(adapter);
		if (predictionList.isEmpty()){
			System.out.println ("List size is 0!");
			nextBusesHeader.setVisibility(View.INVISIBLE);
			nextBusTimes.setVisibility(View.INVISIBLE);
		}
	}
	
	
}
