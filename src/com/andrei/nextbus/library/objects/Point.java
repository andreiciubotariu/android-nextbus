package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Point extends XmlObj{

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
		try {
			return Double.parseDouble(value);
		}
		catch (Exception e){
			e.printStackTrace();
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
}