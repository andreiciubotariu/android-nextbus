package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Direction extends BaseInfoObj{
	private String title;
	private List <Prediction> predictions = new ArrayList <Prediction>();
	
	@Override
	public void init(Map<String, String> attributes) {
		this.title = attributes.get("title");
	}

	@Override
	public void add(XmlObj m) {
		if (m instanceof Prediction){
			predictions.add((Prediction)m);
		}
	}

	@Override
	public String getTag() {
		return null;
	}

	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle (String s){
		title = s;
	}
	
	public List<Prediction> getPredictions(){
		return predictions;
	}
}