package com.andrei.nextbus.library;

import java.util.ArrayList;
import java.util.List;

public class Commands {

	public static List <Agency> getAgencies (String xml){
		XmlTagFilter wanted = new XmlTagFilter (2,"agency");
		return Parser.parse(Agency.class,wanted,xml);
	}

	public static List <Direction> getDirections (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"direction");
		return Parser.parse(Direction.class,wanted,xml);
	}

	public static List <Path> getPaths (String xml){
		return Parser.parsePaths(xml);
	}

	public static List <Route> getRoutes (String xml){
		XmlTagFilter wanted = new XmlTagFilter (2,"route");
		return Parser.parse(Route.class,wanted,xml);
	}

	public static List <Stop> getAllStops (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"stop");
		return Parser.parse(Stop.class,wanted,xml);
	}
	
	public static List <Stop> getStopsForDirection (String xml, String directionTag){
		List <Stop> allStops = getAllStops (xml);
		XmlTagFilter directionFilter = new XmlTagFilter(3, "direction");
		directionFilter.setAttrributeSpec("tag",directionTag);
		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List <Stop> filteredStops = Parser.parse(Stop.class,wanted,xml, directionFilter);
		List <Stop> fullFilteredStops = new ArrayList <Stop>();
		for (int x = 0; x < allStops.size();x++){
			for (int y = 0; y < filteredStops.size();y++){
				if (allStops.get(x).getTag().equals(filteredStops.get(y).getTag())){
					fullFilteredStops.add(allStops.get(x));
					//allStops.remove(x);
					break;
				}
			}
		}
		return fullFilteredStops;
	}

	public static List <Prediction> getPredictionsForStop (String xml){
		XmlTagFilter wanted = new XmlTagFilter (4,"prediction");
		return Parser.parse(Prediction.class,wanted,xml);
	}

	public static List <Vehicle> getVehicles (String xml){
		XmlTagFilter wanted = new XmlTagFilter(2, "vehicle");
		return Parser.parse(Vehicle.class,wanted,xml);
	}
}
