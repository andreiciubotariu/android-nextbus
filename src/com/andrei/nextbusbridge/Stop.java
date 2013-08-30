package com.andrei.nextbusbridge;

import java.util.HashMap;

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
}
