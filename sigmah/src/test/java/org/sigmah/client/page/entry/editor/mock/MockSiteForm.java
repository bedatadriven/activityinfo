package org.sigmah.client.page.entry.editor.mock;

import com.extjs.gxt.ui.client.store.ListStore;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.entry.editor.AdminFieldSetPresenter;
import org.sigmah.client.page.entry.editor.MapPresenter;
import org.sigmah.client.page.entry.editor.SiteFormPresenter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;

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

    public void init(SiteFormPresenter presenter, ActivityDTO activity, ListStore<PartnerDTO> partnerStore, ListStore<SiteDTO> assessmentStore) {

    }

    public void setSite(SiteDTO site) {
        properties = site.getProperties();
    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityDTO activity) {
        return adminFieldSet;
    }

    @Override
    public MapPresenter.View createMapView(CountryDTO country) {
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
