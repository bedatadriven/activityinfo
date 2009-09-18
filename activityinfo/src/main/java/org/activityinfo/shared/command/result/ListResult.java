package org.activityinfo.shared.command.result;

import java.util.List;
import java.util.ArrayList;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.DTO;

public abstract class ListResult<D extends ModelData> implements CommandResult, ListLoadResult<D> {

	private List<D> data;

	protected ListResult() {
	    data = new ArrayList<D>();	
	}
	
	public ListResult(List<D> data) {
		this.data = data;
	}

    public ListResult(ListResult<D> result) {
        this.data = new ArrayList<D>(result.getData());
    }

    @Override
	public List<D> getData() {
		return data;
	}	
}
