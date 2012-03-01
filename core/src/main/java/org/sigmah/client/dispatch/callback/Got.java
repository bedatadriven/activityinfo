/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Convenience callback class for commands that do not
 * expect error. (Connection failures are handled at higher levels)
 *
 * @param <T>
 */
public abstract class Got<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable arg0) {
      /* No op. Errors are handled at higher levels */
    }

    @Override
    public void onSuccess(T result) {
        got(result);
    }

    public abstract void got(T result);

}
