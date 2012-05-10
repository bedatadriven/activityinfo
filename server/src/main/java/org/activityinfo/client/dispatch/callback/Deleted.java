/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.callback;

import org.activityinfo.shared.command.result.VoidResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Convienence call back for Delete command calls.
 */
public abstract class Deleted implements AsyncCallback<VoidResult> {

    @Override
    public void onFailure(Throwable caught) {
    }


    @Override
    public void onSuccess(VoidResult result) {
        deleted();
    }

    /**
     * Called when upon succesfull deletion.
     */
    public abstract void deleted();

}
