package org.activityinfo.client.page.entry.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.SiteModel;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormLoader  {

    private final CommandService dataConn;
    private final EventBus eventBus;

    private SiteFormLeash leash = null;

    public SiteFormLoader(EventBus eventBus, CommandService service) {
        this.dataConn = service;
        this.eventBus = eventBus;
    }

    public void edit(final ActivityModel activity, final SiteModel site,
                     final AsyncMonitor monitor) {

        monitor.beforeRequest();

        if(leash != null && leash.getActivityId() == activity.getId()) {
            leash.setSite(site);
            monitor.onCompleted();
            return;
        } else if(leash != null) {
            leash.destroy();
            leash = null;
        }


        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                monitor.onConnectionProblem();
            }

            @Override
            public void onSuccess() {

                SiteForm form = new SiteForm();
                SiteFormDialog dlg = new SiteFormDialog(form);

                SiteFormPresenter presenter = new SiteFormPresenter(eventBus, dataConn, activity, dlg);

                presenter.setSite(site);

                monitor.onCompleted();

                if(monitor instanceof TestingMonitor) {
                    ((TestingMonitor)monitor).onFormLoaded(presenter, form, dlg);
                }

                leash = presenter;
            }
        });
    }

    
    public interface TestingMonitor extends AsyncMonitor {

        public void onFormLoaded(SiteFormPresenter presenter, SiteForm form, SiteFormDialog dlg);


    }


}
