package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.CommandLoadEvent;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.BatchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEditorGridPresenter<ModelT extends ModelData>
        extends AbstractGridPresenter<ModelT> {

    private GridView view;
    private CommandService service;
    private boolean isDirty = false;

    protected AbstractEditorGridPresenter(EventBus eventBus, CommandService service, IStateManager stateMgr, GridView view) {
        super(eventBus, stateMgr, view);
        this.view = view;
        this.service = service;
    }

    @Override
    protected void initListeners(Store store, Loader loader) {
        super.initListeners(store, loader);

        store.addListener(Store.Update, new Listener<StoreEvent>() {
            public void handleEvent(StoreEvent be) {
                boolean isDirtyNow = be.getStore().getModifiedRecords().size() != 0;
                if(isDirty != isDirtyNow) {
                    isDirty = isDirtyNow;
                    onDirtyFlagChanged(isDirty);
                }
            }
        });
    }

    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);

        if(UIActions.save.equals(actionId)) {
            onSave();
        } else if(UIActions.discardChanges.equals(actionId)) {
            getStore().rejectChanges();
        }

    }

    protected abstract Command createSaveCommand();

    public abstract Store<ModelT> getStore();

    /**
     * Returns the list of modified records. The default implementation
     * simply calls <code>getStore().getModifiedRecords()</code> but should
     * be overriden for EditorTree which doesn't appear to track which
     * records have been modified.
     * 
     * @return  The list of modified records
     */
    public List<Record> getModifiedRecords() {
        return getStore().getModifiedRecords();
    }

    /**
     * Responds to an explict user action to save
     */
    protected void onSave() {

        service.execute(createSaveCommand(), view.getSavingMonitor(), new AsyncCallback() {
            public void onFailure(Throwable caught) {
                // let the monitor handle failure, we're not
                // expecting any exceptions
            }

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

        if(getModifiedRecords().size()!=0) {
            le.addCommandToBatch(createSaveCommand());
        }
    }

    /*
     * The user has chosen to navigate away from this page
     * We will automatically try to save any unsaved changes, but
     * if it fails, we give the user a choice between retrying and
     * and discarding changes
     */
    public void requestToNavigateAway(Place place, final NavigationCallback callback) {

        if(getModifiedRecords().size() == 0) {
            callback.onDecided(true);
        } else {
            service.execute(createSaveCommand(), view.getSavingMonitor(), new AsyncCallback<BatchResult>() {

                public void onSuccess(BatchResult result) {
                    getStore().commitChanges();
                    callback.onDecided(true);
                }

                public void onFailure(Throwable caught) {
                    // TODO
                }
            });
        }
    }

    public String beforeWindowCloses() {
        if(getModifiedRecords().size() == 0) {
            return null;
        } else {
            return Application.CONSTANTS.unsavedChangesWarning();
        }
    }

    @Override
    public void onDirtyFlagChanged(boolean isDirty) {
        view.setActionEnabled(UIActions.save, isDirty);
    }
    
    protected Map<String,Object> getChangedProperties(Record record) {
        Map<String,Object> changes = new HashMap<String, Object>();

        for(String property : record.getChanges().keySet()) {
            changes.put(property, record.get(property));
        }
        return changes;
    }

    private String logRecord(Record record) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(String p : record.getPropertyNames()) {
            if(sb.length() >1 ) sb.append(", ");
            sb.append(p).append(": ").append(record.get(p));
            if(record.isModified(p)) sb.append("[*]");
            if(!record.isValid(p)) sb.append("[!]");
        }
        sb.append("}");
        return sb.toString();
    }

    protected void onSaved() {
        
    }

}
