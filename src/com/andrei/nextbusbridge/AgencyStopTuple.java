package com.andrei.nextbusbridge;

public class AgencyStopTuple {
	private String both;

	public AgencyStopTuple (String agencyTag, String stopTag){
		both = agencyTag + "|" + stopTag;
	}
	
	public String getConcatedTuple (){
		return both;
	}
}
