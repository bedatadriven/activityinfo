/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table.drilldown;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.common.grid.ConfirmCallback;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;

public class DrillDownGrid extends ContentPanel implements DrillDownEditor.View {
    ActivityDTO currentActivity;
    IndicatorDTO currentIndicator;
	@Override
	public void init(SiteEditor presenter, ActivityDTO activity,
			ListStore<SiteDTO> store) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AsyncMonitor getLoadingMonitor() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setSelection(int siteId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showLockedPeriods(List<LockedPeriodDTO> list) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setSite(SiteDTO selectedSite) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(SiteDTO site) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setSelected(int id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(SiteDTO site) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActionEnabled(String actionId, boolean enabled) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void confirmDeleteSelected(ConfirmCallback callback) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SiteDTO getSelection() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AsyncMonitor getDeletingMonitor() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AsyncMonitor getSavingMonitor() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void show(SiteEditor presenter, ActivityDTO activity,
			IndicatorDTO indicator, ListStore<SiteDTO> store) {
		// TODO Auto-generated method stub
		
	}

//    public DrillDownGrid() {
//    	super(false);
//        setHeading(I18N.CONSTANTS.drilldown());
//    }
//
//    public void show(SiteEditor presenter, ActivityDTO activity, IndicatorDTO indicator, ListStore<SiteDTO> store) {
//        currentIndicator = indicator;
//
//        if (currentActivity == null) {
//            currentActivity = activity;
//            init(presenter, activity, store);
//
//        } else if (activity.getId() != currentActivity.getId()) {
//            currentActivity = activity;
//            //grid.reconfigure(store, createColumnModel(activity));
//        } else {
//            grid.getColumnModel().setDataIndex(grid.getColumnModel().getIndexById("indicator"),
//                    indicator.getPropertyName());
//            if (grid.isRendered()) {
//                grid.getView().refresh(true);
//            }
//        }
//
//        setHeading(I18N.CONSTANTS.drilldown() + " - " + indicator.getName());
//    }
//
//
//    @Override
//    protected void createIndicatorColumns() {
//        ColumnConfig column = createIndicatorColumn(currentIndicator, I18N.CONSTANTS.value());
//        column.setId("indicator");
//        column.setDataIndex(currentIndicator.getPropertyName());
//        columns.add(column);
//    }
}
