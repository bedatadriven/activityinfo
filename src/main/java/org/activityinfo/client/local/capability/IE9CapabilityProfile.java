package org.activityinfo.client.local.capability;

/**
 * Internet Explorer 9 capability profile
 * 
 * @author alex
 *
 */
public class IE9CapabilityProfile extends LocalCapabilityProfile {

	@Override
	public boolean isOfflineModeSupported() {
		return false;
	}

	@Override
	public String getInstallInstructions() {
		return ProfileResources.INSTANCE.startupMessageIE9().getText();
	}

}
