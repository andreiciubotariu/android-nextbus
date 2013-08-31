package com.andrei.nextbusbridge;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class FullParser {
	//<Name, <Tag, LinkedObject>>
	Map <String, Map <String,LinkedObject>>  nameMap = new LinkedHashMap <String, Map <String, LinkedObject>> ();

	//<Tag, LinkedObject>
	Map <String, LinkedObject > tagMap = new LinkedHashMap <String, LinkedObject> ();


	public List <LinkedObject> parse (String xmlUrl){
		List <LinkedObject> list = new ArrayList <LinkedObject> ();
		String xmlContent = null;
		xmlContent = getXmlAsString(xmlUrl);

		if (xmlContent == null){
			return list;
		}

		try {
			XmlPullParser xpp = initParser (xmlContent);
			int eventType = xpp.getEventType();
			int depth = 0;
			LinkedObject o = new LinkedObject();
			while (eventType != XmlPullParser.END_DOCUMENT){
				if (eventType == XmlPullParser.START_TAG){ 
					boolean newDepth = false;
					if (xpp.getDepth() > depth){
						depth = xpp.getDepth();
						newDepth = true;
					}
					String name = xpp.getName();
					HashMap <String, String> attribs = new HashMap <String,String>();

					for (int x = 0; x < xpp.getAttributeCount();x++){
						attribs.put(xpp.getAttributeName(x).trim(), xpp.getAttributeValue(x).trim());
					}
					LinkedObject l = new LinkedObject (o,name, attribs);
					o.add (l);

					Map <String, LinkedObject> tagObject = new LinkedHashMap <String, LinkedObject> ();
					tagObject.put(attribs.get("tag"), l);
					if (nameMap.containsKey(name)){
						if (attribs.get("tag") != null &&  !nameMap.get(name).containsKey(attribs.get("tag"))){
							nameMap.get(name).put(attribs.get("tag"), l);
						}
						else if (attribs.get("tag") != null){
							nameMap.put(name, tagObject);
						}
					}
					else {
						nameMap.put (name, tagObject);
					}

					if (attribs.get("tag") != null && !tagMap.containsKey(attribs.get("tag"))){
						tagMap.put(attribs.get("tag"), l);
					}

					if (newDepth){
						o = l;
					}
				}
				if (eventType == XmlPullParser.END_TAG && xpp.getDepth() == depth){
					depth = xpp.getDepth()-1;
					o = o.getParent();
				}

				eventType = tryToGetNext(xpp);
			}

			return o.getNextDepth();
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
