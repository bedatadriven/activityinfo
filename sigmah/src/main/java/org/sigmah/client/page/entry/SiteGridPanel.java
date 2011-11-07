package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.common.widget.CollapsibleTabPanel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.client.page.entry.location.LocationDialog;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The SiteGridPanel contains the main toolbar for the Site Grid display, and
 * switches between the {@link FlatSiteGridPanel} and the tree grids
 * 
 */
public class SiteGridPanel extends ContentPanel implements ActionListener {

	private final Dispatcher dispatcher;
	private static final String VIEW_GROUP = "view";
	
	private ActionToolBar toolBar;
	private CollapsibleTabPanel tabPanel;
	
	private Component center = null;
	
	private DataEntryPlace currentPlace;
	
	public SiteGridPanel(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		setHeading(I18N.CONSTANTS.sitesHeader());
		setIcon(IconImageBundle.ICONS.mapped());
		setLayout(new BorderLayout());
		
		createToolBar();
		createTabPanel();
	}

	private void createToolBar() {
    	toolBar = new ActionToolBar(this);
            	
        toolBar.addButton(UIActions.add, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addEditButton();
        toolBar.addPrintButton();
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
        toolBar.addLockedPeriodsButton();

        toolBar.add(new SeparatorToolItem());
//
//        toolBar.addToggleButton().inGroup(VIEW_GROUP)
//    		.withText(I18N.CONSTANTS.list())
//    		.withIcon(IconImageBundle.ICONS.list())
//    		.withListener(new SelectionListener<ButtonEvent>() {
//				
//				@Override
//				public void componentSelected(ButtonEvent ce) {
//					showList();
//				}
//			});
//        
//        toolBar.addToggleButton().inGroup(VIEW_GROUP)
//        	.withText(I18N.CONSTANTS.treeTime())
//        	.withIcon(IconImageBundle.ICONS.treeviewAdmin())
//        	.withListener(new SelectionListener<ButtonEvent>() {
//
//				@Override
//				public void componentSelected(ButtonEvent ce) {
//					showLocationTree();
//				}
//        	});
//        		
       
        
//        togglebuttonList = toolBar.addToggleButton(UIActions.list, I18N.CONSTANTS.list(), IconImageBundle.ICONS.list()); 
//        togglebuttonTreeGeo = toolBar.addToggleButton(UIActions.treeGeo, "Tree geo", IconImageBundle.ICONS.treeviewAdmin());
//        togglebuttonTreeTime = toolBar.addToggleButton(UIActions.treeTime, I18N.CONSTANTS.treeTime(), IconImageBundle.ICONS.treeviewTime());
        
//        if (sideBarButtons.size() > 0) {
//            toolBar.add(new SeparatorToolItem());
//        }
//        for(ToggleButton button : sideBarButtons) {
//            toolBar.add(button);
//        }
        
        setTopComponent(toolBar);
    }
    
	public void navigate(DataEntryPlace place) {
		this.currentPlace = place;


		
		if(place.getGrouping() == NullGroupingModel.INSTANCE) {
			FlatSiteGridPanel panel = new FlatSiteGridPanel(dispatcher);
			panel.navigate(place);
			installCenterComponent(panel);
			
		} else {
						
			
		}
       	layout();    	
    }
	
	private void installCenterComponent(Component component) {
		if(center != null) {
			remove(center);
   		}	
		center = component;
		add(center, new BorderLayoutData(LayoutRegion.CENTER));
		layout();
	}
		
    private void createTabPanel() {

		tabPanel = new CollapsibleTabPanel();
		tabPanel.add(new DetailsTab());
		add(tabPanel, tabPanel.getBorderLayoutData());
		
    }

	@Override
	public void onUIAction(String actionId) {
		if (UIActions.add.equals(actionId)) {
			addSite();
		}
	}

	private void addSite() {
		if(currentPlace.getFilter().isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
			dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(SchemaDTO schema) {
					ActivityDTO activity = schema.getActivityById(
							currentPlace.getFilter().getRestrictedCategory(DimensionType.Activity));
					
					LocationDialog dialog = new LocationDialog(dispatcher, activity.getDatabase().getCountry(),
							activity.getLocationType());
					
					dialog.show();
					
				}
			});
		}
	}
	
}
