package org.activityinfo.client.page.entry.editor;


import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.maps.client.Maps;

import org.activityinfo.client.Application;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.page.base.*;
import org.activityinfo.client.page.config.form.ModelFormPanel;
import org.activityinfo.shared.dto.*;

import java.util.*;

public class SiteForm extends ModelFormPanel implements SiteFormPresenter.View {

    protected SiteFormPresenter presenter;
	protected ActivityModel activity;

    private ActivityFieldSet activityFieldSet;
    private LocationFieldSet locationFieldSet;
    private MapPresenter.View mapView;
    private AttributeFieldSet attributeFieldSet;
    private IndicatorFieldSet indicatorFieldSet;
    private CommentFieldSet commentFieldSet;


    public SiteForm()
	{
    	this.setBodyStyle("padding: 3px");
		this.setIcon(Application.ICONS.editPage());
        this.setHeading(Application.CONSTANTS.loading());

        add(new LoadingPlaceHolder());

    }



    @Override
    public void init(SiteFormPresenter presenter,
                     ActivityModel activity,
                     ListStore<PartnerModel> partnerStore,
                     ListStore<SiteModel> assessmentStore) {

        removeAll();
        setLayout(new FlowLayout());

        this.presenter = presenter;
        this.activity = activity;

        this.setLayout(new FlowLayout());
        this.setScrollMode(Scroll.AUTOY);
        this.setHeading(activity.getName());

        // ACTIVITY fieldset

        activityFieldSet = new ActivityFieldSet(activity, partnerStore, assessmentStore);
        add(activityFieldSet);

        // LOCATION fieldset

        add(locationFieldSet);

        // GEO POSITION

        add((FieldSet)mapView);

        if(Maps.isLoaded()) {
            registerField(((MapFieldSet) mapView).getLngField());
            registerField(((MapFieldSet)mapView).getLatField());
        } else {
            registerFieldSet((FieldSet)mapView);
        }
                
        // ATTRIBUTE fieldset

        if(activity.getReportingFrequency() == ActivityModel.REPORT_ONCE) {

            attributeFieldSet = new AttributeFieldSet(activity);
            registerFieldSet(attributeFieldSet);
            add(attributeFieldSet);

            // INDICATOR fieldset

            indicatorFieldSet = new IndicatorFieldSet(activity);
            registerFieldSet(indicatorFieldSet);
            add(indicatorFieldSet);


            // COMMENT

            commentFieldSet = new CommentFieldSet();
            add(commentFieldSet);
        }
            
        registerAll();

        layout();
    }


    public void setSite(SiteModel site) {
        updateForm(site);

    }

    @Override
    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.saving());
    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityModel activity) {

        locationFieldSet = new LocationFieldSet(activity);
        
        return locationFieldSet;
    }

    @Override
    public MapPresenter.View createMapView() {

        if(Maps.isLoaded()) {
            MapFieldSet mapFieldSet = new MapFieldSet();
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
        return true;
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
}
