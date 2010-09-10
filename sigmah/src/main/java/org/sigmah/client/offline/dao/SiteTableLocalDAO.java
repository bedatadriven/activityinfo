/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.offline.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	public Map<Integer, Set<AdminEntity>> getSiteIdToEntitiesMap(
			Set<Integer> siteIds) {

		List<AdminEntity> entities = adminEntityDAO.findBySiteIds(siteIds);
		Connection conn = null;

		Map<Integer, Set<AdminEntity>> map = new HashMap<Integer, Set<AdminEntity>>(
				siteIds.size());
		try {
			conn = provider.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement(ENTITY_TO_SITES_QUERY);
			ResultSet r;
			r = stmt.getResultSet();

			HashMap<Integer, AdminEntity> cache = new HashMap<Integer, AdminEntity>();

			if (r != null && r.next()) {
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
				adminSet.add(linked);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
		
		
	private final static String  ENTITY_TO_SITES_QUERY = 
		"    select"
		+"       this_.SiteId as siteId,"
		+"       entity6_.AdminEntityId as adminId "
		+"   from"
		+"       Site this_ "
		+"   inner join"
		+"       Activity activity4_ "
		+"           on this_.ActivityId=activity4_.ActivityId "
		+"   inner join"
		+"       UserDatabase database5_ "
		+"           on activity4_.DatabaseId=database5_.DatabaseId "
		+"   left outer join"
		+"       Project database5_1_ "
		+"           on database5_.DatabaseId=database5_1_.DatabaseId "
		+"   inner join"
		+"       Location location2_ "
		+"           on this_.LocationId=location2_.LocationID "
		+"   inner join"
		+"       LocationAdminLink adminentit12_ "
		+"           on location2_.LocationID=adminentit12_.LocationId "
		+"   inner join"
		+"       AdminEntity entity6_ "
		+"           on adminentit12_.AdminEntityId=entity6_.AdminEntityId "
		+"   inner join"
		+"       AdminLevel level7_ "
		+"           on entity6_.AdminLevelId=level7_.AdminLevelId "
		+"   inner join"
		+"       LocationType locationty3_ "
		+"           on location2_.LocationTypeID=locationty3_.LocationTypeId "
		+"   inner join"
		+"       Partner partner1_ "
		+"           on this_.PartnerId=partner1_.PartnerId";
    
}
