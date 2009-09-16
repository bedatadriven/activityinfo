package org.activityinfo.client.page.entry;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.NullAsyncMonitor;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.common.grid.GridView;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.entry.editor.SiteFormLoader;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.date.DateUtil;
import org.activityinfo.shared.dto.*;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class HierSiteEditor extends AbstractEditorGridPresenter<ModelData> {


    public interface View extends GridView<HierSiteEditor, ModelData> {

        void init(HierSiteEditor editor, TreeStore<ModelData> store);
        void setStartMonth(Month month);

    }

    private final EventBus eventBus;
    private final CommandService service;
    private final IStateManager stateMgr;
    private final ActivityModel activity;
    private final View view;
    private final SiteFormLoader formLoader;
    private final DateUtil dateUtil;

    private List<AdminLevelModel> hierarchy;
    private SiteTreeProxy proxy;
    private TreeLoader<ModelData> loader;
    private TreeStore<ModelData> store;

    private Listener<SiteEvent> siteListener;

    public HierSiteEditor(EventBus eventBus, CommandService service, IStateManager stateMgr,
                          DateUtil dateUtil, final ActivityModel activity, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.stateMgr = stateMgr;
        this.dateUtil = dateUtil;
        this.formLoader = new SiteFormLoader(eventBus, service);
        this.activity = activity;
        this.view = view;

        hierarchy =  activity.getAdminLevels();
        proxy = new SiteTreeProxy(service, hierarchy, activity.getId());
        initStartMonth();

        loader = new BaseTreeLoader<ModelData>(proxy) {
            @Override
            public boolean hasChildren(ModelData parent) {
                return !(parent instanceof IndicatorRow);
            }
        };
        store = new TreeStore<ModelData>(loader);
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            public String getKey(ModelData model) {
                if(model instanceof AdminLevelModel) {
                    return "A" + ((AdminLevelModel) model).getId();
                } else if(model instanceof SiteModel) {
                    return "S" + ((SiteModel)model).getId();
                } else if(model instanceof IndicatorRow) {
                    return "I" + ((IndicatorRow)model).getIndicatorId();
                }
                return model.toString();
            }
        });

        this.view.init(this, store);
        this.view.setActionEnabled(UIActions.add, activity.getDatabase().isEditAllowed());
        this.view.setActionEnabled(UIActions.delete, false);
        this.view.setActionEnabled(UIActions.edit, false);

        this.view.setStartMonth(proxy.getStartMonth().plus(-7));

        siteListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent be) {
                if(be.getType() == AppEvents.SiteCreated && be.getSite().getActivityId()==activity.getId()) {
                    onSiteAdded(be.getSite());
                }
            }
        };
        eventBus.addListener(AppEvents.SiteCreated, siteListener);

    }


    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteCreated, siteListener);
    }

    @Override
    public Store getStore() {
        return store;
    }

    public TreeStore<ModelData> getTreeStore() {
        return store;
    }

    @Override
    protected String getStateId() {
        return "hierSite" + activity.getId();
    }                                         

    private void initStartMonth() {
        String stateKey = "monthlyView" + this.activity.getId() + "startMonth";
        if(stateMgr.getString(stateKey)!=null) {
            try {
                proxy.setStartMonth(Month.parseMonth(stateMgr.getString(stateKey)));
            } catch(NumberFormatException e) {
            }
        }
        if(proxy.getStartMonth() == null) {
            proxy.setStartMonth(dateUtil.getCurrentMonth());
        }
        proxy.setEndMonth(proxy.getStartMonth().plus(7));
    }

    public void onSelectionChanged(ModelData selectedItem) {

        if(selectedItem instanceof SiteModel) {

            UserDatabaseDTO db = activity.getDatabase();
            boolean editable = db.isEditAllAllowed() ||
                    (db.isEditAllowed() && db.getMyPartnerId() == ((SiteModel) selectedItem).getPartner().getId());

            view.setActionEnabled(UIActions.delete, editable);
            view.setActionEnabled(UIActions.edit, editable);

            eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, this, (SiteModel) selectedItem));

        } else {

            view.setActionEnabled(UIActions.delete, false);
            view.setActionEnabled(UIActions.edit, false);
        }
    }

    private AdminEntityModel findEntity(int id, List<ModelData> models) {
        for(ModelData model : models) {
            Integer modelId = model.get("id");
            if(modelId != null && modelId == id) {
                return (AdminEntityModel) model;
            }
        }
        return null;
    }

    private void onSiteAdded(SiteModel site) {

        loader.load();

//        AdminEntityModel parent = null;
//        for(int i=0; i!=hierarchy.size()-1; ++i) {
//
//            AdminEntityModel entity = site.getAdminEntity(hierarchy.get(i).getId());
//            int entityId = entity.getId();
//            AdminEntityModel child =null;
//            if(parent == null) {
//                child = findEntity(entityId, store.getRootItems());
//            } else {
//
//                child = findEntity(entityId, store.getChildren(parent));
//            }
//            if(child == null) {
//                child = entity;
//                store.add(parent, child, false);
//            }
//            parent = child;
//        }
//        store.add(parent, new SiteModel(site), false);
    }


    public PageId getPageId() {
        return Pages.SiteGrid;
    }

    public Object getWidget() {
        return view;
    }

    public boolean navigate(Place place) {
        return false;
    }



    public boolean beforeEdit(Record record, String property) {
        return (record.getModel() instanceof IndicatorRow);
    }

    public void onMonthSelected(Month month) {
        Month startMonth = month.plus(-3);
        proxy.setStartMonth(startMonth);
        loader.load();
    }

    @Override
    protected void onAdd() {

        SiteModel newSite = new SiteModel();
        newSite.setActivityId(activity.getId());

        if(!activity.getDatabase().isEditAllAllowed()) {
            newSite.setPartner(activity.getDatabase().getMyPartner());
        }

        // initialize with defaults
        ModelData sel = view.getSelection();
        ModelData parent = store.getParent(sel);
        while(parent instanceof AdminEntityModel) {
            AdminEntityModel entity = (AdminEntityModel) parent;
            newSite.setAdminEntity(entity.getLevelId(), entity);
        }

        formLoader.edit(activity, newSite, new NullAsyncMonitor());

    }

    @Override
    protected Command createSaveCommand() {
        Map<Integer, ArrayList<UpdateMonthlyReports.Change>> sites = new HashMap<Integer, ArrayList<UpdateMonthlyReports.Change>>();
        for(Record record : store.getModifiedRecords()) {
            SiteModel site = (SiteModel)store.getParent(record.getModel());

            ArrayList<UpdateMonthlyReports.Change> changes = sites.get(site.getId());
            if(changes == null) {
                changes = new ArrayList<UpdateMonthlyReports.Change>();
                sites.put(site.getId(), changes);
            }

            IndicatorRow report = (IndicatorRow) record.getModel();
            for(String property : record.getChanges().keySet()) {
                UpdateMonthlyReports.Change change = new UpdateMonthlyReports.Change();
                change.indicatorId = report.getIndicatorId();
                change.month = IndicatorRow.monthForProperty(property);
                change.value = report.get(property);
                changes.add(change);
            }
        }

        BatchCommand batch = new BatchCommand();
        for(Map.Entry<Integer, ArrayList<UpdateMonthlyReports.Change>> changes : sites.entrySet()) {
            batch.add(new UpdateMonthlyReports(changes.getKey(), changes.getValue()));
        }
        return batch;
    }

    @Override
    protected void onDeleteConfirmed(final ModelData model) {

        service.execute(new Delete((EntityDTO) model), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });
    }
}
