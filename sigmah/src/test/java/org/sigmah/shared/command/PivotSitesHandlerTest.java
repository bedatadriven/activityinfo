/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.util.date.DateUtilCalendarImpl;
import org.sigmah.shared.command.PivotSites.ValueType;
import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.QuarterCategory;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.AttributeGroupDimension;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PivotSitesHandlerTest extends CommandTestCase2 {


	private SqlDatabase db;
    private Set<Dimension> dimensions;
    private Dimension indicatorDim;
    private Filter filter;
    private AdminDimension provinceDim;
    private AdminDimension territoireDim;
    private List<Bucket> buckets;
    private Dimension projectDim = new Dimension(DimensionType.Project);
    private Dimension partnerDim;
    private ValueType valueType = ValueType.INDICATOR;


    private static final int OWNER_USER_ID = 1;
    private static final int NB_BENEFICIARIES_ID = 1;

    @Inject
    public PivotSitesHandlerTest(SqlDatabase db) {  
    	this.db = db;
    }

    @BeforeClass
    public static void setup() {

    }

    @Test
    public void testBasic() {
        withIndicatorAsDimension();

        execute();

        assertThat().forIndicator(1).thereIsOneBucketWithValue(15100);
    }
    

    @Test
    public void testTotalSiteCount() {
        forTotalSiteCounts();

        execute();

        assertThat().thereIsOneBucketWithValue(8);
    }

    @Test
    public void testYears() {
    	forTotalSiteCounts();
    	dimensions.add(new DateDimension(DateUnit.YEAR));
    	
    	execute();
    	
    	assertThat().forYear(2008).thereIsOneBucketWithValue(1);
    	assertThat().forYear(2009).thereIsOneBucketWithValue(4);
    }
    
    @Test
    public void testMonths() {
    	forTotalSiteCounts();
    	dimensions.add(new DateDimension(DateUnit.MONTH));
    	filter.setDateRange( new DateUtilCalendarImpl().yearRange(2009));
        	
    	execute();
    	
    	assertThat().thereAre(3).buckets();
    }


    @Test
    public void testIndicatorFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Database, 1);
        filter.addRestriction(DimensionType.Activity, 1);
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Partner, 2);

        execute();

        assertThat().thereIsOneBucketWithValue(10000);
    }

    @Test
    public void testAdminFilter() {
        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.AdminLevel, 11);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereIsOneBucketWithValue(3600);
    }

    @Test
    public void testPartnerPivot() {

        withIndicatorAsDimension();
        withPartnerAsDimension();
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(2).buckets();
        assertThat().forPartner(OWNER_USER_ID).thereIsOneBucketWithValue(5100).andItsPartnerLabelIs("NRC");
        assertThat().forPartner(2).thereIsOneBucketWithValue(10000).andItsPartnerLabelIs("Solidarites");
    }

    @Test
    public void testAttributePivot() {
        withIndicatorAsDimension();
        withAttributeGroupDim(1);

        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(3).buckets();

        assertThat().forAttributeGroupLabeled(1, "Deplacement")
                        .thereIsOneBucketWithValue(3600);

        assertThat().forAttributeGroupLabeled(1, "Catastrophe Naturelle")
                        .thereIsOneBucketWithValue(10000);
    }

    
    @Test
    public void testAdminPivot() {
        withIndicatorAsDimension();
        withAdminDimension(provinceDim);
        withAdminDimension(territoireDim);
        filter.addRestriction(DimensionType.Indicator, 1);

        execute();

        assertThat().thereAre(3).buckets();
        assertThat().forProvince(2).thereAre(2).buckets();
        assertThat().forProvince(2).forTerritoire(11).thereIsOneBucketWithValue(3600)
                .with(provinceDim).label("Sud Kivu")
                .with(territoireDim).label("Walungu");
        assertThat().forProvince(4).thereIsOneBucketWithValue(10000);
    }


    @Test
    public void testSiteCount() {

        withIndicatorAsDimension();
        filter.addRestriction(DimensionType.Indicator, 103);

        execute();

        int expectedCount = 1;
        assertBucketCount(expectedCount);
        assertEquals(3, (int) buckets.get(0).doubleValue());

    }
    
    
    @Test
    public void projects() {
    	
    	withIndicatorAsDimension();
    	withProjectAsDimension();
    	filter.addRestriction(DimensionType.Database, 1);
    	filter.addRestriction(DimensionType.Indicator, 1);
    	
    	execute();
    	
    	assertBucketCount(2);
    	assertThat().forProject(1).thereIsOneBucketWithValue(5100);
    	assertThat().forProject(2).thereIsOneBucketWithValue(10000);
  
    }
    
    @Test
    public void projectFilters() {
    	
    	withIndicatorAsDimension();
    	withProjectAsDimension();
    	
    	filter.addRestriction(DimensionType.Database, 1);
    	filter.addRestriction(DimensionType.Project, 1);
    	
    	execute();
    	
    	assertBucketCount(4);
    	
    	assertThat().forIndicator(1).thereIsOneBucketWithValue(5100);
    	assertThat().forIndicator(2).thereIsOneBucketWithValue(1700);
    	assertThat().forIndicator(103).thereIsOneBucketWithValue(2);
    }
    

    private void assertBucketCount(int expectedCount) {
        assertEquals(expectedCount, buckets.size());
    }

    @Test
    public void testIndicatorOrder() {

        withIndicatorAsDimension();

        filter.addRestriction(DimensionType.Indicator, 1);
        filter.addRestriction(DimensionType.Indicator, 2);

        execute();
       

        assertEquals(2, buckets.size());

        Bucket indicator1 = findBucketsByCategory(buckets, indicatorDim, new EntityCategory(1)).get(0);
        Bucket indicator2 = findBucketsByCategory(buckets, indicatorDim, new EntityCategory(2)).get(0);

        EntityCategory cat1 = (EntityCategory) indicator1.getCategory(indicatorDim);
        EntityCategory cat2 = (EntityCategory) indicator2.getCategory(indicatorDim);

        assertEquals(2, cat1.getSortOrder().intValue());
        assertEquals(OWNER_USER_ID, cat2.getSortOrder().intValue());

    }

    @Test
    @OnDataSet("/dbunit/sites-deleted.db.xml")
    public void testDeletedNotIncluded() {

        withIndicatorAsDimension();

        execute();
        
        assertEquals(1, buckets.size());
        assertEquals(13600, (int) buckets.get(0).doubleValue());

    }


    @Test
    @OnDataSet("/dbunit/sites-zeros.db.xml")
    public void testZerosExcluded() {

        withIndicatorAsDimension();

        execute();
        
        assertEquals(1, buckets.size());
        assertEquals(0, (int) buckets.get(0).doubleValue());
        assertEquals(5, ((EntityCategory) buckets.get(0).getCategory(this.indicatorDim)).getId());
    }


    @Test
    @OnDataSet("/dbunit/sites-quarters.db.xml")
    public void testQuarters() {

        final Dimension quarterDim = new DateDimension(DateUnit.QUARTER);
        dimensions.add(quarterDim);

        Filter filter = new Filter();

        execute();
        
        assertEquals(3, buckets.size());
        assertEquals(1500, (int)findBucketByQuarter(buckets, 2009, 1).doubleValue());
        assertEquals(3600, (int)findBucketByQuarter(buckets, 2009, 2).doubleValue());
        assertEquals(10000, (int)findBucketByQuarter(buckets, 2008, 4).doubleValue());
    }

    private List<Bucket> findBucketsByCategory(List<Bucket> buckets, Dimension dim, DimensionCategory cat) {
        List<Bucket> matching = new ArrayList<Bucket>();
        for (Bucket bucket : buckets) {
            if (bucket.getCategory(dim).equals(cat)) {
                matching.add(bucket);
            }
        }
        return matching;
    }

    private Bucket findBucketByQuarter(List<Bucket> buckets, int year, int quarter) {
        for(Bucket bucket : buckets) {
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
    
    private void forTotalSiteCounts() {
    	valueType = valueType.TOTAL_SITES;
    }

    private void withIndicatorAsDimension() {
        indicatorDim = new Dimension(DimensionType.Indicator);
        dimensions.add(indicatorDim);
    }
    
    private void withProjectAsDimension() {
    	dimensions.add(projectDim);
    }

    private void withAdminDimension(AdminDimension adminDimension) {
        dimensions.add(adminDimension);
    }

    private void withPartnerAsDimension() {
        partnerDim = new Dimension(DimensionType.Partner);
        dimensions.add(partnerDim);
    }

    private void withAttributeGroupDim(int groupId) {
        dimensions.add(new AttributeGroupDimension(groupId));
    }

    private void execute() {
    	
    	setUser(OWNER_USER_ID);
    	try {
			PivotSites pivot = new PivotSites(dimensions, filter);
			pivot.setValueType(valueType);
			buckets = execute(pivot).getBuckets();
		} catch (CommandException e) {
			throw new RuntimeException(e);
		}
  
        System.err.println("buckets = [");
        for (Bucket bucket : buckets) {
            System.err.println("  { value: " + bucket.doubleValue());
            for (Dimension dim : bucket.dimensions()) {
                DimensionCategory cat = bucket.getCategory(dim);
                System.err.print("    " + dim.toString() + ": ");
                System.err.print(cat.toString());
                System.err.println("  ]");

            }
        }
    }

    public AssertionBuilder assertThat() {
        return new AssertionBuilder();
    }

    private class AssertionBuilder {
        List<Bucket> matching = new ArrayList<Bucket>(buckets);
        StringBuilder criteria = new StringBuilder();

        Object predicate;

        public AssertionBuilder forIndicator(int indicatorId) {
            criteria.append(" with indicator ").append(indicatorId);
            filter(indicatorDim, indicatorId);
            return this;
        }

        public AssertionBuilder forYear(int year) {
        	criteria.append(" in year ").append(year);
        	filter(new DateDimension(DateUnit.YEAR), Integer.toString(year));
        	return this;
		}

		public AssertionBuilder forProject(int projectId) {
			criteria.append(" with project ").append(projectId);
			filter(projectDim, projectId);
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

        public AssertionBuilder forAttributeGroupLabeled(int groupId, String label) {
            criteria.append(" with a dimension labeled '").append(label).append("'");
            filter(new AttributeGroupDimension(groupId), label);
            return this;
        }

        private void filter(Dimension dim, String label) {
            ListIterator<Bucket> it = matching.listIterator();
            while (it.hasNext()) {
                Bucket bucket = it.next();
                DimensionCategory category = bucket.getCategory(dim);
                if (category == null || !category.getLabel().equals(label)) {
                    it.remove();
                }
            }
        }


        private void filter(Dimension dim, int id) {
            ListIterator<Bucket> it = matching.listIterator();
            while (it.hasNext()) {
                Bucket bucket = it.next();
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
