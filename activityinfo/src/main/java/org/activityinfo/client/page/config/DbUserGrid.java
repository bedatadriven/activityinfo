package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractEditorGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.UserForm;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class DbUserGrid extends AbstractEditorGridView<UserPermissionDTO, DbUserEditor>
        implements DbUserEditor.View {

    protected EditorGrid<UserPermissionDTO> grid;
    protected UserDatabaseDTO db;

    public DbUserGrid() {
        setHeading(Application.CONSTANTS.users());
        setLayout(new FitLayout());
    }

    public void init(DbUserEditor presenter, UserDatabaseDTO db, ListStore<UserPermissionDTO> store) {
        this.db = db;
        super.init(presenter, store);
        this.setHeading(db.getName() + " - " + Application.CONSTANTS.users());
    }


    @Override
    protected Grid<UserPermissionDTO> createGridAndAddToContainer(Store store) {

        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig name = new ColumnConfig("name", Application.CONSTANTS.name(), 100);
        columns.add(name);

        ColumnConfig email = new ColumnConfig("email", Application.CONSTANTS.email(), 150);
        columns.add(email);

        ColumnConfig partner = new ColumnConfig("partner", Application.CONSTANTS.partner(), 150);
        columns.add(partner);

        CheckColumnConfig allowView = new CheckColumnConfig("allowViewSimple", Application.CONSTANTS.allowView(), 75);
        allowView.setDataIndex("allowView");
        allowView.setToolTip(Application.CONSTANTS.allowViewLong());
        columns.add(allowView);

        CheckColumnConfig allowEdit = new CheckColumnConfig("allowEditSimple", Application.CONSTANTS.allowEdit(), 75);
        allowEdit.setDataIndex("allowEdit");
        allowEdit.setToolTip(Application.CONSTANTS.allowEditLong());
        columns.add(allowEdit);

        // only users with the right to manage all users can view/change these attributes
        CheckColumnConfig allowViewAll = null;
        CheckColumnConfig allowEditAll = null;

        if (db.isManageAllUsersAllowed()) {
            allowViewAll = new CheckColumnConfig("allowViewAll", Application.CONSTANTS.allowViewAll(), 75);
            allowViewAll.setToolTip(Application.CONSTANTS.allowViewAllLong());
            columns.add(allowViewAll);

            allowEditAll = new CheckColumnConfig("allowEditAll", Application.CONSTANTS.allowEditAll(), 75);
            allowEditAll.setToolTip(Application.CONSTANTS.allowEditAllLong());
            columns.add(allowEditAll);
        }

        CheckColumnConfig allowManageUsers = null;
        if (db.isManageUsersAllowed()) {
            allowManageUsers = new CheckColumnConfig("allowManageUsers", Application.CONSTANTS.allowManageUsers(), 150);
            columns.add(allowManageUsers);
        }

        CheckColumnConfig allowManageAllUsers = null;
        if (db.isManageAllUsersAllowed()) {
            allowManageAllUsers = new CheckColumnConfig("allowManageAllUsers", Application.CONSTANTS.manageAllUsers(), 150);
            columns.add(allowManageAllUsers);
        }

        // only users with the right to design them selves can change the design attribute
        CheckColumnConfig allowDesign = null;
        if (db.isDesignAllowed()) {
            allowDesign = new CheckColumnConfig("allowDesign", Application.CONSTANTS.allowDesign(), 75);
            allowDesign.setToolTip(Application.CONSTANTS.allowDesignLong());
            columns.add(allowDesign);
        }

        final ListStore<UserPermissionDTO> listStore = (ListStore) store;
        grid = new EditorGrid<UserPermissionDTO>(listStore, new ColumnModel(columns));
        grid.setLoadMask(true);
        if (allowDesign != null) grid.addPlugin(allowDesign);
        //   grid.addPlugin(allowView);
        if (allowViewAll != null) grid.addPlugin(allowViewAll);
        grid.addPlugin(allowEdit);
        if (allowEditAll != null) grid.addPlugin(allowEditAll);
        if (allowManageUsers != null) grid.addPlugin(allowManageUsers);
        if (allowManageAllUsers != null) grid.addPlugin(allowManageAllUsers);
        grid.addListener(Events.CellClick, new Listener<GridEvent>() {

            public void handleEvent(GridEvent ge) {

                if (ge.getColIndex() >= 4) {

                    String property = columns.get(ge.getColIndex()).getDataIndex();
                    Record record = listStore.getRecord(listStore.getAt(ge.getRowIndex()));
                    presenter.onRowEdit(property, (Boolean) record.get(property), record);
                }
            }
        });

        add(grid);

        return grid;
    }

    @Override
    protected void initToolBar() {
        toolBar.addSaveSplitButton();
        toolBar.addButton(UIActions.add, Application.CONSTANTS.addUser(), Application.ICONS.addUser());
        toolBar.addButton(UIActions.delete, Application.CONSTANTS.delete(), Application.ICONS.deleteUser());
    }

    public FormDialogTether showNewForm(UserPermissionDTO user, FormDialogCallback callback) {

        UserForm form = new UserForm(db);
        form.getBinding().bind(user);

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setHeading(Application.CONSTANTS.newUser());
        dlg.setWidth(400);
        dlg.setHeight(300);

        dlg.show(callback);

        return dlg;
    }
}
