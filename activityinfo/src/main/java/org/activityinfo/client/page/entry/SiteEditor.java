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
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.CommandLoadEvent;
import org.activityinfo.client.command.loader.PagingCmdLoader;
import org.activityinfo.client.command.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.DownloadEvent;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.Shutdownable;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.entry.editor.SiteFormLoader;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AdminLevelModel;
import org.activityinfo.shared.dto.SiteModel;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteEditor extends AbstractEditorGridPresenter<SiteModel> implements PagePresenter {

    private Listener<SiteEvent> siteChangedListener;
    private Listener<SiteEvent> siteCreatedListener;
    private Listener<SiteEvent> siteSelectedListner;
    private List<Shutdownable> subComponents = new ArrayList<Shutdownable>();

    public static final int PAGE_SIZE = 25;

    public interface View extends GridView<SiteEditor, SiteModel> {

        public void init(SiteEditor presenter, ActivityModel activity, ListStore<SiteModel> store);

        void setSelection(int siteId);
    }

    private final View view;
    private final EventBus eventBus;
    private final CommandService service;
    private final SiteFormLoader formLoader;

    protected final ListStore<SiteModel> store;
    protected final PagingCmdLoader<SiteResult> loader;

    protected ActivityModel currentActivity;

    private Integer siteIdToSelectOnNextLoad;

    @Inject
    public SiteEditor(EventBus eventBus, CommandService service, IStateManager stateMgr, final View view) {
        super(eventBus, service, stateMgr, view);
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;

        formLoader = new SiteFormLoader(eventBus, service);

        loader = new PagingCmdLoader<SiteResult>(service);
        store = new ListStore<SiteModel>(loader);

        initListeners(store, loader);

        siteChangedListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent se) {

                SiteModel ourCopy = store.findModel("id", se.getSite().getId());
                if(ourCopy != null) {
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
                if(se.getSource() != SiteEditor.this) {
                    SiteModel site = store.findModel("id", se.getSiteId());
                    if(site != null) {
                        view.setSelection(se.getSiteId());
                    }
                }
            }
        };
        this.eventBus.addListener(AppEvents.SiteSelected, siteSelectedListner);
    }

    private void onSiteCreated(SiteEvent se) {
        if(store.getCount() < PAGE_SIZE) {
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

        for(Shutdownable subComponet : subComponents) {
            subComponet.shutdown();
        }
    }

    @Override
    public ListStore<SiteModel> getStore() {
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
        return Pages.SiteGrid;
    }

    public ActivityModel getCurrentActivity() {
        return currentActivity;
    }

    private String stateId(String suffix) {
        return "sitegridpage." + currentActivity.getId();
    }

    public void go(SiteGridPlace place, ActivityModel activity) {

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


    public boolean navigate(final Place place) {

        if(!(place instanceof SiteGridPlace)) {
            return false;
        }
     
        final SiteGridPlace gridPlace = (SiteGridPlace)place;

        if(currentActivity.getId() != gridPlace.getActivityId()) {
            return false;
        }

        handleGridNavigation(loader, gridPlace);

        return true;
    }

    protected void onLoaded(LoadEvent le) {
        super.onLoaded(le);

        PagingResult result = (PagingResult)le.getData();

        view.setActionEnabled(UIActions.export, result.getTotalLength() != 0);

        /*
         * Let everyone else know we have navigated
         */
        firePageEvent(new SiteGridPlace(currentActivity), le);

        /*
         * Select a site
         */

        if(siteIdToSelectOnNextLoad != null) {
            view.setSelection(siteIdToSelectOnNextLoad);
            siteIdToSelectOnNextLoad = null;
        }
    }

    public void onSelectionChanged(SiteModel selectedSite) {

        if(selectedSite == null) {
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

    private boolean isEditable(SiteModel selectedSite) {
        UserDatabaseDTO db = currentActivity.getDatabase();
        boolean editable = db.isEditAllAllowed() ||
                (db.isEditAllowed() && db.getMyPartnerId() == selectedSite.getPartner().getId());
        return editable;
    }

    @Override
    public boolean beforeEdit(Record record, String property) {
        return isEditable((SiteModel) record.getModel());
    }

    @Override
    protected Command createSaveCommand() {

        BatchCommand batch = new BatchCommand();
        for(Record record : store.getModifiedRecords()) {
            batch.add(new UpdateEntity("Site", (Integer)record.get("id"), getChangedProperties(record)));
        }

        return batch;
    }

    @Override
    public void onUIAction(String actionId) {
        super.onUIAction(actionId);
        if(UIActions.export.equals(actionId)) {
            onExport();
        } else if(UIActions.map.equals(actionId)) {
           
        }
    }

//    public ListStore<AdminEntityModel> getAdminEntityStore(String property, SiteModel site) {
//
//        int levelId = AdminLevelModel.levelIdForProperty(property);
//        AdminLevelModel level = schema.getAdminLevelById(levelId);
//
//        GetAdminEntities cmd;
//
//        if(level.isRoot()) {
//            cmd = new GetAdminEntities(levelId);
//        } else {
//            AdminEntityModel parent = site.getAdminEntity( level.getParentLevelId());
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
//        ListStore<AdminEntityModel> store = new ListStore<AdminEntityModel>(loader);
//        if(site.get(property) != null) {
//            store.add((AdminEntityModel) site.get(property));
//        }
//
//        return store;
//    }



    protected void onAdd() {

        SiteModel newSite = new SiteModel();
        newSite.setActivityId(currentActivity.getId());

        if(!currentActivity.getDatabase().isEditAllAllowed()) {
            newSite.setPartner(currentActivity.getDatabase().getMyPartner());
        }

        // initialize with defaults
        SiteModel sel = view.getSelection();
        if(sel != null) {
            for(Map.Entry<String,Object> prop : sel.getProperties().entrySet()) {
                if(prop.getKey().startsWith(AdminLevelModel.PROPERTY_PREFIX)) {
                    newSite.set(prop.getKey(), prop.getValue());
                }
            }
        }

        formLoader.edit(currentActivity, newSite, new NullAsyncMonitor());

    }

    protected void onEdit(SiteModel site) {
        formLoader.edit(currentActivity, site, new NullAsyncMonitor());
    }


    @Override
    protected void onDeleteConfirmed(final SiteModel site) {

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
        eventBus.fireEvent(new DownloadEvent(url));
   }

    public void onFilter(String filter) {
        GetSites cmd = (GetSites) loader.getCommand();
        cmd.setFilter(filter);
        loader.load();
    }

}
