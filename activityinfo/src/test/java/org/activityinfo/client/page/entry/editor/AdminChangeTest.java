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

package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.mock.MockEventBus;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.SiteModel;
import static org.easymock.EasyMock.createNiceMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
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
        Schema schema = DummyData.PEAR();
        SiteModel site = DummyData.PEAR_Sites().get(4);

        // CLASS UNDER TEST
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, schema.getActivityById(91), view);

        // VERIFY: changing one adminlevel works properlty
        presenter.setSite(site);
        presenter.onSelectionChanged(3, new AdminEntityModel(3, 9221, "Ruizi"));

        Map<String,Object> properties = presenter.getPropertyMap();

        Assert.assertEquals("Nord Kivu", ((AdminEntityModel) properties.get(AdminLevelModel.getPropertyName(1))).getName());
        Assert.assertEquals("Beni", ((AdminEntityModel) properties.get(AdminLevelModel.getPropertyName(2))).getName());
        Assert.assertEquals("Ruizi", ((AdminEntityModel) properties.get(AdminLevelModel.getPropertyName(3))).getName());

    }

}
