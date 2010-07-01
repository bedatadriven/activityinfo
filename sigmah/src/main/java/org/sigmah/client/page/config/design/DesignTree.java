/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.design;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.TreeGridDragSource;
import com.extjs.gxt.ui.client.dnd.TreeGridDropTarget;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.treegrid.CellTreeGridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractEditorTreeGridView;
import org.sigmah.client.page.common.grid.ImprovedCellTreeGridSelectionModel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.dto.*;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class DesignTree extends AbstractEditorTreeGridView<ModelData, Designer>
        implements Designer.View {

    protected final Dispatcher service;

    protected EditorTreeGrid<ModelData> tree;
    protected ContentPanel formContainer;

    protected UserDatabaseDTO db;

    @Inject
    public DesignTree(Dispatcher service) {
        this.service = service;
    }

    @Override
    public void init(Designer presenter, UserDatabaseDTO db, TreeStore store) {

        this.db = db;

        setLayout(new BorderLayout());
        setHeading(I18N.CONSTANTS.design() + " - " + db.getName());
        setIcon(IconImageBundle.ICONS.design());

        super.init(presenter, store);

        createFormContainer();
    }


    @Override
    protected Grid<ModelData> createGridAndAddToContainer(Store store) {


        final TreeStore treeStore = (TreeStore) store;

        tree = new EditorTreeGrid<ModelData>(treeStore, createColumnModel());
        tree.setSelectionModel(new ImprovedCellTreeGridSelectionModel<ModelData>());
        tree.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        tree.setAutoExpandColumn("name");
        tree.setHideHeaders(true);
        tree.setLoadMask(true);
        //   tree.setContextMenu(createContextMenu());

        tree.setIconProvider(new ModelIconProvider<ModelData>() {
            public AbstractImagePrototype getIcon(ModelData model) {
                if (model instanceof ActivityDTO) {
                    return IconImageBundle.ICONS.activity();
                } else if (model instanceof Folder) {
                    return GXT.IMAGES.tree_folder_closed();
                } else if (model instanceof AttributeGroupDTO) {
                    return IconImageBundle.ICONS.attributeGroup();
                } else if (model instanceof AttributeDTO) {
                    return IconImageBundle.ICONS.attribute();
                } else if (model instanceof IndicatorDTO) {
                    return IconImageBundle.ICONS.indicator();
                } else {
                    return null;
                }
            }
        });
        tree.addListener(Events.CellClick, new Listener<GridEvent>() {
            public void handleEvent(GridEvent ge) {
                showForm(tree.getStore().getAt(ge.getRowIndex()));
            }
        });

        add(tree, new BorderLayoutData(Style.LayoutRegion.CENTER));

        TreeGridDragSource source = new TreeGridDragSource(tree);
        source.addDNDListener(new DNDListener() {
            @Override
            public void dragStart(DNDEvent e) {

                ModelData sel = ((CellTreeGridSelectionModel) tree.getSelectionModel()).getSelectCell().model;
                if (!db.isDesignAllowed() || sel == null || sel instanceof Folder) {
                    e.setCancelled(true);
                    e.getStatus().setStatus(false);
                    return;
                }
                super.dragStart(e);
            }
        });

        TreeGridDropTarget target = new TreeGridDropTarget(tree);
        target.setAllowSelfAsSource(true);
        target.setFeedback(DND.Feedback.BOTH);
        target.setAutoExpand(false);
        target.addDNDListener(new DNDListener() {
            @Override
            public void dragMove(DNDEvent e) {
                List<TreeModel> sourceData = e.getData();
                ModelData source = sourceData.get(0).get("model");
                TreeGrid.TreeNode target = tree.findNode(e.getTarget());

                if (treeStore.getParent(target.getModel()) !=
                        treeStore.getParent(source)) {

                    e.setCancelled(true);
                    e.getStatus().setStatus(false);
                }
            }

            @Override
            public void dragDrop(DNDEvent e) {
                List<TreeModel> sourceData = e.getData();
                ModelData source = sourceData.get(0).get("model");
                presenter.onNodeDropped(source);
            }
        });
        return tree;
    }


    @Override
    protected void initToolBar() {

        toolBar.addSaveSplitButton();

        SelectionListener<MenuEvent> listener = new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {

                presenter.onNew(ce.getItem().getItemId());
            }
        };

        Menu newMenu = new Menu();
        initNewMenu(newMenu, listener);

        Button newButtonMenu = new Button(I18N.CONSTANTS.newText(), IconImageBundle.ICONS.add());
        newButtonMenu.setMenu(newMenu);
        newButtonMenu.setEnabled(db.isDesignAllowed());
        toolBar.add(newButtonMenu);

        toolBar.addDeleteButton();


    }

    protected void initNewMenu(Menu menu, SelectionListener<MenuEvent> listener) {

        MenuItem newActivity = new MenuItem(I18N.CONSTANTS.newActivity(), IconImageBundle.ICONS.activity(), listener);
        newActivity.setItemId("Activity");
        menu.add(newActivity);

        final MenuItem newAttributeGroup = new MenuItem(I18N.CONSTANTS.newAttributeGroup(), IconImageBundle.ICONS.attributeGroup(), listener);
        newAttributeGroup.setItemId("AttributeGroup");
        menu.add(newAttributeGroup);

        final MenuItem newAttribute = new MenuItem(I18N.CONSTANTS.newAttribute(), IconImageBundle.ICONS.attribute(), listener);
        newAttribute.setItemId("Attribute");
        menu.add(newAttribute);

        final MenuItem newIndicator = new MenuItem(I18N.CONSTANTS.newIndicator(), IconImageBundle.ICONS.indicator(), listener);
        newIndicator.setItemId("Indicator");
        menu.add(newIndicator);


        menu.addListener(Events.BeforeShow, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {

                ModelData sel = getSelection();

                newAttributeGroup.setEnabled(sel != null);
                newAttribute.setEnabled(sel instanceof AttributeGroupDTO || sel instanceof AttributeDTO);
                newIndicator.setEnabled(sel != null);
            }
        });
    }

    protected void initRemoveMenu(Menu menu) {
        final MenuItem removeItem = new MenuItem(I18N.CONSTANTS.delete(), IconImageBundle.ICONS.delete());
        removeItem.setItemId(UIActions.delete);
        menu.add(removeItem);

    }

    protected Menu createContextMenu() {
        Menu menu = new Menu();

        initNewMenu(menu, null);
        menu.add(new SeparatorMenuItem());
        initRemoveMenu(menu);

        return menu;
    }


    private void createFormContainer() {
        formContainer = new ContentPanel();
        formContainer.setHeaderVisible(false);
        formContainer.setBorders(false);
        formContainer.setFrame(false);

        BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.EAST);
        layout.setSplit(true);
        layout.setCollapsible(true);
        layout.setSize(375);
        layout.setMargins(new Margins(0, 0, 0, 5));

        add(formContainer, layout);
    }

    private ColumnModel createColumnModel() {

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);

        ColumnConfig nameColumn = new ColumnConfig("name", I18N.CONSTANTS.name(), 150);
        nameColumn.setEditor(new CellEditor(nameField));
        nameColumn.setRenderer(new TreeGridCellRenderer());

        columns.add(nameColumn);

        return new ColumnModel(columns);
    }

    protected Class formClassForSelection(ModelData sel) {

        if (sel instanceof ActivityDTO) {
            return ActivityForm.class;
        } else if (sel instanceof AttributeGroupDTO) {
            return AttributeGroupForm.class;
        } else if (sel instanceof IndicatorDTO) {
            return IndicatorForm.class;
        }

        return null;

    }

    protected AbstractDesignForm createForm(ModelData sel) {
        if (sel instanceof ActivityDTO) {
            return new ActivityForm(service, db);
        } else if (sel instanceof AttributeGroupDTO) {
            return new AttributeGroupForm();
        } else if (sel instanceof AttributeDTO) {
            return new AttributeForm();
        } else if (sel instanceof IndicatorDTO) {
            return new IndicatorForm();
        }

        return null;
    }


    public void showForm(ModelData model) {

        // do we have the right form?
        Class formClass = formClassForSelection(model);

        AbstractDesignForm currentForm = null;
        if (formContainer.getItemCount() != 0) {
            currentForm = (AbstractDesignForm) formContainer.getItem(0);
        }

        if (formClass == null) {
            if (currentForm != null) {
                currentForm.getBinding().unbind();
                formContainer.removeAll();
            }
            return;
        } else {

            if (currentForm == null ||
                    (currentForm != null && !formClass.equals(currentForm.getClass()))) {

                if (currentForm != null) {
                    formContainer.removeAll();
                    currentForm.getBinding().unbind();
                }

                currentForm = createForm(model);
                currentForm.setReadOnly(!db.isDesignAllowed());
                currentForm.setHeaderVisible(false);
                currentForm.setBorders(false);
                currentForm.setFrame(false);
                currentForm.getBinding().setStore(tree.getStore());
                formContainer.add(currentForm);
                formContainer.layout();
            }
        }
        currentForm.getBinding().bind(model);
    }

    public FormDialogTether showNewForm(EntityDTO entity, FormDialogCallback callback) {

        AbstractDesignForm form = createForm(entity);
        form.getBinding().bind(entity);

        for (FieldBinding field : form.getBinding().getBindings()) {
            field.getField().clearInvalid();
        }

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(form.getPreferredDialogWidth());
        dlg.setHeight(form.getPreferredDialogHeight());
        dlg.setScrollMode(Style.Scroll.AUTOY);

        if (entity instanceof ActivityDTO) {
            dlg.setHeading(I18N.CONSTANTS.newActivity());
        } else if (entity instanceof AttributeGroupDTO) {
            dlg.setHeading(I18N.CONSTANTS.newAttributeGroup());
        } else if (entity instanceof AttributeDTO) {
            dlg.setHeading(I18N.CONSTANTS.newAttribute());
        } else if (entity instanceof IndicatorDTO) {
            dlg.setHeading(I18N.CONSTANTS.newIndicator());
        }

        dlg.show(callback);

        return dlg;
    }
}
