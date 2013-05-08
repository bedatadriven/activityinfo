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

import org.activityinfo.shared.command.GetUserProfile;
import org.activityinfo.shared.dto.UserProfileDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlResultSetRowList;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetUserProfileHandler implements CommandHandlerAsync<GetUserProfile, UserProfileDTO> {

    @Override
    public void execute(final GetUserProfile command, ExecutionContext context,
        final AsyncCallback<UserProfileDTO> callback) {
        
        final int userId = command.getUserId();
        
        SqlQuery.select()
            .appendColumn("u.name", "name")
            .appendColumn("u.email", "email")
            .appendColumn("u.organization", "organization")
            .appendColumn("u.jobtitle", "jobtitle")
            .appendColumn("u.locale", "locale")
            .appendColumn("u.emailNotification", "emailNotification")
            .from("userlogin", "u")
            .where("u.userid").equalTo(userId)

            .execute(context.getTransaction(), new SqlResultCallback() {
                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    SqlResultSetRowList list = results.getRows();
                    if (list.isEmpty()) {
                        callback.onFailure(new IllegalArgumentException("user " + userId + " not found"));
                    }
                    
                    SqlResultSetRow row = list.get(0);
                    UserProfileDTO dto = new UserProfileDTO(userId);
                    dto.setName(row.getString("name"));
                    dto.setEmail(row.getString("email"));
                    dto.setOrganization(row.getString("organization"));
                    dto.setJobtitle(row.getString("jobtitle"));
                    dto.setLocale(row.getString("locale"));
                    dto.setEmailNotification(row.getBoolean("emailNotification"));

                    callback.onSuccess(dto);
                }
            });
    }
}
