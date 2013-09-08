package com.andrei.nextbusbridge;

import java.util.Map;

public class Prediction {

	private String mBlock, mDirTag, mTripTag, mBranch,mVehicle;
	private long mEpochTime;
	private int mSeconds,mMinutes;
	private boolean mIsDeparture, mAffectedByLayover, mIsSheduleBased, mDelayed;
	
	public Prediction (){
		
	}
	
	public Prediction (Map<String, String> map){
		mBlock = map.get("block");
		mDirTag = map.get("dirTag");
		mTripTag = map.get("tripTag");
		mBranch = map.get("branch");
		mVehicle = map.get("vehicle");
		
		mEpochTime = Long.parseLong(map.get("epochTime"));
		
		mSeconds = Integer.parseInt (map.get("seconds"));
		mMinutes = Integer.parseInt (map.get("minutes"));
		
		mIsDeparture = Boolean.parseBoolean("isDeparture");
		mAffectedByLayover = Boolean.parseBoolean("affectedByLayover");
		mIsSheduleBased = Boolean.parseBoolean("isScheduleBased");
		mDelayed = Boolean.parseBoolean("delayed");
	}

	public String getBlock() {
		return mBlock;
	}

	public void setBlock(String block) {
		this.mBlock = block;
	}

	public String getDirTag() {
		return mDirTag;
	}

	public void setDirTag(String dirTag) {
		this.mDirTag = dirTag;
	}

	public String getTripTag() {
		return mTripTag;
	}

	public void setTripTag(String tripTag) {
		this.mTripTag = tripTag;
	}

	public String getBranch() {
		return mBranch;
	}

	public void setBranch(String branch) {
		this.mBranch = branch;
	}

	public boolean isDeparture() {
		return mIsDeparture;
	}

	public void setDeparture(boolean isDeparture) {
		this.mIsDeparture = isDeparture;
	}

	public boolean isAffectedByLayover() {
		return mAffectedByLayover;
	}

	public void setAffectedByLayover(boolean affectedByLayover) {
		this.mAffectedByLayover = affectedByLayover;
	}

	public boolean isSheduleBased() {
		return mIsSheduleBased;
	}

	public void setSheduleBased(boolean isSheduleBased) {
		this.mIsSheduleBased = isSheduleBased;
	}

	public boolean isDelayed() {
		return mDelayed;
	}

	public void setDelayed(boolean delayed) {
		this.mDelayed = delayed;
	}

	public String getVehicle() {
		return mVehicle;
	}

	public void setVehicle(String vehicle) {
		this.mVehicle = vehicle;
	}

	public long getEpochTime() {
		return mEpochTime;
	}

	public void setEpochTime(long epochTime) {
		this.mEpochTime = epochTime;
	}

	public int getSeconds() {
		return mSeconds;
	}

	public void setSeconds(int seconds) {
		this.mSeconds = seconds;
	}

	public int getMinutes() {
		return mMinutes;
	}

	public void setMinutes(int minutes) {
		this.mMinutes = minutes;
	}
}
