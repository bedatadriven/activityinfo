package org.sigmah.client.offline.sync;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;

public class SynchronizerStats {

	
	double timeStarted;
	double timeWaitingForServer = 0;
	double timeWaitingForLocalDatabase = 0;
	
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
