package com.andrei.nextbus.library.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.SparseArray;

import com.andrei.nextbus.library.objects.Agency;
import com.andrei.nextbus.library.objects.Direction;
import com.andrei.nextbus.library.objects.Path;
import com.andrei.nextbus.library.objects.Point;
import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus.library.objects.Route;
import com.andrei.nextbus.library.objects.Stop;
import com.andrei.nextbus.library.objects.Vehicle;

public class Commands {

	public List<Agency> getAgencies(String xml) {
		XmlTagFilter wanted = new XmlTagFilter(2, "agency");
		return Parser.parse(Agency.class, wanted, xml);
	}

	public static List<Direction> getDirections(String xml) {
		XmlTagFilter wanted = new XmlTagFilter(3, "direction");
		return Parser.parse(Direction.class, wanted, xml);
	}

	public List<Path> getPaths(String xml) {
		XmlTagFilter main = new XmlTagFilter(3, "path");

		XmlTagFilter point = new XmlTagFilter(4, "point", Point.class, main);
		HashMap<DepthTagPair, XmlTagFilter> children = new HashMap<DepthTagPair, XmlTagFilter>();
		children.put(new DepthTagPair(point.getDepth(), point.getTag()), point);

		return Parser.newParse(Path.class, xml, main, children, null);
	}

	public List<Route> getRoutes(String xml) {
		XmlTagFilter wanted = new XmlTagFilter(2, "route");
		return Parser.parse(Route.class, wanted, xml);
	}



	public List<Stop> getAllStops(String xmlAsString) {
		XmlTagFilter main = new XmlTagFilter(3, "stop");
		return Parser.newParse(Stop.class, xmlAsString, main, null, null);
	}

	public List<Stop> getStopsForDirection(String xml, String directionTag) {
		List<Stop> allStops = getAllStops(xml);
		XmlTagFilter directionFilter = new XmlTagFilter(3, "direction");
		directionFilter.setAttrributeSpec("tag", directionTag);
		SparseArray<XmlTagFilter> filters = new SparseArray<XmlTagFilter>();
		filters.append(directionFilter.getDepth(), directionFilter);

		XmlTagFilter wanted = new XmlTagFilter(4, "stop");
		List<Stop> filteredStops = Parser.newParse(Stop.class, xml, wanted,
				null, filters);
		List<Stop> fullFilteredStops = new ArrayList<Stop>();
		for (int x = 0; x < allStops.size(); x++) {
			for (int y = 0; y < filteredStops.size(); y++) {
				if (allStops.get(x).getTag()
						.equals(filteredStops.get(y).getTag())) {
					fullFilteredStops.add(allStops.get(x));
					// allStops.remove(x);
					break;
				}
			}
		}
		return fullFilteredStops;
	}

	public List<Prediction> getPredictionsForStop(String xml) {
		XmlTagFilter wanted = new XmlTagFilter(4, "prediction");
		return Parser.parse(Prediction.class, wanted, xml);
	}

	public List<Vehicle> getVehicles(String xml) {
		XmlTagFilter wanted = new XmlTagFilter(2, "vehicle");
		return Parser.parse(Vehicle.class, wanted, xml);
	}
}