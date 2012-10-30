package org.activityinfo.server.command.handler;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetAdminLevels;
import org.activityinfo.shared.command.result.AdminLevelResult;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetAdminLevelsHandlerTest extends CommandTestCase2 {

	@Test
	public void test() {
		
		GetAdminLevels query = new GetAdminLevels();
		query.setIndicatorIds(Sets.newHashSet(1));
		
		AdminLevelResult result = execute(query);
		
		System.out.println(result.getData());
	}
	
}
