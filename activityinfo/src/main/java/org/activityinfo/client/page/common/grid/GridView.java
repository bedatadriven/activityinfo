package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import org.activityinfo.client.command.monitor.AsyncMonitor;
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
