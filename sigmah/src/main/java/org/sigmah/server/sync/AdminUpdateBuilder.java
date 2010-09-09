/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;
import org.json.JSONException;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.User;

import java.util.List;

public class AdminUpdateBuilder implements UpdateBuilder {
    private AdminDAO dao;
    protected int levelId;
    private AdminLocalState localState;
    private static final int LAST_VERSION_NUMBER = 1;
    protected JpaUpdateBuilder builder;


    @Inject
    public AdminUpdateBuilder(AdminDAO dao) {
        this.dao = dao;
    }

    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        parseLevelId(request);
        localState = new AdminLocalState(request.getLocalVersion());

        SyncRegionUpdate update = new SyncRegionUpdate();
        builder = new JpaUpdateBuilder();

        if(localState.version < LAST_VERSION_NUMBER) {
            /**
             * This level is out of date, delete all on the client and send all from the server
             */
            builder.createTableIfNotExists(AdminEntity.class);
            builder.executeStatement("delete from AdminEntity where AdminLevelId=" + levelId);

            List<AdminEntity> entities = dao.query().level(levelId).execute();
            update.setSql(makeJson(entities));
            localState.complete = false;
            localState.version = LAST_VERSION_NUMBER;
        }

        update.setComplete(localState.complete);
        update.setVersion(localState.toString());

        return update;
    }

    private void parseLevelId(GetSyncRegionUpdates request) {
        levelId = Integer.parseInt(request.getRegionId().substring("admin/".length()));
    }

    private String makeJson(List<AdminEntity> entities) throws JSONException {
        builder.insert(AdminEntity.class, entities);
        return builder.asJson();
    }

}
