package org.sigmah.shared.command.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.endpoint.gwtrpc.CommandTestCase2;
import org.sigmah.shared.command.GetPartnersWithSites;
import org.sigmah.shared.command.result.PartnerResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;


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
