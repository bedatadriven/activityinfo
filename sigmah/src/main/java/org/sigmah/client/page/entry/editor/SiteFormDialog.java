/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.entry.editor.ProjectPresenter.ProjectPickerView;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;

public class SiteFormDialog extends FormDialogImpl<SiteForm> implements SiteFormPresenter.View {

    private SiteFormPresenter presenter;
    private ActivityDTO activity;

    public SiteFormDialog(SiteForm form) {
        super(form);

        int clientHeight = com.google.gwt.user.client.Window.getClientHeight();

        this.setHeight((int) (clientHeight * 0.95));
        this.setWidth(450);
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {

        if (UIActions.save.equals(actionId)) {
            saveButton.setEnabled(enabled);
        }
    }

    @Override
    public AsyncMonitor getMonitor() {
        return this;
    }

    public void init(SiteFormPresenter presenter, ActivityDTO activity, ListStore<PartnerDTO> partnerStore, ListStore<SiteDTO> assessmentStore, ListStore<ProjectDTO> projectStore) {
        this.presenter = presenter;
        this.activity = activity;
        form.init(presenter, activity, partnerStore, assessmentStore, projectStore);

    }

    public void setSite(SiteDTO site) {
        form.show();
        if (site.getLocationName() == null) {
            setHeading(activity.getName());
        } else {
            setHeading(activity.getName() + " - " + site.getLocationName());
        }

        form.setSite(site);

        show();

    }

    @Override
    public AdminFieldSetPresenter.View createAdminFieldSetView(ActivityDTO activity) {
        return form.createAdminFieldSetView(activity);
    }

    @Override
    public MapPresenter.EditView createMapView(CountryDTO country) {
        return form.createMapView(country);
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

	@Override
	public ProjectPickerView createProjectView(ProjectDTO project) {
		return form.createProjectView(null);
	}
}
