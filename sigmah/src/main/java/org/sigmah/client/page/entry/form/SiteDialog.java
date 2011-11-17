package org.sigmah.client.page.entry.form;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.common.collect.Lists;

public class SiteDialog extends Window {
	
	private FormNavigationListView navigationListView;
	private LayoutContainer sectionContainer;
	private CardLayout sectionLayout;
	
	private Button prevButton;
	private Button nextButton;
	private Button finishButton;
	
	private List<FormSection<SiteDTO>> sections = Lists.newArrayList();
	private LocationFormSection locationForm;
		
	public SiteDialog(Dispatcher dispatcher, ActivityDTO activity) {
		setHeading(I18N.MESSAGES.addNewSiteForActivity(activity.getName()));
		setWidth(500);
		setHeight(450);
	
		setLayout(new BorderLayout());
		
		navigationListView = new FormNavigationListView();
		BorderLayoutData navigationLayout = new BorderLayoutData(LayoutRegion.WEST);
		navigationLayout.setSize(150);
		add(navigationListView, navigationLayout);
		
		sectionContainer = new LayoutContainer();
		sectionLayout = new CardLayout();
		sectionContainer.setLayout(sectionLayout);
		
		add(sectionContainer, new BorderLayoutData(LayoutRegion.CENTER));

		if(activity.getLocationType().isAdminLevel()) {
			locationForm = new BoundLocationSection(dispatcher, activity);
		} else {
			locationForm = new LocationSection(activity);
		}
		
		addSection(FormSectionModel.forComponent(new ActivitySection(activity))
				.withHeader("Site Details")
				.withDescription("Choose the project and partner implementing this activity"));
		
		addSection(FormSectionModel.forComponent(locationForm)
				.withHeader(I18N.CONSTANTS.location())
				.withDescription("Choose the location of the activity site"));

		addSection(FormSectionModel.forComponent(new AttributeSection(activity))
				.withHeader(I18N.CONSTANTS.attributes())
				.withDescription("Choose the attributes of this activity site"));
		
		addSection(FormSectionModel.forComponent(new IndicatorSection(activity))
				.withHeader(I18N.CONSTANTS.indicators())
				.withDescription("Enter indicator results for this site"));
		
		addSection(FormSectionModel.forComponent(new CommentSection())
				.withHeader(I18N.CONSTANTS.comments())
				.withDescription("Add additional comments for this activity site"));
		
	
		sectionLayout.setActiveItem(locationForm.asComponent());
		
		SiteFormResources.INSTANCE.style().ensureInjected();
		
		navigationListView.addListener(Events.Select, new Listener<ListViewEvent<FormSectionModel<SiteDTO>>>() {

			@Override
			public void handleEvent(ListViewEvent<FormSectionModel<SiteDTO>> be) {
				sectionLayout.setActiveItem(be.getModel().getComponent());
			}
		});
		
		prevButton = new Button("&lt;&lt; Back", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				navigationListView.moveSelectedUp();
			}
		});
		
		nextButton = new Button("&gt;&gt; Next", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
			}
		});
		
		finishButton = new Button("Finish", new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				save();
			}
		});
		
		getButtonBar().add(prevButton);
		getButtonBar().add(nextButton);
		getButtonBar().add(finishButton);
	}
	
	public void showNew(SiteDTO site, LocationDTO location, boolean locationIsNew) {
		for(FormSectionModel<SiteDTO> section : navigationListView.getStore().getModels()) {
			section.getSection().updateForm(site);
		}
	}
	
	
	private void addSection(FormSectionModel<SiteDTO> model) {
		navigationListView.addSection(model);
		sectionContainer.add(model.getComponent());
		sections.add(model.getSection());
	}
	
	private void save() {
		for(FormSectionModel<SiteDTO> section : navigationListView.getStore().getModels()) {
			if(!section.getSection().validate()) {
				navigationListView.getSelectionModel().select(section, false);
				MessageBox.alert(getHeading(), I18N.CONSTANTS.pleaseCompleteForm(), null);
			}
		}
	}

}
