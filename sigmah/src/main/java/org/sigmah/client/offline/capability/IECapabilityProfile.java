package org.sigmah.client.offline.capability;


import com.google.gwt.gears.client.Factory;

/**
 * Internet Explorer 6-8 offline capability profile.
 * 
 * If gears is not installed, the user is prompted to install.
 * 
 * IE itself does not support offline features. There is an "experimental" indexeddb plugin
 * that can be downloaded from MS lab's web site, but there is absolutely no appcache support,
 * even in IE9.
 */
public class IECapabilityProfile extends OfflineCapabilityProfile {

	@Override
	public boolean isOfflineModeSupported() {
		return gearsIsInstalled();
	}

	@Override
	public String getStartupMessageHtml() {
		if(gearsIsInstalled()) {
			return ProfileResources.INSTANCE.startupMessage().getText();
		} else {
			return ProfileResources.INSTANCE.startupMessageIE().getText();
		}
	}
	
	private boolean gearsIsInstalled() {
		return Factory.getInstance() != null;
	}

}
