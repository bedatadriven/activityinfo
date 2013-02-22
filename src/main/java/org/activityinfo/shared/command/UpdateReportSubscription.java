package org.activityinfo.shared.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.report.model.EmailDelivery;

/**
 * Updates the relationship between a user and a report
 * 
 */
public class UpdateReportSubscription implements MutatingCommand<VoidResult> {
    private int reportId;
    private String userEmail;

    private Boolean pinnedToDashboard;
    private Integer emailDay;
    private EmailDelivery frequency;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getPinnedToDashboard() {
        return pinnedToDashboard;
    }

    public void setPinnedToDashboard(Boolean pinnedToDashboard) {
        this.pinnedToDashboard = pinnedToDashboard;
    }

    public Integer getEmailDay() {
        return emailDay;
    }

    public void setEmailDay(Integer day) {
        this.emailDay = day;
    }

    public EmailDelivery getEmailDelivery() {
        return frequency;
    }

    public void setEmailDelivery(EmailDelivery frequency) {
        this.frequency = frequency;
    }
}
