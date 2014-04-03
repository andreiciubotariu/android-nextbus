package com.andrei.nextbus_demo;

import android.support.v4.app.Fragment;

public abstract class MainFragment <T> extends Fragment{

	public abstract void onPreExecute ();
    public abstract void onProgressUpdate(int percent);
    public abstract void onCancelled();
    public abstract void onPostExecute(T result);
}