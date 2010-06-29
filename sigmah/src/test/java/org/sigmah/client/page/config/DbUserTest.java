package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.store.Record;
import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.mock.DummyData;
import org.sigmah.client.mock.MockCommandService;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.mock.MockStateManager;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.UpdateUserPermissions;
import org.sigmah.shared.command.result.UserResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.SchemaDTO;

import static org.easymock.EasyMock.*;

/**
 * @author Alex Bertram
 */
public class DbUserTest {

    @Test
    public void testSaveButtonEnabled() {


        // Test Data
        UserResult users = DummyData.RRM_Users();
        SchemaDTO schema = DummyData.PEAR();

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
        editor.go(schema.getDatabaseById(1), new DbPageState(DbUserEditor.DatabaseUsers, 1));
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
        SchemaDTO schema = DummyData.PEAR();

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUserPermissions.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new MockStateManager(), view);
        editor.go(schema.getDatabaseById(1), new DbPageState(DbUserEditor.DatabaseUsers, 1));
        
        // VERIFY that following a modification a user command to save results in
        // an update user command

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        editor.onUIAction(UIActions.save);

        UpdateUserPermissions cmd = service.getLastExecuted(UpdateUserPermissions.class);
        Assert.assertEquals("typhaine@sol.net", cmd.getModel().getEmail());
        Assert.assertEquals(true, cmd.getModel().getAllowViewAll());
    }


    @Test
    public void testSaveButtonDisabledAfterSave() {


        // Test Data
        UserResult users = DummyData.RRM_Users();
        SchemaDTO schema = DummyData.PEAR();

        // Collaborator: command service
        MockCommandService service = new MockCommandService();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUserPermissions.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new MockStateManager(), view);
        editor.go(schema.getDatabaseById(1), new DbPageState(DbUserEditor.DatabaseUsers, 1));

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
