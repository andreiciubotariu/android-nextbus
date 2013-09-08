package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Commands {

	public static <T extends MapReader> List <T> getObjects (List <Map <String,String>> rawObjects, Class <T> clazz){
		List <T> list = new ArrayList <T> ();
		for (int x = 0; x < rawObjects.size(); x++){
			T obj;
			try {
				obj = clazz.newInstance();
				obj.init(rawObjects.get(x));
				list.add(obj);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public static List <Agency> getAgencies (String xml){
		XmlTagFilter wanted = new XmlTagFilter (2,"agency");
		return getObjects(Parser.parse(wanted,xml), Agency.class);
	}

	public static List <Direction> getDirections (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"direction");
		return getObjects(Parser.parse(wanted,xml), Direction.class);
	}

	public static List <Path> getPaths (String xml){
		return Parser.parsePaths(xml);
	}

	public static List <Route> getRoutes (String xml){
		XmlTagFilter wanted = new XmlTagFilter (2,"route");
		return getObjects(Parser.parse(wanted,xml), Route.class);
	}

	public static List <Stop> getAllStops (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"stop");
		return getObjects(Parser.parse(wanted,xml), Stop.class);
	}
	
	public static List <Stop> getStopsForDirection (String xml, String directionTag){
		List <Stop> allStops = getAllStops (xml);
		XmlTagFilter directionFilter = new XmlTagFilter(3, "direction");
		directionFilter.setAttrributeSpec("tag",directionTag);
		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List <Stop> filteredStops = getObjects(Parser.parse(wanted,xml,directionFilter), Stop.class);
		List <Stop> fullFilteredStops = new ArrayList <Stop>();
		for (int x = 0; x < allStops.size();x++){
			for (int y = 0; y < filteredStops.size();y++){
				if (allStops.get(x).getTag().equals(filteredStops.get(y).getTag())){
					fullFilteredStops.add(allStops.get(x));
					allStops.remove(x);
					break;
				}
			}
		}
		return fullFilteredStops;
	}

	public static List <Prediction> getPredictionsForStop (String xml){
		XmlTagFilter wanted = new XmlTagFilter (4,"prediction");
		return getObjects(Parser.parse(wanted,xml), Prediction.class);
	}

	public static List <Vehicle> getVehicles (String xml){
		XmlTagFilter wanted = new XmlTagFilter(2, "vehicle");
		return getObjects(Parser.parse(wanted,xml), Vehicle.class);
	}
}
