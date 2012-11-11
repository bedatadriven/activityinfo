/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap.model;

import javax.servlet.http.HttpServletRequest;


public class HostPageModel extends PageModel {
    private String appUrl;
    private boolean appCacheEnabled;
    private String mapsApiKey;
    private boolean redirectIfNoAuthCookie = true;
    
    public void setAppUrl(String appUrl) {
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
	
	public boolean isRedirectIfNoAuthCookie() {
		return redirectIfNoAuthCookie;
	}

	public void setRedirectIfNoAuthCookie(boolean redirectIfNoAuthCookie) {
		this.redirectIfNoAuthCookie = redirectIfNoAuthCookie;
	}

	/**
     * @return  The url used for the desktop shortcut
     */
    public static String computeAppUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(request.getServerName());
        if(request.getServerPort() != 80) {
           url.append(":").append(request.getServerPort());
        }
        url.append(request.getRequestURI());
        return url.toString();
    }
}
