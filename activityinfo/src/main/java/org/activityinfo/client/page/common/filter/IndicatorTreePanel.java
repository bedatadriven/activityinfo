package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.Application;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class IndicatorTreePanel extends ContentPanel {

    private final CommandService service;

    private TreeLoader<ModelData> loader;
    private TreeStore<ModelData> store;
    private TreePanel<ModelData> tree;
    private ToolBar toolBar;
    private StoreFilterField filter;

    public IndicatorTreePanel(CommandService service, final boolean multipleSelection) {
        this.service = service;
        this.setHeaderVisible(false);
        this.setHeading(Application.CONSTANTS.indicators());
        this.setIcon(Application.ICONS.indicator());
        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);

        loader = new BaseTreeLoader<ModelData>(new Proxy()) {
            @Override
            public boolean hasChildren(ModelData parent) {
                return !(parent instanceof IndicatorModel) &&
                       !(parent instanceof IndicatorGroup);
            }
        };

        store = new TreeStore<ModelData>(loader);
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            @Override
            public String getKey(ModelData model) {
                if(model instanceof UserDatabaseDTO) {
                    return "db" + ((UserDatabaseDTO) model).getId();
                } else if(model instanceof ActivityModel) {
                    return "act" + ((ActivityModel) model).getId();
                } else if(model instanceof IndicatorModel){
                    return "i" + ((IndicatorModel) model).getId();
                } else {
                    return model.get("name");
                }
            }
        });

        tree = new TreePanel<ModelData>(store);
        tree.setCheckable(true);
        if(multipleSelection) {
            tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
            tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);
        } else {
            tree.setCheckNodes(TreePanel.CheckNodes.LEAF);
            tree.setCheckStyle(TreePanel.CheckCascade.NONE);
        }
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);
        tree.setLabelProvider(new ModelStringProvider<ModelData>() {
            public String getStringValue(ModelData model, String property) {
                String name = model.get("name");
                if(model instanceof IndicatorModel) {
                    return name;
                } else {
                    return "<b>" + name + "</b>";
                }
            }
        });
//        tree.setIconProvider(new ModelIconProvider<ModelData>() {
//
//            @Override
//            public AbstractImagePrototype getIcon(ModelData model) {
//                if(model instanceof UserDatabaseDTO) {
//                    return Application.ICONS.database();
//                } else if(model instanceof ActivityModel) {
//                    return Application.ICONS.activity();
//                } else {
//                    return null;
//                }
//            }
//        });
        tree.setStateId("indicatorPanel");
        tree.setStateful(true);
        tree.setAutoSelect(true);
        tree.addListener(Events.OnClick, new Listener<TreePanelEvent<ModelData>>() {
            public void handleEvent(TreePanelEvent<ModelData> tpe) {

                // when the user clicks on the label of an indicator, check
                // the indicator
                if(tpe.getNode() != null) {
                    ModelData model = tpe.getNode().getModel();
                    if(model instanceof IndicatorModel &&
                         tpe.within(tree.getView().getTextElement(tpe.getNode())) &&
                       ! tree.isChecked(model)) {

                        tree.setChecked(model, true);
                    }
                }
            }
        });
        tree.addListener(Events.BrowserEvent, new Listener<TreePanelEvent<ModelData>>() {

            public void handleEvent(TreePanelEvent<ModelData> be) {
                if(be.getEventTypeInt() == Event.ONKEYPRESS) {
                    if(!toolBar.isVisible()) {
                        toolBar.setVisible(true);
                    }
//                    StringBuilder sb = new StringBuilder();
//                    if(filter.getRawValue()!=null) {
//                        sb.append(filter.getRawValue());
//                    \}
//                    sb.append((char)be.getEvent().getKeyCode());
//                    filter.setRawValue(sb.toString());
                    filter.focus();
                }
            }
        });
        tree.addListener(Events.CheckChange, new Listener<TreePanelEvent<ModelData>>() {
            public void handleEvent(TreePanelEvent<ModelData> event) {

                // when a user checks a parent node, expand all
                // child nodes
                if(!(event.getItem() instanceof IndicatorModel) &&
                    event.isChecked()) {

                    tree.setExpanded(event.getItem(), true );
                }

                // for single select, assure that only one indicator is selected
                if(!multipleSelection && event.isChecked()) {
                    for(ModelData model : tree.getCheckedSelection()) {
                        if(model != event.getItem()) {
                            tree.setChecked(model, false);
                        }
                    }
                }
            }
        });

        add(tree);

        createFilterBar();

    }

    private void createFilterBar() {
        toolBar = new ToolBar();
        toolBar.add(new LabelToolItem(Application.CONSTANTS.search()));
        filter = new StoreFilterField() {

            @Override
            protected boolean doSelect(Store store, ModelData parent,
                                       ModelData record, String property, String filter) {

                String keywords[] = filter.toLowerCase().split("\\s+");

                String name = ((String) record.get("name")).toLowerCase();
                for(String keyword : keywords) {
                    if(name.indexOf(keyword) == - 1)
                        return false;
                }
                
                return true;
            }
        };
        filter.addListener(Events.Blur, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if(filter.getRawValue() == null || filter.getRawValue().length()==0) {
                    toolBar.setVisible(false);
                }
            }
        });
        toolBar.add(filter);
        toolBar.setVisible(false);
        filter.bind(store);
        setTopComponent(toolBar);
    }

    private class Proxy implements DataProxy<List<ModelData>> {

        private Schema schema;

        public void load(DataReader<List<ModelData>> listDataReader, Object parent, final AsyncCallback<List<ModelData>> callback) {

            if(parent == null) {
                service.execute(new GetSchema(), null, new AsyncCallback<Schema>() {
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    public void onSuccess(Schema result) {
                        schema = result;
                        callback.onSuccess(new ArrayList<ModelData>(schema.getDatabases()));
                    }
                });
            } else if(parent instanceof UserDatabaseDTO) {
                callback.onSuccess(new ArrayList<ModelData>(((UserDatabaseDTO) parent).getActivities()));

            } else if(parent instanceof ActivityModel) {
                ActivityModel activity = ((ActivityModel)parent);
                ArrayList<ModelData> list = new ArrayList<ModelData>();

                for(IndicatorGroup group : activity.groupIndicators()) {
                    if(group.getName() != null)
                        list.add(group);
                    for(IndicatorModel indicator : group.getIndicators()) {
                        list.add(indicator);
                    }
                }
                callback.onSuccess(list);
            }
        }
    }

//
//    public void setSchema(Schema schema) {
//
//        store.removeAll();
//        store.setFiresEvents(false);
//
//        for(UserDatabaseDTO db : schema.getDatabases()) {
//            if(db.hasIndicators()) {
//
//                store.add(db, false);
//
//                for(ActivityModel activity : db.getActivities()) {
//                    if(activity.getIndicators().size() != 0) {
//                        store.add(db, activity, false);
//
//                        for(IndicatorModel indicator : activity.getIndicators()) {
//                            store.add(activity, indicator, false);
//                        }
//                    }
//                }
//            }
//        }
//
//        store.setFiresEvents(true);
//        store.fireEvent(Store.DataChanged, new TreeStoreEvent<ModelData>(store));
//    }

    public List<IndicatorModel> getSelection() {

        List<IndicatorModel> list = new ArrayList<IndicatorModel>();

        for(ModelData model : tree.getCheckedSelection()) {
            if(model instanceof IndicatorModel) {
                list.add((IndicatorModel) model);
            }
        }
        return list;
    }

    public void setSelection(int indicatorId) {

    }

    public void setSelection(Set<Integer> indicatorIds) {
        List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
        
    }

    public List<Integer> getSelectedIds() {

        List<Integer> list = new ArrayList<Integer>();

        for(ModelData model : tree.getCheckedSelection()) {
            if(model instanceof IndicatorModel) {
                list.add(((IndicatorModel) model).getId());
            }
        }
        return list;
    }
}
