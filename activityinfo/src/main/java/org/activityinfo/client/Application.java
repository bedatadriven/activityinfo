package org.activityinfo.client;


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.shared.i18n.UIConstants;
import org.activityinfo.shared.i18n.UIMessages;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {

    public static final UIConstants CONSTANTS = (UIConstants)GWT.create(UIConstants.class);
	
	public static final UIMessages MESSAGES = (UIMessages)GWT.create(UIMessages.class);

    public static final IconImageBundle ICONS = (IconImageBundle)GWT.create(IconImageBundle.class);


    /**
	 * This is the entry point method.
	 */
	public void onModuleLoad() { 

		GXT.setDefaultTheme(Theme.BLUE, true);

        AppInjector injector = GWT.create(AppInjector.class);

        injector.createSchemaCache();
        injector.createAdminEntityCache();

        injector.createWelcomeLoader();
        injector.createDataEntryLoader();
        injector.createPivotLoader();
        injector.createChartLoader();
        injector.createMapLoader();
        injector.createReportLoader();
        injector.createConfigLoader();

        injector.createSchemaCache();
        injector.createAdminEntityCache();

        injector.createHistoryManager();

        GWT.log("Application: everyone plugged, firing Init event", null);

        injector.getEventBus().fireEvent(AppEvents.Init);

    }



}
