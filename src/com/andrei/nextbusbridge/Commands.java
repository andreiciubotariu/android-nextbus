package com.andrei.nextbusbridge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {

	public static List <Agency> getAgencies(){
		XmlTagFilter wanted = new XmlTagFilter (2,"agency");
		List<HashMap<String, String>> rawObjects = null;
		try {
			rawObjects = Parser.parse(wanted,new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List <Agency> agencies = new ArrayList <Agency>();
		for (int x = 0; x < rawObjects.size(); x++){
			agencies.add(new Agency (rawObjects.get(x)));
		}
		return agencies;
	}
	
	public static List <Direction> getDirections(String agencyTag, String routeTag){
		XmlTagFilter wanted = new XmlTagFilter (3,"direction");
		List<HashMap<String, String>> rawObjects = null;
		try {
			rawObjects = Parser.parse(wanted, new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generateDirections(rawObjects);
	}
	
	private static List <Direction> generateDirections (List <HashMap <String,String>> rawObjects){
		List <Direction> directions = new ArrayList <Direction>();
		for (int x = 0; x < rawObjects.size(); x++){
			directions.add(new Direction(rawObjects.get(x)));
		}
		return directions;
	}
	
	/*public static List <Path> getPathsForRoute (String agencyTag, String routeTag){
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
	}*/
	
	public static List <Stop> getAllStops(String agencyTag, String routeTag){
		XmlTagFilter wanted = new XmlTagFilter (3,"stop");
		List<HashMap<String, String>> rawObjects = null;
		try {
			rawObjects = Parser.parse(wanted,new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		XmlTagFilter body = new XmlTagFilter (1, "body");
		XmlTagFilter route = new XmlTagFilter (2,"route");
		route.setAttrributeSpec("tag", routeTag);
		XmlTagFilter parent = new XmlTagFilter(3, "direction");
		parent.setAttrributeSpec("tag",directionTag);
		
		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List<HashMap<String, String>> rawObjects = null;
		try {
			rawObjects = Parser.parse(wanted,new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag), body, route, parent);
			System.out.println ("Rawobjects: " + rawObjects.size());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		List<HashMap<String, String>> rawObjects = null;
		try {
			rawObjects = Parser.parse(wanted, new URL (url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List <Prediction> predictions = new ArrayList <Prediction> ();
		for (int x = 0; x < rawObjects.size();x++){
			predictions.add(new Prediction (rawObjects.get(x)));
		}		
		return predictions;
	}
	
	public static List <Vehicle> getVehicles (String agencyTag, String routeTag, long timeFilter){
		if (timeFilter < 0){
			timeFilter = 0L;
		}
		List <Vehicle> vehicles = new ArrayList <Vehicle> ();
		try {
			URL url = new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a="+agencyTag+"&r="+routeTag+"&t="+timeFilter);
			XmlTagFilter wanted = new XmlTagFilter(2, "vechile");
			List <HashMap <String, String>> rawObjects = Parser.parse(wanted, url);
			for (int x = 0; x < rawObjects.size();x++){
				vehicles.add(new Vehicle (rawObjects.get(x)));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vehicles;
	}
	
	public static void getStopHeaders (){
		try {
			URL url = new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=schedule&a=ttc&r=36");
			XmlTagFilter routeFilter = new XmlTagFilter (2, "route");
			routeFilter.setAttrributeSpec("direction", "East");
			routeFilter.setAttrributeSpec("serviceClass","wkd");
			XmlTagFilter headerFilter = new XmlTagFilter (3,"header");
			XmlTagFilter wanted = new XmlTagFilter (4,"stop");
			List <HashMap <String, String>> list = Parser.parse(wanted, url, routeFilter, headerFilter);
			System.out.println ("Size is " + list.size());
			/*for (HashMap <String,String> m : list){
				System.out.println (m.get("tag"));
			}*/
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List <Route> getSheduleForRoute (String agencyTag, String routeTag){
		return null;
	}
	
	public static class MultiStopPredictions {
		public List <MSDir> directions = new ArrayList <MSDir> ();
		public Map <String,String> attribs;
		
		public MultiStopPredictions (Map <String,String> attribs){
			this.attribs = attribs;
		}
	}
	
	public static class MSDir {
		public Map <String,String> attribs; 
		public List <Prediction> predictions = new ArrayList <Prediction> ();
	}
	
	
	//Object --> Determine what class to use for this
	public List <Object> getPredictionsForMultiStops (String agencyTag, AgencyStopTuple ... stops){
		StringBuilder s = new StringBuilder ("http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=")
		.append(agencyTag);
		for (int x = 0; x < stops.length;x++){
			s.append("&stops=")
			.append (stops[x]);
		}
		try {
			URL url = new URL (s.toString());
			String content = Parser.getXmlAsString(url);
			XmlTagFilter wanted = new XmlTagFilter (2, "predictions");
			List <HashMap <String,String>> rawObjects = Parser.parse(wanted, content);
			List <MultiStopPredictions> predictions = new ArrayList <MultiStopPredictions> ();
			for (int x = 0; x < rawObjects.size();x++){
				MultiStopPredictions m = new MultiStopPredictions (rawObjects.get(x));
				predictions.add(m);
			}	
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
