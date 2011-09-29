package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;

public class SiteTreeGrid extends AbstractSiteGrid implements SiteTreeEditor.View {
	private EditorTreeGrid<SiteDTO> grid;
	private SiteTreeEditor treePresenter;
	
	public SiteTreeGrid() {
		super();
	}

	public SiteTreeGrid(boolean enableDragSource) {
		super(enableDragSource);
	}

	@Override 
	public Grid<SiteDTO> createGridAndAddToContainer(Store store) {
        grid = new EditorTreeGrid<SiteDTO>(treePresenter.getStore(), createColumnModel(activity));
        
        grid.setLoadMask(true); 
        grid.setStateful(true);
        grid.setStateId("SiteGrid" + activity.getId());
        grid.setAutoExpandColumn("name");
        
        GridSelectionModel<SiteDTO> sm = new GridSelectionModel<SiteDTO>();
        sm.setSelectionMode(SelectionMode.SINGLE);
		grid.setSelectionModel(sm);
		
        grid.setClicksToEdit(ClicksToEdit.TWO); 

        add(grid, new BorderLayoutData(Style.LayoutRegion.CENTER));

        if(enableDragSource) {
            new SiteGridDragSource(grid);
        }

        return grid;
	}

	@Override
	protected ColumnModel createColumnModel(ActivityDTO activity) {
		ColumnConfig name = new ColumnConfig("name", "Name", 50);
		name.setRenderer(new TreeGridCellRenderer<ModelData>());
		columns.add(name);
		return super.createColumnModel(activity);
	}

	@Override
	public void setSelection(int siteId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSite(SiteDTO selectedSite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(SiteDTO site) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelected(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(SiteDTO site) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(SiteTreeEditor presenter, ActivityDTO activity,
			TreeStore<SiteDTO> store) {
		this.treePresenter=presenter;
		this.activity = presenter.getCurrentActivity();
		super.init(presenter, store);
	}

}
