package org.sigmah.client.page.summaries;

import java.util.Date;
import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.View;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

/*
 * Widget displaying a given site as a summary in read-only mode
 */
public class SitePresenter {
	public interface SiteView extends View<SiteDTO> {
	
	}
	
	public class SiteSummary extends LayoutContainer implements SiteView {
		// Data
		private SiteDTO site;
		private List<SiteDTO> sites;

		// General UI fields
		private FieldSet fieldsetGeneral;
		private LabelField labelNumberSites;
		private LabelField labelDatabaseName;
		private LabelField labelPartnerName;
		private LabelField labelProjectName;
		private LabelField labelTotalTime;

		// Activity UI fields
		private FieldSet fieldsetSite;
		private LabelField labelSiteName;

		// Comment UI fields
		private FieldSet fieldsetComment;
		private LabelField labelComment;

		public SiteSummary() {
			createGeneralFieldset();
			createActivityFieldset();
			createLocationFieldset();
			createAttributeFieldset();
			createIndicatorFieldset();
			createCommentFieldset();
		}

		private void createGeneralFieldset() {
			labelDatabaseName = new LabelField();
			labelDatabaseName.setFieldLabel(I18N.CONSTANTS.database());
			fieldsetGeneral.add(labelDatabaseName);
			
			labelProjectName = new LabelField();
			labelProjectName.setFieldLabel(I18N.CONSTANTS.project());
			fieldsetGeneral.add(labelProjectName);
			
			labelPartnerName = new LabelField();
			labelPartnerName.setFieldLabel(I18N.CONSTANTS.partner());
			fieldsetGeneral.add(labelPartnerName);
			
			labelTotalTime = new LabelField();
			labelTotalTime.setFieldLabel(I18N.CONSTANTS.timePeriod());
			fieldsetGeneral.add(labelTotalTime);
		}

		private void createCommentFieldset() {
			labelComment = new LabelField();
			fieldsetComment.add(labelComment);

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
			
		}

		private void createActivityFieldset() {
			// TODO Auto-generated method stub

		}

		@Override
		public void initialize() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public AsyncMonitor getAsyncMonitor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setValue(SiteDTO value) {
			this.site = value;
			
			// Site
			//labelDatabaseName.setText
			labelPartnerName.setText(site.getPartnerName());
			labelProjectName.setText(site.getProjectName());
			labelTotalTime.setText(createPeriod(site.getDate1(), site.getDate2()));
			
			labelComment.setText(site.getComments());
		}
		
		private String createPeriod(Date from, Date to) {
			return from.toString() + " " + I18N.CONSTANTS.toDate() + " " + to.toString();
		}

		@Override
		public SiteDTO getValue() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private EventBus eventBus;
	private Dispatcher service;
	private SiteView view;
	private SiteDTO site;
	
	@Inject
	public SitePresenter(EventBus eventBus, Dispatcher service, SiteView view) {
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
		
		getData();
	}

	private void getData() {
	}

	public void setSite(SiteDTO site) {
		this.site = site;
	}

	public SiteDTO getSite() {
		return site;
	}
}
