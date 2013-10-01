package org.activityinfo.shared.command.handler;

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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.GetAdminLevels;
import org.activityinfo.shared.command.result.AdminLevelResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.util.CollectionUtil;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAdminLevelsHandler implements
    CommandHandlerAsync<GetAdminLevels, AdminLevelResult> {

    @Override
    public void execute(GetAdminLevels command, ExecutionContext context,
        final AsyncCallback<AdminLevelResult> callback) {

        if (CollectionUtil.isEmpty(command.getIndicatorIds())) {
            callback.onSuccess(new AdminLevelResult(new ArrayList<AdminLevelDTO>()));
            return;
        }
       
        
        String hasPolygonsSubQuery = "exists (select e.adminentityid from adminentity e " +
        		"where e.adminlevelid=level.adminlevelid and geometry is not null)";
        
        
        SqlQuery
            .select()
            .appendColumn("level.adminlevelId", "id")
            .appendColumn("level.name", "name")
            .appendColumn(hasPolygonsSubQuery, "polygons")
            .from(Tables.INDICATOR, "i")
            .innerJoin(Tables.ACTIVITY, "a").on("i.activityId=a.activityId")
            .innerJoin(Tables.USER_DATABASE, "db").on("a.databaseid=db.databaseid")
            .innerJoin(Tables.COUNTRY, "c").on("db.countryid=c.countryid")
            .innerJoin(Tables.ADMIN_LEVEL, "level").on("level.countryid=c.countryid")
            .where("i.indicatorId").in(command.getIndicatorIds())
            .groupBy("level.adminlevelid")
            .groupBy("level.name")
            .execute(context.getTransaction(), new SqlResultCallback() {

                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    List<AdminLevelDTO> levels = Lists.newArrayList();
                    for (SqlResultSetRow row : results.getRows()) {
                        AdminLevelDTO level = new AdminLevelDTO();
                        level.setId(row.getInt("id"));
                        level.setName(row.getString("name"));
                        level.setPolygons(row.getBoolean("polygons"));
                        levels.add(level);
                    }

                    callback.onSuccess(new AdminLevelResult(levels));
                }
            });
    }
}
