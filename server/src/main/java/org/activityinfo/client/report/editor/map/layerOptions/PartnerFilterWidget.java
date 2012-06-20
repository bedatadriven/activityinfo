package org.activityinfo.client.report.editor.map.layerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PartnerFilterWidget extends FilterWidget {
	
	private Dispatcher dispatcher;
	private PartnerFilterDialog dialog;
	
	public PartnerFilterWidget(Dispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
		this.dimensionSpan.setInnerText(I18N.CONSTANTS.partners());
		this.stateSpan.setInnerText(I18N.CONSTANTS.all());
	}

	@Override
	public void choose(Event event) {
		if(dialog == null) {
			dialog = new PartnerFilterDialog(dispatcher);
		}
		dialog.show(getBaseFilter(), getValue(), new SelectionCallback<Set<Integer>>() {
			
			@Override
			public void onSelected(Set<Integer> selection) {
				Filter newValue = new Filter();
				if (selection != null && !selection.isEmpty()) {
					newValue.addRestriction(DimensionType.Partner, selection); 
				}
				setValue(newValue);
			}
		});
	}
	
	public void clear() {

	}
	
	@Override
	public void updateView() {
		if(getValue().isRestricted(DimensionType.Partner)) {
			setState(I18N.CONSTANTS.loading());
			retrievePartnerNames();
		} else {
			setState(I18N.CONSTANTS.all()); 
		}
	}

	private void retrievePartnerNames() {
		dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				formatPartners(result);
			}				
		});
	}
	
	private void formatPartners(SchemaDTO schema) {
		List<String> partnerNames = new ArrayList<String>();
		for(Integer id : getValue().getRestrictions(DimensionType.Partner)) {
			partnerNames.add(schema.getPartnerById(id).getName());
		}
		Collections.sort(partnerNames);
		setState(FilterResources.MESSAGES.filteredPartnerList(partnerNames));
	}
}
