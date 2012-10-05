/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.sync;

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
    private static final int LAST_VERSION_NUMBER = 1;
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
