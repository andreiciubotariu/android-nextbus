package com.andrei.nextbus.library;

public class AgencyStopPair {
	private String both;

	public AgencyStopPair (String agencyTag, String stopTag){
		both = agencyTag + "|" + stopTag;
	}
	
	public String getConcatedTuple (){
		return both;
	}
}
