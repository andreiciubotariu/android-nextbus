package com.andrei.nextbus_demo.providers;

import java.util.HashMap;

import com.andrei.nextbus_demo.wizard.ChooserFragment;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class SavedStopProvider extends ContentProvider {

	private static final String TAG = "SavedStopProvider";
	private final static String DATABASE_NAME  = "savedstops.db";
	private static final int DATABASE_VERSION  = 1;
	private static final String SavedStops_TABLE_NAME = "saved_stops";

	public static final String AUTHORITY = "com.andrei.nextbus_demo.providers.SavedStopProvider";
	private static final UriMatcher sUriMatcher;

	private static final int SavedStop = 1; //make it clear it is a constant.
	private static final int SavedStops_ID = 2;

	private static HashMap <String, String> SavedStopsProjectionMap;

	//store our table
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper (Context context){
			super (context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate (SQLiteDatabase db){
			String CREATE_PROFILES_TABLE = "CREATE TABLE " + SavedStops_TABLE_NAME + "("
					+ SavedStops._ID + " INTEGER PRIMARY KEY," 
					+ SavedStops.TAG +"  TEXT,"
					+ SavedStops.TITLE + " TEXT, "
					+ SavedStops.STOP_ID + " TEXT, "
					+ SavedStops.LAT + " TEXT, "
					+ SavedStops.LON + " TEXT, "
					+ SavedStops.AGENCY_TAG + " TEXT, "
					+ SavedStops.AGENCY_TITLE + " TEXT, "
					+ SavedStops.ROUTE_TAG + " TEXT, "
					+ SavedStops.ROUTE_TITLE + " TEXT, "
					+ SavedStops.DIR_TITLE + " TEXT,"
					+ SavedStops.DIR_TAG + " TEXT"
					+ ")";
			db.execSQL(CREATE_PROFILES_TABLE);
		}

		@Override
		public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + SavedStops_TABLE_NAME);
			onCreate(db);
		}
	}

	//instance of our table
	private DatabaseHelper mDbHelper;

	@Override
	public int delete (Uri uri, String selection, String [] selectionArgs){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)){
		case SavedStop:
			break;
		case SavedStops_ID:
			selection = selection + SavedStops._ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		int count = db.delete(SavedStops_TABLE_NAME, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType (Uri uri){
		switch (sUriMatcher.match(uri)){
		case SavedStop:
			return SavedStops.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != SavedStop) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor query = this.query(uri, new String []{SavedStops._ID}, SavedStops.TAG + " =?", new String[]{values.get(ChooserFragment.KEY_STOP_TAG).toString()}, null);
		if (query != null){
			if (query.moveToFirst()){
				values.put(SavedStops._ID, query.getString(0));
			}
			query.close();
		}
		long rowId = db.insertWithOnConflict(SavedStops_TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
		Log.d("Database","rowId of stop is: " + rowId);
		if (rowId > 0) {
			Uri contactUri = ContentUris.withAppendedId(SavedStops.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(contactUri, null);
			return contactUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SavedStops_TABLE_NAME);
		qb.setProjectionMap(SavedStopsProjectionMap);

		switch (sUriMatcher.match(uri)) {    
		case SavedStop:
			break;
		case SavedStops_ID:
			selection = (selection != null ? selection + " " : "") + SavedStops._ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor savedStopCursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		savedStopCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return savedStopCursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case SavedStop:
			count = db.update(SavedStops_TABLE_NAME, values, where, whereArgs);
			break;
		case SavedStops_ID:
			where = where + SavedStops._ID + " = " + uri.getLastPathSegment();
			count = db.update(SavedStops_TABLE_NAME, values, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, SavedStops_TABLE_NAME, SavedStop);
		sUriMatcher.addURI(AUTHORITY, SavedStops_TABLE_NAME + "/#", SavedStops_ID);

		SavedStopsProjectionMap = new HashMap<String, String>();
		SavedStopsProjectionMap.put(SavedStops._ID, SavedStops._ID);
		SavedStopsProjectionMap.put(SavedStops.TAG, SavedStops.TAG);
		SavedStopsProjectionMap.put(SavedStops.TITLE, SavedStops.TITLE);
		SavedStopsProjectionMap.put(SavedStops.STOP_ID, SavedStops.STOP_ID);
		SavedStopsProjectionMap.put(SavedStops.LAT, SavedStops.LAT);
		SavedStopsProjectionMap.put(SavedStops.LON, SavedStops.LON);
		SavedStopsProjectionMap.put(SavedStops.AGENCY_TAG, SavedStops.AGENCY_TAG);
		SavedStopsProjectionMap.put(SavedStops.AGENCY_TITLE, SavedStops.AGENCY_TITLE);
		SavedStopsProjectionMap.put(SavedStops.ROUTE_TAG, SavedStops.ROUTE_TAG);
		SavedStopsProjectionMap.put(SavedStops.ROUTE_TITLE, SavedStops.ROUTE_TITLE);
		SavedStopsProjectionMap.put(SavedStops.DIR_TITLE, SavedStops.DIR_TITLE);
		SavedStopsProjectionMap.put(SavedStops.DIR_TAG, SavedStops.DIR_TAG);
	}
}
