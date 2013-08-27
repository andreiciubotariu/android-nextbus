package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Agency implements BaseInformationProvider {

	private final static String [] TAGS = new String [] {"body", "agency"};
	private String mTag, mTitle, mShortTitle, mRegionTitle;

	public Agency (String tag, String title, String shortTitle, String regionTitle){
		mTag = tag;
		mTitle = title;
		mShortTitle = shortTitle;
		mRegionTitle = regionTitle;
	}

	public Agency (HashMap<String, String> values){
		mTag = values.get("tag");
		mTitle = values.get("title");
		mShortTitle = values.get("shortTitle");
		mRegionTitle = values.get("regionTitle");
	}

	@Override
	public String getTag() {
		return mTag;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public String getmShortTitle() {
		return mShortTitle;
	}

	public String getmRegionTitle() {
		return mRegionTitle;
	}

	public static List <Agency> getAgencies(){
		List <HashMap <String, String>> rawObjects = Parser.parse(TAGS, "http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList");
		List <Agency> agencies = new ArrayList <Agency>();
		for (int x = 0; x < rawObjects.size(); x++){
			agencies.add(new Agency (rawObjects.get(x)));
		}

		return agencies;
	}
}
