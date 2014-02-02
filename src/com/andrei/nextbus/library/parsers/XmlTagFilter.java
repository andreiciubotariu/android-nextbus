package com.andrei.nextbus.library.parsers;

import com.andrei.nextbus.library.objects.MapInitializable;

public class XmlTagFilter {

	private XmlTagFilter parent;
	private int wantedDepth;
	private String wantedTag;

	private String wantedAttribute;
	private String wantedAtributeValue;
	
	private Class <? extends MapInitializable> clazz;

	public XmlTagFilter (int depth, String wantedTag){
		this(depth,wantedTag,null);
	}
	
	public XmlTagFilter (int depth, String wantedTag, Class <? extends MapInitializable> clazz){
		this (depth, wantedTag, clazz, null);
	}
	public XmlTagFilter (int depth, String wantedTag, Class <? extends MapInitializable> clazz, XmlTagFilter parent){
		this.wantedDepth = depth;
		this.wantedTag = wantedTag;
		this.clazz = clazz;
		this.parent = parent;
	}

	public int getDepth (){
		return wantedDepth;
	}
	
	public String getTag (){
		return wantedTag;
	}
	
	public String getAttribute (){
		return wantedAttribute;
	}

	public String getAttributeValue (){
		return wantedAtributeValue;
	}
	
	public Class <? extends MapInitializable> getTargetClass (){
		return clazz;
	}
	
	public XmlTagFilter getParent (){
		return parent;
	}
	
	public void setAttrributeSpec (String attrib,String value){
		this.wantedAttribute = attrib;
		this.wantedAtributeValue = value;
	}
}
