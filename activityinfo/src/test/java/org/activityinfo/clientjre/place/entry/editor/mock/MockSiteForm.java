package org.activityinfo.clientjre.place.entry.editor.mock;

import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.client.page.entry.editor.MapPresenter;
import org.activityinfo.client.page.entry.editor.SiteFormPresenter;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.SiteModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockSiteForm implements SiteFormPresenter.View {

    public MockAdminFieldSet adminFieldSet = new MockAdminFieldSet();
    public MockMapView mapView = new MockMapView();
    public Map<String, Object> changes = new HashMap<String, Object>();
    public Set<String> disabledActions = new HashSet<String>();
    public Map<String, Object> properties = new HashMap<String, Object>();

    public void init(SiteFormPresenter presenter, ActivityModel activity, ListStore<PartnerModel> partnerStore, ListStore<SiteModel> assessmentStore) {

    }

    public void setSite(SiteModel site) {
        properties = site.getProperties();
    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityModel activity) {
        return adminFieldSet;
    }

    @Override
    public MapPresenter.View createMapView() {
        return mapView;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public Map<String, Object> getChanges() {
        return changes;
    }

    public Map<String, Object> getPropertyMap() {
        return properties;
    }

    @Override
    public AsyncMonitor getMonitor() {
        return null;
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {
        if (enabled) {
            disabledActions.remove(actionId);
        } else {
            disabledActions.add(actionId);
        }
    }

    public boolean isEnabled(String actionId) {
        return !disabledActions.contains(actionId);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    public void hide() {
    }
}
