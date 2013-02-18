/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.i18n;

import org.activityinfo.client.i18n.UIConstants;

import com.google.gwt.core.client.GWT;
import com.teklabs.gwt.i18n.client.LocaleFactory;

/**
 * Contains global instances of UIConstants and UIMessages
 */
public final class I18N {

    private I18N() {}

    public static final UIConstants CONSTANTS;
    public static final UIMessages MESSAGES;
    public static final FromEntities FROM_ENTITIES;
    
    static {
    	if (GWT.isClient()) {
    		CONSTANTS = GWT.create(UIConstants.class);
    		MESSAGES = GWT.create(UIMessages.class);
    	} else {
    		// on the server side: LocaleProxy is initialized in LocaleModule
    		// locale is set for each request in CommandServlet
    		CONSTANTS = LocaleFactory.get(UIConstants.class);
    		MESSAGES = LocaleFactory.get(UIMessages.class);
    	}
    	FROM_ENTITIES = new FromEntities();
    }
}
