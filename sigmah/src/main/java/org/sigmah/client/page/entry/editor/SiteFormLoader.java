/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.client.map.MapApiLoader;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormLoader {

    private final Dispatcher dataConn;
    private final EventBus eventBus;

    private SiteFormLeash leash = null;

    public SiteFormLoader(EventBus eventBus, Dispatcher service) {
        this.dataConn = service;
        this.eventBus = eventBus;
    }

    public void edit(final ActivityDTO activity, final SiteDTO site,
                     final AsyncMonitor monitor) {

        monitor.beforeRequest();
        // It can take awhile to build the form on some browsers,
        // so give the browser time to update the display to indicate
        // that loading is in process.

        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
               doEdit(activity, site, monitor);
            }
        });
    }

    private void doEdit(final ActivityDTO activity, final SiteDTO site, final AsyncMonitor monitor) {

        if (leash != null && leash.getActivityId() == activity.getId()) {
            leash.setSite(site);
            monitor.onCompleted();
            return;
        } else if (leash != null) {
            leash.destroy();
            leash = null;
        }

        // start loading the Maps API in parallel
        MapApiLoader.load();
        loadCodeFragment(activity, site, monitor);
    }

    private void loadCodeFragment(final ActivityDTO activity, final SiteDTO site, final AsyncMonitor monitor) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                monitor.onConnectionProblem();
            }

            @Override
            public void onSuccess() {
                onCodeFragmentLoaded(activity, site, monitor);
            }
        });
    }

    private void onCodeFragmentLoaded(final ActivityDTO activity, final SiteDTO site, final AsyncMonitor monitor) {
        MapApiLoader.load(new NullAsyncMonitor(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                showForm(activity, site, monitor);
            }

            @Override
            public void onSuccess(Void aVoid) {
                showForm(activity, site, monitor);
            }
        });
    }

    private void showForm(ActivityDTO activity, SiteDTO site, AsyncMonitor monitor) {
        SiteForm form = new SiteForm();
        SiteFormDialog dlg = new SiteFormDialog(form);

        SiteFormPresenter presenter = new SiteFormPresenter(eventBus, dataConn, activity, dlg);
        presenter.setSite(site);

        monitor.onCompleted();

        leash = presenter;
    }
}
