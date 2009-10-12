package org.activityinfo.clientjre.place.entry.editor;

import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.clientjre.place.entry.editor.mock.MockAdminFieldSet;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.SiteModel;
import static org.easymock.EasyMock.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AdminFieldSetTest {



    @Test
    public void testSetSite() throws Exception {

        // Test data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 100), DummyData.getTerritoires(100));

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS Under Test
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);


        // Verify that setting a sites results in the correct values being
        // sent to the view, and that the correct combos are enabled
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        Assert.assertEquals(101, fieldSet.getValue(2).getId());

        Assert.assertTrue("province combo is enabled", fieldSet.getEnabled(1));
        Assert.assertTrue("territory combo is enabled", fieldSet.getEnabled(2));


        // VERIFY that the correct command has been set for combos
        ListStore<AdminEntityModel> store = fieldSet.getStore(2);

        store.getLoader().load();

        Assert.assertEquals("territory store count", 2, store.getCount());

    }

    @Test
    public void testInitBlank() throws Exception {

        // TEST data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: Command Service
        MockCommandService service = new MockCommandService();

        // Collaborator: View
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS under TEST
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);
        presenter.setSite(new SiteModel());

        // verify that the combos are properly enabled
        Assert.assertTrue("province combo is enabled", fieldSet.getEnabled(1));
        Assert.assertFalse("territory combo is disabled", fieldSet.getEnabled(2));
        Assert.assertFalse("secteur combo is disabled", fieldSet.getEnabled(3));
    }

    @Test
    public void testCascade() throws Exception {


        // Test Data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: Command Service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 100), DummyData.getTerritoires(100));

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS under TEST
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);



        // VERIFY that root level loader is properly configured
        presenter.setSite(DummyData.PEAR_Sites().get(5));

        ListStore<AdminEntityModel> store = fieldSet.getStore(1);
        store.getLoader().load();

        Assert.assertEquals("province store count", 2, store.getCount());



        // VERIFY that a change to the province reconfigures the territory loader
        presenter.onSelectionChanged(1, DummyData.getProvinces().getData().get(0));

        store = fieldSet.getStore(2);
        store.getLoader().load();

        Assert.assertEquals("territory store count", 2, store.getCount());
        Assert.assertEquals("Beni", store.getAt(0).getName());
    }


    @Test
    public void testCascadeReplace() throws Exception {

        // Test data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 200), DummyData.getTerritoires(200));

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS Under Test
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);

        // SETUP selection
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        // VERIFY that change to province correctly cascades
        presenter.onSelectionChanged(1, DummyData.getProvinces().getData().get(1));

        Assert.assertNull("territory is null", fieldSet.getValue(2));
        Assert.assertNull("sector is null", fieldSet.getValue(3));
        Assert.assertFalse("sector is disabled", fieldSet.getEnabled(3));

        ListStore<AdminEntityModel> store = fieldSet.getStore(2);
        store.getLoader().load();

        Assert.assertEquals("territory store count", 2, store.getCount());
        Assert.assertEquals("correct territory list", "Shabunda", store.getAt(0).getName());
    }

    @Test
    public void testBounds() {

        // Test data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: command service      
        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 200), DummyData.getTerritoires(200));

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS Under Test
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);

        // SETUP selection
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        // VERIFY bounds are correct
        Assert.assertEquals("bounds", DummyData.Beni.getBounds(), presenter.getBounds());
        Assert.assertEquals("bounds name", DummyData.Beni.getName(), presenter.getBoundsName());
    }



    @Test
    public void testBoundsChange() {

        // Test data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetAdminEntities(1), DummyData.getProvinces());
        service.setResult(new GetAdminEntities(2, 200), DummyData.getTerritoires(200));

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS Under Test
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);

        // SETUP selection
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        // VERIFY that a change to admin level triggers a bounds changed call

        AdminFieldSetPresenter.Listener listener = createMock(AdminFieldSetPresenter.Listener.class);
        listener.onBoundsChanged(eq(DummyData.Masisi.getName()), eq(DummyData.Masisi.getBounds()));
        listener.onModified();
        replay(listener);

        presenter.setListener(listener);
        fieldSet.setValueAndFire(2, DummyData.Masisi);

        verify(listener);
    }



    @Test
    public void testDirtyChecking() throws Exception {

        // Test data
        Schema schema = DummyData.PEAR();
        ActivityModel nfi = schema.getActivityById(91);

        // Collaborator: command service
        MockCommandService service = new MockCommandService();

        // Collaborator: view
        MockAdminFieldSet fieldSet = new MockAdminFieldSet();

        // CLASS Under Test
        AdminFieldSetPresenter presenter = new AdminFieldSetPresenter(service, nfi, fieldSet);

        // VERIFY isDirty returns false
        presenter.setSite(DummyData.PEAR_Sites().get(4));

        Assert.assertFalse(presenter.isDirty());

        // VERIFY isDirty returns true after a change
        fieldSet.setValueAndFire(3, new AdminEntityModel(3, 999, DummyData.Beni.getId(), "Ruizi"));

        Assert.assertTrue(presenter.isDirty());

        // VERIFY isDirty returns false after we change back
        fieldSet.setValueAndFire(3, DummyData.Watalina);

        Assert.assertFalse(presenter.isDirty());

    }



}
