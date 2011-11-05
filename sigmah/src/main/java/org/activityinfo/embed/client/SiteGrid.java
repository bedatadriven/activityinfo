package org.activityinfo.embed.client;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.AbstractSiteGrid;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class SiteGrid extends AbstractSiteGrid implements SiteEditor.View  {

	protected Grid<SiteDTO> grid;
	private ListStore<SiteDTO> listStore;

	public SiteGrid() {

	}

	@Override
	public void init(SiteEditor presenter, ActivityDTO activity, ListStore<SiteDTO> store) {
        this.activity = activity;
        this.listStore=store;
        
        setHeading(I18N.MESSAGES.activityTitle(activity.getDatabase().getName(), activity.getName()));
        super.init(presenter, listStore);
        toggle(togglebuttonList);
	}

	public Grid<SiteDTO> createGridAndAddToContainer(Store store) {
		grid = new Grid<SiteDTO>((ListStore) store,	createColumnModel(activity));

		grid.setLoadMask(true);
		grid.setStateful(true);
		grid.setStateId("SiteGrid" + activity.getId());

		GridSelectionModel<SiteDTO> sm = new GridSelectionModel<SiteDTO>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		grid.setSelectionModel(sm);

		add(grid, new BorderLayoutData(Style.LayoutRegion.CENTER));

		if (enableDragSource) {
			new SiteGridDragSource(grid);
		}

		return grid;
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


}
