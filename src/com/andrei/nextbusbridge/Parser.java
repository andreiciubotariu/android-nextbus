package com.andrei.nextbusbridge;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

import com.andrei.nextbusbridge.Message.ConfiguredRoute;
import com.andrei.nextbusbridge.Message.ConfiguredStop;

public class Parser {
	public static List <HashMap <String,String>> parse (XmlTagFilter wanted, URL xmlUrl){
		String content = getXmlAsString(xmlUrl);
		return parse (wanted, content);
	}

	public static List <HashMap <String, String>> parse (XmlTagFilter wanted, String xmlContent){
		return parse (null, wanted, xmlContent);
	}

	public static List <HashMap <String,String>> parse (XmlTagFilter parent, XmlTagFilter wanted, URL xmlUrl){
		String content = getXmlAsString(xmlUrl);
		return parse (parent, wanted, content);
	}

	public static List <HashMap <String, String>> parse (XmlTagFilter parent,XmlTagFilter wanted, String xmlContent){

		boolean filtered = parent != null;
		boolean filterFulfilled = !filtered;
		List<HashMap <String,String>> list = new ArrayList <HashMap <String,String>> ();

		if (xmlContent == null){
			return list;
		}

		try {
			XmlPullParser xpp = initParser (xmlContent);

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					System.out.println (xpp.getName().trim());
					if (filtered && xpp.getName().trim().equals(parent.getTag()) && xpp.getDepth() == parent.getDepth()){
						int attribCount = xpp.getAttributeCount();
						if (attribCount == 0){
							filterFulfilled = true;
						}
						for (int x = 0; x < attribCount;x++){
							if (xpp.getAttributeName(x).trim().equals (parent.getAttribute()) && 
									xpp.getAttributeValue(x).trim().equals(parent.getAttributeValue())){
								filterFulfilled = true;
								break;
							}
						}
					}
					else if (filterFulfilled && xpp.getName().trim().equals(wanted.getTag()) && xpp.getDepth() == wanted.getDepth()){
						HashMap <String,String> node = new HashMap <String,String>();
						for (int x = 0; x < xpp.getAttributeCount();x++){
							String name = xpp.getAttributeName(x).trim();
							String value = xpp.getAttributeValue(x).trim();
							System.out.println (name + " #####|##### "+ value);
							node.put (name, value);
						}
						if (!node.isEmpty()){
							list.add(node);
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG){
					if (parent != null && xpp.getDepth() == parent.getDepth()){
						filterFulfilled = false;
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

