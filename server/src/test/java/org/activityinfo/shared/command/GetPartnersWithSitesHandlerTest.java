package org.activityinfo.shared.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetPartnersWithSitesHandlerTest extends CommandTestCase2 {

	@Test
	public void test() throws CommandException {
		
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 1);
		
		PartnerResult result = execute(new GetPartnersWithSites(filter));
		
		assertThat(result.getData().size(), equalTo(2));
		
	}
	
}
