/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.LoadListener;

/**
 * Base class for command-pattern-based implementations of GXT {@link com.extjs.gxt.ui.client.data.Loader}
 * interfaces
 *
 * @deprecated Use standard GXT loader with custom {@link com.extjs.gxt.ui.client.data.DataProxy}
 * @author Alex Bertram (akbertram@gmail.com)
 */
abstract class AbstractLoader<ResultT> extends BaseObservable implements Loader<ResultT> {
    @Override
    public void addLoadListener(LoadListener listener) {
        this.addListener(Loader.BeforeLoad, listener);
        this.addListener(Loader.Load, listener);
        this.addListener(Loader.LoadException, listener);
    }

    @Override
    public void removeLoadListener(LoadListener listener) {
        this.removeListener(Loader.BeforeLoad, listener);
        this.removeListener(Loader.Load, listener);
        this.removeListener(Loader.LoadException, listener);
    }
}
