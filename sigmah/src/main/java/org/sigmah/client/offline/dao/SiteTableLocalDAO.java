/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.offline.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.persistence.client.ConnectionProvider;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.inject.Inject;

/**
 * 
 * A dao for fetching sites when off-line.
 * 
 * @author jon
 *
 */
public class SiteTableLocalDAO extends OfflineDAO<User, Integer> {
	
	private AdminDAO adminEntityDAO;
	private ConnectionProvider provider;
	
	@Inject
	protected SiteTableLocalDAO(EntityManager em, AdminDAO adminEntityDAO, ConnectionProvider provider) {
		super(em);
		this.adminEntityDAO = adminEntityDAO;
		this.provider = provider;
	}

	@Override
	public User findById(Integer primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Site> querySites(Map <String, Object> criteria, List<SortInfo> order) {

		StringBuilder sql = new StringBuilder();
		// include all site fields in query
		sql.append("select * from Site");
		// add where to sql
		StringBuilder where = new StringBuilder();
		for (String key: criteria.keySet()) {
			append(where, " where ", " = ", " and ", key, criteria.get(key));
		}
		sql.append(where.toString());
		
		// add order to sql
		StringBuilder oBuff = new StringBuilder();
		for (SortInfo info: order) {
			append(oBuff, " order by ", " " , " , ", info.getSortField(), info.getSortDir()); 
		}
		sql.append(oBuff.toString());
		
		// execute
		Query q=em.createNativeQuery(sql.toString(), Site.class);
		return q.getResultList();
	}
	
	public int queryPageNumber(User user, Map<String, Object> criterion,
			List<SortInfo> order, int pageSize, int siteId) {
		
		List<Site> results = querySites(criterion, order);
		int index = results.indexOf(siteId);
		if (index == -1) {
			return -1;
		}
		return index / pageSize; // java integer division rounds down to zero
	}
	
	public List<Site> query(User user, Map<String, Object> criterion,
			List<SortInfo> order) {

		return querySites(criterion, order);
	}
	
	private static StringBuilder append(StringBuilder buff, String token , String op, String separator, String field, Object o) {
		if (o != null) {
			if (buff.length() == 0) {
				buff.append(token);
			} else {
				buff.append(separator);
			}
			buff.append(field);
			buff.append(op);
			if (o instanceof Boolean) {
				buff.append("1");
			} else {
				buff.append(o.toString());
			}
		}
		return buff; 
	}

	public Map<Integer, Map<Integer, Boolean>> getSiteIdToAttributeMap(Set<Integer> siteIds) {
		Map<Integer, Map<Integer, Boolean>> map = new HashMap<Integer, Map<Integer, Boolean>>(
				siteIds.size());
		Connection conn = null;
		try {
			conn = provider.getConnection();
			StringBuilder buff = new StringBuilder();
			buff.append(SITE_ATTRIBUTES_QUERY);
			buff.append(" where Site.SiteId in (");
			append(buff, siteIds);
			buff.append(")");
			Statement stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(buff.toString());
			r = stmt.getResultSet();

			while (r.next()) {

				Log.debug("found attribute");
				Map<Integer, Boolean> attributes;
				Integer siteId = r.getInt(1);
				if (map.containsKey(siteId)) {
					attributes = map.get(siteId);
				} else {
					attributes = new HashMap<Integer,Boolean>();
					map.put(siteId, attributes);
				}
				attributes.put(r.getInt(2), r.getBoolean(3));			
				Log.debug("found attribute " + attributes);
			}

		} catch (SQLException e) {
			Log.debug("Query Failed: " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public Map<Integer, Map<Integer, Double>> getSiteIdToIndicatorMap(Set<Integer> siteIds) {
		Map<Integer, Map<Integer, Double>> map = new HashMap<Integer, Map<Integer, Double>>(
				siteIds.size());
		Connection conn = null;
		try {
			conn = provider.getConnection();
			StringBuilder buff = new StringBuilder();
			buff.append(SITE_INDICATOR_QUERY);
			buff.append(" where Site.SiteId in (");
			append(buff, siteIds);
			buff.append(")");
			Statement stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(buff.toString());
			r = stmt.getResultSet();

			while (r.next()) {

				Map<Integer, Double> indicators;
				Integer siteId = r.getInt(1);
				if (map.containsKey(siteId)) {
					indicators = map.get(siteId);
				} else {
					indicators = new HashMap<Integer,Double>();
					map.put(siteId, indicators);
				}
				indicators.put(r.getInt(2), r.getDouble(3));		

				Log.debug("found indicator " + indicators);
			}
			
		} catch (SQLException e) {
			Log.debug("Query Failed: " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
		
	}
	
	
	public Map<Integer, Set<AdminEntity>> getSiteIdToEntitiesMap(
			Set<Integer> siteIds) {

		List<AdminEntity> entities = adminEntityDAO.findBySiteIds(siteIds);
		Connection conn = null;

		Map<Integer, Set<AdminEntity>> map = new HashMap<Integer, Set<AdminEntity>>(
				siteIds.size());
		try {
			conn = provider.getConnection();
			StringBuilder buff = new StringBuilder();
			buff.append(ENTITY_TO_SITES_QUERY);
			buff.append(" where Site.SiteId in (");
			append(buff, siteIds);
			buff.append(")");
		
			Statement stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(buff.toString());
			HashMap<Integer, AdminEntity> cache = new HashMap<Integer, AdminEntity>();

			while ( r.next()) {
				Log.debug("found entity");
				Integer siteId = r.getInt(1);
				Set<AdminEntity> adminSet;
				if (map.containsKey(siteId)) {
					adminSet = map.get(siteId);
				} else {
					adminSet = new HashSet<AdminEntity>();
					map.put(siteId, adminSet);
				}
				int adminId = r.getInt(2);
				AdminEntity linked;
				if (cache.containsKey(adminId)) {
					linked = cache.get(adminId);
				} else {
					linked = adminEntityDAO.findById(adminId);
					cache.put(adminId, linked);
				}
				Log.debug(linked.toString());
				adminSet.add(linked);
				Log.debug("entity set " + adminSet.toString());
			}
		} catch (SQLException e) {
			Log.debug("Query Failed: " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return map;
	}
		
    private StringBuilder append(StringBuilder buff, Set l){
    	boolean first = true;
    	for (Object o: l) {
    		if (!first) {
    			buff.append(", ");
    		} else {
    			first = false;
    		}
    		buff.append(o.toString());
    	}
    	return buff;
    }
	
	private final static String  SITE_ATTRIBUTES_QUERY = 
			"select"
		+"      site.SiteId,"
		+"      attribute7_.AttributeId as attributeId,"
		+"      attributev6_.Value as attributeValue "
		+"  from"
		+"      Site site"
		+"  inner join"
		+"      Activity activity4_ "
		+"          on site.ActivityId=activity4_.ActivityId "
		+"  inner join"
		+"      AttributeValue attributev6_ "
		+"          on site.SiteId=attributev6_.SiteId "
		+"  inner join"
		+"      Attribute attribute7_ "
		+"          on attributev6_.AttributeId=attribute7_.AttributeId ";

	
	private final static String SITE_INDICATOR_QUERY = 
			" select"
		+"       site.SiteId,"
		+"       indicator8_.IndicatorId as y1_,"
		+"       indicator8_.Aggregation as y2_,"
		+"       values7_.Value as y3_ "
		+"   from"
		+"       Site site"
		+"   inner join"
		+"       ReportingPeriod period6_ "
		+"           on site.SiteId=period6_.SiteId "
		+"   inner join"
		+"       IndicatorValue values7_ "
		+"           on period6_.ReportingPeriodId=values7_.ReportingPeriodId "
		+"   inner join"
		+"       Indicator indicator8_ "
		+"           on values7_.IndicatorId=indicator8_.IndicatorId";
	
		
	
	private final static String  ENTITY_TO_SITES_QUERY = 
		"    select"
		+"       site.SiteId as siteId,"
		+"       entity6_.AdminEntityId as adminId "
		+"   from"
		+"       Site site "
		+"   inner join"
		+"       Activity activity4_ "
		+"           on site.ActivityId=activity4_.ActivityId "
		+"   inner join"
		+"       Location location2_ "
		+"           on site.LocationId=location2_.LocationID "
		+"   inner join"
		+"       LocationAdminLink adminentit12_ "
		+"           on location2_.LocationID=adminentit12_.LocationId "
		+"   inner join"
		+"       AdminEntity entity6_ "
		+"           on adminentit12_.AdminEntityId=entity6_.AdminEntityId ";
    
}
