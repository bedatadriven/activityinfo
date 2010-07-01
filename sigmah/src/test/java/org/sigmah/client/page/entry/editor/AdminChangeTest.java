/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.mock.DispatcherStub;
import org.sigmah.client.mock.DummyData;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;

import java.util.Map;

import static org.easymock.EasyMock.createNiceMock;
/*
 * @author Alex Bertram
 */

public class AdminChangeTest {


    @Test
    public void testChange3rdLevelAdmin() {

        // collaborator: event bus
        MockEventBus eventBus = new MockEventBus();

        // collaborator: command service
        DispatcherStub service = new DispatcherStub();

        // collaborator: view
        AdminFieldSetPresenter.View view = createNiceMock(AdminFieldSetPresenter.View.class);

        // test data
        SchemaDTO schema = DummyData.PEAR();
        SiteDTO site = DummyData.PEAR_Sites().get(4);

        // CLASS UNDER TEST
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, schema.getActivityById(91), view);

        // VERIFY: changing one adminlevel works properlty
        presenter.setSite(site);
        presenter.onSelectionChanged(3, new AdminEntityDTO(3, 9221, "Ruizi"));

        Map<String,Object> properties = presenter.getPropertyMap();

        Assert.assertEquals("Nord Kivu", ((AdminEntityDTO) properties.get(AdminLevelDTO.getPropertyName(1))).getName());
        Assert.assertEquals("Beni", ((AdminEntityDTO) properties.get(AdminLevelDTO.getPropertyName(2))).getName());
        Assert.assertEquals("Ruizi", ((AdminEntityDTO) properties.get(AdminLevelDTO.getPropertyName(3))).getName());

    }

}
