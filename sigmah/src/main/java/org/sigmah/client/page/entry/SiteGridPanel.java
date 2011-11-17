package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.entry.column.ColumnModelProvider;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.client.page.entry.grouping.GroupingModel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.Component;
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
public final class SiteGridPanel extends ContentPanel implements ActionListener {

	private final Dispatcher dispatcher;
	private final ColumnModelProvider columnModelProvider;
			
	public SiteGridPanel(Dispatcher dispatcher, ColumnModelProvider columnModelProvider) {
		this.dispatcher = dispatcher;
		this.columnModelProvider = columnModelProvider;
		
		setHeading(I18N.CONSTANTS.sitesHeader());
		setIcon(IconImageBundle.ICONS.table());
		setLayout(new FitLayout());
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
		
	protected void createGrid(GroupingModel grouping, Filter filter,
			ColumnModel columnModel) {

		if(grouping == NullGroupingModel.INSTANCE) {
			FlatSiteGridPanel panel = new FlatSiteGridPanel(dispatcher);
			panel.addSelectionChangedListener(new SelectionChangedListener<SiteDTO>() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
					fireEvent(Events.SelectionChange, se);
				}
			});
			panel.initGrid(filter, columnModel);
			installGrid(panel);
			
		} else if(grouping instanceof AdminGroupingModel) {
			SiteTreeGrid grid = new SiteTreeGrid(dispatcher, (AdminGroupingModel) grouping, filter, columnModel);	
			grid.addSelectionChangeListener(new SelectionChangedListener<SiteDTO>() {

				@Override
				public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
					fireEvent(Events.SelectionChange, se);
				}
				
			});
			installGrid(grid);
		} else {
			throw new IllegalArgumentException(grouping.toString());
		}
	}


	public void addSelectionChangedListener(SelectionChangedListener<SiteDTO> listener) {
		addListener(Events.SelectionChange, listener);
	}

	private void installGrid(Component grid) {
		removeAll();
		add(grid);
		layout();
	}

	@Override
	public void onUIAction(String actionId) {
		// TODO Auto-generated method stub
		
	}

}
