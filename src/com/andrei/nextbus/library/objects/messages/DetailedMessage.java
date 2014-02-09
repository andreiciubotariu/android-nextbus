package com.andrei.nextbus.library.objects.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.andrei.nextbus.library.objects.XmlObj;

public class DetailedMessage extends Message {
	
	private List <RouteConfiguredForMessage> configuredRoutes;
	private String id, creator, startBoundaryStr, endBoundaryStr;
	private Text normalText;
	private TextSecondaryLang secondaryText;
	private TextPhoneme phonemeText;
	private long startBoundary, endBoundary;
	private boolean sendToBuses;

	public DetailedMessage (){
		configuredRoutes = new ArrayList <RouteConfiguredForMessage> ();
	}
	
	@Override
	public void init(Map <String, String> attributes){
		id = attributes.get("tag");
		creator = attributes.get("creator");
		startBoundaryStr = attributes.get ("startBoundaryStr");
		endBoundaryStr = attributes.get("startBoundaryStr");
		startBoundary = getLong(attributes.get("startBoundary"));
		endBoundary = getLong(attributes.get("endBoundary"));
		sendToBuses = Boolean.parseBoolean(attributes.get("sendToBuses"));
	}
	
	private static long getLong(String value){
		try {
			return Long.parseLong(value);
		}
		catch (Exception e){
			e.printStackTrace();
			return -1L;
		}
	}

	
	@Override
	public void add (XmlObj m){
		super.add(m);
		if (m instanceof RouteConfiguredForMessage){
			configuredRoutes.add((RouteConfiguredForMessage)m);
		}
		else if (m instanceof TextSecondaryLang){
			secondaryText = (TextSecondaryLang)m;
		}
		else if (m instanceof TextPhoneme){
			phonemeText = (TextPhoneme)m;
		}
		else if (m instanceof Text){
			normalText = (Text)m;
		}
	}

	public boolean forAllRoutes (){
		return configuredRoutes.size()==0;
	}


	public List <RouteConfiguredForMessage> getRoutesForMessage (){
		return configuredRoutes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getStartBoundaryStr() {
		return startBoundaryStr;
	}

	public void setStartBoundaryStr(String startBoundaryStr) {
		this.startBoundaryStr = startBoundaryStr;
	}

	public String getEndBoundaryStr() {
		return endBoundaryStr;
	}

	public void setEndBoundaryStr(String endBoundaryStr) {
		this.endBoundaryStr = endBoundaryStr;
	}

	public long getStartBoundary() {
		return startBoundary;
	}

	public void setStartBoundary(long startBoundary) {
		this.startBoundary = startBoundary;
	}

	public long getEndBoundary() {
		return endBoundary;
	}

	public void setEndBoundary(long endBoundary) {
		this.endBoundary = endBoundary;
	}

	public boolean sendToBuses() {
		return sendToBuses;
	}

	public void setSendToBuses(boolean sendToBuses) {
		this.sendToBuses = sendToBuses;
	}
	
	@Override
	public String getText(){
		return normalText.getText();
	}
}