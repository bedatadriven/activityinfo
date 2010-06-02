/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.client.offline.sync;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.modulestore.client.ModuleStores;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstallSteps {

    private List<Step> steps = new ArrayList<Step>();
    private Iterator<Step> stepIt;

    public InstallSteps() {
        steps.add(new CacheScript(ModuleStores.getCommon()));
        steps.add(new CacheScript(ModuleStores.getPermutation()));
    }

    public void run() {
        stepIt = steps.iterator();
        runNext();
    }

    private void runNext() {
        while(stepIt.hasNext()) {
            final Step step = stepIt.next();
            if(!step.isComplete()) {
                executeStep(step);
            }
        }
        Log.debug("Offline installation done!");
    }

    private void executeStep(final Step step) {
        Log.debug("Executing offine installation step: " + step.getDescription());
        step.execute(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Offline installation step failed: " + step.getDescription(), throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                runNext();
            }
        });
    }
}
