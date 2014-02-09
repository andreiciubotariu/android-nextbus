package com.andrei.nextbus.library.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.andrei.nextbus.library.objects.messages.DetailedMessage;
import com.andrei.nextbus.library.objects.messages.Message;

public class Route extends BaseInfoObj {

	private String mTitle, mTag;
	private List <DetailedMessage> messages;
	
	public Route (){
		messages = new ArrayList <DetailedMessage>();
	}
	
	public Route (String tag, String title){
		this();
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
	
	public List <DetailedMessage> getMessages(){
		return messages;
	}

	@Override
	public void add(XmlObj m){
		if (m instanceof DetailedMessage){
			messages.add((DetailedMessage)m);
		}
	}
}