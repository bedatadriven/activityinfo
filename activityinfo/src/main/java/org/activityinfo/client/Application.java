package org.activityinfo.client;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.maps.client.Maps;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.app.AppFrameSet;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.offline.ui.OfflineMenu;
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

        GWT.log("Application: everyone plugged, firing Init event", null);

        injector.getEventBus().fireEvent(AppEvents.Init);

        // preload Maps API
        MapApiLoader.preload();

    }

    protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }



}
