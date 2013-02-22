package org.activityinfo.client.page.entry.column;

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

import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class LockedColumnRenderer implements
		GridCellRenderer<ModelData> {
	private final LockedPeriodSet lockSet;

	public LockedColumnRenderer(LockedPeriodSet lockSet) {
		super();
		this.lockSet = lockSet;
	}

	@Override
	public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
		if (model instanceof SiteDTO) {
			SiteDTO siteModel = (SiteDTO) model;
	    	StringBuilder builder = new StringBuilder();
	    	if (siteModel.isLinked()) {
	    		return IconImageBundle.ICONS.link().getHTML();
	    	} else if (lockSet.isLocked(siteModel)) {
	    		//String tooltip = buildTooltip(model, activity);
	    		
	    		//builder.append("<span qtip='");
	    		//builder.append(tooltip);
	    		//builder.append("'>");
	    		builder.append(IconImageBundle.ICONS.lockedPeriod().getHTML());
	    		//builder.append("</span>");
	    		return builder.toString();
	    	} 
	    } 
	    return "";
	}
}