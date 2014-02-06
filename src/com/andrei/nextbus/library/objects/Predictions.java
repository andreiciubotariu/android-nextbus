package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Predictions extends XmlObj{
	private String agencyTitle;
	private String routeTitle;
	private String routeTag;
	private String stopTitle;
	private String stopTag;
	
	private List <BareDirection> predictionList = new ArrayList <BareDirection>();
	private List <BareMessage> messageList = new ArrayList <BareMessage>();
	
	@Override
	public void init(Map<String, String> attributes) {
		agencyTitle = attributes.get("agencyTitle");
		agencyTitle = attributes.get("routeTitle");
		agencyTitle = attributes.get("routeTag");
		agencyTitle = attributes.get("stopTitle");
		agencyTitle = attributes.get("stopTag");
	}
	
	public void addPrediction (BareDirection d){
		predictionList.add(d);
	}
	
	public List<BareDirection> getDirectionsServed(){
		return predictionList;
	}
	
	public List<BareMessage> getMessages(){
		return messageList;
	}

	@Override
	public void add(XmlObj m) {
		if (m instanceof BareDirection){
			predictionList.add((BareDirection)m);
		}
		else if (m instanceof BareMessage){
			messageList.add((BareMessage)m);
		}
		
	}

	@Override
	public void setText(String text) {
		//not used
	}

	public String getAgencyTitle() {
		return agencyTitle;
	}

	public String getRouteTitle() {
		return routeTitle;
	}

	public String getRouteTag() {
		return routeTag;
	}

	public String getStopTitle() {
		return stopTitle;
	}

	public String getStopTag() {
		return stopTag;
	}

	public List<BareDirection> getPredictionList() {
		return predictionList;
	}

	public List<BareMessage> getMessageList() {
		return messageList;
	}
}