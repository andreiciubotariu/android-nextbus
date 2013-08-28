package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private List <Point> mPoints;
	
	public Path (List <Point> points){
		mPoints = points;
	}
	
	public Path() {
		mPoints = new ArrayList <Point>();
	}
	
	public void addPoint (Point point){
		mPoints.add(point);
	}

	public List <Point> getPoints (){
		return mPoints;
	}
	
	public static List <Path> getPathsForRoute (){
		return Parser.parsePaths("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=104");
	}
	
}
