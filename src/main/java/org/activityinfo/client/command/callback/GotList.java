package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.List;

import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.DTO;

public abstract class GotList<T extends ModelData> implements AsyncCallback<ListResult<T>> {

    public void onSuccess(ListResult<T> result) {
        got(result.getData());
    }

    public abstract void got(List<T> list);

    public void onFailure(Throwable throwable) {
        // do nothing, let monitor handle failure
    }
}
