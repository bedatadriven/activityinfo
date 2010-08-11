/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

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

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class IndicatorTreePanel extends ContentPanel {

    private final Dispatcher service;

    private TreeLoader<ModelData> loader;
    private TreeStore<ModelData> store;
    private TreePanel<ModelData> tree;
    private ToolBar toolBar;
    private StoreFilterField filter;
    private AsyncMonitor monitor; 
    
    public IndicatorTreePanel(Dispatcher service, final boolean multipleSelection) {
    	this(service, multipleSelection, null);
    }
    
    public IndicatorTreePanel(Dispatcher service, final boolean multipleSelection, AsyncMonitor monitor) {
        this.service = service;
        //this.setHeaderVisible(false);
        this.setHeading(I18N.CONSTANTS.indicators());
        this.setIcon(IconImageBundle.ICONS.indicator());
        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.NONE);
        this.monitor = monitor;
     

        loader = new BaseTreeLoader<ModelData>(new Proxy()) {
            @Override
            public boolean hasChildren(ModelData parent) {
                return !(parent instanceof IndicatorDTO) &&
                        !(parent instanceof IndicatorGroup);
            }
        };

        store = new TreeStore<ModelData>(loader);
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            @Override
            public String getKey(ModelData model) {
                if (model instanceof UserDatabaseDTO) {
                    return "db" + ((UserDatabaseDTO) model).getId();
                } else if (model instanceof ActivityDTO) {
                    return "act" + ((ActivityDTO) model).getId();
                } else if (model instanceof IndicatorDTO) {
                    return "i" + ((IndicatorDTO) model).getId();
                } else {
                    return model.get("name");
                }
            }
        });

        tree = new TreePanel<ModelData>(store);
        tree.setCheckable(true);
        if (multipleSelection) {
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
                if (model instanceof IndicatorDTO) {
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
//                } else if(model instanceof ActivityDTO) {
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
                if (tpe.getNode() != null) {
                    ModelData model = tpe.getNode().getModel();
                    if (model instanceof IndicatorDTO &&
                            tpe.within(tree.getView().getTextElement(tpe.getNode())) &&
                            !tree.isChecked(model)) {

                        tree.setChecked(model, true);
                    }
                }
            }
        });
        tree.addListener(Events.BrowserEvent, new Listener<TreePanelEvent<ModelData>>() {

            public void handleEvent(TreePanelEvent<ModelData> be) {
                if (be.getEventTypeInt() == Event.ONKEYPRESS) {
                    if (!toolBar.isVisible()) {
                        toolBar.setVisible(true);
                    }
//                    StringBuilder sb = new StringBuilder();
//                    if(filter.getRawValue()!=null) {
//                        sb.append(filter.getRawValue());
//                    }
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
                if (!(event.getItem() instanceof IndicatorDTO) &&
                        event.isChecked()) {
                    tree.setExpanded(event.getItem(), true);
                }

                // for single select, assure that only one indicator is selected
                if (!multipleSelection && event.isChecked()) {
                    for (ModelData model : tree.getCheckedSelection()) {
                        if (model != event.getItem()) {
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
        toolBar.add(new LabelToolItem(I18N.CONSTANTS.search()));
        filter = new StoreFilterField() {

            @Override
            protected boolean doSelect(Store store, ModelData parent,
                                       ModelData record, String property, String filter) {

                String keywords[] = filter.toLowerCase().split("\\s+");

                String name = ((String) record.get("name")).toLowerCase();
                for (String keyword : keywords) {
                    if (name.indexOf(keyword) == -1) {
                        return false;
                    }
                }

                return true;
            }
        };
        filter.addListener(Events.Blur, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                if (filter.getRawValue() == null || filter.getRawValue().length() == 0) {
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

        private SchemaDTO schema;

        public void load(DataReader<List<ModelData>> listDataReader, Object parent, final AsyncCallback<List<ModelData>> callback) {

            if (parent == null) {
                service.execute(new GetSchema(), monitor, new AsyncCallback<SchemaDTO>() {
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    public void onSuccess(SchemaDTO result) {
                        schema = result;
                        callback.onSuccess(new ArrayList<ModelData>(schema.getDatabases()));
                    }
                });
            } else if (parent instanceof UserDatabaseDTO) {
                callback.onSuccess(new ArrayList<ModelData>(((UserDatabaseDTO) parent).getActivities()));

            } else if (parent instanceof ActivityDTO) {
                ActivityDTO activity = ((ActivityDTO) parent);
                ArrayList<ModelData> list = new ArrayList<ModelData>();

                for (IndicatorGroup group : activity.groupIndicators()) {
                    if (group.getName() != null) {
                        list.add(group);
                    }
                    for (IndicatorDTO indicator : group.getIndicators()) {
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
//                for(ActivityDTO activity : db.getActivities()) {
//                    if(activity.getIndicators().size() != 0) {
//                        store.add(db, activity, false);
//
//                        for(IndicatorDTO indicator : activity.getIndicators()) {
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

    public List<IndicatorDTO> getSelection() {

        List<IndicatorDTO> list = new ArrayList<IndicatorDTO>();

        for (ModelData model : tree.getCheckedSelection()) {
            if (model instanceof IndicatorDTO) {
                list.add((IndicatorDTO) model);
            }
        }
        return list;
    }

    public void setSelection(int indicatorId) {

    }

    public void setSelection(Set<Integer> indicatorIds) {
        List<IndicatorDTO> indicators = new ArrayList<IndicatorDTO>();

    }

    public List<Integer> getSelectedIds() {

        List<Integer> list = new ArrayList<Integer>();

        for (ModelData model : tree.getCheckedSelection()) {
            if (model instanceof IndicatorDTO) {
                list.add(((IndicatorDTO) model).getId());
            }
        }
        return list;
    }
}
