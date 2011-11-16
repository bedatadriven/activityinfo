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
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SitesList extends LayoutContainer {

	private final AppInjector injector;
	private final int databaseId;

	int defaultActivityId = 0;

	@Inject
	public SitesList(AppInjector injector, int databaseId) {
		this.injector = injector;
		this.databaseId = databaseId;

		setLayout(new BorderLayout());

		// setHeight(500);
		loadSiteGrid();
	}

	public void addToContainer(Widget w) {
		add(w, new BorderLayoutData(Style.LayoutRegion.CENTER));
		if (isRendered()) {
			layout();
		}
	}

	protected void loadSiteGrid() {

		injector.getService().execute(new GetSchema(), null,
				new Got<SchemaDTO>() {
					@Override
					public void got(SchemaDTO schema) {

						setDefaultActivityId(schema);
						ActivityDTO activity = schema
								.getActivityById(defaultActivityId);

						SiteGrid grid;

						grid = new SiteGrid(true);
						grid.setHeight(500);

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

						if (siteEditor != null && databaseExist(schema)) {
							SiteGridPageState place = new SiteGridPageState(
									activity);
							siteEditor.go((SiteGridPageState) place, activity);
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
								.getDatabaseById(databaseId);
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
