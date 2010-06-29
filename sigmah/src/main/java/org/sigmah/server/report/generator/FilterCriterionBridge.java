/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import org.hibernate.criterion.*;
import org.sigmah.server.dao.SiteTableColumn;
import org.sigmah.server.domain.Location;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.Filter;

public class FilterCriterionBridge {
    /**
     * Given a filter, creates the Hibernate criteria need to call
     * {@link org.sigmah.server.dao.hibernate.SiteTableDAOHibernate}
     *
     * @param filter
     * @return
     */
    public static Criterion resolveCriterion(Filter filter) {


        Conjunction criterion = Restrictions.conjunction();

        addRestriction(criterion, filter, DimensionType.Database, SiteTableColumn.database_id);
        addRestriction(criterion, filter, DimensionType.Activity, SiteTableColumn.activity_id);
        addRestriction(criterion, filter, DimensionType.Partner, SiteTableColumn.partner_id);
        addAdminRestriction(criterion, filter);

        if(filter.isDateRestricted()) {

            if(filter.getMinDate() != null && filter.getMaxDate() == null) {
                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.ge(SiteTableColumn.date1.property(), filter.getMinDate()))
                        .add( Restrictions.ge(SiteTableColumn.date2.property(), filter.getMinDate())) );

            } else if(filter.getMaxDate() != null && filter.getMinDate() == null) {

                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.le(SiteTableColumn.date1.property(), filter.getMaxDate()))
                        .add( Restrictions.le(SiteTableColumn.date2.property(), filter.getMaxDate())) );

            } else {
                criterion.add( Restrictions.disjunction()
                        .add( Restrictions.between(SiteTableColumn.date1.property(), filter.getMinDate(), filter.getMaxDate()))
                        .add( Restrictions.between(SiteTableColumn.date2.property(), filter.getMinDate(), filter.getMaxDate())) );

            }

        }

        return criterion;
    }

    public static void addRestriction(Conjunction criterion, Filter filter, DimensionType dimType, SiteTableColumn column) {
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
