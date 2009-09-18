package org.activityinfo.client.page.report;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.client.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.common.grid.GridView;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.ListCmdLoader;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.*;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.UpdateSubscription;
import org.activityinfo.shared.dto.ReportTemplateDTO;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.inject.Inject;
import com.google.inject.ImplementedBy;
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

    private GroupingStore<ReportTemplateDTO> store;

    @Inject
    public ReportHomePresenter(EventBus eventBus, CommandService service, IStateManager stateMgr, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;

        ListCmdLoader loader = new ListCmdLoader(service);
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
                    report.getSubscriptionFrequency(),
                    report.getSubscriptionDay()
            ));
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

    public boolean navigate(Place place) {
        return true;
    }
}
