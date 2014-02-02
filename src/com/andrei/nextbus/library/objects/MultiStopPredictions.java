package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class MultiStopPredictions implements MapInitializable {
	public List <MultiStopDirection> directions = new ArrayList <MultiStopDirection> ();
	public Map <String,String> attribs;

	public MultiStopPredictions (){
	}
	
	public void init (Map <String,String> attribs){
			this.attribs = attribs;
	}

	@Override
	public void add(MapInitializable m) {
		// TODO Auto-generated method stub
		
	}
}
