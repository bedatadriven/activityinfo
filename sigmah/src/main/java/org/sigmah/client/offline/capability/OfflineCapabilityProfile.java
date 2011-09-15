package org.sigmah.client.offline.capability;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Provides information about the current browser's capability
 * for offline mode.
 * 
 * This default implementation is for fully supported browsers: webkit and opera.
 */
public abstract class OfflineCapabilityProfile {


	public boolean isOfflineModeSupported() {
		return false;
	}
	
	/**
	 * 
	 * @return installation instructions for offline mode for this 
	 * specific browser, or null if no installation is required to use offline mode
	 */
	public String getInstallInstructions() {
		return null;
	}
	
	/**
	 * Acquire all necessary permissions from the user to use offline mode.
	 * 
	 * @param callback
	 * @throws UnsupportedOperationException if this browser does not support offline mode
	 */
	public void acquirePermission(AsyncCallback<Void> callback) {
		callback.onFailure(new UnsupportedOperationException());
	}

	public boolean hasPermission() {
		throw new UnsupportedOperationException("offline mode is not supported");
	}
}
