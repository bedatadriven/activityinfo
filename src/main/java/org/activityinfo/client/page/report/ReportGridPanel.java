package org.activityinfo.client.page.report;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Arrays;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.DeleteReport;
import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.UpdateReportSubscription;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ReportMetadataDTO;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReportGridPanel extends ContentPanel implements ActionListener {
    private Dispatcher dispatcher;
    private ListStore<ReportMetadataDTO> store;
    private EventBus eventBus;
    private EditorGrid<ReportMetadataDTO> grid;
    private ActionToolBar toolBar;

    public ReportGridPanel(final EventBus eventBus, Dispatcher dispatcher) {
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;

        setHeading("Saved Reports");

        toolBar = new ActionToolBar(this);
        toolBar.addButton(UIActions.PRINT, I18N.CONSTANTS.open(),
            IconImageBundle.ICONS.pdf());
        toolBar.addDeleteButton();
        toolBar.addButton("share", I18N.CONSTANTS.sharingOptions(),
            IconImageBundle.ICONS.group());
        toolBar.addButton("email", I18N.CONSTANTS.emailOptions(),
            IconImageBundle.ICONS.email());

        setTopComponent(toolBar);

        ListLoader<ReportsResult> loader = new BaseListLoader<ReportsResult>(
            new ReportsProxy(dispatcher));
        store = new ListStore<ReportMetadataDTO>(loader);

        grid = new EditorGrid<ReportMetadataDTO>(store, createColumnModel());
        grid.setStripeRows(true);
        grid.setAutoExpandColumn("title");
        grid.setSelectionModel(new GridSelectionModel<ReportMetadataDTO>());
        grid.setClicksToEdit(ClicksToEdit.ONE);
        grid.setLoadMask(true);
        grid.addListener(Events.CellDoubleClick,
            new Listener<GridEvent<ReportMetadataDTO>>() {

                @Override
                public void handleEvent(GridEvent<ReportMetadataDTO> be) {
                    open(be.getModel());
                }
            });
        grid.addListener(Events.CellClick,
            new Listener<GridEvent<ReportMetadataDTO>>() {

                @Override
                public void handleEvent(GridEvent<ReportMetadataDTO> event) {
                    if (event.getColIndex() == 0) {
                        onDashboardClicked(event.getModel());
                    }
                }
            });
        grid.getSelectionModel().addSelectionChangedListener(
            new SelectionChangedListener<ReportMetadataDTO>() {

                @Override
                public void selectionChanged(
                    SelectionChangedEvent<ReportMetadataDTO> se) {
                    onSelectionChanged(se.getSelectedItem());
                }
            });

        setLayout(new FitLayout());
        add(grid);

        updateToolbarState(null);

        loader.load();
    }

    private void onSelectionChanged(ReportMetadataDTO selectedItem) {
        updateToolbarState(selectedItem);
    }

    private void updateToolbarState(ReportMetadataDTO selectedItem) {
        toolBar.setActionEnabled(UIActions.DELETE, selectedItem != null &&
            selectedItem.getAmOwner());
        toolBar.setActionEnabled(UIActions.PRINT, selectedItem != null);
        toolBar.setActionEnabled("email", selectedItem != null);
    }

    private ColumnModel createColumnModel() {
        ColumnConfig dashboard = new ColumnConfig("dashboard", "", 28);
        dashboard.setHeader(IconImageBundle.ICONS.star().getHTML());
        dashboard.setResizable(false);
        dashboard.setMenuDisabled(true);
        dashboard.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

            @Override
            public Object render(ReportMetadataDTO model, String property,
                ColumnData config, int rowIndex, int colIndex,
                ListStore<ReportMetadataDTO> store, Grid<ReportMetadataDTO> grid) {

                return "<div style='cursor:pointer'>"
                    + (model.isDashboard() ? IconImageBundle.ICONS.star()
                        .getHTML() :
                        IconImageBundle.ICONS.starWhite().getHTML()) + "</div>";

            }
        });

        ColumnConfig title = new ColumnConfig("title", I18N.CONSTANTS.title(),
            150);
        ColumnConfig owner = new ColumnConfig("ownerName",
            I18N.CONSTANTS.ownerName(), 100);
        owner.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

            @Override
            public Object render(ReportMetadataDTO model, String property,
                ColumnData config, int rowIndex, int colIndex,
                ListStore<ReportMetadataDTO> store,
                Grid<ReportMetadataDTO> grid) {

                if (model.getAmOwner()) {
                    return "Me";
                } else {
                    return model.getOwnerName();
                }
            }
        });

        ColumnConfig email = new ColumnConfig("email",
            I18N.CONSTANTS.emailSubscription(), 100);
        email.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

            @Override
            public Object render(ReportMetadataDTO model, String property,
                ColumnData config, int rowIndex, int colIndex,
                ListStore<ReportMetadataDTO> store, Grid<ReportMetadataDTO> grid) {

                switch (model.getEmailDelivery()) {
                case NONE:
                    return "<i>" + I18N.CONSTANTS.none() + "</i>";
                case MONTHLY:
                    return I18N.CONSTANTS.monthly();
                case WEEKLY:
                default:
                    return I18N.CONSTANTS.weekly();
                }
            }
        });

        return new ColumnModel(Arrays.asList(dashboard, title, owner, email));
    }

    private class ReportsProxy extends RpcProxy<ReportsResult> {

        private final Dispatcher dispatcher;

        public ReportsProxy(Dispatcher dispatcher) {
            super();
            this.dispatcher = dispatcher;
        }

        @Override
        protected void load(Object loadConfig,
            final AsyncCallback<ReportsResult> callback) {

            dispatcher.execute(new GetReports(),
                new AsyncCallback<ReportsResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(ReportsResult result) {
                        callback.onSuccess(result);
                    }
                });
        }
    }

    private void onDashboardClicked(final ReportMetadataDTO model) {
        model.setDashboard(!model.isDashboard());
        store.update(model);

        UpdateReportSubscription update = new UpdateReportSubscription();
        update.setReportId(model.getId());
        update.setPinnedToDashboard(model.isDashboard());

        dispatcher.execute(update, new AsyncCallback<VoidResult>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(VoidResult result) {
                Info.display(
                    I18N.CONSTANTS.saved(),
                    model.isDashboard() ? I18N.MESSAGES.addedToDashboard(model
                        .getTitle()) :
                        I18N.MESSAGES.removedFromDashboard(model.getTitle()));
            }
        });

    }

    @Override
    public void onUIAction(String actionId) {
        if (UIActions.PRINT.equals(actionId)) {
            open(grid.getSelectionModel().getSelectedItem());
        } else if ("email".equals(actionId)) {
            showEmailDialog();
        } else if ("share".equals(actionId)) {
            ShareReportDialog dialog = new ShareReportDialog(dispatcher);
            dialog.show(grid.getSelectionModel().getSelectedItem());
        } else if (UIActions.DELETE.equals(actionId)) {
            delete();
        }
    }

    private void showEmailDialog() {
        EmailDialog dialog = new EmailDialog(dispatcher);
        final ReportMetadataDTO selected = grid.getSelectionModel()
            .getSelectedItem();
        dialog.show(selected, new EmailDialog.Callback() {

            @Override
            public void onUpdated() {
                store.update(selected);
            }
        });
    }

    private void open(ReportMetadataDTO model) {
        eventBus.fireEvent(new NavigationEvent(
            NavigationHandler.NAVIGATION_REQUESTED,
            new ReportDesignPageState(model.getId())));
    }

    private void delete() {
        final ReportMetadataDTO report = grid.getSelectionModel()
            .getSelectedItem();
        MessageBox.confirm(I18N.CONSTANTS.delete(),
            I18N.MESSAGES.confirmDeleteReport(report.getTitle()),
            new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                        dispatcher.execute(new DeleteReport(report.getId()),
                            new MaskingAsyncMonitor(ReportGridPanel.this,
                                I18N.CONSTANTS.delete()),
                            new AsyncCallback<VoidResult>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    // handled by monitor
                                }

                                @Override
                                public void onSuccess(VoidResult result) {
                                    grid.getStore().remove(report);
                                }
                            });
                    }
                }
            });

    }
}
