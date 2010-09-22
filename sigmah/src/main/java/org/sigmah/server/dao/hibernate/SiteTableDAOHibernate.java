/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.ResultTransformer;
import org.sigmah.server.dao.SiteProjectionBinder;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.server.dao.hibernate.criterion.SiteAdminOrder;
import org.sigmah.server.dao.hibernate.criterion.SiteIndicatorOrder;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.SiteOrder;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates data-access calls needed to retrieve and project
 * tables of sites and their various fixed and user-defined
 * qualities, including dates, indicator values, administrative levels,
 * etc.
 * <p/>
 * Properties of the following entities are retrieved through an initial "base" query:
 * <ul>
 * <li>Site</li>
 * <li>Location</li>
 * <li>Partner</li>
 * <li>Activity</li>
 * <li>Database (Database)</li>
 * </ul>
 * <p/>
 * Subsequent queries retrieve and flatten user-defined columns defined in
 * <code>AdminLevel</code>, <code>Indicator</code>, and <code>AttributeGroup</code>
 * and <code>Attribute</code>
 * <p/>
 * Calls to SiteTableDAOHibernate are bound to particular data structures
 * through implementations of the <code>SiteProjectionBinder</code>
 * <p/>
 * Design choices: it also might be possible to retrieve the same
 * data using subqueries or derived tables instead of multiple select statements which would
 * shift much of the logic/burden towards the SQL server. Would need to
 * move significantly away from Hibernate, either in using native SQL queries
 * or straight-up JDBC. Performance? Code maintainability...?
 *
 * @author Alex Bertram
 */
public class SiteTableDAOHibernate implements SiteTableDAO {

    private final EntityManager em;

    @Inject
    public SiteTableDAOHibernate(EntityManager em) {
        this.em = em;
    }


    /**
     * @param source
     * @return The index of the given column in the <code>values</code>
     *         array provided to <code>SiteProjectionBinder.newInstance</code>
     */
    public static int getColumnIndex(SiteTableColumn source) {
        int index = 0;
        for (SiteTableColumn col : SiteTableColumn.values()) {
            if (col == source) {
                return index;
            }
            index++;
        }
        throw new Error();
    }

    public static Map<SiteTableColumn, Integer> getColumnMap() {
        Map<SiteTableColumn, Integer> map = new HashMap<SiteTableColumn, Integer>();
        int index = 0;
        for (SiteTableColumn col : SiteTableColumn.values()) {
            map.put(col, index++);
        }
        return map;
    }

    @Override
    public int queryPageNumber(User user, Filter filter, List<SiteOrder> orderings, int pageSize, int siteId) {
        Criteria criteria = createBaseCriteria(filter);
        addOrderings(criteria, orderings);

        criteria.setProjection(Projections.property("site.id"));
        List<Integer> results = criteria.list();

        int index = results.indexOf(siteId);

        if (index == -1) {
            return -1;
        }

        return index / pageSize; // java integer division rounds down to zero
    }

    /**
     * @param <RowT>    The type of the data structure used to store the results of the query
     * @param filter    Filter to apply to the "base" query
     * @param orderings Hibernate orderings to apply to the "base" query
     * @param binder    Instanceof {@link SiteProjectionBinder} responsible for
     *                  binding the results of the query to the <code>RowT</code> data structure.
     * @param retrieve  Bitmask of additional entities to flatten and retrieve:
     *                  RETRIEVE_ALL, RETRIEVE_NONE, RETRIEVE_ADMIN, RETRIEVE_INDICATORS, RETRIEVE_ATTRIBS
     * @param offset    For paged queries, the first row to retrieve (0-based)
     * @param limit     For paged queries, the maximum number of rows to retrieve
     * @return
     */

    @Override
    public <RowT> List<RowT> query(
            User user,
            Filter filter,
            List<SiteOrder> orderings,
            final SiteProjectionBinder<RowT> binder,
            final int retrieve,
            int offset,
            int limit) {

        Criteria criteria = createBaseCriteria(filter);

        ProjectionList projection = Projections.projectionList();

        for (SiteTableColumn column : SiteTableColumn.values()) {
            projection.add(Projections.property(column.property()), column.alias());
        }
        criteria.setProjection(projection);

        addOrderings(criteria, orderings);

        if (offset != 0) {
            criteria.setFirstResult(offset);
        }
        if (limit > 0) {
            criteria.setMaxResults(limit);
        }

        final Map<Integer, RowT> siteMap = new HashMap<Integer, RowT>();

        criteria.setResultTransformer(new ResultTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public List transformList(List collection) {
                return collection;
            }

            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                RowT site = binder.newInstance(aliases, tuple);

                if (retrieve != 0) {
                    siteMap.put((Integer) tuple[0], site);
                }
                return site;
            }
        });

        List<RowT> sites = criteria.list();

        if ((retrieve & RETRIEVE_ADMIN) != 0) {
            this.addAdminEntities(siteMap, filter, binder);
        }
        if ((retrieve & RETRIEVE_ATTRIBS) != 0) {
            this.addAttributeValues(siteMap, filter, binder);
        }
        if ((retrieve & RETRIEVE_INDICATORS) != 0) {
            this.addIndicatorValues(siteMap, filter, binder);
        }

        return sites;
    }

    private <RowT> void addOrderings(Criteria criteria, List<SiteOrder> orderings) {
        if (orderings != null) {
            for (SiteOrder order : orderings) {

                if(order.getColumn().startsWith(IndicatorDTO.PROPERTY_PREFIX)) {

                    Indicator indicator = em.find(Indicator.class, IndicatorDTO.indicatorIdForPropertyName(order.getColumn()));
                    criteria.addOrder(new SiteIndicatorOrder(indicator, !order.isDescending()));

                } else if (order.getColumn().startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
                    
                    int levelId = AdminLevelDTO.levelIdForPropertyName(order.getColumn());
                    criteria.addOrder(new SiteAdminOrder(levelId, !order.isDescending()));

                } else {
                    criteria.addOrder(order.isDescending() ? Order.desc(order.getColumn()) : Order.asc(order.getColumn()));
                }
            }
        }
    }

    @Override
    public int queryCount(Filter filter) {

        return ((Number) createBaseCriteria(filter)
                .setProjection(Projections.rowCount())
                .uniqueResult()).intValue();

    }

    private Criteria createBaseCriteria(Filter filter) {

        Session session = ((HibernateEntityManager) em).getSession();

        Criteria criteria = session.createCriteria(Site.class, "site");

        /*
           * make sure we get location and partner data by joining
           */
        criteria.createAlias("site.partner", "partner")
                .setFetchMode("partner", FetchMode.JOIN);

        criteria.createAlias("site.location", "location")
                .setFetchMode("site.location", FetchMode.JOIN);

        criteria.createAlias("location.locationType", "locationType")
                .setFetchMode("location.locationType", FetchMode.JOIN);

        criteria.createAlias("site.activity", "activity")
                .setFetchMode("activity", FetchMode.JOIN);

        criteria.createAlias("activity.database", "database");

        if (filter != null) {
            criteria.add(FilterCriterionBridge.resolveCriterion(filter));
        }

        return criteria;
    }


    private DetachedCriteria createBaseDetachedCriteria(Filter filter) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Site.class, "site");

        /*
           * make sure we get location and partner data by joining
           */
        criteria.createAlias("site.partner", "partner")
                .setFetchMode("partner", FetchMode.JOIN);

        criteria.createAlias("site.location", "location")
                .setFetchMode("site.location", FetchMode.JOIN);

        criteria.createAlias("location.locationType", "locationType")
                .setFetchMode("location.locationType", FetchMode.JOIN);

        criteria.createAlias("site.activity", "activity")
                .setFetchMode("activity", FetchMode.JOIN);

        criteria.createAlias("activity.database", "database");

        if(filter != null) {
            criteria.add(FilterCriterionBridge.resolveCriterion(filter));
        }

        return criteria;
    }


    @SuppressWarnings("unchecked")
    protected <SiteT> void addAdminEntities(
            Map<Integer, SiteT> siteMap,
            Filter filter,
            SiteProjectionBinder<SiteT> binder) {


        /* First, get all admin entities associated with this collection
           * of sites
           */

        Session session = ((HibernateEntityManager) em).getSession();


        List<AdminEntity> entities = session.createCriteria(AdminEntity.class, "entity")
                .createAlias("entity.locations", "location")
                .createAlias("location.sites", "site")
                .add(Subqueries.propertyIn("site.id",
                        createBaseDetachedCriteria(filter)
                                .setProjection(Property.forName("site.id"))))
                .list();


        Map<Integer, AdminEntity> entityMap = new HashMap<Integer, AdminEntity>(entities.size());
        for (AdminEntity entity : entities) {
            entityMap.put(entity.getId(), entity);
        }

        /* Now query for the link between sites and admin entities */

        List<Object[]> list = createBaseCriteria(filter)
                .createAlias("location.adminEntities", "entity")
                .createAlias("entity.level", "level")
                .setProjection(Projections.projectionList()
                        .add(Projections.property("site.id"))
                        .add(Projections.property("entity.id"))
                )
                .list();

        for (Object[] tuple : list) {

            int siteId = (Integer) tuple[0];

            SiteT site = siteMap.get(siteId);
            if (site != null) {

                int entityId = (Integer) tuple[1];

                binder.setAdminEntity(site, entityMap.get(entityId));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <SiteT> void addIndicatorValues(Map<Integer, SiteT> siteMap, Filter filter, SiteProjectionBinder<SiteT> transformer) {


        List<Object[]> list = createBaseCriteria(filter)
                .createAlias("site.reportingPeriods", "period")
                .createAlias("period.indicatorValues", "values")
                .createAlias("values.indicator", "indicator")
                .setFetchMode("values", FetchMode.JOIN)
                .setProjection(Projections.projectionList()
                        .add(Projections.property("site.id"))
                        .add(Projections.property("indicator.id"))
                        .add(Projections.property("indicator.aggregation"))
                        .add(Projections.property("values.value"))
                )
                .list();

        for (Object[] tuple : list) {

            SiteT site = siteMap.get((Integer) tuple[0]);
            if (site != null) {
                transformer.addIndicatorValue(site, (Integer) tuple[1], (Integer) tuple[2], (Double) tuple[3]);
            }
        }
    }


    @SuppressWarnings("unchecked")
    protected <SiteT> void addAttributeValues(
            Map<Integer, SiteT> siteMap,
            Filter filter,
            SiteProjectionBinder<SiteT> transformer) {

        List<Object[]> list = createBaseCriteria(filter)
                .createAlias("site.attributeValues", "attributeValue")
                .createAlias("attributeValue.attribute", "attribute")
                .setFetchMode("values", FetchMode.JOIN)
                .setProjection(Projections.projectionList()
                        .add(Projections.property("site.id"))
                        .add(Projections.property("attribute.id"))
                        .add(Projections.property("attributeValue.value"))
                )
                .list();

        for (Object[] tuple : list) {

            SiteT site = siteMap.get((Integer) tuple[0]);
            if (site != null) {
                transformer.setAttributeValue(site, (Integer) tuple[1], (Boolean) tuple[2]);
            }
        }
    }
}
