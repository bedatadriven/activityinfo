/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.QuarterCategory;
import org.sigmah.shared.report.model.*;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PivotDAOImplTest {


    private PivotDAO dao;
    private Set<Dimension> dimensions;
    private Dimension indicatorDim;
    private Filter filter;
    private AdminDimension provinceDim;
    private AdminDimension territoireDim;
    private List<PivotDAO.Bucket> buckets;
    private Dimension partnerDim;
    private static final int OWNER_USER_ID = 1;

    @Inject
    public PivotDAOImplTest(PivotHibernateDAO dao) {
        this.dao = dao;
    }

    @BeforeClass
    public static void setup() {

    }

    @Test
    public void testBasic() {
        withIndicatorAsDimension();

        execute();

        assertThat().forIndicator(OWNER_USER_ID).thereIsOneBucketWithValue(15100);
    }


    @Test
    public void testIndicatorFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Database, OWNER_USER_ID);
        filter.addRestriction(DimensionType.Activity, OWNER_USER_ID);
        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);
        filter.addRestriction(DimensionType.Partner, 2);

        execute();

        assertThat().thereIsOneBucketWithValue(10000);
    }

    @Test
    public void testAdminFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.AdminLevel, 11);
        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);

        execute();

        assertThat().thereIsOneBucketWithValue(3600);
    }

    @Test
    public void testPartnerPivot() {

        withIndicatorAsDimension();
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);

        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forPartner(OWNER_USER_ID).thereIsOneBucketWithValue(5100).andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(10000).andItsPartnerLabelIs("Solidarites");
    }

/*
    @Test
    public void testAttributePivot() {

        withIndicatorAsDimension();
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);

        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forPartner(OWNER_USER_ID).thereIsOneBucketWithValue(5100).andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(10000).andItsPartnerLabelIs("Solidarites");
    }
*/
    
    
    @Test
    public void testAdminPivot() {

        withIndicatorAsDimension();
        withAdminDimension(provinceDim);
        withAdminDimension(territoireDim);
        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);

        execute();

        assertThat().thereAre(3).buckets();
        assertThat().forProvince(2).thereAre(2).buckets();
        assertThat().forProvince(2).forTerritoire(11).thereIsOneBucketWithValue(3600)
                .with(provinceDim).label("Sud Kivu")
                .with(territoireDim).label("Walungu");
        assertThat().forProvince(4).thereIsOneBucketWithValue(10000);


//        List<PivotDAO.Bucket> matches = findBucketsByCategory(buckets, provinceDim,
//                new EntityCategory(2, null));
//        assertEquals(2, matches.size());
//
//        List<PivotDAO.Bucket> subMatches = findBucketsByCategory(matches, territoireDim,
//                new EntityCategory(11, null));
//        assertEquals(1, subMatches.size());
//        assertEquals(3600, (int)subMatches.get(0).doubleValue());
//        assertEquals("Sud Kivu", ((EntityCategory) subMatches.get(0).getCategory(provinceDim)).getLabel());
//        assertEquals("Walungu", ((EntityCategory) subMatches.get(0).getCategory(territoireDim)).getLabel());
//
//
//        matches = findBucketsByCategory(buckets, provinceDim,
//                       new EntityCategory(4, null));
//       assertEquals(1, matches.size());
//       assertEquals(10000, (int)matches.get(0).doubleValue());
    }


    @Test
    public void testSiteCount() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 103);

        execute();

        int expectedCount = OWNER_USER_ID;
        assertBucketCount(expectedCount);
        assertEquals(3, (int) buckets.get(0).doubleValue());

    }

    private void assertBucketCount(int expectedCount) {
        assertEquals(expectedCount, buckets.size());
    }

    @Test
    public void testIndicatorOrder() {

        withIndicatorAsDimension();

        filter.addRestriction(DimensionType.Indicator, OWNER_USER_ID);
        filter.addRestriction(DimensionType.Indicator, 2);

        List<PivotDAO.Bucket> buckets = dao.aggregate(OWNER_USER_ID, filter, dimensions);

        assertEquals(2, buckets.size());

        PivotDAO.Bucket indicator1 = findBucketsByCategory(buckets, indicatorDim, new EntityCategory(OWNER_USER_ID)).get(0);
        PivotDAO.Bucket indicator2 = findBucketsByCategory(buckets, indicatorDim, new EntityCategory(2)).get(0);

        EntityCategory cat1 = (EntityCategory) indicator1.getCategory(indicatorDim);
        EntityCategory cat2 = (EntityCategory) indicator2.getCategory(indicatorDim);

        assertEquals(2, cat1.getSortOrder().intValue());
        assertEquals(OWNER_USER_ID, cat2.getSortOrder().intValue());

    }

    @Test
    @OnDataSet("/dbunit/sites-deleted.db.xml")
    public void testDeletedNotIncluded() {

        withIndicatorAsDimension();

        List<PivotDAO.Bucket> buckets = dao.aggregate(OWNER_USER_ID, new Filter(), dimensions);

        assertEquals(OWNER_USER_ID, buckets.size());
        assertEquals(13600, (int) buckets.get(0).doubleValue());

    }


    @Test
    @OnDataSet("/dbunit/sites-zeros.db.xml")
    public void testZerosExcluded() {

        withIndicatorAsDimension();

        Filter filter = new Filter();

        List<PivotDAO.Bucket> buckets = dao.aggregate(OWNER_USER_ID, filter, dimensions);

        assertEquals(OWNER_USER_ID, buckets.size());
        assertEquals(0, (int) buckets.get(0).doubleValue());
        assertEquals(5, ((EntityCategory) buckets.get(0).getCategory(this.indicatorDim)).getId());
    }


    @Test
    @OnDataSet("/dbunit/sites-quarters.db.xml")
    public void testQuarters() {

        final Dimension quarterDim = new DateDimension(DateUnit.QUARTER);
        dimensions.add(quarterDim);

        Filter filter = new Filter();

        List<PivotDAO.Bucket> buckets = dao.aggregate(OWNER_USER_ID, filter, dimensions);

        assertEquals(3, buckets.size());
        assertEquals(1500, (int)findBucketByQuarter(buckets, 2009, OWNER_USER_ID).doubleValue());
        assertEquals(3600, (int)findBucketByQuarter(buckets, 2009, 2).doubleValue());
        assertEquals(10000, (int)findBucketByQuarter(buckets, 2008, 4).doubleValue());
    }

    private List<PivotDAO.Bucket> findBucketsByCategory(List<PivotDAO.Bucket> buckets, Dimension dim, DimensionCategory cat) {
        List<PivotDAO.Bucket> matching = new ArrayList<PivotDAO.Bucket>();
        for (PivotDAO.Bucket bucket : buckets) {
            if (bucket.getCategory(dim).equals(cat)) {
                matching.add(bucket);
            }
        }
        return matching;
    }

    private PivotDAO.Bucket findBucketByQuarter(List<PivotDAO.Bucket> buckets, int year, int quarter) {
        for(PivotDAO.Bucket bucket : buckets) {
            QuarterCategory category = (QuarterCategory) bucket.getCategory(new DateDimension(DateUnit.QUARTER));
            if(category.getYear() == year && category.getQuarter() == quarter) {
                return bucket;
            }
        }
        throw new AssertionError("No bucket for " + year + "q" + quarter);
    }


    @Before
    public void setUp() throws Exception {
        dimensions = new HashSet<Dimension>();
        filter = new Filter();

        provinceDim = new AdminDimension(OWNER_USER_ID);
        territoireDim = new AdminDimension(2);
    }

    private void withIndicatorAsDimension() {
        indicatorDim = new Dimension(DimensionType.Indicator);
        dimensions.add(indicatorDim);
    }

    private void withAdminDimension(AdminDimension adminDimension) {
        dimensions.add(adminDimension);
    }

    private void withPartnerAsDimension() {
        partnerDim = new Dimension(DimensionType.Partner);
        dimensions.add(partnerDim);
    }

    private void execute() {
        buckets = dao.aggregate(OWNER_USER_ID, filter, dimensions);

        System.err.println("buckets = [");
        for (PivotDAO.Bucket bucket : buckets) {
            System.err.println("  { value: " + bucket.doubleValue());
            for (Dimension dim : bucket.dimensions()) {
                DimensionCategory cat = bucket.getCategory(dim);
                System.err.print("    " + dim.toString() + ": ");
                if (cat instanceof EntityCategory) {
                    System.err.print(((EntityCategory) cat).getId() + " '" + ((EntityCategory) cat).getLabel() + "'");
                }
                System.err.println("  ]");

            }
        }
    }

    public AssertionBuilder assertThat() {
        return new AssertionBuilder();
    }

    private class AssertionBuilder {
        List<PivotDAO.Bucket> matching = new ArrayList<PivotDAO.Bucket>(buckets);
        StringBuilder criteria = new StringBuilder();

        Object predicate;

        public AssertionBuilder forIndicator(int indicatorId) {
            criteria.append(" with indicator ").append(indicatorId);
            filter(indicatorDim, indicatorId);
            return this;
        }

        public AssertionBuilder forPartner(int partnerId) {
            criteria.append(" with partner ").append(partnerId);
            filter(partnerDim, partnerId);
            return this;
        }

        public AssertionBuilder forProvince(int provinceId) {
            criteria.append(" with province ").append(provinceId);
            filter(provinceDim, provinceId);
            return this;
        }

        public AssertionBuilder forTerritoire(int territoireId) {
            criteria.append(" with territoire ").append(territoireId);
            filter(territoireDim, territoireId);
            return this;
        }

        private void filter(Dimension dim, int id) {
            ListIterator<PivotDAO.Bucket> it = matching.listIterator();
            while (it.hasNext()) {
                PivotDAO.Bucket bucket = it.next();
                DimensionCategory category = bucket.getCategory(dim);
                if (!(category instanceof EntityCategory) ||
                        ((EntityCategory) category).getId() != id) {

                    it.remove();

                }
            }
        }

        private String description(String assertion) {
            String s = assertion + " " + criteria.toString();
            return s.trim();
        }

        public AssertionBuilder thereAre(int predicate) {
            this.predicate = predicate;
            return this;
        }

        public AssertionBuilder with(Dimension predicate) {
            this.predicate = predicate;
            return this;
        }

        public AssertionBuilder buckets() {
            bucketCountIs((Integer) predicate);
            return this;
        }

        public AssertionBuilder label(String label) {
            Dimension dim = (Dimension) predicate;
            assertEquals(description(dim.toString() + " label of only bucket"), label,
                    ((EntityCategory) matching.get(0).getCategory(dim)).getLabel());
            return this;
        }

        public AssertionBuilder bucketCountIs(int expectedCount) {
            assertEquals(description("count of buckets"), expectedCount, matching.size());
            return this;
        }

        public AssertionBuilder thereIsOneBucketWithValue(int expectedValue) {
            bucketCountIs(OWNER_USER_ID);
            assertEquals(description("value of only bucket"), expectedValue, (int) matching.get(0).doubleValue());
            return this;
        }

        public AssertionBuilder andItsPartnerLabelIs(String label) {
            bucketCountIs(OWNER_USER_ID);
            with(partnerDim).label(label);
            return this;
        }
    }
}
