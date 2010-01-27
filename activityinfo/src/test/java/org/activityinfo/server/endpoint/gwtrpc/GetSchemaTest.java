package org.activityinfo.server.endpoint.gwtrpc;


import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AttributeModel;
import org.activityinfo.shared.dto.IndicatorModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class GetSchemaTest extends CommandTestCase {

    @BeforeClass
    public static void runBeforeClass() {

        populate("schema1");
    }


    @Test
    public void testDatabaseVisibilityForOwners() throws CommandException {

        // owners should be able to see their databases

        setUser(1); // Alex

        Schema schema = execute(new GetSchema());

        Assert.assertTrue("ALEX(owner) in PEAR", schema.getDatabaseById(1) != null);     // PEAR
        Assert.assertTrue("ALEX can design", schema.getDatabaseById(1).isDesignAllowed());
        Assert.assertTrue("Alex can edit all", schema.getDatabaseById(1).isEditAllowed());
        Assert.assertTrue("object graph is preserved", schema.getDatabaseById(1).getCountry() == schema.getDatabaseById(2).getCountry());
        Assert.assertTrue("object graph is preserved (database-activity)",
                schema.getDatabaseById(1) ==
                        schema.getDatabaseById(1).getActivities().get(0).getDatabase());
    }

    @Test
    public void testDatabaseVisibilityForView() throws CommandException {


        setUser(2); // Bavon

        Schema schema = execute(new GetSchema());

        Assert.assertTrue("BAVON in PEAR", schema.getDatabaseById(1) != null);

        Assert.assertTrue("bavon can edit", schema.getDatabaseById(1).isEditAllowed());
        Assert.assertFalse("bavon cannot edit all", schema.getDatabaseById(1).isEditAllAllowed());

    }

    @Test
    public void testDatabaseVisibilityNone() throws CommandException {

        setUser(3); // Stefan

        Schema schema = execute(new GetSchema());


        Assert.assertTrue("STEFAN not in PEAR", schema.getDatabaseById(1) == null);

    }

//	@Test 
//	public void testUserVisibility() {
//	
//		// only owners should be able to see the user queries
//		
//		EntityManager em = emf.createEntityManager();
//		

    //		Schema schema = SchemaFactory.build(em, em.find(User.class, 1));
//		Assert.assertTrue(schema.getDatabases().getById(1).getUsers().size() !=0);
//		
//		schema = SchemaFactory.build(em, em.find(User.class, 2));
//
//		Assert.assertTrue(schema.getDatabases().getById(1).getUsers().size() ==0);
//	}
//	
    @Test
    public void testIndicators() throws CommandException {

        setUser(1); // Alex

        Schema schema = execute(new GetSchema());

        Assert.assertTrue("no indicators case",
                schema.getActivityById(2).getIndicators().size() == 0);

        ActivityModel nfi = schema.getActivityById(1);
        IndicatorModel[] indicators = nfi.getIndicators().toArray(new IndicatorModel[0]);

        Assert.assertEquals("indicators are present", 2, indicators.length);
        //Assert.assertTrue("indicators are sorted",
        //		indicators[0].getSortOrder() <= indicators[1].getSortOrder());

        IndicatorModel test = nfi.getIndicatorById(2);
        Assert.assertEquals("property:name", test.getName(), "kits");
        Assert.assertEquals("property:units", test.getUnits(), "menages");
        Assert.assertEquals("property:aggregation", test.getAggregation(), 0);
        Assert.assertEquals("property:collectIntervention", test.getCollectIntervention(), true);
        Assert.assertEquals("property:collectMonitoring", test.getCollectMonitoring(), true);
        Assert.assertEquals("property:category", test.getCategory(), "outputs");
        Assert.assertEquals("property:listHeader", test.getListHeader(), "header");
        Assert.assertEquals("property:description", test.getDescription(), "desc");
    }

    @Test
    public void testAttributes() throws CommandException {

        setUser(1); // Alex

        Schema schema = execute(new GetSchema());

        Assert.assertTrue("no attributes case", schema.getActivityById(2).getAttributeGroups().size() == 0);

        ActivityModel nfi = schema.getActivityById(1);
        AttributeModel[] attributes = nfi.getAttributeGroups().get(0).getAttributes().toArray(new AttributeModel[0]);

        Assert.assertTrue("attributes are present", attributes.length == 2);

        AttributeModel test = nfi.getAttributeById(1);

        Assert.assertEquals("property:name", "Retour", test.getName());
    }


}
