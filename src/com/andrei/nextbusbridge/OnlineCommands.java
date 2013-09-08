package com.andrei.nextbusbridge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnlineCommands extends Commands {

	private static URL createURL (String s){
		try {
			return new URL (s);
		} catch (MalformedURLException e){
			e.printStackTrace();
			return null;
		}
	}

	public static List <Agency> getAgencies(){
		URL url = createURL ("http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList");
		return getAgencies (Parser.getXmlAsString(url));
	}

	public static List <Direction> getDirections(String agencyTag, String routeTag){
		URL url = createURL ("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return getDirections (Parser.getXmlAsString(url));
	}

	public static List <Path> getPathsForRoute (String agencyTag, String routeTag){
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return getPaths (Parser.getXmlAsString(url));
	}


	public static List <Route> getRoutes(String agencyTag){
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a="+agencyTag);
		return getRoutes (Parser.getXmlAsString(url));
	}

	public static List <Stop> getAllStops(String agencyTag, String routeTag){
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return getAllStops (Parser.getXmlAsString(url));
	}

	public static List <Stop> getAllStopsForDirection (String agencyTag, String routeTag, String directionTag){
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+agencyTag+"&r="+routeTag);
		return getStopsForDirection(Parser.getXmlAsString(url), directionTag);
	}

	public static List <Prediction> getPredictionsForStop (String agencyTag, String routeTag, String stopTag){
		URL url = createURL("http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a="+agencyTag+"&r="+routeTag+"&s="+stopTag);
		return getPredictionsForStop (Parser.getXmlAsString(url));
	}

	public static List <Vehicle> getVehicles (String agencyTag, String routeTag, long timeFilter){
		if (timeFilter < 0){
			timeFilter = 0L;
		}
		URL url = createURL ("http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a="+agencyTag+"&r="+routeTag+"&t="+timeFilter);
		return getVehicles (Parser.getXmlAsString(url));
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

	public static List <MultiStopPredictions> getPredictionsForMultiStops (String agencyTag, AgencyStopTuple ... stops){
		StringBuilder s = new StringBuilder ("http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=")
		.append(agencyTag);
		for (int x = 0; x < stops.length;x++){
			s.append("&stops=")
			.append (stops[x].getConcatedTuple());
		}
		List <MultiStopPredictions> predictions = new ArrayList <MultiStopPredictions> ();
		URL url = createURL (s.toString());
		String content = Parser.getXmlAsString(url);
		XmlTagFilter prediction = new XmlTagFilter (2, "predictions");
		List <Map <String,String>> rawObjects = Parser.parse(prediction, content);
		for (int x = 0; x < rawObjects.size();x++){
			MultiStopPredictions m = new MultiStopPredictions (rawObjects.get(x));
			predictions.add(m);
			prediction.setAttrributeSpec("stopTag", m.attribs.get("stopTag"));
			XmlTagFilter pred = new XmlTagFilter (4,"prediction");
			List <Map <String,String>> rawPreds = Parser.parse(pred, content, prediction);
			List <Prediction> preds = new ArrayList <Prediction> ();
			for (int y = 0; y <  rawPreds.size();y++){
				Prediction p = new Prediction ();
				p.init(rawPreds.get(x));
				preds.add (p);
			}
			MSDir d = new MSDir ();
			d.predictions = preds;
			List <MSDir> l = new ArrayList <MSDir> ();
			l.add(d);
			m.directions = l; 
		}
		return predictions;
	}
}
