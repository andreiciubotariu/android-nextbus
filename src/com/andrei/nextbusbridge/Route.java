package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Route implements BaseInformationProvider {

	private String mTitle, mTag;
	
	public Route (String tag, String title){
		mTag = tag;
		mTitle = title;
	}
	
	public Route(HashMap<String, String> values) {
		mTag = values.get("tag");
		mTitle = values.get("title");
	}

	@Override
	public String getTag() {
		return mTag;
	}
	
	@Override
	public String getTitle() {
		return mTitle;
	}
	
	public static List <Route> getRoutes(){
		List <HashMap <String, String>> rawObjects = Parser.parse(2,"route", "http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=ttc");
		List <Route> routes = new ArrayList <Route>();
		for (int x = 0; x < rawObjects.size(); x++){
			routes.add(new Route (rawObjects.get(x)));
		}

		return routes;
	}

}
