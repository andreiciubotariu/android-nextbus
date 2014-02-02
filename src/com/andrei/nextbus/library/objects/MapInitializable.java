package com.andrei.nextbus.library.objects;

import java.util.Map;

public interface MapInitializable {
	public void init (Map <String,String> attributes);
	
	public void add (MapInitializable m);
}
