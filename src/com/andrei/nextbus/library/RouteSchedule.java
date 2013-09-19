package com.andrei.nextbus.library;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouteSchedule {
	private Map <String, ScheduledStop> stops;
	private Map <String,String> attributes;

	public  RouteSchedule (Map <String,String> attributes){
		this.attributes = attributes;
		this.stops = new LinkedHashMap <String,ScheduledStop> ();
	}

	public String getAttribute (String tag){
		return attributes.get(tag);
	}

	public Collection <ScheduledStop> getStops(){
		return stops.values();
	}

	public ScheduledStop getStop (String stopTag){
		return stops.get(stopTag);
	}

	public void addScheduledStop (String stopTag, ScheduledStop stop){
		stops.put(stopTag,stop);
	}
}
