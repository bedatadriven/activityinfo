/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstallSteps {

    private List<Step> steps = new ArrayList<Step>();
    private Iterator<Step> stepIt;
    private EventBus eventBus;
    private AsyncCallback callback;

    @Inject
    public InstallSteps(EventBus eventBus, InitialSyncStep syncStep, CacheUserDetails cacheUserDetails,
                        CacheScript cacheScript) {
        this.eventBus = eventBus;
        steps.add(cacheUserDetails);
        if(GWT.isScript()) {
            // managed resources stores cause no end of problems in hosted mode,
            // so only invoke here if we are actually running in scripted mode
            steps.add(cacheScript);
        }
        steps.add(syncStep);
        steps.add(new ShortcutStep());
    }

    public void run(AsyncCallback callback) {
        this.callback = callback;
        stepIt = steps.iterator();
        runNext();
    }

    private void runNext() {
        while(stepIt.hasNext()) {
            final Step step = stepIt.next();
            if(!step.isComplete()) {
                executeStep(step);
                return;
            }
        }
        Log.debug("Offline installation done!");
        callback.onSuccess(null);
    }

    private void executeStep(final Step step) {
        Log.debug("Executing offline installation step: " + step.getDescription());
        step.execute(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
            	RuntimeException ex = new RuntimeException("Offline installation step failed: " + step.getDescription(), throwable);
                Log.error(ex.getMessage(), throwable);
                callback.onFailure(ex);
            }

            @Override
            public void onSuccess(Void aVoid) {
                runNext();
            }
        });
    }
}
