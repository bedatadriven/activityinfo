/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.modulestore.client.ModuleStores;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstallSteps {

    private List<Step> steps = new ArrayList<Step>();
    private Iterator<Step> stepIt;
    private AsyncCallback callback;

    @Inject
    public InstallSteps(InitialSyncStep syncStep, CacheUserDetails cacheUserDetails) {
        steps.add(cacheUserDetails);
        if(GWT.isScript()) {
            steps.add(new CacheScript(ModuleStores.getCommon()));
            steps.add(new CacheScript(ModuleStores.getPermutation()));
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
        Log.debug("Executing offine installation step: " + step.getDescription());
        step.execute(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Offline installation step failed: " + step.getDescription(), throwable);
                callback.onFailure(null);
            }

            @Override
            public void onSuccess(Void aVoid) {
                runNext();
            }
        });
    }
}
