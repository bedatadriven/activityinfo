package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

final class SiteTreeGrid extends EditorTreeGrid<ModelData> {
	
	public SiteTreeGrid(Dispatcher dispatcher, AdminGroupingModel groupingModel, Filter filter, ColumnModel columnModel) {
		super(createStore(dispatcher, groupingModel), columnModel);
		setLoadMask(true); 
		setStateful(true);
		setClicksToEdit(ClicksToEdit.TWO);
		
		setIconProvider(new ModelIconProvider<ModelData>() {
			@Override
			public AbstractImagePrototype getIcon(ModelData model) {
				if (model instanceof AdminEntityDTO) {
					return IconImageBundle.ICONS.folder();
				} else if (model instanceof SiteDTO) {
					SiteDTO site = (SiteDTO)model;
					if(site.hasCoords()) {
						return IconImageBundle.ICONS.mapped();
					} else {
						return IconImageBundle.ICONS.unmapped();
					}
				}

				return null;
			}
		});
		
		GridSelectionModel<ModelData> sm = new GridSelectionModel<ModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		sm.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				if(se.getSelectedItem() instanceof SiteDTO) {
					fireEvent(Events.SelectionChange, se);
				}
			}
		});
		setSelectionModel(sm);
		
		getLoader().setFilter(filter);
	}

	private static TreeStore<ModelData> createStore(Dispatcher dispatcher, AdminGroupingModel groupingModel) {
		SiteAdminTreeLoader loader = new SiteAdminTreeLoader(dispatcher);
		loader.setAdminLeafLevelId(groupingModel.getAdminLevelId());
				
		return new TreeStore<ModelData>(loader);
	}

	private SiteAdminTreeLoader getLoader() {
		return (SiteAdminTreeLoader) getTreeStore().getLoader();
	}

	public void addSelectionChangeListener(
			SelectionChangedListener<SiteDTO> selectionChangedListener) {
		addListener(Events.SelectionChange, selectionChangedListener);
	}
	
}
