package com.andrei.nextbus.library.objects;

public class RouteStopPair {
	private String both;

	public RouteStopPair (String routeTag, String stopTag){
		both = routeTag + "|" + stopTag;
	}
	
	public RouteStopPair (Route route, Stop stop){
		this (route.getTag(), stop.getTag());
	}
	
	public String getConcatedTuple (){
		return both;
	}
}
