package org.activityinfo.shared.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({
    MockHibernateModule.class,
    BeanMappingModule.class,
    GwtRpcModule.class,
    LoggingModule.class
})
public class LocalGetSchemaHandlerIntTest extends LocalHandlerTestCase {

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void forDatabaseOwner() throws CommandException {

        synchronizeFirstTime();

        SchemaDTO schema = executeLocally(new GetSchema());
        assertThat(schema.getDatabases().size(), equalTo(3));
        assertThat(schema.getDatabaseById(1).isDesignAllowed(), equalTo(true));
        assertThat(schema.getDatabaseById(1).getAmOwner(), equalTo(true));
        assertThat(schema.getDatabaseById(2).getAmOwner(), equalTo(true));
        assertThat(schema.getDatabaseById(1).getOwnerName(), equalTo("Alex"));
    }

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void forUser() throws CommandException {

        setUser(4); // only has view access to databse 1
        synchronizeFirstTime();

        SchemaDTO schema = executeLocally(new GetSchema());

        assertThat(schema.getDatabases().size(), equalTo(2));

        UserDatabaseDTO pearDb = schema.getDatabaseById(1);
        assertThat(pearDb.getAmOwner(), equalTo(false));
        assertThat(pearDb.isViewAllAllowed(), equalTo(false));
        assertThat(pearDb.isEditAllowed(), equalTo(false));
        assertThat(pearDb.isEditAllAllowed(), equalTo(true));

        ActivityDTO activity = schema.getActivityById(1);
        assertThat(activity.getAttributeGroups().size(), equalTo(3));

        AttributeGroupDTO group = activity.getAttributeGroupById(1);
        assertThat(group.getName(), equalTo("cause"));
        assertThat(group.getAttributes().size(), equalTo(2));
    }
}
