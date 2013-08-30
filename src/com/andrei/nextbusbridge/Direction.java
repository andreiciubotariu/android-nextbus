package com.andrei.nextbusbridge;

import java.util.HashMap;

public class Direction implements BaseInformationProvider{

	public final static String [] TAGS = new String [] {"body","route","direction"};
	private String mTag, mTitle, mName, mBranch;
	private boolean mUseForUi = true;
	
	public Direction(HashMap<String, String> values) {
		mTag = values.get("tag");
		mTitle = values.get("title");
		mName = values.get("name");
		mBranch = values.get("branch");
		mUseForUi = Boolean.parseBoolean("useForUI");
	}

	@Override
	public String getTag() {
		return mTag;
	}

	public void setTag (String newTag){
		mTag =  newTag;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public void setTitle (String newTitle){
		mTitle = newTitle;
	}
	
	public String getName (){
		return mName;
	}
	
	public void setName (String newName){
		mName = newName;
	}

	public String getBranch (){
		return mBranch;
	}

	public void setBranch (String newBranch){
		mBranch = newBranch;
	}

	public boolean usedForUI (){
		return mUseForUi;
	}

	public void setForUI (boolean forUI){
		mUseForUi = forUI;
	}
}
