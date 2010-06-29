/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.loader.ListCmdLoader;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.GridView;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.*;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
/*
 * @author Alex Bertram
 */

public class ReportHomePresenter extends AbstractEditorGridPresenter<ReportDefinitionDTO> {
    public static final PageId ReportHome = new PageId("reports");


    @ImplementedBy(ReportGrid.class)
    public interface View extends GridView<ReportHomePresenter, ReportDefinitionDTO> {
        public void init(ReportHomePresenter presenter, ListStore<ReportDefinitionDTO> store);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private ListCmdLoader loader;
    private GroupingStore<ReportDefinitionDTO> store;

    @Inject
    public ReportHomePresenter(EventBus eventBus, Dispatcher service, IStateManager stateMgr, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;

        loader = new ListCmdLoader(service);
        loader.setCommand(new GetReportTemplates());
        loader.setRemoteSort(false);

        store = new GroupingStore<ReportDefinitionDTO>(loader);
        store.groupBy("databaseName");

        this.view.init(this, store);

        loader.load();
    }

    public void shutdown() {

    }


    @Override
    protected Command createSaveCommand() {
        BatchCommand batch = new BatchCommand();
        for (Record record : store.getModifiedRecords()) {
            ReportDefinitionDTO report = (ReportDefinitionDTO) record.getModel();
            batch.add(new UpdateSubscription(
                    report.getId(),
                    report.isSubscribed()));
        }
        return batch;
    }

    @Override
    public Store<ReportDefinitionDTO> getStore() {
        return store;
    }

    @Override
    protected String getStateId() {
        return "reportGrid";
    }

    public void onSelectionChanged(ReportDefinitionDTO selectedItem) {

    }

    public PageId getPageId() {
        return ReportHome;
    }

    public Object getWidget() {
        return view;
    }

    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void onTemplateSelected(ReportDefinitionDTO dto) {
        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested,
                new ReportPreviewPageState(dto.getId())));
    }

    public void onNewReport(final int dbId) {

        final ReportXmlForm form = new ReportXmlForm();
        form.setXml("<report>\n<title></title>\n</report>");

        final FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(400);
        dlg.setHeight(400);
        dlg.show(new FormDialogCallback() {
            @Override
            public void onValidated() {

                service.execute(new CreateReportDef(dbId, form.getXml()), dlg, new AsyncCallback<CreateResult>() {
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

    public boolean navigate(PageState place) {
        return true;
    }
}
