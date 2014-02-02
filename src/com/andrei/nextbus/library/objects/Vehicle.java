package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Vehicle implements MapInitializable {

	private String mId, mRouteTag, mDirTag;
	private double mLat, mLon, mSpeedKmKr;
	private int mSecsSinceReport, mHeading;
	private boolean mPredictable;
	
	public Vehicle (){
	}
	
	public void init (Map<String, String> map){
		mId = map.get("id");
		mRouteTag = map.get("routeTag");
		mDirTag = map.get("dirTag");
		mLat = Double.parseDouble(map.get("lat"));
		mLon = Double.parseDouble (map.get("lon"));
		mSecsSinceReport =  Integer.parseInt(map.get("secsSinceReport"));
		mPredictable = Boolean.parseBoolean(map.get("predictable"));
		mHeading = Integer.parseInt(map.get("heading"));
		mSpeedKmKr = Double.parseDouble(map.get("speedKmHr"));
	}
	
	public String getid() {
		return mId;
	}
	public void setid(String id) {
		this.mId = id;
	}
	public String getrouteTag() {
		return mRouteTag;
	}
	public void setrouteTag(String routeTag) {
		this.mRouteTag = routeTag;
	}
	public String getdirTag() {
		return mDirTag;
	}
	public void setdirTag(String dirTag) {
		this.mDirTag = dirTag;
	}
	public double getlat() {
		return mLat;
	}
	public void setlat(double lat) {
		this.mLat = lat;
	}
	public double getlon() {
		return mLon;
	}
	public void setlon(double lon) {
		this.mLon = lon;
	}
	public double getspeedKmKr() {
		return mSpeedKmKr;
	}
	public void setspeedKmKr(double speedKmKr) {
		this.mSpeedKmKr = speedKmKr;
	}
	public int getsecsSinceReport() {
		return mSecsSinceReport;
	}
	public void setsecsSinceReport(int secsSinceReport) {
		this.mSecsSinceReport = secsSinceReport;
	}
	public int getheading() {
		return mHeading;
	}
	public void setheading(int heading) {
		this.mHeading = heading;
	}
	public boolean isPredictable() {
		return mPredictable;
	}
	public void setPredictable(boolean predictable) {
		this.mPredictable = predictable;
	}

	@Override
	public void add(MapInitializable m) {
		// TODO Auto-generated method stub
		
	}
}
