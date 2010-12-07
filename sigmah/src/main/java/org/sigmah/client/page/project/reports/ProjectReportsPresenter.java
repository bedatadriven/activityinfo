/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.reports;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.shared.command.GetProjectReport;
import org.sigmah.shared.command.GetProjectReports;
import org.sigmah.shared.command.result.ProjectReportListResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.report.ProjectReportDTO;

/**
 * Sub presenter that manage the "reports" view from the project page.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportsPresenter implements SubPresenter {
    private Dispatcher dispatcher;
    private EventBus eventBus;
    private Authentication authentication;

    private ProjectPresenter projectPresenter;
    private ProjectDTO currentProjectDTO;

    private ProjectReportsView view;
    private ListStore<GetProjectReports.ReportReference> reportStore;
    private boolean reportStoreNeedsRefresh = false;

    int currentReportId = -1;

    public ProjectReportsPresenter(Authentication authentication, Dispatcher dispatcher, EventBus eventBus,
            ProjectPresenter projectPresenter) {
        this.authentication = authentication;
        this.dispatcher = dispatcher;
        this.eventBus = eventBus;
        this.projectPresenter = projectPresenter;
    }

    @Override
    public Component getView() {
        // Creates the view
        if (view == null) {
            reportStore = new ListStore<GetProjectReports.ReportReference>();
            reportStore.setMonitorChanges(true);
            view = new ProjectReportsView(eventBus, dispatcher, reportStore);
        }

        // Calculating the report id
        int reportId = currentReportId;
        final String arg = projectPresenter.getCurrentState().getArgument();
        if (arg != null)
            reportId = Integer.parseInt(arg);

        if (!projectPresenter.getCurrentProjectDTO().equals(currentProjectDTO)) {
            // If the current project has changed, clear the view
            currentProjectDTO = projectPresenter.getCurrentProjectDTO();
            reportStore.removeAll();

            if (arg == null)
                reportId = -1;
        }

        // If the report id has changed
        if (currentReportId != reportId) {
            currentReportId = reportId;

            reportStoreNeedsRefresh = true;

            // Configuring the view to display the given report
            Log.debug("Loading report #" + reportId);
            final GetProjectReport getProjectReport = new GetProjectReport(reportId);
            dispatcher.execute(getProjectReport, null, new AsyncCallback<ProjectReportDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void onSuccess(ProjectReportDTO result) {
                    view.setReport(result);
                }
            });
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // Updating the current state
        view.setCurrentState(projectPresenter.getCurrentState());
        view.setPhaseName(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO().getPhaseModelDTO().getName());

        // Reset the attach documents menu.
        new AttachDocumentMenu(authentication, projectPresenter.getCurrentProjectDTO(), view.getAttachButton());

        if (reportStore.getCount() == 0 || reportStoreNeedsRefresh) {
            GetProjectReports getProjectReports = new GetProjectReports(currentProjectDTO.getId());
            dispatcher.execute(getProjectReports, null, new AsyncCallback<ProjectReportListResult>() {
                @Override
                public void onSuccess(ProjectReportListResult result) {
                    if (reportStoreNeedsRefresh) {
                        reportStore.removeAll();
                        reportStoreNeedsRefresh = false;
                    }

                    reportStore.add(result.getData());
                }

                @Override
                public void onFailure(Throwable caught) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
    }
}
