package com.andrei.nextbusbridge;

import java.util.Map;

public class Point {

	private double mLat;
	private double mLon;

	public Point (String lat, String lon){
		mLat = getDouble (lat);
		mLon = getDouble (lon);
	}
	
	public Point (Map<String, String> node){
		mLat = getDouble (node.get("lat"));
		mLon = getDouble (node.get("lon"));
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
