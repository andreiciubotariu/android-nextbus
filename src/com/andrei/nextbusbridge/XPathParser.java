package com.andrei.nextbusbridge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XPathParser {

	public static void parse (){
		HttpURLConnection conn = null;
		try {
			conn = getXmlStream("http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList");
			String expression = "//agency";
			InputSource inputSrc = new InputSource (conn.getInputStream());
			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodes = (NodeList) xpath.evaluate (expression,inputSrc,XPathConstants.NODESET);

			if(nodes != null && nodes.getLength() > 0) {
				int len = nodes.getLength();
				for(int i = 0; i < len; ++i) {
					// query value
					Node node = nodes.item(i);
					System.out.println (node.getNodeName() + " " + node.getAttributes().getNamedItem("tag").getNodeValue());
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (conn != null){
				conn.disconnect();
			}
		}

	}

	private static HttpURLConnection getXmlStream (String xmlUrl){
		try {
			URL url = new URL(xmlUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(3000);
			//return IOUtils.toString(url, "UTF-8");
			return urlConnection;

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
