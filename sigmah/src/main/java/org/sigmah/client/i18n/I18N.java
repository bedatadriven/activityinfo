/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.i18n;

import com.google.gwt.core.client.GWT;
import com.teklabs.gwt.i18n.client.LocaleFactory;

/**
 * Contains global instances of UIConstants and UIMessages
 */
public class I18N {

    private I18N() {}

    public static final UIConstants CONSTANTS;
    public static final UIMessages MESSAGES;
    public static final FromEntities fromEntities;
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
    	fromEntities = new FromEntities();
    }
}
