package org.activityinfo.client.offline.sync;

import java.util.Date;

import org.activityinfo.client.Log;

/**
 * Logging utility to track where time is spent during sychronization.
 * Should compile out when logging is turned off.
 * 
 */
public class SynchronizerStats {

	
	private double timeStarted;
	private double timeWaitingForServer = 0;
	private double timeWaitingForLocalDatabase = 0;
	
	public void onStart() {
		if(Log.isDebugEnabled()) {
			timeStarted = now();
		}
	}
	
	public void onRemoteCallStarted() {
		if(Log.isDebugEnabled()) {
			timeWaitingForServer -= now();
		}
	}

	public void onRemoteCallFinished() {
		if(Log.isDebugEnabled()) {
			timeWaitingForServer += now();
		}
	}

	public void onDbUpdateStarted() {
		if(Log.isDebugEnabled()) {
			timeWaitingForLocalDatabase -= now();
		}
	}

	public void onDbUpdateFinished() {
		if(Log.isDebugEnabled()) {
			timeWaitingForLocalDatabase += now();			
		}
	}

	public void onFinished() {
		if(Log.isDebugEnabled()) {
			double totalLength = now() - timeStarted;
			Log.debug("Sync stats: total time: " + totalLength / 1000 + " s");
			Log.debug("Sync stats: time waiting for remote server: " + timeWaitingForServer / 1000d + " s");
			Log.debug("Sync stats: time waiting for local database updates: " + timeWaitingForLocalDatabase / 1000d + " s");
		}
	}
	
	private double now() {
		return new Date().getTime();
	}
	
}
