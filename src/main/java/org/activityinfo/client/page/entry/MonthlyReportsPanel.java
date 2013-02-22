

package org.activityinfo.client.page.entry;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.shared.command.GetMonthlyReports;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.UpdateMonthlyReports;
import org.activityinfo.shared.command.result.MonthlyReportResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.IndicatorRowDTO;
import org.activityinfo.shared.dto.SiteDTO;

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

public class MonthlyReportsPanel extends ContentPanel implements ActionListener {
    private final Dispatcher service;

    private ListLoader<MonthlyReportResult> loader;
    private ListStore<IndicatorRowDTO> store;
    private MonthlyGrid grid;
	private ReportingPeriodProxy proxy;
	private MappingComboBox<Month> monthCombo;
    
	private int currentSiteId;

	private ActionToolBar toolBar;

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
    	toolBar = new ActionToolBar();
    	toolBar.setListener(this);
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
		this.currentSiteId = site.getId();
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
    
	@Override
	public void onUIAction(String actionId) {
		if(UIActions.SAVE.equals(actionId)) {
			save();
		}
	}

    private void save() {
        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        for (Record record : store.getModifiedRecords()) {
            IndicatorRowDTO report = (IndicatorRowDTO) record.getModel();
            for (String property : record.getChanges().keySet()) {
                UpdateMonthlyReports.Change change = new UpdateMonthlyReports.Change();
                change.setIndicatorId(report.getIndicatorId());
                change.setMonth(IndicatorRowDTO.monthForProperty(property));
                change.setValue((Double)report.get(property));
                changes.add(change);
            }
        }
        service.execute(new UpdateMonthlyReports(currentSiteId, changes), 
        		new MaskingAsyncMonitor(this, I18N.CONSTANTS.save()), new AsyncCallback<VoidResult>() {

				@Override
				public void onFailure(Throwable caught) {
					// handled by monitor
				}

				@Override
				public void onSuccess(VoidResult result) {
				}
        });
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
		
			service.execute(new GetMonthlyReports(siteId, startMonth, 7), callback);
		}
    }
}
