package org.activityinfo.shared.command;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.shared.command.GetIndicatorLinks;
import org.activityinfo.shared.command.result.IndicatorLinkResult;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-linked.db.xml")
public class IndicatorLinksTest extends CommandTestCase2 {

	
	@Test
	public void get() {
		
		IndicatorLinkResult result = execute(new GetIndicatorLinks());
		
		
	}
}
