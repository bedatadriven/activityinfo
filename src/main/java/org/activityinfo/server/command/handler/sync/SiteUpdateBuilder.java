/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.sync;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.server.database.hibernate.entity.AttributeValue;
import org.activityinfo.server.database.hibernate.entity.IndicatorValue;
import org.activityinfo.server.database.hibernate.entity.ReportingPeriod;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.json.JSONException;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;

public class SiteUpdateBuilder implements UpdateBuilder {

    private final EntityManager entityManager;

    private int databaseId;
    public static final int MAX_RESULTS = 500;

    private List<Site> all = new ArrayList<Site>();
    private List<Site> updated = new ArrayList<Site>();
    private List<Integer> updatedIds = new ArrayList<Integer>();
    
    private List<Site> deleted = new ArrayList<Site>();


    private final JpaUpdateBuilder builder;
    private long localVersion;

    @Inject
    public SiteUpdateBuilder(EntityManagerFactory entityManagerFactory) {
        // create a new, unfiltered entity manager so we can see deleted records

        this.entityManager = entityManagerFactory.createEntityManager();
        builder = new JpaUpdateBuilder();
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        try {
	    	parseRegion(request.getRegionId());
	
	        // todo: check user permissions for access to this db
	
	        localVersion = TimestampHelper.fromString(request.getLocalVersion());
	
	        // Retrieve the sites to that have changed in some way since our last call
	
	        retrieveNextBatchOfModifiedSites(localVersion);
	
	        if(!all.isEmpty()) {
	            removeDependentAttributeValuesOfDeletedOrUpdatedSites();
	            removeDependentIndicatorValuesOfDeletedOrUpdatedSites();
	            removeDependentReportingPeriodsOfDeletedOrUpdatedSites();
	        }
	
	        builder.delete(Site.class, deleted);
	
	        if(!updated.isEmpty()) {
	            builder.insert("or replace", Site.class, updated);
	        	insertDependentAttributeValues();
	            insertDependentReportingPeriods();
	            insertDependentIndicatorValues();
	        }
	
	        SyncRegionUpdate update = new SyncRegionUpdate();
	        update.setComplete(all.size() < MAX_RESULTS);
	        if(all.isEmpty()) {
	            update.setVersion(request.getLocalVersion());
	        } else {
	            update.setVersion(TimestampHelper.toString(all.get(all.size()-1).getTimeEdited()));
	            update.setSql(builder.asJson());
	        }
	
	        return update;
        
        } finally {
        	entityManager.close();
        }
    }


    private void retrieveNextBatchOfModifiedSites(long localVersion) {
        all = entityManager.createQuery(
                "select s from Site s " +
                        "WHERE  (s.timeEdited > :localVersion) AND " +
                        "(s.activity.database = :database)" +
                        "ORDER BY s.timeEdited")
                .setMaxResults(MAX_RESULTS)
                .setParameter("localVersion", localVersion)
                .setParameter("database", entityManager.getReference(UserDatabase.class, databaseId))
                .getResultList();


        for(Site site : all) {
            if(site.isDeleted()) {
                deleted.add(site);
            } else {
            	updated.add(site);
            	updatedIds.add(site.getId());
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
        builder.beginPreparedStatement("delete from IndicatorValue where IndicatorValue.ReportingPeriodId in " +
                "(select ReportingPeriodId from ReportingPeriod where SiteId = ?) ");
        addIdsOfUpdatedOrDeleted(builder);
        builder.finishPreparedStatement();
    }

    private void insertDependentAttributeValues() throws JSONException {
        List<AttributeValue> values = entityManager.createQuery(
                "SELECT av from Site s " +
                        "JOIN s.attributeValues av " +
                        "WHERE s.id in (:sites)")
                .setParameter("sites", updatedIds)
                .getResultList();

        builder.insert(AttributeValue.class, values);
    }

    private void insertDependentReportingPeriods() throws JSONException {
        List<ReportingPeriod> rps = entityManager.createQuery("SELECT p from Site s JOIN s.reportingPeriods p WHERE " +
                "s.id in (:sites)")
                .setParameter("sites", updatedIds)
                .getResultList();

        builder.insert("or replace", ReportingPeriod.class, rps);
    }

    private void insertDependentIndicatorValues() throws JSONException {
        List<IndicatorValue> values = entityManager.createQuery(
                "SELECT v from Site s JOIN s.reportingPeriods p JOIN p.indicatorValues v " +
                        "WHERE s.id in (:sites)")
                .setParameter("sites", updatedIds)
                .getResultList();

        builder.insert("or replace", IndicatorValue.class, values);
    }

    private void addIdsOfUpdatedOrDeleted(JpaUpdateBuilder builder) throws JSONException {
        for(Site site : all) {
            builder.addExecution(site.getId());
        }
    }

    private void parseRegion(String regionId) {
        String[] parts = regionId.split("/");
        databaseId = Integer.parseInt(parts[1]);
    }
}
