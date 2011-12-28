package org.sigmah.client.page.report;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.grid.AbstractEditorGridView;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class ReportDesignListPage extends
		AbstractEditorGridView<ModelData, ReportDesignListPresenter>
		implements ReportDesignListPresenter.View {

	private final Dispatcher service;

	public ReportDesignListPage(Dispatcher service) {
		super();
		this.service = service;

		setLayout(new FitLayout());
		setHeaderVisible(false);
	}

	@Override
	public void init(ReportDesignListPresenter presenter,
			ListStore<ModelData> store) {
		super.init(presenter, store);

	}

	@Override
	protected Grid<ModelData> createGridAndAddToContainer(Store store) {
		EditorGrid<ModelData> grid = new EditorGrid<ModelData>(
				(ListStore) store, createColumnModel());

		grid.addListener(Events.CellDoubleClick,
				new Listener<GridEvent<ModelData>>() {
					@Override
					public void handleEvent(GridEvent<ModelData> event) {
						if (event.getColIndex() == 1) {
							// TODO Auto-generated method stub
							// presenter.onTemplateSelected(event.getModel());
						}
					}
				});

		add(grid);

		return grid;
	}

	private ColumnModel createColumnModel() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
        columns.add(new ColumnConfig("title", I18N.CONSTANTS.title(), 350));

		return new ColumnModel(columns);
	}

	@Override
	protected void initToolBar() {
		// TODO Auto-generated method stub

	}

}
