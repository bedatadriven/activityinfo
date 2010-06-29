/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.remote.Authentication;

import java.util.Date;

public class CacheUserDetails implements Step {

    private final Authentication authentication;

    @Inject
    public CacheUserDetails(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Caching user information";
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
        Date now = new Date();
        Date expires = new Date(now.getTime() * 1000 * 60 * 60 * 24 * 365);
        Cookies.setCookie("userId", Integer.toString(authentication.getUserId()), expires);
        Cookies.setCookie("email", authentication.getEmail(), expires);

        callback.onSuccess(null);
    }
}
