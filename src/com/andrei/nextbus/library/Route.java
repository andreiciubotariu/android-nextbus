package com.andrei.nextbus.library;

import java.util.Map;

public class Route implements BaseInformationProvider,MapInitializable {

	private String mTitle, mTag;
	
	public Route (){
	}
	
	public Route (String tag, String title){
		mTag = tag;
		mTitle = title;
	}
	
	public void init (Map<String, String> map) {
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
