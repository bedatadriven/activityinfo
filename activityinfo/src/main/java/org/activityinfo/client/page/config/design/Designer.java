package org.activityinfo.client.page.config.design;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.TreeGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.DbPlace;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.i18n.UIConstants;

import java.util.List;


/*
 * @author Alex Bertram
 */

public class Designer extends AbstractEditorGridPresenter<ModelData> implements PagePresenter {

    @ImplementedBy(DesignTree.class)
    public interface View extends TreeGridView<Designer, ModelData> {
        public void init(Designer presenter, UserDatabaseDTO db, TreeStore store);

        public FormDialogTether showNewForm(EntityDTO entity, FormDialogCallback callback);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;
    private final UIConstants messages;

    private UserDatabaseDTO db;
    private TreeStore<ModelData> treeStore;


    @Inject
    public Designer(EventBus eventBus, Dispatcher service, IStateManager stateMgr,
                    View view, UIConstants messages) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.messages = messages;
    }

    public void go(UserDatabaseDTO db) {

        this.db = db;

        treeStore = new TreeStore<ModelData>();
        fillStore(messages);

        initListeners(treeStore, null);

        this.view.init(this, db, treeStore);
        this.view.setActionEnabled(UIActions.delete, false);
    }

    public void shutdown() {

    }

    private void fillStore(UIConstants messages) {

        for (ActivityModel activity : db.getActivities()) {
            ActivityModel activityNode = new ActivityModel(activity);
            treeStore.add(activityNode, false);

            AttributeFolder attributeFolder = new AttributeFolder(activityNode, messages.attributes());
            treeStore.add(activityNode, attributeFolder, false);

            for (AttributeGroupModel group : activity.getAttributeGroups()) {
                AttributeGroupModel groupNode = new AttributeGroupModel(group);
                treeStore.add(attributeFolder, groupNode, false);

                for (AttributeModel attribute : group.getAttributes()) {
                    AttributeModel attributeNode = new AttributeModel(attribute);
                    treeStore.add(groupNode, attributeNode, false);
                }
            }

            IndicatorFolder indicatorFolder = new IndicatorFolder(activityNode, messages.indicators());
            treeStore.add(activityNode, indicatorFolder, false);

            for (IndicatorModel indicator : activity.getIndicators()) {
                IndicatorModel indicatorNode = new IndicatorModel(indicator);
                treeStore.add(indicatorFolder, indicatorNode, false);
            }
        }
    }

    @Override
    public Store<ModelData> getStore() {
        return treeStore;
    }

    public TreeStore<ModelData> getTreeStore() {
        return treeStore;
    }

    public boolean navigate(Place place) {
        return place instanceof DbPlace &&
                place.getPageId().equals(Pages.Design) &&
                ((DbPlace) place).getDatabaseId() == db.getId();
    }

    public void onNodeDropped(ModelData source) {

        // update sortOrder

        ModelData parent = treeStore.getParent(source);
        List<ModelData> children = parent == null ? treeStore.getRootItems() : treeStore.getChildren(parent);

        for (int i = 0; i != children.size(); ++i) {
            Record record = treeStore.getRecord(children.get(i));
            record.set("sortOrder", i);
        }

    }

    public void onNew(String entityName) {

        final EntityDTO newEntity;
        ModelData parent;

        ModelData selected = view.getSelection();

        if ("Activity".equals(entityName)) {
            newEntity = new ActivityModel(db);
            newEntity.set("databaseId", db.getId());
            parent = null;

        } else if ("AttributeGroup".equals(entityName)) {
            ActivityModel activity = findActivityFolder(selected);

            newEntity = new AttributeGroupModel();
            newEntity.set("activityId", activity.getId());
            parent = treeStore.getChild(activity, 0);

        } else if ("Attribute".equals(entityName)) {
            AttributeGroupModel group = findAttributeGroupNode(selected);

            newEntity = new AttributeModel();
            newEntity.set("attributeGroupId", group.getId());

            parent = group;

        } else if ("Indicator".equals(entityName)) {
            ActivityModel activity = findActivityFolder(selected);

            IndicatorModel newIndicator = new IndicatorModel();
            newIndicator.setCollectIntervention(true);
            newIndicator.setAggregation(IndicatorModel.AGGREGATE_SUM);

            newEntity = newIndicator;
            newEntity.set("activityId", activity.getId());

            parent = treeStore.getChild(activity, 1);

        } else {
            return; // TODO log error
        }

        createEntity(parent, newEntity);
    }

    private void createEntity(final ModelData parent, final EntityDTO newEntity) {
        view.showNewForm(newEntity, new FormDialogCallback() {
            @Override
            public void onValidated(final FormDialogTether tether) {

                service.execute(new CreateEntity(newEntity), tether, new AsyncCallback<CreateResult>() {
                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(CreateResult result) {
                        newEntity.set("id", result.getNewId()); // todo add setId to EntityDTO interface

                        if (parent == null) {
                            treeStore.add(newEntity, false);
                        } else {
                            treeStore.add(parent, newEntity, false);
                        }

                        if (newEntity instanceof ActivityModel) {
                            treeStore.add(newEntity, new AttributeFolder((ActivityModel) newEntity, messages.attributes()), false);
                            treeStore.add(newEntity, new IndicatorFolder((ActivityModel) newEntity, messages.indicators()), false);
                        }

                        tether.hide();

                        eventBus.fireEvent(AppEvents.SchemaChanged);
                    }
                });

            }
        });
    }

    protected ActivityModel findActivityFolder(ModelData selected) {

        while (!(selected instanceof ActivityModel)) {
            selected = treeStore.getParent(selected);
        }

        return (ActivityModel) selected;
    }

    protected AttributeGroupModel findAttributeGroupNode(ModelData selected) {
        if (selected instanceof AttributeGroupModel) {
            return (AttributeGroupModel) selected;
        }
        if (selected instanceof AttributeModel) {
            return (AttributeGroupModel) treeStore.getParent(selected);
        }
        throw new AssertionError("not a valid selection to add an attribute !");

    }

    @Override
    protected void onDeleteConfirmed(final ModelData model) {
        service.execute(new Delete((EntityDTO) model), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                treeStore.remove(model);
                eventBus.fireEvent(AppEvents.SchemaChanged);
            }
        });
    }

    @Override
    protected String getStateId() {
        return "Design" + db.getId();
    }

    @Override
    protected Command createSaveCommand() {
        BatchCommand batch = new BatchCommand();

        for (ModelData model : treeStore.getRootItems()) {
            prepareBatch(batch, model);
        }
        return batch;
    }

    protected void prepareBatch(BatchCommand batch, ModelData model) {
        if (model instanceof EntityDTO) {
            Record record = treeStore.getRecord(model);
            if (record.isDirty()) {
                batch.add(new UpdateEntity((EntityDTO) model, this.getChangedProperties(record)));
            }
        }

        for (ModelData child : treeStore.getChildren(model)) {
            prepareBatch(batch, child);
        }
    }

    public void onSelectionChanged(ModelData selectedItem) {
        view.setActionEnabled(UIActions.delete, this.db.isDesignAllowed() &&
                selectedItem instanceof EntityDTO);
    }

    public PageId getPageId() {
        return Pages.Design;
    }

    public Object getWidget() {
        return view;
    }

    @Override
    protected void onSaved() {
        eventBus.fireEvent(AppEvents.SchemaChanged);
    }
}
