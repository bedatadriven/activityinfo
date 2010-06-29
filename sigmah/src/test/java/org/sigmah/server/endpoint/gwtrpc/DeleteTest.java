/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import com.extjs.gxt.ui.client.data.ModelData;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.PagingResult;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import java.util.Collection;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class DeleteTest extends CommandTestCase {


    public <T extends ModelData> T getById(Collection<T> list, Integer id) {
        for (T element : list) {
            if (id.equals(element.get("id"))) {
                return element;
            }
        }
        return null;
    }

    @Test
    public void testDeleteSite() throws CommandException {

        PagingResult<SiteDTO> sites = execute(GetSites.byId(3));
        execute(new Delete(sites.getData().get(0)));

        sites = execute(GetSites.byId(3));
        Assert.assertEquals(0, sites.getData().size());

        sites = execute(new GetSites());
        Assert.assertNull(getById(sites.getData(), 3));
    }

    @Test
    public void testDeleteIndicator() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getIndicatorById(1)));

        schema = execute(new GetSchema());
        Assert.assertNull(schema.getIndicatorById(1));

        PagingResult<SiteDTO> sites = execute(GetSites.byId(1));
        Assert.assertNull(sites.getData().get(0).getIndicatorValue(1));
    }

    @Test
    public void testDeleteAttribute() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getActivityById(1).getAttributeById(1)));

        schema = execute(new GetSchema());
        Assert.assertNull(schema.getActivityById(1).getAttributeById(1));
    }


    @Test
    public void testDeleteActivity() throws CommandException {

        SchemaDTO schema = execute(new GetSchema());
        execute(new Delete(schema.getActivityById(1)));
        execute(new Delete("Activity", 4));

        schema = execute(new GetSchema());
        Assert.assertNull("delete by entity reference", schema.getActivityById(1));
        Assert.assertNull("delete by id", schema.getActivityById(4));

    }

}
