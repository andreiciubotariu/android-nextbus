package com.andrei.nextbusbridge;

public class Point {

	private double mLat;
	private double mLon;

	public Point (String lat, String lon){
		mLat = getDouble (lat);
		mLon = getDouble (lon);
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
