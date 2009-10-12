package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.domain.AggregationMethod;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.model.*;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotDAOHibernateJdbc implements PivotDAO {

    private EntityManager em;

    @Inject
    public PivotDAOHibernateJdbc(EntityManager em) {
        this.em = em;
    }


    private interface Bundler {

        public void bundle(ResultSet rs, Bucket bucket) throws SQLException;
    }

    private static class SumAndAverageBundler implements Bundler {

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
             int aggMethod = rs.getInt(1);

            double value;
            if(aggMethod == AggregationMethod.Sum.code()) {
                value = rs.getDouble(2);
            } else if(aggMethod == AggregationMethod.Average.code()) {
                value = rs.getDouble(3);
            } else {
                assert false : "Database has a weird value for aggregation method = " + aggMethod;
                value = rs.getDouble(2);
            }

            bucket.setDoubleValue(value);
        }
    }

    private static class SiteCountBundler implements Bundler {

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            bucket.setDoubleValue( (double) rs.getInt(1) );
        }
    }

    private static class SimpleBundler implements Bundler {

        private final Dimension dimension;
        private final int labelColumnIndex;

        private SimpleBundler(Dimension dimension, int labelColumnIndex) {
            this.labelColumnIndex = labelColumnIndex;
            this.dimension = dimension;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            bucket.setCategory(dimension, new SimpleCategory(rs.getString(labelColumnIndex)));
        }
    }

    private static class EntityBundler implements Bundler {

        private final int idColumnIndex;
        private final Dimension dimension;

        private EntityBundler(Dimension key, int idColumnIndex) {
            this.idColumnIndex = idColumnIndex;
            this.dimension = key;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            bucket.setCategory(dimension, new EntityCategory(
                    rs.getInt(idColumnIndex),
                    rs.getString(idColumnIndex+1)));
        }
    }


    private static class OrderedEntityBundler implements Bundler {

        private final int idColumnIndex;
        private final Dimension dimension;

        private OrderedEntityBundler(Dimension dimension, int idColumnIndex) {
            this.idColumnIndex = idColumnIndex;
            this.dimension = dimension;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            bucket.setCategory(dimension, new EntityCategory(
                    rs.getInt(idColumnIndex),
                    rs.getString(idColumnIndex+1),
                    rs.getInt(idColumnIndex+2)  ));
        }
    }

    private static class YearBundler implements Bundler {


        private final Dimension dimension;
        private final int yearColumnIndex;

        private YearBundler(Dimension dimension, int yearColumnIndex) {
            this.dimension = dimension;
            this.yearColumnIndex = yearColumnIndex;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            bucket.setCategory(dimension, new YearCategory(
                    rs.getInt(yearColumnIndex) ));
        }
    }

    private static class MonthBundler implements Bundler {

        private final Dimension dimension;
        private final int yearColumnIndex;

        private MonthBundler(Dimension dimension, int yearColumnIndex) {
            this.dimension = dimension;
            this.yearColumnIndex = yearColumnIndex;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            int year = rs.getInt(yearColumnIndex);
            int month = rs.getInt(yearColumnIndex+1);

            bucket.setCategory(dimension, new MonthCategory(year, month));
        }
    }

    private static class QuarterBundler implements Bundler {

        private final Dimension dimension;
        private final int yearColumnIndex;

        private QuarterBundler(int yearColumnIndex, Dimension dimension) {
            this.dimension = dimension;
            this.yearColumnIndex = yearColumnIndex;
        }

        @Override
        public void bundle(ResultSet rs, Bucket bucket) throws SQLException {
            int year = rs.getInt(yearColumnIndex);
            int quarter = rs.getInt(yearColumnIndex+1);
            
            bucket.setCategory(dimension, new QuarterCategory(year, quarter));
        }
    }


    @Override
    public List<Bucket> aggregate(Filter filter, Set<Dimension> dimensions) {


        final List<Bucket> buckets = new ArrayList<Bucket>();

        queryForSumAndAverages(filter, dimensions, buckets);
        queryForSiteCounts(filter, dimensions, buckets);

        return buckets;

    }

    private void queryForSumAndAverages(Filter filter, Set<Dimension> dimensions, List<Bucket> buckets) {

        /* We're just going to go ahead and add all the tables we need to the SQL statement;
        * this saves us some work and hopefully the SQL server will optimze out any unused
        * tables
        */

        StringBuilder from = new StringBuilder();
        from.append(" IndicatorValue V " +
                "LEFT JOIN ReportingPeriod period ON (period.ReportingPeriodId=V.ReportingPeriodId) " +
                "LEFT JOIN Indicator ON (Indicator.IndicatorId = V.IndicatorId) " +
                "LEFT JOIN Site ON (period.SiteId = Site.SiteId) " +
                "LEFT JOIN Partner ON (site.PartnerId = Partner.PartnerId) " +
                "LEFT JOIN Location ON (Location.LocationId = Site.LocationId) " +
                "LEFT JOIN Activity ON (Activity.ActivityId = Site.ActivityId) " +
                "LEFT JOIN UserDatabase Db ON (Activity.DatabaseId = Db.DatabaseId) ");


        /* First add the indicator to the query: we can't aggregate values from different
        * indicators so this is a must
        *
        */
        StringBuilder columns = new StringBuilder();
        StringBuilder groupBy = new StringBuilder();

        columns.append("Indicator.Aggregation, " +
                "SUM(V.Value), " +
                "AVG(V.Value)");

        groupBy.append("V.IndicatorId, Indicator.Aggregation");

        StringBuilder where = new StringBuilder(
                "( (V.value <> 0 and Indicator.Aggregation=0) or Indicator.Aggregation=1) ");

        buildAndExecuteRestOfQuery(filter, dimensions, buckets, from, columns, where, groupBy, 4,
                                    new SumAndAverageBundler());
    }

    private void queryForSiteCounts(Filter filter, Set<Dimension> dimensions, List<Bucket> buckets) {

          /* We're just going to go ahead and add all the tables we need to the SQL statement;
          * this saves us some work and hopefully the SQL server will optimze out any unused
          * tables
          */

          StringBuilder from = new StringBuilder();
            from.append(" Site " +
                "LEFT JOIN Partner ON (site.PartnerId = Partner.PartnerId) " +
                "LEFT JOIN Location ON (Location.LocationId = Site.LocationId) " +
                "LEFT JOIN Activity ON (Activity.ActivityId = Site.ActivityId) " +
                "LEFT JOIN Indicator ON (Indicator.ActivityId = Activity.ActivityId) " +
                "LEFT JOIN UserDatabase Db ON (Activity.DatabaseId = Db.DatabaseId) " +
                "LEFT JOIN ReportingPeriod period ON (period.SiteId = Site.SiteId) ");

          /* First add the indicator to the query: we can't aggregate values from different
          * indicators so this is a must
          *
          */
          StringBuilder columns = new StringBuilder();
          StringBuilder groupBy = new StringBuilder();

          columns.append("COUNT(DISTINCT Site.SiteId)");

          groupBy.append("Indicator.IndicatorId");

          StringBuilder where = new StringBuilder(
                  "Indicator.Aggregation=2 and period.Monitoring=0 ");


          buildAndExecuteRestOfQuery(filter, dimensions, buckets, from, columns, where, groupBy, 2,
                                      new SiteCountBundler());
      }


    



    protected void buildAndExecuteRestOfQuery(Filter filter, Set<Dimension> dimensions,
                                              final List<Bucket> buckets,
                                              StringBuilder from,
                                              StringBuilder columns,
                                              StringBuilder where,
                                              StringBuilder groupBy, int nextColumnIndex, Bundler valueBundler) {

        final List<Bundler> bundlers = new ArrayList<Bundler>();
        bundlers.add(valueBundler);

        StringBuilder dimColumns = new StringBuilder();

        /* Now add any other dimensions  */

        for(Dimension dimension : dimensions) {

            if(dimension.getType() == DimensionType.Activity) {

                dimColumns.append(", Site.ActivityId, Activity.Name, Activity.SortOrder");
                bundlers.add(new OrderedEntityBundler(dimension, nextColumnIndex));
                nextColumnIndex += 3;

            } else if(dimension.getType() == DimensionType.ActivityCategory) {
                dimColumns.append(", Activity.Category");
                bundlers.add(new SimpleBundler(dimension, nextColumnIndex));
                nextColumnIndex += 1;

            } else if(dimension.getType() == DimensionType.Database ) {

                dimColumns.append(", Activity.DatabaseId, Db.Name");
                bundlers.add(new EntityBundler(dimension, nextColumnIndex));
                nextColumnIndex += 2;

            } else if(dimension.getType() == DimensionType.Partner) {

                dimColumns.append(", Site.PartnerId, Partner.Name");
                bundlers.add(new EntityBundler(dimension, nextColumnIndex));
                nextColumnIndex += 2;

            } else if(dimension.getType() == DimensionType.Indicator) {

                dimColumns.append(", Indicator.IndicatorId, Indicator.Name, Indicator.SortOrder");
                bundlers.add(new OrderedEntityBundler(dimension, nextColumnIndex));
                nextColumnIndex += 3;

            } else if(dimension.getType() == DimensionType.IndicatorCategory) {

                dimColumns.append(", Indicator.Category");
                bundlers.add(new SimpleBundler(dimension, nextColumnIndex));
                nextColumnIndex += 1;

            } else if(dimension instanceof DateDimension) {

                DateDimension dateDim = (DateDimension) dimension;

                if(dateDim.getUnit() == DateUnit.YEAR) {

                    dimColumns.append(", YEAR(Period.Date2) ");
                    bundlers.add(new YearBundler(dimension, nextColumnIndex));
                    nextColumnIndex += 1;

                } else if(dateDim.getUnit() == DateUnit.MONTH) {

                    dimColumns.append(", YEAR(Period.Date2), MONTH(Period.Date2)");
                    bundlers.add(new MonthBundler(dimension, nextColumnIndex));
                    nextColumnIndex += 2;

                } else if(dateDim.getUnit() == DateUnit.QUARTER) {

                    dimColumns.append(", YEAR(Period.Date2), FLOOR((MONTH(Period.Date2)-1)/3)+1");
                    bundlers.add(new QuarterBundler(nextColumnIndex, dimension));
                    nextColumnIndex += 2;
                }

            } else if(dimension instanceof AdminDimension) {

                AdminDimension adminDim = (AdminDimension)dimension;

                String tableAlias = "AdminLevel" + adminDim.getLevelId();

                from.append(" LEFT JOIN " +
                        "(SELECT L.LocationId, E.AdminEntityId, E.Name " +
                        "FROM LocationAdminLink L " +
                        "LEFT JOIN AdminEntity E ON (L.AdminEntityId=E.AdminEntityID) " +
                        "WHERE E.AdminLevelId=").append(adminDim.getLevelId())
                        .append(") AS ").append(tableAlias)
                        .append(" ON (Location.LocationId=").append(tableAlias).append(".LocationId)");

                dimColumns.append(", ").append(tableAlias).append(".AdminEntityId")
                        .append(", ").append(tableAlias).append(".Name");

                bundlers.add(new EntityBundler(adminDim, nextColumnIndex));
                nextColumnIndex += 2;
            }
        }

        /* add the dimensions to our column and group by list */
        columns.append(dimColumns);
        groupBy.append(dimColumns);


        /* And start on our where clause... */

        // don't include entities that have been deleted
        where.append(" and site.dateDeleted is null and " +
                  "activity.dateDeleted is null and " +
                  "indicator.dateDeleted is null and " +
                  "Db.dateDeleted is null ");


        final List<Object> parameters = new ArrayList<Object>();


        if(filter.getMinDate() != null) {
            where.append(" AND period.date2 >= ?");
            parameters.add(new java.sql.Date(filter.getMinDate().getTime()));
        }
        if(filter.getMaxDate() != null) {
            where.append(" AND period.date2 <= ?");
            parameters.add(new java.sql.Date(filter.getMaxDate().getTime()));
        }

        for(DimensionType type : filter.getRestrictedDimensions()) {
            if(type == DimensionType.Indicator) {
                appendIdCriteria(where, "Indicator.IndicatorId", filter.getRestrictions(type), parameters);
            } else if(type == DimensionType.Activity) {
                appendIdCriteria(where, "Site.ActivityId", filter.getRestrictions(type), parameters);
            } else if(type == DimensionType.Database) {
                appendIdCriteria(where, "Activity.DatabaseId", filter.getRestrictions(type), parameters);
            } else if(type == DimensionType.Partner) {
                appendIdCriteria(where, "Site.PartnerId", filter.getRestrictions(type), parameters);
            } else if(type == DimensionType.AdminLevel) {
                where.append(" AND Site.LocationId IN " +
                        "(SELECT Link.LocationId FROM LocationAdminLink Link WHERE 1=1 ");

                appendIdCriteria(where, "Link.AdminEntityId", filter.getRestrictions(type), parameters);
                where.append(") ");
            }
        }

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(columns).append(" FROM ").append(from)
                .append(" WHERE ").append(where).append(" GROUP BY ").append(groupBy);


        Session session = ((HibernateEntityManager)em).getSession();

        System.out.println(sql.toString());


        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                PreparedStatement stmt = connection.prepareStatement(sql.toString());

                for(int i=0; i!=parameters.size(); ++i) {
                    stmt.setObject(i+1, parameters.get(i));
                }

                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {

                    Bucket bucket = new Bucket();

                    for(Bundler bundler : bundlers) {
                        bundler.bundle(rs, bucket);
                    }

                    buckets.add(bucket);
                }

            }
        });
    }

    private void appendIdCriteria(StringBuilder sb, String fieldName, Set<Integer> ids, List<Object> parameters) {

        sb.append(" AND ").append(fieldName);

        if(ids.size()==1) {
            sb.append(" = ?");
        } else {
            sb.append(" IN (? ");
            for(int i=1; i!=ids.size(); ++i) {
                sb.append(", ?");
            }
            sb.append(")");
        }

        for(Integer id : ids) {
            parameters.add(id);
        }
    }

    @Override
    public List<String> getFilterLabels(DimensionType type, Collection<Integer> ids) {

        return new ArrayList<String>();

    }
}
