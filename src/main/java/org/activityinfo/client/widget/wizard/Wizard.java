package org.activityinfo.client.widget.wizard;

import com.extjs.gxt.ui.client.event.BaseObservable;

public abstract class Wizard extends BaseObservable {

	public abstract WizardPage[] getPages();

	public abstract String getTitle();

	public boolean isPageEnabled(WizardPage wizardPage) {
		return true;
	}

	public boolean isFinishEnabled() {
		return true;
	}
	
}
