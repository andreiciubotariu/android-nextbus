package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiStopDirection {
	public Map <String,String> attribs; 
	public List <Prediction> predictions = new ArrayList <Prediction> ();
}