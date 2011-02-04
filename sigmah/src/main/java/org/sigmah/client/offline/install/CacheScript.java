/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.AuthTokenUtil;

public class CacheScript implements Step {
    private final Authentication authentication;
    protected AppCache appcache;

    @Inject
    public CacheScript(Authentication authentication) {
        this.authentication = authentication;
        this.appcache = AppCacheFactory.get();
    }

    @Override
    public boolean isComplete() {
        return false; // always force a version check
    }

    @Override
    public String getDescription() {
        return "AppCache";
    }

    @Override
    public void execute(final AsyncCallback<Void> callback) {
        AuthTokenUtil.ensurePersistentCookie(authentication);
        appcache.ensureCached(callback);
    }
}
