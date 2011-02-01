/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.EventBus;

public class CacheScript implements Step {
    private final EventBus eventBus;
    protected AppCache appcache;
    private String status;

    public CacheScript(EventBus eventBus) {
        this.eventBus = eventBus;
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
      appcache.ensureCached(callback);
    }
}
