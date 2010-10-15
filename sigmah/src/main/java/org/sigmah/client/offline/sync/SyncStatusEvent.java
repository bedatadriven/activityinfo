/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class SyncStatusEvent extends BaseEvent {

    public static final EventType TYPE = new EventType();

    private String task;
    private double percentComplete;

    public SyncStatusEvent(String task, double percentComplete) {
        super(TYPE);
        this.task = task;
        this.percentComplete = percentComplete;
    }

    public String getTask() {
        return task;
    }

    public double getPercentComplete() {
        return percentComplete;
    }
}
