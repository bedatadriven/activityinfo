package org.activityinfo.client;

import com.allen_sauer.gwt.log.client.Log;
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

        Log.info("Application: onModuleLoad starting");

        if(!GWT.isScript()) {
            Log.setCurrentLogLevel(Log.LOG_LEVEL_TRACE);
        }

//        if(Log.isTraceEnabled())
//            registerStatsHandler();
        if(Log.isErrorEnabled())
            GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
                @Override
                public void onUncaughtException(Throwable e) {
                    Log.error("Uncaught exception", e);
                }
            });

		GXT.setDefaultTheme(Theme.BLUE, true);

        Log.trace("Application: GXT theme set");

        AppInjector injector = GWT.create(AppInjector.class);


        injector.createWelcomeLoader();
        injector.createDataEntryLoader();
        injector.createChartLoader();
        injector.createReportLoader();
        injector.createPivotLoader();
        injector.createMapLoader();
        injector.createConfigLoader();

        injector.getUsageTracker();
        injector.getHistoryManager();
        injector.getDownloadManager();
        
        injector.createOfflineManager();

        createCaches(injector);

        Log.info("Application: everyone plugged, firing Init event");

        injector.getEventBus().fireEvent(AppEvents.Init);


        // preload Maps API
        //MapApiLoader.preload();

    }

    protected void createCaches(AppInjector injector) {
        injector.createSchemaCache();
        injector.createAdminCache();
    }


    /**
     * Registers an event handler for the GWT stats system
     *
     * Events have the form of:
     *
     * <code>
     *     $stats && $stats({
     *       moduleName:'__MODULE_NAME__',
     *       sessionId: $sessionId,
     *       subSystem:'startup',
     *       evtGroup: 'loadExternalRefs',
     *       millis:(new Date()).getTime(),
     *       type: 'begin'
     *     });
     */
    protected native void registerStatsHandler() /*-{
         $wnd.__appStatsEvent = function(evt) {
            @org.activityinfo.client.Application::logStatsEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(evt.subSystem, evt.evtGroup, evt[type]);
         };

    }-*/;

    protected static void logStatsEvent(String subSystem, String evtGroup, String type) {
        Log.trace(subSystem + ": " + evtGroup + " " + type);
    }

}
