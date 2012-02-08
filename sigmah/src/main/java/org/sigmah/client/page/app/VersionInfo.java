/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.app;

import com.google.gwt.i18n.client.Dictionary;

public final class VersionInfo {

	private VersionInfo() { }
	
    public static String getRevision() {
        Dictionary versionInfo = Dictionary.getDictionary("VersionInfo");
        return versionInfo.get("revision");
    }
}
