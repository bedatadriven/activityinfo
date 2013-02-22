package org.activityinfo.client.page.config;

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

import java.util.List;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public final class ShowLockedPeriodsDialog extends Dialog implements ShowLockedPeriodsView{
	private final LockedPeriodGrid grid = new LockedPeriodGrid();

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
