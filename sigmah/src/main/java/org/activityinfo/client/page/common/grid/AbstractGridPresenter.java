package org.activityinfo.client.page.common.grid;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.loader.CommandLoadEvent;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.SortInfoEqualityChecker;
import org.activityinfo.client.util.state.IStateManager;

import java.util.HashMap;
import java.util.Map;
/*
 * @author Alex Bertram
 */

public abstract class AbstractGridPresenter<ModelT extends ModelData>
        implements GridPresenter<ModelT>, Page {

    private final EventBus eventBus;
    private final IStateManager stateMgr;
    private final GridView<GridPresenter, ModelT> view;

    protected AbstractGridPresenter(EventBus eventBus, IStateManager stateMgr, GridView view) {
        this.eventBus = eventBus;
        this.stateMgr = stateMgr;
        this.view = view;

    }

    public int getPageSize() {
        return -1;
    }

    protected void initListeners(Store store, Loader loader) {
        if (loader != null) {
            loader.addLoadListener(new LoadListener() {
                @Override
                public void loaderLoad(LoadEvent le) {
                    onLoaded(le);
                }

                @Override
                public void loaderBeforeLoad(LoadEvent le) {
                    onBeforeLoad((CommandLoadEvent) le);
                }
            });
        }
    }

    public void onDirtyFlagChanged(boolean isDirty) {

    }

    public void onUIAction(String actionId) {
        if (UIActions.delete.equals(actionId)) {
            view.confirmDeleteSelected(new ConfirmCallback() {
                public void confirmed() {
                    onDeleteConfirmed(view.getSelection());
                }
            });
        } else if (UIActions.edit.equals(actionId)) {
            onEdit(view.getSelection());
        } else if (UIActions.add.equals(actionId)) {
            onAdd();
        }
    }

    public int pageFromOffset(int offset) {
        return (offset / getPageSize()) + 1;
    }

    public int offsetFromPage(int pagenum) {
        return (pagenum - 1) * getPageSize();
    }

    protected void initLoaderDefaults(PagingLoader loader, AbstractPagingGridPageState place, SortInfo defaultSort) {
        Map<String, Object> stateMap = getState();
        if (place.getSortInfo() != null) {
            loader.setSortField(place.getSortInfo().getSortField());
            loader.setSortDir(place.getSortInfo().getSortDir());
        } else if (stateMap.containsKey("sortField")) {
            loader.setSortField((String) stateMap.get("sortField"));
            loader.setSortDir("DESC".equals(stateMap.get("sortDir")) ?
                    Style.SortDir.DESC : Style.SortDir.ASC);
        } else {
            loader.setSortField(defaultSort.getSortField());
            loader.setSortDir(defaultSort.getSortDir());
        }

        loader.setLimit(getPageSize());

        if (place.getPageNum() > 0) {
            loader.setOffset(offsetFromPage(place.getPageNum()));
        } else if (stateMap.containsKey("offset")) {
            loader.setOffset((Integer) stateMap.get("offset"));
        } else {
            loader.setOffset(0);
        }
    }


    protected void onDeleteConfirmed(ModelT model) {

    }

    protected void onAdd() {

    }

    protected void onEdit(ModelT model) {

    }

    protected abstract String getStateId();

    protected Map<String, Object> getState() {

        Map<String, Object> map = stateMgr.getMap(getStateId());
        if (map != null) {
            return map;
        } else {
            return new HashMap<String, Object>();
        }

    }

    protected void saveState(Map<String, Object> stateMap) {
        stateMgr.set(getStateId(), stateMap);
    }

    protected void onBeforeLoad(CommandLoadEvent le) {

    }

    protected void onLoaded(LoadEvent le) {

        Map<String, Object> stateMap = new HashMap<String, Object>();

        Object config = le.getConfig();
        if (config instanceof ListLoadConfig) {
            SortInfo si = ((ListLoadConfig) config).getSortInfo();
            stateMap.put("sortField", si.getSortField());
            stateMap.put("sortDir", si.getSortDir() == Style.SortDir.ASC ? "ASC" : "DESC");
        }
        if (config instanceof PagingLoadConfig) {
            int offset = ((PagingLoadConfig) config).getOffset();
            stateMap.put("offset", offset);
        }

        saveState(stateMap);
    }

    protected void firePageEvent(AbstractGridPageState place, LoadEvent le) {

        Object config = le.getConfig();
        if (config instanceof ListLoadConfig) {
            place.setSortInfo(((ListLoadConfig) config).getSortInfo());
        }
        if (config instanceof PagingLoadConfig && place instanceof AbstractPagingGridPageState) {
            int offset = ((PagingLoadConfig) config).getOffset();
            ((AbstractPagingGridPageState) place).setPageNum(pageFromOffset(offset));
        }

        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationAgreed, place));
    }


    protected void handleGridNavigation(ListLoader loader, AbstractGridPageState gridPlace) {

        boolean reloadRequired = false;

        if (gridPlace.getSortInfo() != null &&
                !SortInfoEqualityChecker.equals(gridPlace.getSortInfo(), new SortInfo(loader.getSortField(), loader.getSortDir()))) {

            loader.setSortField(gridPlace.getSortInfo().getSortField());
            loader.setSortDir(gridPlace.getSortInfo().getSortDir());
            reloadRequired = true;
        }

        if (gridPlace instanceof AbstractPagingGridPageState) {
            AbstractPagingGridPageState pgPlace = (AbstractPagingGridPageState) gridPlace;

            if (pgPlace.getPageNum() > 0) {
                int offset = offsetFromPage(pgPlace.getPageNum());
                if (offset != ((PagingLoader) loader).getOffset()) {
                    ((PagingLoader) loader).setOffset((pgPlace.getPageNum() - 1) * getPageSize());
                    reloadRequired = true;
                }
            }
        }

        if (reloadRequired) {
            loader.load();
        }
    }

    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);

    }

    public String beforeWindowCloses() {
        return null;
    }

    public boolean beforeEdit(Record record, String property) {
        return true;
    }
}
