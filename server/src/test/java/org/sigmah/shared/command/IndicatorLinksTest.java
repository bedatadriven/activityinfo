package org.sigmah.shared.command;

import org.activityinfo.shared.command.GetIndicatorLinks;
import org.activityinfo.shared.command.result.IndicatorLinkResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-linked.db.xml")
public class IndicatorLinksTest extends CommandTestCase2 {

	
	@Test
	public void get() {
		
		IndicatorLinkResult result = execute(new GetIndicatorLinks());
		
		
	}
}
