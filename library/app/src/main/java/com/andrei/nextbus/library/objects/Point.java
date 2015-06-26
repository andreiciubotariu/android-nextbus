package com.andrei.nextbus.library.objects;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class Point extends XmlObj implements Parcelable{
	
	public double latitude;
	public double longitude;

	public Point(){
		
	}
	
	public Point (String lat, String lon){
		latitude = getDouble (lat);
		longitude = getDouble (lon);
	}
	
	public Point (Map<String, String> attributes){
		latitude = getDouble (attributes.get("lat"));
		longitude = getDouble (attributes.get("lon"));
	}

	private static double getDouble (String value){
		if (value == null){
			return -1L;
		}
		try {
			return Double.parseDouble(value);
		}
		catch (Exception e){
			//e.printStackTrace();
			return -1.0D;
		}
	}

	@Override
	public void init(Map<String, String> attributes) {
		latitude = getDouble (attributes.get("lat"));
		longitude = getDouble (attributes.get("lon"));
	}

	@Override
	public void add(XmlObj m) {
		//not used
	}
	
	@Override
	public String toString(){
		return "[" + latitude + ", " + longitude + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude)) {
			return false;
		}
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude)) {
			return false;
		}
		return true;
	}

	//parcelable
	private Point (Parcel in){
		latitude = in.readDouble();
		longitude = in.readDouble();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}
	

	public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {

		@Override
		public Point createFromParcel(Parcel source) {
			return new Point (source);
		}

		@Override
		public Point[] newArray(int size) {
			return new Point [size];
		}
	};
}