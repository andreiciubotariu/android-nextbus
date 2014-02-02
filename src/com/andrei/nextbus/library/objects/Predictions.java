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
	
	private List <Prediction> predictionList = new ArrayList <Prediction>();
	
	@Override
	public void init(Map<String, String> attributes) {
		agencyTitle = attributes.get("agencyTitle");
		agencyTitle = attributes.get("routeTitle");
		agencyTitle = attributes.get("routeTag");
		agencyTitle = attributes.get("stopTitle");
		agencyTitle = attributes.get("stopTag");
	}
	
	public void addPrediction (Prediction p){
		predictionList.add(p);
	}
	
	public List<Prediction> getPredictions(){
		return predictionList;
	}

	@Override
	public void add(MapInitializable m) {
		if (m instanceof Prediction){
			predictionList.add((Prediction)m);
		}
		
	}

}
