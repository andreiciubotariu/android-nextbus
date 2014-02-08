package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Message extends XmlObj {
	private String text;
	private String priority;
	
	public Message(){
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		text=attributes.get("text");
		priority=attributes.get("priority");
	}

	public String getText(){
		return text;
	}
	
	public String getPriority(){
		return priority;
	}
	
	@Override
	public void add(XmlObj m) {
		//no-op
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

}