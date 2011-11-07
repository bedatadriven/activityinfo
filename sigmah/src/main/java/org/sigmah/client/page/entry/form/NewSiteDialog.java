package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.ActivityDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;

public class NewSiteDialog extends Window {
	
	private FormNavigationListView navigationListView;
	private LayoutContainer sectionContainer;
	private CardLayout sectionLayout;
	
	private Button prevButton;
	private Button nextButton;
	private Button finishButton;
		
	public NewSiteDialog(Dispatcher dispatcher, ActivityDTO activity) {
		setHeading(I18N.MESSAGES.addNewSiteForActivity(activity.getName()));
		setWidth((int)(com.google.gwt.user.client.Window.getClientWidth() * 0.85));
		setHeight((int)(com.google.gwt.user.client.Window.getClientHeight() * 0.90));
	
		setLayout(new BorderLayout());
		
		navigationListView = new FormNavigationListView();
		BorderLayoutData navigationLayout = new BorderLayoutData(LayoutRegion.WEST);
		navigationLayout.setSize(150);
		add(navigationListView, navigationLayout);
		
		sectionContainer = new LayoutContainer();
		sectionLayout = new CardLayout();
		sectionContainer.setLayout(sectionLayout);
		
		add(sectionContainer, new BorderLayoutData(LayoutRegion.CENTER));
		
		BoundLocationSection locationForm = new BoundLocationSection(dispatcher, activity);
		addSection(new FormSectionModel()
				.withHeader(I18N.CONSTANTS.location())
				.withDescription("Choose the location of the activity site")
				.forComponent(locationForm));

		addSection(new FormSectionModel()
				.withHeader(I18N.CONSTANTS.attributes())
				.withDescription("Choose the attributes of this activity site")
				.forComponent(new AttributeSection(activity)));
		
		addSection(new FormSectionModel()
				.withHeader(I18N.CONSTANTS.comments())
				.withDescription("Add additional comments for this activity site")
				.forComponent(new CommentSection()));
		
	
		sectionLayout.setActiveItem(locationForm);
		
		SiteFormResources.INSTANCE.style().ensureInjected();
		
		navigationListView.addListener(Events.Select, new Listener<ListViewEvent<FormSectionModel>>() {

			@Override
			public void handleEvent(ListViewEvent<FormSectionModel> be) {
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
				navigationListView.prev();
			}
		});
		
		finishButton = new Button("Finish", new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				navigationListView.next();
			}
		});
		
		getButtonBar().add(prevButton);
		getButtonBar().add(nextButton);
		getButtonBar().add(finishButton);
	}
	
	private void addSection(FormSectionModel model) {
		navigationListView.addSection(model);
		sectionContainer.add(model.getComponent());
	}

}
