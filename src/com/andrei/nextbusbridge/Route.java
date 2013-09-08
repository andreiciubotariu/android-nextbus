package com.andrei.nextbusbridge;

import java.util.Map;

public class Route implements BaseInformationProvider {

	private String mTitle, mTag;
	
	public Route (String tag, String title){
		mTag = tag;
		mTitle = title;
	}
	
	public Route(Map<String, String> map) {
		mTag = map.get("tag");
		mTitle = map.get("title");
	}

	@Override
	public String getTag() {
		return mTag;
	}
	
	@Override
	public String getTitle() {
		return mTitle;
	}
}
