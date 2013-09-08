package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Commands {

	public static List <Agency> getAgencies (String xml){
		List <Agency> agencies = new ArrayList <Agency>();
		XmlTagFilter wanted = new XmlTagFilter (2,"agency");
		List<Map<String, String>> rawObjects = Parser.parse(wanted,xml);
		for (int x = 0; x < rawObjects.size(); x++){
			agencies.add(new Agency (rawObjects.get(x)));
		}
		return agencies;
	}

	public static List <Direction> getDirections (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"direction");
		return generateDirections (Parser.parse(wanted, xml));
	}

	private static List <Direction> generateDirections (List <Map <String,String>> rawObjects){
		List <Direction> directions = new ArrayList <Direction>();
		for (int x = 0; x < rawObjects.size(); x++){
			directions.add(new Direction(rawObjects.get(x)));
		}
		return directions;
	}

	public static List <Path> getPaths (String xml){
		return Parser.parsePaths(xml);
	}

	public static List <Route> getRoutes (String xml){
		List <Route> routes = new ArrayList <Route>();
		XmlTagFilter wanted = new XmlTagFilter (2,"route");
		List <Map <String, String>> rawObjects = Parser.parse(wanted, xml);
		for (int x = 0; x < rawObjects.size(); x++){
			routes.add(new Route (rawObjects.get(x)));
		}
		return routes;
	}

	public static List <Stop> getAllStops (String xml){
		XmlTagFilter wanted = new XmlTagFilter (3,"stop");
		List<Map<String, String>> rawObjects = Parser.parse(wanted,xml);
		return generateStops (rawObjects);
	}

	private static List <Stop> generateStops (List <Map <String, String>> rawObjects){
		List <Stop> allStops = new ArrayList <Stop>();
		for (int x = 0; x < rawObjects.size(); x++){
			allStops.add(new Stop (rawObjects.get(x)));
		}
		return allStops;
	}

	public static List <Stop> getStopsForDirection (String xml, String directionTag){
		List <Stop> allStops = getAllStops (xml);
		XmlTagFilter directionFilter = new XmlTagFilter(3, "direction");
		directionFilter.setAttrributeSpec("tag",directionTag);
		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List<Map<String, String>> rawObjects = null;
		rawObjects = Parser.parse(wanted,xml, directionFilter);

		List <Stop> filteredStops = generateStops(rawObjects);

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
		List<Map<String, String>> rawObjects = null;
		rawObjects = Parser.parse(wanted, xml);
		List <Prediction> predictions = new ArrayList <Prediction> ();
		for (int x = 0; x < rawObjects.size();x++){
			predictions.add(new Prediction (rawObjects.get(x)));
		}		
		return predictions;
	}

	public static List <Vehicle> getVehicles (String xml){
		List <Vehicle> vehicles = new ArrayList <Vehicle> ();
		XmlTagFilter wanted = new XmlTagFilter(2, "vehicle");
		List <Map <String, String>> rawObjects = Parser.parse(wanted, xml);
		for (int x = 0; x < rawObjects.size();x++){
			vehicles.add(new Vehicle (rawObjects.get(x)));
		}
		return vehicles;
	}
}
