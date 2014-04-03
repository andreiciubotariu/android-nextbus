package com.andrei.nextbus_demo.workers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * This Fragment manages a single background task and retains 
 * itself across configuration changes.
 */
public abstract class TaskContainerFragment <T> extends Fragment implements Startable{

	public static final String WORKER_SUFFIX = "-worker";
  /**
   * Callback interface through which the fragment will report the
   * task's progress and results back to the Activity.
   */
  public static interface TaskCallbacks <T> {
    void onPreExecute(String tag);
    void onProgressUpdate(String tag, int percent);
    void onCancelled(String tag);
    void onPostExecute(String tag, T result);
  }

  private TaskCallbacks <T> mCallbacks;
  private T mResult;
  private AsyncTask <Void,Void,T> mTask;

  /**
   * Hold a reference to the parent Activity so we can report the
   * task's current progress and results. The Android framework 
   * will pass us a reference to the newly created Activity after 
   * each configuration change.
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mCallbacks = (TaskCallbacks<T>) activity;
    deliverResult();
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

    // Create and execute the background task.
    start(true);
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
  
  @Override
  public void start(boolean forceRefresh){
	  if (forceRefresh || mTask == null || (mTask != null && mResult == null && mTask.getStatus().equals(Status.FINISHED))){
		  System.out.println ("started");
		  mTask = getAsyncTask().execute();
	  }
	  else if (mResult != null ){
		  deliverResult();
	  }
	  System.out.println ("called");
  }
  
  public T getResult(){
	  return mResult;
  }
  
  public void storeResult (T result){
	  System.out.println ("Storing result");
	 mResult = result;
  }
  
  public TaskCallbacks <T> getCallBacks(){
	  return mCallbacks;
  }
  
  public void deliverResult(){
	  if (mCallbacks != null && mResult != null){
		  System.out.println ("delivering result to parent activity");
		  mCallbacks.onPostExecute(getTag(), mResult);
	  }
	  else {
		  System.out.println("Could not deliver result. Callback or result null");
	  }
  }

  protected abstract AsyncTask <Void,Void,T> getAsyncTask();
  
  
  
  public abstract class StoredResultTask extends AsyncTask <Void, Void, T>{
	  
	  @Override
	  public final void onPostExecute (T result){
		  storeResult (result);
		  deliverResult();
	  }
  }
}