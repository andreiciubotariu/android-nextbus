package com.andrei.nextbus.library.objects;

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
}
