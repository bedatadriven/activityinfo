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

import java.util.Map;

import org.activityinfo.shared.command.GetDimensionLabels;
import org.activityinfo.shared.command.GetDimensionLabels.DimensionLabels;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetDimensionLabelsHandler implements
    CommandHandlerAsync<GetDimensionLabels, DimensionLabels> {

    @Override
    public void execute(GetDimensionLabels command, ExecutionContext context,
        final AsyncCallback<DimensionLabels> callback) {

        SqlQuery query = composeQuery(command);
        query.execute(context.getTransaction(), new SqlResultCallback() {
            @Override
            public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                Map<Integer, String> labels = Maps.newHashMap();
                for (SqlResultSetRow row : results.getRows()) {
                    labels.put(row.getInt("id"), row.getString("name"));
                }
                callback.onSuccess(new DimensionLabels(labels));
            }
        });
    }

    private SqlQuery composeQuery(GetDimensionLabels command) {
        String tableName;
        String primaryKey;

        switch (command.getType()) {
        case Site:
            tableName = "(select s.siteid, l.name from site s left join location l on (s.locationid=l.locationid))";
            primaryKey = "siteid";
            break;
        case Database:
            tableName = "UserDatabase";
            primaryKey = "DatabaseId";
            break;
        case AdminLevel:
            tableName = "AdminEntity";
            primaryKey = "AdminEntityId";
            break;
        default:
            tableName = command.getType().toString();
            primaryKey = tableName + "Id";
        }

        return SqlQuery.select()
            .appendColumn("name")
            .appendColumn(primaryKey, "id")
            .from(tableName.toLowerCase(), "t")
            .where(primaryKey)
            .in(command.getIds());
    }
}
