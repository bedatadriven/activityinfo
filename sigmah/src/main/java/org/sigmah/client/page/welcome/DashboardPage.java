/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DashboardPage extends Portal implements Page {


    public static final PageId PAGE_ID = new PageId("dashboard");

    @Inject
    public DashboardPage() {
    	super(3);
        Portal portal = this;  
        portal.setBorders(true);  
        portal.setStyleAttribute("backgroundColor", "white");  
        portal.setColumnWidth(0, .33);  
        portal.setColumnWidth(1, .33);  
        portal.setColumnWidth(2, .33);  
      
        Portlet portlet = new Portlet();  
        portlet.setHeading("Grid in a Portlet");  
        configPanel(portlet);  
        portlet.setLayout(new FitLayout());  
        portlet.add(createGrid());  
        portlet.setHeight(250);  
      
        portal.add(portlet, 0);  
      
        portlet = new Portlet();  
        portlet.setHeading("Another Panel 1");  
        configPanel(portlet);  
        portlet.addText(getBogusText());  
        portal.add(portlet, 0);  
      
        portlet = new Portlet();  
        portlet.setHeading("Panel 2");  
        configPanel(portlet);  
        portlet.addText(getBogusText());  
        portal.add(portlet, 1);  
      
        portlet = new Portlet();  
        portlet.setHeading("Another Panel 2");  
        configPanel(portlet);  
        portlet.addText(getBogusText());  
        portal.add(portlet, 1);  
      
        portlet = new Portlet();  
        portlet.setHeading("Panel 3");  
        configPanel(portlet);  
        portlet.addText(getBogusText());  
        portal.add(portlet, 2);  
//    	
//        this.view = view;
//        this.view.setHeading(I18N.CONSTANTS.welcomeMessage());
//        this.view.setIntro(I18N.CONSTANTS.selectCategory());
//
////        this.view.add(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.dataEntryDescription(), 
////        		"form.png", new DashboardPageState());
//
//        this.view.add(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.dataEntryDescription(), 
//        		"form.png", new DataEntryPlace());
//
//        this.view.add(I18N.CONSTANTS.siteLists(), I18N.CONSTANTS.siteListsDescriptions(),
//                "grid.png", new DataEntryPlace());
//
//        this.view.add(I18N.CONSTANTS.pivotTables(), I18N.CONSTANTS.pivotTableDescription(),
//                "pivot.png", new PivotPageState());
//
//        this.view.add(I18N.CONSTANTS.charts(), I18N.CONSTANTS.chartsDescription(),
//                "charts/time.png", new ChartPageState());
//createGrid
//        this.view.add(I18N.CONSTANTS.maps(), I18N.CONSTANTS.mapsDescription(),
//                "map.png", new MapPageState());

    }

    private Widget createGrid() {
		return new Label("foo");
	}

	private String getBogusText() {
		return "the quick brown fox jumped over the slow lazy dog";
	}

	private void configPanel(final ContentPanel panel) {  
        panel.setCollapsible(true);  
        panel.setAnimCollapse(false);  
        panel.getHeader().addTool(new ToolButton("x-tool-gear"));  
        panel.getHeader().addTool(  
            new ToolButton("x-tool-close", new SelectionListener<IconButtonEvent>() {  
      
              @Override  
              public void componentSelected(IconButtonEvent ce) {  
                panel.removeFromParent();  
              }  
      
            }));  
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
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
	public String beforeWindowCloses() {
        return null;
    }

    @Override
	public void shutdown() {

    }

    @Override
	public boolean navigate(PageState place) {
        return true;
    }
}