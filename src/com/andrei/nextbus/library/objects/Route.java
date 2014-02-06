package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Route extends BaseInfoObj {

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

	@Override
	public void add(XmlObj m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
}