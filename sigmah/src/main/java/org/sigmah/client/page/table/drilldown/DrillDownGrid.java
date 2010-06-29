package org.sigmah.client.page.table.drilldown;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import org.sigmah.client.Application;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.client.page.entry.SiteGrid;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SiteDTO;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class DrillDownGrid extends SiteGrid implements DrillDownEditor.View {

    ActivityDTO currentActivity;
    IndicatorDTO currentIndicator;

    public DrillDownGrid() {
        setHeading(Application.CONSTANTS.drilldown());
    }

    public void show(SiteEditor presenter, ActivityDTO activity, IndicatorDTO indicator, ListStore<SiteDTO> store) {

        currentIndicator = indicator;

        if (currentActivity == null) {
            currentActivity = activity;
            init(presenter, activity, store);

        } else if (activity.getId() != currentActivity.getId()) {
            currentActivity = activity;
            grid.reconfigure(store, createColumnModel(activity));
        } else {
            grid.getColumnModel().setDataIndex(grid.getColumnModel().getIndexById("indicator"),
                    indicator.getPropertyName());
            if (grid.isRendered()) {
                grid.getView().refresh(true);
            }
        }

        setHeading(Application.CONSTANTS.drilldown() + " - " + indicator.getName());
    }


    @Override
    protected void addIndicatorColumns(ActivityDTO activity, List<ColumnConfig> columns) {

        ColumnConfig column = createIndicatorColumn(currentIndicator, Application.CONSTANTS.value());
        column.setId("indicator");
        column.setDataIndex(currentIndicator.getPropertyName());
        columns.add(column);

    }
}
