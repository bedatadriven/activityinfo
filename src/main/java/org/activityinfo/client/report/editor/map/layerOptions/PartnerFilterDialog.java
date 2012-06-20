package org.activityinfo.client.report.editor.map.layerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PartnerFilterDialog extends Dialog {

	private final Dispatcher service;
	private ListStore<PartnerDTO> store;
	private CheckBoxListView<PartnerDTO> listView;
	
	private SelectionCallback<Set<Integer>> callback;
	
    public PartnerFilterDialog(Dispatcher service) {
    	this.service = service;
    	
        initializeComponent();
        createList();
    }
    
	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
        setWidth(250);
        setHeight(350);
        setLayout(new FitLayout());
        setScrollMode(Style.Scroll.NONE);
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
	}
    
	private void createList() {
		store = new ListStore<PartnerDTO>();
		listView = new CheckBoxListView<PartnerDTO>();
		listView.setStore(store);
		listView.setDisplayProperty("name");
		add(listView);
	}
	
	public Set<Integer> getSelectedIds() {
		Set<Integer> set = new HashSet<Integer>();

		for (PartnerDTO model : listView.getChecked()) {
			set.add(model.getId());
		}
		return set;
	}
	
	public void show(Filter baseFilter, final Filter currentFilter, SelectionCallback<Set<Integer>> callback) {
		show();
		this.callback = callback;
		service.execute(new GetPartnersWithSites(baseFilter), new AsyncCallback<PartnerResult>() {
	
			@Override
			public void onFailure(Throwable caught) {
				
			}
	
			@Override
			public void onSuccess(PartnerResult result) {
				Set<Integer> ids = currentFilter.getRestrictions(DimensionType.Partner);
				
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

	@Override
	protected void onButtonPressed(Button button) {
		if(button.getItemId().equals("ok")) {
			callback.onSelected(getSelectedIds());
		}
		callback = null;
		hide();
	}
}
