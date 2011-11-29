/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.FilterPanel;
import org.sigmah.client.page.common.filter.FilterToolBar;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterHandler;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterHandler;
import org.sigmah.client.page.common.filter.FilterToolBar;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetPartnersWithSites;
import org.sigmah.shared.command.result.PartnerResult;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.report.model.DimensionType;

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
	public void applyBaseFilter(Filter filter) {
		if(baseFilter == null || !baseFilter.equals(filter)) {
			service.execute(new GetPartnersWithSites(filter), null, new AsyncCallback<PartnerResult>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(PartnerResult result) {
					List<Integer> ids = getSelectedIds();
					store.removeAll();
					store.add(result.getData());
					for(PartnerDTO partner : store.getModels()) {
						if(ids.contains(partner.getId())) {
							listView.setChecked(partner, true);
						}
					}
					
				}
			});
		}
	}
	
	
	protected void removeFilter() {
		for(PartnerDTO partner : listView.getStore().getModels()) {
			listView.setChecked(partner, false);
		}
		ValueChangeEvent.fire(this, getValue());
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(false);
	}

	protected void applyFilter() {
		ValueChangeEvent.fire(this, getValue());
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(true);
	}

	public List<PartnerDTO> getSelection() {
		List<PartnerDTO> list = new ArrayList<PartnerDTO>();

		for (PartnerDTO model : listView.getChecked()) {
			list.add(model);
		}
		return list;
	}

	public List<Integer> getSelectedIds() {
		List<Integer> list = new ArrayList<Integer>();

		for (PartnerDTO model : listView.getChecked()) {
			list.add(model.getId());
		}
		return list;
	}

	@Override
	public Filter getValue() {
		Filter filter = new Filter();
		if(isRendered()) {
			List<Integer> selectedIds = getSelectedIds();
			if (selectedIds.size() > 0) {
				filter.addRestriction(DimensionType.Partner, getSelectedIds());
			}
		}
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
