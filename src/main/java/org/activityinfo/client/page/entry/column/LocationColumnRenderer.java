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

import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class LocationColumnRenderer implements GridCellRenderer<ModelData> {

	@Override
	public Object render(ModelData model, String property, ColumnData config,
			int rowIndex, int colIndex, ListStore<ModelData> store,
			Grid<ModelData> grid) {

		if(model instanceof SiteDTO) {
			return render((SiteDTO)model);
		} else {
			return "";
		}
	}
	
	public Object render(SiteDTO model) {
		
		String name = model.getLocationName();
		String axe = model.getLocationAxe();
		
		if(axe == null) {
			return name;
		} else {
			return ColumnTemplates.INSTANCE.locationCell(model.getLocationName(), model.getLocationAxe()).asString();
		}
	}

}
