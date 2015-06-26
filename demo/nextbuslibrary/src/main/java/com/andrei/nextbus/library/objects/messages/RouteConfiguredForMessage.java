package com.andrei.nextbus.library.objects.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.andrei.nextbus.library.objects.BaseInformationProvider;
import com.andrei.nextbus.library.objects.Stop;
import com.andrei.nextbus.library.objects.XmlObj;

public class RouteConfiguredForMessage extends XmlObj implements BaseInformationProvider{
	private String tag;
	private List <Stop> stopsAffected;
	
	public RouteConfiguredForMessage(){
		stopsAffected = new ArrayList<Stop>();
	}
	@Override
	public void init(Map<String, String> attributes) {
		tag = attributes.get("tag");
	}

	@Override
	public void add(XmlObj m) {
		if (m instanceof Stop){
			stopsAffected.add((Stop)m);
		}
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public String getTitle() {
		return null;
	}
	
	public List <Stop> getStopsAffected(){
		return stopsAffected;
	}
}
