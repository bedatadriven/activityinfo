/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.config.form.ModelFormPanel;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.maps.client.Maps;

public class SiteForm extends ModelFormPanel implements SiteFormPresenter.View {

    protected SiteFormPresenter presenter;
    protected ActivityDTO activity;
    protected SiteDTO site;

    private ActivityFieldSet activityFieldSet;
    private LocationFieldSet locationFieldSet;
    private MapEditView mapView;
    private AttributeFieldSet attributeFieldSet;
    private IndicatorFieldSet indicatorFieldSet;
    private CommentFieldSet commentFieldSet;

    private TabPanel tabPanel;
    
    public SiteForm() {
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

        removeAll();
        setLayout(new FitLayout());
        
        tabPanel = new TabPanel();
        add(tabPanel);
        
        this.presenter = presenter;       
        this.activity = activity;

        // ACTIVITY fieldset
        activityFieldSet = new ActivityFieldSet(activity, partnerStore, assessmentStore, projectStore);
        registerFieldSet(activityFieldSet);
        
        addTab(I18N.CONSTANTS.details(), activityFieldSet);
        
        // LOCATION fieldset
        

        // GEO POSITION        this.site = site;

        if (Maps.isLoaded()) {
            registerField(((MapFieldSet) mapView).getLngField());
            registerField(((MapFieldSet) mapView).getLatField());
        } else {
            registerFieldSet((FieldSet) mapView);
        }

        addTab(I18N.CONSTANTS.location(), locationFieldSet, (FieldSet)mapView);

        
        // ATTRIBUTE fieldset
        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {

            attributeFieldSet = new AttributeFieldSet(activity);
            registerFieldSet(attributeFieldSet);

            // INDICATOR fieldset

            indicatorFieldSet = new IndicatorFieldSet(activity);
            registerFieldSet(indicatorFieldSet);
           
            
            addTab(I18N.CONSTANTS.attributes(), attributeFieldSet, indicatorFieldSet);
        }

        // COMMENT
        commentFieldSet = new CommentFieldSet();
        registerFieldSet(commentFieldSet);
        addTab(I18N.CONSTANTS.comments(), commentFieldSet);


        layout();
    }

	
	private void addTab(String title, Component... components) {
		TabItem tab = new TabItem();
        tab.setText(title);
        tab.setScrollMode(Scroll.AUTOY);
        for(Component component : components) {
        	tab.add(component);
        }
        
        tabPanel.add(tab);
	}

    public void setSite(SiteDTO site) {
        updateForm(site);

    }

    @Override
    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.saving());
    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityDTO activity) {
        locationFieldSet = new LocationFieldSet(activity);
        registerFieldSet(locationFieldSet);
        return locationFieldSet;
    }

    @Override
    public MapEditView createMapView(CountryDTO country) {

        if (Maps.isLoaded()) {
            MapFieldSet mapFieldSet = new MapFieldSet(country);
            this.mapView = mapFieldSet;
        } else {
            CoordFieldSet coordFieldSet = new CoordFieldSet();
            this.mapView = coordFieldSet;
        }

        return mapView;
    }

    @Override
    public Map<String, Object> getChanges() {
        return super.getModified();
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

    public Map<String, Object> getPropertyMap() {
        return super.getAllValues();
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
