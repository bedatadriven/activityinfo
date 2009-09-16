package org.activityinfo.client.command.loader;

import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class AbstractTreeLoader<M extends ModelData> extends AbstractLoader<List<M>> implements
        TreeLoader<M> {

    @Override
    public boolean load() {
        return loadRootNodes();

    }

    protected abstract boolean loadRootNodes();

    @Override
    public boolean load(Object loadConfig) {
        if(loadConfig == null)
            return loadRootNodes();
        else
            return loadChildren((M)loadConfig);
    }

}
