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

import java.util.List;

import org.activityinfo.shared.command.GetIndicatorLinks;
import org.activityinfo.shared.command.result.IndicatorLink;
import org.activityinfo.shared.command.result.IndicatorLinkResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetIndicatorLinksHandler implements
    CommandHandlerAsync<GetIndicatorLinks, IndicatorLinkResult> {

    @Override
    public void execute(GetIndicatorLinks command, ExecutionContext context,
        final AsyncCallback<IndicatorLinkResult> callback) {

        SqlQuery.select()
            .appendColumn("L.SourceIndicatorId", "SourceIndicatorId")
            .appendColumn("SDB.DatabaseId", "SourceDatabaseId")
            .appendColumn("L.DestinationIndicatorId", "DestinationIndicatorId")
            .appendColumn("DDB.DatabaseId", "DestinationDatabaseId")
            .from(Tables.INDICATOR_LINK, "L")
            .innerJoin(Tables.INDICATOR, "SI")
            .on("SI.IndicatorId=L.SourceIndicatorId")
            .innerJoin(Tables.ACTIVITY, "SA").on("SA.ActivityId=SI.ActivityId")
            .innerJoin(Tables.USER_DATABASE, "SDB")
            .on("SDB.DatabaseId=SA.DatabaseId")
            .innerJoin(Tables.INDICATOR, "DI")
            .on("DI.IndicatorId=L.DestinationIndicatorId")
            .innerJoin(Tables.ACTIVITY, "DA").on("DA.ActivityId=DI.ActivityId")
            .innerJoin(Tables.USER_DATABASE, "DDB")
            .on("DDB.DatabaseId=DA.DatabaseId")
            .execute(context.getTransaction(), new SqlResultCallback() {

                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    List<IndicatorLink> links = Lists.newArrayList();
                    for (SqlResultSetRow row : results.getRows()) {
                        IndicatorLink link = new IndicatorLink();
                        link.setSourceDatabaseId(row.getInt("SourceDatabaseId"));
                        link.setSourceIndicatorId(row
                            .getInt("SourceIndicatorId"));
                        link.setDestinationDatabaseId(row
                            .getInt("DestinationDatabaseId"));
                        link.setDestinationIndicatorId(row
                            .getInt("DestinationIndicatorId"));
                        links.add(link);
                    }
                    callback.onSuccess(new IndicatorLinkResult(links));
                }
            });

    }

}
