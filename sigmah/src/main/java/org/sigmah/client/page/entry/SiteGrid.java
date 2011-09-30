/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import java.util.Collections;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/** Displays a list of Indicators and allows the user to edit various columns in the grid */
public class SiteGrid extends AbstractSiteGrid implements SiteEditor.View {
    protected EditorGrid<SiteDTO> editorGrid;
    private ListStore<SiteDTO> listStore;

    public SiteGrid(boolean enableDragSource) {
        super(enableDragSource);
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
    	editorGrid = new EditorGrid<SiteDTO>((ListStore)store, createColumnModel(activity));
        
    	editorGrid.setLoadMask(true);
    	editorGrid.setStateful(true);
    	editorGrid.setStateId("SiteGrid" + activity.getId());
        
        GridSelectionModel<SiteDTO> sm = new GridSelectionModel<SiteDTO>();
        sm.setSelectionMode(SelectionMode.SINGLE);
        editorGrid.setSelectionModel(sm);
		
		editorGrid.setClicksToEdit(ClicksToEdit.TWO);

        add(editorGrid, new BorderLayoutData(Style.LayoutRegion.CENTER));

        if(enableDragSource) {
            new SiteGridDragSource(editorGrid);
        }

        return editorGrid;
    }

    public void setSelection(int siteId) {
        for(int r=0; r!=listStore.getCount(); ++r) {
            if(listStore.getAt(r).getId() == siteId) {
            	this.currentSite=listStore.getAt(r);
            	editorGrid.getView().ensureVisible(r, 0, false);
                if(editorGrid.getSelectionModel() instanceof CellSelectionModel) {
                    ((CellSelectionModel) editorGrid.getSelectionModel()).selectCell(r, 0);                	
                } else {
                	editorGrid.getSelectionModel().setSelection(Collections.singletonList(listStore.getAt(r)));
                }
            }
        }
    }

	@Override
	public void setSite(SiteDTO selectedSite) {
		currentSite = selectedSite;
	}

	@Override
	public void update(SiteDTO site) {
		listStore.update(site);
	}

	@Override
	public void setSelected(int id) {
        SiteDTO site = listStore.findModel("id", id);
        if (site != null) {
            setSelection(id);
        }
	}

	@Override
	public void remove(SiteDTO site) {
		listStore.remove(site);
	}
}
