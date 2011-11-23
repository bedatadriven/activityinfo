/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import java.util.ArrayList;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetMonthlyReports;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.command.UpdateMonthlyReports;
import org.sigmah.shared.command.result.MonthlyReportResult;
import org.sigmah.shared.dto.IndicatorRowDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MonthlyReportsPanel extends ContentPanel {
    private final Dispatcher service;

    private ListLoader<MonthlyReportResult> loader;
    private ListStore<IndicatorRowDTO> store;
    private MonthlyGrid grid;
	private ReportingPeriodProxy proxy;
	private MappingComboBox<Month> monthCombo;
    
	private int currentSiteId;

    public MonthlyReportsPanel(Dispatcher service) {
        this.service = service;
        
        setHeading(I18N.CONSTANTS.monthlyReports());
        setIcon(IconImageBundle.ICONS.table());
        setLayout(new FitLayout());
        
        proxy = new ReportingPeriodProxy();
		loader = new BaseListLoader<MonthlyReportResult>(proxy);
        store = new ListStore<IndicatorRowDTO>(loader);
        store.setMonitorChanges(true);
        grid = new MonthlyGrid(store);
        add(grid);
        
        addToolBar();
    }
    
    private void addToolBar() {
    	ActionToolBar toolBar = new ActionToolBar();
    	toolBar.addSaveSplitButton();
    	toolBar.add(new LabelToolItem(I18N.CONSTANTS.month() + ": "));

    	monthCombo = new MappingComboBox<Month>();
    	monthCombo.setEditable(false);
    	monthCombo.addListener(Events.Select, new Listener<FieldEvent>() {
    		@Override
			public void handleEvent(FieldEvent be) {
    			selectStartMonth(monthCombo.getMappedValue());
    		}	
    	});

    	DateWrapper today = new DateWrapper();
    	DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM yyyy");
    	for(int year = today.getFullYear(); year != today.getFullYear()-3; --year) {

    		for(int month = 12; month != 0; --month) {

    			DateWrapper d= new DateWrapper(year, month, 1);

    			Month m = new Month(year, month);
    			monthCombo.add(m, monthFormat.format(d.asDate()));
    		}
    	}

    	toolBar.add(monthCombo);
    	
    	setTopComponent(toolBar);
	}

	public void load(SiteDTO site) {
    	Month startMonth = getInitialStartMonth(site);
    	monthCombo.setMappedValue(startMonth);
    	grid.updateMonthColumns(startMonth);
    	proxy.setStartMonth(startMonth);
    	proxy.setSiteId(site.getId());
    	loader.load();
    }
	
	private void selectStartMonth(Month startMonth) {
		proxy.setStartMonth(startMonth);
		grid.updateMonthColumns(startMonth);
		loader.load();
	}

    private Month getInitialStartMonth(SiteDTO site) {
        String stateKey = "monthlyView" + site.getActivityId() + "startMonth";
        if (StateManager.get().getString(stateKey) != null) {
            try {
                return Month.parseMonth(StateManager.get().getString(stateKey));
            } catch (NumberFormatException e) {
            }
        }
   
        DateWrapper today = new DateWrapper();
        return new Month(today.getFullYear(), today.getMonth());
    }


    public void onMonthSelected(Month month) {
    	Month startMonth = new Month(month.getYear(), month.getMonth() - 3);
    	proxy.setStartMonth(startMonth);
    	grid.updateMonthColumns(startMonth);
        loader.load();
    }

    private Command createSaveCommand() {
        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        for (Record record : store.getModifiedRecords()) {
            IndicatorRowDTO report = (IndicatorRowDTO) record.getModel();
            for (String property : record.getChanges().keySet()) {
                UpdateMonthlyReports.Change change = new UpdateMonthlyReports.Change();
                change.indicatorId = report.getIndicatorId();
                change.month = IndicatorRowDTO.monthForProperty(property);
                change.value = report.get(property);
                changes.add(change);
            }
        }
        return new UpdateMonthlyReports(currentSiteId, changes);
    }
    
    private class ReportingPeriodProxy extends RpcProxy<MonthlyReportResult> {

    	private Month startMonth;
    	private int siteId;

    	public void setSiteId(int siteId) {
    		this.siteId = siteId;
    	}
    	
    	public void setStartMonth(Month startMonth) {
    		this.startMonth = startMonth;
    	}

		@Override
		protected void load(Object loadConfig,
				AsyncCallback<MonthlyReportResult> callback) {
		
			service.execute(new GetMonthlyReports(siteId, startMonth, 7), null, callback);
		}
    }
}
