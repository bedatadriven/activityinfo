package org.activityinfo.client.local.capability;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Capability profile for web-kit based browsers,
 * for which offline access is fully supported.
 *
 */
public class WebKitCapabilityProfile extends LocalCapabilityProfile {

	@Override
	public boolean isOfflineModeSupported() {
		return true;
	}

	@Override
	public String getInstallInstructions() {
		return null;
	}

	@Override
	public void acquirePermission(AsyncCallback<Void> callback) {
		// no user permission required for web kit
		callback.onSuccess(null);
	}

	@Override
	public boolean hasPermission() {
		return true;
	}
}
