package org.activityinfo.client.page.config.link;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.dom.client.Element;

public class IndicatorGridPanel extends ContentPanel {
	
	private static final int INDENT = 10;
	
	private Set<Integer> linked = Collections.emptySet();
	private ListStore<ModelData> store;
	private Grid<ModelData> grid;
	
	public IndicatorGridPanel() {
				
		store = new ListStore<ModelData>();
		grid = new Grid<ModelData>(store, createColumnModel());
		grid.setView(new HighlightingGridView() {

			@Override
			protected boolean isHighlightable(ModelData model) {
				return model instanceof IndicatorDTO;
			}
		});
		grid.getView().setEmptyText(I18N.CONSTANTS.selectDatabaseHelp());
		grid.setAutoExpandColumn("name");
		grid.setHideHeaders(true);
		
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		grid.getSelectionModel().addListener(Events.BeforeSelect, new Listener<SelectionEvent<ModelData>>() {

			@Override
			public void handleEvent(SelectionEvent<ModelData> event) {
				if(!(event.getModel() instanceof IndicatorDTO)) {
					event.setCancelled(true);
				}
			}
		});
		setLayout(new FitLayout());
		add(grid);
		
	}
	
	public HighlightingGridView getGridView() {
		return (HighlightingGridView) grid.getView();
	}
	
	public int getRowY(IndicatorDTO indicator) {
		int rowIndex = grid.getStore().indexOf(indicator);
		if(rowIndex == -1) {
			throw new IllegalArgumentException("indicatorId="+indicator.getId());
		}
		Element row = grid.getView().getRow(rowIndex);
		Point p = El.fly(row).getAnchorXY("c", false);
		return p.y;
	}

	public void addMouseOverListener(Listener<GridEvent<IndicatorDTO>> listener) {
		grid.addListener(HighlightingGridView.RowMouseOver, listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addSelectionChangeListener(SelectionChangedListener<IndicatorDTO> listener) {
		grid.getSelectionModel().addSelectionChangedListener((SelectionChangedListener)listener);
	}

	private ColumnModel createColumnModel() {
		ColumnConfig icon = new ColumnConfig("icon", 28);
		icon.setRenderer(new GridCellRenderer<ModelData>() {

			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				
				if(model instanceof IndicatorDTO) {
					int id = ((IndicatorDTO) model).getId();
					if(linked.contains(id)) {
						return IconImageBundle.ICONS.link().getHTML();
					}
				}
				return "";
			}
			
		});
		
		ColumnConfig name = new ColumnConfig("name", I18N.CONSTANTS.name(), 150);
		name.setRenderer(new GridCellRenderer<ModelData>() {

			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				
				StringBuilder html = new StringBuilder();
				html.append("<div style=\"margin-left: ").append(indent(model)).append("px;");
				if(!(model instanceof IndicatorDTO)) {
					html.append(";font-weight: bold;");
				}
				html.append("\">");
				html.append(model.get("name"));
				html.append("</div>");
				return html.toString();
			}
			
		});
		
		return new ColumnModel(Arrays.asList(icon, name));
	}
	
	protected int indent(ModelData model) {
		if(model instanceof IndicatorGroup) {
			return INDENT;
		} else if(model instanceof IndicatorDTO) {
			IndicatorDTO indicator = (IndicatorDTO) model;
			if(indicator.getCategory() == null) {
				return 2 * INDENT;
			} else {
				return 3 * INDENT;
			}
		}
		return 0;
	}

	public void setDatabase(UserDatabaseDTO db) {
		setHeading(db.getName());
		store.removeAll();
		for(ActivityDTO activity : db.getActivities()) {
			store.add(activity);
			for(IndicatorGroup group : activity.groupIndicators()) {
				if(group.getName() == null) {
					for(IndicatorDTO indicator : group.getIndicators()) {
						store.add(indicator);
					}
				} else {
					store.add(group);
					for(IndicatorDTO indicator : group.getIndicators()) {
						store.add(indicator);
					}
				}
			}
		}
	}

	public void setLinked(Set<Integer> ids) {
		this.linked = ids;
		if(grid.isRendered()) {
			getGridView().refreshAllRows();
		}
	}

	public IndicatorDTO getSelectedItem() {
		return (IndicatorDTO) grid.getSelectionModel().getSelectedItem();
	}

	
}
