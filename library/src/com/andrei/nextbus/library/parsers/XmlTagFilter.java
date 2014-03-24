package com.andrei.nextbus.library.parsers;

import com.andrei.nextbus.library.objects.XmlObj;

public class XmlTagFilter {

	private XmlTagFilter parent;
	private int wantedDepth;
	private String wantedTag;

	private String wantedAttribute;
	private String wantedAtributeValue;
	
	private Class <? extends XmlObj> clazz;

	public XmlTagFilter (int depth, String wantedTag){
		this(depth,wantedTag,null);
	}
	
	public XmlTagFilter (int depth, String wantedTag, Class <? extends XmlObj> clazz){
		this (depth, wantedTag, clazz, null);
	}
	public XmlTagFilter (int depth, String wantedTag, Class <? extends XmlObj> clazz, XmlTagFilter parent){
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
	
	public Class <? extends XmlObj> getTargetClass (){
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