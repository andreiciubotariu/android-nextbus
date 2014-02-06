package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Message {

	public static class ConfiguredRoute {
		private String tag;
		private List <ConfiguredStop> stops;

		public ConfiguredRoute (String tag){
			this.tag = tag;
			stops = new ArrayList <ConfiguredStop> ();
		}

		public String getTag (){
			return tag;
		}

		public void addConfiguredStop (ConfiguredStop stop){
			stops.add(stop);
		}
		public List <ConfiguredStop> getConfiguredStops (){
			return stops;
		}
	}

	public static class ConfiguredStop {
		private String tag;
		private String title;

		public ConfiguredStop (String tag, String title){
			this.tag = tag;
			this.title = title;
		}

		public String getTag (){
			return tag;
		}

		public String getTitle (){
			return title;
		}
	}

	private List <ConfiguredRoute> configuredRoutes;
	private String id, creator, startBoundaryStr, endBoundaryStr, text;
	private long startBoundary, endBoundary;
	private boolean sendToBuses;

	public Message (Map <String, String> values){
		configuredRoutes = new ArrayList <ConfiguredRoute> ();
		id = values.get("tag");
		creator = values.get("creator");
		startBoundaryStr = values.get ("startBoundaryStr");
		endBoundaryStr = values.get("startBoundaryStr");
		startBoundary = Long.parseLong(values.get("startBoundary"));
		endBoundary = Long.parseLong(values.get("endBoundary"));
		sendToBuses = Boolean.parseBoolean(values.get("sendToBuses"));
	}

	public boolean forAllRoutes (){
		return configuredRoutes.size()==0;
	}

	public void addConfiguredRoute (ConfiguredRoute route){
		configuredRoutes.add(route);
	}

	public List <ConfiguredRoute> getConfiguredRoutes (){
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
}