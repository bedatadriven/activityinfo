package org.sigmah.shared.command;

import java.util.Date;

import org.sigmah.shared.command.result.HistoryResult;

/**
 * Command to retrieves all the history tokens of an element.
 * 
 * @author tmi
 * 
 */
public class GetHistory implements Command<HistoryResult> {

    private static final long serialVersionUID = 7467635662274038700L;

    /**
     * The element id.
     */
    private long elementId;

    /**
     * The project id.
     */
    private int projectId;

    /**
     * The date before which the history is ignored. Set to <code>null</code> to
     * retrieves the complete history.
     */
    private Date maxDate;

    public GetHistory() {
        // Serialization
    }

    public GetHistory(long elementId, int projectId) {
        this.elementId = elementId;
        this.projectId = projectId;
    }

    public GetHistory(long elementId, int projectId, Date maxDate) {
        this.elementId = elementId;
        this.maxDate = maxDate;
        this.projectId = projectId;
    }

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (elementId ^ (elementId >>> 32));
        result = prime * result + ((maxDate == null) ? 0 : maxDate.hashCode());
        result = prime * result + (int) (projectId ^ (projectId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetHistory other = (GetHistory) obj;
        if (elementId != other.elementId)
            return false;
        if (maxDate == null) {
            if (other.maxDate != null)
                return false;
        } else if (!maxDate.equals(other.maxDate))
            return false;
        if (projectId != other.projectId)
            return false;
        return true;
    }
}
