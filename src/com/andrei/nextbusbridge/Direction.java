package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Direction implements BaseInformationProvider{

	public final static String [] TAGS = new String [] {"body","route","direction"};
	String mTag, mTitle, mName, mBranch;
	boolean mUseForUi = true;

	public Direction(HashMap<String, String> values) {
		mTag = values.get("tag");
		mTitle = values.get("title");
		mName = values.get("name");
		mUseForUi = Boolean.parseBoolean("useForUI");
	}

	@Override
	public String getTag() {
		return mTag;
	}

	public void setTag (String newTag){
		mTag =  newTag;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public void setTitle (String newTitle){
		mTitle = newTitle;
	}

	public String getBranch (){
		return mBranch;
	}

	public void setBranch (String newBranch){
		mBranch = newBranch;
	}

	public boolean usedForUI (){
		return mUseForUi;
	}

	public void setForUI (boolean forUI){
		mUseForUi = forUI;
	}
	
	public static List <Direction> getDirections(){
		List <HashMap <String, String>> rawObjects = Parser.parse(TAGS, "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=ttc&r=104");
		List <Direction> directions = new ArrayList <Direction>();
		for (int x = 0; x < rawObjects.size(); x++){
			directions.add(new Direction(rawObjects.get(x)));
		}

		return directions;
	}
}
