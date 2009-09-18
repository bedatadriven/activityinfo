package org.activityinfo.client.pages.entry.editor;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.activityinfo.client.LoggingEventBus;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.CommandRequest;
import org.activityinfo.client.command.CommandServiceImpl;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.page.entry.editor.SiteForm;
import org.activityinfo.client.page.entry.editor.SiteFormDialog;
import org.activityinfo.client.page.entry.editor.SiteFormLoader;
import org.activityinfo.client.page.entry.editor.SiteFormPresenter;
import org.activityinfo.client.util.GWTTimerImpl;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.SiteModel;
import org.junit.Test;

import java.util.Date;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class CreateSiteGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.activityinfo.ApplicationTest";
    }

    @Test
    public void testCreate() throws Throwable {



        RemoteCommandServiceAsync remoteService = (RemoteCommandServiceAsync) GWT.create(RemoteCommandService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) remoteService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "cmd";
		endpoint.setServiceEntryPoint(moduleRelativeURL);

        LoggingEventBus eventBus = new LoggingEventBus();

        Authentication auth = new Authentication("9cdbcf2b2ce45c282f6f0c98ea4a3dea", "user@user.org");

        final CommandServiceImpl service = new CommandServiceImpl(remoteService, eventBus, new GWTTimerImpl(), auth) {

            @Override
            protected void onServerError(List<CommandRequest> executingCommands, Throwable caught) {
                caught.printStackTrace();
                fail("server error");
            }

            @Override
            protected void onRemoteCallSuccess(List<CommandResult> results, List<CommandRequest> executingCommands) {

                if(results.get(1) instanceof CreateResult) {
                    finishTest();
                }

                super.onRemoteCallSuccess(results, executingCommands);
            }
        };

        final SiteFormLoader formLoader = new SiteFormLoader(eventBus, service);

        service.execute(new GetSchema(), null, new AsyncCallback<Schema>() {
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                fail("get schema failed");
            }

            public void onSuccess(Schema result) {

                ActivityModel activity = result.getActivityById(33);

                SiteModel site = new SiteModel();
                site.setActivityId(33);

                formLoader.edit(activity, site, new SiteFormLoader.TestingMonitor() {

                    public void onFormLoaded(SiteFormPresenter presenter, SiteForm form, SiteFormDialog dlg) {

                        form.testingSetValue("locationName", "Test location");
                        form.testingSetValue("locationAxe", "Test locationAxe");
                        form.testingSetValue("date1", new Date());
                        form.testingSetValue("date2", new Date());
                        form.testingSetValue("status", 1);
                        form.testingSetValue("partner", new PartnerModel(8, "Cartias"));

                        presenter.onUIAction(UIActions.save);


                    }

                    public void beforeRequest() {

                    }

                    public void onCompleted() {

                    }

                    public void onConnectionProblem() {

                    }

                    public boolean onRetrying() {
                        return false;
                    }

                    public void onServerError() {

                    }
                }) ;

                service.executePending();

            }
        });


        service.executePending();

        delayTestFinish(50000);
        

    }

}
