package org.activityinfo.client.page.entry.column;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.entry.grouping.AdminGroupingModel;
import org.activityinfo.client.page.entry.grouping.GroupingModel;
import org.activityinfo.client.page.entry.grouping.NullGroupingModel;
import org.activityinfo.client.page.entry.grouping.TimeGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultColumnModelProvider implements ColumnModelProvider {

    private final Dispatcher dispatcher;

    public DefaultColumnModelProvider(Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;
    }

    @Override
    public void get(final Filter filter, final GroupingModel grouping,
        final AsyncCallback<ColumnModel> callback) {

        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SchemaDTO result) {
                if (filter
                    .isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
                    ActivityDTO singleActivity = result.getActivityById(filter
                        .getRestrictedCategory(DimensionType.Activity));
                    callback.onSuccess(forSingleActivity(grouping,
                        singleActivity));
                } else if (filter
                    .isDimensionRestrictedToSingleCategory(DimensionType.Database)) {
                    UserDatabaseDTO singleDatabase = result
                        .getDatabaseById(filter
                            .getRestrictedCategory(DimensionType.Database));
                    callback.onSuccess(forSingleDatabase(grouping,
                        singleDatabase));
                } else {
                    callback.onSuccess(forMultipleDatabases(grouping, result));
                }
            }
        });
    }

    /**
     * Builds
     * 
     * @param activity
     */
    private ColumnModel forSingleActivity(GroupingModel grouping,
        ActivityDTO activity) {
        if (grouping == NullGroupingModel.INSTANCE) {
            return new ColumnModelBuilder()
                .addMapColumn()
                .maybeAddLockColumn(activity)
                .maybeAddDateColumn(activity)
                .maybeAddPartnerColumn(activity.getDatabase())
                .maybeAddProjectColumn(activity.getDatabase())
                .maybeAddKeyIndicatorColumns(activity)
                .maybeAddTwoLineLocationColumn(activity)
                .addAdminLevelColumns(activity)
                .build();
        } else if (grouping instanceof AdminGroupingModel) {

            return new ColumnModelBuilder()
                .maybeAddLockColumn(activity)
                .addTreeNameColumn()
                .maybeAddDateColumn(activity)
                .maybeAddPartnerColumn(activity.getDatabase())
                .maybeAddProjectColumn(activity.getDatabase())

                .build();
        } else if (grouping instanceof TimeGroupingModel) {

            return new ColumnModelBuilder()
                .maybeAddLockColumn(activity)
                .addTreeNameColumn()
                .maybeAddDateColumn(activity)
                .maybeAddPartnerColumn(activity.getDatabase())
                .maybeAddProjectColumn(activity.getDatabase())
                .maybeAddSingleLineLocationColumn(activity)
                .addAdminLevelColumns(activity)
                .build();
        } else {
            throw new IllegalArgumentException(grouping.toString());
        }
    }

    private ColumnModel forSingleDatabase(GroupingModel grouping,
        UserDatabaseDTO database) {
        if (grouping == NullGroupingModel.INSTANCE) {
            return new ColumnModelBuilder()
                .addMapColumn()
                .addActivityColumn(database)
                .addLocationColumn()
                .maybeAddPartnerColumn(database)
                .maybeAddProjectColumn(database)
                .addAdminLevelColumns(database)
                .build();

        } else if (grouping instanceof AdminGroupingModel) {

            return new ColumnModelBuilder()
                .addTreeNameColumn()
                .addActivityColumn(database)
                .maybeAddPartnerColumn(database)
                .maybeAddProjectColumn(database)
                .build();

        } else if (grouping instanceof TimeGroupingModel) {

            return new ColumnModelBuilder()
                .addTreeNameColumn()
                .addActivityColumn(database)
                .maybeAddPartnerColumn(database)
                .maybeAddProjectColumn(database)
                .addLocationColumn()
                .addAdminLevelColumns(database)
                .build();
        } else {
            throw new IllegalArgumentException(grouping.toString());
        }
    }

    private ColumnModel forMultipleDatabases(GroupingModel grouping,
        SchemaDTO schema) {
        if (grouping == NullGroupingModel.INSTANCE) {
            return new ColumnModelBuilder()
                .addMapColumn()
                .addDatabaseColumn(schema)
                .addActivityColumn(schema)
                .addLocationColumn()
                .addPartnerColumn()
                .build();

        } else if (grouping instanceof AdminGroupingModel) {

            return new ColumnModelBuilder()
                .addTreeNameColumn()
                .addPartnerColumn()
                .build();

        } else if (grouping instanceof TimeGroupingModel) {

            return new ColumnModelBuilder()
                .addTreeNameColumn()
                .addPartnerColumn()
                .addLocationColumn()
                .build();
        } else {
            throw new IllegalArgumentException(grouping.toString());
        }
    }

}
