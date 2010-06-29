/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.shared.command.result.CreateResult;

/**
 * Convenience callback for commands that return CreateResult
 *
 * @author Alex Bertram
 */
public abstract class Created implements AsyncCallback<CreateResult> {

    /**
     * By default, this method does nothing on failure
     * <p/>
     * The provided {@link AsyncMonitor} should take care of any
     * UI stuff that needs to happen; you probably only need to
     * provide a substantive implementation here if something
     * needs to be rolled back.
     */
    @Override
    public void onFailure(Throwable caught) {

    }

    @Override
    public void onSuccess(CreateResult result) {
        created(result.getNewId());
    }

    public abstract void created(int newId);

}
