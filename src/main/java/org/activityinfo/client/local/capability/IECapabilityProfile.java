package org.activityinfo.client.local.capability;



/**
 * Internet Explorer 6-8 offline capability profile.
 * 
 * If gears is not installed, the user is prompted to install.
 * 
 * IE itself does not support offline features. There is an "experimental" indexeddb plugin
 * that can be downloaded from MS lab's web site, but there is absolutely no appcache support,
 * even in IE9.
 */
public class IECapabilityProfile extends GearsCapabilityProfile {


	@Override
	public String getInstallInstructions() {
		return ProfileResources.INSTANCE.startupMessageIE().getText();
	}
	
}
