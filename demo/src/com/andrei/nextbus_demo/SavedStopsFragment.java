package com.andrei.nextbus_demo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;

import com.andrei.nextbus_demo.providers.SavedStops;
import com.andrei.nextbus_demo.stop_display.StopDisplayActivity;
import com.andrei.nextbus_demo.wizard.ChooserFragment;

public class SavedStopsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	/*
	 * Defines an array that contains column names to move from
	 * the Cursor to the ListView.
	 */

	private final static String[] FROM_COLUMNS = {
		SavedStops.TITLE, SavedStops.ROUTE_TITLE, SavedStops.DIR_TITLE, SavedStops.AGENCY_TITLE, 
	};

	private static final String[] PROJECTION = {
		SavedStops._ID,
		SavedStops.TITLE,
		SavedStops.TAG,
		SavedStops.AGENCY_TAG,
		SavedStops.AGENCY_TITLE,
		SavedStops.ROUTE_TAG,
		SavedStops.ROUTE_TITLE,
		SavedStops.DIR_TITLE,
		SavedStops.DIR_TAG
	};

	/*
	 * Defines an array that contains resource ids for the layout views
	 * that get the Cursor column contents.
	 */
	private final static int[] TO_IDS = {
		R.id.stop_name, R.id.route_title,R.id.dir_name, R.id.agency_name
		/*, R.id.agency_name,R.id.agency_name, R.id.agency_name*/
	};

	private static final String TAG = "ContactsFragment";

	// An adapter that binds the result Cursor to the ListView
	private SimpleCursorAdapter mCursorAdapter;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Gets a CursorAdapter
		mCursorAdapter = new SimpleCursorAdapter(
				getActivity(),
				R.layout.fav_stop_listitem,
				null,
				FROM_COLUMNS, 
				TO_IDS,
				0);
		// Sets the adapter for the ListView
		setListAdapter(mCursorAdapter);

		//change space between list items
		ListView listView = getListView();
		listView.setFastScrollEnabled(true);
		listView.setDivider(null);
		int dividerSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
		listView.setDividerHeight(dividerSize);
		listView.setCacheColorHint(Color.TRANSPARENT);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View item, int position, long rowID) {
     String stopTitle = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.TITLE));
     String stopTag = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.TAG));
     String agencyTag = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.AGENCY_TAG));
     String agencyTitle = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.AGENCY_TITLE));
     String routeTag = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.ROUTE_TAG));
     String routeTitle = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.ROUTE_TITLE));
     String dirTitle = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.DIR_TITLE));
     String dirTag = mCursorAdapter.getCursor().getString(mCursorAdapter.getCursor().getColumnIndex(SavedStops.DIR_TAG));
     
     
     Intent i = new Intent (getActivity(), StopDisplayActivity.class);
     i.putExtra(ChooserFragment.KEY_STOP_TITLE, stopTitle);
     i.putExtra(ChooserFragment.KEY_STOP_TAG, stopTag);
     i.putExtra(ChooserFragment.KEY_AGENCY_TAG, agencyTag);
     i.putExtra(ChooserFragment.KEY_AGENCY_TITLE, agencyTitle);
     i.putExtra(ChooserFragment.KEY_ROUTE_TAG, routeTag);
     i.putExtra(ChooserFragment.KEY_ROUTE_TITLE, routeTitle);
     i.putExtra(ChooserFragment.KEY_DIR_TITLE, dirTitle);
     i.putExtra(ChooserFragment.KEY_DIR_TAG, dirTag);
     
     startActivity(i);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		System.out.println ("LOADER CALLED");
		return new CursorLoader(
				getActivity(),
				SavedStops.CONTENT_URI,
				PROJECTION,
				null,//selection
				null,//selectionargs
				SavedStops.AGENCY_TITLE + " ASC," + SavedStops.ROUTE_TITLE + " ASC," + SavedStops.DIR_TITLE + " ASC," + SavedStops.TITLE + " ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		System.out.println ("LOAD FINISHED");
		mCursorAdapter.swapCursor(cursor);
		setEmptyText("No stops found");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.i(TAG, "Loader reset");
		mCursorAdapter.swapCursor(null);
	}
}