package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private List <Point> mPoints;
	
	public Path (List <Point> points){
		mPoints = points;
	}
	
	public List <Point> getPoints (){
		return mPoints;
	}
	
	public static List <Path> getPathsForRoute (){
		//TODO actual implementation
		return new ArrayList <Path> ();
	}
	
}
