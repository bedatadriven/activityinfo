package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.column.ColumnModelBuilder;

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

	public SiteTreeGrid(Dispatcher dispatcher, int activityId) {
		super(createStore(), createColumnModel());
		setLoadMask(true); 
		setStateful(true);
		//grid.setStateId("site-treeview" + activity.getId());
		setAutoExpandColumn("name");
		setClicksToEdit(ClicksToEdit.TWO);
		
		setIconProvider(new ModelIconProvider<ModelData>() {
			@Override
			public AbstractImagePrototype getIcon(ModelData model) {
				if (model instanceof YearViewModel || model instanceof MonthViewModel || model instanceof AdminViewModel) {
					return IconImageBundle.ICONS.folder();
				}

				if (model instanceof ShowSitesViewModel) {
					return IconImageBundle.ICONS.drilldown();
				}

				return null;
			}
		});
		
		GridSelectionModel<ModelData> sm = new GridSelectionModel<ModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		setSelectionModel(sm);
		
		 

	}

	public static TreeStore<ModelData> createStore() {
		return new TreeStore<ModelData>();
	}

	public static ColumnModel createColumnModel() {
		return new ColumnModelBuilder() 
		.addTreeNameColumn()
		.addLocationColumn()
		.build();

	}
}
