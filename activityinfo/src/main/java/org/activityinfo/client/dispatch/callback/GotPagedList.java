package org.activityinfo.client.dispatch.callback;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.shared.command.result.PagingResult;

import java.util.List;

public abstract class GotPagedList<T extends ModelData> implements AsyncCallback<PagingResult<T>> {

    @Override
    public void onFailure(Throwable caught) {

    }

    @Override
    public void onSuccess(PagingResult<T> result) {
        got(result.getData());
    }

    public abstract void got(List<T> result);

}
