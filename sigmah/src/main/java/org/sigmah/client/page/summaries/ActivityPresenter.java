package org.sigmah.client.page.summaries;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.View;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.shared.EventBus;

public class ActivityPresenter {
	public interface ActivityView extends View<ActivityDTO> {
		public void setSites(List<SiteDTO> sites);
	}
	
	public class ActivityViewImpl extends LayoutContainer implements ActivityView {
		// Data
		private ActivityDTO activity;
		private List<SiteDTO> sites;
		
		// Activity UI fields
		private FieldSet fieldsetActivity;
		private LabelField labelActivityName;
		
		// Sites UI fields
		private FieldSet fieldsetSites;
		private LabelField labelNumberSites;
		
		public ActivityViewImpl() {
			
		}

		@Override
		public void initialize() {
			createActivityUI();
			createSitesUI();
		}

		private void createSitesUI() {
			fieldsetSites = new FieldSet();
			add(fieldsetSites);
			
			labelNumberSites = new LabelField();
			labelNumberSites.setFieldLabel("Amound of related sites:");
		}

		private void createActivityUI() {
			labelActivityName = new LabelField();
			labelActivityName.setFieldLabel(I18N.CONSTANTS.activity());
		}

		@Override
		public void setSites(List<SiteDTO> sites) {
			this.sites=sites;
			
			labelNumberSites.setText(Integer.toString(sites.size()));
		}

		@Override
		public AsyncMonitor getAsyncMonitor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setValue(ActivityDTO value) {
			this.activity = value;
			
			labelActivityName.setText(activity.getName());
		}

		@Override
		public ActivityDTO getValue() {
			return activity;
		}
		
	}
	
	private Dispatcher service;
	private EventBus eventBus;
	
	public ActivityPresenter(Dispatcher service, EventBus eventBus) {
		super();
		this.service = service;
		this.eventBus = eventBus;
	}
	
}
