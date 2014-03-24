package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduledStop extends Stop{
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

	@Override
	public String getTag () {
		return tag;
	}
	
	@Override
	public String getTitle (){
		return title;
	}

	public void setTitle (String title){
		this.title = title;
	}

	public List <TimePair> getTimes (){
		return times;
	}

	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
	}

	@Override
	public void add(XmlObj m) {
		//nothing
	}
}