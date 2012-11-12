/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.sync;

import java.io.IOException;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.inject.Inject;

public class SiteUpdateBuilder implements UpdateBuilder {

    private final EntityManager entityManager;

    private int databaseId;
    private SqliteBatchBuilder batch;
    private long localVersion;

    @Inject
    public SiteUpdateBuilder(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws IOException {
    	batch = new SqliteBatchBuilder();
    	databaseId = parseDatabaseId(request.getRegionId());
        localVersion = TimestampHelper.fromString(request.getLocalVersion());
             
        SyncRegionUpdate update = new SyncRegionUpdate();
        long latestVersion = queryLatestVersion();
        
        if(latestVersion > localVersion) {
	        SqlQuery updatedQuery = updatedSites();
	        String updatedIds = SqlQueryUtil.queryIdSet(entityManager, updatedQuery);
	        
	        deleteUpdated(updatedIds);	
	        insertUpdatedSites();
	        insertUpdatedReportingPeriods();	        
	        insertUpdatedAttributeValues();
	        insertUpdatedIndicatorValues();
	        
	        update.setSql(batch.build());
        }
        
        update.setComplete(true);
        update.setVersion(Long.toString(latestVersion));
        return update;
    }
    
    private int parseDatabaseId(String regionId) {
        String[] parts = regionId.split("/");
        return Integer.parseInt(parts[1]);
    }

	private void insertUpdatedIndicatorValues() {
		SqlQuery ivQuery = updatedIndicatorValueQuery();

		batch.insert()
			.into(Tables.INDICATOR_VALUE)
			.from(ivQuery)
			.execute(entityManager);
	}

	private void insertUpdatedReportingPeriods() {
		SqlQuery rpQuery = updatedPeriodQuery();

		batch.insert()
			.into(Tables.REPORTING_PERIOD)
			.from(rpQuery)
			.execute(entityManager);
	}

	private void insertUpdatedAttributeValues() {
		SqlQuery avQuery = updatedAttributeValuesQuery();
		
		batch.insert()
			.into(Tables.ATTRIBUTE_VALUE)
			.from(avQuery)
			.execute(entityManager);
	}

	private void insertUpdatedSites() {
		SqlQuery siteQuery = updatedSitesQuery();
		
		// insert sites
		batch.insert()
			.into(Tables.SITE)
			.from(siteQuery)
			.execute(entityManager);
	}

	private void deleteUpdated(String updatedIds) throws IOException {
		batch.addStatement("DELETE FROM site WHERE siteId IN " + updatedIds);
		batch.addStatement("DELETE FROM attributevalue WHERE siteId IN " + updatedIds);
		batch.addStatement("DELETE FROM indicatorvalue WHERE reportingperiodid IN " +
				"(SELECT reportingperiodid FROM reportingperiod WHERE siteId IN " + updatedIds + ")");
		batch.addStatement("DELETE FROM reportingperiod WHERE siteId IN " + updatedIds);
		
		// there seem to be some clients left in an inconsistent state, probably due to 
		// errors on the server side earlier. So we clean up.
		batch.addStatement("DELETE FROM indicatorvalue WHERE indicatorvalue.reportingperiodid " +
				"NOT IN (select reportingperiodid from reportingperiod)");
		
	}

	private SqlQuery updatedSites() {
		return SqlQuery.select()
			.from(Tables.SITE, "s")
			.leftJoin(Tables.ACTIVITY, "a").on("a.ActivityId=s.ActivityId")
			.appendColumn("s.SiteId")
			.where("a.DatabaseId").equalTo(databaseId)
			.where("s.timeEdited").greaterThan(localVersion);
	}

	private SqlQuery updatedIndicatorValueQuery() {
		return SqlQuery.select()
			.from(Tables.INDICATOR_VALUE, "iv")
			.leftJoin(Tables.REPORTING_PERIOD, "rp").on("rp.ReportingPeriodId=iv.ReportingPeriodId")
			.leftJoin(Tables.SITE, "s").on("rp.SiteId = s.SiteId")
			.leftJoin(Tables.ACTIVITY, "a").on("s.ActivityId = a.ActivityId")
			.appendColumn("iv.IndicatorId")
			.appendColumn("iv.ReportingPeriodId")
			.appendColumn("iv.Value")
			.where("a.DatabaseId").equalTo(databaseId)
			.where("s.timeEdited").greaterThan(localVersion)
			.whereTrue("s.dateDeleted IS NULL");
	}

	private SqlQuery updatedPeriodQuery() {
		return SqlQuery.select()
			.from(Tables.REPORTING_PERIOD, "rp")
			.leftJoin(Tables.SITE, "s").on("rp.SiteId = s.SiteId")
			.leftJoin(Tables.ACTIVITY, "a").on("s.ActivityId = a.ActivityId")
			.appendColumn("rp.ReportingPeriodId")
			.appendColumn("rp.SiteId")
			.appendColumn("rp.Date1")
			.appendColumn("rp.Date2")
			.where("a.DatabaseId").equalTo(databaseId)
			.where("s.timeEdited").greaterThan(localVersion)
			.whereTrue("s.dateDeleted IS NULL");
	}

	private SqlQuery updatedAttributeValuesQuery() {
		return SqlQuery.select()
			.from(Tables.ATTRIBUTE_VALUE, "av")
			.leftJoin(Tables.SITE, "s").on("av.SiteId = s.SiteId")
			.leftJoin(Tables.ACTIVITY, "a").on("s.ActivityId = a.ActivityId")
			.appendColumn("av.AttributeId")
			.appendColumn("av.SiteId")
			.appendColumn("av.Value")
			.where("a.DatabaseId").equalTo(databaseId)
			.where("s.timeEdited").greaterThan(localVersion)
			.whereTrue("s.dateDeleted IS NULL");
	}

	private SqlQuery updatedSitesQuery() {
		return SqlQuery.select()
			.from(Tables.SITE, "s")
			.leftJoin(Tables.ACTIVITY, "a").on("a.ActivityId=s.ActivityId")
			.appendColumn("s.SiteId")
			.appendColumn("s.Date1")
			.appendColumn("s.Date2")
			.appendColumn("s.ActivityId")
			.appendColumn("s.LocationId")
			.appendColumn("s.PartnerId")
			.appendColumn("s.ProjectId")
			.appendColumn("s.Comments")
			.where("s.timeEdited").greaterThan(localVersion)
			.where("a.DatabaseId").equalTo(databaseId)
			.whereTrue("s.dateDeleted IS NULL");
	}

	private long queryLatestVersion() {
		SqlQuery query = SqlQuery.select()
			.from(Tables.SITE, "s")
			.leftJoin(Tables.ACTIVITY, "a").on("a.ActivityId=s.ActivityId")		
			.appendColumn("MAX(timeEdited)", "last")
			.where("a.DatabaseId").equalTo(databaseId);
		
		return SqlQueryUtil.queryLong(entityManager, query);
		
	}	
}
