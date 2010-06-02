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

package org.activityinfo.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchemaUpdateBuilder {

    private final SchemaDAO schemaDAO;

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

    public SchemaUpdateBuilder(SchemaDAO schemaDAO) {
        this.schemaDAO = schemaDAO;
    }

    public SyncRegionUpdate build(User user)  {
        databases = schemaDAO.getDatabases(user);

        makeEntityLists();

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setRegionId(REGION_ID);
        update.setVersion(getCurrentSchemaVersion(databases, user));
        try {
            update.setSql(buildSql());
        } catch (JSONException e) {
            throw new RuntimeException(e);
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

        return builder.toString();
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

    public static long getCurrentSchemaVersion(List<UserDatabase> databases, User user) {
        long currentVersion = 1;
        for(UserDatabase db : databases) {
            if(db.getLastSchemaUpdate().getTime() > currentVersion)
                currentVersion = db.getLastSchemaUpdate().getTime();

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
