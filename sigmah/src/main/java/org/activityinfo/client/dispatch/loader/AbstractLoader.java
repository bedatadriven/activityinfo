package org.activityinfo.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.LoadListener;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class AbstractLoader<ResultT> extends BaseObservable implements Loader<ResultT> {
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
