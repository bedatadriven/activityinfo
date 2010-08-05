/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import java.util.Arrays;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.element.FlexibleElementDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

/**
 * Initializes the view elements of a project page.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectView extends LayoutContainer implements ProjectPresenter.View  {
	
	private TabPanel tabPanelPhases;
    private LayoutContainer panelProjectModel;
    private LayoutContainer panelSelectedPhase;
    private ContentPanel panelPhaseGuide;
    
    private ContentPanel panelProjectBanner;
    private ContentPanel panelReminders;
    private ContentPanel panelWatchedPoints;
    private ContentPanel panelFinancialProjects;
    private ContentPanel panelLocalProjects;
    private Grid<FlexibleElementDTO> gridRequiredElements;

    /**
     * Initializes a new ProjectView object.
     */
    public ProjectView() {
    	if (Log.isDebugEnabled()) {
    		Log.debug("Initializing the ProjectView object.");
    	}
    	
    	setLayout(new BorderLayout());
    	
    	/* North panel */
    	panelProjectBanner = new ContentPanel();
    	panelProjectBanner.setHeading(I18N.CONSTANTS.projectBannerHeader());
    	panelProjectBanner.setBorders(true);
    	
        /* Center panel */
    	ListStore<FlexibleElementDTO> storeRequiredElements = new ListStore<FlexibleElementDTO>();
    	gridRequiredElements = new Grid<FlexibleElementDTO>(storeRequiredElements, getColumModel());
    	gridRequiredElements.setStyleName(I18N.CONSTANTS.projectRequiredElementsGridStyle());
    	gridRequiredElements.setAutoExpandColumn("label");
    	
    	// Phases tab panel
    	tabPanelPhases = new TabPanel();
    	
    	// Tab item main panel
    	RowLayout layout = new RowLayout(Orientation.HORIZONTAL);
    	panelProjectModel = new LayoutContainer(layout);
    	
    	panelSelectedPhase = new LayoutContainer(new FitLayout());
    	panelPhaseGuide = new ContentPanel(new FitLayout());
    	panelPhaseGuide.setHeading(I18N.CONSTANTS.projectPhaseGuideHeader());
    	
    	panelProjectModel.add(gridRequiredElements, new RowData(0.33, 1, new Margins(4)));
    	panelProjectModel.add(panelSelectedPhase, new RowData(0.34, 1, new Margins(4)));
    	panelProjectModel.add(panelPhaseGuide, new RowData(0.33, 1, new Margins(4)));
    	
        /* West panel */
        VerticalPanel westPanel = new VerticalPanel();
        panelReminders = new ContentPanel();
        panelReminders.setHeading(I18N.CONSTANTS.projectRemindersHeader());
        panelReminders.addText("This panel displays the reminders.");
        panelReminders.setBorders(true);
        panelReminders.setCollapsible(true);
        panelReminders.setWidth(250);
        
        panelWatchedPoints = new ContentPanel();
        panelWatchedPoints.setHeading(I18N.CONSTANTS.projectWatchedPointsHeader());
        panelWatchedPoints.addText("This panel displays the watched points.");
        panelWatchedPoints.setBorders(true);
        panelWatchedPoints.setCollapsible(true);
        panelWatchedPoints.setWidth(250);
        
        westPanel.add(panelReminders);
        westPanel.add(panelWatchedPoints);
        
        /* South panel */
        VerticalPanel southPanel = new VerticalPanel();
        panelFinancialProjects = new ContentPanel();
        panelFinancialProjects.setBorders(true);
        panelFinancialProjects.setHeading(I18N.CONSTANTS.projectFinancialProjectsHeader());
        panelFinancialProjects.addText("This panel displays the financial projects.");
        
        panelLocalProjects = new ContentPanel();
        panelLocalProjects.setHeading(I18N.CONSTANTS.projectLocalPartnerProjectsHeader());
        panelLocalProjects.setBorders(true);
        panelLocalProjects.addText("This panel displays the local partner projects.");
        southPanel.add(panelFinancialProjects);
        southPanel.add(panelLocalProjects);
        
        /* BorderLayoutData */
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 100);
        northData.setMargins(new Margins(5));
        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH);
        southData.setMargins(new Margins(5));
        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 250);
        westData.setMargins(new Margins(5));
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));
        
        add(panelProjectBanner, northData);
        add(southPanel, southData);
        add(westPanel, westData);
        add(tabPanelPhases, centerData);
    }
    
    /**
     * Generates the {@link ColumnModel} for the required elements grid.
     * 
     * @return the {@link ColumnModel} for the required elements grid.
     */
    private ColumnModel getColumModel() {
    	CheckColumnConfig checkConfig = new CheckColumnConfig("filledIn", I18N.CONSTANTS.projectRequiredElementsGridChecked(), 75);
    	checkConfig.setMenuDisabled(false);
    	checkConfig.setSortable(false);
    	ColumnConfig configName = new ColumnConfig("label", I18N.CONSTANTS.projectRequiredElementsGridLabel(), 230);
    	return new ColumnModel(Arrays.asList(configName, checkConfig));
    }
	
	public LayoutContainer getPanelProjectModel() {
		return panelProjectModel;
	}
	
	public ContentPanel getPanelProjectBanner() {
		return panelProjectBanner;
	}
	
	public ContentPanel getPanelReminders() {
		return panelReminders;
	}
	
	public ContentPanel getPanelWatchedPoints() {
		return panelWatchedPoints;
	}
	
	public ContentPanel getPanelFinancialProjects() {
		return panelFinancialProjects;
	}
	
	public ContentPanel getPanelLocalProjects() {
		return panelLocalProjects;
	}
	
	public TabPanel getTabPanelPhases() {
		return tabPanelPhases;
	}

	public Grid<FlexibleElementDTO> getGridRequiredElements() {
		return gridRequiredElements;
	}
	
	public LayoutContainer getPanelSelectedPhase() {
		return panelSelectedPhase;
	}
	
	public ContentPanel getPanelPhaseGuide() {
		return panelPhaseGuide;
	}
	
}
