package com.emporia.common.util;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.Log;

/**
 * this class is only to monitor the Activity life cycle, easy to debug program 
 * @author sky
 * @version 1.0
 */
public class ActivityLifecycleMonitor implements ActivityLifecycleCallbacks {
	private static final String TAG = ActivityLifecycleMonitor.class
			.getSimpleName();
	public static int sResumed;
	public static int sPaused;
	public static int sStarted;
	public static int sStopped;
	public static Activity currentActivity;

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		Log.i(TAG, "activity name:" + activity + " created");
	}

	@Override
	public void onActivityStarted(Activity activity) {
		Log.i(TAG, "activity name:" + activity + " started");
		sStarted++;
	}

	@Override
	public void onActivityResumed(Activity activity) {
		Log.i(TAG, "activity name:" + activity + " resumed");
		sResumed++;
		currentActivity = activity;

	}

	@Override
	public void onActivityPaused(Activity activity) {
		Log.i(TAG, "activity name:" + activity + " paused");
		sPaused++;
	}

	@Override
	public void onActivityStopped(Activity activity) {
		Log.i(TAG, "activity name:" + activity + " stopped");
		sStopped++;
	}
	
	public static boolean isApplicationVisible() {
		return sStarted > sStopped;
	} 
	
	public static boolean isApplicationResume() {
		return sResumed > sPaused;
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		Log.i(TAG, "activity name:" + activity + " saveInstanceState");
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		Log.i(TAG, "activity name:" + activity + " destroyed");
	}

}
