package com.andrei.nextbus.library.objects.messages;

import java.util.Map;

import com.andrei.nextbus.library.objects.XmlObj;

public class Message extends XmlObj {
	private String priority;
	
	public Message(){
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		setText(attributes.get("text"));
		priority=attributes.get("priority");
	}
	
	public String getPriority(){
		return priority;
	}
	
	@Override
	public void add(XmlObj m) {
		//no-op
	}
}