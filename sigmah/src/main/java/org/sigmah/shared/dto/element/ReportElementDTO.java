/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.page.project.reports.EditReportDialog;
import org.sigmah.client.ui.WidgetField;
import org.sigmah.shared.command.GetProjectReports;
import org.sigmah.shared.command.result.ProjectReportListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.ProjectDTO;

/**
 * Field that can hold a project report.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ReportElementDTO extends FlexibleElementDTO {
    private static final long serialVersionUID = 1L;

    public Integer getModelId() {
        return get("modelId");
    }

    public void setModelId(Integer modelId) {
        set("modelId", modelId);
    }

    @Override
    public Component getComponent(ValueResult valueResult, boolean enabled) {
        final Button button = new Button();
        final WidgetField<Button, String> field = new WidgetField<Button, String>(button);

        if(valueResult != null && valueResult.isValueDefined()) {
            // If a report is attached to this element
            button.setText(I18N.CONSTANTS.reportOpenReport());

            final String reportId = (String) valueResult.getValueObject();

            // Retrieving the name of the report attached to this element
            final GetProjectReports getProjectReports = new GetProjectReports();
            getProjectReports.setReportId(Integer.parseInt(reportId));

            dispatcher.execute(getProjectReports, null, new AsyncCallback<ProjectReportListResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    // No action
                }

                @Override
                public void onSuccess(ProjectReportListResult result) {
                    final List<GetProjectReports.ReportReference> results = result.getData();

                    if(results.size() == 1)
                        button.setText(I18N.MESSAGES.reportOpenReport(results.get(0).getName()));
                }
            });

            // Report & Document path
            final PageState targetState;

            if(currentContainerDTO instanceof ProjectDTO) {
                // This element is displayed in a project
                final ProjectState state = new ProjectState(currentContainerDTO.getId());
                state.setCurrentSection(ProjectPresenter.REPORT_TAB_INDEX);
                state.setArgument(reportId);
                targetState = state;

            } else {
                Log.debug("ReportElementDTO does not know how to render properly from a '"+currentContainerDTO.getClass()+"' container.");
                targetState = null;
            }

            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, targetState));
                }
            });
            
        } else {
            // New report button
            final IconImageBundle imageBundle = GWT.create(IconImageBundle.class);

            final Image addIcon = imageBundle.add().createImage();
            addIcon.getElement().getStyle().setVerticalAlign(VerticalAlign.TEXT_TOP);
            button.setHTML(addIcon.getElement().getString()+' '+I18N.CONSTANTS.reportCreateReport());

            // Report & Document path
            if(currentContainerDTO instanceof ProjectDTO) {
                // This element is displayed in a project
                final ProjectDTO projectDTO = (ProjectDTO) currentContainerDTO;

                final ProjectState state = new ProjectState(currentContainerDTO.getId());
                state.setCurrentSection(ProjectPresenter.REPORT_TAB_INDEX);
                state.setArgument((String) valueResult.getValueObject());

                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        final HashMap<String, Serializable> properties = new HashMap<String, Serializable>();
                        properties.put("flexibleElementId", getId());
                        properties.put("containerId", currentContainerDTO.getId());
                        properties.put("reportModelId", getModelId());
                        properties.put("phaseName", projectDTO.getCurrentPhaseDTO().getPhaseModelDTO().getName());
                        properties.put("projectId", projectDTO.getId());

                        EditReportDialog.getDialog(properties, state, eventBus, dispatcher).show();
                    }
                });
                
            } else {
                Log.debug("ReportElementDTO does not know how to render properly from the '"+History.getToken()+"' page.");
            }

        }

        field.setFieldLabel(getLabel());
        field.setEnabled(enabled);

        return field;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {
        return result != null && result.isValueDefined();
    }

    @Override
    public String getEntityName() {
        return "element.ReportElement";
    }

}
