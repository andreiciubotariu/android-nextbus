package com.andrei.nextbusbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LinkedObject {

	private List <LinkedObject> nextDepth;
	private HashMap <String, String> attributes;
	private String tagName;
	private LinkedObject parent;
	
	public LinkedObject (){
		nextDepth = new ArrayList <LinkedObject>();
	}
	
	public LinkedObject (String tagName, HashMap <String,String> attributes){
		this();
		this.tagName = tagName;
		this.attributes = attributes;
	}
	
	public LinkedObject (LinkedObject parent,String tagName, HashMap <String,String> attributes){
		this (tagName, attributes);
		this.parent = parent;
	}
	
	public String getName (){
		return tagName;
	}
	
	public String getValue (String attribute){
		return attributes.get(attribute);
	}
	
	public List <LinkedObject> getNextDepth (){
		return nextDepth;
	}
	
	public void add (LinkedObject o){
		nextDepth.add (o);
	}
	
	public LinkedObject getParent (){
		return parent;
	}
}
