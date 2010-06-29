package org.sigmah.client.page.entry.editor;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.mock.DummyData;
import org.sigmah.client.mock.MockCommandService;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.mock.MockSiteForm;
import org.sigmah.shared.dto.SchemaDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormTest {


    @Test
    public void testAdminInit() {

        // Test data
        SchemaDTO schema = DummyData.PEAR();

        // Collaborator: EventBus
        MockEventBus eventBus = new MockEventBus();

        // Collaborator: Command Service
        MockCommandService service = new MockCommandService();

        // Collaborator: View
        MockSiteForm form = new MockSiteForm();

        // CLASS UNDER TEST
        SiteFormPresenter presenter = new SiteFormPresenter(eventBus, service, schema.getActivityById(91), form);
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        // Verify that the admin field set is intialzed
        Assert.assertEquals("province is set", "Nord Kivu", form.adminFieldSet.getValue(1).getName());

        // Verify the save button is disabled
        Assert.assertFalse("save action is disabled", form.isEnabled(UIActions.save));

        // Verify that a change to the admin field set enables the save button
        form.adminFieldSet.setValueAndFire(1, DummyData.SudKivu);

        Assert.assertTrue("save button is now enabled", form.isEnabled(UIActions.save));

    }

    @Test
    public void testMapBoundsSetOnInit() {
        // Test data
        SchemaDTO schema = DummyData.PEAR();

        // Collaborator: EventBus
        MockEventBus eventBus = new MockEventBus();

        // Collaborator: Command Service
        MockCommandService service = new MockCommandService();

        // Collaborator: View
        MockSiteForm form = new MockSiteForm();

        // CLASS UNDER TEST
        SiteFormPresenter presenter = new SiteFormPresenter(eventBus,service, schema.getActivityById(91), form);
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        // Verify that the map view has been centered on the admin bounds
        Assert.assertTrue(form.mapView.getMapView().contains(DummyData.Beni.getBounds()));


    }

}
