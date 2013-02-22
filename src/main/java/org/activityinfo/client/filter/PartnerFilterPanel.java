

package org.activityinfo.client.filter;

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
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterHandler;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterHandler;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * UI Component that allows the user to choose a list of partners
 * on which to restrict the query.
 */
public class PartnerFilterPanel extends ContentPanel implements FilterPanel {
	private final Dispatcher service;
	private FilterToolBar filterToolBar;
	private Filter baseFilter = null;
	
	private Filter value = new Filter();
	
	private ListStore<PartnerDTO> store;
	private CheckBoxListView<PartnerDTO> listView;
	
	@Inject
    public PartnerFilterPanel(Dispatcher service) {
    	this.service = service;
    	
        initializeComponent();

        createFilterToolBar();
        createList();
    }
    
	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
        
        setLayout(new FitLayout());
        setScrollMode(Style.Scroll.NONE);
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
	}
    
	private void createFilterToolBar() {
		filterToolBar = new FilterToolBar();
		filterToolBar.addApplyFilterHandler(new ApplyFilterHandler() {
			
			@Override
			public void onApplyFilter(ApplyFilterEvent deleteEvent) {
				applyFilter();
			}
		});
		filterToolBar.addRemoveFilterHandler(new RemoveFilterHandler() {
			
			@Override
			public void onRemoveFilter(RemoveFilterEvent deleteEvent) {
				removeFilter();
			}
		});
		setTopComponent((Component) filterToolBar);
	}

	private void createList() {
		store = new ListStore<PartnerDTO>();
		listView = new CheckBoxListView<PartnerDTO>();
		listView.setStore(store);
		listView.setDisplayProperty("name");
		listView.addListener(Events.Select, new Listener<ListViewEvent<PartnerDTO>>() {

			@Override
			public void handleEvent(ListViewEvent<PartnerDTO> be) {
				filterToolBar.setApplyFilterEnabled(true);
			}
		});
		add(listView);

	}
	
	@Override
	public void applyBaseFilter(Filter rawFilter) {
		
		// we need to remove any partner filter from this base filter
		// so the user has the full selection
		final Filter filter = new Filter(rawFilter);
		filter.clearRestrictions(DimensionType.Partner);
		
		if (baseFilter == null || !baseFilter.equals(filter)) {
			service.execute(new GetPartnersWithSites(filter), new AsyncCallback<PartnerResult>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(PartnerResult result) {
					List<Integer> ids = getSelectedIds();
					store.removeAll();
					store.add(result.getData());
					applyInternalValue();
					
					for(PartnerDTO partner : store.getModels()) {
						if(ids.contains(partner.getId())) {
							listView.setChecked(partner, true);
						}
					}
					
					baseFilter = filter;
				}
			});
		}
	}
	
	
	protected void removeFilter() {
		for(PartnerDTO partner : listView.getStore().getModels()) {
			listView.setChecked(partner, false);
		}
		value = new Filter();
		ValueChangeEvent.fire(this, value);
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(false);
	}

	protected void applyFilter() {
		
		value = new Filter();
		if(isRendered()) {
			List<Integer> selectedIds = getSelectedIds();
			if (selectedIds.size() > 0) {
				value.addRestriction(DimensionType.Partner, getSelectedIds());
			}
		}
	
		ValueChangeEvent.fire(this, value);
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(true);
	}


	private List<Integer> getSelectedIds() {
		List<Integer> list = new ArrayList<Integer>();

		for (PartnerDTO model : listView.getChecked()) {
			list.add(model.getId());
		}
		return list;
	}

	@Override
	public Filter getValue() {
		return value;
	}

	@Override
	public void setValue(Filter value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		this.value = new Filter();
		this.value.addRestriction(DimensionType.Partner, value.getRestrictions(DimensionType.Partner));
		applyInternalValue();
		if(fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	private void applyInternalValue() {
		for(PartnerDTO model : listView.getStore().getModels()){
			listView.setChecked((PartnerDTO) model, value.getRestrictions(DimensionType.Partner).contains(model.getId()));
		}
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(value.isRestricted(DimensionType.Partner));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
