package org.activityinfo.client.command.loader;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.data.Loader;

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
