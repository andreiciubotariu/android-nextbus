package com.andrei.nextbus.library.objects;

import java.util.Map;

public abstract class XmlObj{
	private String text;
	public abstract void init (Map <String,String> attributes);

	public abstract void add (XmlObj m);

	public void setText(String text){
		this.text = text;
	}
	
	public  String getText(){
		return text;
	}
	
}