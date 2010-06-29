package org.sigmah.client.page.common.filter;

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
import org.sigmah.client.Application;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.widget.RemoteComboBox;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class AdminFilterPanel extends ContentPanel {

    private final Dispatcher service;
    private TreeStore<AdminEntityDTO> store;
    private AdminTreeLoader loader;
    private ComboBox<AdminLevelDTO> levelCombo;

    private TreePanel<AdminEntityDTO> tree;

    public AdminFilterPanel(Dispatcher service) {
        this.service = service;

        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeading(Application.CONSTANTS.filterByGeography());
        this.setIcon(Application.ICONS.filter());


        loader = new AdminTreeLoader(service);
        store = new TreeStore<AdminEntityDTO>(loader);

        tree = new TreePanel<AdminEntityDTO>(store);

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

        ListLoader<AdminLevelDTO> loader = new BaseListLoader(new AdminLevelProxy(service));
        final ListStore<AdminLevelDTO> store = new ListStore<AdminLevelDTO>(loader);

        levelCombo = new RemoteComboBox<AdminLevelDTO>();
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
        Button clear = new Button(Application.CONSTANTS.remove(), Application.ICONS.delete(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                levelCombo.setValue(null);
                onHierarchyChanged(Collections.<AdminLevelDTO>emptyList());
            }
        });
        //toolBar.add(clear);    // this button was creating layout problems and is not essential

        setTopComponent(toolBar);
    }

    protected List<AdminLevelDTO> buildHierarchy(List<AdminLevelDTO> levels, AdminLevelDTO selected) {
        List<AdminLevelDTO> list = new ArrayList<AdminLevelDTO>();
        list.add(selected);

        while (selected.getParentLevelId() != null) {
            for (AdminLevelDTO level : levels) {
                if (level.getId() == selected.getParentLevelId()) {
                    list.add(level);
                    selected = level;
                    break;
                }
            }
        }
        Collections.reverse(list);

        return list;
    }

    protected void onHierarchyChanged(List<AdminLevelDTO> hierarchy) {
        loader.setHierarchy(hierarchy);
        store.removeAll();
        loader.load();
    }

    public List<AdminEntityDTO> getSelection() {
        List<AdminEntityDTO> checked = tree.getCheckedSelection();
        List<AdminEntityDTO> selected = new ArrayList<AdminEntityDTO>();

        if (levelCombo.getValue() == null) {
            return selected;
        }
        int filterLevelId = levelCombo.getValue().getId();

        for (AdminEntityDTO entity : checked) {
            if (entity.getLevelId() == filterLevelId) {
                selected.add(entity);
            }
        }
        return selected;
    }

}
