package com.andrei.nextbus.library.objects;

import java.util.Map;

public class DetailedDirection extends Direction{

	private String mTag, mName, mBranch;
	private boolean mUseForUi = true;
	
	public DetailedDirection (){
	}
	
	public void init (Map<String, String> attributes) {
		super.init(attributes);
		mTag = attributes.get("tag");
		mName = attributes.get("name");
		mBranch = attributes.get("branch");
		mUseForUi = Boolean.parseBoolean("useForUI");
	}

	@Override
	public String getTag() {
		return mTag;
	}

	public void setTag (String newTag){
		mTag =  newTag;
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