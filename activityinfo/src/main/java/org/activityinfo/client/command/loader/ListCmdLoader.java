package org.activityinfo.client.command.loader;

import org.activityinfo.client.command.CommandService;
import org.activityinfo.shared.command.GetListCommand;
import org.activityinfo.shared.command.result.ListResult;

import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;

public class ListCmdLoader<ResultT extends ListResult> extends
        AbstractListCmdLoader<ResultT, GetListCommand<ResultT>>
implements ListLoader<ResultT> {

	public ListCmdLoader(CommandService service) {
		super(service);
	}

    

}
