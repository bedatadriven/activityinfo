package org.activityinfo.clientjre.place.entry.editor;

import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.clientjre.mock.MockEventBus;
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
