/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.CommandProxy;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.DispatchListener;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;

/**
 * Caches the results of {@link org.sigmah.shared.command.GetAdminEntities}
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

        if (command.getActivityId() != null) {
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

        if (command.getActivityId() == null) {
            cache(command, new AdminEntityResult((AdminEntityResult) result));
        }
    }
}
