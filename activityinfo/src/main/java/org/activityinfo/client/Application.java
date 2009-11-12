package org.activityinfo.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.map.MapApiLoader;
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

        Log.info("Application: onModuleLoad starting");

		GXT.setDefaultTheme(Theme.BLUE, true);

        AppInjector injector = GWT.create(AppInjector.class);

        injector.createWelcomeLoader();
        injector.createDataEntryLoader();
        injector.createChartLoader();
        injector.createReportLoader();
        injector.createPivotLoader();
        injector.createMapLoader();
        injector.createConfigLoader();

        injector.getHistoryManager();
        injector.getDownloadManager();

        createCaches(injector);

        Log.info("Application: everyone plugged, firing Init event");

        injector.getEventBus().fireEvent(AppEvents.Init);


        // preload Maps API
        MapApiLoader.preload();

    }

    protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }



}
