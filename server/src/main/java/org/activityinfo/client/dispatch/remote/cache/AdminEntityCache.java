/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.client.dispatch.CommandProxy;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.DispatchListener;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Caches the results of {@link org.activityinfo.shared.command.GetAdminEntities}
 *
 * @author Alex Bertram
 */
@Singleton
public class AdminEntityCache extends AbstractCache implements CommandProxy<GetAdminEntities>, DispatchListener<GetAdminEntities> {

    @Inject
    public AdminEntityCache(DispatchEventSource connection) {
        connection.registerProxy(GetAdminEntities.class, this);
        connection.registerListener(GetAdminEntities.class, this);
    }

    @Override
    public ProxyResult<AdminEntityResult> maybeExecute(GetAdminEntities command) {

        if (command.getFilter() != null) {
            return ProxyResult.couldNotExecute();
        }

        AdminEntityResult result = (AdminEntityResult) fetch(command);
        if (result == null) {
            return ProxyResult.couldNotExecute();
        } else {
            return new ProxyResult<AdminEntityResult>(new AdminEntityResult(result));
        }
    }

    @Override
    public void beforeDispatched(GetAdminEntities command) {

    }

    @Override
    public void onFailure(GetAdminEntities command, Throwable caught) {

    }

    @Override
    public void onSuccess(GetAdminEntities command, CommandResult result) {

        if (command.getFilter() == null) {
            cache(command, new AdminEntityResult((AdminEntityResult) result));
        }
    }
}
