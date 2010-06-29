/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.shared.command.result.VoidResult;

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
