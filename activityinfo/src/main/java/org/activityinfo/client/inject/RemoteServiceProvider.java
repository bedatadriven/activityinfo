package org.activityinfo.client.inject;

import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

import com.google.inject.Provider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
/*
 * @author Alex Bertram
 */

public class RemoteServiceProvider implements Provider<RemoteCommandServiceAsync> {

    public RemoteCommandServiceAsync get() {
        RemoteCommandServiceAsync remoteService = (RemoteCommandServiceAsync)
                GWT.create(RemoteCommandService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) remoteService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "cmd";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
        return remoteService;
    }
}
