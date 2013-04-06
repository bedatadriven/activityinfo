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

import java.util.Date;

import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteSiteHandler implements
    CommandHandlerAsync<DeleteSite, VoidResult> {

    @Override
    public void execute(DeleteSite command, ExecutionContext context,
        final AsyncCallback<VoidResult> callback) {

        context
            .getTransaction()
            .executeSql(
                "DELETE FROM indicatorvalue WHERE ReportingPeriodId IN "
                    +
                    "(SELECT ReportingPeriodId FROM reportingperiod WHERE siteid = ?)",
                new Object[] { command.getId() });

        SqlUpdate.delete(Tables.ATTRIBUTE_VALUE)
            .where("siteId", command.getId())
            .execute(context.getTransaction());

        SqlUpdate.delete(Tables.REPORTING_PERIOD)
            .where("siteId", command.getId())
            .execute(context.getTransaction());

        SqlUpdate.update(Tables.SITE)
            .value("dateDeleted", new Date())
            .value("timeEdited", new Date().getTime())
            .where("siteId", command.getId())
            .execute(context.getTransaction(), new SqlResultCallback() {

                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    callback.onSuccess(null);
                }
            });

    }

}
