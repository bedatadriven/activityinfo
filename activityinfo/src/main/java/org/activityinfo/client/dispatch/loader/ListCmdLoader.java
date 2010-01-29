package org.activityinfo.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.ListLoader;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetListCommand;
import org.activityinfo.shared.command.result.ListResult;

public class ListCmdLoader<ResultT extends ListResult> extends
        AbstractListCmdLoader<ResultT, GetListCommand<ResultT>>
        implements ListLoader<ResultT> {

    public ListCmdLoader(Dispatcher service) {
        super(service);
    }


}
