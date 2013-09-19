package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;

public class ScheduledStop {
	private List <TimePair> times;
	private String tag;
	private String title;

	public ScheduledStop (String tag){
		this.tag = tag;
		times = new ArrayList <TimePair>();
	}

	public void addTime (TimePair t){
		times.add(t);
	}

	public String getTag () {
		return tag;
	}
	public String getTitle (){
		return title;
	}

	public void setTitle (String title){
		this.title = title;
	}

	public List <TimePair> getTimes (){
		return times;
	}
}