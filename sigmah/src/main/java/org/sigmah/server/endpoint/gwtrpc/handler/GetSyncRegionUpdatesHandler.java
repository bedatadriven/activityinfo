/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.json.JSONException;
import org.sigmah.server.domain.User;
import org.sigmah.server.sync.AdminUpdateBuilder;
import org.sigmah.server.sync.LocationUpdateBuilder;
import org.sigmah.server.sync.SchemaUpdateBuilder;
import org.sigmah.server.sync.UpdateBuilder;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

public class GetSyncRegionUpdatesHandler implements CommandHandler<GetSyncRegionUpdates> {

    private final Injector injector;

    @Inject
    public GetSyncRegionUpdatesHandler(Injector injector) {
        this.injector = injector;
    }

    @Override
    public CommandResult execute(GetSyncRegionUpdates cmd, User user) throws CommandException {

        UpdateBuilder builder;

        if(cmd.getRegionId().equals("schema")) {
            builder = injector.getInstance(SchemaUpdateBuilder.class);

        } else if(cmd.getRegionId().startsWith("admin/")) {
            builder = injector.getInstance(AdminUpdateBuilder.class);

        } else if(cmd.getRegionId().equals("locations")) {
            builder = injector.getInstance(LocationUpdateBuilder.class);

        } else {
            throw new CommandException("Unknown sync region: " + cmd.getRegionId());
        }

        try {
            return builder.build(user, cmd);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
