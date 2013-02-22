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

import java.util.List;
import java.util.Set;

import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AdminLevelPredicates;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.common.collect.Sets;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Renders the site's admin membership as a single cell, with a maximum
 * of three rows, with a quick tip for the rest.
 */
public class AdminColumnRenderer implements GridCellRenderer<ModelData> {

	private List<AdminLevelDTO> levels;

	// reused to avoid reallocing for each row
	private Set<String> seen = Sets.newHashSet();
	
	public AdminColumnRenderer(List<AdminLevelDTO> levels) {
		super();
		this.levels = AdminLevelPredicates.breadthFirstSort(levels);
	}
	
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

	private Object render(SiteDTO model) {
		
		StringBuilder qtip = new StringBuilder();
		SafeHtmlBuilder summary = new SafeHtmlBuilder();
		
		// we use this set to keep track of names that we've added
		// to the summary to avoid duplication that is common between
		// territories, zones de sante, provinces and districts, etc
		seen.clear();
		
		int summaryLines = 0;
		
		for(AdminLevelDTO level : levels) {
			AdminEntityDTO entity = model.getAdminEntity(level.getId());

			if(entity != null) {
				String name = entity.getName();
				if(qtip.length() > 0) {
					qtip.append("<br>");
				}
				qtip.append(level.getName()).append(": ").append(name);
				if(summaryLines < 3 && !seen.contains(name)) {
					if(summaryLines > 0) {
						summary.appendHtmlConstant("<br/>");
					}
					summary.appendEscaped(name);
					seen.add(name);
					summaryLines ++; 
				}
			}
		}
		//return summary.toSafeHtml().asString();
		return ColumnTemplates.INSTANCE.adminCell(qtip.toString(), summary.toSafeHtml()).asString();
	}
}
