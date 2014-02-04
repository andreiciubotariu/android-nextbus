package com.andrei.nextbus.library.parsers;

public class DepthTagPair {
	int depth;
	String tag;
	
	public DepthTagPair(int depth, String tag){
		this.depth = depth;
		this.tag = tag;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public String getTag(){
		return tag;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + depth;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DepthTagPair other = (DepthTagPair) obj;
		if (depth != other.depth) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		return true;
	}


}
