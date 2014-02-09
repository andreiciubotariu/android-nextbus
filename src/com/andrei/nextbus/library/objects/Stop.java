package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Stop extends BaseInfoObj {

	private String mTag, mTitle, mStopId;
	private Point mStopLocation;

	public Stop (){
	}

	@Override
	public void init (Map<String, String> map){
		mTag = map.get("tag");
		mTitle = map.get("title");
		mStopLocation = new Point (map.get("lat"),map.get("lon"));
		mStopId = map.get("stopId");
	}

	@Override
	public String getTag() {
		return mTag;
	}

	public void setTag(String newTag) {
		this.mTag = newTag;
	}

	@Override
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

	@Override
	public void add(XmlObj m) {
		// TODO Auto-generated method stub
		
	}
}