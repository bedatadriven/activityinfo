/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.DispatcherStub;
import org.activityinfo.client.mock.StateManagerStub;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.DbUserEditor;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.DTOs;
import org.activityinfo.shared.dto.SchemaDTO;
import org.junit.Assert;
import org.junit.Test;

import com.extjs.gxt.ui.client.store.Record;

/**
 * @author Alex Bertram
 */
public class DbUserTest {

    @Test
    public void testSaveButtonEnabled() {


        // Test Data
        UserResult users = DTOs.RRM_Users();
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator: command service
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetUsers.class, users);

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        view.setActionEnabled(UIActions.SAVE, false);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new StateManagerStub(), view);

        // VERIFY save button is initially disabled
        editor.go(schema.getDatabaseById(1));
        verify(view);

        // VERIFY that following a modification the button is enabled
        reset(view);
        view.setActionEnabled(UIActions.SAVE, true);
        replay(view);

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        verify(view);
    }

    @Test
    public void testSave() {


        // Test Data
        UserResult users = DTOs.RRM_Users();
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator: command service
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUserPermissions.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new StateManagerStub(), view);
        editor.go(schema.getDatabaseById(1));
        
        // VERIFY that following a modification a user command to save results in
        // an update user command

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        editor.onUIAction(UIActions.SAVE);

        UpdateUserPermissions cmd = service.getLastExecuted(UpdateUserPermissions.class);
        Assert.assertEquals("typhaine@sol.net", cmd.getModel().getEmail());
        Assert.assertEquals(true, cmd.getModel().getAllowViewAll());
    }


    @Test
    public void testSaveButtonDisabledAfterSave() {


        // Test Data
        UserResult users = DTOs.RRM_Users();
        SchemaDTO schema = DTOs.PEAR();

        // Collaborator: command service
        DispatcherStub service = new DispatcherStub();
        service.setResult(GetUsers.class, users);
        service.setResult(UpdateUserPermissions.class, new VoidResult());

        // Collaborator: View
        DbUserEditor.View view = createNiceMock(DbUserEditor.View.class);
        replay(view);

        // Class under test
        DbUserEditor editor = new DbUserEditor(new MockEventBus(), service, new StateManagerStub(), view);
        editor.go(schema.getDatabaseById(1));

        // VERIFY that following a successful save the save button is disabled

        Record record = editor.getStore().getRecord(users.getData().get(0));
        record.set("allowViewAll", true);

        reset(view);
        view.setActionEnabled(UIActions.SAVE, false);
        replay(view);

        editor.onUIAction(UIActions.SAVE);

        verify(view);
    }
}
