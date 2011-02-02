/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import org.sigmah.server.domain.Authentication;

public class HostPageModel extends PageModel {
    private Authentication auth;
    private boolean offline;
    private String appUrl;

    public HostPageModel(Authentication auth, String appUrl) {
        this.auth = auth;
        this.appUrl = appUrl;
    }


    public Authentication getAuth() {
        return auth;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public boolean isOffline() {
        return offline;
    }
}
