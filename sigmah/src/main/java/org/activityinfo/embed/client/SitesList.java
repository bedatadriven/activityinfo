package org.activityinfo.embed.client;

import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.entry.AbstractSiteEditor;
import org.sigmah.client.page.entry.AbstractSiteGrid;
import org.sigmah.client.page.entry.SiteEditor;
import org.sigmah.client.page.entry.SiteGrid;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.entry.SiteMap;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SitesList extends ContentPanel {

	private final AppInjector injector;
	SiteEditor siteEditor;
	SiteGrid grid;

	int defaultActivityId = 0;

	@Inject
	public SitesList(AppInjector injector, int databaseId) {
		this.injector = injector;
		UserDatabaseDTO database = new UserDatabaseDTO();

		setHeaderVisible(false);
		setHeight(500);
		loadSiteGrid();
		
		grid = new SiteGrid(true);
		grid.setHeight(500);

		add(grid);
		// add(grid.getGrid());

	}

	protected void loadSiteGrid() {

		injector.getService().execute(new GetSchema(), null,
				new Got<SchemaDTO>() {
					@Override
					public void got(SchemaDTO schema) {

						setDefaultActivityId(schema);
						ActivityDTO activity = schema
								.getActivityById(defaultActivityId);

						AbstractSiteGrid abstractSiteGrid = null;
						AbstractSiteEditor abstractSiteEditor = null;
						SiteEditor siteEditor = null;

						siteEditor = new SiteEditor(injector.getEventBus(),
								injector.getService(), injector
										.getStateManager(), grid);
						abstractSiteGrid = grid;
						abstractSiteEditor = siteEditor;

						SiteMap map = new SiteMap(injector.getEventBus(),
								injector.getService(), activity);
						abstractSiteEditor.addSubComponent(map);
						abstractSiteGrid.addSidePanel(I18N.CONSTANTS.map(),
								IconImageBundle.ICONS.map(), map);

						if (siteEditor != null) {
							SiteGridPageState place = new SiteGridPageState(
									activity);
							siteEditor.go((SiteGridPageState) place, activity);
						}

						// call back or whatever

						// callback.onSuccess(abstractSiteEditor);
					}

					private void setDefaultActivityId(SchemaDTO schema) {

						if (schema.getFirstActivity() != null) {
							defaultActivityId = schema.getFirstActivity()
									.getId();
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}
				});

	}
}
