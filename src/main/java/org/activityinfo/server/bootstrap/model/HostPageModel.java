/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap.model;


public class HostPageModel extends PageModel {
    private String appUrl;
    private boolean appCacheEnabled;
    private String mapsApiKey;
    
    public HostPageModel(String appUrl) {
        this.appUrl = appUrl;
    }


    public String getAppUrl() {
        return appUrl;
    }

	public boolean isAppCacheEnabled() {
		return appCacheEnabled;
	}

	public void setAppCacheEnabled(boolean appCacheEnabled) {
		this.appCacheEnabled = appCacheEnabled;
	}

	public String getMapsApiKey() {
		return mapsApiKey;
	}

	public void setMapsApiKey(String mapsApiKey) {
		this.mapsApiKey = mapsApiKey;
	}
}
