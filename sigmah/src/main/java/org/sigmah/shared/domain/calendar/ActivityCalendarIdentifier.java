/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.calendar;

import java.io.Serializable;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ActivityCalendarIdentifier implements Serializable {
    private int projectId;
    private String calendarName;
    private String activityPrefix;

    public ActivityCalendarIdentifier() {}

    public ActivityCalendarIdentifier(int projectId, String calendarName, String activityPrefix) {
        this.projectId = projectId;
        this.calendarName = calendarName;
        this.activityPrefix = activityPrefix;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getActivityPrefix() {
        return activityPrefix;
    }

    public void setActivityPrefix(String activityPrefix) {
        this.activityPrefix = activityPrefix;
    }
}
