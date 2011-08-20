package org.sigmah.client.offline.capability;


import com.google.gwt.gears.client.Factory;

/**
 * IE6-8 <i>can</i> run offline mode using gears, but if Gears is 
 * not already installed we encourage them to download Chrome Frame instead.
 * 
 * If ChromeFrame is installed, then we the webkit permutation should be loaded
 * and not this profile.
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
