package com.andrei.nextbus.library.objects;

public abstract class BaseInfoObj extends XmlObj implements BaseInformationProvider {
	@Override
	public String toString(){
		return getTitle();
	}
}