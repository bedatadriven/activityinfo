/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.util.Collector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.beanMapping.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

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

        assertThat(schema.getDatabases().size(), equalTo(1));

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
