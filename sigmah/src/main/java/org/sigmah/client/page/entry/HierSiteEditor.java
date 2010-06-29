/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.GridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.SiteFormLoader;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.*;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.date.DateUtil;
import org.sigmah.shared.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * @author Alex Bertram
 */

public class HierSiteEditor extends AbstractEditorGridPresenter<ModelData> {


    public interface View extends GridView<HierSiteEditor, ModelData> {

        void init(HierSiteEditor editor, TreeStore<ModelData> store);

        void setStartMonth(Month month);

    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final IStateManager stateMgr;
    private final ActivityDTO activity;
    private final View view;
    private final SiteFormLoader formLoader;
    private final DateUtil dateUtil;

    private List<AdminLevelDTO> hierarchy;
    private SiteTreeProxy proxy;
    private TreeLoader<ModelData> loader;
    private TreeStore<ModelData> store;

    private Listener<SiteEvent> siteListener;

    public HierSiteEditor(EventBus eventBus, Dispatcher service, IStateManager stateMgr,
                          DateUtil dateUtil, final ActivityDTO activity, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.stateMgr = stateMgr;
        this.dateUtil = dateUtil;
        this.formLoader = new SiteFormLoader(eventBus, service);
        this.activity = activity;
        this.view = view;

        hierarchy = activity.getAdminLevels();
        proxy = new SiteTreeProxy(service, hierarchy, activity.getId());
        initStartMonth();

        loader = new BaseTreeLoader<ModelData>(proxy) {
            @Override
            public boolean hasChildren(ModelData parent) {
                return !(parent instanceof IndicatorRowDTO);
            }
        };
        store = new TreeStore<ModelData>(loader);
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            public String getKey(ModelData model) {
                if (model instanceof AdminLevelDTO) {
                    return "A" + ((AdminLevelDTO) model).getId();
                } else if (model instanceof SiteDTO) {
                    return "S" + ((SiteDTO) model).getId();
                } else if (model instanceof IndicatorRowDTO) {
                    return "I" + ((IndicatorRowDTO) model).getIndicatorId();
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
                if (be.getType() == AppEvents.SiteCreated && be.getSite().getActivityId() == activity.getId()) {
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
        if (stateMgr.getString(stateKey) != null) {
            try {
                proxy.setStartMonth(Month.parseMonth(stateMgr.getString(stateKey)));
            } catch (NumberFormatException e) {
            }
        }
        if (proxy.getStartMonth() == null) {
            proxy.setStartMonth(dateUtil.getCurrentMonth());
        }
        proxy.setEndMonth(proxy.getStartMonth().plus(7));
    }

    public void onSelectionChanged(ModelData selectedItem) {

        if (selectedItem instanceof SiteDTO) {

            UserDatabaseDTO db = activity.getDatabase();
            boolean editable = db.isEditAllAllowed() ||
                    (db.isEditAllowed() && db.getMyPartnerId() == ((SiteDTO) selectedItem).getPartner().getId());

            view.setActionEnabled(UIActions.delete, editable);
            view.setActionEnabled(UIActions.edit, editable);

            eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, this, (SiteDTO) selectedItem));

        } else {

            view.setActionEnabled(UIActions.delete, false);
            view.setActionEnabled(UIActions.edit, false);
        }
    }

    private AdminEntityDTO findEntity(int id, List<ModelData> models) {
        for (ModelData model : models) {
            Integer modelId = model.get("id");
            if (modelId != null && modelId == id) {
                return (AdminEntityDTO) model;
            }
        }
        return null;
    }

    private void onSiteAdded(SiteDTO site) {

        loader.load();

//        AdminEntityDTO parent = null;
//        for(int i=0; i!=hierarchy.size()-1; ++i) {
//
//            AdminEntityDTO entity = site.getAdminEntity(hierarchy.get(i).getId());
//            int entityId = entity.getId();
//            AdminEntityDTO child =null;
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
//        store.add(parent, new SiteDTO(site), false);
    }


    public PageId getPageId() {
        return SiteEditor.ID;
    }

    public Object getWidget() {
        return view;
    }

    public boolean navigate(PageState place) {
        return false;
    }


    public boolean beforeEdit(Record record, String property) {
        return (record.getModel() instanceof IndicatorRowDTO);
    }

    public void onMonthSelected(Month month) {
        Month startMonth = month.plus(-3);
        proxy.setStartMonth(startMonth);
        loader.load();
    }

    @Override
    protected void onAdd() {

        SiteDTO newSite = new SiteDTO();
        newSite.setActivityId(activity.getId());

        if (!activity.getDatabase().isEditAllAllowed()) {
            newSite.setPartner(activity.getDatabase().getMyPartner());
        }

        // initialize with defaults
        ModelData sel = view.getSelection();
        ModelData parent = store.getParent(sel);
        while (parent instanceof AdminEntityDTO) {
            AdminEntityDTO entity = (AdminEntityDTO) parent;
            newSite.setAdminEntity(entity.getLevelId(), entity);
        }

        formLoader.edit(activity, newSite, new NullAsyncMonitor());

    }

    @Override
    protected Command createSaveCommand() {
        Map<Integer, ArrayList<UpdateMonthlyReports.Change>> sites = new HashMap<Integer, ArrayList<UpdateMonthlyReports.Change>>();
        for (Record record : store.getModifiedRecords()) {
            SiteDTO site = (SiteDTO) store.getParent(record.getModel());

            ArrayList<UpdateMonthlyReports.Change> changes = sites.get(site.getId());
            if (changes == null) {
                changes = new ArrayList<UpdateMonthlyReports.Change>();
                sites.put(site.getId(), changes);
            }

            IndicatorRowDTO report = (IndicatorRowDTO) record.getModel();
            for (String property : record.getChanges().keySet()) {
                UpdateMonthlyReports.Change change = new UpdateMonthlyReports.Change();
                change.indicatorId = report.getIndicatorId();
                change.month = IndicatorRowDTO.monthForProperty(property);
                change.value = report.get(property);
                changes.add(change);
            }
        }

        BatchCommand batch = new BatchCommand();
        for (Map.Entry<Integer, ArrayList<UpdateMonthlyReports.Change>> changes : sites.entrySet()) {
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
