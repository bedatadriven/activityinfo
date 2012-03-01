package org.sigmah.client.offline.ui;

import org.sigmah.client.offline.OfflineController.EnableCallback;

public class ConfirmEnableOfflineDialog extends BasePromptDialog {
	
	public ConfirmEnableOfflineDialog(EnableCallback callback) {
		super(capabilityProfile.getInstallInstructions());
	}

}
