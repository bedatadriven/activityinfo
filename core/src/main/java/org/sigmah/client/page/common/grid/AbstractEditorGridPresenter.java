/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.loader.CommandLoadEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.BatchResult;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractEditorGridPresenter<M extends ModelData>
        extends AbstractGridPresenter<M> {

    private GridView view;
    private Dispatcher service;
    private boolean isDirty = false;

    protected AbstractEditorGridPresenter(EventBus eventBus, Dispatcher service, StateProvider stateMgr, GridView view) {
        super(eventBus, stateMgr, view);
        this.view = view;
        this.service = service;
    }

    @Override
    protected void initListeners(Store store, Loader loader) {
        super.initListeners(store, loader);

        store.addListener(Store.Update, new Listener<StoreEvent>() {
            @Override
			public void handleEvent(StoreEvent be) {
                boolean isDirtyNow = be.getStore().getModifiedRecords().size() != 0;
                if (isDirty != isDirtyNow) {
                    isDirty = isDirtyNow;
                    onDirtyFlagChanged(isDirty);
                }
            }
        });
    }

    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);

        if (UIActions.SAVE.equals(actionId)) {
            onSave();
        } else if (UIActions.DISCARD_CHANGES.equals(actionId)) {
            getStore().rejectChanges();
        }

    }

    protected abstract Command createSaveCommand();

    public abstract Store getStore();

    /**
     * Returns the list of modified records. The default implementation
     * simply calls <code>getStore().getModifiedRecords()</code> but should
     * be overriden for EditorTree which doesn't appear to track which
     * records have been modified.
     *
     * @return The list of modified records
     */
    public List<Record> getModifiedRecords() {
        return getStore().getModifiedRecords();
    }

    /**
     * Responds to an explict user action to save
     */
    protected void onSave() {

        service.execute(createSaveCommand(), view.getSavingMonitor(), new AsyncCallback() {
            @Override
			public void onFailure(Throwable caught) {
                // let the monitor handle failure, we're not
                // expecting any exceptions
            }

            @Override
			public void onSuccess(Object result) {
                getStore().commitChanges();

                onSaved();
            }
        });
    }

    /**
     * The grid is about to be refreshed, if there are modifications
     * then the save command needs to be included in the call to the server
     *
     * @param le Load Event
     */
    @Override
    protected void onBeforeLoad(CommandLoadEvent le) {
        if (getModifiedRecords().size() != 0) {
            le.addCommandToBatch(createSaveCommand());
        }
    }

    /*
     * The user has chosen to navigate away from this page
     * We will automatically try to save any unsaved changes, but
     * if it fails, we give the user a choice between retrying and
     * and discarding changes
     */

    @Override
	public void requestToNavigateAway(PageState place, final NavigationCallback callback) {

        if (getModifiedRecords().size() == 0) {
            callback.onDecided(true);
        } else {
            service.execute(createSaveCommand(), view.getSavingMonitor(), new AsyncCallback<BatchResult>() {

                @Override
				public void onSuccess(BatchResult result) {
                    getStore().commitChanges();
                    callback.onDecided(true);
                }

                @Override
				public void onFailure(Throwable caught) {
                    // TODO
                }
            });
        }
    }

    @Override
	public String beforeWindowCloses() {
        if (getModifiedRecords().size() == 0) {
            return null;
        } else {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
    }

    @Override
    public void onDirtyFlagChanged(boolean isDirty) {
        view.setActionEnabled(UIActions.SAVE, isDirty);
    }

    protected Map<String, Object> getChangedProperties(Record record) {
        Map<String, Object> changes = new HashMap<String, Object>();

        for (String property : record.getChanges().keySet()) {
            changes.put(property, record.get(property));
        }
        return changes;
    }

    protected void onSaved() {

    }

}
