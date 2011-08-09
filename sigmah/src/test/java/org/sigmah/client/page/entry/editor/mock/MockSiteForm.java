/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor.mock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.entry.editor.AdminFieldSetPresenter;
import org.sigmah.client.page.entry.editor.MapEditView;
import org.sigmah.client.page.entry.editor.ProjectPresenter;
import org.sigmah.client.page.entry.editor.SiteFormPresenter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockSiteForm implements SiteFormPresenter.View {

    public MockAdminFieldSet adminFieldSet = new MockAdminFieldSet();
    public MockProjectFieldSet projectFieldSet = new MockProjectFieldSet();
    public MockMapView mapView = new MockMapView();
    public Map<String, Object> changes = new HashMap<String, Object>();
    public Set<String> disabledActions = new HashSet<String>();
    public Map<String, Object> properties = new HashMap<String, Object>();

	@Override
	public void init(SiteFormPresenter presenter, ActivityDTO activity,
			ListStore<PartnerDTO> partnerStore,
			ListStore<SiteDTO> assessmentStore,
			ListStore<ProjectDTO> projectStore) {
	}

    public void setSite(SiteDTO site) {
        properties = site.getProperties();
    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityDTO activity) {
        return adminFieldSet;
    }

    @Override
    public MapEditView createMapView(CountryDTO country) {
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

	@Override
	public ProjectPresenter.ProjectPickerView createProjectView(ProjectDTO project) {
		return projectFieldSet; 
	}


}
