package org.activityinfo.client.report.editor.map;

public interface Wizard {

	WizardPage[] getPages();

	String getTitle();

	boolean isPageEnabled(WizardPage wizardPage);

	
}
