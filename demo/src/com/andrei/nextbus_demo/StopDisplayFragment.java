package com.andrei.nextbus_demo;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andrei.nextbus.library.commands.OnlineCommands;
import com.andrei.nextbus.library.objects.Prediction;

public class StopDisplayFragment extends Fragment {
	
	public static StopDisplayFragment getInstance (Bundle args){
		StopDisplayFragment f = new StopDisplayFragment();
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
		TextView closestTime = (TextView) v.findViewById(R.id.closest_time);
		//requestTransparentRegion is needed since gingerbread does NOT refresh the view until only after the activity is recreated
		root.requestTransparentRegion(v);
		List <Prediction> predictionList = new OnlineCommands().getPredictionsForStop(getArguments().getString(ChooserFragment.KEY_AGENCY_TAG), getArguments().getString(ChooserFragment.KEY_ROUTE_TAG), getArguments().getString(ChooserFragment.KEY_STOP_TAG));
		if (!predictionList.isEmpty()){
			closestTime.setText(predictionList.get(0).getMinutes() + " minutes");
			predictionList.remove(0);
		}
		ArrayAdapter <Prediction> adapter = new PredictionsAdapter(getActivity(), predictionList);
		ListView nextBusTimes = (ListView) v.findViewById(R.id.next_bus_times);
		nextBusTimes.setAdapter(adapter);
		if (predictionList.isEmpty()){
			TextView nextBusesHeader = (TextView) v.findViewById(R.id.next_buses_header);
			nextBusesHeader.setVisibility(View.INVISIBLE);
			nextBusTimes.setVisibility(View.INVISIBLE);
		}
		return v;
	}
}
