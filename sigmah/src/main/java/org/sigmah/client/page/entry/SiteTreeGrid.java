package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.column.ColumnModelBuilder;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class SiteTreeGrid extends EditorTreeGrid<ModelData> {

	public SiteTreeGrid(Dispatcher dispatcher, AdminGroupingModel groupingModel) {
		super(createStore(dispatcher, groupingModel), createColumnModel());
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
		setSelectionModel(sm);
	}

	private static TreeStore<ModelData> createStore(Dispatcher dispatcher, AdminGroupingModel groupingModel) {
		SiteAdminTreeLoader loader = new SiteAdminTreeLoader(dispatcher);
		loader.setAdminLeafLevelId(groupingModel.getAdminLevelId());
				
		return new TreeStore<ModelData>(loader);
	}

	private static ColumnModel createColumnModel() {
		return new ColumnModelBuilder() 
		.addTreeNameColumn()
		.build();
	}

	private SiteAdminTreeLoader getLoader() {
		return (SiteAdminTreeLoader) getTreeStore().getLoader();
	}
	
	public void show(Filter filter) {
		getLoader().setFilter(filter);
		getLoader().load();
	}
}
