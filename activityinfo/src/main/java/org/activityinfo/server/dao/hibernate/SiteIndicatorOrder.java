package org.activityinfo.server.dao.hibernate;

import org.activityinfo.server.domain.AggregationMethod;
import org.activityinfo.server.domain.Indicator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;


public class SiteIndicatorOrder extends Order {
	
	private int indicatorId;
	private int aggrMethod;
	private boolean ascending;
	
	public SiteIndicatorOrder(Indicator indicator, boolean ascending) {
		super(null, ascending);
	
		this.indicatorId = indicator.getId();
		this.aggrMethod = indicator.getAggregation();
		this.ascending = ascending;
		
		if(this.aggrMethod == AggregationMethod.SiteCount.code()) {
			throw new IllegalArgumentException("It doesn't really make much sense to sort a site list by a site count indicator because the value will be the same for all rows!");
		}
	}
	
	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {

	
		/* First we have to determine how to aggregate this
		 * indicator.
		 */
		
		String sqlFragment;
	
		String afn;
		if(aggrMethod == AggregationMethod.Average.code()) {
			afn = "AVG";
		} else if(aggrMethod == AggregationMethod.Sum.code()) {
			afn = "SUM";
		} else {
			throw new RuntimeException("Unknown aggregation type " + aggrMethod);
		}
	
		String[] siteIdColumns = criteriaQuery.getColumnsUsingProjection(criteria, "site.id");
		
		sqlFragment = String.format(
				"(SELECT %s(v.Value) FROM IndicatorValue v " +	
					"WHERE v.IndicatorId=%d AND " +
						  "v.ReportingPeriodId" +
								" IN (SELECT r.ReportingPeriodId FROM ReportingPeriod r " +
										"WHERE r.SiteId = %s))", afn, indicatorId, siteIdColumns[0] );
		
		
		if(ascending) {
			sqlFragment += " asc";
		} else {
			sqlFragment += " desc";
		}
	
		return sqlFragment;
		
	}
}
