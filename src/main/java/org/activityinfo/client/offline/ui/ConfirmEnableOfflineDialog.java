package org.activityinfo.client.offline.ui;

import org.activityinfo.client.offline.OfflineController.EnableCallback;

public class ConfirmEnableOfflineDialog extends BasePromptDialog {
	
	public ConfirmEnableOfflineDialog(EnableCallback callback) {
		super(capabilityProfile.getInstallInstructions());
	}

}
