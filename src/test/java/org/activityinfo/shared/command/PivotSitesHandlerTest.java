/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.util.date.DateUtilCalendarImpl;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.QuarterCategory;
import org.activityinfo.shared.report.content.WeekCategory;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PivotSitesHandlerTest extends CommandTestCase2 {

	
    private Set<Dimension> dimensions;
    private Dimension indicatorDim;
    private Filter filter;
    private AdminDimension provinceDim;
    private AdminDimension territoireDim;
    private List<Bucket> buckets;
    private final Dimension projectDim = new Dimension(DimensionType.Project);
    private Dimension partnerDim;
    private ValueType valueType = ValueType.INDICATOR;


    private static final int OWNER_USER_ID = 1;
    private static final int NB_BENEFICIARIES_ID = 1;


    @BeforeClass
    public static void setup() {
    	Logger.getLogger("org.activityinfo").setLevel(Level.ALL);
    	Logger.getLogger("org.activityinfo").addHandler(new ConsoleHandler());
    	
    	Logger.getLogger("com.bedatadriven.rebar").setLevel(Level.ALL);

    }

	@Test
	public void testNoIndicator() {
		withIndicatorAsDimension();

		execute();

		assertThat().forIndicator(1).thereIsOneBucketWithValue(15100);
	}

	@Test
	public void testBasic() {
		withIndicatorAsDimension();
		filter.addRestriction(DimensionType.Indicator, 1);

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
	public void testSiteCountOnQuarters() {
		forTotalSiteCounts();
		dimensions.add(new DateDimension(DateUnit.QUARTER));

		execute();

		assertThat().forQuarter(2008, 4).thereIsOneBucketWithValue(1);
		assertThat().forQuarter(2009, 1).thereIsOneBucketWithValue(4);
	}

	@Test
	public void testMonths() {
		forTotalSiteCounts();
		dimensions.add(new DateDimension(DateUnit.MONTH));
		filter.setDateRange(new DateUtilCalendarImpl().yearRange(2009));

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
		assertThat().forPartner(1).thereIsOneBucketWithValue(5100).andItsPartnerLabelIs("NRC");
		assertThat().forPartner(2).thereIsOneBucketWithValue(10000).andItsPartnerLabelIs("Solidarites");
	}

    @Test
	@OnDataSet("/dbunit/sites-simple-target.db.xml")
    public void testTargetPivot() {

        withIndicatorAsDimension();
        dimensions.add(new DateDimension(DateUnit.YEAR));
        dimensions.add(new Dimension(DimensionType.Target));
        filter.addRestriction(DimensionType.Indicator, 1);
        filter.setDateRange(new DateRange(new LocalDate(2008, 1, 1), new LocalDate(2008, 12, 31)));
        execute();

        assertThat().thereAre(2).buckets();
    }

    
	@Test
	public void testAttributePivot() {
		withIndicatorAsDimension();
		withAttributeGroupDim(1);

		filter.addRestriction(DimensionType.Indicator, 1);

		execute();

		assertThat().thereAre(3).buckets();

		assertThat().forAttributeGroupLabeled(1, "Deplacement").thereIsOneBucketWithValue(3600);

		assertThat().forAttributeGroupLabeled(1, "Catastrophe Naturelle").thereIsOneBucketWithValue(10000);
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
		assertThat().forProvince(2).forTerritoire(11).thereIsOneBucketWithValue(3600).with(provinceDim)
				.label("Sud Kivu").with(territoireDim).label("Walungu");
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
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,2,103));
		
		execute();

		assertBucketCount(3);

		assertThat().forIndicator(1).thereIsOneBucketWithValue(5100);
		assertThat().forIndicator(2).thereIsOneBucketWithValue(1700);
		assertThat().forIndicator(103).thereIsOneBucketWithValue(2);
	}

	private void assertBucketCount(int expectedCount) {
		assertEquals(expectedCount, buckets.size());
	}

	@Test
	public void siteCountWithIndicatorFilter() {
		
		//PivotSites [dimensions=[Partner], filter=Indicator={ 275 274 278 277 276 129 4736 119 118 125 124 123 122 121 }, 
		// valueType=TOTAL_SITES]
		withPartnerAsDimension();
		forTotalSiteCounts();
		filter.addRestriction(DimensionType.Indicator, Lists.newArrayList(1,2,3));
		
		execute();
	}
	
	@Test
	public void targetFilter() {
		// Pivoting: PivotSites [dimensions=[Date, Partner, Date, Target, Activity, Indicator], 
		// filter=AdminLevel={ 141801 }, Partner={ 130 },
		// Indicator={ 747 746 745 744 749 748 739 738 743 740 119 118 3661 125 124 123 122 121 }, valueType=INDICATOR]
		
		withPartnerAsDimension();
		dimensions.add(new DateDimension(DateUnit.YEAR));
		dimensions.add(new Dimension(DimensionType.Target));
		dimensions.add(new Dimension(DimensionType.Activity));
		dimensions.add(new Dimension(DimensionType.Indicator));
		
		filter.addRestriction(DimensionType.AdminLevel, 141801);
		filter.addRestriction(DimensionType.Partner, 130);
		filter.addRestriction(DimensionType.Indicator, 1);
		
		execute();
		
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
		filter.addRestriction(DimensionType.Indicator, 1);
		
		execute();
		
		assertEquals(1, buckets.size());
		assertEquals(13600, (int) buckets.get(0).doubleValue());

	}

	@Test
	@OnDataSet("/dbunit/sites-zeros.db.xml")
	public void testZerosExcluded() {

		withIndicatorAsDimension();
		filter.addRestriction(DimensionType.Indicator, 5);
		
		execute();

		assertEquals(1, buckets.size());
		assertEquals(0, (int) buckets.get(0).doubleValue());
		assertEquals(5, ((EntityCategory) buckets.get(0).getCategory(this.indicatorDim)).getId());
	}

	@Test
	@OnDataSet("/dbunit/sites-weeks.db.xml")
	public void testWeeks() {

		final Dimension weekDim = new DateDimension(DateUnit.WEEK_MON);
		dimensions.add(weekDim);

		filter.addRestriction(DimensionType.Indicator, 1);
		
		execute();

		assertEquals(3, buckets.size());
		assertEquals(3600, (int) findBucketByWeek(buckets, 2011, 52).doubleValue());
		assertEquals(1500, (int) findBucketByWeek(buckets, 2012, 1).doubleValue());
		assertEquals(4142, (int) findBucketByWeek(buckets, 2012, 13).doubleValue());

	}

	@Test
	@OnDataSet("/dbunit/sites-quarters.db.xml")
	public void testQuarters() {

		final Dimension quarterDim = new DateDimension(DateUnit.QUARTER);
		dimensions.add(quarterDim);

		filter.addRestriction(DimensionType.Indicator, 1);
		
		execute();

		assertEquals(3, buckets.size());
		assertEquals(1500, (int) findBucketByQuarter(buckets, 2009, 1).doubleValue());
		assertEquals(3600, (int) findBucketByQuarter(buckets, 2009, 2).doubleValue());
		assertEquals(10000, (int) findBucketByQuarter(buckets, 2008, 4).doubleValue());
	}

	@Test
	@OnDataSet("/dbunit/sites-linked.db.xml")
	public void testLinked() {
		withIndicatorAsDimension();
		filter.addRestriction(DimensionType.Indicator, 1);
		execute();
		assertThat().forIndicator(1).thereIsOneBucketWithValue(1900);
	}

	@Test
	@OnDataSet("/dbunit/sites-linked.db.xml")
	public void testLinkedValuesAreDuplicated() {
		withIndicatorAsDimension();
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,3));
		execute();
		assertThat().forIndicator(1).thereIsOneBucketWithValue(1900);
		assertThat().forIndicator(3).thereIsOneBucketWithValue(1500);
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
    
    private Bucket findBucketByWeek(List<Bucket> buckets, int year, int week) {
        for(Bucket bucket : buckets) {
            WeekCategory category = (WeekCategory) bucket.getCategory(new DateDimension(DateUnit.WEEK_MON));
	  	    if(category != null && category.getYear() == year && category.getWeek() == week) {
	            return bucket;
	        }
        }
        throw new AssertionError("No bucket for " + year + " W " + week);
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

        public AssertionBuilder forQuarter(int year, int quarter) {
        	criteria.append(" in quarter ").append(year)
        			.append("Q").append(quarter).append(" ");
			filter(new DateDimension(DateUnit.QUARTER), year + "Q" + quarter);
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
