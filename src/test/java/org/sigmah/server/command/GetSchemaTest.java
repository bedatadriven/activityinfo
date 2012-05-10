/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.test.InjectionSupport;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetSchemaTest extends CommandTestCase2 {

	@Before
	public void cleanUpScheduler() {
		JdbcScheduler.get().forceCleanup();
	}
	
    @Test
    public void testDatabaseVisibilityForOwners() throws CommandException {

        // owners should be able to see their databases

        setUser(1); // Alex

        SchemaDTO schema = execute(new GetSchema());

        assertThat("database count", schema.getDatabases().size(), equalTo(3));
        assertThat("database list is sorted", schema.getDatabases().get(0).getName(), equalTo("Alpha"));
        
        assertTrue("ALEX(owner) in PEAR", schema.getDatabaseById(1) != null);     // PEAR
        assertTrue("ALEX can design", schema.getDatabaseById(1).isDesignAllowed());
        assertTrue("Alex can edit all", schema.getDatabaseById(1).isEditAllowed());
        assertTrue("object graph is preserved", schema.getDatabaseById(1).getCountry() == schema.getDatabaseById(2).getCountry());
        assertTrue("object graph is preserved (database-activity)",
                schema.getDatabaseById(1) ==
                        schema.getDatabaseById(1).getActivities().get(0).getDatabase());
        AdminLevelDTO adminLevel = schema.getCountries().get(0).getAdminLevels().get(0);
		assertThat("CountryId is not null", adminLevel.getCountryId(), not(equalTo(0)));
		assertThat("CountryId is not null", adminLevel.getId(), not(equalTo(0)));

        assertTrue("CountryId is not null", schema.getCountries().get(0).getAdminLevels().get(0).getCountryId()!=0);
        
        assertThat("deleted attribute is not present", schema.getActivityById(1).getAttributeGroups().size(), equalTo(3));
    }

    @Test @OnDataSet("/dbunit/sites-public.db.xml")
    public void testDatabasePublished() throws CommandException {

    	// Anonymouse user should fetch schema database with pulished activities.
        setUser(0); 

        SchemaDTO schema = execute(new GetSchema());

        assertThat(schema.getDatabases().size(), equalTo(1));
    }
    
    @Test
    public void testDatabaseVisibilityForView() throws CommandException {


        setUser(2); // Bavon

        SchemaDTO schema = execute(new GetSchema());

        assertThat(schema.getDatabases().size(), equalTo(1));
        assertThat("BAVON in PEAR", schema.getDatabaseById(1), is(not(nullValue())));
        assertThat(schema.getDatabaseById(1).getMyPartnerId(), equalTo(1));
        assertThat(schema.getDatabaseById(1).isEditAllowed(), equalTo(true));
        assertThat(schema.getDatabaseById(1).isEditAllAllowed(), equalTo(false));
    }

    @Test
    public void testDatabaseVisibilityNone() throws CommandException {
        setUser(3); // Stefan

        SchemaDTO schema = execute(new GetSchema());

        assertTrue("STEFAN does not have access to RRM", schema.getDatabaseById(2) == null);
    }

    @Test
    public void testIndicators() throws CommandException {

        setUser(1); // Alex

        SchemaDTO schema = execute(new GetSchema());

        assertTrue("no indicators case",
                schema.getActivityById(2).getIndicators().size() == 0);

        ActivityDTO nfi = schema.getActivityById(1);

        assertThat("indicators are present", nfi.getIndicators().size(), equalTo(4));

        IndicatorDTO test = nfi.getIndicatorById(2);
        assertThat("property:name", test.getName(), equalTo("baches"));
        assertThat("property:units", test.getUnits(), equalTo("menages"));
        assertThat("property:aggregation", test.getAggregation(), equalTo(IndicatorDTO.AGGREGATE_SUM));
        assertThat("property:category", test.getCategory(), equalTo("outputs"));
        assertThat("property:listHeader", test.getListHeader(), equalTo("header"));
        assertThat("property:description", test.getDescription(), equalTo("desc"));
    }

    @Test
    public void testAttributes() throws CommandException {

        setUser(1); // Alex

        SchemaDTO schema = execute(new GetSchema());

        assertTrue("no attributes case", schema.getActivityById(3).getAttributeGroups().size() == 0);

        ActivityDTO nfi = schema.getActivityById(1);
        AttributeDTO[] attributes = nfi.getAttributeGroups().get(0).getAttributes().toArray(new AttributeDTO[0]);

        assertTrue("attributes are present", attributes.length == 2);

        AttributeDTO test = nfi.getAttributeById(1);

        assertEquals("property:name", "Catastrophe Naturelle", test.getName());
    }
}
