package com.andrei.nextbus_demo.workers;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.andrei.nextbus_demo.workers.TaskContainerFragment.TaskCallbacks;

public class AsyncActivity <T> extends FragmentActivity implements TaskCallbacks <T>{


	@Override
	public void onPreExecute(String tag){

	}

	@Override
	public void onProgressUpdate(String tag, int percent){

	}

	@Override
	public void onCancelled(String tag){

	}

	@Override
	public void onPostExecute(String workerTag, T result){
		System.out.println ("Activity delivering result to " + workerTag.split("-")[0]);
		FragmentManager manager = getSupportFragmentManager();
		ResultListener l = (ResultListener) manager.findFragmentByTag(workerTag.split("-")[0]);
		if (l != null){
			l.onResultObtained(result);
		}
	}



}