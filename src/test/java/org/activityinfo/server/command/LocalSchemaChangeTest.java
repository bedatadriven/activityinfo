package org.activityinfo.server.command;

import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.dao.ActivityDAO;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.CreateReport;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.LocalHandlerTestCase;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;


@RunWith(InjectionSupport.class)
@Modules({
        MockHibernateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class LocalSchemaChangeTest extends LocalHandlerTestCase {

	
	@Test
	public void updateIndicator() {

		synchronizeFirstTime();
		
		Map<String, Object> changes = Maps.newHashMap();
		changes.put("name", "New Name");
		
		UpdateEntity update = new UpdateEntity("Indicator", 5, changes);
		
		executeRemotely(update);
		
		synchronize();
		
		SchemaDTO schema = executeLocally(new GetSchema());
		
		assertThat(schema.getIndicatorById(5).getName(), equalTo("New Name"));		
	}

	@Test
	public void createActivity() {

		synchronizeFirstTime();
		
		SchemaDTO schema = executeLocally(new GetSchema());
		
		ActivityDTO activity = new ActivityDTO();
		activity.setName("New Activity");
		activity.setReportingFrequency(0);
		activity.setLocationTypeId(1);
		
		
		CreateResult createResult = executeRemotely(CreateEntity.Activity(schema.getDatabaseById(1), activity));
		
		synchronize();
		
		schema = executeLocally(new GetSchema());
		
		ActivityDTO createdActivity = schema.getActivityById(createResult.getNewId());
		
		assertThat(createdActivity, is(not(nullValue())));
		assertThat(createdActivity.getName(), equalTo(activity.getName()));
	}

	@Test
	public void createIndicator() {

		synchronizeFirstTime();
		
		SchemaDTO schema = executeLocally(new GetSchema());
		
		Map<String, Object> indicator = Maps.newHashMap();
		indicator.put("name", "New Indicator");
		indicator.put("units", "bricks");
		indicator.put("activityId", 2);
		
		CreateResult createResult = executeRemotely(new CreateEntity("Indicator", indicator));
		
		synchronize();
		
		schema = executeLocally(new GetSchema());
		
		IndicatorDTO createdIndicator = schema.getIndicatorById(createResult.getNewId());
		
		assertThat(createdIndicator, is(not(nullValue())));
		assertThat(createdIndicator.getName(), equalTo("New Indicator"));
	}
	


	@Test
	public void deleteActivity() {

		synchronizeFirstTime();
		
		SchemaDTO schema = executeLocally(new GetSchema());
		assertThat(schema.getActivityById(2), is(not(nullValue())));
		
		executeRemotely(new Delete("Activity", 2));
		
		synchronize();
		
		schema = executeLocally(new GetSchema());
		
		assertThat(schema.getActivityById(2), is(nullValue()));
	}

	
	@Test
	public void updateAttribute() {

		Map<String, Object> changes = Maps.newHashMap();
		changes.put("name", "New Name");
	}
	
}
