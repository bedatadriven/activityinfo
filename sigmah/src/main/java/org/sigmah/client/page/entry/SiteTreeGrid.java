package org.sigmah.client.page.entry;

import java.util.Arrays;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.SiteTreeGridPageState.TreeType;
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
	
	public SiteTreeGrid(boolean enableDragSource) {
		super(enableDragSource);
	}

	@Override 
	public Grid<SiteDTO> createGridAndAddToContainer(Store store) {
        grid = new EditorTreeGrid<SiteDTO>(treePresenter.getStore(), createColumnModel(activity));
        
        grid.setLoadMask(true); 
        grid.setStateful(true);
        grid.setStateId("site-treeview" + activity.getId());
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
		if (treePresenter.getPlace().getTreeType() == TreeType.GEO) {
			createGeoColumns();
		}
		if (treePresenter.getPlace().getTreeType() == TreeType.TIME) {
			createTimeColumns();
		}
        return new ColumnModel(columns);
	}
	
	private void createTimeColumns() {
		ColumnConfig name = new ColumnConfig("name", I18N.CONSTANTS.date(), 120);
		name.setRenderer(new TreeGridCellRenderer<ModelData>());
		columns.add(name);

		createMapColumn();
        createLockColumn();
        createPartnerColumn();
        
        // Only show Project column when the database has projects
        if (!activity.getDatabase().getProjects().isEmpty()) {
            createProjectColumn();
        }
        
        createLocationColumn();
        createIndicatorColumns();

        getAdminLevels();
        createAdminLevelsColumns();
	}
	
	private void createGeoColumns() {
		ColumnConfig name = new ColumnConfig("name", "Name", 50);
		name.setRenderer(new TreeGridCellRenderer<ModelData>());
		columns.add(name);

		createMapColumn();
        createLockColumn();
        createDateColumn();
        createPartnerColumn();
        
        // Only show Project column when the database has projects
        if (!activity.getDatabase().getProjects().isEmpty()) {
            createProjectColumn();
        }
        
        createLocationColumn();
        createIndicatorColumns();

        getAdminLevels();
	}
	
	

	@Override
	public void setSelection(int siteId) {
		for (SiteDTO site : treePresenter.getStore().getAllItems()) {
			if (site.getId() == siteId) {
				grid.getSelectionModel().select(site, false);
				return;
			}
		}
	}

	@Override
	public void setSite(SiteDTO selectedSite) {
		setSelection(selectedSite.getId());
	}

	@Override
	public void update(SiteDTO updatedSite) {
		for (SiteDTO site : treePresenter.getStore().getAllItems()) {
			if (site instanceof MonthViewModel) {
				MonthViewModel month = (MonthViewModel)site;
				if (month.getYear() == site.getDate2().getYear() &&
						month.getMonth() == site.getDate2().getMonthOfYear()) {
					treePresenter.getStore().add(month, Arrays.asList(updatedSite), true);
				}
			}
		}
	}

	@Override
	public void setSelected(int id) {
		setSelection(id);
	}

	@Override
	public void remove(SiteDTO removedSite) {
		for (SiteDTO site : treePresenter.getStore().getAllItems()) {
			if (site.getId() == removedSite.getId()) {
				treePresenter.getStore().remove(site);
			}
		}
	}

	@Override
	public void init(SiteTreeEditor presenter, ActivityDTO activity, TreeStore<SiteDTO> store) {
		this.treePresenter=presenter;
		this.activity = presenter.getCurrentActivity();

		super.init(presenter, store);
		updateMode();
	}

	@Override
	public void updateMode() {
		if (treePresenter.getPlace().getTreeType() == TreeType.GEO) {
			toggle(togglebuttonTreeGeo);
		}
		if (treePresenter.getPlace().getTreeType() == TreeType.TIME) {
			toggle(togglebuttonTreeTime);
		}
	}

}
