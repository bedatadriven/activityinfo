/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.io.Serializable;

public class SyncRegion implements Serializable{
    private String id;
    private String currentVersion;
    
    public SyncRegion() {
    }

    public SyncRegion(String id) {
        this.id = id;
    }
    
    public SyncRegion(String id, String currentVersion) {
		super();
		this.id = id;
		this.currentVersion = currentVersion;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
    
    
}
