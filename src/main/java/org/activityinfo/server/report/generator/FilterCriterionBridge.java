package org.activityinfo.server.report.generator;

import org.activityinfo.server.domain.Location;
import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;
import org.hibernate.criterion.*;

public class FilterCriterionBridge {
    /**
     * Given a filter, creates the Hibernate criteria need to call
     * {@link org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate}
     *
     * @param filter
     * @return
     */
    public static Criterion resolveCriterion(Filter filter) {


        Conjunction criterion = Restrictions.conjunction();

        addRestriction(criterion, filter, DimensionType.Database, SiteColumn.database_id);
        addRestriction(criterion, filter, DimensionType.Activity, SiteColumn.activity_id);
        addRestriction(criterion, filter, DimensionType.Partner, SiteColumn.partner_id);
        addAdminRestriction(criterion, filter);

        if(filter.isDateRestricted()) {

            if(filter.getMinDate() != null && filter.getMaxDate() == null) {
                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.ge(SiteColumn.date1.property(), filter.getMinDate()))
                        .add( Restrictions.ge(SiteColumn.date2.property(), filter.getMinDate())) );

            } else if(filter.getMaxDate() != null && filter.getMinDate() == null) {

                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.le(SiteColumn.date1.property(), filter.getMaxDate()))
                        .add( Restrictions.le(SiteColumn.date2.property(), filter.getMaxDate())) );

            } else {
                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.between(SiteColumn.date1.property(), filter.getMinDate(), filter.getMaxDate()))
                        .add( Restrictions.between(SiteColumn.date2.property(), filter.getMinDate(), filter.getMaxDate())) );

            }

        }

        return criterion;
    }

    public static void addRestriction(Conjunction criterion, Filter filter, DimensionType dimType, SiteColumn column) {
        if(filter.isRestricted(dimType)) {
            criterion.add( Restrictions.in(column.property(), filter.getRestrictions(dimType)));
        }
    }

    public static void addAdminRestriction(Conjunction criterion, Filter filter) {
        if(filter.isRestricted(DimensionType.AdminLevel))  {
            DetachedCriteria locations = DetachedCriteria.forClass(Location.class);
            locations.createAlias("adminEntities", "entity");
            locations.add( Restrictions.in("entity.id", filter.getRestrictions(DimensionType.AdminLevel)  ));
            locations.setProjection( Projections.property("id"));

            criterion.add( Subqueries.propertyIn("location.id", locations) );
        }
    }
}
