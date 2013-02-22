package org.activityinfo.client.page.entry.form;

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

import org.activityinfo.client.Log;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.activityinfo.client.page.entry.location.LocationDialog;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteDialogLauncher {

    private final Dispatcher dispatcher;

    public SiteDialogLauncher(Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;
    }

    public void addSite(final Filter filter, final SiteDialogCallback callback) {
        if (filter
            .isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
            dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

                @Override
                public void onFailure(Throwable arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onSuccess(SchemaDTO schema) {
                    final ActivityDTO activity = schema.getActivityById(
                        filter.getRestrictedCategory(DimensionType.Activity));
                    Log.trace("adding site for activity " + activity
                        + ", locationType = " + activity.getLocationType());

                    if (activity.getLocationType().isAdminLevel()) {
                        addNewSiteWithBoundLocation(activity, callback);
                    } else {
                        chooseLocationThenAddSite(activity, callback);
                    }
                }
            });
        }
    }

    public void editSite(final SiteDTO site, final SiteDialogCallback callback) {
        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SchemaDTO schema) {
                // check whether the site has been locked
                LockedPeriodSet locks = new LockedPeriodSet(schema);
                if (locks.isLocked(site)) {
                    MessageBox.alert(I18N.CONSTANTS.lockedSiteTitle(),
                        I18N.CONSTANTS.siteIsLocked(), null);
                    return;
                }

                ActivityDTO activity = schema.getActivityById(site
                    .getActivityId());
                SiteDialog dialog = new SiteDialog(dispatcher, activity);
                dialog.showExisting(site, callback);
            }
        });
    }

    private void chooseLocationThenAddSite(final ActivityDTO activity,
        final SiteDialogCallback callback) {
        LocationDialog dialog = new LocationDialog(dispatcher, activity
            .getDatabase().getCountry(),
            activity.getLocationType());

        dialog.show(new LocationDialog.Callback() {

            @Override
            public void onSelected(LocationDTO location, boolean isNew) {
                SiteDTO newSite = new SiteDTO();
                newSite.setActivityId(activity.getId());
                newSite.setLocation(location);

                SiteDialog dialog = new SiteDialog(dispatcher, activity);
                dialog.showNew(newSite, location, isNew, callback);
            }
        });
    }

    private void addNewSiteWithBoundLocation(ActivityDTO activity,
        SiteDialogCallback callback) {
        SiteDTO newSite = new SiteDTO();
        newSite.setActivityId(activity.getId());

        LocationDTO location = new LocationDTO();
        location.setId(new KeyGenerator().generateInt());
        location.setLocationTypeId(activity.getLocationTypeId());

        SiteDialog dialog = new SiteDialog(dispatcher, activity);
        dialog.showNew(newSite, location, true, callback);
    }
}
