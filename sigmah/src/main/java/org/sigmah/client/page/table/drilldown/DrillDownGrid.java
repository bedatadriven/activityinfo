/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table.drilldown;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import org.sigmah.client.i18n.I18N;
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
        setHeading(I18N.CONSTANTS.drilldown());
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

        setHeading(I18N.CONSTANTS.drilldown() + " - " + indicator.getName());
    }


    @Override
    protected void addIndicatorColumns(ActivityDTO activity, List<ColumnConfig> columns) {

        ColumnConfig column = createIndicatorColumn(currentIndicator, I18N.CONSTANTS.value());
        column.setId("indicator");
        column.setDataIndex(currentIndicator.getPropertyName());
        columns.add(column);

    }
}
