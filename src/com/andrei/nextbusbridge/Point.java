package com.andrei.nextbusbridge;

import java.util.HashMap;

public class Point {

	private double mLat;
	private double mLon;

	public Point (String lat, String lon){
		mLat = getDouble (lat);
		mLon = getDouble (lon);
	}
	
	public Point (HashMap<String,String> values){
		mLat = getDouble (values.get("lat"));
		mLon = getDouble (values.get("lon"));
	}

	private static double  getDouble (String value){
		try {
			return Double.parseDouble(value);
		}
		catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	public double getLat (){
		return mLat;
	}

	public double getLon (){
		return mLon;
	}
}
