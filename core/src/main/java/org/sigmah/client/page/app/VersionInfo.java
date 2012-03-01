/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.app;

import com.google.gwt.i18n.client.Dictionary;

public final class VersionInfo {

	private static final Dictionary DICTIONARY = Dictionary.getDictionary("VersionInfo");

	private VersionInfo() { }
	
	/**
	 * 
	 * @return the display name of the loaded version (e.g. "2.5.6")
	 */
    public static String getDisplayName() {
        return DICTIONARY.get("display");
    }
    
    /**
     * 
     * @return the git commit id of the loaded version
     */
    public static String getCommitId() {
        return DICTIONARY.get("commitId");
    }
}
