package com.andrei.nextbus.library.parsers;

public class XmlTagFilter {

	private int wantedDepth;
	private String wantedTag;

	private String wantedAttribute;
	private String wantedAtributeValue;

	public XmlTagFilter (int depth, String wantedTag){
		this.wantedDepth = depth;
		this.wantedTag = wantedTag;
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
	
	public void setAttrributeSpec (String attrib,String value){
		this.wantedAttribute = attrib;
		this.wantedAtributeValue = value;
	}
}
