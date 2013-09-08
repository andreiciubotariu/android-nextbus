package com.andrei.nextbusbridge;

import java.util.Map;

public class Agency implements BaseInformationProvider,MapReader {

	private String mTag, mTitle, mShortTitle, mRegionTitle;

	public Agency (){
		
	}
	public Agency (String tag, String title, String shortTitle, String regionTitle){
		mTag = tag;
		mTitle = title;
		mShortTitle = shortTitle;
		mRegionTitle = regionTitle;
	}

	public void init (Map<String, String> values){
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
}
