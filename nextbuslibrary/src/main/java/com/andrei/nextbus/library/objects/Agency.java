package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Agency extends BaseInfoObj {

	private String mTag, mTitle, mShortTitle, mRegionTitle;

	public Agency (){	
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

	public String getShortTitle() {
		return mShortTitle;
	}

	public String getRegionTitle() {
		return mRegionTitle;
	}

	@Override
	public void add(XmlObj m) {
		//not used
	}
}