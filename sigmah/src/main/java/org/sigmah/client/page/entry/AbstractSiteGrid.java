package org.sigmah.client.page.entry;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.columns.EditableLocalDateColumn;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.client.page.common.grid.AbstractEditorGridView;
import org.sigmah.client.page.common.grid.GridPresenter.SiteGridPresenter;
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
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
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
import com.google.gwt.i18n.client.NumberFormat;
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
    protected Grid<ModelData> grid;
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


    
    @Override
	protected <D extends ModelData> Grid<D> createGridAndAddToContainer(
			Store store) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initToolBar() {
		// TODO Auto-generated method stub
		
	}

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
    
    protected void toggle(ToggleButton button) {
    	togglebuttonList.toggle(false);
    	togglebuttonTreeGeo.toggle(false);
    	togglebuttonTreeTime.toggle(false);
    	button.toggle(true);
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

        }

    }

}
