package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Predictions implements MapInitializable{
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
	public void add(MapInitializable m) {
		if (m instanceof BareDirection){
			predictionList.add((BareDirection)m);
		}
		else if (m instanceof BareMessage){
			messageList.add((BareMessage)m);
		}
		
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

}
