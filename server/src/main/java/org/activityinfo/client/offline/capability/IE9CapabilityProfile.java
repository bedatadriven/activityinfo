package org.activityinfo.client.offline.capability;

/**
 * Internet Explorer 9 capability profile
 * 
 * @author alex
 *
 */
public class IE9CapabilityProfile extends OfflineCapabilityProfile {

	@Override
	public boolean isOfflineModeSupported() {
		return false;
	}

	@Override
	public String getInstallInstructions() {
		return ProfileResources.INSTANCE.startupMessageIE9().getText();
	}

}
