package org.sigmah.client.offline.capability;


/**
 * Provides information about the current browser's capability
 * for offline mode.
 * 
 * This default implementation is for fully supported browsers: webkit and opera.
 */
public class OfflineCapabilityProfile {

	public boolean isOfflineModeSupported() {
		return true;
	}
	
	public String getStartupMessageHtml() {
		return ProfileResources.INSTANCE.startupMessage().getText();
	}
}
