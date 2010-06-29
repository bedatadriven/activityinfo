/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import org.sigmah.server.domain.Authentication;

public class HostPageModel extends PageModel {
    private Authentication auth;
    private boolean offline;

    public HostPageModel(Authentication auth) {
        this.auth = auth;
    }


    public Authentication getAuth() {
        return auth;
    }

    public boolean isOffline() {
        return offline;
    }
}
