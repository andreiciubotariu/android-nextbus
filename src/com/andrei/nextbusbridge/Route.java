package com.andrei.nextbusbridge;

import java.util.HashMap;

public class Route implements BaseInformationProvider {

	private String mTitle, mTag;
	
	public Route (String tag, String title){
		mTag = tag;
		mTitle = title;
	}
	
	public Route(HashMap<String, String> values) {
		mTag = values.get("tag");
		mTitle = values.get("title");
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
