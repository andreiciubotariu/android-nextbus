package com.andrei.nextbus_demo.stop_display;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus_demo.R;
import com.andrei.nextbus_demo.providers.SavedStops;
import com.andrei.nextbus_demo.workers.ResultListener;
import com.andrei.nextbus_demo.workers.TaskContainerFragment;

public class PredictionDisplayFragment extends Fragment implements ResultListener{

	private static final String UPDATED_TIME = "Updated at: ";
	private static final int RED = Color.parseColor("#FF1800");
	private static final int BLUE = Color.parseColor ("#1240AB");
	private static final int YELLOW = Color.parseColor ("#E2FA00");
	private static final int GREEN = Color.parseColor ("#00C12B");
	
	
	public static PredictionDisplayFragment getInstance (Bundle args){
		PredictionDisplayFragment f = new PredictionDisplayFragment();
		f.setArguments(args);
		f.setHasOptionsMenu(true);
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

		TextView dirTitle = (TextView) v.findViewById(R.id.dir_title);
		if (getArguments().containsKey(SavedStops.DIR_TITLE)){
			dirTitle.setText(getArguments().getString(SavedStops.DIR_TITLE));
		}
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

	}

	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.fragment_prediction_display, menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case R.id.action_save_stop:
			saveStopToDB();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addToValues (ContentValues c, Bundle b, String key){
		c.put(key, b.getString(key));
	}

	private void saveStopToDB(){
		Bundle args = getArguments();
		ContentValues values = new ContentValues();
		addToValues(values, args, SavedStops.AGENCY_TAG);
		addToValues(values, args, SavedStops.AGENCY_TITLE);
		addToValues(values, args, SavedStops.ROUTE_TAG);
		addToValues(values, args, SavedStops.ROUTE_TITLE);
		addToValues(values, args, SavedStops.TAG);
		addToValues(values, args, SavedStops.TITLE);
		addToValues(values, args, SavedStops.DIR_TAG);
		addToValues(values, args, SavedStops.DIR_TITLE);

		getActivity().getContentResolver().insert(SavedStops.CONTENT_URI,values);

		System.out.println ("inserted");
	}

	@Override
	public void onResultObtained(Object result) {
		List <Prediction> predictionList = (List <Prediction>) result;
		if (result == null || getView() == null || !isAdded()){
			return;
		}
		TextView fetchedTimeView = (TextView) getView().findViewById(R.id.updated_time_display);
		TextView closestTime = (TextView) getView().findViewById(R.id.closest_time);
		ListView nextBusTimes = (ListView) getView().findViewById(R.id.next_bus_times);
		TextView nextBusesHeader = (TextView) getView().findViewById(R.id.next_buses_header);
		if (!predictionList.isEmpty()){
			String timeString = predictionList.get(0).getMinutes() + " minutes";
			if (predictionList.get(0).getMinutes() == 1){
				timeString = "1 minute";
			}
			else if (predictionList.get(0).getMinutes() == 0){
				timeString = "Arriving";
			}
			closestTime.setText(timeString);
			changePredictionColor(closestTime, predictionList.get(0).getMinutes());
			if (predictionList.size() > 0){
				predictionList = predictionList.subList(1, predictionList.size());
			}
			nextBusesHeader.setVisibility(View.VISIBLE);
			nextBusTimes.setVisibility(View.VISIBLE);
			
			TaskContainerFragment w = (TaskContainerFragment) getFragmentManager().findFragmentByTag(this.getTag() + TaskContainerFragment.WORKER_SUFFIX);
			if (w != null){
				long timeStored = w.getTimeStored();
				Time time = new Time();
				time.set(timeStored);
				fetchedTimeView.setText(UPDATED_TIME + time.format("%k:%M"));
				
			}
		}
		ArrayAdapter <Prediction> adapter = new PredictionsAdapter(getActivity(), predictionList);
		nextBusTimes.setAdapter(adapter);
		if (predictionList.isEmpty()){
			System.out.println ("List size is 0!");
			nextBusesHeader.setVisibility(View.INVISIBLE);
			nextBusTimes.setVisibility(View.INVISIBLE);
			closestTime.setText("No predictions");
			fetchedTimeView.setText(UPDATED_TIME + "--:--");
			changePredictionColor(fetchedTimeView, -1);
		}
	}
	
	private void changePredictionColor (TextView predictionView, int minutes){
		if (minutes == -1){
			predictionView.setBackgroundColor(0x000000);
			predictionView.setTextColor(Color.BLACK);
		}
		else if (minutes <= 1){
			predictionView.setBackgroundColor(GREEN);
			predictionView.setTextColor(Color.WHITE);
		}
		else if (minutes > 1 && minutes <= 10){
			predictionView.setBackgroundColor(BLUE);
			predictionView.setTextColor(Color.WHITE);
		}
		else if (minutes > 10 && minutes <= 20){
			predictionView.setBackgroundColor(YELLOW);
			predictionView.setTextColor(Color.BLACK);
		}
		else {
			predictionView.setBackgroundColor(RED);
			predictionView.setTextColor(Color.WHITE);
		}
	}
}
