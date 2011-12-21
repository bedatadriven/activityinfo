package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.entry.column.ColumnModelProvider;
import org.sigmah.client.page.entry.column.DefaultColumnModelProvider;
import org.sigmah.client.page.entry.grouping.GroupingModel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The SiteGridPanel contains the main toolbar for the Site Grid display, and
 * switches between the {@link FlatSiteGridPanel} and the tree grids.
 * 
 * <p>Note that this class is FINAL. It should not be subclassed. The grid
 * can be customized by providing a different {@code columnModelProvider}, and 
 * containers can install their own toolbar by calling {@code setTopComponent() }
 * 
 */
public final class SiteGridPanel extends ContentPanel  {

	private final Dispatcher dispatcher;
	private final ColumnModelProvider columnModelProvider;
	
	private SiteGridPanelView grid = null;
			
	public SiteGridPanel(Dispatcher dispatcher, ColumnModelProvider columnModelProvider) {
		this.dispatcher = dispatcher;
		this.columnModelProvider = columnModelProvider;
		
		setHeading(I18N.CONSTANTS.sitesHeader());
		setIcon(IconImageBundle.ICONS.table());
		setLayout(new FitLayout());
	}
	
	public SiteGridPanel(Dispatcher dispatcher) {
		this(dispatcher, new DefaultColumnModelProvider(dispatcher));
	}

    
	public void load(final GroupingModel grouping, final Filter filter) {
		
		removeAll();
		add(new LoadingPlaceHolder());
		layout();
		columnModelProvider.get(filter, grouping, new AsyncCallback<ColumnModel>() {
			@Override
			
			public void onSuccess(ColumnModel columnModel) {
				createGrid(grouping, filter, columnModel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void refresh() {
		if(grid != null) {
			grid.refresh();
		}
	}
		
	protected void createGrid(GroupingModel grouping, Filter filter,
			ColumnModel columnModel) {

		if(grouping == NullGroupingModel.INSTANCE) {
			FlatSiteGridPanel panel = new FlatSiteGridPanel(dispatcher);

			panel.initGrid(filter, columnModel);
			installGrid(panel);
			
		} else {
			SiteTreeGrid treeGrid = new SiteTreeGrid(dispatcher, grouping, filter, columnModel);	
			treeGrid.addSelectionChangeListener(new SelectionChangedListener<SiteDTO>() {

				@Override
				public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
					fireEvent(Events.SelectionChange, se);
				}
				
			});
			installGrid(treeGrid);
		} 
	}


	public void addSelectionChangedListener(SelectionChangedListener<SiteDTO> listener) {
		addListener(Events.SelectionChange, listener);
	}

	private void installGrid(SiteGridPanelView grid) {
		this.grid = grid;
		
		removeAll();
		add(grid.asComponent());
		layout();
		
		grid.addSelectionChangeListener(new SelectionChangedListener<SiteDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
				fireEvent(Events.SelectionChange, se);
			}
		});
	}

	public SiteDTO getSelection() {
		if(grid == null) {
			return null;
		} else {
			return grid.getSelection();
		}
	}
}
