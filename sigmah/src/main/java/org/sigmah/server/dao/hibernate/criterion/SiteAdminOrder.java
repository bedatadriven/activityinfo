package org.sigmah.server.dao.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;


/**
 * Hibernate {@link org.hibernate.criterion.Order Order} clause that orders
 * {@link org.sigmah.server.domain.Site}s by AdminEntity membership at
 * a given {@link org.sigmah.server.domain.AdminEntity} level.
 *
 * @author Alex Bertram
 */
public class SiteAdminOrder extends Order {

	private int levelId;
	private boolean ascending;
	

	public SiteAdminOrder(int levelId, boolean ascending) {
		super(null, ascending);	
		
		this.ascending = ascending;
		this.levelId = levelId;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		StringBuffer fragment = new StringBuffer();
		fragment.append("(SELECT Name FROM AdminEntity AS entity " +
                  "  WHERE (AdminLevelId = ");
		
		fragment.append(levelId);
		
		fragment.append(" AND (AdminEntityId IN " + 
                  " (SELECT AdminEntityId " +
                  	"	FROM   LocationAdminLink AS link " +
                  	"   WHERE (LocationId = ");
		
		fragment.append(criteriaQuery.getColumn(criteria, "location.id"));
		fragment.append(")))))");
		fragment.append( ascending ? " asc" : " desc" );
		
		return fragment.toString();
	}
	
}
