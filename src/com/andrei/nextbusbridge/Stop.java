package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stop implements BaseInformationProvider {
	
	private final static String [] TAGS = new String [] {"body","route","stop"};
	private String mTag, mTitle, mStopId;
	double mLat, mLon;

	public Stop (){
		
	}
	
	public Stop (HashMap <String, String> values){
		mTag = values.get("tag");
		mTitle = values.get("title");
		mLat = Double.parseDouble(values.get("lat"));
		mLon = Double.parseDouble(values.get("lon"));
		mStopId = values.get("stopId");
	}
	@Override
	public String getTag() {
		return mTag;
	}

	public void setTag(String newTag) {
		this.mTag = newTag;
	}

	public String getTitle() {
		return mTitle;
	}


	public void setTitle(String newTitle) {
		this.mTitle = newTitle;
	}

	public String getStopId() {
		return mStopId;
	}


	public void setStopId(String newStopId) {
		this.mStopId = newStopId;
	}

	public double getLat() {
		return mLat;
	}

	public void setLat(double newLat) {
		this.mLat = newLat;
	}

	public double getLon() {
		return mLon;
	}


	public void setLon(double newLon) {
		this.mLon = newLon;
	}

	public static List <Stop> getAllStops(){
		List <HashMap <String, String>> rawObjects = Parser.parse(TAGS, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=104");
		List <Stop> allStops = new ArrayList <Stop>();
		for (int x = 0; x < rawObjects.size(); x++){
			allStops.add(new Stop (rawObjects.get(x)));
		}

		return allStops;
	}
}
