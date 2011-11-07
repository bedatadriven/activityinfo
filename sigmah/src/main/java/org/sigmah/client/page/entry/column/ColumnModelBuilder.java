package org.sigmah.client.page.entry.column;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.columns.EditableLocalDateColumn;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

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

public class ColumnModelBuilder {

	List<ColumnConfig> columns = Lists.newArrayList();	
	
    public static ColumnModel buildForActivity(ActivityDTO activity) {
    	
    	return new ColumnModelBuilder()
    		.addMapColumn()
    		.maybeAddLockColumn(activity)
    		.maybeAddDateColumn(activity)
    		.maybeAddPartnerColumn(activity.getDatabase())
    		.maybeAddProjectColumn(activity.getDatabase())
    		.addAdminLevelColumns(activity)
    		.maybeAddLocationColumns(activity)
    		.build();
     }
    
    public static ColumnModel buildForDatabase(UserDatabaseDTO database) {
    	return new ColumnModelBuilder()
    		.addMapColumn()
    		.addActivityColumn(database)
    		.addLocationColumn()
    		.maybeAddPartnerColumn(database)
    		.maybeAddProjectColumn(database)
    		.addAdminLevelColumns(database)
    		.build();
    }
    
	public static ColumnModel buildForAll(SchemaDTO schema) {
		return new ColumnModelBuilder()
			.addMapColumn()
			.addActivityColumn(schema)
			.addLocationColumn()
			.build();
	}
    
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
    	ColumnConfig config = new ColumnConfig("activityId", 100);
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
		ColumnConfig columnLocked = new ColumnConfig("x", "", 24);
        columnLocked.setRenderer(new LockedColumnRenderer(activity));
        columns.add(columnLocked);
        return this;
	}
	
	public ColumnConfig addIndicatorColumn(IndicatorDTO indicator, String header) {
        final NumberFormat format = NumberFormat.getFormat("0");

        NumberField indicatorField = new NumberField();
        indicatorField.getPropertyEditor().setFormat(format);

        ColumnConfig indicatorColumn = new ColumnConfig(indicator.getPropertyName(), header, 50);

        indicatorColumn.setNumberFormat(format);
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
                        return format.format(value);
                    } else {
                        return "";
                    }
                }
            });
        }
        return indicatorColumn;
	}
	
	public ColumnModelBuilder maybeAddLocationColumns(ActivityDTO activity) {
		if(activity.getLocationType().getBoundAdminLevelId() == null) {
	        ReadTextColumn column = new ReadTextColumn("locationName", activity.getLocationType().getName(), 100);
	        column.setRenderer(new LocationColumnRenderer());
			columns.add(column);
        }
		return this;
	}
	
	public ColumnModelBuilder addLocationColumn() {
        return this;
	}

	public ColumnModelBuilder addAdminLevelColumns(ActivityDTO activity) {
		return addAdminLevelColumns(getAdminLevels(activity));
	}

	public ColumnModelBuilder addAdminLevelColumns(List<AdminLevelDTO> adminLevels) {
		ColumnConfig admin = new ColumnConfig("admin", I18N.CONSTANTS.location(), 100);
		admin.setRenderer(new AdminColumnRenderer(adminLevels));
		columns.add(admin);
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
            columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 100));
        }
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

    public ColumnModelBuilder createIndicatorColumns(ActivityDTO activity) {
    	// Only add indicators that have a queries heading
        for (IndicatorDTO indicator : activity.getIndicators()) {
            if(indicator.getListHeader() != null && !indicator.getListHeader().isEmpty()) {
                columns.add(addIndicatorColumn(indicator, indicator.getListHeader()));
            }
        }
        return this;
    }

	public ColumnModelBuilder addTreeNameColumn() {
		ColumnConfig name = new ColumnConfig("name", "Name", 50);
		name.setRenderer(new TreeGridCellRenderer<ModelData>());
		columns.add(name);
		
		return this;
	}
}
