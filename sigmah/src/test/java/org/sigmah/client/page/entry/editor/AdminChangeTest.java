/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.client.page.entry.editor;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.mock.DummyData;
import org.sigmah.client.mock.MockCommandService;
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
        MockCommandService service = new MockCommandService();

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
