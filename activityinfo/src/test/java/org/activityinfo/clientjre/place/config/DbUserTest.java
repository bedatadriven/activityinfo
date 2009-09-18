package org.activityinfo.clientjre.place.config;

import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.config.DbPlace;
import org.activityinfo.client.page.config.DbUserEditor;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.clientjre.mock.MockStateManager;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.UpdateUser;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.Schema;
import org.junit.Test;
import org.junit.Assert;
import static org.easymock.EasyMock.*;
import com.extjs.gxt.ui.client.store.Record;
/*
 * @author Alex Bertram
 */

public class DbUserTest {

    @Test
    public void testSaveButtonEnabled() {


        // Test Data
        UserResult users = DummyData.RRM_Users();
        Schema schema = DummyData.PEAR();

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(GetUsers.class, users);

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        view.setActionEnabled(UIActions.save, false);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new MockStateManager(), view);

        // VERIFY save button is initially disabled
        editor.go(schema.getDatabaseById(1), new DbPlace(Pages.DatabaseUsers, 1));
        verify(view);

        // VERIFY that following a modification the button is enabled
        reset(view);
        view.setActionEnabled(UIActions.save, true);
        replay(view);

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        verify(view);
    }

    @Test
    public void testSave() {


        // Test Data
        UserResult users = DummyData.RRM_Users();
        Schema schema = DummyData.PEAR();

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUser.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new MockStateManager(), view);
        editor.go(schema.getDatabaseById(1), new DbPlace(Pages.DatabaseUsers, 1));
        
        // VERIFY that following a modification a user command to save results in
        // an update user command

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        editor.onUIAction(UIActions.save);

        UpdateUser cmd = service.getLastExecuted(UpdateUser.class);
        Assert.assertEquals("typhaine@sol.net", cmd.getModel().getEmail());
        Assert.assertEquals(true, cmd.getModel().getAllowViewAll());
    }


    @Test
    public void testSaveButtonDisabledAfterSave() {


        // Test Data
        UserResult users = DummyData.RRM_Users();
        Schema schema = DummyData.PEAR();

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUser.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new MockStateManager(), view);
        editor.go(schema.getDatabaseById(1), new DbPlace(Pages.DatabaseUsers, 1));

        // VERIFY that following a successful save the save button is disabled

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        reset(view);
        view.setActionEnabled(UIActions.save, false);
        replay(view);

        editor.onUIAction(UIActions.save);

        verify(view);
    }
}
