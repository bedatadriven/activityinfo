package org.activityinfo.clientjre.place.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.store.Record;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.entry.SiteEditor;
import org.activityinfo.client.page.entry.SiteGridPlace;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.clientjre.mock.MockStateManager;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.SiteModel;
import static org.easymock.EasyMock.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGridTest {


    @Before
    public void setUp() {

    }


    @Test
    public void testLoader() {

        Schema schema = DummyData.PEAR();

        // collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), schema);
        service.setResult(GetSites.class, DummyData.PEAR_Sites_Result());

        // collaborator: eventbus
        MockEventBus eventBus = new MockEventBus();

        // collaborator: view
        SiteEditor.View view = createNiceMock(SiteEditor.View.class);
        replay(view);

        // Class Under Test !!
        SiteEditor presenter = new SiteEditor(eventBus, service,
                new MockStateManager(), view);

        // VERIFY that command is correct

        presenter.go(new SiteGridPlace(91), schema.getActivityById(91));

        GetSites cmd = service.getLastExecuted(GetSites.class);
        Assert.assertEquals(91, cmd.getActivityId());
                         
        // VERIFY that rows are loaded

        Assert.assertEquals("number of rows", 2, presenter.getStore().getCount());

    }



    @Test
    public void testPartnerLevelPermissions() {

        Schema schema = DummyData.PEAR();

        // collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), schema);
        service.setResult(GetSites.class, DummyData.PEAR_Sites_Result());

        // collaborator: eventbus
        MockEventBus eventBus = new MockEventBus();


        // collaborator: view
        SiteEditor.View view = createNiceMock("view", SiteEditor.View.class);
        // on load, we have permission to add, but since nothing
        // is yet selected, edit+delete should be set to false
        view.setActionEnabled(UIActions.add, true);
        view.setActionEnabled(UIActions.delete, false);
        view.setActionEnabled(UIActions.edit, false);
        replay(view);

        // Class Under Test !
        SiteEditor presenter = new SiteEditor(eventBus, service, new MockStateManager(), view);

        // navigate to NFI data entry
        // verify that the appropriate actions were enabled correctly
        presenter.go(new SiteGridPlace(91), schema.getActivityById(91));

        verify(view);

        // mock select the first site from NRC, which we have the right to modify

        reset(view);
        view.setActionEnabled(UIActions.edit, true);
        view.setActionEnabled(UIActions.delete, true);
        replay(view);

        presenter.onSelectionChanged(DummyData.PEAR_Sites().get(4));

        Record record =  presenter.getStore().getRecord(presenter.getStore().getAt(0));
        Assert.assertTrue(presenter.beforeEdit(record, "foobar"));

        verify(view);

        // mock select the second site from AVSI, which we do not
        reset(view);
        view.setActionEnabled(UIActions.edit, false);
        view.setActionEnabled(UIActions.delete, false);
        replay(view);

        record =  presenter.getStore().getRecord(presenter.getStore().getAt(0));
        Assert.assertTrue(presenter.beforeEdit(record, "foobar"));

        presenter.onSelectionChanged(DummyData.PEAR_Sites().get(5));

        verify(view);

    }


    @Test
    public void testExportButtonEnabled() {
        Schema schema = DummyData.PEAR();

        // collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), schema);
        service.setResult(GetSites.class, DummyData.PEAR_Sites_Result());

        // collaborator: eventbus
        MockEventBus eventBus = new MockEventBus();

        // collaborator: view
        // we expect the export button to be initially disabled
        SiteEditor.View view = createNiceMock("view", SiteEditor.View.class);
        view.setActionEnabled(UIActions.export, false);
        replay(view);

        // Class Under Test !!

        SiteEditor presenter = new SiteEditor(eventBus, service,
                new MockStateManager(),view);

        // VERIFY that the button is enabled on load
        reset(view);
        view.setActionEnabled(UIActions.export, true);
        replay(view);

        presenter.go(new SiteGridPlace(91), schema.getActivityById(91));

        verify(view);
    }

    @Test
    public void testExportButtonDisabled() {
        Schema schema = DummyData.PEAR();

        // collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), schema);
        service.setResult(GetSites.class, new SiteResult());

        // collaborator: eventbus
        MockEventBus eventBus = new MockEventBus();

        // collaborator: view
        // we expect the export button to be initially disabled
        SiteEditor.View view = createNiceMock("view", SiteEditor.View.class);
        view.setActionEnabled(UIActions.export, false);
        replay(view);

        // Class Under Test !!

        SiteEditor presenter = new SiteEditor(eventBus, service,
                new MockStateManager(), view);

        // VERIFY that the export is not enabled

        presenter.go(new SiteGridPlace(91), schema.getActivityById(91));

        verify(view);
    }


    @Test
    public void testNavigation() {

        final Schema schema = DummyData.PEAR();

        // collaborator: eventBus
        EventBus eventBus = new MockEventBus();

        // collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), schema);
        service.setResult(GetSites.class, new SiteResult(Collections.<SiteModel>emptyList()));

        // collaborator: view
        SiteEditor.View view = createNiceMock(SiteEditor.View.class);
        replay(view);

        // Class under test !
        SiteEditor presenter = new SiteEditor(eventBus, service, new MockStateManager(), view);


        // VERIFY that a navigation request result in command load

        SiteGridPlace firstPlace = new SiteGridPlace(schema.getActivityById(91));
        firstPlace.setPageNum(2);
        firstPlace.setSortInfo(new SortInfo("location", Style.SortDir.DESC));

        presenter.go(firstPlace, schema.getActivityById(91));

        service.assertExecuteCount(GetSites.class, 1);

        GetSites cmd = service.getLastExecuted(GetSites.class);
        Assert.assertEquals(91, cmd.getActivityId());
        Assert.assertEquals("location", cmd.getSortInfo().getSortField());
        Assert.assertEquals(Style.SortDir.DESC, cmd.getSortInfo().getSortDir());
        Assert.assertEquals(SiteEditor.PAGE_SIZE, cmd.getOffset());

        service.resetLog();





        // VERIFY that a second, identical navigation doesn't generate a reload

        presenter.navigate(firstPlace);

        service.assertExecuteCount(GetSites.class, 0);



        


        // verify that a navigation request for a different activity returns false

        Assert.assertFalse(presenter.navigate(new SiteGridPlace(92)));

    }

    @Test
    public void testNewSiteTriggersSeekToPage() {                                       

        // Collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), DummyData.PEAR());
        service.setResult(GetSites.class, DummyData.PEAR_Sites_Many_Results(100));

        // Collaborator: eventbus
        MockEventBus eventBus = new MockEventBus();

        // Collaborator: View
        SiteEditor.View view = createNiceMock(SiteEditor.View.class);
        replay(view);

        // CLASS UNDER Test
        SiteEditor presenter = new SiteEditor(eventBus, service, new MockStateManager(),
                 view);

        // VERIFY that a new site triggers the correct command AND
        //        that the site is selected upon load

        presenter.go(new SiteGridPlace(91), DummyData.PEAR().getActivityById(91));

        resetToDefault(view);
        view.setSelection(3);
        view.setActionEnabled((String) anyObject(), anyBoolean());
        expectLastCall().anyTimes();
        replay(view);

        service.setResult(GetSites.class, new SiteResult(new SiteModel(3)));

        eventBus.fireEvent(new SiteEvent(AppEvents.SiteCreated, this, new SiteModel(3)));

        GetSites cmd = service.getLastExecuted(GetSites.class);
        Assert.assertEquals(3, cmd.getSeekToSiteId());

        verify(view);

    }

    @Test
    public void testSiteUpdate() {

        // Collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(new GetSchema(), DummyData.PEAR());
        service.setResult(GetSites.class, DummyData.PEAR_Sites_Result());

        // Collaborator: eventbus  
        MockEventBus eventBus = new MockEventBus();

        // Collaborator: View
        SiteEditor.View view = createNiceMock(SiteEditor.View.class);
        replay(view);

        // CLASS UNDER TEST
        Schema schema = DummyData.PEAR();
        SiteEditor presenter = new SiteEditor(eventBus, service, new MockStateManager(),
                view);

        presenter.go(new SiteGridPlace(91), schema.getActivityById(91));

        //VERIFY that an external chnage is propageted to the store
        SiteModel site = DummyData.PEAR_Sites().get(4);
        site.setLocationName("Freeport");

        eventBus.fireEvent(new SiteEvent(AppEvents.SiteChanged, this, site));

        SiteModel stored = presenter.getStore().findModel("id", site.getId());
        Assert.assertEquals("Freeport", stored.get("locationName"));

    }

    @Test
    public void testSave() {

        // Dummy data
        Schema schema = DummyData.PEAR();
        SiteResult sites = DummyData.PEAR_Sites_Result();

        // Collaborator: service
        MockCommandService service = new MockCommandService();
        service.setResult(GetSites.class, sites);
        service.setResult(UpdateEntity.class, new VoidResult());

        // Collaborator: View
        SiteEditor.View view = createNiceMock(SiteEditor.View.class);
        replay(view);

        // CLASS UNDER TEST
        SiteEditor editor = new SiteEditor(new MockEventBus(), service, new MockStateManager(),
                 view);

        editor.go(new SiteGridPlace(91), schema.getActivityById(91));


        //VERIFY that an inline change is results in an update entity call

        Record record = editor.getStore().getRecord(sites.getData().get(0));
        record.set("locationName", "Freeport Indiana");

        editor.onUIAction(UIActions.save);

        UpdateEntity cmd = service.getLastExecuted(UpdateEntity.class);
        Assert.assertEquals(sites.getData().get(0).getId(), cmd.getId());
        Assert.assertEquals("Site", cmd.getEntityName());
        Assert.assertEquals("Freeport Indiana", cmd.getChanges().get("locationName"));
                
    }

}
