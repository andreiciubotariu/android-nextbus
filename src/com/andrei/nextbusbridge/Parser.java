package com.andrei.nextbusbridge;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Parser {

	public static List <HashMap <String, String>> parse (int depth, String wantedTag, String xmlUrl){
		return parse (-1,null,null,null, depth,wantedTag, xmlUrl);
	}

	public static List <HashMap <String, String>> parse (int filterDepth, String filterTag, String filterAttrib, 
			String filterValue,	int depth, String wantedTag, String xmlUrl){

		boolean filtered = filterDepth >= 0 && filterTag != null;
		boolean filterFulfilled = !filtered;

		String xmlContent = null;
		List<HashMap <String,String>> list = new ArrayList <HashMap <String,String>> ();

		xmlContent = getXmlAsString(xmlUrl);

		if (xmlContent == null){
			return list;
		}

		XmlPullParserFactory factory;

		try {
			factory = XmlPullParserFactory.newInstance();

			factory.setNamespaceAware(true);
			XmlPullParser xpp;

			xpp = factory.newPullParser();


			xpp.setInput(new StringReader (xmlContent == null ? "<notag> no tag </notag>" : xmlContent));

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
					System.out.println (xpp.getName().trim());
					if (filtered && xpp.getName().trim().equals(filterTag) && xpp.getDepth() == filterDepth){
						int attribCount = xpp.getAttributeCount();
						if (attribCount == 0){
							filterFulfilled = true;
						}
						for (int x = 0; x < attribCount;x++){
							if (xpp.getAttributeName(x).trim().equals (filterAttrib) && 
									xpp.getAttributeValue(x).trim().equals(filterValue)){
								filterFulfilled = true;
								break;
							}
						}
					}
					else if (filterFulfilled && xpp.getName().trim().equals(wantedTag) && xpp.getDepth() == depth){
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
					if (xpp.getDepth() == filterDepth){
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

	private static String getXmlAsString (String xmlUrl){
		try {
			URL url = new URL(xmlUrl);
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

