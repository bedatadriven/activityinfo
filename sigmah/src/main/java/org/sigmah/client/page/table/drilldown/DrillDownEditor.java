/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table.drilldown;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.event.PivotCellEvent;
import org.sigmah.client.page.common.grid.AbstractGridPageState;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.date.DateUtil;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;

public class DrillDownEditor extends SiteEditor {
    public interface View extends SiteEditor.View {
        void show(SiteEditor presenter, ActivityDTO activity, IndicatorDTO indicator, ListStore<SiteDTO> store);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final DateUtil dateUtil;
    private final View view;
    private Listener<PivotCellEvent> eventListener;
    private ListStore<SiteDTO> listStore;

    public DrillDownEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, DateUtil dateUtil,
                           View view) {
        super(eventBus, service, stateMgr, view);

        this.eventBus = eventBus;
        this.dateUtil = dateUtil;
        this.service = service;
        this.view = view;

        eventListener = new Listener<PivotCellEvent>() {
            public void handleEvent(PivotCellEvent be) {
                onDrillDown(be);
            }
        };
        eventBus.addListener(AppEvents.Drilldown, eventListener);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        eventBus.removeListener(AppEvents.Drilldown, eventListener);
    } 

    public void onDrillDown(PivotCellEvent event) {

        // construct our filter from the intersection of rows and columns
        Filter filter = new Filter(filterFromAxis(event.getRow()), filterFromAxis(event.getColumn()));

        // apply the effective filter
        final Filter effectiveFilter = new Filter(filter, event.getElement().getContent().getEffectiveFilter());

        // determine the indicator
        final int indicatorId = effectiveFilter.getRestrictions(DimensionType.Indicator).iterator().next();
        effectiveFilter.clearRestrictions(DimensionType.Indicator);

        service.execute(new GetSchema(), null, new Got<SchemaDTO>() {
            @Override
            public void got(SchemaDTO schema) {

                ActivityDTO activity = schema.getActivityByIndicatorId(indicatorId);
                IndicatorDTO indicator = activity.getIndicatorById(indicatorId);

                drill(activity, indicator, effectiveFilter);
            }
        });
    }

    public void drill(ActivityDTO activity, IndicatorDTO indicator, Filter filter) {
        currentActivity = activity;

        GetSites cmd = GetSites.byActivity(currentActivity.getId());
        cmd.setFilter(filter);

        pagingCmdLoader.setCommand(cmd);
        pagingCmdLoader.setSortField(indicator.getPropertyName());
        pagingCmdLoader.setSortDir(Style.SortDir.DESC);

        view.show(this, currentActivity, indicator, listStore);

        pagingCmdLoader.load();
    }


    private Filter filterFromAxis(PivotTableData.Axis axis) {

        Filter filter = new Filter();
        while (axis != null) {
            if (axis.getDimension() != null) {
                if (axis.getDimension().getType() == DimensionType.Date) {
                    filter.setDateRange(dateUtil.rangeFromCategory(axis.getCategory()));
                } else if (axis.getCategory() instanceof EntityCategory) {
                    filter.addRestriction(axis.getDimension().getType(), ((EntityCategory) axis.getCategory()).getId());
                }
            }
            axis = axis.getParent();
        }
        return filter;
    }

    @Override
    protected void firePageEvent(AbstractGridPageState place, LoadEvent le) {
        // no page events, we're embedded in another page
    }
}


