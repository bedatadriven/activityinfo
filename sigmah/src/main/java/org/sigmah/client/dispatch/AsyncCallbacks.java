/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Utility class for {@link AsyncCallback}s.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AsyncCallbacks {
    public final static AsyncCallback<Object> EMPTY_CALLBACK = new AsyncCallback<Object>() {
        @Override
        public void onFailure(Throwable caught) {}

        @Override
        public void onSuccess(Object result) {}
    };

    private AsyncCallbacks() {}

    public static <T> AsyncCallback<T> emptyCallback() {
        return (AsyncCallback<T>) EMPTY_CALLBACK;
    }
}
