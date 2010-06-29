/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

public class SyncRegion {
    private String id;
    private boolean required;

    public SyncRegion() {
    }

    public SyncRegion(String id) {
        this.id = id;
    }

    public SyncRegion(String id, boolean required) {
        this.id = id;
        this.required = required;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
