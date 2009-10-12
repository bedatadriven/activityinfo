package org.activityinfo.client.page.report;

import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.ListCmdLoader;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;
/*
 * @author Alex Bertram
 */

public class ReportHomePresenter extends AbstractEditorGridPresenter<ReportTemplateDTO> {

    @ImplementedBy(ReportGrid.class)
    public interface View extends GridView<ReportHomePresenter, ReportTemplateDTO> {
        public void init(ReportHomePresenter presenter, ListStore<ReportTemplateDTO> store);
    }

    private final EventBus eventBus;
    private final CommandService service;
    private final View view;

    private ListCmdLoader loader;
    private GroupingStore<ReportTemplateDTO> store;

    @Inject
    public ReportHomePresenter(EventBus eventBus, CommandService service, IStateManager stateMgr, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;

        loader = new ListCmdLoader(service);
        loader.setCommand(new GetReportTemplates());
        loader.setRemoteSort(false);

        store = new GroupingStore<ReportTemplateDTO>(loader);
        store.groupBy("databaseName");

        this.view.init(this, store);

        loader.load();
    }

    public void shutdown() {

    }


    @Override
    protected Command createSaveCommand() {
        BatchCommand batch = new BatchCommand();
        for(Record record : store.getModifiedRecords()) {
            ReportTemplateDTO report = (ReportTemplateDTO) record.getModel();
            batch.add(new UpdateSubscription(
                    report.getId(),
                    report.isSubscribed()));
        }
        return batch;
    }

    @Override
    public Store<ReportTemplateDTO> getStore() {
        return store;
    }

    @Override
    protected String getStateId() {
        return "reportGrid";
    }

    public void onSelectionChanged(ReportTemplateDTO selectedItem) {

    }

    public PageId getPageId() {
        return Pages.ReportHome;
    }

    public Object getWidget() {
        return view;
    }

    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void onTemplateSelected(ReportTemplateDTO dto) {
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new ReportPreviewPlace(dto.getId())));
    }

    @Override
    public void onUIAction(String actionId) {
        if(UIActions.add.equals(actionId)) {


            final ReportXmlForm form = new ReportXmlForm();
            form.setXml("<report>\n<title></title>\n</report>");

            final FormDialogImpl dlg = new FormDialogImpl(form);
            dlg.setWidth(400);
            dlg.setHeight(400);
            dlg.show(new FormDialogCallback() {
                @Override
                public void onValidated() {

                    service.execute(new CreateReportDef(null, form.getXml()), dlg, new AsyncCallback<CreateResult>() {
                        public void onFailure(Throwable caught) {
                            dlg.onServerError();
                        }

                        public void onSuccess(CreateResult result) {
                            dlg.hide();
                            loader.load();

                        }
                    });

                }
            });
        }
    }

    public boolean navigate(Place place) {
        return true;
    }
}
