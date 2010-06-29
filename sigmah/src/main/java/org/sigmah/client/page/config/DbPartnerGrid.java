/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractGridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.form.PartnerForm;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class DbPartnerGrid extends AbstractGridView<PartnerDTO, DbPartnerEditor>
                            implements DbPartnerEditor.View {

    private final UIConstants messages;
    private final IconImageBundle icons;

    private Grid<PartnerDTO> grid;
    private ListStore<PartnerDTO> store;

    @Inject
    public DbPartnerGrid(UIConstants messages, IconImageBundle icons) {
        this.messages = messages;
        this.icons = icons;
    }

    public void init(DbPartnerEditor editor, UserDatabaseDTO db, ListStore<PartnerDTO> store) {
        super.init(editor, store);
        this.setHeading(db.getName() + " - " + messages.partners());

    }

    @Override
    protected Grid<PartnerDTO> createGridAndAddToContainer(Store store) {
        grid = new Grid<PartnerDTO>((ListStore)store, createColumnModel());
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

    public FormDialogTether showAddDialog(PartnerDTO partner, FormDialogCallback callback) {

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
