package org.activityinfo.embed.client;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.AbstractSiteEditor;
import org.sigmah.client.page.entry.AbstractSiteGrid;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.client.page.entry.SiteGrid;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.entry.SiteMap;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SitesList extends LayoutContainer {

	private int activityId;

	int defaultActivityId = 0;

	private final EventBus eventBus;
	private final Dispatcher dispatcher;
	private final StateProvider stateProvider;


	@Inject
	public SitesList(EventBus eventBus, Dispatcher dispatcher, StateProvider stateProvider) {
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.stateProvider = stateProvider;
		
		setLayout(new FitLayout());

	}

	public void load(int databaseId) {
		this.activityId = databaseId;
		loadSiteGrid();

	}
	
	public void addToContainer(Widget w) {
		add(w);
		if (isRendered()) {
			layout();
		}
	}

	protected void loadSiteGrid() {

		dispatcher.execute(new GetSchema(), null,
				new Got<SchemaDTO>() {

					@Override
					public void got(SchemaDTO schema) {

						ActivityDTO activity = schema
								.getActivityById(activityId);
						
						if(activity == null) {
							add(new Label("Sorry, this activity is not published."));
							layout();
							return;
						}

						SiteGrid grid;

						grid = new SiteGrid(true);

						AbstractSiteGrid abstractSiteGrid = null;
						AbstractSiteEditor abstractSiteEditor = null;
						SiteEditor siteEditor = null;

						siteEditor = new SiteEditor(eventBus,
								dispatcher, stateProvider, grid);
						abstractSiteGrid = grid;
						abstractSiteEditor = siteEditor;

						SiteMap map = new SiteMap(eventBus,
								dispatcher, activity);
						abstractSiteEditor.addSubComponent(map);
						abstractSiteGrid.addSidePanel(I18N.CONSTANTS.map(),
								IconImageBundle.ICONS.map(), map);

						if (siteEditor != null && databaseExist(schema)) {
							SiteGridPageState place = new SiteGridPageState(
									activity);
							siteEditor.go(place, activity);
						}

						addToContainer((Widget) abstractSiteEditor.getWidget());
					}

					private void setDefaultActivityId(SchemaDTO schema) {

						if (schema.getFirstActivity() != null) {
							defaultActivityId = schema.getFirstActivity()
									.getId();
						}

					}

					private boolean databaseExist(SchemaDTO schema) {
						UserDatabaseDTO userDatabase = schema
								.getDatabaseById(activityId);
						if (userDatabase == null) {
							return false;
						}

						return true;
					}

					@Override
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}
				});

	}
}
