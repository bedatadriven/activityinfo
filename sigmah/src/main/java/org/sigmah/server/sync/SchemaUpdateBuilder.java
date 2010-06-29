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
import org.sigmah.server.dao.UserDatabaseDAO;
import org.sigmah.server.domain.*;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchemaUpdateBuilder implements UpdateBuilder {

    private final UserDatabaseDAO userDatabaseDAO;

    private Set<Integer> countryIds = new HashSet<Integer>();
    private List<Country> countries = new ArrayList<Country>();
    private List<AdminLevel> adminLevels = new ArrayList<AdminLevel>();

    private List<UserDatabase> databases = new ArrayList<UserDatabase>();

    private Set<Integer> partnerIds = new HashSet<Integer>();
    private List<Partner> partners = new ArrayList<Partner>();

    private List<Activity> activities = new ArrayList<Activity>();
    private List<Indicator> indicators = new ArrayList<Indicator>();

    private Class[] schemaClasses = new Class[] {
            Country.class,
            AdminLevel.class,
            UserDatabase.class,
            Partner.class,
            Activity.class,
            Indicator.class
    };
    private static final String REGION_ID = "schema";

    @Inject
    public SchemaUpdateBuilder(UserDatabaseDAO userDatabaseDAO) {
        this.userDatabaseDAO = userDatabaseDAO;
    }

    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        databases = userDatabaseDAO.queryAllUserDatabasesAlphabetically();

        long localVersion = request.getLocalVersion() == null ? 0 : Long.parseLong(request.getLocalVersion());
        long serverVersion = getCurrentSchemaVersion(user);

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setVersion(Long.toString(serverVersion));
        update.setComplete(true);

        if(localVersion == serverVersion) {
            update.setComplete(true);
        } else {
            makeEntityLists();
            update.setSql(buildSql());
        }
        return update;
    }

    private String buildSql() throws JSONException {
        JpaUpdateBuilder builder = new JpaUpdateBuilder();
        for(Class schemaClass : schemaClasses) {
            builder.createTableIfNotExists(schemaClass);
            builder.deleteAll(schemaClass);
        }

        builder.insert(Country.class, countries);
        builder.insert(AdminLevel.class, adminLevels);
        builder.insert(UserDatabase.class, databases);
        builder.insert(Partner.class, partners);
        builder.insert(Activity.class, activities);
        builder.insert(Indicator.class, indicators);

        return builder.asJson();
    }

    private void makeEntityLists() {
        for(UserDatabase database : databases) {
            if(!countryIds.contains(database.getCountry().getId())) {
                countries.add(database.getCountry());
                adminLevels.addAll(database.getCountry().getAdminLevels());
                countryIds.add(database.getCountry().getId());
            }
            for(Partner partner : database.getPartners()) {
                if(!partnerIds.contains(partner.getId())) {
                    partners.add(partner);
                    partnerIds.add(partner.getId());
                }
            }
            for(Activity activity : database.getActivities()) {
                activities.add(activity);
                for(Indicator indicator : activity.getIndicators()) {
                    indicators.add(indicator);
                }
            }
        }
    }

    public long getCurrentSchemaVersion(User user) {
        long currentVersion = 1;
        for(UserDatabase db : databases) {
            if(db.getLastSchemaUpdate().getTime() > currentVersion) {
                currentVersion = db.getLastSchemaUpdate().getTime();
            }

            if(db.getOwner().getId() != user.getId()) {
                UserPermission permission = db.getPermissionByUser(user);
                if(permission.getLastSchemaUpdate().getTime() > permission.getId()) {
                    currentVersion = permission.getLastSchemaUpdate().getTime();
                }
            }
        }
        return currentVersion;
    }
}
