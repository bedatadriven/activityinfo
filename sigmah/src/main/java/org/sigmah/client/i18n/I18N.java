/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.i18n;

import com.google.gwt.core.client.GWT;

/**
 * Contains global instances of UIConstants and UIMessages
 */
public class I18N {

    private I18N() {}

    public static final UIConstants CONSTANTS = (UIConstants) GWT.create(UIConstants.class);
    public static final UIMessages MESSAGES = (UIMessages)GWT.create(UIMessages.class);
}
