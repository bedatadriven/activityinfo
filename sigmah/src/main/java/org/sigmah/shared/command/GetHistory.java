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
     * The date before which the history is ignored. Set to <code>null</code> to
     * retrieves the complete history.
     */
    private Date maxDate;

    public GetHistory() {
        // Serialization
    }

    public GetHistory(long elementId) {
        this.elementId = elementId;
    }

    public GetHistory(long elementId, Date maxDate) {
        this.elementId = elementId;
        this.maxDate = maxDate;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (elementId ^ (elementId >>> 32));
        result = prime * result + ((maxDate == null) ? 0 : maxDate.hashCode());
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
        return true;
    }
}
