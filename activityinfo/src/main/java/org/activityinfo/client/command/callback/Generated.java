package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.shared.report.content.Content;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class Generated<T extends Content> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onSuccess(T content) {

    }

    protected abstract void got(T content);
}
