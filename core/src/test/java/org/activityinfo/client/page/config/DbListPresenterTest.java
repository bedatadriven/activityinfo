/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.or;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;

import java.util.HashSet;
import java.util.Set;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.DbListPresenter;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DbListPresenterTest {
    private static final int OWNED_DB_ID = 1;
    private static final int DESIGNABLE_DB_ID = 2;
    private static final int VIEWABLE_DB_ID = 3;

    private Dispatcher dispatcher = createMock(Dispatcher.class);

    private MockEventBus eventBus = new MockEventBus();
    private DbListPresenter.View view = createMock(DbListPresenter.View.class);
    private DbListPresenter presenter;
    private SchemaDTO schema = new SchemaDTO();
    protected UserDatabaseDTO ownedDb;
    protected UserDatabaseDTO designableDb;
    protected UserDatabaseDTO viewableDb;

    @Before
    public void setUp() throws Exception {

        ownedDb = new UserDatabaseDTO(OWNED_DB_ID, "My Database");
        ownedDb.setAmOwner(true);
        ownedDb.setDesignAllowed(true);
        ownedDb.setManageUsersAllowed(true);
        schema.getDatabases().add(ownedDb);

        designableDb = new UserDatabaseDTO(DESIGNABLE_DB_ID, "My Database");
        designableDb.setAmOwner(false);
        designableDb.setDesignAllowed(true);
        designableDb.setManageUsersAllowed(true);
        schema.getDatabases().add(designableDb);

        viewableDb = new UserDatabaseDTO(VIEWABLE_DB_ID, "My database");
        viewableDb.setAmOwner(false);
        viewableDb.setDesignAllowed(false);
        viewableDb.setManageUsersAllowed(false);
        schema.getDatabases().add(viewableDb);
    }

    @Test
    public void loaderPopulatesStore() {

        ignoreView();

        expectDispatch(new GetSchema(), schema);
        replay(dispatcher);

        createPresenter();
        ListStore<UserDatabaseDTO> store = presenter.getStore();

        assertThat("store.getCount()", store.getCount(), is(equalTo(3)));

        verify(dispatcher);
    }

    @Test
    public void deleteAndEditAreDisabledIfNoDatabaseIsSelected() {
        expectSetActionEnabled();
        replay(view);

        createPresenter();
        presenter.onSelectionChanged(null);

        assertThat(disabledActions, hasItems(UIActions.EDIT, UIActions.DELETE));
    }

    @Test
    public void deleteAndEditAreEnabledIfDatabaseIsOwned() {
        expectSetActionEnabled();
        replay(view);

        createPresenter();
        presenter.onSelectionChanged(ownedDb);

        assertThat(disabledActions, not(hasItem(UIActions.EDIT)));
        assertThat(disabledActions, not(hasItem(UIActions.DELETE)));
    }

    @Test
    public void editIsEnabledIfUserHasDesignRights() {
        expectSetActionEnabled();
        replay(view);

        createPresenter();
        presenter.onSelectionChanged(designableDb);

        assertThat(disabledActions, hasItem(UIActions.DELETE));
        assertThat(disabledActions, not(hasItem(UIActions.EDIT)));
    }

    @Test
    public void editDeleteAreDisabledWithoutRights() {
        expectSetActionEnabled();
        replay(view);

        createPresenter();
        presenter.onSelectionChanged(viewableDb);

        assertThat(disabledActions, hasItems(UIActions.EDIT, UIActions.DELETE));
    }

    @Test
    public void commandShouldBePreparedProperly() {

        Capture<CreateEntity> cmd = new Capture<CreateEntity>();
        expectDispatch(new GetSchema(), schema);
        captureDispatch(cmd);
        replay(dispatcher);

        UserDatabaseDTO newDb = new UserDatabaseDTO();
        newDb.setCountry(new CountryDTO(31, "Haiti"));
        newDb.setName("My Db");

        createPresenter();
        presenter.save(newDb, niceFormDialogMock());

        assertTrue("command was dispatched", cmd.hasCaptured());
        assertThat((Integer)cmd.getValue().getProperties().get("countryId"), is(equalTo(31)));
    }

    private FormDialogTether niceFormDialogMock() {
        FormDialogTether mock = createNiceMock(FormDialogTether.class);
        replay(mock);
        return mock;
    }

    private void captureDispatch(Capture<CreateEntity> cmd) {
        dispatcher.execute(and(isA(CreateEntity.class), capture(cmd)), optionalMonitor(), isA(AsyncCallback.class));
    }


    private void createPresenter() {
        presenter = new DbListPresenter(eventBus, dispatcher, view);
    }

    private void ignoreView() {
        view = createNiceMock(DbListPresenter.View.class);
        replay(view);
    }

    private Set<String> disabledActions = new HashSet<String>();

    private void expectSetActionEnabled() {
        view.setActionEnabled(isA(String.class), anyBoolean());
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                String actionId = (String) getCurrentArguments()[0];
                Boolean enabled = (Boolean) getCurrentArguments()[1];
                if(enabled) {
                    disabledActions.remove(actionId);
                } else {
                    disabledActions.add(actionId);
                }
                return null;
            }
        }).anyTimes();
    }

    private <T extends CommandResult> void expectDispatch(Command<T> command, final T result) {
        this.dispatcher.execute(eq(command),
                optionalMonitor(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                ((AsyncCallback) getCurrentArguments()[DESIGNABLE_DB_ID]).onSuccess(schema);
                return null;
            }
        });
    }

    private AsyncMonitor optionalMonitor() {
        return or(isA(AsyncMonitor.class), EasyMock.<AsyncMonitor>isNull());
    }

}