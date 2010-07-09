/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.event.PivotCellEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.table.drilldown.DrillDownEditor;
import org.sigmah.client.page.table.drilldown.DrillDownGrid;
import org.sigmah.client.util.DateUtilGWTImpl;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionFolder;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPage extends LayoutContainer implements PivotPresenter.View {

    protected EventBus eventBus;
    protected Dispatcher service;
    protected IStateManager stateMgr;

    protected PivotPresenter presenter;
    
    protected ListStore<Dimension> rowDims;
    protected ListStore<Dimension> colDims;
   
    protected TreeLoader<ModelData> loader;
	protected TreePanel tree;
	protected TreeStore<ModelData> store;
    
    protected ContentPanel filterPane;
    protected IndicatorTreePanel indicatorPanel;
    protected AdminFilterPanel adminPanel;
    protected DateRangePanel datePanel;
    protected PartnerFilterPanel partnerPanel;
    protected LayoutContainer center;
    protected PivotGridPanel gridContainer;
    protected ToolBar gridToolBar;
    protected DrillDownGrid drilldownPanel;
    private Listener<PivotCellEvent> initialDrillDownListener;

    @Inject
    public PivotPage(EventBus eventBus, Dispatcher service, IStateManager stateMgr) {

        this.eventBus = eventBus;
        this.service = service;
        this.stateMgr = stateMgr;

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setEnableState(true);
        setStateId("pivotPage");
        setLayout(borderLayout);

        createPane();
        createFilterPane();
        createIndicatorPanel();
        createAdminFilter();
        createDateFilter();
        createPartnerFilter();
        createGridContainer();
        
        initialDrillDownListener = new Listener<PivotCellEvent>() {
            public void handleEvent(PivotCellEvent be) {
                createDrilldownPanel(be);
            }
        };
        eventBus.addListener(AppEvents.Drilldown, initialDrillDownListener);
    }

	public void createPane() {

        ContentPanel pane = new ContentPanel();
        pane.setHeading(I18N.CONSTANTS.dimensions());
        
        // tree loader
		loader = new BaseTreeLoader<ModelData>(new Proxy()) {
			@Override
			public boolean hasChildren(ModelData parent) {
				if (parent instanceof AttributeGroupDTO) {
					return !((AttributeGroupDTO)parent).isEmpty();
				}
				
				return parent instanceof DimensionFolder 
					|| parent instanceof AttributeGroupDTO;
			}
		};

		// tree store
		TreeStore<ModelData> store = new TreeStore<ModelData>(loader);
		store.setKeyProvider(new ModelKeyProvider<ModelData>() {
			public String getKey(ModelData model) {
				return "node_" + model.get("id");
			}
		});

		tree = new TreePanel<ModelData>(store);
		tree.setStateful(true);
		tree.setLabelProvider(new ModelStringProvider<ModelData>() {
			public String getStringValue(ModelData model, String property) {
				return trim((String)model.get("caption"));
			}
		});

		tree.setId("statefullavaildims");
		   
        TreePanelDragSource source = new TreePanelDragSource(tree);
        source.setTreeSource(DND.TreeSource.LEAF);
        TreePanelDropTarget target = new TreePanelDropTarget(tree);
        
        target.setFeedback(DND.Feedback.INSERT);
        target.setAllowSelfAsSource(true);
		
		add(tree);
		tree.collapseAll();
        
        
        VBoxLayout layout = new VBoxLayout();
        layout.setPadding(new Padding(5));
        layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);

        pane.setLayout(layout);

        VBoxLayoutData labelLayout = new VBoxLayoutData();

        VBoxLayoutData listLayout = new VBoxLayoutData();
        listLayout.setFlex(1.0);
       
        pane.add(tree, listLayout); 
        pane.add(new Text(I18N.CONSTANTS.rows()), labelLayout);

        rowDims = createStore();
        
        pane.add(createList(rowDims), listLayout);
        pane.add(new Text(I18N.CONSTANTS.columns()), labelLayout);

        colDims = createStore();
        pane.add(createList(colDims), listLayout);

        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST);
        east.setCollapsible(true);
        east.setSplit(true);
        east.setMargins(new Margins(0, 5, 0, 0));
	
        add(pane, east);
        
    }	
	
    private ListStore<Dimension> createStore() {
        ListStore<Dimension> store = new ListStore<Dimension>();
        store.addStoreListener(new StoreListener<Dimension>() {
            @Override
            public void storeDataChanged(StoreEvent<Dimension> se) {
                if (presenter != null) {
                    presenter.onDimensionsChanged();
                }
            }
        });
        return store;
    }

    private ListView createList(ListStore<Dimension> store) {
        ListView<Dimension> list = new ListView<Dimension>(store);
        list.setDisplayProperty("caption");

        ListViewDragSource source = new ListViewDragSource(list);

        ListViewDropTarget target = new ListViewDropTarget(list);
        target.setFeedback(DND.Feedback.INSERT);
        target.setAllowSelfAsSource(true);

        return list;
    }
  
    private void createFilterPane() {
        filterPane = new ContentPanel();
        filterPane.setHeading(I18N.CONSTANTS.filter());
        filterPane.setIcon(IconImageBundle.ICONS.filter());
        filterPane.setLayout(new AccordionLayout());

        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST);
        west.setMinSize(250);
        west.setSize(250);
        west.setCollapsible(true);
        west.setSplit(true);
        west.setMargins(new Margins(0, 0, 0, 5));

        add(filterPane, west);
    }

    private void createIndicatorPanel() {
        indicatorPanel = new IndicatorTreePanel(service, true);
        indicatorPanel.setHeaderVisible(true);
        indicatorPanel.setHeading(I18N.CONSTANTS.indicators());
        indicatorPanel.setIcon(IconImageBundle.ICONS.indicator());

        filterPane.add(indicatorPanel);
    }


    private void createAdminFilter() {
        adminPanel = new AdminFilterPanel(service);
        adminPanel.setHeading(I18N.CONSTANTS.filterByGeography());
        adminPanel.setIcon(IconImageBundle.ICONS.filter());

        filterPane.add(adminPanel);
    }

    private void createDateFilter() {
        datePanel = new DateRangePanel();

        filterPane.add(datePanel);
    }

    private void createPartnerFilter() {
		 partnerPanel = new PartnerFilterPanel(service);
		 
		 filterPane.add(partnerPanel);
	}
    
    private void createGridContainer() {

        center = new LayoutContainer();
        center.setLayout(new BorderLayout());
        add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));

        gridContainer = new PivotGridPanel(eventBus);
        gridContainer.setHeaderVisible(true);
        gridContainer.setHeading(I18N.CONSTANTS.preview());

        gridToolBar = new ToolBar();
        gridContainer.setTopComponent(gridToolBar);

        SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (presenter != null && ce.getButton().getItemId() != null) {
                    presenter.onUIAction(ce.getButton().getItemId());
                }
            }
        };

        Button refresh = new Button(I18N.CONSTANTS.refreshPreview(),
                IconImageBundle.ICONS.refresh(), listener);
        refresh.setItemId(UIActions.refresh);
        gridToolBar.add(refresh);

//        Button filter = new Button(Application.CONSTANTS.filter(),
//                Application.ICONS.filter(), listener);
//        filter.setItemId(UIActions.filter);
//        gridToolBar.add(filter);


        Button export = new Button(I18N.CONSTANTS.export(),
                IconImageBundle.ICONS.excel(), listener);
        export.setItemId(UIActions.export);
        gridToolBar.add(export);

        center.add(gridContainer, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createDrilldownPanel(PivotCellEvent event) {

        BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.SOUTH);
        layout.setSplit(true);
        layout.setCollapsible(true);

        drilldownPanel = new DrillDownGrid();
        DrillDownEditor drilldownEditor = new DrillDownEditor(eventBus, service, stateMgr, new DateUtilGWTImpl(), drilldownPanel);
        drilldownEditor.onDrillDown(event);

        center.add(drilldownPanel, layout);

        // disconnect our initial drilldown listener;
        // subsequent events will be handled by the DrillDownEditor's listener
        eventBus.removeListener(AppEvents.Drilldown, initialDrillDownListener);

        layout();

    }

    @Override
    public ListStore<Dimension> getRowStore() {
        return rowDims;
    }

    @Override
    public ListStore<Dimension> getColStore() {
        return colDims;
    }

    public void setSchema(SchemaDTO result) {
        //    indicatorPanel.setSchema(result);
    }

    public void bindPresenter(PivotPresenter presenter) {

        this.presenter = presenter;
    }

    public void enableUIAction(String actionId, boolean enabled) {
        Component button = gridToolBar.getItemByItemId(actionId);
        if (button != null) {
            button.setEnabled(enabled);
        }
    }

    public void setContent(PivotTableElement element) {
        gridContainer.setData(element);
    }

    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
    }

    public List<IndicatorDTO> getSelectedIndicators() {
        return indicatorPanel.getSelection();
    }

    public List<AdminEntityDTO> getAdminRestrictions() {
        return adminPanel.getSelection();
    }

    public Date getMinDate() {
        return datePanel.getMinDate();
    }

    public Date getMaxDate() {
        return datePanel.getMaxDate();
    }
    
    public List<PartnerDTO> getPartnerRestrictions() {
    	return partnerPanel.getSelection();
    }
    
    private class DimFolder extends BaseModelData {
    	private DimensionType type;
    	private String name;
    	
    	public DimFolder (DimensionType type, String name) {
    		this.name = name;
    		this.type = type;
    	}
    	
    	public DimensionType getType () {
    		return this.type;
    	}
    	
    	public String getName() {
    		return this.name;
    	}
    	
    	public String get(String field) {
    		if ("type".equals(field)) {
    			return "" + this.type;
    		} else if ("name".equals(field)) {
    			return this.name;
    		} else {
    			return null;
    		}
    	}
    }
  
    
    private class Proxy implements DataProxy<List<ModelData>> {

		private SchemaDTO schema;
		private HashMap <String, AttributeGroupDTO > attribGroups = new HashMap <String, AttributeGroupDTO> ();
		private int idSeq = 0;
		
		public void load(DataReader<List<ModelData>> listDataReader,
				Object parent, final AsyncCallback<List<ModelData>> callback) {

			// load root
			if (parent == null) {
				final ArrayList<ModelData> dims = new ArrayList<ModelData>();
			
			   	dims.add(createDateDimension(DateUnit.YEAR, I18N.CONSTANTS.year()));
                dims.add(createDateDimension(DateUnit.QUARTER, I18N.CONSTANTS.quarter()));
                dims.add(createDateDimension(DateUnit.MONTH, I18N.CONSTANTS.month()));
                
				if (schema == null) {
					service.execute(new GetSchema(), getMonitor(),
							new AsyncCallback<SchemaDTO>() {
								public void onFailure(Throwable caught) {
									callback.onFailure(caught);
								}

								public void onSuccess(SchemaDTO result) {
									schema = result;
									for (CountryDTO country : schema.getCountries()) {
						                for (AdminLevelDTO level : country.getAdminLevels()) {
						                	AdminDimension dimension = new AdminDimension(level.getId());
						                	dimension.set("id", "admin_dim_" + idSeq++);
						                	dimension.set("caption", level.getName());						                
						                    dims.add(dimension);
						                	
						                }
						            }
									dims.add(createDimensionFolder( DimensionType.Attribute, "Attributes"));
									callback.onSuccess(dims);
								}
							});
				} else {
					callback.onSuccess(dims);
				}
				
			// load attribute groups
			} else if (parent instanceof DimensionFolder) {
				DimensionType type = ((DimensionFolder) parent).getType();
				final ArrayList<ModelData> list = new ArrayList<ModelData> ();
				
			  	for (UserDatabaseDTO db: schema.getDatabases()) {
    	    		for (ActivityDTO act: db.getActivities()) {
    	    			for (AttributeGroupDTO group: act.getAttributeGroups()) {
    	    				if (group.get("name") != null 
    	    						&& !"".equals(group.get("name"))
    	    						&& !group.isEmpty()) {
    	 
    	    					if (!attribGroups.containsKey(group.getName())){
    	    						group.set("caption",group.getName());
    	    						list.add(group);
    	    						attribGroups.put((String)group.get("name"), group);
    	    					}
    	    				}
    	    			}
    	    		}
    	    	}
				callback.onSuccess(list);
				 
			//load attributes
			} else if (parent instanceof AttributeGroupDTO) {
				
				if (attribGroups.containsKey(((AttributeGroupDTO)parent).get("name"))) {
					AttributeGroupDTO g  = attribGroups.get(((AttributeGroupDTO)parent).get("name"));
					List<AttributeDTO> attrs =  g.getAttributes();
					ArrayList<ModelData> list = new ArrayList <ModelData>();
					for (AttributeDTO a: attrs) {
						Dimension d = createDimension(DimensionType.Attribute, a.getName());
						d.set("id", a.getId());
						list.add(d);
					}
					callback.onSuccess(list);
				}
			}
		}
    
		private DimensionFolder createDimensionFolder(DimensionType type, String caption) {
		    DimensionFolder dim = new DimensionFolder(caption,type, "dim_folder_" + idSeq++);
		    dim.set("caption", caption);
		    return dim;
		}
		
		private Dimension createDimension(DimensionType type, String caption) {
		    Dimension dim = new Dimension(type);
		    dim.set("caption", caption);
		    return dim;
		}
		
		private Dimension createDateDimension(DateUnit unit, String caption) {
		    Dimension dim = new DateDimension(unit);
		    dim.set("caption", caption);
		    dim.set("id", "dim_date_" + idSeq++ );
		    return dim;
		}
    }

	private String trim(String s) {
		if (s == null || "".equals(s)) {
			return "NO_NAME";
		} 
		s = s.trim();
		if (s.length() > 20) {
			return s.substring(0,19) + "...";
		} else {
			return s;
		}
	}
		
}
