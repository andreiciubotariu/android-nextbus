package com.andrei.nextbus.library.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.SparseArray;

import com.andrei.nextbus.library.objects.RouteSchedule;
import com.andrei.nextbus.library.objects.ScheduledStop;
import com.andrei.nextbus.library.objects.TimePair;
import com.andrei.nextbus.library.objects.XmlObj;

public class Parser {

	public static <T extends XmlObj> List <T> parse (Class <T> clazz, String xmlContent,XmlTagFilter main,HashMap <DepthTagPair,XmlTagFilter> children, 
			SparseArray <XmlTagFilter> filters){ //change sparseArray and uses to hashmap or don't. Filters have to be linear
		List<T> list = new ArrayList <T> ();
		if (xmlContent == null || xmlContent.length() == 0){
			return list;
		}

		HashMap <DepthTagPair,List <? extends XmlObj>> depthListMap = new HashMap <DepthTagPair,List<? extends XmlObj>>();
		depthListMap.put(new DepthTagPair(main.getDepth(), main.getTag()), list);

		if (children != null){
			for (DepthTagPair k: children.keySet()){
				depthListMap.put(k, new ArrayList<XmlObj>());
			}
		}

		boolean filtered = filters != null && filters.size() > 0;
		boolean filterFulfilled = !filtered;

		int currentFilter = 0;
		try {
			XmlPullParser xpp = initParser (xmlContent);
			int eventType = xpp.getEventType();
			String tag = null;
			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					tag = xpp.getName().trim();
					if (filtered && !filterFulfilled && filters.get(xpp.getDepth()) != null && tag.equals(filters.get(xpp.getDepth()).getTag())){
						int attribCount = xpp.getAttributeCount();
						//body tag contains a copyright attribute, which should be ignored
						boolean currentFilterFulfilled = attribCount == 0 || tag.trim().equals ("body");
						XmlTagFilter f = filters.get(xpp.getDepth());
						for (int x = 0; x < attribCount;x++){
							if (xpp.getAttributeName(x).trim().equals (f.getAttribute()) && 
									xpp.getAttributeValue(x).trim().equals(f.getAttributeValue())){
								currentFilterFulfilled = true;
								break;
							}
						}
						filterFulfilled = currentFilterFulfilled && ++currentFilter == filters.size();
					}
					else if (filterFulfilled){
						Map <String,String> node = getAttributes(xpp);
						if (xpp.getDepth() == main.getDepth() && tag.equals(main.getTag())){
							T obj = clazz.newInstance();
							if (!node.isEmpty()){
								obj.init(node);
							}
							list.add(obj);
						}
						else if (children != null){
							XmlTagFilter currentLevel = children.get(new DepthTagPair(xpp.getDepth(), xpp.getName().trim()));
							if (currentLevel != null && tag.equals(currentLevel.getTag()) && currentLevel.getParent() != null){
								XmlTagFilter parent = currentLevel.getParent();
								List <? extends XmlObj> parentList = depthListMap.get(new DepthTagPair(parent.getDepth(), parent.getTag()));
								XmlObj m = currentLevel.getTargetClass().newInstance();
								if (!node.isEmpty()){
									m.init(node);
								}
								parentList.get(parentList.size()-1).add(m);

								List <XmlObj> currentList = (List<XmlObj>) depthListMap.get(new DepthTagPair(currentLevel.getDepth(), currentLevel.getTag()));
								currentList.add(m);
							}
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					if (filtered && filters.get(xpp.getDepth()) != null && xpp.getName().trim().equals(filters.get(xpp.getDepth()).getTag())){
						currentFilter--;
						filterFulfilled = false;
					}
				}
				else if (eventType == XmlPullParser.TEXT && xpp.getText() != null && xpp.getText().trim().length() > 0 && /*prevTag*/tag != null){
					String text = xpp.getText();
					XmlTagFilter currentLevel = children.get(new DepthTagPair(xpp.getDepth(), tag));
					if (currentLevel != null){
						List <XmlObj> currentList = (List<XmlObj>) depthListMap.get(new DepthTagPair(currentLevel.getDepth(), currentLevel.getTag()));
						if (currentList.size() > 0){
							currentList.get(currentList.size()-1).setText(text);
						}
					}
				}
				eventType = tryToGetNext(xpp);
			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static Map <String,String> attributes = new HashMap <String,String> ();
	private static Map <String,String> getAttributes (XmlPullParser xpp){
		if (attributes == null){
			attributes = new HashMap <String,String> ();
			Log.w("Parser","Making a new HashMap");
		}
		attributes.clear();
		int attributeCount = xpp.getAttributeCount();
		for (int x =0; x < attributeCount ;x++){
			attributes.put(xpp.getAttributeName(x).trim(), xpp.getAttributeValue(x).trim());
		}
		return attributes;
	}

	public static List <RouteSchedule> parseSchedule (URL xmlUrl){
		List<RouteSchedule> list = new ArrayList <RouteSchedule> ();

		HttpURLConnection urlConnection;
		RouteSchedule r = null;
		ScheduledStop s = null;
		TimePair t = null;
		XmlPullParser xpp = null;
		BufferedReader b;
		boolean inHeader =  false;
		try {
			urlConnection = (HttpURLConnection) xmlUrl.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(10000);
			b = new BufferedReader (new InputStreamReader (urlConnection.getInputStream()));
			XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
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
							t = new TimePair ();
							t.epochTime = epochTime;
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT && xpp.getText() != null && xpp.getText().trim().length() > 0){
					//	Log.i("TEXT", xpp.getName());
					if (inHeader/* && s != null*/){
						s.setTitle(xpp.getText());
					}
					else if (!inHeader /*t != null*/ ){
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