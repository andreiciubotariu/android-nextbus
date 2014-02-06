package com.andrei.nextbus.library.objects;

import java.util.Map;

public class Direction extends BaseInfoObj{

	private String mTag, mTitle, mName, mBranch;
	private boolean mUseForUi = true;
	
	public Direction (){
	}
	
	public void init (Map<String, String> map) {
		mTag = map.get("tag");
		mTitle = map.get("title");
		mName = map.get("name");
		mBranch = map.get("branch");
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

	@Override
	public void add(XmlObj m) {
		//no implementation
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
}