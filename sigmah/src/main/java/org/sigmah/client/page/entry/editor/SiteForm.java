/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.config.form.ModelFormPanel;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.collect.Maps;

public class SiteForm extends ModelFormPanel implements SiteFormPresenter.View {
    protected SiteFormPresenter presenter;
    protected ActivityDTO activity;
    protected SiteDTO site;
	private Dispatcher service;
	private EventBus eventBus;
	
    private ActivityFieldSet activityFieldSet;
    private LocationView locationFieldSet;
    private AttributeFieldSet attributeFieldSet;
    private IndicatorFieldSet indicatorFieldSet;
    private CommentFieldSet commentFieldSet;

    private TabPanel tabPanel;
    
    public SiteForm(Dispatcher service, EventBus eventBus) {
		this.service=service;
		this.eventBus=eventBus;
//        this.setIcon(IconImageBundle.ICONS.editPage());
//        this.setHeading(I18N.CONSTANTS.loading());

        add(new LoadingPlaceHolder());
    }

    @Override        
    public void init(SiteFormPresenter presenter,
                     ActivityDTO activity,
                     ListStore<PartnerDTO> partnerStore,
                     ListStore<SiteDTO> assessmentStore, 
                     ListStore<ProjectDTO> projectStore) {
        this.presenter = presenter;       
        this.activity = activity;

        removeAll();
        setLayout(new FitLayout());
        
        tabPanel = new TabPanel();
        add(tabPanel);

        // ACTIVITY fieldset
        activityFieldSet = new ActivityFieldSet(activity, partnerStore, assessmentStore, projectStore);
        registerFieldSet(activityFieldSet);
        
        addFlowTab(I18N.CONSTANTS.details(), activityFieldSet);
        
        locationFieldSet = new LocationView(eventBus, service, activity);
        
        registerFieldSet(locationFieldSet, true);
        addFitTab(I18N.CONSTANTS.location(), locationFieldSet);

        // ATTRIBUTE fieldset
        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            attributeFieldSet = new AttributeFieldSet(activity);
            registerFieldSet(attributeFieldSet);
           
            // INDICATOR fieldset
            indicatorFieldSet = new IndicatorFieldSet(activity);
            registerFieldSet(indicatorFieldSet);
            addFlowTab(I18N.CONSTANTS.attributes(), attributeFieldSet, indicatorFieldSet);
        }

        // COMMENT
        commentFieldSet = new CommentFieldSet();
        registerFieldSet(commentFieldSet);
        addFlowTab(I18N.CONSTANTS.comments(), commentFieldSet);

        layout();
    }

	
	private void addFlowTab(String title, Component... components) {
		TabItem tab = new TabItem();
        tab.setText(title);
        tab.setScrollMode(Scroll.AUTOY);
        for(Component component : components) {
        	tab.add(component);
        }
        
        tabPanel.add(tab);
	}

	private void addFitTab(String title, Component component) {
		TabItem tab = new TabItem();
        tab.setText(title);
        tab.setLayout(new FitLayout());
       	tab.add(component);
        
        tabPanel.add(tab);
	}

    public void setSite(SiteDTO site) {
        updateForm(site);
        locationFieldSet.setSite(site);
    }

    @Override
    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.saving());
    }

    @Override
    public Map<String, Object> getChanges() {
        Map<String, Object> changes = Maps.newHashMap();
        changes.putAll(super.getModified());
        changes.putAll(locationFieldSet.getChanges());
        return changes;
    }

    @Override
    public boolean validate() {
        return isValid(false);
    }

    @Override
    protected void onDirtyFlagChanged(boolean isDirty) {
        presenter.onModified();
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {

    }

    @Override
	public boolean isDirty() {
		return super.isDirty() || locationFieldSet.isDirty();
	}

	public Map<String, Object> getPropertyMap() {
        Map<String, Object> changes = Maps.newHashMap();
        
        changes.putAll(super.getAllValues());
        changes.putAll(locationFieldSet.getAllValues());
        
        return changes;
    }
    
    private void fixUpProperties(Map<String,Object> map) {
    	if(map.containsKey("partner")) {
    		PartnerDTO partner = (PartnerDTO)map.get("partner");
    		if(partner != null) {
    			map.put("partnerId", partner.getId());
    		}
    		map.remove("partner");
    	}
    }

}
