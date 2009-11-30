package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import org.activityinfo.client.Application;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.page.common.widget.RemoteComboBox;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class AdminFilterPanel extends ContentPanel {

    private final CommandService service;
    private TreeStore<AdminEntityModel> store;
    private AdminTreeLoader loader;
    private ComboBox<AdminLevelModel> levelCombo;

    private TreePanel<AdminEntityModel> tree;

    public AdminFilterPanel(CommandService service) {
        this.service = service;

        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeading(Application.CONSTANTS.filterByGeography());
        this.setIcon(Application.ICONS.filter());


        loader = new AdminTreeLoader(service);
        store = new TreeStore<AdminEntityModel>(loader);

        tree = new TreePanel<AdminEntityModel>(store);

        tree.setCheckable(true);
        tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
        tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);
        
        tree.setDisplayProperty("name");
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);


        add(tree);

        createFilterBar();
    }


    private void createFilterBar() {
        ToolBar toolBar = new ToolBar();
        //toolBar.add(new LabelToolItem(Application.CONSTANTS.filter()));
        toolBar.setEnableOverflow(false);

        ListLoader<AdminLevelModel> loader = new BaseListLoader(new AdminLevelProxy(service));
        final ListStore<AdminLevelModel> store = new ListStore<AdminLevelModel>(loader);

        levelCombo = new RemoteComboBox<AdminLevelModel>();
        levelCombo.setStore(store);
        levelCombo.setDisplayField("name");
        levelCombo.setValueField("id");
        levelCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
        levelCombo.setEditable(false);
        levelCombo.setForceSelection(true);
        levelCombo.addListener(Events.Select, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                onHierarchyChanged(buildHierarchy(store.getModels(), levelCombo.getValue()));
            }
        });

        toolBar.add(levelCombo);

        // TODO : 118n
        Button clear = new Button("Enlever", Application.ICONS.delete(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                levelCombo.setValue(null);
                onHierarchyChanged(Collections.<AdminLevelModel>emptyList());
            }
        });
        //toolBar.add(clear);    // this button was creating layout problems and is not essential

        setTopComponent(toolBar);
    }

    protected List<AdminLevelModel> buildHierarchy(List<AdminLevelModel> levels, AdminLevelModel selected) {
        List<AdminLevelModel> list = new ArrayList<AdminLevelModel>();
        list.add(selected);

        while(selected.getParentLevelId() != null) {
            for(AdminLevelModel level : levels) {
                if(level.getId() == selected.getParentLevelId()) {
                    list.add(level);
                    selected = level;
                    break;
                }
            }
        }
        Collections.reverse(list);

        return list;
    }

    protected void onHierarchyChanged(List<AdminLevelModel> hierarchy) {
        loader.setHierarchy(hierarchy);
        store.removeAll();
        loader.load();
    }

    public List<AdminEntityModel> getSelection() {
        List<AdminEntityModel> checked = tree.getCheckedSelection();
        List<AdminEntityModel> selected = new ArrayList<AdminEntityModel>();

        if(levelCombo.getValue() == null) {
            return selected;
        }
        int filterLevelId = levelCombo.getValue().getId();

        for(AdminEntityModel entity : checked) {
            if(entity.getLevelId() == filterLevelId) {
                selected.add(entity);
            }
        }
        return selected;
    }

}
