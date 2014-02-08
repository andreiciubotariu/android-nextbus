package com.andrei.nextbus.library.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.andrei.nextbus.library.objects.Agency;
import com.andrei.nextbus.library.objects.Direction;
import com.andrei.nextbus.library.objects.Message;
import com.andrei.nextbus.library.objects.DetailedDirection;
import com.andrei.nextbus.library.objects.Path;
import com.andrei.nextbus.library.objects.Prediction;
import com.andrei.nextbus.library.objects.Predictions;
import com.andrei.nextbus.library.objects.Route;
import com.andrei.nextbus.library.objects.Stop;
import com.andrei.nextbus.library.objects.Vehicle;
import com.andrei.nextbus.library.parsers.DepthTagPair;
import com.andrei.nextbus.library.parsers.Parser;
import com.andrei.nextbus.library.parsers.RouteStopPair;
import com.andrei.nextbus.library.parsers.XmlTagFilter;

public class OnlineCommands extends Commands {
	private static URL createURL(String s) {
		try {
			return new URL(s);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Agency> getAgencies() {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList");
		return getAgencies(Parser.getXmlAsString(url));
	}

	public List<DetailedDirection> getDirections(String agencyTag, String routeTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag);
		return getDirections(Parser.getXmlAsString(url));
	}

	public List<Path> getPathsForRoute(String agencyTag, String routeTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag);
		return getPaths(Parser.getXmlAsString(url));
	}

	public List<Route> getRoutes(String agencyTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a="
				+ agencyTag);
		return super.getRoutes(Parser.getXmlAsString(url));
	}

	public List<Stop> getAllStops(String agencyTag, String routeTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag);
		return getAllStops(Parser.getXmlAsString(url));
	}

	public List<Stop> getAllStopsForDirection(String agencyTag,
			String routeTag, String directionTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag + "&terse");
		return getStopsForDirection(Parser.getXmlAsString(url), directionTag);
	}

	public List<Stop> newGetAllStops(String agencyTag, String routeTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag);
		return getAllStops(Parser.getXmlAsString(url));
	}

	public List<Stop> newGetAllStopsForDirection(String agencyTag,String routeTag, String directionTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="
				+ agencyTag + "&r=" + routeTag + "&terse");
		return getStopsForDirection(Parser.getXmlAsString(url), directionTag);
	}

	public List<Prediction> getPredictionsForStop(String agencyTag,
			String routeTag, String stopTag) {
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a="
				+ agencyTag + "&r=" + routeTag + "&s=" + stopTag);
		return getPredictionsForStop(Parser.getXmlAsString(url));
	}

	public List<Vehicle> getVehicles(String agencyTag, String routeTag,
			long timeFilter) {
		if (timeFilter < 0) {
			timeFilter = 0L;
		}
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a="
				+ agencyTag + "&r=" + routeTag + "&t=" + timeFilter);
		return getVehicles(Parser.getXmlAsString(url));
	}

	public List<Predictions> getPredictionsForMultiStops(String agencyTag,RouteStopPair... stops) {
		StringBuilder s = new StringBuilder(
				"http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=").append(agencyTag);
		for (int x = 0; x < stops.length; x++) {
			s.append("&stops=").append(stops[x].getConcatedTuple());
		}
		URL url = createURL(s.toString());
		XmlTagFilter main = new XmlTagFilter(2, "predictions");
		HashMap<DepthTagPair, XmlTagFilter> children = new HashMap<DepthTagPair, XmlTagFilter>();
		XmlTagFilter direction = new XmlTagFilter(3, "direction",Direction.class, main);
		children.put(
				new DepthTagPair(direction.getDepth(), direction.getTag()),direction);

		XmlTagFilter prediction = new XmlTagFilter(4, "prediction",	Prediction.class, direction);
		children.put(
				new DepthTagPair(prediction.getDepth(), prediction.getTag()),prediction);

		XmlTagFilter messages = new XmlTagFilter(3, "message",
				Message.class, main);
		children.put(new DepthTagPair(messages.getDepth(), messages.getTag()),messages);
		return Parser.parse(Predictions.class, Parser.getXmlAsString(url),main, children, null);
	}
}