/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;
import org.json.JSONException;
import org.sigmah.server.dao.AdminDAO;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

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
