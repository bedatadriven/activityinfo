/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.Collector;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
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

        synchronize();

        GetSchemaHandler handler = new GetSchemaHandler(localDatabase);
        Collector<SchemaDTO> collector = new Collector<SchemaDTO>();
        handler.execute(new GetSchema(), commandContext, collector);

        SchemaDTO schema = collector.getResult();
        assertThat(schema.getDatabases().size(), equalTo(3));
        assertThat(schema.getDatabaseById(1).isDesignAllowed(), equalTo(true));
        assertThat(schema.getDatabaseById(1).getAmOwner(), equalTo(true));
        assertThat(schema.getDatabaseById(2).getAmOwner(), equalTo(true));
        assertThat(schema.getDatabaseById(1).getOwnerName(), equalTo("Alex"));

    }

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void forUser() throws CommandException {

        setUser(2); // Bavon, has access only to PEAR
        synchronize();

        GetSchemaHandler handler = new GetSchemaHandler(localDatabase);
        Collector<SchemaDTO> collector = new Collector<SchemaDTO>();
        handler.execute(new GetSchema(), commandContext, collector);

        SchemaDTO schema = collector.getResult();

        assertThat(schema.getDatabases().size(), equalTo(1));

        UserDatabaseDTO pearDb = schema.getDatabaseById(1);
        assertThat(pearDb.getAmOwner(), equalTo(false));
        assertThat(pearDb.isViewAllAllowed(), equalTo(false));
        assertThat(pearDb.isEditAllowed(), equalTo(true));
        assertThat(pearDb.isEditAllAllowed(), equalTo(false));

        ActivityDTO activity = schema.getActivityById(1);
        assertThat(activity.getAttributeGroups().size(), equalTo(3));

        AttributeGroupDTO group = activity.getAttributeGroupById(1);
        assertThat(group.getName(), equalTo("cause"));
        assertThat(group.getAttributes().size(), equalTo(2));
    }
}
