package org.sigmah.shared.domain.calendar;

import java.io.Serializable;

public class MonitoredPointCalendarIdentifier implements Serializable {

    private static final long serialVersionUID = -3131879006325032077L;

    private int monitoredListId;
    private String calendarName;
    private String completedEventString;
    private String expectedDateString;
    private String dateFormat;

    public MonitoredPointCalendarIdentifier() {
        // Serialization
    }

    public MonitoredPointCalendarIdentifier(int monitoredListId, String calendarName, String completedEventString,
            String expectedDateString, String dateFormat) {
        this.monitoredListId = monitoredListId;
        this.calendarName = calendarName;
        this.completedEventString = completedEventString;
        this.expectedDateString = expectedDateString;
        this.dateFormat = dateFormat;
    }

    public int getMonitoredListId() {
        return monitoredListId;
    }

    public void setMonitoredListId(int monitoredListId) {
        this.monitoredListId = monitoredListId;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getCompletedEventString() {
        return completedEventString;
    }

    public void setCompletedEventString(String completedEventString) {
        this.completedEventString = completedEventString;
    }

    public String getExpectedDateString() {
        return expectedDateString;
    }

    public void setExpectedDateString(String expectedDateString) {
        this.expectedDateString = expectedDateString;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calendarName == null) ? 0 : calendarName.hashCode());
        result = prime * result + ((completedEventString == null) ? 0 : completedEventString.hashCode());
        result = prime * result + ((dateFormat == null) ? 0 : dateFormat.hashCode());
        result = prime * result + ((expectedDateString == null) ? 0 : expectedDateString.hashCode());
        result = prime * result + monitoredListId;
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
        MonitoredPointCalendarIdentifier other = (MonitoredPointCalendarIdentifier) obj;
        if (calendarName == null) {
            if (other.calendarName != null)
                return false;
        } else if (!calendarName.equals(other.calendarName))
            return false;
        if (completedEventString == null) {
            if (other.completedEventString != null)
                return false;
        } else if (!completedEventString.equals(other.completedEventString))
            return false;
        if (dateFormat == null) {
            if (other.dateFormat != null)
                return false;
        } else if (!dateFormat.equals(other.dateFormat))
            return false;
        if (expectedDateString == null) {
            if (other.expectedDateString != null)
                return false;
        } else if (!expectedDateString.equals(other.expectedDateString))
            return false;
        if (monitoredListId != other.monitoredListId)
            return false;
        return true;
    }

}
