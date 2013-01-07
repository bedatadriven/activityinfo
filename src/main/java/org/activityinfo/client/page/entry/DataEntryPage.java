package org.activityinfo.client.page.entry;

import java.util.Set;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.entry.column.DefaultColumnModelProvider;
import org.activityinfo.client.page.entry.form.PrintDataEntryForm;
import org.activityinfo.client.page.entry.form.SiteDialogCallback;
import org.activityinfo.client.page.entry.form.SiteDialogLauncher;
import org.activityinfo.client.page.entry.grouping.GroupingComboBox;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.client.widget.CollapsibleTabPanel;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This is the container for the DataEntry page.
 *
 */
public class DataEntryPage extends LayoutContainer implements Page, ActionListener {

	public static final PageId PAGE_ID = new PageId("data-entry");

	private final Dispatcher dispatcher;
	private final EventBus eventBus;
	
	private GroupingComboBox groupingComboBox;
		
	private FilterPane filterPane;

	private SiteGridPanel gridPanel;
	private CollapsibleTabPanel tabPanel;
	
	private DetailTab detailTab;
		
	private MonthlyReportsPanel monthlyPanel;
	private TabItem monthlyTab;
	
	private DataEntryPlace currentPlace = new DataEntryPlace();

	private AttachmentsTab attachmentsTab;

	private SiteHistoryTab siteHistoryTab;

	private ActionToolBar toolBar;

	
	@Inject
	public DataEntryPage(final EventBus eventBus, Dispatcher dispatcher) {
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		
		setLayout(new BorderLayout());
		
		addFilterPane();
		addCenter();
	}

	private void addFilterPane() {
		filterPane = new FilterPane(dispatcher);
		BorderLayoutData filterLayout = new BorderLayoutData(LayoutRegion.WEST);
		filterLayout.setCollapsible(true);
		filterLayout.setMargins(new Margins(0,5,0,0));
		filterLayout.setSplit(true);
		add(filterPane, filterLayout);
		
		filterPane.getSet().addValueChangeHandler(new ValueChangeHandler<Filter>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Filter> event) {
				eventBus.fireEvent( new NavigationEvent(NavigationHandler.NavigationRequested, 
						currentPlace.copy()
							.setFilter(event.getValue())) );
			}
		});
	//	filterPane.getSet().applyBaseFilter(new Filter());
	}

	private void addCenter() {
		gridPanel = new SiteGridPanel(dispatcher, new DefaultColumnModelProvider(dispatcher));
		gridPanel.setTopComponent(createToolBar());
		
		LayoutContainer center = new LayoutContainer();
		center.setLayout(new BorderLayout());
				
		center.add(gridPanel, new BorderLayoutData(LayoutRegion.CENTER));
		
		gridPanel.addSelectionChangedListener(new SelectionChangedListener<SiteDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<SiteDTO> se) {
				onSiteSelected(se);
			}
		});
		
		detailTab = new DetailTab(dispatcher);
		
		monthlyPanel = new MonthlyReportsPanel(dispatcher);
		monthlyTab = new TabItem(I18N.CONSTANTS.monthlyReports());
		monthlyTab.setLayout(new FitLayout());
		monthlyTab.add(monthlyPanel);
		
		attachmentsTab = new AttachmentsTab(dispatcher, eventBus);
						
		siteHistoryTab = new SiteHistoryTab(dispatcher);
		
		tabPanel = new CollapsibleTabPanel();
		tabPanel.add(detailTab);
		tabPanel.add(monthlyTab);
		tabPanel.add(attachmentsTab);
		tabPanel.add(siteHistoryTab);
		tabPanel.setSelection(detailTab);
		center.add(tabPanel, tabPanel.getBorderLayoutData());
		
		add(center, new BorderLayoutData(LayoutRegion.CENTER));
	}
		
	private ActionToolBar createToolBar() {
		toolBar = new ActionToolBar(this);
		
		groupingComboBox = new GroupingComboBox(dispatcher);
    	groupingComboBox.withSelectionListener(new Listener<FieldEvent>() {
			
			@Override
			public void handleEvent(FieldEvent be) {
				eventBus.fireEvent( new NavigationEvent(NavigationHandler.NavigationRequested, 
						currentPlace.copy()
							.setGrouping(groupingComboBox.getGroupingModel())) );
			}
		});
    	
    	toolBar.add(new Label(I18N.CONSTANTS.grouping()));
    	toolBar.add(groupingComboBox);
    	
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addButton(UIActions.EDIT, I18N.CONSTANTS.edit(), IconImageBundle.ICONS.edit());
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
        toolBar.addPrintButton();
        toolBar.addButton("EMBED", I18N.CONSTANTS.embed(), IconImageBundle.ICONS.embed());
        
        return toolBar;
	}
	

	private void onSiteSelected(final SelectionChangedEvent<SiteDTO> se) {
		if(se.getSelection().isEmpty()) {
			onNoSelection();
		} else {
			dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
	
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onSuccess(SchemaDTO schema) {
					SiteDTO site = se.getSelectedItem();
					ActivityDTO activity = schema.getActivityById(site.getActivityId());
					updateSelection(activity, site);
				}
			});
		}
	}
	
	private void updateSelection(ActivityDTO activity, SiteDTO site) {
		
		boolean permissionToEdit = activity.getDatabase().isAllowedToEdit(site);
		toolBar.setActionEnabled(UIActions.EDIT, permissionToEdit && !site.isLinked());
		toolBar.setActionEnabled(UIActions.DELETE, permissionToEdit && !site.isLinked());
		
		detailTab.setSite(site);
		attachmentsTab.setSite(site);
		if(activity.getReportingFrequency() == ActivityDTO.REPORT_MONTHLY) {
			monthlyPanel.load(site);
			monthlyTab.setEnabled(true);
		} else {
			monthlyTab.setEnabled(false);
			if(tabPanel.getSelectedItem() == monthlyTab) {
				tabPanel.setSelection(detailTab);
			}
		}
		siteHistoryTab.setSite(site);
	}
	
	private void onNoSelection() {
		toolBar.setActionEnabled(UIActions.EDIT, false);
		toolBar.setActionEnabled(UIActions.DELETE, false); 
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
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
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		currentPlace = (DataEntryPlace) place;
		if(!currentPlace.getFilter().isRestricted(DimensionType.Activity) &&
		   !currentPlace.getFilter().isRestricted(DimensionType.Database)) {
			
			redirectToFirstActivity();
		} else {
			doNavigate();
		}
		return true;
	}

	private void redirectToFirstActivity() {
		dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				for(UserDatabaseDTO db : result.getDatabases()) {
					if(!db.getActivities().isEmpty()) {
						currentPlace.getFilter().addRestriction(DimensionType.Activity, db.getActivities().get(0).getId());
						doNavigate();
						return;
					}
				}
			}
		});
	}

	private void doNavigate() {
		gridPanel.load(currentPlace.getGrouping(), currentPlace.getFilter());
		groupingComboBox.setFilter(currentPlace.getFilter());
		filterPane.getSet().applyBaseFilter(new Filter());
		
		// currently the print form only does one activity
		Set<Integer> activities = currentPlace.getFilter().getRestrictions(DimensionType.Activity);			
		toolBar.setActionEnabled(UIActions.PRINT, activities.size() == 1);
		toolBar.setActionEnabled(UIActions.EXPORT, activities.size() == 1);
		
		// also embedding is only implemented for one activity
		toolBar.setActionEnabled("EMBED", activities.size() == 1);
	
		// adding is also only enabled for one activity, but we have to
		// lookup to see whether it possible for this activity
		toolBar.setActionEnabled(UIActions.ADD, false);
		if(activities.size() == 1) {
			checkWhetherEditingIsAllowed(activities.iterator().next());
		}
		
	}

	private void checkWhetherEditingIsAllowed(final int activityId) {
		dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
			
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				toolBar.setActionEnabled(UIActions.ADD, 
						result.getActivityById(activityId).getDatabase().isEditAllowed());
			}
		});
	}

	@Override
	public void onUIAction(String actionId) {
		if(UIActions.ADD.equals(actionId)) {
			SiteDialogLauncher formHelper = new SiteDialogLauncher(dispatcher);
			formHelper.addSite(currentPlace.getFilter(), new SiteDialogCallback() {
				
				@Override
				public void onSaved(SiteDTO site) {
					gridPanel.refresh();
				}
			});
		} else if(UIActions.EDIT.equals(actionId)) {
			SiteDialogLauncher launcher = new SiteDialogLauncher(dispatcher);
			launcher.editSite(gridPanel.getSelection(), new SiteDialogCallback() {
				
				@Override
				public void onSaved(SiteDTO site) {
					gridPanel.refresh();
				}
			});
			
		} else if(UIActions.DELETE.equals(actionId)) {
			
			delete();
		
		} else if(UIActions.PRINT.equals(actionId)) {
			dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(SchemaDTO result) {
					ActivityDTO activity = result.getActivityById(
							currentPlace.getFilter().getRestrictedCategory(DimensionType.Activity));
					
					new PrintDataEntryForm(activity).print();
				}
			});
			
		} else if(UIActions.EXPORT.equals(actionId)) {
			Window.Location.assign(GWT.getModuleBaseURL() + "export?filter=" +
					FilterUrlSerializer.toUrlFragment(currentPlace.getFilter()));
		
		} else if("EMBED".equals(actionId)) {
			EmbedDialog dialog = new EmbedDialog(dispatcher);
			dialog.show(currentPlace);
		}
	}

	private void delete() {
		dispatcher.execute(new DeleteSite(gridPanel.getSelection().getId()), 
				new MaskingAsyncMonitor(this, I18N.CONSTANTS.deleting()),
				new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// handled by monitor
			}

			@Override
			public void onSuccess(VoidResult result) {
				gridPanel.refresh();
			}
		});
	}
}
