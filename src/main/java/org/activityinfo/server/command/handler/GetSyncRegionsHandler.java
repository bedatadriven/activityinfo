package org.activityinfo.server.command.handler;

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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.sync.AdminUpdateBuilder;
import org.activityinfo.server.command.handler.sync.SiteTableUpdateBuilder;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegion;
import org.activityinfo.shared.command.result.SyncRegions;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.util.CollectionUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class GetSyncRegionsHandler implements CommandHandler<GetSyncRegions> {

    private EntityManager entityManager;

    @Inject
    public GetSyncRegionsHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandResult execute(GetSyncRegions cmd, User user)
        throws CommandException {

        List<Object[]> databases = entityManager.createQuery(
            "SELECT " +
                "db.id, db.country.id, db.version, " +
                "MAX(p.version) " +
                "FROM UserDatabase db " +
                "LEFT JOIN db.userPermissions p " +
                "GROUP BY db.id")
            .getResultList();

        List<Integer> databaseIds = Lists.newArrayList();
        Set<Integer> countryIds = Sets.newHashSet();
        long schemaVersion = 1;
        for (Object[] db : databases) {
            databaseIds.add((Integer) db[0]);
            countryIds.add((Integer) db[1]);
            if (db[2] != null) {
                schemaVersion = Math.max(schemaVersion, (Long) db[2]);
            }
            if (db[3] != null) {
                schemaVersion = Math.max(schemaVersion, (Long) db[3]);
            }
        }

        List<SyncRegion> regions = new ArrayList<SyncRegion>();
        regions.add(new SyncRegion("schema", Long.toString(schemaVersion)));
        regions.addAll(listAdminRegions(countryIds));
        regions.addAll(listLocations(databaseIds));
        regions.addAll(listSiteRegions(databaseIds));
        return new SyncRegions(regions);
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends SyncRegion> listLocations(
        List<Integer> databases) {

        List<SyncRegion> locationRegions = new ArrayList<SyncRegion>();

        if (CollectionUtil.isNotEmpty(databases)) {
            List<Object[]> regions = entityManager.createQuery(
                "SELECT " +
                    "a.locationType.id, " +
                    "MAX(loc.timeEdited) " +
                    "FROM Activity a " +
                    "INNER JOIN a.locationType.locations loc " +
                    "WHERE a.database.id in (:dbs) " +
                    "GROUP BY a.locationType")
                .setParameter("dbs", databases)
                .getResultList();

            for (Object[] region : regions) {
                locationRegions.add(new SyncRegion("location/" + region[0], region[1].toString()));
            }
        }
        return locationRegions;
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends SyncRegion> listAdminRegions(
        Set<Integer> countryIds) {
        
        List<SyncRegion> adminRegions = new ArrayList<SyncRegion>();
        
        if (CollectionUtil.isNotEmpty(countryIds)) {
            List<Integer> levels = entityManager.createQuery(
                "SELECT " +
                    "level.id " +
                    "FROM AdminLevel level " +
                    "WHERE level.country.id in (:countries) ")
                .setParameter("countries", countryIds)
                .getResultList();
            
            for (Integer level : levels) {
                adminRegions.add(new SyncRegion("admin/" + level,
                    Integer.toString(AdminUpdateBuilder.LAST_VERSION_NUMBER)));
            }
        }
        return adminRegions;
    }

    /**
     * We need a separate sync region for each Partner/UserDatabase combination
     * because we may be given permission to view data at different times.
     */
    @SuppressWarnings("unchecked")
    private Collection<? extends SyncRegion> listSiteRegions(
        List<Integer> databases) {
        List<SyncRegion> siteRegions = new ArrayList<SyncRegion>();

        // our initial sync region manages the table schema
        siteRegions.add(new SyncRegion("site-tables", SiteTableUpdateBuilder.CURRENT_VERSION));

        if (CollectionUtil.isNotEmpty(databases)) {
            // do one sync region per database
            List<Object[]> regions = entityManager.createQuery(
                "SELECT " +
                    "s.activity.database.id, " +
                    "MAX(s.timeEdited) " +
                    "FROM Site s " +
                    "WHERE s.activity.database.id in (:dbs) " +
                    "GROUP BY s.activity.database.id")
                .setParameter("dbs", databases)
                .getResultList();

            for (Object[] region : regions) {
                siteRegions.add(new SyncRegion("site/" + region[0], region[1].toString()));
            }
        }
        return siteRegions;
    }
}
