package org.activityinfo.server.command;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.AddTarget;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetTargets;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.EntityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.TargetDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class TargetTest extends CommandTestCase {

	private static final int DATABASE_OWNER = 1;
	private static UserDatabaseDTO db;
	
	@Before
	public void setUser() {
		 setUser(DATABASE_OWNER);
		 	/*
			 * Initial data load
			 */

			SchemaDTO schema = execute(new GetSchema());
			db = schema.getDatabaseById(1);
	}

	@Test
	public void testTarget() throws CommandException {

		

		/*
		 * Create a new Target
		 */

		TargetDTO target = createTarget();

		CreateResult cresult = execute(new AddTarget(db.getId(), target));

		int newId = cresult.getNewId();

		/*
		 * Load Targets to verify the changes have stuck
		 */

		List<TargetDTO> targets = execute(new GetTargets(db.getId())).getData();

		TargetDTO dto = getTargetById(targets, newId);

		Assert.assertNotNull(dto);
		Assert.assertEquals("name", "Target0071", dto.getName());
	}

	@Test
	public void updateTargetNameTest() throws Throwable {

		Map<String, Object> changes1 = new HashMap<String, Object>();
		changes1.put("name", "newNameOfTarget");

		execute(new BatchCommand(new UpdateEntity("Target", 1, changes1)));

		List<TargetDTO> targets = execute(new GetTargets(db.getId())).getData();

		TargetDTO dto = getTargetById(targets, 1);

		Assert.assertEquals("newNameOfTarget", dto.getName());
	}

	@Test
	public void deleteTargetTest() {

		TargetDTO target = createTarget();

		CreateResult cresult = execute(new AddTarget(db.getId(), target));

		int newId = cresult.getNewId();

		/*
		 * Load Targets to verify the changes have stuck
		 */

		List<TargetDTO> targets = execute(new GetTargets(db.getId())).getData();

		TargetDTO dto = getTargetById(targets, newId);

		Assert.assertEquals("name", "Target0071", dto.getName());

		/*
		 * Delete new target now
		 */

		VoidResult result = execute(new Delete((EntityDTO) dto));

		/*
		 * Verify if target is deleted.
		 */

		targets = execute(new GetTargets()).getData();

		TargetDTO deleted = getTargetById(targets, newId);

		Assert.assertNull(deleted);

	}

	private TargetDTO createTarget() {
		Date date1 = new Date();
		Date date2 = new Date();
		/*
		 * Create a new Target
		 */

		TargetDTO target = new TargetDTO();
		target.setName("Target0071");
		target.setDate1(date1);
		target.setDate2(date2);

		return target;
	}

	private TargetDTO getTargetById(List<TargetDTO> targets, int id) {
		for (TargetDTO dto : targets) {
			if (id == dto.getId()) {
				return dto;
			}
		}

		return null;
	}
}
