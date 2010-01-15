package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.Site;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.domain.SiteColumn;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.ResultTransformer;

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

    private EntityManager em;

    @Inject
    public SiteTableDAOHibernate(EntityManager em) {
        this.em = em;
    }


    /**
     * @param source
     * @return The index of the given column in the <code>values</code>
     *         array provided to <code>SiteProjectionBinder.newInstance</code>
     */
    public static int getColumnIndex(SiteColumn source) {
        int index = 0;
        for (SiteColumn col : SiteColumn.values()) {
            if (col == source) {
                return index;
            }
            index++;
        }
        throw new Error();
    }

    public static Map<SiteColumn, Integer> getColumnMap() {
        Map<SiteColumn, Integer> map = new HashMap<SiteColumn, Integer>();
        int index = 0;
        for (SiteColumn col : SiteColumn.values()) {
            map.put(col, index++);
        }
        return map;
    }

    @Override
    public int queryPageNumber(User user, Criterion criterion, List<Order> orderings, int pageSize, int siteId) {


        Session session = ((HibernateEntityManager) em).getSession();

        Criteria criteria = createBaseCriteria(criterion);
        if (orderings != null) {
            for (Order order : orderings) {
                criteria.addOrder(order);
            }
        }

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
     * @param criterion Hibernate criterion to apply to the "base" query
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
            Criterion criterion,
            List<Order> orderings,
            final SiteProjectionBinder<RowT> binder,
            final int retrieve,
            int offset,
            int limit) {

        Criteria criteria = createBaseCriteria(criterion);

        Session session = ((HibernateEntityManager) em).getSession();


        ProjectionList projection = Projections.projectionList();

        for (SiteColumn column : SiteColumn.values()) {
            projection.add(Projections.property(column.property()), column.alias());
        }
        criteria.setProjection(projection);

        if (orderings != null) {
            for (Order order : orderings) {
                criteria.addOrder(order);
            }
        }

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
            this.addAdminEntities(siteMap, criterion, binder);
        }
        if ((retrieve & RETRIEVE_ATTRIBS) != 0) {
            this.addAttributeValues(siteMap, criterion, binder);
        }
        if ((retrieve & RETRIEVE_INDICATORS) != 0) {
            this.addIndicatorValues(siteMap, criterion, binder);
        }

        return sites;
    }

    @Override
    public int queryCount(Conjunction criterion) {

        return ((Number) createBaseCriteria(criterion)
                .setProjection(Projections.rowCount())
                .uniqueResult()).intValue();

    }

    private Criteria createBaseCriteria(Criterion criterion) {


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

        if (criterion != null)
            criteria.add(criterion);

        return criteria;
    }


    private DetachedCriteria createBaseDetachedCriteria(Criterion criterion) {

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

        if (criterion != null)
            criteria.add(criterion);

        return criteria;
    }


    @SuppressWarnings("unchecked")
    protected <SiteT> void addAdminEntities(
            Map<Integer, SiteT> siteMap,
            Criterion criterion,
            SiteProjectionBinder<SiteT> binder) {


        /* First, get all admin entities associated with this collection
           * of sites
           */

        Session session = ((HibernateEntityManager) em).getSession();


        List<AdminEntity> entities = session.createCriteria(AdminEntity.class, "entity")
                .createAlias("entity.locations", "location")
                .createAlias("location.sites", "site")
                .add(Subqueries.propertyIn("site.id",
                        createBaseDetachedCriteria(criterion)
                                .setProjection(Property.forName("site.id"))))
                .list();


        Map<Integer, AdminEntity> entityMap = new HashMap<Integer, AdminEntity>(entities.size());
        for (AdminEntity entity : entities) {
            entityMap.put(entity.getId(), entity);
        }

        /* Now query for the link between sites and admin entities */

        List<Object[]> list = createBaseCriteria(criterion)
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
    protected <SiteT> void addIndicatorValues(Map<Integer, SiteT> siteMap, Criterion criterion, SiteProjectionBinder<SiteT> transformer) {


        List<Object[]> list = createBaseCriteria(criterion)
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
            Criterion criterion,
            SiteProjectionBinder<SiteT> transformer) {

        List<Object[]> list = createBaseCriteria(criterion)
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
