package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.andrei.nextbus.library.objects.messages.Message;

public class Predictions extends XmlObj{
	private String agencyTitle;
	private String routeTitle;
	private String routeTag;
	private String stopTitle;
	private String stopTag;
	
	private List <Direction> directionList = new ArrayList <Direction>();
	private List <Message> messageList = new ArrayList <Message>();
	
	@Override
	public void init(Map<String, String> attributes) {
		agencyTitle = attributes.get("agencyTitle");
		agencyTitle = attributes.get("routeTitle");
		agencyTitle = attributes.get("routeTag");
		agencyTitle = attributes.get("stopTitle");
		agencyTitle = attributes.get("stopTag");
	}
	
	public void addDirection (Direction d){
		directionList.add(d);
	}
	
	public List<Direction> getDirectionsServed(){
		return directionList;
	}
	
	public List<Message> getMessages(){
		return messageList;
	}

	@Override
	public void add(XmlObj m) {
		if (m instanceof Direction){
			directionList.add((Direction)m);
		}
		else if (m instanceof Message){
			messageList.add((Message)m);
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

//	public List<Direction> getPredictionList() {
//		return predictionList;
//	}

	public List<Message> getMessageList() {
		return messageList;
	}
}