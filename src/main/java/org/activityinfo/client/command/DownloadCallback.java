package org.activityinfo.client.command;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import org.activityinfo.shared.command.result.RenderResult;
/*
 * @author Alex Bertram
 */

public class DownloadCallback implements AsyncCallback<RenderResult> {

    public void onFailure(Throwable caught) {

    }

    public void onSuccess(RenderResult result) {
        Window.open("../download?" + result.getUrl(), "_downloadFrame", null);
    }
}
