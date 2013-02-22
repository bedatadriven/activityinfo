

package org.activityinfo.client.i18n;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
