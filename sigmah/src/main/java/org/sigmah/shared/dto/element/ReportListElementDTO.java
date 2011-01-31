/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.ui.FlexibleGrid;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.report.ReportReference;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ProjectDTO;

/**
 * Flexible element that can contain a list of project reports.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ReportListElementDTO extends FlexibleElementDTO {
    
    public Integer getModelId() {
        return get("modelId");
    }

    public void setModelId(Integer modelId) {
        set("modelId", modelId);
    }

    @Override
    protected Component getComponent(ValueResult valueResult, boolean enabled) {
        final ContentPanel component = new ContentPanel();
        component.setHeading(getLabel());

        // Setting up the report store
        final List<?> reports = valueResult.getValuesObject();
        
        final ListStore<ReportReference> store = new ListStore<ReportReference>();
        if(reports != null)
            store.add((List<ReportReference>) reports);

        // Creating the toolbar
        component.setTopComponent(createToolbar(enabled, store));

        // Creating the grid
        final FlexibleGrid<ReportReference> reportGrid = new FlexibleGrid<ReportReference>(store, null, createColumnModel(enabled));
        reportGrid.setAutoExpandColumn("name");
        reportGrid.setVisibleElementsCount(5);

        component.add(reportGrid);

        return component;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {
        final List<?> reports = result.getValuesObject();
        return reports != null && !reports.isEmpty();
    }

    @Override
    public String getEntityName() {
        return "element.ReportListElement";
    }

    private PageState createPageState(final Integer reportId) {
        // Report & Document path
        final PageState targetState;

        if(currentContainerDTO instanceof ProjectDTO) {
            // This element is displayed in a project
            final ProjectState state = new ProjectState(currentContainerDTO.getId());
            state.setCurrentSection(ProjectPresenter.REPORT_TAB_INDEX);
            state.setArgument(reportId.toString());
            targetState = state;

        } else {
            Log.debug("ReportElementDTO does not know how to render properly from a '"+currentContainerDTO.getClass()+"' container.");
            targetState = null;
        }

        return targetState;
    }

    /**
     * Creates and configure the column model for the grid contained in this component.
     * @param enabled <code>true</code> to enable the delete column,
     *                <code>false</code> to disable it.
     * @return A new array of column configs.
     */
    private ColumnConfig[] createColumnModel(final boolean enabled) {
        // Creating columns
        final ColumnConfig lastEditDateColumn = new ColumnConfig("lastEditDate", I18N.CONSTANTS.reportLastEditDate(), 60);
        final ColumnConfig nameColumn = new ColumnConfig("name", I18N.CONSTANTS.reportName(), 100);
        final ColumnConfig editorNameColumn = new ColumnConfig("editorName", I18N.CONSTANTS.reportEditor(), 100);
        final ColumnConfig deleteColumn = new ColumnConfig("delete", "", 10);

        // Date column specificities
        lastEditDateColumn.setDateTimeFormat(DateTimeFormat.getShortDateFormat());

        // Name column specificities
        nameColumn.setRenderer(new GridCellRenderer<ReportReference>() {
            @Override
            public Object render(final ReportReference model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                final Anchor anchor = new Anchor((String) model.get(property));
                anchor.addStyleName("flexibility-link");
                anchor.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, createPageState(model.getId())));
                    }
                });
                return anchor;
            }
        });

        // Delete column specificities
        deleteColumn.setSortable(false);
        deleteColumn.setRenderer(new GridCellRenderer<ReportReference>() {
            @Override
            public Object render(final ReportReference model, String property, ColumnData config, int rowIndex, int colIndex, final ListStore store, Grid grid) {
                if(enabled) {
                    final Image image = IconImageBundle.ICONS.remove().createImage();
                    image.setTitle(I18N.CONSTANTS.remove());
                    image.addStyleName("flexibility-action");

                    // Action
                    image.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            MessageBox.confirm(I18N.CONSTANTS.remove(), I18N.MESSAGES.reportRemoveConfirm(model.getName()), new Listener<MessageBoxEvent>() {
                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    if(Dialog.YES.equals(be.getButtonClicked().getItemId())) {
                                        // TODO: Delete the report
                                        Log.debug("Removing '"+model.getName()+"'...");
                                        final Delete delete = new Delete("report.ProjectReport", model.getId());
                                        dispatcher.execute(delete, null, new AsyncCallback<VoidResult>() {

                                            @Override
                                            public void onSuccess(VoidResult result) {
                                                store.remove(model);
                                                Notification.show("OK", "OK");
                                            }

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                MessageBox.alert("ERROR", "ERROR", null);
                                            }
                                            
                                        });
                                    }
                                }
                            });
                        }
                    });

                    return image;
                } else {
                    return "-";
                }
            }
        });

        return new ColumnConfig[] {lastEditDateColumn, nameColumn, editorNameColumn, deleteColumn};
    }

    /**
     * Creates the toolbar of this component.
     * @param enabled <code>true</code> to enable the buttons of this toolbar,
     *                <code>false</code> to disable them.
     * @return A new toolbar.
     */
    private ToolBar createToolbar(final boolean enabled, final ListStore<ReportReference> store) {
        final ToolBar toolbar = new ToolBar();

        // Creating buttons
        final Button createReportButton = new Button(I18N.CONSTANTS.reportCreateReport(), IconImageBundle.ICONS.add());

        // "Create" button action
        createReportButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                MessageBox.prompt(I18N.CONSTANTS.reportCreateReport(), I18N.CONSTANTS.reportName(), new Listener<MessageBoxEvent>() {

                    @Override
                    public void handleEvent(MessageBoxEvent be) {
                        if(Dialog.OK.equals(be.getButtonClicked().getItemId())) {
                            final String name = be.getValue();

                            final HashMap<String, Serializable> properties = new HashMap<String, Serializable>();
                            properties.put("name", name);
                            properties.put("flexibleElementId", getId());
                            properties.put("reportModelId", getModelId());
                            properties.put("containerId", currentContainerDTO.getId());
                            properties.put("projectId", currentContainerDTO.getId());
                            properties.put("multiple", true);

                            if(currentContainerDTO instanceof ProjectDTO)
                                properties.put("phaseName", ((ProjectDTO)currentContainerDTO).getCurrentPhaseDTO().getPhaseModelDTO().getName());

                            final CreateEntity createEntity = new CreateEntity("ProjectReport", properties);

                            dispatcher.execute(createEntity, null, new AsyncCallback<CreateResult>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    MessageBox.alert(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateError(), null);
                                }

                                @Override
                                public void onSuccess(CreateResult result) {
                                    final ReportReference reference = new ReportReference();
                                    reference.setId(result.getNewId());
                                    reference.setName(name);
                                    reference.setLastEditDate(new Date());
                                    reference.setEditorName(authentication.getUserShortName());
                                    store.add(reference);

                                    Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateSuccess());
                                }

                            });
                        }
                    }
                });
            }
        });

        // Enabling / desabling buttons
        createReportButton.setEnabled(enabled);

        // Adding buttons to the toolbar
        toolbar.add(createReportButton);

        return toolbar;
    }

}
