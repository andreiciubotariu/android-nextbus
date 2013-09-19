package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Point {

	public final double latitude;
	public final double longitude;

	public Point (String lat, String lon){
		latitude = getDouble (lat);
		longitude = getDouble (lon);
	}
	
	public Point (Map<String, String> attributes){
		latitude = getDouble (attributes.get("lat"));
		longitude = getDouble (attributes.get("lon"));
	}

	private static double getDouble (String value){
		try {
			return Double.parseDouble(value);
		}
		catch (Exception e){
			e.printStackTrace();
			return -1.0D;
		}
	}
}
