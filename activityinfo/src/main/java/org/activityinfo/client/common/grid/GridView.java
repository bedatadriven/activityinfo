package org.activityinfo.client.common.grid;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;

import org.activityinfo.client.command.monitor.AsyncMonitor;

import java.util.List;
/*
 * @author Alex Bertram
 */

public interface GridView<PresenterT extends GridPresenter, ModelT extends ModelData> {

    public void setActionEnabled(String actionId, boolean enabled);

    public void confirmDeleteSelected(ConfirmCallback callback);

    public ModelT getSelection();

    public AsyncMonitor getDeletingMonitor();

    public AsyncMonitor getSavingMonitor();


}
