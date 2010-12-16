package org.sigmah.shared.domain.calendar;

import java.io.Serializable;

public class ReminderCalendarIdentifier implements Serializable {

    private static final long serialVersionUID = -3131879006325032077L;

    private int reminderListId;
    private String calendarName;
    private String completedEventString;
    private String expectedDateString;
    private String dateFormat;

    public ReminderCalendarIdentifier() {
        // Serialization
    }

    public ReminderCalendarIdentifier(int reminderListId, String calendarName, String completedEventString,
            String expectedDateString, String dateFormat) {
        this.reminderListId = reminderListId;
        this.calendarName = calendarName;
        this.completedEventString = completedEventString;
        this.expectedDateString = expectedDateString;
        this.dateFormat = dateFormat;
    }

    public int getReminderListId() {
        return reminderListId;
    }

    public void setReminderListId(int reminderListId) {
        this.reminderListId = reminderListId;
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
        result = prime * result + reminderListId;
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
        ReminderCalendarIdentifier other = (ReminderCalendarIdentifier) obj;
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
        if (reminderListId != other.reminderListId)
            return false;
        return true;
    }

}
