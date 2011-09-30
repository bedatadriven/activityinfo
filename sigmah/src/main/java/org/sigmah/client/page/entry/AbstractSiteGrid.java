package org.sigmah.client.page.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.columns.EditTextColumn;
import org.sigmah.client.page.common.columns.EditableLocalDateColumn;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.client.page.common.grid.AbstractEditorGridView;
import org.sigmah.client.page.common.grid.GridPresenter.SiteGridPresenter;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.ShowLockedPeriodsDialog;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.i18n.client.NumberFormat;

public abstract class AbstractSiteGrid
	extends 
		AbstractEditorGridView<SiteDTO, SiteGridPresenter> 
	implements 
		AbstractSiteEditor.View 
{
	public class SiteGridDragSource extends DragSource {
	    private final Grid grid;

	    public SiteGridDragSource(Grid grid) {
	        super(grid);
	        this.grid = grid;
	    }

	    @Override
	    protected void onDragStart(DNDEvent e) {
	        int rowIndex = grid.getView().findRowIndex(e.getTarget());
	        if (rowIndex == -1) {
	            e.setCancelled(true);
	            return;
	        }

	        ModelData site = grid.getStore().getAt(rowIndex);

	        e.setCancelled(false);
	        e.setData(grid.getStore().getRecord(site));
	        e.getStatus().update("");
	        e.getStatus().update("Déposer sur le carte");
	    }
	}
	
	protected ActivityDTO activity;
    protected Grid<SiteDTO> grid;
    protected boolean enableDragSource;
    protected List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    protected List<AdminLevelDTO> levels;
    protected final ShowLockedPeriodsDialog showLockedPeriods = new ShowLockedPeriodsDialog();
    protected SiteDTO currentSite;

    public AbstractSiteGrid(boolean enableDragSource) {
        this();
        
        this.enableDragSource = enableDragSource;
    }

    public AbstractSiteGrid() {
        initializeComponent();
    }

	private void initializeComponent() {
		this.setLayout(new BorderLayout());
	}

    @Override
    public AsyncMonitor getLoadingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
    }

    protected void initToolBar() {
        toolBar.addSaveSplitButton();
        toolBar.add(new SeparatorToolItem());
        
        toolBar.addButton(UIActions.add, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addEditButton();
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
        toolBar.addLockedPeriodsButton();
        
        toolBar.addButton(UIActions.list, "Sites list", IconImageBundle.ICONS.list());
        toolBar.addButton(UIActions.treeGeo, "Tree geo", IconImageBundle.ICONS.treeviewAdmin());
        toolBar.addButton(UIActions.treeTime, "Tree time", IconImageBundle.ICONS.treeviewTime());
    }

    protected ColumnModel createColumnModel(ActivityDTO activity) {
        createMapColumn();
        createLockColumn();
        createDateColumn();
        createPartnerColumn();
        
        // Only show Project column when the database has projects
        if (!activity.getDatabase().getProjects().isEmpty()) {
            createProjectColumn();
        }
        createLocationColumn();
        //createIndicatorColumns();

        getAdminLevels();
        createAdminLevelsColumns();
        //createGeographyColumn();

        return new ColumnModel(columns);
    }
    
    protected void createGeographyColumn() {
		if(activity.getDatabase().isViewAllAllowed()) {
			ColumnConfig columnGeography = new ColumnConfig();
			GridCellRenderer<SiteDTO> projectRenderer = new GridCellRenderer<SiteDTO>() {

				@Override
				public Object render(SiteDTO model, String property, ColumnData config,
						int rowIndex, int colIndex, ListStore<SiteDTO> store,
						Grid<SiteDTO> grid) {
					return null;
				}
			};
			columnGeography.setRenderer(projectRenderer);
			columnGeography.setHeader(I18N.CONSTANTS.geography());
			columnGeography.setWidth(100);
			columns.add(columnGeography);
        }
	}

	protected void createProjectColumn() {
		if(activity.getDatabase().isViewAllAllowed()) {
			columns.add(new ReadTextColumn("project", I18N.CONSTANTS.project(), 100));
        }
	}

	protected void createLockColumn() {
		ColumnConfig columnLocked = new ColumnConfig("x", "", 24);
        columnLocked.setRenderer(new GridCellRenderer<SiteDTO>() {
            @Override
            public Object render(SiteDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
            	StringBuilder builder = new StringBuilder();
            	if (model.fallsWithinLockedPeriod(activity)) {
            		String tooltip = buildTooltip(model, activity);
            		
            		//builder.append("<span qtip='");
            		//builder.append(tooltip);
            		//builder.append("'>");
            		builder.append(IconImageBundle.ICONS.lockedPeriod().getHTML());
            		//builder.append("</span>");
            		return builder.toString();
            	} else {
            		return "";
            	}
            }

			private String buildTooltip(SiteDTO model, ActivityDTO activity) {
				Set<LockedPeriodDTO> lockedPeriods = model.getAffectedLockedPeriods(activity);
				for (LockedPeriodDTO lockedPeriod : lockedPeriods) {

				}
				return "woei! tooltip";
			}
        });
        columns.add(columnLocked);
	}
	
	protected ColumnConfig createIndicatorColumn(IndicatorDTO indicator, String header) {
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
	
	private void createLocationColumn() {
		if(activity.getLocationType().getBoundAdminLevelId() == null) {
            columns.add(new EditTextColumn("locationName", I18N.CONSTANTS.location(), 100));
            columns.add(new EditTextColumn("locationAxe", I18N.CONSTANTS.axe(), 100));
        }
	}

	private void createAdminLevelsColumns() {
		for (AdminLevelDTO level : levels) {
            columns.add(new ColumnConfig(level.getPropertyName(), level.getName(), 75));
        }
	}

	private void getAdminLevels() {
		if( activity.getLocationType().isAdminLevel()) {
            levels = activity.getDatabase().getCountry().getAdminLevelAncestors(activity.getLocationType().getBoundAdminLevelId());
        } else {
            levels = activity.getDatabase().getCountry().getAdminLevels();
        }
	}

	private void createPartnerColumn() {
		if(activity.getDatabase().isViewAllAllowed()) {
            columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 100));
        }
	}

	private void createDateColumn() {
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            columns.add(new EditableLocalDateColumn("date2", I18N.CONSTANTS.date(), 100));
        }
	}

	private void createMapColumn() {
		ColumnConfig mapColumn = new ColumnConfig("x", "", 25);
        mapColumn.setRenderer(new GridCellRenderer<SiteDTO>() {
            @Override
            public Object render(SiteDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                if(model.hasCoords()) {
                    return "<div class='mapped'>&nbsp;&nbsp;</div>";
                } else {
                    return "<div class='unmapped'>&nbsp;&nbsp;</div>";
                }
            }
        });
        columns.add(mapColumn);
	}

    protected void createIndicatorColumns() {
    	// Only add indicators that have a queries heading
        for (IndicatorDTO indicator : activity.getIndicators()) {
            if(indicator.getListHeader() != null && !indicator.getListHeader().isEmpty()) {
                columns.add(createIndicatorColumn(indicator, indicator.getListHeader()));
            }
        }
    }

	@Override
	public void showLockedPeriods(List<LockedPeriodDTO> list) {
		showLockedPeriods.show();
		showLockedPeriods.setActivityFilter(activity);
    	showLockedPeriods.setValue(list);
    	showLockedPeriods.setHeader(I18N.MESSAGES.showLockedPeriodsTitle
    			(activity.getDatabase().getName(), 
    					currentSite.getProjectName(), 
    					activity.getName()));
	}
}
