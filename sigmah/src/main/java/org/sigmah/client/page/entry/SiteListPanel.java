/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.column.ColumnModelBuilder;
import org.sigmah.client.page.entry.place.ActivityDataEntryPlace;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.client.page.entry.place.DatabaseDataEntryPlace;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/** 
 * Displays a list of activity sites 
 */
public class SiteListPanel extends ContentPanel {
    private final Dispatcher dispatcher;
	
	private EditorGrid<SiteDTO> editorGrid;
    private ListStore<SiteDTO> listStore;
    private PagingToolBar pagingToolBar;

    private DataEntryPlace currentPlace = new DataEntryPlace();
    
    	
    @Inject
    public SiteListPanel(Dispatcher dispatcher) {
    	this.dispatcher = dispatcher;
    	
    	setHeaderVisible(false);
    	setLayout(new FitLayout());
    	
    	pagingToolBar = new PagingToolBar(50);
    	setBottomComponent(pagingToolBar);
    }
	
    public void navigate(DataEntryPlace place) {
    	this.currentPlace = place;
    	if(place instanceof ActivityDataEntryPlace) {
    		showActivity(((ActivityDataEntryPlace) place).getActivityId());
    	} else if(place instanceof DatabaseDataEntryPlace) {
    		showDatabase(((DatabaseDataEntryPlace) place).getDatabaseId());
    	}
	}
    
    public void loadAll(SchemaDTO schema) {
    	initGrid(ColumnModelBuilder.buildForAll(schema));
    }
    
    public void showDatabase(final int databaseId) {
    	dispatcher.execute(new GetSchema(), new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading()), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
			
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				showDatabase(result.getDatabaseById(databaseId));
			}
    	});
    }
    
    public void showDatabase(UserDatabaseDTO database) {
    	initGrid(ColumnModelBuilder.buildForDatabase(database));
    }
    
    public void showActivity(final int activityId) {
    	dispatcher.execute(new GetSchema(), new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading()), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SchemaDTO schema) {
				showActivity(schema.getActivityById(activityId));
			}
    	});
    }
    
    public void showActivity(ActivityDTO activity) {
    	initGrid(ColumnModelBuilder.buildForActivity(activity));
    }
//
//    public void setSelection(int siteId) {
//        for(int r=0; r!=listStore.getCount(); ++r) {
//            if(listStore.getAt(r).getId() == siteId) {
//            	this.currentSite=listStore.getAt(r);
//            	editorGrid.getView().ensureVisible(r, 0, false);
//                if(editorGrid.getSelectionModel() instanceof CellSelectionModel) {
//                    ((CellSelectionModel) editorGrid.getSelectionModel()).selectCell(r, 0);                	
//                } else {
//                	editorGrid.getSelectionModel().setSelection(Collections.singletonList(listStore.getAt(r)));
//                }
//            }
//        }
//    }

	private void initGrid(ColumnModel columnModel) {
		
		PagingLoader<PagingLoadResult<SiteDTO>> loader = new BasePagingLoader<PagingLoadResult<SiteDTO>>(new SiteProxy());
		pagingToolBar.bind(loader);
		listStore = new ListStore<SiteDTO>(loader);
    	
    	if(editorGrid == null) {
    		editorGrid = new EditorGrid<SiteDTO>(listStore, columnModel);
	    	editorGrid.setLoadMask(true);
	    	editorGrid.setStateful(true);
			editorGrid.setClicksToEdit(ClicksToEdit.TWO);

	    	GridSelectionModel<SiteDTO> sm = new GridSelectionModel<SiteDTO>();
	        sm.setSelectionMode(SelectionMode.SINGLE);
	        editorGrid.setSelectionModel(sm);

	        add(editorGrid, new BorderLayoutData(Style.LayoutRegion.CENTER));
	        layout();

    	} else {
    		editorGrid.reconfigure(listStore, columnModel);
    	}
    	
    	loader.load();
	}
	
	private class SiteProxy extends RpcProxy<PagingLoadResult<SiteDTO>> {

		@Override
		protected void load(Object loadConfig,
				final AsyncCallback<PagingLoadResult<SiteDTO>> callback) {

			PagingLoadConfig config = (PagingLoadConfig)loadConfig;
			GetSites command = new GetSites();
			command.setOffset(config.getOffset());
			command.setLimit(config.getLimit());
			command.setFilter(currentPlace.getFilter());
			command.setSortInfo(config.getSortInfo());
			dispatcher.execute(command, null, new AsyncCallback<SiteResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(SiteResult result) {
					callback.onSuccess(result);
				}
			});
		}
	}


}
