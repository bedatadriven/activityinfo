package org.activityinfo.server.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;


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
	
	//	Dialect dialect = criteriaQuery.getFactory().getDialect();

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
