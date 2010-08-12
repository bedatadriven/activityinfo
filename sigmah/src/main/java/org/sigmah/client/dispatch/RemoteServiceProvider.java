/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.sigmah.shared.command.RemoteCommandService;
import org.sigmah.shared.command.RemoteCommandServiceAsync;

/**
 * Configures and provides the RemoteCommandServiceAsync instance
 *
 * @author Alex Bertram
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
