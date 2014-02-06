package com.andrei.nextbus.library.objects;

import java.util.Map;

public abstract class XmlObj{
	public abstract void init (Map <String,String> attributes);

	public abstract void add (XmlObj m);

	public abstract void setText(String text);
	
}