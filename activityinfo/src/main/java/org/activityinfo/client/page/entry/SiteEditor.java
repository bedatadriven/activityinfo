package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.loader.CommandLoadEvent;
import org.activityinfo.client.dispatch.loader.PagingCmdLoader;
import org.activityinfo.client.event.DownloadEvent;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.Shutdownable;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.entry.editor.SiteFormLoader;
import org.activityinfo.client.util.state.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteEditor extends AbstractEditorGridPresenter<SiteDTO> implements Page {

    private Listener<SiteEvent> siteChangedListener;
    private Listener<SiteEvent> siteCreatedListener;
    private Listener<SiteEvent> siteSelectedListner;
    private List<Shutdownable> subComponents = new ArrayList<Shutdownable>();

    public static final int PAGE_SIZE = 25;
    public static final PageId ID = new PageId("site-grid");

    public interface View extends GridView<SiteEditor, SiteDTO> {

        public void init(SiteEditor presenter, ActivityDTO activity, ListStore<SiteDTO> store);
        public AsyncMonitor getLoadingMonitor();

        void setSelection(int siteId);
    }

    private final View view;
    private final EventBus eventBus;
    private final Dispatcher service;
    private final SiteFormLoader formLoader;

    protected final ListStore<SiteDTO> store;
    protected final PagingCmdLoader<SiteResult> loader;

    protected ActivityDTO currentActivity;

    private Integer siteIdToSelectOnNextLoad;

    @Inject
    public SiteEditor(EventBus eventBus, Dispatcher service, IStateManager stateMgr, final View view) {
        super(eventBus, service, stateMgr, view);
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;

        formLoader = new SiteFormLoader(eventBus, service);

        loader = new PagingCmdLoader<SiteResult>(service);
        store = new ListStore<SiteDTO>(loader);

        initListeners(store, loader);

        siteChangedListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {

                SiteDTO ourCopy = store.findModel("id", se.getSite().getId());
                if (ourCopy != null) {
                    ourCopy.setProperties(se.getSite().getProperties());
                }
                store.update(ourCopy);

            }
        };
        this.eventBus.addListener(AppEvents.SiteChanged, siteChangedListener);

        siteCreatedListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {
                onSiteCreated(se);
            }
        };
        this.eventBus.addListener(AppEvents.SiteCreated, siteCreatedListener);

        siteSelectedListner = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {
                // check to see if this site is on the current page
                if (se.getSource() != SiteEditor.this) {
                    SiteDTO site = store.findModel("id", se.getSiteId());
                    if (site != null) {
                        view.setSelection(se.getSiteId());
                    }
                }
            }
        };
        this.eventBus.addListener(AppEvents.SiteSelected, siteSelectedListner);
    }

    private void onSiteCreated(SiteEvent se) {
        if (store.getCount() < PAGE_SIZE) {
            // there is only one page, so we can save some time by justing adding this model to directly to
            //  the store
            store.add(se.getSite());
        } else {
            // there are multiple pages and we don't really know where this site is going
            // to end up, so do a reload and seek to the page with the new site
            ((GetSites) loader.getCommand()).setSeekToSiteId(se.getSite().getId());
            siteIdToSelectOnNextLoad = se.getSite().getId();
            loader.load();
        }
    }


    public void addSubComponent(Shutdownable subComponent) {
        subComponents.add(subComponent);
    }

    public void shutdown() {

        eventBus.removeListener(AppEvents.SiteChanged, siteChangedListener);
        eventBus.removeListener(AppEvents.SiteCreated, siteCreatedListener);
        eventBus.removeListener(AppEvents.SiteSelected, siteSelectedListner);

        for (Shutdownable subComponet : subComponents) {
            subComponet.shutdown();
        }
    }

    @Override
    public ListStore<SiteDTO> getStore() {
        return store;
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    protected String getStateId() {
        return "SiteGrid" + currentActivity.getId();
    }

    @Override
    public PageId getPageId() {
        return ID;
    }

    public ActivityDTO getCurrentActivity() {
        return currentActivity;
    }

    private String stateId(String suffix) {
        return "sitegridpage." + currentActivity.getId();
    }

    public void go(SiteGridPageState place, ActivityDTO activity) {

        currentActivity = activity;

        /*
        * Define the intial load parameters based on
        * the navigation event, the, by previous user state
        * and then by sensible defaults (sorted by date)
        */

        initLoaderDefaults(loader, place, new SortInfo("date2", Style.SortDir.DESC));

        loader.setCommand(GetSites.byActivity(currentActivity.getId()));


        view.init(SiteEditor.this, currentActivity, store);

        view.setActionEnabled(UIActions.add, currentActivity.getDatabase().isEditAllowed());
        view.setActionEnabled(UIActions.edit, false);
        view.setActionEnabled(UIActions.delete, false);

        loader.load();
    }


    public boolean navigate(final PageState place) {

        if (!(place instanceof SiteGridPageState)) {
            return false;
        }

        final SiteGridPageState gridPlace = (SiteGridPageState) place;

        if (currentActivity.getId() != gridPlace.getActivityId()) {
            return false;
        }

        handleGridNavigation(loader, gridPlace);

        return true;
    }

    protected void onLoaded(LoadEvent le) {
        super.onLoaded(le);

        PagingResult result = (PagingResult) le.getData();

        view.setActionEnabled(UIActions.export, result.getTotalLength() != 0);

        /*
         * Let everyone else know we have navigated
         */
        firePageEvent(new SiteGridPageState(currentActivity), le);

        /*
         * Select a site
         */

        if (siteIdToSelectOnNextLoad != null) {
            view.setSelection(siteIdToSelectOnNextLoad);
            siteIdToSelectOnNextLoad = null;
        }
    }

    public void onSelectionChanged(SiteDTO selectedSite) {

        if (selectedSite == null) {
            view.setActionEnabled(UIActions.delete, false);
            view.setActionEnabled(UIActions.edit, false);
        } else {

            boolean editable = isEditable(selectedSite);

            view.setActionEnabled(UIActions.delete, editable);
            view.setActionEnabled(UIActions.edit, editable);
        }

        eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, this, selectedSite));
    }

    @Override
    protected void onBeforeLoad(CommandLoadEvent le) {
        super.onBeforeLoad(le);

        view.setActionEnabled(UIActions.add, currentActivity.getDatabase().isEditAllowed());
        view.setActionEnabled(UIActions.edit, false);
        view.setActionEnabled(UIActions.delete, false);
    }

    private boolean isEditable(SiteDTO selectedSite) {
        UserDatabaseDTO db = currentActivity.getDatabase();
        boolean editable = db.isEditAllAllowed() ||
                (db.isEditAllowed() && db.getMyPartnerId() == selectedSite.getPartner().getId());
        return editable;
    }

    @Override
    public boolean beforeEdit(Record record, String property) {
        return isEditable((SiteDTO) record.getModel());
    }

    @Override
    protected Command createSaveCommand() {

        BatchCommand batch = new BatchCommand();
        for (Record record : store.getModifiedRecords()) {
            batch.add(new UpdateEntity("Site", (Integer) record.get("id"), getChangedProperties(record)));
        }

        return batch;
    }

    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);
        if (UIActions.export.equals(actionId)) {
            onExport();
        } else if (UIActions.map.equals(actionId)) {

        }
    }

//    public ListStore<AdminEntityDTO> getAdminEntityStore(String property, SiteDTO site) {
//
//        int levelId = AdminLevelDTO.levelIdForProperty(property);
//        AdminLevelDTO level = schema.getAdminLevelById(levelId);
//
//        GetAdminEntities cmd;
//
//        if(level.isRoot()) {
//            cmd = new GetAdminEntities(levelId);
//        } else {
//            AdminEntityDTO parent = site.getAdminEntity( level.getParentLevelId());
//            if( parent != null ) {
//                cmd = new GetAdminEntities(levelId, parent.getId());
//            } else {
//                return null;
//            }
//        }
//
//        ListCmdLoader<AdminEntityResult> loader = new ListCmdLoader<AdminEntityResult>(service);
//        loader.setCommand(cmd);
//
//        ListStore<AdminEntityDTO> store = new ListStore<AdminEntityDTO>(loader);
//        if(site.get(property) != null) {
//            store.add((AdminEntityDTO) site.get(property));
//        }
//
//        return store;
//    }


    protected void onAdd() {

        SiteDTO newSite = new SiteDTO();
        newSite.setActivityId(currentActivity.getId());

        if (!currentActivity.getDatabase().isEditAllAllowed()) {
            newSite.setPartner(currentActivity.getDatabase().getMyPartner());
        }

        // initialize with defaults
        SiteDTO sel = view.getSelection();
        if (sel != null) {
            for (Map.Entry<String, Object> prop : sel.getProperties().entrySet()) {
                if (prop.getKey().startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
                    newSite.set(prop.getKey(), prop.getValue());
                }
            }
        }

        formLoader.edit(currentActivity, newSite, view.getLoadingMonitor());

    }

    protected void onEdit(SiteDTO site) {
        formLoader.edit(currentActivity, site, view.getLoadingMonitor());
    }


    @Override
    protected void onDeleteConfirmed(final SiteDTO site) {

        service.execute(new Delete(site), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {

            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                store.remove(site);
            }
        });
    }

    private void onExport() {
        String url = GWT.getModuleBaseURL() + "export?auth=#AUTH#&a=" + currentActivity.getId();
        eventBus.fireEvent(new DownloadEvent("siteExport", url));
    }

    public void onFilter(String filter) {
        GetSites cmd = (GetSites) loader.getCommand();
        cmd.setFilter(filter);
        loader.load();
    }

}
