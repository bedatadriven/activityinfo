package org.activityinfo.server.dao;

import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.QuarterCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotDAOTest extends DbUnitTestCase {

    @BeforeClass
    public static void setup() {

    }

    @Test
    public void testBasic() {

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        List<PivotDAO.Bucket> buckets = dao.aggregate(new Filter(), dimensions );

        List<PivotDAO.Bucket> matching = findBucketsByCategory(buckets,
                new Dimension(DimensionType.Indicator),
                new EntityCategory(1));

        Assert.assertEquals(1, matching.size());
        Assert.assertEquals(15100.0, matching.get(0).doubleValue());

    }

    @Test
    public void testIndicatorFilter() {


        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        Filter filter = new Filter();

        filter.addRestriction(DimensionType.Database, 1);
        filter.addRestriction(DimensionType.Activity,  1);
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Partner, 2);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );


        Assert.assertEquals(1, buckets.size());
        Assert.assertEquals(10000.0, buckets.get(0).doubleValue());
    }

    @Test
    public void testIndicatorsSorted() {

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        Dimension indicatorDim = new Dimension(DimensionType.Indicator);
        dimensions.add(indicatorDim);

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity,  1);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );

        for(PivotDAO.Bucket bucket : buckets) {
            EntityCategory cat = (EntityCategory) bucket.getCategory(indicatorDim);    
            Assert.assertNotNull(cat.getSortOrder());
        }
    }

    @Test
    public void testAdminFilter() {

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        Filter filter = new Filter();

        filter.addRestriction(DimensionType.AdminLevel, 11);
        filter.addRestriction(DimensionType.Indicator, 1);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );

        Assert.assertEquals(1, buckets.size());
        Assert.assertEquals(3600.0, buckets.get(0).doubleValue());

    }

    @Test
    public void testPartnerPivot() {

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        Dimension partnerDim = new Dimension(DimensionType.Partner);
        dimensions.add(partnerDim);

        Filter filter = new Filter();

        filter.addRestriction(DimensionType.Indicator, 1);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );


        Assert.assertEquals(2, buckets.size());

        List<PivotDAO.Bucket> matches = findBucketsByCategory(buckets, partnerDim,
                new EntityCategory(1, null));
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(5100.0, matches.get(0).doubleValue());
        Assert.assertEquals("NRC", ((EntityCategory)matches.get(0).getCategory(partnerDim)).getLabel());

        matches = findBucketsByCategory(buckets, partnerDim,
                       new EntityCategory(2, null));
        Assert.assertEquals(1, matches.size());
        Assert.assertEquals(10000.0, matches.get(0).doubleValue());
        Assert.assertEquals("Solidarites", ((EntityCategory)matches.get(0).getCategory(partnerDim)).getLabel());


    }

    @Test
    public void testAdminPivot() {

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));
        AdminDimension provinceDim = new AdminDimension(1);
        dimensions.add(provinceDim);
        AdminDimension territoireDim = new AdminDimension(2);
        dimensions.add(territoireDim);

        Filter filter = new Filter();

        filter.addRestriction(DimensionType.Indicator, 1);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );

        Assert.assertEquals(3, buckets.size());

        List<PivotDAO.Bucket> matches = findBucketsByCategory(buckets, provinceDim,
                new EntityCategory(2, null));
        Assert.assertEquals(2, matches.size());

        List<PivotDAO.Bucket> subMatches = findBucketsByCategory(matches, territoireDim,
                new EntityCategory(11, null));
        Assert.assertEquals(1, subMatches.size());
        Assert.assertEquals(3600.0, subMatches.get(0).doubleValue());
        Assert.assertEquals("Sud Kivu", ((EntityCategory) subMatches.get(0).getCategory(provinceDim)).getLabel());   
        Assert.assertEquals("Walungu", ((EntityCategory) subMatches.get(0).getCategory(territoireDim)).getLabel());


        matches = findBucketsByCategory(buckets, provinceDim,
                       new EntityCategory(4, null));
       Assert.assertEquals(1, matches.size());
       Assert.assertEquals(10000.0, matches.get(0).doubleValue());
    }

    @Test
    public void testSiteCount() {

        populate("sites-simple1");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Indicator, 103);

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions );


        Assert.assertEquals(1, buckets.size());
        Assert.assertEquals(3.0, buckets.get(0).doubleValue());
        
    }

    private List<PivotDAO.Bucket> findBucketsByCategory(List<PivotDAO.Bucket> buckets, Dimension dim, DimensionCategory cat)  {


        List<PivotDAO.Bucket> matching = new ArrayList<PivotDAO.Bucket>();
        for(PivotDAO.Bucket bucket : buckets) {
            if(bucket.getCategory(dim).equals(cat)) {
                matching.add(bucket);
            }
        }
        return matching;

    }


    @Test
    public void testDeletedNotIncluded() {

        populate("sites-deleted");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        dimensions.add(new Dimension(DimensionType.Indicator));

        List<PivotDAO.Bucket> buckets = dao.aggregate(new Filter(), dimensions);

        Assert.assertEquals(1, buckets.size());
        Assert.assertEquals(13600.0, buckets.get(0).doubleValue());

    }


    @Test
    public void testZerosExcluded() {

        populate("sites-zeros");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        Dimension indicatorDim = new Dimension(DimensionType.Indicator);
        dimensions.add(indicatorDim);

        Filter filter = new Filter();

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions);

        Assert.assertEquals(1, buckets.size());
        Assert.assertEquals(0.0, buckets.get(0).doubleValue());
        Assert.assertEquals(5, ((EntityCategory) buckets.get(0).getCategory(indicatorDim)).getId());
    }


    @Test
    public void testQuarters() {

        populate("sites-quarters");

        EntityManager em = emf.createEntityManager();
        PivotDAO dao = new PivotDAOHibernateJdbc(em);

        Set<Dimension> dimensions = new HashSet<Dimension>();
        Dimension quarterDim = new DateDimension(DateUnit.QUARTER);
        dimensions.add(quarterDim);

        Filter filter = new Filter();

        List<PivotDAO.Bucket> buckets = dao.aggregate(filter, dimensions);

        Assert.assertEquals(3, buckets.size());
        Assert.assertEquals(10000.0, buckets.get(0).doubleValue());
        Assert.assertEquals(1500.0, buckets.get(1).doubleValue());
        Assert.assertEquals(3600.0, buckets.get(2).doubleValue());

        Assert.assertEquals(2008, ((QuarterCategory) buckets.get(0).getCategory(quarterDim)).getYear());
        Assert.assertEquals(4, ((QuarterCategory) buckets.get(0).getCategory(quarterDim)).getQuarter());

        Assert.assertEquals(2009, ((QuarterCategory) buckets.get(1).getCategory(quarterDim)).getYear());
        Assert.assertEquals(1, ((QuarterCategory) buckets.get(1).getCategory(quarterDim)).getQuarter());

        Assert.assertEquals(2009, ((QuarterCategory) buckets.get(2).getCategory(quarterDim)).getYear());
        Assert.assertEquals(2, ((QuarterCategory) buckets.get(2).getCategory(quarterDim)).getQuarter());
    }

}
