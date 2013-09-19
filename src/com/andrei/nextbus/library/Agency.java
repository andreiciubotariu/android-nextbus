package com.andrei.nextbus.library;

import java.util.Map;

public class Agency implements BaseInformationProvider,MapInitializable {

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

	public String getmShortTitle() {
		return mShortTitle;
	}

	public String getmRegionTitle() {
		return mRegionTitle;
	}
}
