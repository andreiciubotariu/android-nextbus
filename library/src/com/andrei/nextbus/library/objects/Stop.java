package com.andrei.nextbus.library.objects;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class Stop extends BaseInfoObj implements Parcelable{

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

	//parcelable portion
	private Stop (Parcel in){
		mTag = in.readString();
		mTitle = in.readString();
		mStopId = in.readString();
		mStopLocation = (Point) in.readValue(getClass().getClassLoader());
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mTag);
		out.writeString(mTitle);
		out.writeString(mStopId);
		out.writeValue(mStopLocation);
	}
	
	public static final Parcelable.Creator<Stop> CREATOR = new Parcelable.Creator<Stop>() {

		@Override
		public Stop createFromParcel(Parcel source) {
			return new Stop (source);
		}

		@Override
		public Stop[] newArray(int size) {
			return new Stop [size];
		}
	};
}