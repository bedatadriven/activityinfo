package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.common.widget.CollapsibleTabPanel;
import org.sigmah.client.page.entry.place.DataEntryPlace;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;

public class SiteGridPanel extends ContentPanel {

	private final Dispatcher dispatcher;
	
	private ActionToolBar toolBar;
	private CollapsibleTabPanel tabPanel;
	
	private Component center = null;
	
	private static final String VIEW_GROUP = "view";
	
	public SiteGridPanel(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		setHeaderVisible(false);
		setLayout(new BorderLayout());
		
		createToolBar();
		createTabPanel();
	}
	
    private void createToolBar() {
    	toolBar = new ActionToolBar();
        toolBar.addSaveSplitButton();
        toolBar.add(new SeparatorToolItem());
        
        toolBar.addButton(UIActions.add, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addEditButton();
        toolBar.addPrintButton();
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
        toolBar.addLockedPeriodsButton();

        toolBar.add(new SeparatorToolItem());

        toolBar.addToggleButton().inGroup(VIEW_GROUP)
    		.withText(I18N.CONSTANTS.list())
    		.withIcon(IconImageBundle.ICONS.list())
    		.withListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					showList();
				}
			});
        
        toolBar.addToggleButton().inGroup(VIEW_GROUP)
        	.withText(I18N.CONSTANTS.treeTime())
        	.withIcon(IconImageBundle.ICONS.treeviewAdmin())
        	.withListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					showLocationTree();
				}
        	});
        		
       
        
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
		if(center != null) {
			remove(center);
   		}
		SiteListPanel panel = new SiteListPanel(dispatcher);
		panel.navigate(place);
		add(panel, new BorderLayoutData(LayoutRegion.CENTER));
		
		center = panel;
       	layout();    	
    }
	

	private void showList() {
		// TODO Auto-generated method stub
		
	}

    private void showLocationTree() {
		// TODO Auto-generated method stub
		
	}
	
    private void createTabPanel() {

		tabPanel = new CollapsibleTabPanel();
		tabPanel.add(new DetailsTab());
		add(tabPanel, tabPanel.getBorderLayoutData());
		
    }
	
}
