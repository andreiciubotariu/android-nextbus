package com.andrei.nextbusbridge;

import java.util.HashMap;

public class Vehicle  {

	private String mId, mRouteTag, mDirTag;
	private double mLat, mLon, mSpeedKmKr;
	private int mSecsSinceReport, mHeading;
	private boolean mPredictable;
	
	public Vehicle (){
	}
	
	public Vehicle (HashMap <String, String> values){
		mId = values.get("id");
		mRouteTag = values.get("routeTag");
		mDirTag = values.get("dirTag");
		mLat = Double.parseDouble(values.get("lat"));
		mLon = Double.parseDouble (values.get("lon"));
		mSecsSinceReport =  Integer.parseInt(values.get("secsSinceReport"));
		mPredictable = Boolean.parseBoolean(values.get("predictable"));
		mHeading = Integer.parseInt(values.get("heading"));
		mSpeedKmKr = Double.parseDouble(values.get("speedKmHr"));
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
}
