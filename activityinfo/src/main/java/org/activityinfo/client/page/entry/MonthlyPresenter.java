package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.DateWrapper;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.loader.ListCmdLoader;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetMonthlyReports;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.UpdateMonthlyReports;
import org.activityinfo.shared.command.result.MonthlyReportResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.IndicatorRow;
import org.activityinfo.shared.dto.SiteModel;

import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class MonthlyPresenter extends AbstractEditorGridPresenter<IndicatorRow> {

    public interface View extends GridView<MonthlyPresenter, IndicatorRow> {
        void init(MonthlyPresenter presenter, ListStore<IndicatorRow> store);

        void setStartMonth(Month startMonth);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private final ActivityModel activity;

    private ListStore<IndicatorRow> store;
    private ListCmdLoader<MonthlyReportResult> loader;

    private Listener<SiteEvent> siteListener;

    private int currentSiteId = -1;
    private Month startMonth;

    public MonthlyPresenter(EventBus eventBus, Dispatcher service, IStateManager stateMgr, ActivityModel activity, final View view) {
        super(eventBus, service, stateMgr, view);
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;
        this.activity = activity;

        loader = new ListCmdLoader<MonthlyReportResult>(this.service);
        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                view.setStartMonth(startMonth);
            }
        });
        store = new ListStore<IndicatorRow>(loader);
        store.setMonitorChanges(true);
        this.view.init(this, store);

        siteListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent be) {
                if (be.getType() == AppEvents.SiteSelected) {
                    onSiteSelected(be.getSite());
                }
            }
        };
        eventBus.addListener(AppEvents.SiteSelected, siteListener);

        getInitialStartMonth(stateMgr);

        this.view.setStartMonth(startMonth);
    }

    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteSelected, siteListener);
    }

    private void getInitialStartMonth(IStateManager stateMgr) {
        String stateKey = "monthlyView" + this.activity.getId() + "startMonth";
        if (stateMgr.getString(stateKey) != null) {
            try {
                startMonth = Month.parseMonth(stateMgr.getString(stateKey));
            } catch (NumberFormatException e) {
            }
        }
        if (startMonth == null) {
            DateWrapper today = new DateWrapper();
            startMonth = new Month(today.getFullYear(), today.getMonth());
        }
    }

    @Override
    public Store<IndicatorRow> getStore() {
        return store;
    }

    private void onSiteSelected(SiteModel site) {
        currentSiteId = site.getId();
        loader.setCommand(new GetMonthlyReports(currentSiteId, startMonth, 7));
        loader.load();
    }

    public void onMonthSelected(Month month) {

        startMonth = new Month(month.getYear(), month.getMonth() - 3);
        loader.setCommand(new GetMonthlyReports(currentSiteId, startMonth, 7));
        loader.load();
    }


    public boolean navigate(Place place) {
        return false;
    }

    @Override
    protected String getStateId() {
        return "monthView";
    }

    public void onSelectionChanged(IndicatorRow selectedItem) {

    }

    public PageId getPageId() {
        return null;
    }

    public Object getWidget() {
        return null;
    }

    @Override
    protected Command createSaveCommand() {
        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        for (Record record : store.getModifiedRecords()) {
            IndicatorRow report = (IndicatorRow) record.getModel();
            for (String property : record.getChanges().keySet()) {
                UpdateMonthlyReports.Change change = new UpdateMonthlyReports.Change();
                change.indicatorId = report.getIndicatorId();
                change.month = IndicatorRow.monthForProperty(property);
                change.value = report.get(property);
                changes.add(change);
            }
        }
        return new UpdateMonthlyReports(currentSiteId, changes);
    }
}
