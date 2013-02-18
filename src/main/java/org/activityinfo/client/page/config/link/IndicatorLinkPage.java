package org.activityinfo.client.page.config.link;

import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.command.GetIndicatorLinks;
import org.activityinfo.shared.command.LinkIndicators;
import org.activityinfo.shared.command.result.IndicatorLinkResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class IndicatorLinkPage extends ContentPanel implements Page {

	public static final PageId PAGE_ID = new PageId("links");

	private final Dispatcher dispatcher;
	private LinkGraph linkGraph;

	private DatabaseGridPanel sourceDatabaseGrid;
	private DatabaseGridPanel destDatabaseGrid;

	private IndicatorGridPanel sourceIndicatorGrid;
	private IndicatorGridPanel destIndicatorGrid;

	private ToggleButton linkButton;
	
	@Inject
	public IndicatorLinkPage(Dispatcher dispatcher,
			StateProvider stateMgr) {

		this.dispatcher = dispatcher;
		
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
		layout.setPadding(new Padding(10, 5, 5, 5));
		setLayout(layout);
		setHeading(I18N.CONSTANTS.linkIndicators());

		addSourceContainer();
		addGalley();
		addDestinationContainer();
			
		sourceDatabaseGrid.addMouseOverListener(new Listener<GridEvent<UserDatabaseDTO>>() {

			@Override
			public void handleEvent(GridEvent<UserDatabaseDTO> event) {
				sourceDatabaseGrid.getGridView().clearHighlight();
				destDatabaseGrid.getGridView().highlight( linkGraph.destinationForDatabase( event.getModel() ));
			}
		});
		
		sourceDatabaseGrid.addSelectionChangeListener(new SelectionChangedListener<UserDatabaseDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<UserDatabaseDTO> se) {
				sourceIndicatorGrid.setDatabase(se.getSelectedItem());
				onDatabaseLinksChanged();
			}
		});
		
		sourceIndicatorGrid.addMouseOverListener(new Listener<GridEvent<IndicatorDTO>>() {
			
			@Override
			public void handleEvent(GridEvent<IndicatorDTO> event) {
				
				Set<Integer> linkedTo = linkGraph.destForIndicator(
						event.getModel(), 
						destDatabaseGrid.getSelectedItem());

				sourceDatabaseGrid.getGridView().clearHighlight();
				destDatabaseGrid.getGridView().clearHighlight();
				destIndicatorGrid.getGridView().highlight(linkedTo);
			}
		});
		
		sourceIndicatorGrid.addSelectionChangeListener(new SelectionChangedListener<IndicatorDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<IndicatorDTO> se) {
				onIndicatorSelectionChanged();
			}
		});
		
		destDatabaseGrid.addMouseOverListener(new Listener<GridEvent<UserDatabaseDTO>>() {

			@Override
			public void handleEvent(GridEvent<UserDatabaseDTO> event) {
				destDatabaseGrid.getGridView().clearHighlight();
				sourceDatabaseGrid.getGridView().highlight( linkGraph.sourcesForDatabase(event.getModel()));
			}
		});
		
		destDatabaseGrid.addSelectionChangeListener(new SelectionChangedListener<UserDatabaseDTO>() {

			@Override
			public void selectionChanged(
					SelectionChangedEvent<UserDatabaseDTO> se) {
				destIndicatorGrid.setDatabase(se.getSelectedItem());
				onDatabaseLinksChanged();
			}
		});
		
		destIndicatorGrid.addMouseOverListener(new Listener<GridEvent<IndicatorDTO>>() {

			@Override
			public void handleEvent(GridEvent<IndicatorDTO> be) {
				sourceDatabaseGrid.getGridView().clearHighlight();
				destDatabaseGrid.getGridView().clearHighlight();
				sourceIndicatorGrid.getGridView().highlight(
						linkGraph.sourceForIndicator(
								sourceDatabaseGrid.getSelectedItem(),
								be.getModel()));
			}
		});
		
		destIndicatorGrid.addSelectionChangeListener(new SelectionChangedListener<IndicatorDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<IndicatorDTO> se) {
				onIndicatorSelectionChanged();
			}
		});
	}
	

	private void addSourceContainer() {
		LayoutContainer container = createContainer();	
		
		Html html = new Html("");
		
		sourceDatabaseGrid = new DatabaseGridPanel(dispatcher);
		sourceDatabaseGrid.setHeading("Source Database");
		container.add(sourceDatabaseGrid, vflex(3));
		
		sourceIndicatorGrid = new IndicatorGridPanel();
		container.add(sourceIndicatorGrid, vflex(7));
	}
	
	private void addGalley() {
	
		
		linkButton = new ToggleButton("", IconImageBundle.ICONS.link());
		linkButton.disable();
		linkButton.setWidth(28);
		linkButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				onToggleLink();
			}
		});
		
		LayoutContainer container = new LayoutContainer();
		container.setWidth(35);
		container.add(linkButton);
		container.setLayout(new BoxLayout() {

			@Override
			protected void onLayout(Container<?> container, El target) {
			    super.onLayout(container, target);

				Size size = target.getStyleSize();
				innerCt.setSize(size.width, size.height, true);
		    	Component c = container.getItem(0);
		    	int ch = c.getOffsetHeight();
		    	setPosition(c, 5, ((int)(size.height * 0.65)) - (ch / 2));
			}
			
		});
		
		add(container);	
	}
	

	private void addDestinationContainer() {

		LayoutContainer container = createContainer();
					
		destDatabaseGrid = new DatabaseGridPanel(dispatcher);
		destDatabaseGrid.setHeading("Destination Database");
		container.add(destDatabaseGrid, vflex(3));
		
		destIndicatorGrid = new IndicatorGridPanel();
		container.add(destIndicatorGrid, vflex(7));	
	}

	
	private LayoutContainer createContainer() {
			
		HBoxLayoutData containerData = new HBoxLayoutData();
		containerData.setFlex(1.0);
	
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
	
		LayoutContainer container = new LayoutContainer(layout);
		add(container, containerData);
		
		return container;
	}
	
	private VBoxLayoutData vflex(double flex) {
		VBoxLayoutData data = new VBoxLayoutData();
		data.setFlex(flex);
		return data;
	}


	private void onDatabaseGraphChanged() {
		sourceDatabaseGrid.setLinked(linkGraph.sourceDatabases());
		destDatabaseGrid.setLinked(linkGraph.destDatabases());
	}
	

	private void onDatabaseLinksChanged() {
		sourceIndicatorGrid.setLinked( 
				linkGraph.sourceIndicators(sourceDatabaseGrid.getSelectedItem(), destDatabaseGrid.getSelectedItem() ));
		destIndicatorGrid.setLinked(
				linkGraph.destinationIndicators(sourceDatabaseGrid.getSelectedItem(), destDatabaseGrid.getSelectedItem() ));
	}
	


	protected void onIndicatorSelectionChanged() {
		IndicatorDTO source = sourceIndicatorGrid.getSelectedItem();
		IndicatorDTO dest = destIndicatorGrid.getSelectedItem();
		
		if(source == null || dest == null) {
			linkButton.disable();
		} else {
			double y1 = sourceIndicatorGrid.getRowY(source);
			double y2 = destIndicatorGrid.getRowY(dest);
			int y = (int)((y1+y2)/2.0);
			int x = sourceIndicatorGrid.el().getRight(false);
			
			linkButton.toggle(linkGraph.linked(source, dest));
			linkButton.enable();
//			linkButton.showAt(x - LinkTip.WIDTH/2, y - LinkTip.HEIGHT/2);

		}
	}


	protected void onToggleLink() {
		IndicatorDTO source = sourceIndicatorGrid.getSelectedItem();
		IndicatorDTO dest = destIndicatorGrid.getSelectedItem();	
		
		final boolean link = !linkGraph.linked(source, dest);
		linkButton.toggle(link);
		
		LinkIndicators update = new LinkIndicators();
		update.setLink(link);
		update.setSourceIndicatorId(source.getId());
		update.setDestIndicatorId(dest.getId());
		
		if(link) {
			linkGraph.link(sourceDatabaseGrid.getSelectedItem(), source, 
						   destDatabaseGrid.getSelectedItem(), dest);
		} else {
			linkGraph.unlink(source, dest);
		}
		destDatabaseGrid.setLinked( linkGraph.destDatabases() );
		sourceDatabaseGrid.setLinked( linkGraph.sourceDatabases() );
		onDatabaseLinksChanged();
		
		dispatcher.execute(update, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(VoidResult result) {
				Info.display(I18N.CONSTANTS.saved(), link ? "Link created" : "Link removed");
			}
		});
		
	}


	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public boolean navigate(PageState place) {
		dispatcher.execute(new GetIndicatorLinks(), new AsyncCallback<IndicatorLinkResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(IndicatorLinkResult result) {
				linkGraph = new LinkGraph(result.getLinks());
				onDatabaseGraphChanged();
			}
		});
		return true;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
		
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

}
