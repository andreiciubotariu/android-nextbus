package com.andrei.nextbus_demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

public class TaskFragment extends Fragment {

	  /**
	   * Callback interface through which the fragment will report the
	   * task's progress and results back to the Activity.
	   */
	  public static interface TaskCallbacks {
	    public void onPreExecute();
	    public void onProgressUpdate(int percent);
	    public void onCancelled();
	    public void onPostExecute();
	  }

	  private TaskCallbacks mCallbacks;
	  private DummyTask mTask;

	  /**
	   * Hold a reference to the parent Activity so we can report the
	   * task's current progress and results. The Android framework 
	   * will pass us a reference to the newly created Activity after 
	   * each configuration change.
	   */
	  @Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    mCallbacks = /*(TaskCallbacks) activity*/ new TaskCallbacks() {
			
			@Override
			public void onProgressUpdate(int percent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPostExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancelled() {
				// TODO Auto-generated method stub
				
			}
		};
	    System.out.println ("ATTACHED");
	  }

	  /**
	   * This method will only be called once when the retained
	   * Fragment is first created.
	   */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    // Retain this fragment across configuration changes.
	    setRetainInstance(true);

	    createAndRunTask();
	  }

	  public void createAndRunTask(){
		// Create and execute the background task.
		mTask = new DummyTask();
		mTask.execute();
	  }
	  /**
	   * Set the callback to null so we don't accidentally leak the 
	   * Activity instance.
	   */
	  @Override
	  public void onDetach() {
	    super.onDetach();
	    mCallbacks = null;
	  }

	  /**
	   * A dummy task that performs some (dumb) background work and
	   * proxies progress updates and results back to the Activity.
	   *
	   * Note that we need to check if the callbacks are null in each
	   * method in case they are invoked after the Activity's and
	   * Fragment's onDestroy() method have been called.
	   */
	  private class DummyTask extends AsyncTask<Void, Integer, Void> {

	    @Override
	    protected void onPreExecute() {
	      if (mCallbacks != null) {
	        mCallbacks.onPreExecute();
	      }
	    }

	    /**
	     * Note that we do NOT call the callback object's methods
	     * directly from the background thread, as this could result 
	     * in a race condition.
	     */
	    @Override
	    protected Void doInBackground(Void... ignore) {
	      for (int i = 0; !isCancelled() && i < 100; i++) {
	        SystemClock.sleep(100);
	        publishProgress(i);
	      }
	      return null;
	    }

	    @Override
	    protected void onProgressUpdate(Integer... percent) {
	      if (mCallbacks != null) {
	        mCallbacks.onProgressUpdate(percent[0]);
	      }
	    }

	    @Override
	    protected void onCancelled() {
	      if (mCallbacks != null) {
	        mCallbacks.onCancelled();
	      }
	    }

	    @Override
	    protected void onPostExecute(Void ignore) {
	      if (mCallbacks != null) {
	        mCallbacks.onPostExecute();
	      }
	    }
	  }
	}