package org.activityinfo.client.page.config;

import org.activityinfo.client.Application;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.common.dialog.FormDialogCallback;
import org.activityinfo.client.common.dialog.FormDialogImpl;
import org.activityinfo.client.common.dialog.FormDialogTether;
import org.activityinfo.client.common.grid.AbstractEditorGridView;
import org.activityinfo.client.page.config.form.UserForm;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserModel;

import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.GridEvent;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class DbUserGrid extends AbstractEditorGridView<UserModel, DbUserEditor>
        implements DbUserEditor.View {

    protected EditorGrid<UserModel> grid;
    protected UserDatabaseDTO db;

    public DbUserGrid() {
        setHeading(Application.CONSTANTS.users());
        setLayout(new FitLayout());
    }

    public void init(DbUserEditor presenter, UserDatabaseDTO db, ListStore<UserModel> store) {
        super.init(presenter, store);
        this.db = db;
        this.setHeading(db.getName() + " - " + Application.CONSTANTS.users());
    }


    @Override
    protected Grid<UserModel> createGridAndAddToContainer(Store store) {

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
        
        CheckColumnConfig allowViewAll = new CheckColumnConfig("allowViewAll", Application.CONSTANTS.allowViewAll(), 75);
        allowViewAll.setToolTip(Application.CONSTANTS.allowViewAllLong());
        columns.add(allowViewAll);

        CheckColumnConfig allowEditAll = new CheckColumnConfig("allowEditAll", Application.CONSTANTS.allowEditAll(), 75);
        allowEditAll.setToolTip(Application.CONSTANTS.allowEditAllLong());
        columns.add(allowEditAll);

        CheckColumnConfig allowDesign = new CheckColumnConfig("allowDesign", Application.CONSTANTS.allowDesign(), 75);
        allowDesign.setToolTip(Application.CONSTANTS.allowDesignLong());
        columns.add(allowDesign);

        final ListStore<UserModel> listStore = (ListStore) store;
        grid = new EditorGrid<UserModel>(listStore, new ColumnModel(columns));
        grid.setLoadMask(true);
        grid.addPlugin(allowDesign);
     //   grid.addPlugin(allowView);
        grid.addPlugin(allowViewAll);
        grid.addPlugin(allowEdit);
        grid.addPlugin(allowEditAll);
        grid.addListener(Events.CellClick, new Listener<GridEvent>() {

            public void handleEvent(GridEvent ge) {
               
                if(ge.getColIndex() >= 4) {

                    String property = columns.get(ge.getColIndex()).getDataIndex();
                    Record record = listStore.getRecord(listStore.getAt(ge.getRowIndex()));
                    presenter.onRowEdit(property, (Boolean)record.get(property), record);
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

    public FormDialogTether showNewForm(UserModel user, FormDialogCallback callback) {

        UserForm form = new UserForm(db);
        form.getBinding().bind(user);

        FormDialogImpl dlg=  new FormDialogImpl(form);
        dlg.setHeading(Application.CONSTANTS.newUser());
        dlg.setWidth(400);
        dlg.setHeight(300);

        dlg.show(callback);

        return dlg;
    }
}
