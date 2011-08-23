package org.sigmah.client.page.config;

import java.util.List;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ShowLockedPeriodsDialog extends Dialog implements ShowLockedPeriodsView{
	LockedPeriodGrid grid = new LockedPeriodGrid();

	public ShowLockedPeriodsDialog() {
		super();
		
		setHideOnButtonClick(true);
		
		grid.setReadOnly(true);
		add(grid);
		setMinHeight(400);
		setMinWidth(600); 
		setLayout(new FitLayout());
	}

	@Override
	public List<LockedPeriodDTO> getValue() {
		return null;
	}

	@Override
	public void setValue(List<LockedPeriodDTO> value) {
		grid.setItems(value);
	}

	@Override
	public void setValue(List<LockedPeriodDTO> value, boolean fireEvents) {
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<LockedPeriodDTO>> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActivityFilter(ActivityDTO activity) {
		grid.setActivityFilter(activity);
	}

	@Override
	public void show() {
		super.show();
		
		layout(true);
	}

	@Override
	public void setHeader(String header) {
		setHeading(header);
		grid.setHeaderVisible(false);
	}
	
}
