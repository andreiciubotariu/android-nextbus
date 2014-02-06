package com.andrei.nextbus.library.objects;

import java.util.Map;

public class BareMessage extends XmlObj {
	private String text;
	private String priority;
	
	public BareMessage(){
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