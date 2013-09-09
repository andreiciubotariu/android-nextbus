package com.andrei.nextbusbridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.andrei.nextbusbridge.Message.ConfiguredRoute;
import com.andrei.nextbusbridge.Message.ConfiguredStop;

public class Parser {
	public static <T extends MapReader> List <T> parse (Class <T> clazz, XmlTagFilter wanted,URL xmlUrl,  XmlTagFilter ... filters){
		String content = getXmlAsString(xmlUrl);
		return parse (clazz, wanted,content,filters);
	}

	public static <T extends MapReader> List <T> parse (Class <T> clazz, XmlTagFilter wanted,String xmlContent, XmlTagFilter ... filters ){
		List<T> list = new ArrayList <T> ();

		if (xmlContent == null || xmlContent.length() == 0){
			return list;
		}

		boolean filtered = filters != null && filters.length > 0;
		boolean filterFulfilled = !filtered;

		int currentFilter = 0;
		try {
			XmlPullParser xpp = initParser (xmlContent);

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					String name = xpp.getName().trim();
					int depth = xpp.getDepth();
					if (filtered && !filterFulfilled && name.equals(filters [currentFilter].getTag()) && depth == filters [currentFilter].getDepth()){
						boolean currentFilterFulfilled = false;
						int attribCount = xpp.getAttributeCount();
						if (attribCount == 0 || name.trim().equals ("body")){ //body tag contains a copyright attribute, which should be ignored
							currentFilterFulfilled = true;
						}
						for (int x = 0; x < attribCount;x++){
							if (xpp.getAttributeName(x).trim().equals (filters [currentFilter].getAttribute()) && 
									xpp.getAttributeValue(x).trim().equals(filters [currentFilter].getAttributeValue())){
								currentFilterFulfilled = true;
								break;
							}
						}
						if (currentFilterFulfilled){
							currentFilter++;
							if (currentFilter == filters.length){
								filterFulfilled = true;
							}
						}
					}
					else if (filterFulfilled && name.equals(wanted.getTag()) && depth == wanted.getDepth()){
						Map <String,String> node = getAttributes(xpp);
						if (!node.isEmpty()){
							T obj = clazz.newInstance();
							obj.init(node);
							list.add(obj);
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					int prevFilter = currentFilter - 1;
					if (filtered && prevFilter >= 0 && filters [prevFilter] != null && xpp.getName().trim().equals(filters [prevFilter].getTag()) && xpp.getDepth() == filters [prevFilter].getDepth()){
						filterFulfilled = false;
						currentFilter --;
					}
				}
				eventType = tryToGetNext(xpp);
			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e){
			e.printStackTrace();
		}
		catch (IllegalAccessException e){
			e.printStackTrace();
		}
		return list;
	}

	public static List <Path> parsePaths (String xmlContent){
		List<Path> list = new ArrayList <Path> ();

		if (xmlContent == null || xmlContent.length() == 0){
			return list;
		}

		try {
			XmlPullParser xpp = initParser (xmlContent);
			int eventType = xpp.getEventType();

			Path currentPath = new Path();
			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					String nodeName = xpp.getName().trim();
					if (nodeName.equals("path")){
						currentPath = new Path ();
					}
					else if (nodeName.equals("point")){
						Map <String, String> node = getAttributes(xpp);
						if (!node.isEmpty()){
							currentPath.addPoint(new Point (node));
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					if (xpp.getName().equals("path")){
						list.add(currentPath);
						currentPath = new Path();
					}
				}
				eventType = tryToGetNext(xpp);
			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List <Message> getMessages (String xmlContent){
		List <Message> messages = new ArrayList <Message> ();
		if (xmlContent == null || xmlContent.length() == 0){
			return messages;
		}
		try {
			XmlPullParser xpp = initParser(xmlContent);
			int eventType = xpp.getEventType();
			Message m = null;
			ConfiguredRoute r = null;
			ConfiguredStop s = null;
			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					String name = xpp.getName().trim();
					if (name.equals ("message")){
						m = new Message (getAttributes (xpp));
					}
					else if (name.equals("routeConfiguredForMessage")){
						for (int x = 0; x < xpp.getAttributeCount();x++){
							if (xpp.getAttributeName(x).trim().equals("tag")){
								r = new ConfiguredRoute(xpp.getAttributeValue(x));
								break;
							}
						}
					}
					else if (name.equals("stop")){
						String tag = null, title = null;
						for (int x = 0; x < xpp.getAttributeCount();x++){
							if (xpp.getAttributeName(x).trim().equals("tag")){
								tag = xpp.getAttributeValue(x);
							}
							else if (xpp.getAttributeName(x).trim().equals("title")){
								title = xpp.getAttributeValue(x);
							}
						}
						if (tag != null && title != null){
							s = new ConfiguredStop(tag, title);
						}
					}
				}
				else if(eventType == XmlPullParser.TEXT && m != null){
					//System.out.println ("text - "+ xpp.getName() + ": " +xpp.getText());
					String text = xpp.getText().trim();
					if (text != null && text.length() > 0){
						m.setText(text);
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					String name = xpp.getName().trim();
					if (name.equals ("message") && m != null){
						messages.add(m);
					}
					else if (name.equals ("routeConfiguredForMessage") && r != null && m!= null){
						m.addConfiguredRoute(r);
					}
					else if (name.equals ("stop") && s!= null && r != null){
						r.addConfiguredStop(s);
					}
				}
				eventType = tryToGetNext(xpp);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return messages;
	}

	private static Map <String,String> getAttributes (XmlPullParser xpp){
		Map <String,String> attributes = new HashMap <String,String> ();
		int attributeCount = xpp.getAttributeCount();
		for (int x =0; x < attributeCount ;x++){
			attributes.put(xpp.getAttributeName(x).trim(), xpp.getAttributeValue(x).trim());
		}
		return attributes;
	}

	public static class TimeTuple {
		public String epochTime;
		public String readableTime;


		/*public long getEpochTime (){
			return epochTime;
		}

		public String getReadableTime (){
			return readableTime;
		}*/
	}

	public static class ScheduledStop {
		private List <TimeTuple> times;
		private String tag;
		private String title;

		public ScheduledStop (String tag){
			this.tag = tag;
			times = new ArrayList <TimeTuple>();
		}

		public void addTime (TimeTuple t){
			times.add(t);
		}

		public String getTag () {
			return tag;
		}
		public String getTitle (){
			return title;
		}

		public void setTitle (String title){
			this.title = title;
		}

		public List <TimeTuple> getTimes (){
			return times;
		}
	}


	public static class RouteSchedule {
		private Map <String, ScheduledStop> stops;
		private Map <String,String> attributes;

		public  RouteSchedule (Map <String,String> attributes){
			this.attributes = attributes;
			this.stops = new LinkedHashMap <String,ScheduledStop> ();
		}

		public String getAttribute (String tag){
			return attributes.get(tag);
		}

		public Collection <ScheduledStop> getStops(){
			return stops.values();
		}

		public ScheduledStop getStop (String stopTag){
			return stops.get(stopTag);
		}

		public void addScheduledStop (String stopTag, ScheduledStop stop){
			stops.put(stopTag,stop);
		}
	}

	public static List <RouteSchedule> parseSchedule (URL xmlUrl){
		List<RouteSchedule> list = new ArrayList <RouteSchedule> ();

		HttpURLConnection urlConnection;
		RouteSchedule r = null;
		ScheduledStop s = null;
		TimeTuple t = null;
		XmlPullParser xpp = null;
		BufferedReader b;
		int inHeader =  -1;
		try {
			urlConnection = (HttpURLConnection) xmlUrl.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(10000);
			b = new BufferedReader (new InputStreamReader (urlConnection.getInputStream()));
			XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			xpp = factory.newPullParser();
			factory = null;
			xpp.setInput(b);
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					String nodeName = xpp.getName().trim();
					if (nodeName.equals("route")){
						Map <String,String> attributes = getAttributes(xpp);
						r = new RouteSchedule (attributes);
					}
					else if (nodeName.equals("header")){
						inHeader = 1;
					}
					else if (nodeName.equals ("stop")){
						String tag = null;
						String epochTime = null;
						for (int x = 0; x < xpp.getAttributeCount();x++){
							if (xpp.getAttributeName(x).trim().equals ("tag")){
								tag = xpp.getAttributeValue(x).trim();
							}
							else if (xpp.getAttributeName(x).trim().equals ("epochTime")){
								epochTime = xpp.getAttributeValue(x).trim();
							}
						}
						if (inHeader == 1){
							s = new ScheduledStop (tag);
						}
						else {
							s = r.getStop(tag);
							t = new TimeTuple ();
							t.epochTime = epochTime;
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT && xpp.getText() != null && xpp.getText().trim().length() > 0){
					//	Log.i("TEXT", xpp.getName());
					if (inHeader == 1/* && s != null*/){
						s.setTitle(xpp.getText());
					}
					else if (inHeader == 0 /*t != null*/ ){
						t.readableTime = xpp.getText();
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					String name = xpp.getName().trim();
					if (name.equals("route")){
						list.add(r);
					}
					else if (name.equals ("header")){
						inHeader = 0;
					}
					else if (name.equals ("stop")){
						if (inHeader == 1){
							r.addScheduledStop(s.getTag(), s);
						}
						else {
							s.addTime(t);
						}
					}
				}
				eventType = tryToGetNext(xpp);
			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally {
			s = null;
			t = null;
			r = null;
			b  = null;
			if (xpp != null){
				try {
					xpp.setInput(null);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
			xpp = null;
			System.gc();
		}
		return list;
	}

	private static XmlPullParser initParser (String xmlContent) throws XmlPullParserException{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true); //check if needed, no namespaces provided anyways
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader (xmlContent == null ? "<notag> no tag </notag>" : xmlContent));
		return xpp;
	}

	public static String getXmlAsString (URL url){
		if (url == null){
			throw new IllegalArgumentException ("URL must not be null");
		}
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(3000);
			return IOUtils.toString(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (urlConnection != null){
				urlConnection.disconnect();
			}
		}
	}

	private static int tryToGetNext (XmlPullParser xpp){
		try {
			return xpp.next();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return XmlPullParser.END_DOCUMENT;
	}
}

