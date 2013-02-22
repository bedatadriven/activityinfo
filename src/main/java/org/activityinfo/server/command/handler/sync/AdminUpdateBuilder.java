

package org.activityinfo.server.command.handler.sync;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.dao.AdminDAO;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.db.Tables;
import org.json.JSONException;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;

public class AdminUpdateBuilder implements UpdateBuilder {
    private EntityManager em;
    private int levelId;
    private AdminLocalState localState;
    public static final int LAST_VERSION_NUMBER = 1;
    private SqliteBatchBuilder builder;

    @Inject
    public AdminUpdateBuilder(EntityManager em) {
        this.em = em;
    }

    @Override
	public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws IOException {
        parseLevelId(request);
        localState = new AdminLocalState(request.getLocalVersion());

        SyncRegionUpdate update = new SyncRegionUpdate();
        builder = new SqliteBatchBuilder();

        if(localState.getVersion() < LAST_VERSION_NUMBER) {
            /*
             * This level is out of date, delete all on the client and send all from the server
             */
            builder.addStatement("CREATE TABLE IF NOT EXISTS AdminEntity " +
            		"(AdminEntityId INT, " +
            		"Name TEXT," +
            		"Code TEXT," +
            		"AdminLevelId INT," +
            		"AdminEntityParentId INT," +
            		"X1 REAL, Y1 REAL, X2 REAL, Y2 REAL)");
            builder.addStatement("DELETE FROM AdminEntity WHERE AdminLevelId=" + levelId);

            SqlQuery query = SqlQuery.select(
            		"AdminEntityId", "Name", "AdminLevelId", "AdminEntityParentId", 
            		"Code", "AdminLevelId", "AdminEntityParentId", 
            		"X1", "Y1", "X2", "Y2")
            		.from(Tables.ADMIN_ENTITY)
            		.where("AdminLevelId").equalTo(levelId);
            
            builder.insert()
            	.into(Tables.ADMIN_ENTITY)
            	.from(query)
            	.execute(em);
            	
            update.setSql(builder.build());
        } 
    	update.setComplete(true);
    	update.setVersion(Integer.toString(LAST_VERSION_NUMBER));
    
        return update;
    }

    private void parseLevelId(GetSyncRegionUpdates request) {
        levelId = Integer.parseInt(request.getRegionId().substring("admin/".length()));
    }

}
