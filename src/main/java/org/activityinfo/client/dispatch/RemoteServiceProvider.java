/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch;

import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Configures and provides the RemoteCommandServiceAsync instance
 */
@Singleton
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
