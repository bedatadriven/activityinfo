package org.sigmah.client.page.project.logframe;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.Presenter;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.util.NotImplementedMethod;
import org.sigmah.shared.command.UpdateLogFrame;
import org.sigmah.shared.command.result.LogFrameResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.logframe.LogFrameDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter for the project log frame.
 * 
 * @author tmi
 * 
 */
public class ProjectLogFramePresenter implements Presenter {

    /**
     * Description of the view managed by this presenter.
     */
    public static abstract class View extends LayoutContainer {

        public abstract ProjectLogFrameGrid getLogFrameGrid();

        public abstract Button getSaveButton();

        public abstract Button getCopyButton();

        public abstract Button getPasteButton();

        public abstract Button getWordExportButton();

        public abstract Button getExcelExportButton();

        public abstract TextField<String> getLogFrameTitleTextBox();

        public abstract TextField<String> getLogFrameMainObjectiveTextBox();
    }

    /**
     * This presenter view.
     */
    private View view;

    /**
     * The dispatcher.
     */
    private final Dispatcher dispatcher;

    /**
     * The main project presenter.
     */
    private final ProjectPresenter projectPresenter;

    /**
     * The current displayed project.
     */
    private ProjectDTO currentProjectDTO;

    /**
     * The displayed log frame.
     */
    private LogFrameDTO logFrame;

    public ProjectLogFramePresenter(Dispatcher dispatcher, ProjectPresenter projectPresenter) {
        this.dispatcher = dispatcher;
        this.projectPresenter = projectPresenter;
        this.currentProjectDTO = projectPresenter.getCurrentProjectDTO();
    }

    @Override
    public Component getView() {

        if (view == null) {
            view = new ProjectLogFrameView();
            logFrame = projectPresenter.getCurrentProjectDTO().getLogFrameDTO();
            currentProjectDTO = projectPresenter.getCurrentProjectDTO();
            fillAndInit();
            addListeners();
        }

        // If the current project has changed, clear the view
        if (projectPresenter.getCurrentProjectDTO() != currentProjectDTO) {
            logFrame = projectPresenter.getCurrentProjectDTO().getLogFrameDTO();
            currentProjectDTO = projectPresenter.getCurrentProjectDTO();
            fillAndInit();
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // nothing to do.
    }

    /**
     * Initializes the presenter.
     */
    private void addListeners() {

        // Enable the save button when the log frame is edited.
        view.getLogFrameGrid().addListener(new ProjectLogFrameGrid.LogFrameGridListener() {

            @Override
            public void logFrameEdited() {
                view.getSaveButton().setEnabled(true);
            }
        });

        // Log frame title box listener.
        view.getLogFrameTitleTextBox().addListener(Events.OnKeyUp, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (logFrame != null) {
                    logFrame.setTitle(view.getLogFrameTitleTextBox().getValue());
                    view.getSaveButton().setEnabled(true);
                }
            }
        });

        // Log frame main objective box listener.
        view.getLogFrameMainObjectiveTextBox().addListener(Events.OnKeyUp, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (logFrame != null) {
                    logFrame.setMainObjective(view.getLogFrameMainObjectiveTextBox().getValue());
                    view.getSaveButton().setEnabled(true);
                }
            }
        });

        // Save action.
        view.getSaveButton().addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                // Logs the modified log frame.
                if (Log.isDebugEnabled()) {
                    Log.debug("[handleEvent] Merges the log frame : \n" + logFrame.toString());
                }

                // Sends the merge action to the server.
                dispatcher.execute(new UpdateLogFrame(logFrame, currentProjectDTO.getId()), new MaskingAsyncMonitor(
                        view, I18N.CONSTANTS.loading()), new AsyncCallback<LogFrameResult>() {

                    @Override
                    public void onFailure(Throwable e) {

                        // Informs of the error.
                        Log.error("[execute] Error when saving the log frame.", e);
                        MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.error(), null);
                    }

                    @Override
                    public void onSuccess(LogFrameResult r) {

                        if (Log.isDebugEnabled()) {
                            Log.debug("[execute] Log frame successfully saved.");
                        }

                        // Updates local entities with the new returned log
                        // frame (to get the generated ids).
                        final LogFrameDTO updated = r.getLogFrame();
                        currentProjectDTO.setLogFrameDTO(updated);
                        view.getLogFrameGrid().updateLogFrame(updated);
                        logFrame = updated;

                        // Informs of the success.
                        MessageBox.info(I18N.CONSTANTS.save(), I18N.CONSTANTS.saved(), null);
                        view.getSaveButton().setEnabled(false);
                    }
                });
            }
        });

        // Copy action.
        view.getCopyButton().addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                NotImplementedMethod.methodNotImplemented();
            }
        });

        // Paste action.
        view.getPasteButton().addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                NotImplementedMethod.methodNotImplemented();
            }
        });
    }

    /**
     * Fills the view with the current log frame and initializes buttons state.
     */
    private void fillAndInit() {

        if (logFrame != null) {

            // Fill the log frame title.
            view.getLogFrameTitleTextBox().setValue(logFrame.getTitle());

            // Fill the log frame main objective.
            view.getLogFrameMainObjectiveTextBox().setValue(logFrame.getMainObjective());

            // Fill the grid.
            view.getLogFrameGrid().displayLogFrame(logFrame);
        }

        // Default buttons states.
        view.getSaveButton().setEnabled(false);
        view.getCopyButton().setEnabled(false);
        view.getPasteButton().setEnabled(false);
        view.getWordExportButton().setEnabled(false);
        view.getExcelExportButton().setEnabled(false);
    }
}
