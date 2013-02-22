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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.common.columns.EditableLocalDateColumn;
import org.activityinfo.client.page.common.columns.ReadTextColumn;
import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.activityinfo.client.util.IndicatorNumberFormat;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.common.collect.Lists;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * Builder class for constructing a ColumnModel for site grids
 * 
 */
public class ColumnModelBuilder {

	private List<ColumnConfig> columns = Lists.newArrayList();
   
    public ColumnModelBuilder addActivityColumn(final UserDatabaseDTO database) {
    	ColumnConfig config = new ColumnConfig("activityId", I18N.CONSTANTS.activity(), 100);
    	config.setRenderer(new GridCellRenderer<SiteDTO>() {

			@Override
			public Object render(SiteDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<SiteDTO> store, Grid<SiteDTO> grid) {
				
				ActivityDTO activity = database.getActivityById(model.getActivityId());
				return activity == null ? "" : activity.getName();
			}
		});
    	columns.add(config);
    	return this;
	}
    
    public ColumnModelBuilder addActivityColumn(final SchemaDTO schema) {
    	ColumnConfig config = new ColumnConfig("activityId", I18N.CONSTANTS.activity(), 100);
    	config.setRenderer(new GridCellRenderer<SiteDTO>() {

			@Override
			public Object render(SiteDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<SiteDTO> store, Grid<SiteDTO> grid) {
				
				ActivityDTO activity = schema.getActivityById(model.getActivityId());
				return activity == null ? "" : activity.getName();
			}
		});
    	columns.add(config);
    	return this;
    }
    

	public ColumnModelBuilder addDatabaseColumn(final SchemaDTO schema) {
		ColumnConfig config = new ColumnConfig("activityId", I18N.CONSTANTS.activity(), 100);
    	config.setRenderer(new GridCellRenderer<SiteDTO>() {

			@Override
			public Object render(SiteDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<SiteDTO> store, Grid<SiteDTO> grid) {
				
				ActivityDTO activity = schema.getActivityById(model.getActivityId());
				return activity == null ? "" : activity.getDatabase().getName();
			}
		});
    	columns.add(config);
    	return this;
	}



	public ColumnModel build() {
    	return new ColumnModel(columns);
    }
    
    public void createGeographyColumn(ActivityDTO activity) {
		if(activity.getDatabase().isViewAllAllowed()) {
			ColumnConfig columnGeography = new ColumnConfig();
			columnGeography.setHeader(I18N.CONSTANTS.geography());
			columnGeography.setWidth(100);
			columns.add(columnGeography);
        }
	}

	protected ColumnModelBuilder maybeAddProjectColumn(UserDatabaseDTO userDatabase) {
		if(!userDatabase.getProjects().isEmpty()) {
			addProjectColumn();
        }
		return this;
	}

	public void addProjectColumn() {
		columns.add(new ReadTextColumn("project", I18N.CONSTANTS.project(), 100));
	}

	public ColumnModelBuilder maybeAddLockColumn(final ActivityDTO activity) {
		ColumnConfig columnLocked = new ColumnConfig("x", "", 28);
        columnLocked.setRenderer(new LockedColumnRenderer(new LockedPeriodSet(activity)));
        columnLocked.setSortable(false);
        columnLocked.setMenuDisabled(true);
        columns.add(columnLocked);
        return this;
	}
	
	public ColumnConfig addIndicatorColumn(IndicatorDTO indicator, String header) {

        NumberField indicatorField = new NumberField();
        indicatorField.getPropertyEditor().setFormat(IndicatorNumberFormat.INSTANCE);

        ColumnConfig indicatorColumn = new ColumnConfig(indicator.getPropertyName(), header, 50);

        indicatorColumn.setNumberFormat(IndicatorNumberFormat.INSTANCE);
        indicatorColumn.setEditor(new CellEditor(indicatorField));
        indicatorColumn.setAlignment(Style.HorizontalAlignment.RIGHT);

        // For SUM indicators, don't show ZEROs in the Grid
        // (it looks better if we don't)
        if(indicator.getAggregation() == IndicatorDTO.AGGREGATE_SUM) {
            indicatorColumn.setRenderer(new GridCellRenderer() {
                @Override
                public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                    Double value = model.get(property);
                    if(value != null && value != 0) {
                        return IndicatorNumberFormat.INSTANCE.format(value);
                    } else {
                        return "";
                    }
                }
            });
        }
        return indicatorColumn;
	}
	
	public ColumnModelBuilder maybeAddTwoLineLocationColumn(ActivityDTO activity) {
		if(activity.getLocationType().getBoundAdminLevelId() == null) {
	        ReadTextColumn column = new ReadTextColumn("locationName", activity.getLocationType().getName(), 100);
	        column.setRenderer(new LocationColumnRenderer());
			columns.add(column);
        }
		return this;
	}
	
	public ColumnModelBuilder maybeAddSingleLineLocationColumn(ActivityDTO activity) {
		if(activity.getLocationType().getBoundAdminLevelId() == null) {
	        ReadTextColumn column = new ReadTextColumn("locationName", activity.getLocationType().getName(), 100);
			columns.add(column);
        }
		return this;
	}
	
	
	public ColumnModelBuilder addLocationColumn() {
        ReadTextColumn column = new ReadTextColumn("locationName", I18N.CONSTANTS.location(), 100);
		columns.add(column);
        return this;
	}

	public ColumnModelBuilder addAdminLevelColumns(ActivityDTO activity) {
		return addAdminLevelColumns(getAdminLevels(activity));
	}
	
	
	public ColumnModelBuilder addSingleAdminColumn(ActivityDTO activity) {
		ColumnConfig admin = new ColumnConfig("admin", I18N.CONSTANTS.location(), 100);
		admin.setRenderer(new AdminColumnRenderer(getAdminLevels(activity)));
		columns.add(admin);		
		return this;
	}

	public ColumnModelBuilder addAdminLevelColumns(List<AdminLevelDTO> adminLevels) {
		for(AdminLevelDTO level : adminLevels) {
			columns.add(new ColumnConfig(AdminLevelDTO.getPropertyName(level.getId()), level.getName(), 100));
		}

		return this;
	}
	
	public ColumnModelBuilder addAdminLevelColumns(UserDatabaseDTO database) {
		return addAdminLevelColumns(database.getCountry().getAdminLevels());
		
	}

	public List<AdminLevelDTO> getAdminLevels(ActivityDTO activity) {
		if( activity.getLocationType().isAdminLevel()) {
            return activity.getDatabase().getCountry().getAdminLevelAncestors(activity.getLocationType().getBoundAdminLevelId());
        } else {
        	return activity.getDatabase().getCountry().getAdminLevels();
        }
	}

	public ColumnModelBuilder maybeAddPartnerColumn(UserDatabaseDTO database) {
		if(database.isViewAllAllowed()) {
            addPartnerColumn();
        }
		return this;
	}

	public ColumnModelBuilder addPartnerColumn() {
		columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 100));
		return this;
	}

	public ColumnModelBuilder maybeAddDateColumn(ActivityDTO activity) {
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            columns.add(new EditableLocalDateColumn("date2", I18N.CONSTANTS.date(), 100));
        }
		return this;
	}

	public ColumnModelBuilder addMapColumn() {
		ColumnConfig mapColumn = new ColumnConfig("x", "", 25);
        mapColumn.setRenderer(new GridCellRenderer<ModelData>() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
            	if (model instanceof SiteDTO) {
            		SiteDTO siteModel = (SiteDTO) model;
	                if(siteModel.hasCoords()) {
	                    return "<div class='mapped'>&nbsp;&nbsp;</div>";
	                } else {
	                    return "<div class='unmapped'>&nbsp;&nbsp;</div>";
	                }
            	}
            	return " ";
            }
        });
        columns.add(mapColumn);
        return this;
	}

    public ColumnModelBuilder maybeAddKeyIndicatorColumns(ActivityDTO activity) {
    	// Only add indicators that have a queries heading
    	if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
	        for (IndicatorDTO indicator : activity.getIndicators()) {
	            if(indicator.getListHeader() != null && !indicator.getListHeader().isEmpty()) {
	                columns.add(addIndicatorColumn(indicator, indicator.getListHeader()));
	            }
	        }
    	}
        return this;
    }

	public ColumnModelBuilder addTreeNameColumn() {
		ColumnConfig name = new ColumnConfig("name", I18N.CONSTANTS.location(), 200);
		name.setRenderer(new TreeGridCellRenderer<ModelData>() {

			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {

				return super.render(model, propertyName(model), config, rowIndex, colIndex, store, grid);
			}

			private String propertyName(ModelData model) {
				if(model instanceof SiteDTO) {
					return "locationName";
				} else {
					return "name";
				}
			}
			
		});
		columns.add(name);
		
		return this;
	}

}
