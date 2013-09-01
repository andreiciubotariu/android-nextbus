package com.andrei.nextbusbridge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XPathParser {

	public static List <Map <String,String>> parseXml (String xpathExpression, URL xmlUrl){
		String xmlContent = getXmlAsString(xmlUrl);
		return parseXml (xpathExpression, xmlContent);
	}
	
	public static List <Map <String,String>> parseXml (String xpathExpression,String xmlContent){
		List <Map <String,String>> list = new ArrayList <Map <String,String>> ();
		try {
			if (xmlContent == null){
				return list;
			}
			InputStream byteInputStream = new ByteArrayInputStream(xmlContent.getBytes());
			InputSource inputSource = new InputSource (byteInputStream);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList nodes = (NodeList) xpath.evaluate (xpathExpression,inputSource,XPathConstants.NODESET);
			if(nodes != null && nodes.getLength() > 0) {
				int len = nodes.getLength();
				for(int i = 0; i < len; ++i) {
					// query value
					Node node = nodes.item(i);
					int nodeAttribLength = node.getAttributes().getLength();
					Map <String,String> listItem = new HashMap <String,String>();
					for (int x = 0; x < nodeAttribLength;x ++){
						Node z = node.getAttributes().item(x);
						listItem.put(z.getNodeName(),z.getNodeValue());
					}
					list.add(listItem);
				}
			}
		}
		catch (XPathExpressionException e){
			e.printStackTrace();
		}
		return list;
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
}
