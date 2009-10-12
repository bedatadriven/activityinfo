package org.activityinfo.client.page.entry.editor;

import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.SiteModel;

import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormDialog extends FormDialogImpl<SiteForm> implements SiteFormPresenter.View {

    private SiteFormPresenter presenter;
    private ActivityModel activity;

    public SiteFormDialog(SiteForm form) {
        super(form);

		int clientHeight = com.google.gwt.user.client.Window.getClientHeight();

		this.setHeight((int) (clientHeight * 0.95));
		this.setWidth(450);
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {

        if(UIActions.save.equals(actionId)) {
            saveButton.setEnabled(enabled);
        }
    }

    @Override
    public AsyncMonitor getMonitor() {
        return this;
    }

    public void init(SiteFormPresenter presenter, ActivityModel activity, ListStore<PartnerModel> partnerStore, ListStore<SiteModel> assessmentStore) {
        this.presenter = presenter;
        this.activity = activity;
        form.init(presenter, activity, partnerStore, assessmentStore);
       
    }

    public void setSite(SiteModel site) {
        form.show();
        if(site.getLocationName() == null) {
            setHeading(activity.getName());
        } else {
            setHeading(activity.getName() + " - " + site.getLocationName());
        }
        
        form.setSite(site);

        show();

    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityModel activity) {
        return form.createAdminFieldSetView(activity);
    }

    @Override
    public MapPresenter.View createMapView() {
        return form.createMapView();
    }

    @Override
    public boolean validate() {
        return form.validate();
    }

    @Override
    public Map<String, Object> getChanges() {
        return form.getChanges();
    }

    @Override
    public boolean isDirty() {
        return form.isDirty();
    }

    @Override
    public void onValidated() {
        presenter.onUIAction(UIActions.save);
    }

    public Map<String, Object> getPropertyMap() {
        return form.getPropertyMap();
    }
}
