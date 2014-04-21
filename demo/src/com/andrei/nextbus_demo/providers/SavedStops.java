package com.andrei.nextbus_demo.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class SavedStops implements BaseColumns {
	
	public static final Uri CONTENT_URI = Uri.parse("content://" 
			+ SavedStopProvider.AUTHORITY + "/saved_stops");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.andrei.savedstops";

	public static final String TAG = "stop_tag";

	public static final String TITLE = "stop_title";

	public static final String STOP_ID  = "stopId";
	
	public static final String LAT = "lat";
	
	public static final String LON = "lon";
	
	public static final String AGENCY_TAG = "agency_tag";
	
	public static final String AGENCY_TITLE = "agency_title";
	
	public static final String ROUTE_TAG = "route_tag";
	
	public static final String ROUTE_TITLE = "route_title";
	
	public static final String DIR_TITLE = "dir_title";
	
	public static final String DIR_TAG = "dir_tag";
}
