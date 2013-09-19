package com.andrei.nextbus.library.objects;

public class AgencyStopPair {
	private String both;

	public AgencyStopPair (String agencyTag, String stopTag){
		both = agencyTag + "|" + stopTag;
	}
	
	public AgencyStopPair (Agency agency, Stop stop){
		this (agency.getTag(), stop.getTag());
	}
	
	public String getConcatedTuple (){
		return both;
	}
}
