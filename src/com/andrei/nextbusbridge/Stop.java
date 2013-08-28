package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stop implements BaseInformationProvider {

	private String mTag, mTitle, mStopId;
	private Point mStopLocation;

	public Stop (){

	}

	public Stop (HashMap <String, String> values){
		mTag = values.get("tag");
		mTitle = values.get("title");
		mStopLocation = new Point (values.get("lat"),values.get("lon"));
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

	public Point getLocation() {
		return mStopLocation;
	}

	public void setLocation (Point newLoc){
		mStopLocation = newLoc;
	}

	public static List <Stop> getAllStops(){
		List <HashMap <String, String>> rawObjects = Parser.parse(3,"stop", "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=104");
		//stops for a certain direction: List <HashMap <String, String>> rawObjects = Parser.parse(3,"direction","tag","104_0_104",4,"stop", "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=104");
		List <Stop> allStops = new ArrayList <Stop>();
		for (int x = 0; x < rawObjects.size(); x++){
			allStops.add(new Stop (rawObjects.get(x)));
		}

		return allStops;
	}
}
