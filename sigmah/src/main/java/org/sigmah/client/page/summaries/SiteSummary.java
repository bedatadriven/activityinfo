package org.sigmah.client.page.summaries;

import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.editor.SiteFormPresenter;
import org.sigmah.client.page.entry.editor.SiteFormPresenter.View;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextArea;

/*
 * Widget displaying a given site as a summary in read-only mode
 */
public class SiteSummary extends LayoutContainer implements View {
	// Data
	private SiteDTO site;
	private List<SiteDTO> sites;
	
	// Activity UI fields
	private FieldSet fieldsetSite;
	private LabelField labelSiteName;
	
	// Sites UI fields
	private FieldSet fieldsetSites;
	private LabelField labelNumberSites;
	
	// Comment UI fields
	private FieldSet fieldsetComment;
	private LabelField labelComment;
	
	@Override
	public void init(SiteFormPresenter presenter, ActivityDTO activity,
			ListStore<PartnerDTO> partnerStore,
			ListStore<SiteDTO> assessmentStore,
			ListStore<ProjectDTO> projectStore) {
		
		createActivityFieldset();
		createLocationFieldset();widget
		createAttributeFieldset();
		createIndicatorFieldset();
		createCommentFieldset();
	}
	
	private void createCommentFieldset() {
		labelComment = new LabelField();
		fieldsetComment.add(labelComment);
		labelComment.setText(text)
		
		fieldsetComment = new FieldSet();
		fieldsetComment.setHeading(I18N.CONSTANTS.comments());
		add(fieldsetComment);
	}

	private void createIndicatorFieldset() {
		// TODO Auto-generated method stub
		
	}

	private void createAttributeFieldset() {
		// TODO Auto-generated method stub
		
	}

	private void createLocationFieldset() {
		// TODO Auto-generated method stub
		
	}

	private void createActivityFieldset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSite(SiteDTO site) {
		this.site = site;
	}
	
	@Override
	public org.sigmah.client.page.entry.editor.AdminFieldSetPresenter.View createAdminFieldSetView(
			ActivityDTO activity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public org.sigmah.client.page.entry.editor.MapPresenter.View createMapView(
			CountryDTO country) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public org.sigmah.client.page.entry.editor.ProjectPresenter.View createProjectView(
			ProjectDTO project) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean validate() {
		return true;
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}
	
	@Override
	public Map<String, Object> getChanges() {
		return null;
	}
	
	@Override
	public Map<String, Object> getPropertyMap() {
		return null;
	}
	
	@Override
	public AsyncMonitor getMonitor() {
		return null;
	}
	
	@Override
	public void setActionEnabled(String actionId, boolean enabled) {
		
	}
	
	@Override
	public void hide() {
	}
}
