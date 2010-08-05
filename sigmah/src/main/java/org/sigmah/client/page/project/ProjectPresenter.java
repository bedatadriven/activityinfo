/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.PhaseModelDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Project presenter which manages the {@link ProjectView}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectPresenter implements Page, TabPage {
	
    public static final PageId PAGE_ID = new PageId("project");
    
    private final Dispatcher dispatcher;
    private final View view;
    private final Map<Integer, TabItem> association;

    @Inject
    public ProjectPresenter(Dispatcher dispatcher, View view) {
        this.dispatcher = dispatcher;
        this.view = view;
        this.association = new HashMap<Integer, TabItem>();
    }    
    
    @Override
    public boolean navigate(PageState place) {
    	final ProjectState projectState = (ProjectState) place;
    	final int projectId = projectState.getProjectId();
    	
    	if (Log.isDebugEnabled()) {
    		Log.debug("Loading project #" +projectId+ "...");
    	}
        
        dispatcher.execute(new GetProject(projectId), null, new AsyncCallback<ProjectDTO>() {
            @Override
            public void onFailure(Throwable throwable) {
            	Log.error("Error, project #" +projectId+ " not loaded.");
            }

            @Override
            public void onSuccess(ProjectDTO projectDTO) {
            	if (Log.isDebugEnabled()) {
            		Log.debug("Project loaded : " + projectDTO.getName());
            	}
            	projectState.setTabTitle(projectDTO.getName());
                loadProjectOnView(projectDTO);
            }
        });

        return true;
    }
    
    /**
     * Loads a {@link ProjectDTO} object on the view.
     * 
     * @param projectDTO the {@link ProjectDTO} object loaded on the view
     */
    private void loadProjectOnView(ProjectDTO projectDTO) {
    	PhaseDTO currentPhaseDTO = projectDTO.getCurrentPhaseDTO();
    	Collections.sort(projectDTO.getPhasesDTO());
    	
    	/* Active phase search */
    	for (PhaseDTO phaseDTO : projectDTO.getPhasesDTO()) {
    		TabItem tabItem = new TabItem(phaseDTO.getPhaseModelDTO().getName());
    		tabItem.setLayout(new FitLayout());
    		tabItem.setEnabled(false);
    		
    		association.put(phaseDTO.getPhaseModelDTO().getId(), tabItem);
			view.getTabPanelPhases().add(tabItem);
    		
    		if (currentPhaseDTO.getId() == phaseDTO.getId()) {
    			tabItem.setEnabled(true);
    			tabItem.getHeader().addStyleName(I18N.CONSTANTS.projectActivePhaseTabStyle());
    			view.getTabPanelPhases().setSelection(tabItem);
    		}
    	}
    	
    	/* Tabs listeners must be added after their creation (or event fired for each tab) */
    	for (PhaseDTO phaseDTO : projectDTO.getPhasesDTO()) {
    		TabItem tabItem = association.get(phaseDTO.getPhaseModelDTO().getId());
    		tabItem.addListener(Events.Select, new TabPhaseListener(projectDTO, phaseDTO));
    	}
    	view.getTabPanelPhases().getSelectedItem().fireEvent(Events.Select);
    	enableSuccessors(currentPhaseDTO);
    	
        /* Project banner element */
        if(projectDTO.getProjectModelDTO().getProjectBannerDTO() != null) {
        	view.getPanelProjectBanner().add(projectDTO.getProjectModelDTO().getProjectBannerDTO().getWidget());
        }
        else {
        	view.getPanelProjectBanner().removeAll();
        }
        
        /* View layouts update */
        view.getPanelProjectBanner().layout();
        view.getPanelFinancialProjects().layout();
    }
    
    /**
     * Loads a project phase into the selected tab panel.
     * 
     * @param projectDTO
     * @param phaseDTO
     */
    private void loadPhaseOnTab(ProjectDTO projectDTO, PhaseDTO phaseDTO) {
    	final Grid layoutGrid = (Grid) phaseDTO.getPhaseModelDTO().getWidget();
    	
    	if (projectDTO.getCurrentPhaseDTO().getId() != phaseDTO.getId()) {
    		Button activatePhaseButton = new Button(I18N.CONSTANTS.projectActivatePhaseButton());
    		activatePhaseButton.addListener(Events.Select, new ActivatePhaseListener(phaseDTO));
    		view.getPanelPhaseGuide().add(activatePhaseButton);
    	}
    	
    	view.getPanelPhaseGuide().addText("Phase guide for phase '" + phaseDTO.getPhaseModelDTO().getName() + "'.");
    	view.getPanelSelectedPhase().add(layoutGrid);
    	
    	/* Elements values + required elements */
        for (final LayoutGroupDTO groupDTO : phaseDTO.getPhaseModelDTO().getLayoutDTO().getLayoutGroupsDTO()) {
        	
        	final FieldSet fieldSet = (FieldSet) groupDTO.getWidget();
        	final FormPanel formPanel = (FormPanel) fieldSet.getWidget(0);
        	layoutGrid.setWidget(groupDTO.getRow(), groupDTO.getColumn(), fieldSet);
        	
        	for (LayoutConstraintDTO constraintDTO : groupDTO.getLayoutConstraintsDTO()) {
        		final FlexibleElementDTO elementDTO = constraintDTO.getFlexibleElementDTO();
        		
        		// Command initialization
        		GetValue command = new GetValue(projectDTO.getId(), elementDTO.getId(), elementDTO.getClass().getName());

        		// Server call to obtain element value
        		dispatcher.execute(command, null, new AsyncCallback<ValueResult>() {
        			@Override
        			public void onFailure(Throwable throwable) {
        				Log.error("Error, element value not loaded.");
        			}

        			@Override
        			public void onSuccess(ValueResult valueResult) {
        				if (Log.isDebugEnabled()) {
        					Log.debug("Element value(s) object : " + valueResult.toString());
        				}
        				Component elementComponent = elementDTO.getComponent(valueResult);
        				formPanel.add(elementComponent, new FormData("100%"));
        				formPanel.layout();
        				
        	       		// Required element ?
                		if (elementDTO.getValidates()) {
                			elementDTO.setFilledIn(valueResult.isValueDefined());
                			elementComponent.addListener(Events.OnClick, new RequiredElementListener(elementDTO));
                			view.getGridRequiredElements().getStore().add(elementDTO);
                		}
        				
        			}

        		});
        		
        	}
        }
    }
    
    /**
     * Internal class handling the required elements events.
     */
    private class RequiredElementListener implements Listener<ComponentEvent> {
    	
    	private FlexibleElementDTO elementDTO;
    	
    	public RequiredElementListener(FlexibleElementDTO elementDTO) {
    		this.elementDTO = elementDTO;
    	}

		@Override
		public void handleEvent(ComponentEvent be) {
			Window.alert("Click on " + elementDTO);
		}
    	
    }
    
    /**
     * Internal class handling the phases tabs changes.
     */
    private class TabPhaseListener implements Listener<ComponentEvent> {
    	
    	private PhaseDTO phaseDTO;
    	private ProjectDTO projectDTO;
    	
    	public TabPhaseListener(ProjectDTO projectDTO, PhaseDTO phaseDTO) {
    		this.phaseDTO = phaseDTO;
    		this.projectDTO = projectDTO;
    	}

		@Override
		public void handleEvent(ComponentEvent be) {
			for (TabItem tab : view.getTabPanelPhases().getItems()) {
				tab.removeAll();
			}
			view.getPanelSelectedPhase().removeAll();
			view.getPanelPhaseGuide().removeAll();
			view.getGridRequiredElements().getStore().removeAll();
			view.getTabPanelPhases().getSelectedItem().add(view.getPanelProjectModel());
			
			loadPhaseOnTab(projectDTO, phaseDTO);
			
			view.getTabPanelPhases().getSelectedItem().layout();
		}
    	
    }
    
    
    /**
     * Internal class handling the phases activation.
     */
    private class ActivatePhaseListener implements Listener<ButtonEvent> {
    	
    	private PhaseDTO phaseDTO;
    	
    	public ActivatePhaseListener(PhaseDTO phaseDTO) {
    		this.phaseDTO = phaseDTO;
    	}
    	
		@Override
		public void handleEvent(ButtonEvent be) {
			String tabStyle = I18N.CONSTANTS.projectActivePhaseTabStyle();
			
	    	for (TabItem item : view.getTabPanelPhases().getItems()) {
				item.getHeader().removeStyleName(tabStyle);
			}
			association.get(phaseDTO.getPhaseModelDTO().getId()).getHeader().addStyleName(tabStyle);
			enableSuccessors(phaseDTO);
			phaseDTO.getParentProjectDTO().setCurrentPhaseDTO(phaseDTO);
			view.getPanelSelectedPhase().getWidget(0).removeFromParent();
			
			// TODO database update (project entry)
		}
    }
    
    /**
     * Enables the successors tabs of the new active phase.
     * @param phaseDTO new active phase
     */
    private void enableSuccessors(PhaseDTO phaseDTO) {
		for (PhaseModelDTO successor : phaseDTO.getPhaseModelDTO().getSuccessorsDTO()) {
			TabItem successorTabItem = association.get(successor.getId());
    		if (successorTabItem != null) {
    			successorTabItem.setEnabled(true);
    		}
    	}
    }
		
    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(ProjectView.class)
    public interface View {
    	public LayoutContainer getPanelProjectModel();
    	
    	public LayoutContainer getPanelSelectedPhase();
    	
    	public ContentPanel getPanelPhaseGuide();
    	
    	public ContentPanel getPanelProjectBanner();
    	
    	public ContentPanel getPanelReminders();
    	
    	public ContentPanel getPanelWatchedPoints();
    	
    	public ContentPanel getPanelFinancialProjects();
    	
    	public ContentPanel getPanelLocalProjects();
    	
    	public TabPanel getTabPanelPhases();
    	
    	public com.extjs.gxt.ui.client.widget.grid.Grid<FlexibleElementDTO> getGridRequiredElements();
    }
    
    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.projectMainTabTitle();
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view;
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
    
}
