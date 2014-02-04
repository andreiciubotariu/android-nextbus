package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Path implements MapInitializable{

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

	@Override
	public void init(Map<String, String> attributes) {
		// TODO Auto-generated method stub
	}

	@Override
	public void add(MapInitializable m) {
		if (m instanceof Point){
			mPoints.add((Point)m);
		}
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
}
