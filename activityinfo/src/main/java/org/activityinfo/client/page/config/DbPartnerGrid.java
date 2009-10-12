package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.PartnerForm;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.i18n.UIConstants;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class DbPartnerGrid extends AbstractGridView<PartnerModel, DbPartnerEditor>
                            implements DbPartnerEditor.View {

    private final UIConstants messages;
    private final IconImageBundle icons;

    private Grid<PartnerModel> grid;
    private ListStore<PartnerModel> store;

    @Inject
    public DbPartnerGrid(UIConstants messages, IconImageBundle icons) {
        this.messages = messages;
        this.icons = icons;
    }

    public void init(DbPartnerEditor editor, UserDatabaseDTO db, ListStore<PartnerModel> store) {
        super.init(editor, store);
        this.setHeading(db.getName() + " - " + messages.partners());

    }

    @Override
    protected Grid<PartnerModel> createGridAndAddToContainer(Store store) {
        grid = new Grid<PartnerModel>((ListStore)store, createColumnModel());
        grid.setAutoExpandColumn("fullName");
        grid.setLoadMask(true);

        this.setLayout(new FitLayout());
        this.add(grid);


        return grid;
    }

    protected ColumnModel createColumnModel() {

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", messages.name(), 150));
        columns.add(new ColumnConfig("fullName", messages.fullName(), 300));

        return new ColumnModel(columns);

    }

    @Override
    protected void initToolBar() {
        toolBar.addButton(UIActions.add, messages.addPartner(), icons.add());
        toolBar.addButton(UIActions.delete, messages.delete(), icons.delete());
    }

    public FormDialogTether showAddDialog(PartnerModel partner, FormDialogCallback callback) {

        PartnerForm form = new PartnerForm();
        form.getBinding().bind(partner);

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(450);
        dlg.setHeight(300);
        dlg.setHeading(messages.newPartner());

        dlg.show(callback);

        return dlg;
    }
}
