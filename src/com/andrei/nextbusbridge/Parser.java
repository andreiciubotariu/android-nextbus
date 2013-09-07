package com.andrei.nextbusbridge;

import java.io.IOException;
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

import android.util.Log;

import com.andrei.nextbusbridge.Message.ConfiguredRoute;
import com.andrei.nextbusbridge.Message.ConfiguredStop;

public class Parser {
	public static List <HashMap <String,String>> parse (XmlTagFilter wanted,URL xmlUrl,  XmlTagFilter ... filters){
		String content = getXmlAsString(xmlUrl);
		return parse (wanted,content,filters);
	}

	public static List <HashMap <String, String>> parse (XmlTagFilter wanted,String xmlContent, XmlTagFilter ... filters ){

		boolean filtered = filters != null && filters.length > 0;
		boolean filterFulfilled = !filtered;
		List<HashMap <String,String>> list = new ArrayList <HashMap <String,String>> ();

		if (xmlContent == null){
			return list;
		}

		int currentFilter = 0;
		try {
			XmlPullParser xpp = initParser (xmlContent);

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					//System.out.println (xpp.getName().trim());
					//Log.i ("FILTER", ""+currentFilter);
					/*if (filters.length > 0){
						System.out.println ("Name: " + filters [currentFilter].getTag());
						System.out.println ("filtered: " + filtered);
						System.out.println ("Not filter fulfilled: " + !filterFulfilled);
						System.out.println ("currentFilter" +  currentFilter);
						System.out.println ("filters.length" + filters.length);
						System.out.println ("Current filter object" + filters [currentFilter]);
						System.out.println ("Same name: " +xpp.getName().trim().equals(filters [currentFilter].getTag()) + "<<|>>");
						System.out.println (xpp.getDepth() == filters [currentFilter].getDepth());
					}*/
					if (filtered && !filterFulfilled && xpp.getName().trim().equals(filters [currentFilter].getTag()) && xpp.getDepth() == filters [currentFilter].getDepth()){
						boolean currentFilterFulfilled = false;
						int attribCount = xpp.getAttributeCount();
						System.out.println ("attributeCount is: " + attribCount);
						if (attribCount == 0 || xpp.getName().trim().equals ("body")){
							currentFilterFulfilled = true;
						}
						for (int x = 0; x < attribCount;x++){
							System.out.println (String.format ("%s %s",xpp.getAttributeName(x), xpp.getAttributeName(x)));
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
					else if (filterFulfilled && xpp.getName().trim().equals(wanted.getTag()) && xpp.getDepth() == wanted.getDepth()){
						HashMap <String,String> node = new HashMap <String,String>();
						for (int x = 0; x < xpp.getAttributeCount();x++){
							String name = xpp.getAttributeName(x).trim();
							String value = xpp.getAttributeValue(x).trim();
							//System.out.println (name + " #####|##### "+ value);
							node.put (name, value);
						}
						if (!node.isEmpty()){
							list.add(node);
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					int prevFilter = currentFilter - 1;
					if (filtered && prevFilter >= 0 && filters [prevFilter] != null && xpp.getName().trim().equals(filters [prevFilter].getTag()) && xpp.getDepth() == filters [prevFilter].getDepth()){
						//if (filtered && (filterFulfilled || currentFilter > 0 && filters [currentFilter] != null && xpp.getDepth() == filters [currentFilter].getDepth())){
						filterFulfilled = false;
						currentFilter --;
					}
					/*else {
						filtersCompleted++;
					}
					if (filtersCompleted < 0){
						filtersCompleted = 0;
					}*/
				}
				eventType = tryToGetNext(xpp);
			}
			return list;
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			return list;
		}
	}

	public static List <Path> parsePaths (URL xmlUrl){

		String xmlContent = null;
		List<Path> list = new ArrayList <Path> ();

		xmlContent = getXmlAsString(xmlUrl);

		if (xmlContent == null){
			return list;
		}


		try {
			XmlPullParser xpp = initParser (xmlContent);
			int eventType = xpp.getEventType();

			Path path = new Path();
			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					System.out.println (xpp.getName().trim());
					String nodeName = xpp.getName().trim();
					if (nodeName.equals("path")){
						path = new Path ();
					}
					else if (nodeName.equals("point")){
						HashMap <String, String> node = new HashMap <String,String>();
						for (int x = 0; x < xpp.getAttributeCount();x++){
							String name = xpp.getAttributeName(x).trim();
							String value = xpp.getAttributeValue(x).trim();

							node.put (name, value);
						}
						if (!node.isEmpty()){
							path.addPoint(new Point (node));
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					if (xpp.getName().equals("path")){
						list.add(path);
						path = new Path();
					}
				}
				eventType = tryToGetNext(xpp);
			}
			return list;
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			return list;
		}
	}

	public static List <Message> getMessages (){
		List <Message> messages = new ArrayList <Message> ();
		String xmlContent;
		try {
			xmlContent = getXmlAsString(new URL ("http://webservices.nextbus.com/service/publicXMLFeed?command=messages&a=sf-muni"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					//System.out.println (name);
					if (name.equals ("message")){
						Map <String, String> attribs = new HashMap <String,String> ();
						for (int x =0; x < xpp.getAttributeCount();x++){
							attribs.put(xpp.getAttributeName(x), xpp.getAttributeValue(x));
						}
						m = new Message (attribs);
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
				else if(eventType == XmlPullParser.TEXT){
					//System.out.println ("text - "+ xpp.getName() + ": " +xpp.getText());
					String text = xpp.getText().trim();
					if (m != null && text != null && text.length() > 0){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
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

		String xmlContent = null;
		List<RouteSchedule> list = new ArrayList <RouteSchedule> ();

		xmlContent = getXmlAsString(xmlUrl);

		if (xmlContent == null){
			return list;
		}


		try {
			RouteSchedule r = null;
			ScheduledStop s = null;
			TimeTuple t = null;
			boolean inStop = false;
			boolean inHeader = false;
			XmlPullParser xpp = initParser (xmlContent);
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					System.out.println (xpp.getName().trim());
					String nodeName = xpp.getName().trim();
					if (nodeName.equals("route")){
						Map <String,String> attributes = new HashMap <String,String> ();
						for (int x = 0; x < xpp.getAttributeCount();x++){
							String name = xpp.getAttributeName(x).trim();
							String value = xpp.getAttributeValue(x).trim();

							attributes.put (name, value);
						}
						r = new RouteSchedule (attributes);
					}
					else if (nodeName.equals("header")){
						inHeader = true;
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
						if (inHeader){
							s = new ScheduledStop (tag);
						}
						else {
							s = r.getStop(tag);
							t = new TimeTuple ();
							t.epochTime = epochTime;
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT){
				//	Log.i("TEXT", xpp.getName());
					if (inHeader && s != null){
						s.setTitle(xpp.getText());
					}
					else if (t != null ){
						t.readableTime = xpp.getText();
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					String name = xpp.getName().trim();
					if (name.equals("route")){
						list.add(r);
					}
					else if (name.equals ("header")){
						inHeader = false;
					}
					else if (name.equals ("stop")){
						if (inHeader){
							r.addScheduledStop(s.getTag(), s);
						}
						else {
							s.addTime(t);
						}
					}
				}
				eventType = tryToGetNext(xpp);
			}
			return list;
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			return list;
		}
	}

	private static XmlPullParser initParser (String xmlContent) throws XmlPullParserException{
		XmlPullParserFactory factory;
		factory = XmlPullParserFactory.newInstance();

		factory.setNamespaceAware(true);
		XmlPullParser xpp;

		xpp = factory.newPullParser();


		xpp.setInput(new StringReader (xmlContent == null ? "<notag> no tag </notag>" : xmlContent));

		return xpp;

	}

	private static String getXmlAsString (URL url){
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(3000);
			try {
				return IOUtils.toString(url, "UTF-8");
			}
			finally {
				urlConnection.disconnect();
			}

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
	}

	private static int tryToGetNext (XmlPullParser xpp){
		try {
			return xpp.next();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return XmlPullParser.END_DOCUMENT;
		} catch (IOException e) {
			e.printStackTrace();
			return XmlPullParser.END_DOCUMENT;
		}

	}
}

