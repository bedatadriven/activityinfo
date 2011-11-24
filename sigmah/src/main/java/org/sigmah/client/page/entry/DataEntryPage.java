package org.sigmah.client.page.entry;

import java.util.Set;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.common.widget.CollapsibleTabPanel;
import org.sigmah.client.page.entry.column.DefaultColumnModelProvider;
import org.sigmah.client.page.entry.form.PrintDataEntryForm;
import org.sigmah.client.page.entry.form.SiteDialogCallback;
import org.sigmah.client.page.entry.form.SiteDialogLauncher;
import org.sigmah.client.page.entry.grouping.GroupingComboBox;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.model.DimensionType;

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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

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
		filterPane.getSet().applyBaseFilter(new Filter());
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
		
		attachmentsTab = new AttachmentsTab(dispatcher);
						
		tabPanel = new CollapsibleTabPanel();
		tabPanel.add(detailTab);
		tabPanel.add(monthlyTab);
		tabPanel.add(attachmentsTab);
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
    	
        toolBar.addButton(UIActions.add, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addButton(UIActions.edit, I18N.CONSTANTS.edit(), IconImageBundle.ICONS.edit());
        toolBar.addPrintButton();
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
        toolBar.addPrintButton();
                
        return toolBar;
	}
	

	private void onSiteSelected(final SelectionChangedEvent<SiteDTO> se) {
		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

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
	
	private void updateSelection(ActivityDTO activity, SiteDTO site) {
		
		boolean permissionToEdit = activity.getDatabase().isAllowedToEdit(site);
		toolBar.setActionEnabled(UIActions.edit, permissionToEdit);
		toolBar.setActionEnabled(UIActions.delete, permissionToEdit);
		
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
		gridPanel.load(currentPlace.getGrouping(), currentPlace.getFilter());
		groupingComboBox.setFilter(currentPlace.getFilter());
		filterPane.getSet().applyBaseFilter(new Filter());
		
		// currently the print form only does one activity
		Set<Integer> activities = currentPlace.getFilter().getRestrictions(DimensionType.Activity);			
		toolBar.setActionEnabled(UIActions.print, activities.size() == 1);

		
		return true;
	}

	@Override
	public void onUIAction(String actionId) {
		if(UIActions.add.equals(actionId)) {
			SiteDialogLauncher formHelper = new SiteDialogLauncher(dispatcher);
			formHelper.addSite(currentPlace.getFilter(), new SiteDialogCallback() {
				
				@Override
				public void onSaved(SiteDTO site) {
					gridPanel.refresh();
				}
			});
		} else if(UIActions.edit.equals(actionId)) {
			SiteDialogLauncher launcher = new SiteDialogLauncher(dispatcher);
			launcher.editSite(gridPanel.getSelection(), new SiteDialogCallback() {
				
				@Override
				public void onSaved(SiteDTO site) {
					gridPanel.refresh();
				}
			});
		} else if(UIActions.print.equals(actionId)) {
			dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(SchemaDTO result) {
					ActivityDTO activity = result.getActivityById(
							currentPlace.getFilter().getRestrictedCategory(DimensionType.Activity));
					
					new PrintDataEntryForm(activity, dispatcher);
				}
			});
		}
	}
}
