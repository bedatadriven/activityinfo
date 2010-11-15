package org.sigmah.client.util;

import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;

/**
 * Utility class to show non-modal notifications.
 * 
 * @author tmi
 * 
 */
public final class Notification {

    /**
     * Provides only static methods.
     */
    private Notification() {
    }

    /**
     * Message config.
     */
    private static final InfoConfig info = new InfoConfig(null, null);

    /**
     * Show a default notification for this application.
     * 
     * @param title
     *            The title.
     * @param message
     *            The content message.
     */
    public static void show(String title, String message) {
        info.title = title;
        info.text = message;
        info.display = 4000;
        info.width = 350;
        new Info().show(info);
    }
}
