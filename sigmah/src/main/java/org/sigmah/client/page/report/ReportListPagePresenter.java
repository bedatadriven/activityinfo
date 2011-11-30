/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

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
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.GetReportTemplates;
import org.sigmah.shared.command.UpdateSubscription;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Page which presents the list of reports visible to the user
 *
 * @author Alex Bertram
 */
public class ReportListPagePresenter extends AbstractEditorGridPresenter<ReportDefinitionDTO> {
    public static final PageId REPORT_HOME_PAGE_ID = new PageId("reports");

    @ImplementedBy(ReportListPageView.class)
    public interface View extends GridView<ReportListPagePresenter, ReportDefinitionDTO> {
        void init(ReportListPagePresenter presenter, ListStore<ReportDefinitionDTO> store);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private ListCmdLoader loader;
    private GroupingStore<ReportDefinitionDTO> store;

    @Inject
    public ReportListPagePresenter(EventBus eventBus, Dispatcher service, StateProvider stateMgr, View view) {
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

    @Override
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

//    public void onSelectionChanged(ReportDefinitionDTO selectedItem) {
//    }

    @Override
	public PageId getPageId() {
        return REPORT_HOME_PAGE_ID;
    }

    @Override
	public Object getWidget() {
        return view;
    }

    @Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
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
                    @Override
					public void onFailure(Throwable caught) {
                        dlg.onServerError();
                    }

                    @Override
					public void onSuccess(CreateResult result) {
                        dlg.hide();
                        loader.load();
                    }
                });
            }
        });

    }

    @Override
	public boolean navigate(PageState place) {
        return true;
    }

	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		
	}

}
