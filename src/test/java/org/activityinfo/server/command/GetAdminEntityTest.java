package org.activityinfo.server.command;

import junit.framework.Assert;

import org.activityinfo.server.command.CommandTestCase;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.Bounds;
import org.activityinfo.shared.exception.CommandException;
import org.junit.BeforeClass;
import org.junit.Test;


public class GetAdminEntityTest extends CommandTestCase {



	@Test
	public void testProvince() throws CommandException {

        populate("adminEntities");

        setUser(1);

		ListResult<AdminEntityModel> result = execute(new GetAdminEntities(1));
		
		Assert.assertTrue("all are present", result.getData().size() == 4);	
		Assert.assertTrue("sorted", result.getData().get(0).getName().equals("Bandundu"));
	
		Bounds bounds = result.getData().get(0).getBounds();
		Assert.assertNotNull("bounds", bounds);
		Assert.assertEquals("x1", 1.0, bounds.x1);
		Assert.assertEquals("y1", 2.0, bounds.y1);
		Assert.assertEquals("x2", 3.0, bounds.x2);
		Assert.assertEquals("y2", 4.0, bounds.y2);
				
	}
	
	@Test
	public void testTerritoire() throws CommandException {

        populate("adminEntities");

        setUser(1);
		
		ListResult<AdminEntityModel> result = execute(new GetAdminEntities(2, 2));
		
		Assert.assertEquals("count", 3, result.getData().size());
		Assert.assertEquals("levelId", 2, result.getData().get(0).getLevelId());
		Assert.assertEquals("parentId", 2, (int)result.getData().get(0).getParentId());

	}

    @Test
    public void testActivity() throws Exception {

        populate("sites-simple1");

        setUser(1);

        ListResult<AdminEntityModel> result = execute(new GetAdminEntities(1, null, 4));

        Assert.assertEquals(1, result.getData().size());


    }


	
	
}
