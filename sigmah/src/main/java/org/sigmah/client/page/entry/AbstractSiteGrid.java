package org.sigmah.client.page.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.columns.EditableLocalDateColumn;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.client.page.common.grid.AbstractEditorGridView;
import org.sigmah.client.page.common.grid.GridPresenter.SiteGridPresenter;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.common.widget.CollapsibleTabPanel;
import org.sigmah.client.page.config.ShowLockedPeriodsDialog;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

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
    protected ToggleButton togglebuttonList;
    protected ToggleButton togglebuttonTreeTime;
    protected ToggleButton togglebuttonTreeGeo;
    private CollapsibleTabPanel tabPanel;
    private List<ToggleButton> sideBarButtons = new ArrayList<ToggleButton>();
    private LayoutContainer sidePanel;

    private int tabPanelExandedSize = 200;
    private boolean tabPanelCollapsed;
    private BorderLayoutData tabPanelLayout;
    
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

        toolBar.add(new SeparatorToolItem());
        
        togglebuttonList = toolBar.addToggleButton(UIActions.list, I18N.CONSTANTS.list(), IconImageBundle.ICONS.list()); 
        //togglebuttonTreeGeo = toolBar.addToggleButton(UIActions.treeGeo, "Tree geo", IconImageBundle.ICONS.treeviewAdmin());
        togglebuttonTreeTime = toolBar.addToggleButton(UIActions.treeTime, I18N.CONSTANTS.treeTime(), IconImageBundle.ICONS.treeviewTime());
        
        if (sideBarButtons.size() > 0) {
            toolBar.add(new SeparatorToolItem());
        }
        for(ToggleButton button : sideBarButtons) {
            toolBar.add(button);
        }
    }
    
    protected void toggle(ToggleButton button) {
    	togglebuttonList.toggle(false);
    	//togglebuttonTreeGeo.toggle(false);
    	togglebuttonTreeTime.toggle(false);
    	button.toggle(true);
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
	
	protected void createLocationColumn() {
		if(activity.getLocationType().getBoundAdminLevelId() == null) {
            columns.add(new ReadTextColumn("locationName", I18N.CONSTANTS.location(), 100));
            columns.add(new ReadTextColumn("locationAxe", I18N.CONSTANTS.axe(), 100));
        }
	}

	protected void createAdminLevelsColumns() {
		for (AdminLevelDTO level : levels) {
            columns.add(new ColumnConfig(level.getPropertyName(), level.getName(), 75));
        }
	}

	protected void getAdminLevels() {
		if( activity.getLocationType().isAdminLevel()) {
            levels = activity.getDatabase().getCountry().getAdminLevelAncestors(activity.getLocationType().getBoundAdminLevelId());
        } else {
            levels = activity.getDatabase().getCountry().getAdminLevels();
        }
	}

	protected void createPartnerColumn() {
		if(activity.getDatabase().isViewAllAllowed()) {
            columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 100));
        }
	}

	protected void createDateColumn() {
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            columns.add(new EditableLocalDateColumn("date2", I18N.CONSTANTS.date(), 100));
        }
	}

	protected void createMapColumn() {
		ColumnConfig mapColumn = new ColumnConfig("x", "", 25);
        mapColumn.setRenderer(new GridCellRenderer<SiteDTO>() {
            @Override
            public Object render(SiteDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
            	if (model instanceof MonthViewModel || model instanceof YearViewModel || model instanceof AdminLevelViewModel) {
            		return "";
            	}
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
	
    public void addSidePanel(String name, AbstractImagePrototype icon, final Component component) {
        final ToggleButton sideBarButton = new ToggleButton(name, icon);
        sideBarButton.setToggleGroup("sideBar");
        sideBarButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                BorderLayout borderLayout = (BorderLayout) getLayout();
                if (sideBarButton.isPressed()) {

                    if (sidePanel == null) {
                        sidePanel = new LayoutContainer();
                        sidePanel.setLayout(new CardLayout());

                        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST, 0.4f);
                        east.setSplit(true);
                        east.setMargins(new Margins(0, 0, 0, 5));

                        add(sidePanel, east);
                    } else if(isRendered()) {
                        borderLayout.show(Style.LayoutRegion.EAST);
                    }
                    if (!component.isAttached()) {
                        sidePanel.add(component);
                    }
                    ((CardLayout)sidePanel.getLayout()).setActiveItem(component);
                    borderLayout.layout();
                } else {
                    borderLayout.hide(Style.LayoutRegion.EAST);
                }
            }
        });
        sideBarButtons.add(sideBarButton);
    }

    public void addSouthTab(TabItem tab) {
        if(tabPanel == null) {
            tabPanelLayout = new BorderLayoutData(Style.LayoutRegion.SOUTH);
            tabPanelLayout.setCollapsible(true);
            tabPanelLayout.setSplit(true);
            tabPanelLayout.setMargins(new Margins(5, 0, 0, 0));

            tabPanel = new CollapsibleTabPanel();
            tabPanel.setTabPosition(TabPanel.TabPosition.BOTTOM);
            tabPanel.setAutoSelect(false);
            add(tabPanel, tabPanelLayout);
        }

        tab.getHeader().addListener(Events.BrowserEvent, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                if(be.getEventTypeInt() == Event.ONCLICK) {
                    onTabClicked((TabItem.HeaderItem) be.getComponent());
                }
            }
        });

        tabPanel.add(tab);
    }

    private void onTabClicked(TabItem.HeaderItem header) {
        if(tabPanel.getSelectedItem()!=null && tabPanel.getSelectedItem().getHeader() == header) {
            if(!tabPanelCollapsed) {
                // "collapse" tab panel - show only the tab strip
                collapseTabs();
            } else {
                // expand tab panel to previous size
                expandTabs();
            }
            getLayout().layout();
        } else if(tabPanelCollapsed) {
            expandTabs();
            getLayout().layout();
        }
    }

    private void collapseTabs() {
        tabPanelExandedSize = (int)tabPanelLayout.getSize();
        tabPanelLayout.setSize(tabPanel.getBar().getHeight());
        tabPanelLayout.setMargins(new Margins(0));
        tabPanel.getBody().setVisible(false);
        tabPanelLayout.setSplit(false);
        tabPanelCollapsed = true;
    }

    private void expandTabs() {
        tabPanel.getBody().setVisible(true);
        tabPanelLayout.setSize(tabPanelExandedSize);
        tabPanelLayout.setMargins(new Margins(5, 0, 0, 0));
        tabPanelLayout.setSplit(true);
        tabPanelCollapsed = false;
    }
}
