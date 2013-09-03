package com.andrei.nextbusbridge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Commands {

	public static List <Agency> getAgencies(){
		XmlTagFilter wanted = new XmlTagFilter (2,"agency");
		List <HashMap <String, String>> rawObjects = Parser.parse(wanted, "http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList");
		List <Agency> agencies = new ArrayList <Agency>();
		for (int x = 0; x < rawObjects.size(); x++){
			agencies.add(new Agency (rawObjects.get(x)));
		}
		return agencies;
	}
	
	public static List <Direction> getDirections(String agencyTag, String routeTag){
		XmlTagFilter wanted = new XmlTagFilter (3,"direction");
		List <HashMap <String, String>> rawObjects = Parser.parse(wanted, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return generateDirections(rawObjects);
	}
	
	private static List <Direction> generateDirections (List <HashMap <String,String>> rawObjects){
		List <Direction> directions = new ArrayList <Direction>();
		for (int x = 0; x < rawObjects.size(); x++){
			directions.add(new Direction(rawObjects.get(x)));
		}
		return directions;
	}
	
	public static List <Path> getPathsForRoute (String agencyTag, String routeTag){
		try {
			return Parser.parsePaths(new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static List <Route> getRoutes(String agencyTag){
		XmlTagFilter wanted = new XmlTagFilter (2,"route");
		List <HashMap <String, String>> rawObjects = Parser.parse(wanted, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a="+agencyTag);
		List <Route> routes = new ArrayList <Route>();
		for (int x = 0; x < rawObjects.size(); x++){
			routes.add(new Route (rawObjects.get(x)));
		}
		return routes;
	}
	
	public static List <Stop> getAllStops(String agencyTag, String routeTag){
		XmlTagFilter wanted = new XmlTagFilter (3,"stop");
		List <HashMap <String, String>> rawObjects = Parser.parse(wanted, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return generateStops (rawObjects);
	}
	
	private static List <Stop> generateStops (List <HashMap <String, String>> rawObjects){
		List <Stop> allStops = new ArrayList <Stop>();
		for (int x = 0; x < rawObjects.size(); x++){
			allStops.add(new Stop (rawObjects.get(x)));
		}
		return allStops;
	}
	
	public static List <Stop> getAllStopsForDirection (String agencyTag, String routeTag, String directionTag){
		List <Stop> allStops = getAllStops (agencyTag, routeTag);
		XmlTagFilter parent = new XmlTagFilter(3, "direction");
		parent.setAttrributeSpec("tag",directionTag);
		
		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List <HashMap <String, String>> rawObjects = Parser.parse(parent,wanted, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		
		List <Stop> filteredStops = generateStops(rawObjects);
		
		List <Stop> fullFilteredStops = new ArrayList <Stop>();
		for (int x = 0; x < allStops.size();x++){
			for (int y = 0; y < filteredStops.size();y++){
				if (allStops.get(x).getTag().equals(filteredStops.get(y).getTag())){
					fullFilteredStops.add(allStops.get(x));
					break;
				}
			}
		}
		return fullFilteredStops;
	}
	
	public static List <Prediction> getPredictionsForStop (String agencyTag, String routeTag, String stopTag){
		XmlTagFilter wanted = new XmlTagFilter (4,"prediction");
		String url = "http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a="+agencyTag+"&r="+routeTag+"&s="+stopTag;
		List <HashMap <String, String>> rawObjects = Parser.parse(wanted, url);
		List <Prediction> predictions = new ArrayList <Prediction> ();
		for (int x = 0; x < rawObjects.size();x++){
			predictions.add(new Prediction (rawObjects.get(x)));
		}		
		return predictions;
	}
}
