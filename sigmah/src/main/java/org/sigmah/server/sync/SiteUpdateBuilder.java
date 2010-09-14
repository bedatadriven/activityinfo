/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;
import org.json.JSONException;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.*;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SiteUpdateBuilder implements UpdateBuilder {

    private final EntityManager entityManager;

    private int databaseId;
    private int orgUnitId;
    public static final int MAX_RESULTS = 25;

    private List<Site> all;
    private List<Site> created = new ArrayList<Site>();
    private List<Site> updated = new ArrayList<Site>();
    private List<Site> deleted = new ArrayList<Site>();

    private List<Integer> updatedOrDeleted = new ArrayList<Integer>();
    private List<Integer> createdOrUpdated = new ArrayList<Integer>();
    private final JpaUpdateBuilder builder;

    @Inject
    public SiteUpdateBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
        builder = new JpaUpdateBuilder();
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        parseRegion(request.getRegionId());
        Timestamp localVersion = TimestampHelper.fromString(request.getLocalVersion());

        // Retrieve the sites to that have changed in some way since our last call

        retrieveNextBatchOfModifiedSites(localVersion);

        builder.createTableIfNotExists(Site.class);
        builder.createTableIfNotExists(ReportingPeriod.class);
        // TODO: fix rebar to handle these types of classes correctly
        builder.executeStatement("create table if not exists AttributeValue (SiteId integer, AttributeId integer, Value integer)");
        builder.executeStatement("create table if not exists IndicatorValue (ReportingPeriodId integer, IndicatorId integer, Value real)");

        if(!updatedOrDeleted.isEmpty()) {
            removeDependentAttributeValuesOfDeletedOrUpdatedSites();
            removeDependentIndicatorValuesOfDeletedOrUpdatedSites();
            removeDependentReportingPeriodsOfDeletedOrUpdatedSites();
        }

        builder.delete(Site.class, deleted);
        builder.update(Site.class, updated);
        builder.insert(Site.class, created);

        if(!createdOrUpdated.isEmpty()) {
            insertDependentAttributeValues();
            insertDependentReportingPeriods();
            insertDependentIndicatorValues();
        }

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setComplete(all.size() < MAX_RESULTS);
        update.setVersion(TimestampHelper.toString(all.get(all.size()-1).getDateEdited()));
        update.setSql(builder.asJson());

        return update;
    }


    private void retrieveNextBatchOfModifiedSites(Timestamp localVersion) {
        all = entityManager.createQuery(
                "select s from Site s " +
                        "WHERE  (s.dateEdited > :localVersion) AND " +
                        "(s.dateDeleted is NULL or s.dateCreated < :localVersion) AND " +
                        "(s.activity.database = :database) AND " +
                        "(s.partner = :orgUnit)" +
                        "ORDER BY s.dateEdited")
                .setMaxResults(MAX_RESULTS)
                .setParameter("localVersion", localVersion)
                .setParameter("database", entityManager.getReference(UserDatabase.class, databaseId))
                .setParameter("orgUnit", entityManager.getReference(OrgUnit.class, orgUnitId))
                .getResultList();

        for(Site site : all) {
            if(site.isDeleted()) {
                deleted.add(site);
                updatedOrDeleted.add(site.getId());
                createdOrUpdated.add(site.getId());
            } else if(TimestampHelper.isAfter(site.getDateCreated(), localVersion)) {
                created.add(site);
                createdOrUpdated.add(site.getId());
            } else {
                updated.add(site);
                updatedOrDeleted.add(site.getId());
            }
        }
    }

    private void removeDependentAttributeValuesOfDeletedOrUpdatedSites() throws JSONException {
        builder.beginPreparedStatement("delete from AttributeValue where SiteId = ?");
        addIdsOfUpdatedOrDeleted(builder);
        builder.finishPreparedStatement();
    }

    private void removeDependentReportingPeriodsOfDeletedOrUpdatedSites() throws JSONException {
        builder.beginPreparedStatement("delete from ReportingPeriod where SiteId = ?");
        addIdsOfUpdatedOrDeleted(builder);
        builder.finishPreparedStatement();
    }

    private void removeDependentIndicatorValuesOfDeletedOrUpdatedSites() throws JSONException {
        builder.beginPreparedStatement("delete from IndicatorValue v where v.ReportingPeriodId in " +
                "(select ReportingPeriodId from ReportingPeriod where SiteId = ?) ");
        addIdsOfUpdatedOrDeleted(builder);
        builder.finishPreparedStatement();
    }

    private void insertDependentAttributeValues() throws JSONException {
        List<AttributeValue> values = entityManager.createQuery(
                "SELECT av from Site s " +
                        "JOIN s.attributeValues av " +
                        "WHERE s.id in (:sites)")
                .setParameter("sites", createdOrUpdated)
                .getResultList();

        builder.insert(AttributeValue.class, values);
    }

    private void insertDependentReportingPeriods() throws JSONException {
        List<ReportingPeriod> rps = entityManager.createQuery("SELECT p from Site s JOIN s.reportingPeriods p WHERE " +
                "s.id in (:sites)")
                .setParameter("sites", createdOrUpdated)
                .getResultList();

        builder.insert(ReportingPeriod.class, rps);
    }

    private void insertDependentIndicatorValues() throws JSONException {
        List<IndicatorValue> values = entityManager.createQuery(
                "SELECT v from Site s JOIN s.reportingPeriods p JOIN p.indicatorValues v " +
                    "WHERE s.id in (:sites)")
                .setParameter("sites", createdOrUpdated)
                .getResultList();

        builder.insert(IndicatorValue.class, values);
    }

    private void addIdsOfUpdatedOrDeleted(JpaUpdateBuilder builder) throws JSONException {
        for(Integer siteId : updatedOrDeleted) {
            builder.addExecution(siteId);
        }
    }

    private void parseRegion(String regionId) {
        String[] parts = regionId.split("/");
        databaseId = Integer.parseInt(parts[1]);
        orgUnitId = Integer.parseInt(parts[2]);
    }
}
